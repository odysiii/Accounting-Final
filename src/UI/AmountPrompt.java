package UI;

import java.awt.*;

import javax.swing.*;

// One-field amount pop-up (sales discount, freight in) in the same style as the other cards.
public class AmountPrompt extends ModalCard {

    private double amount = 0;

    public AmountPrompt(Component parent, String title, String label) {

        super(SwingUtilities.getWindowAncestor(parent), title, 400, 250, 30);

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(26, 30, 28, 30));

        JLabel heading = new JLabel(title);
        heading.setFont(Theme.interWeight("ExtraBold", 22f));
        heading.setForeground(Theme.INK);

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        titleRow.add(heading, BorderLayout.CENTER);
        titleRow.add(closeButton(), BorderLayout.EAST);

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(Theme.inter(Font.BOLD, 13f));
        fieldLabel.setForeground(Theme.INK);
        fieldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedTextField field = new RoundedTextField();
        field.setBackgroundColor(new Color(0xDDDDDD));
        field.setCornerRadius(16);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setPreferredSize(new Dimension(0, 46));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        RoundedButton confirm = new RoundedButton();
        confirm.setText("Confirm");
        confirm.setCornerRadius(16);
        confirm.setFont(Theme.inter(Font.BOLD, 14f));
        Theme.stylePrimary(confirm);
        confirm.setPreferredSize(new Dimension(250, 44));
        confirm.addActionListener(e -> {
            String text = field.getText().trim().replace(",", "");
            try {
                amount = text.isEmpty() ? 0 : Double.parseDouble(text);
            } catch (NumberFormatException ex) {
                field.setError(true);
                return;
            }
            dispose();
        });

        card.add(titleRow);
        card.add(Box.createVerticalStrut(20));
        card.add(fieldLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(field);
        card.add(Box.createVerticalGlue());

        JPanel confirmRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        confirmRow.setOpaque(false);
        confirmRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        confirmRow.add(confirm);
        card.add(confirmRow);

        getRootPane().setDefaultButton(confirm);
    }

    // Blank or closed without confirming counts as 0.
    public double getAmount() {
        return amount;
    }

    // Shows the prompt (blocks until it closes) and returns the amount entered.
    public static double ask(Component parent, String title, String label) {
        AmountPrompt prompt = new AmountPrompt(parent, title, label);
        prompt.setVisible(true);
        return prompt.getAmount();
    }
}
