package UI;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

public class CompanyName extends ModalCard {

    private static String companyName;
    private static String yearEndDate;
    private static String asOfDate;

    public CompanyName(Window owner, Runnable onConfirm) {

    super(owner, "Setup Journal Workspace", 470, 510, 56);

    DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    LocalDate today = LocalDate.now();

    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBorder(BorderFactory.createEmptyBorder(30, 34, 28, 34));

    JLabel title = new JLabel("Setup Journal Workspace");
    title.setFont(Theme.interWeight("ExtraBold", 24f));
    title.setForeground(Theme.INK);

    JLabel subtitle = new JLabel("Enter organization details to initialize your journal");
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

    RoundedTextField companyNTF = field("e.g., CLSU CBA", null);
    RoundedTextField aoTF = field(today.format(format), Icons.get(Icons.Type.CALENDAR, 18, new Color(0x6B6B6B)));
    RoundedTextField fty = field(LocalDate.of(today.getYear(), 12, 31).format(format), Icons.get(Icons.Type.CALENDAR, 18, new Color(0x6B6B6B)));

    RoundedButton confirm = new RoundedButton();
    confirm.setText("Confirm Workspace & Start");
    confirm.setIcon(Icons.get(Icons.Type.ARROW, 18, Color.WHITE));
    confirm.setHorizontalTextPosition(SwingConstants.LEFT);
    confirm.setCornerRadius(60);
    confirm.setFont(Theme.inter(Font.BOLD, 13f));
    Theme.stylePrimary(confirm);
    confirm.setAlignmentX(Component.LEFT_ALIGNMENT);
    confirm.setPreferredSize(new Dimension(0, 52));
    confirm.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
    confirm.addActionListener(e -> {
        RoundedTextField[] fields = { companyNTF, aoTF, fty };
        boolean complete = true;
        for (RoundedTextField field : fields) {
            boolean empty = field.getText().trim().isEmpty();
            field.setError(empty);
            complete &= !empty;
        }
        if (!complete) {
            return;
        }
        companyName = companyNTF.getText().trim();
        yearEndDate = fty.getText().trim();
        asOfDate = aoTF.getText().trim();
        dispose();
        onConfirm.run();
    });

    card.add(titleRow);
    card.add(Box.createVerticalStrut(22));
    card.add(label("Input Company Name", Icons.Type.BUILDING));
    card.add(Box.createVerticalStrut(8));
    card.add(companyNTF);
    card.add(Box.createVerticalStrut(18));
    card.add(label("Input As of Date", Icons.Type.CALENDAR));
    card.add(Box.createVerticalStrut(8));
    card.add(aoTF);
    card.add(Box.createVerticalStrut(18));
    card.add(label("Input For the Year ended Date", Icons.Type.CALENDAR));
    card.add(Box.createVerticalStrut(8));
    card.add(fty);
    card.add(Box.createVerticalStrut(26));
    card.add(confirm);

    getRootPane().setDefaultButton(confirm);
    }

    private JPanel label(String text, Icons.Type icon) {
        JLabel label = new JLabel(text, Icons.get(icon, 16, new Color(0x555555)), SwingConstants.LEFT);
        label.setIconTextGap(8);
        label.setFont(Theme.inter(Font.BOLD, 12f));
        label.setForeground(Theme.INK);

        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        row.add(label, BorderLayout.WEST);
        return row;
    }

    private RoundedTextField field(String hint, Icon icon) {
        RoundedTextField field = new RoundedTextField();
        field.setBackgroundColor(Theme.FIELD_BLUSH);
        field.setCornerRadius(44);
        field.setPlaceholder(hint);
        if (icon != null) {
            field.setTrailingIcon(icon);
        }
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setPreferredSize(new Dimension(0, 50));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        return field;
    }

    public static void setDetails(String company, String asOf, String yearEnd) {
        companyName = company;
        asOfDate = asOf;
        yearEndDate = yearEnd;
    }

    public static String getCompanyName() {
      return companyName;
    }
    public static String getAsOfDate() {
      return asOfDate;
    }
    public static String getYearEndDate() {
      return yearEndDate;
    }
}
