package UI;

import java.awt.*;

import javax.swing.*;

public class COGSandFO extends ModalCard {

    private static double cogs;
    private static double freightOut;

    public COGSandFO(Component parent) {

        super(SwingUtilities.getWindowAncestor(parent), "Enter Cogs", 400, 330, 30);

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(26, 30, 28, 30));

        JLabel title = new JLabel("Input COGS");
        title.setFont(Theme.interWeight("ExtraBold", 22f));
        title.setForeground(Theme.INK);

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        titleRow.add(title, BorderLayout.CENTER);
        titleRow.add(closeButton(), BorderLayout.EAST);

        RoundedTextField COGSField = field();
        RoundedTextField FOField = field();

        RoundedButton confirm = new RoundedButton();
        confirm.setText("Confirm");
        confirm.setCornerRadius(16);
        confirm.setFont(Theme.inter(Font.BOLD, 14f));
        Theme.stylePrimary(confirm);
        confirm.setPreferredSize(new Dimension(250, 44));
        confirm.addActionListener(e -> {
            try {
                cogs = Double.valueOf(COGSField.getText().trim().replace(",", ""));
            } catch (NumberFormatException ex) {
                COGSField.setError(true);
                return;
            }
            String freight = FOField.getText().trim().replace(",", "");
            try {
                freightOut = freight.isEmpty() ? 0 : Double.valueOf(freight);
            } catch (NumberFormatException ex) {
                FOField.setError(true);
                return;
            }
            this.dispose();
        });

        card.add(titleRow);
        card.add(Box.createVerticalStrut(20));
        card.add(label("Input Cost of Goods Sold"));
        card.add(Box.createVerticalStrut(8));
        card.add(COGSField);
        card.add(Box.createVerticalStrut(18));
        card.add(label("Input Freight Out"));
        card.add(Box.createVerticalStrut(8));
        card.add(FOField);
        card.add(Box.createVerticalGlue());

        JPanel confirmRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        confirmRow.setOpaque(false);
        confirmRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        confirmRow.add(confirm);
        card.add(confirmRow);

        getRootPane().setDefaultButton(confirm);
    }

    private JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.inter(Font.BOLD, 13f));
        label.setForeground(Theme.INK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private RoundedTextField field() {
        RoundedTextField field = new RoundedTextField();
        field.setBackgroundColor(new Color(0xDDDDDD));
        field.setCornerRadius(16);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setPreferredSize(new Dimension(0, 46));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        return field;
    }

    public static double getCogs() {
      return cogs;
    }
    public static double getFreightOut() {
      return freightOut;
    }

    public static void reset() {
      cogs = 0;
      freightOut = 0;
    }

}
