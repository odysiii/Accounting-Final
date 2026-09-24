package UI;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import Backend.JournalEntry;
import Backend.Ledger;

public class SavingJournals extends ModalCard {

    private static String fileName;

   public SavingJournals(ArrayList<JournalEntry> Entries, Journalizing journalizing) {

        super(SwingUtilities.getWindowAncestor(journalizing), "Saving Journal", 470, 310, 56);

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(30, 34, 28, 34));

        JLabel title = new JLabel("Save Journal");
        title.setFont(Theme.interWeight("ExtraBold", 24f));
        title.setForeground(Theme.INK);

        JLabel subtitle = new JLabel("Enter a file name to store this journal");
        subtitle.setFont(Theme.inter(Font.BOLD, 12f));
        subtitle.setForeground(Theme.HINT);

        JPanel titleText = new JPanel();
        titleText.setOpaque(false);
        titleText.setLayout(new BoxLayout(titleText, BoxLayout.Y_AXIS));
        titleText.add(title);
        titleText.add(Box.createVerticalStrut(4));
        titleText.add(subtitle);

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        titleRow.add(titleText, BorderLayout.CENTER);
        JPanel closeHolder = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        closeHolder.setOpaque(false);
        closeHolder.add(closeButton());
        titleRow.add(closeHolder, BorderLayout.EAST);

        JLabel inputFN = new JLabel("Input File Name");
        inputFN.setFont(Theme.inter(Font.BOLD, 12f));
        inputFN.setForeground(Theme.INK);
        inputFN.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedTextField fileNameField = new RoundedTextField();
        fileNameField.setBackgroundColor(Theme.FIELD_BLUSH);
        fileNameField.setCornerRadius(44);
        fileNameField.setPlaceholder("e.g., Bianca Corp.");
        fileNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        fileNameField.setPreferredSize(new Dimension(0, 50));
        fileNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        //Save Button
        RoundedButton saveButton = new RoundedButton();
        saveButton.setText("Save");
        saveButton.setCornerRadius(60);
        saveButton.setFont(Theme.inter(Font.BOLD, 13f));
        Theme.stylePrimary(saveButton);
        saveButton.addActionListener(e -> {
            if (fileNameField.getText().trim().isEmpty()) {
                fileNameField.setError(true);
                return;
            }
            fileName = fileNameField.getText().trim();
            Ledger.makeLedgers(Entries, Ledger.getUnAdjustedAccounts());
            Ledger.writeCsvFile(fileName);
            Ledger.writeFormattedFile(fileName);
            this.dispose();
            AppFrame.get().markSaved();
            AppFrame.get().showTab(AppFrame.Tab.LEDGERS);
        });

        //Cancel Button
        RoundedButton cancelButton = new RoundedButton();
        cancelButton.setText("Cancel");
        cancelButton.setCornerRadius(60);
        cancelButton.setFont(Theme.inter(Font.BOLD, 13f));
        Theme.styleSecondary(cancelButton);
        cancelButton.addActionListener(e -> {
            Ledger.makeLedgers(Entries, Ledger.getUnAdjustedAccounts());
            this.dispose();
            AppFrame.get().showTab(AppFrame.Tab.LEDGERS);
        });

        JPanel buttons = new JPanel(new GridLayout(1, 2, 14, 0));
        buttons.setOpaque(false);
        buttons.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttons.setPreferredSize(new Dimension(0, 50));
        buttons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        buttons.add(saveButton);
        buttons.add(cancelButton);

        card.add(titleRow);
        card.add(Box.createVerticalStrut(24));
        card.add(inputFN);
        card.add(Box.createVerticalStrut(8));
        card.add(fileNameField);
        card.add(Box.createVerticalGlue());
        card.add(buttons);

        getRootPane().setDefaultButton(saveButton);
    }

    public static String getFileName() {
      return fileName;
    }

    public static void reset() {
      fileName = null;
    }
}
