package UI;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;

import Backend.JournalEntry;
import Backend.Ledger;

public class SavingJournals extends JFrame{

    private static Font lexend = loadFont(GetPath.getPath() + "Lexend-VariableFont_wght.ttf", 20f);
    private static Font poppins = loadFont(GetPath.getPath() + "Poppins-Bold.ttf", 25f);
    private static Font poppins1 = loadFont(GetPath.getPath() + "Poppins-Medium.ttf", 15f);
    

    public static Font loadFont(String path, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (Exception e) {
            e.printStackTrace();
            return null; 
        }
    }

    
    private static String fileName;

   @SuppressWarnings("unused")
    public SavingJournals(ArrayList<JournalEntry> Entries, Journalizing journalizing) {

        super("Saving Journal");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(650, 350);
        setResizable(false);
        setLocationRelativeTo(null);

        //Blue
        JPanel TitlePanel = new JPanel();
        TitlePanel.setBackground(new Color(0, 37, 204));
        TitlePanel.setPreferredSize(new Dimension(700, 48)); 
        TitlePanel.setLayout(new BorderLayout());

        //Sky Blue
        JPanel ContentPanel = new JPanel();
        ContentPanel.setBackground(new Color(125, 216, 255));
        ContentPanel.setPreferredSize(new Dimension(700, 250));
        ContentPanel.setLayout(null);

        add(TitlePanel, BorderLayout.NORTH);
        add(ContentPanel); 

        //Saving Journal in Files...
        JLabel TitleLabel = new JLabel();
        TitleLabel.setText("  SAVING JOURNAL IN FILES...");
        TitleLabel.setForeground(Color.WHITE);
        TitleLabel.setFont(poppins);
        TitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        TitlePanel.add(TitleLabel, BorderLayout.CENTER);

        //Input File Name:
        JLabel InputFN = new JLabel();
        InputFN.setText("  Input File Name: ");
        InputFN.setForeground(Color.BLACK);
        InputFN.setFont(lexend);
        InputFN.setHorizontalAlignment(SwingConstants.LEFT);
        InputFN.setBounds(36, 50, 200, 30);
        ContentPanel.add(InputFN);
        
        //TextBox
        RoundedTextField fileNameField = new RoundedTextField();
        fileNameField.setBounds(42, 82, 537, 50); 
        fileNameField.setCornerRadius(10);
        ContentPanel.add(fileNameField);

        //Buttons
        JPanel ButtonsPanel = new JPanel();
        ButtonsPanel.setBackground(new Color(125, 216, 255));
        ButtonsPanel.setPreferredSize(new Dimension(700, 100));
        add(ButtonsPanel, BorderLayout.SOUTH);

        //Save Button
        RoundedButton saveButton = new RoundedButton();
        saveButton.setText("Save");
        saveButton.setCornerRadius(25);
        saveButton.setForeground(Color.WHITE);
        saveButton.setPreferredSize(new Dimension(170, 40)); 
        saveButton.setFont(poppins1);
        saveButton.setFocusable(false); 
        saveButton.addActionListener(e -> {
            fileName = fileNameField.getText();
            Ledger.makeLedgers(Entries, Ledger.getUnAdjustedAccounts());
            new LedgersFrame(Ledger.getUnAdjustedAccounts(), journalizing);
            Ledger.writeCsvFile(fileName);
            Ledger.writeFormattedFile(fileName);
            this.dispose();
        });

        //Cancel Button
        RoundedButton cancelButton = new RoundedButton();
        cancelButton.setText("Cancel");
        cancelButton.setCornerRadius(25);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setPreferredSize(new Dimension(170, 40)); 
        cancelButton.setFont(poppins1);
        cancelButton.setFocusable(false); 
        cancelButton.addActionListener(e -> {
            Ledger.makeLedgers(Entries, Ledger.getUnAdjustedAccounts());
            new LedgersFrame(Ledger.getUnAdjustedAccounts(), journalizing);
            this.dispose();
        });

        ButtonsPanel.add(saveButton);
        ButtonsPanel.add(cancelButton);

    
        setVisible(true);
    }

    public static String getFileName() {
      return fileName;
    }
}