package UI;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

// The "Add Journal Entry" card shared by the Journalizing and Adjustment screens.
public class EntryForm extends RoundedPanel {

    private final RoundedTextField dateField = field(Theme.FIELD, 16, 36);
    private final RoundedTextField debitAccount = field(Color.WHITE, 10, 28);
    private final RoundedTextField debitAmount = field(Color.WHITE, 10, 28);
    private final RoundedTextField creditAccount = field(Color.WHITE, 10, 28);
    private final RoundedTextField creditAmount = field(Color.WHITE, 10, 28);
    private final RoundedTextField noteField = field(Theme.FIELD, 16, 36);
    private final RoundedButton addButton = new RoundedButton();
    private final RoundedButton finishButton = new RoundedButton();

    // On short windows the card switches to a compact density so it fits without scrolling.
    private final List<Group> groups = new ArrayList<>();
    private final List<Box.Filler> gaps = new ArrayList<>();
    private final List<int[]> gapHeights = new ArrayList<>();
    private boolean compact = false;
    private int roomyHeight;

    public EntryForm(String title, String subtitle, String finishText) {

        setBackground(Color.WHITE);
        setCornerRadius(40);
        setShadowSize(14);
        setClipChildren(true);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(410, 0));

        addButton.setText("Add Entry");
        addButton.setCornerRadius(16);
        addButton.setFont(Theme.inter(Font.BOLD, 14f));
        Theme.stylePrimary(addButton);
        sizeRow(addButton, 42);

        finishButton.setText(finishText);
        finishButton.setCornerRadius(16);
        finishButton.setFont(Theme.inter(Font.BOLD, 13f));
        Theme.styleSecondary(finishButton);
        sizeRow(finishButton, 36);

        JPanel body = new ScrollPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        body.add(titleRow(title, subtitle));
        body.add(gap(14, 8));
        body.add(label("Transaction Date", Theme.INK, 12f));
        body.add(gap(5, 3));
        body.add(dateField);
        body.add(gap(11, 7));
        body.add(group("Debit Account Details", "Debit", new Color(0xE4EFE8), Theme.DEBIT_TEXT, debitAccount, debitAmount));
        body.add(gap(11, 7));
        body.add(group("Credit Account Details", "Credit", new Color(0xF1E3E3), Theme.CREDIT_TEXT, creditAccount, creditAmount));
        body.add(gap(11, 7));
        body.add(label("Note", Theme.INK, 12f));
        body.add(gap(5, 3));
        body.add(noteField);
        body.add(gap(16, 10));
        body.add(addButton);
        body.add(gap(8, 6));
        body.add(finishButton);

        JScrollPane scrollPane = new JScrollPane(body);
        Theme.slim(scrollPane);
        add(scrollPane, BorderLayout.CENTER);

        roomyHeight = body.getPreferredSize().height;
        scrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                boolean tooShort = scrollPane.getHeight() < roomyHeight;
                if (tooShort != compact) {
                    setCompact(tooShort);
                }
            }
        });
    }

    private void setCompact(boolean value) {
        compact = value;
        for (int i = 0; i < gaps.size(); i++) {
            int h = gapHeights.get(i)[value ? 1 : 0];
            Dimension d = new Dimension(0, h);
            gaps.get(i).changeShape(d, d, new Dimension(Short.MAX_VALUE, h));
        }
        sizeField(dateField, value ? 30 : 36);
        sizeField(noteField, value ? 30 : 36);
        for (RoundedTextField wide : new RoundedTextField[] { dateField, noteField }) {
            int v = value ? 3 : 8;
            wide.setBorder(BorderFactory.createEmptyBorder(v, 16, v, 16));
        }
        sizeRow(addButton, value ? 38 : 42);
        sizeRow(finishButton, value ? 32 : 36);
        for (Group group : groups) {
            group.fill(value);
        }
        revalidate();
        repaint();
    }

    public RoundedButton getAddButton() {
        return addButton;
    }

    public RoundedButton getFinishButton() {
        return finishButton;
    }

    public JTextField getDateField() {
        return dateField;
    }

    public JTextField getNoteField() {
        return noteField;
    }

    // "Account:Amount" text the journal logic expects.
    public JTextField debitInput() {
        return new JTextField(debitAccount.getText().trim() + ":" + cleanAmount(debitAmount));
    }

    public JTextField creditInput() {
        return new JTextField(creditAccount.getText().trim() + ":" + cleanAmount(creditAmount));
    }

    // Returns a message describing what is missing, or null when the entry can be added.
    public String validateInput() {
        RoundedTextField[] required = { debitAccount, debitAmount, creditAccount, creditAmount };
        boolean missing = false;
        for (RoundedTextField field : required) {
            boolean empty = field.getText().trim().isEmpty();
            field.setError(empty);
            missing |= empty;
        }
        if (missing) {
            return "Please fill in the account and amount for both the debit and the credit.";
        }
        for (RoundedTextField amount : new RoundedTextField[] { debitAmount, creditAmount }) {
            try {
                Double.parseDouble(cleanAmount(amount));
            } catch (NumberFormatException e) {
                amount.setError(true);
                return "Amounts must be numbers.";
            }
        }
        return null;
    }

    public void clear() {
        for (RoundedTextField field : new RoundedTextField[] { dateField, debitAccount, debitAmount, creditAccount,
                creditAmount, noteField }) {
            field.setText("");
        }
    }

    private String cleanAmount(JTextField field) {
        return field.getText().trim().replace(",", "").replace(":", "");
    }

    private RoundedTextField field(Color background, int arc, int height) {
        RoundedTextField field = new RoundedTextField();
        field.setBackgroundColor(background);
        field.setCornerRadius(arc);
        field.setFont(Theme.inter(Font.PLAIN, height > 34 ? 14f : 13f));
        if (height <= 34) {
            // The default 8px top/bottom padding is taller than these small fields, so the text got clipped.
            field.setBorder(BorderFactory.createEmptyBorder(2, 12, 2, 12));
        }
        sizeField(field, height);
        return field;
    }

    private void sizeField(JComponent field, int height) {
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setPreferredSize(new Dimension(0, height));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
    }

    private void sizeRow(JComponent component, int height) {
        sizeField(component, height);
    }

    // Vertical space that shrinks in compact mode: {roomy, compact} heights.
    private Box.Filler gap(int roomy, int tight) {
        Dimension d = new Dimension(0, roomy);
        Box.Filler filler = new Box.Filler(d, d, new Dimension(Short.MAX_VALUE, roomy));
        gaps.add(filler);
        gapHeights.add(new int[] { roomy, tight });
        return filler;
    }

    private JLabel label(String text, Color color, float size) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.inter(Font.BOLD, size));
        label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel titleRow(String title, String subtitle) {
        RoundedPanel badge = new RoundedPanel();
        badge.setBackground(Theme.BLUE);
        badge.setCornerRadius(14);
        badge.setLayout(new GridBagLayout());
        badge.setPreferredSize(new Dimension(38, 38));
        badge.add(new JLabel(Icons.get(Icons.Type.PLUS, 22, Color.WHITE)));

        JPanel badgeHolder = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        badgeHolder.setOpaque(false);
        badgeHolder.add(badge);

        JLabel heading = new JLabel(title);
        heading.setFont(Theme.interWeight("ExtraBold", 18f));
        heading.setForeground(Theme.INK);
        JLabel sub = new JLabel(subtitle);
        sub.setFont(Theme.inter(Font.BOLD, 11f));
        sub.setForeground(Theme.HINT);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(heading);
        text.add(sub);

        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        row.add(badgeHolder, BorderLayout.WEST);
        row.add(text, BorderLayout.CENTER);
        return row;
    }

    private RoundedPanel group(String heading, String prefix, Color background, Color headingColor,
            RoundedTextField account, RoundedTextField amount) {
        Group group = new Group(heading, prefix, background, headingColor, account, amount);
        groups.add(group);
        group.fill(false);
        return group;
    }

    // Debit/credit box. Roomy: account and amount stacked. Compact: side by side.
    private class Group extends RoundedPanel {

        private final String heading;
        private final String prefix;
        private final Color headingColor;
        private final RoundedTextField account;
        private final RoundedTextField amount;

        Group(String heading, String prefix, Color background, Color headingColor, RoundedTextField account,
                RoundedTextField amount) {
            this.heading = heading;
            this.prefix = prefix;
            this.headingColor = headingColor;
            this.account = account;
            this.amount = amount;
            setBackground(background);
            setCornerRadius(22);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
        }

        void fill(boolean compact) {
            removeAll();
            sizeField(account, compact ? 26 : 28);
            sizeField(amount, compact ? 26 : 28);
            if (compact) {
                setBorder(BorderFactory.createEmptyBorder(8, 14, 9, 14));
                add(label(heading, headingColor, 11f));
                add(Box.createVerticalStrut(3));
                JPanel columns = new JPanel(new GridLayout(1, 2, 10, 0));
                columns.setOpaque(false);
                columns.setAlignmentX(Component.LEFT_ALIGNMENT);
                columns.add(column(prefix + " to : Account", account));
                columns.add(column(prefix + " to : Amount", amount));
                add(indented(columns));
            } else {
                setBorder(BorderFactory.createEmptyBorder(11, 14, 12, 14));
                add(label(heading, headingColor, 11f));
                add(Box.createVerticalStrut(5));
                add(indented(label(prefix + " to : Account", Theme.GRAY_TEXT, 11f)));
                add(Box.createVerticalStrut(2));
                add(indented(account));
                add(Box.createVerticalStrut(5));
                add(indented(label(prefix + " to : Amount", Theme.GRAY_TEXT, 11f)));
                add(Box.createVerticalStrut(2));
                add(indented(amount));
            }
        }

        private JPanel column(String text, RoundedTextField field) {
            JPanel column = new JPanel();
            column.setOpaque(false);
            column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
            column.add(label(text, Theme.GRAY_TEXT, 11f));
            column.add(Box.createVerticalStrut(2));
            column.add(field);
            return column;
        }
    }

    private JComponent indented(JComponent component) {
        JPanel holder = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
            }
        };
        holder.setOpaque(false);
        holder.setAlignmentX(Component.LEFT_ALIGNMENT);
        holder.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        holder.add(component, BorderLayout.CENTER);
        return holder;
    }
}
