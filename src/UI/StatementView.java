package UI;

import java.awt.*;

import javax.swing.*;

import Backend.AccountTitle;

// Heading + card used by the Income Statement and the Balance Sheet.
public class StatementView extends JPanel {

    protected final JPanel body = new ScrollPanel();
    private final Heading heading;
    private final RoundedPanel card = new RoundedPanel();

    public StatementView(String headingText, String title, String subtitle) {
        setOpaque(false);
        setLayout(null);

        heading = new Heading(headingText);

        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(20, 26, 22, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.inter(Font.BOLD, 15f));
        titleLabel.setForeground(Theme.INK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(Theme.inter(Font.PLAIN, 15f));
        subtitleLabel.setForeground(new Color(0x7A6F6F));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(BorderFactory.createEmptyBorder(22, 0, 16, 0));
        top.add(titleLabel);
        top.add(subtitleLabel);

        JPanel divider = new JPanel();
        divider.setBackground(Theme.LINE);
        divider.setPreferredSize(new Dimension(0, 1));

        JPanel head = new JPanel(new BorderLayout());
        head.setOpaque(false);
        head.add(top, BorderLayout.CENTER);
        head.add(divider, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(body);
        Theme.slim(scrollPane);

        card.setBackground(Color.WHITE);
        card.setCornerRadius(30);
        card.setShadowSize(8);
        card.setBorderColor(Theme.LINE);
        card.setClipChildren(true);
        card.setLayout(new BorderLayout());
        card.add(head, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        add(heading);
        add(card);
    }

    // Card is only as tall as its content, up to the space available.
    @Override
    public void doLayout() {
        int headingHeight = heading.getPreferredSize().height;
        heading.setBounds(4, 0, heading.getPreferredSize().width, headingHeight);
        int top = headingHeight + 10;
        card.setBounds(0, top, getWidth(), Math.min(card.getPreferredSize().height, Math.max(0, getHeight() - top)));
    }

    public JPanel makeAccTitle(AccountTitle account, int indent){
        return makeTotal(account.getTitle(), account.computeEndingBal(), indent);
    }

    public JPanel makeHeader(String text, int style, int size, int indent){
        JLabel header = new JLabel(text);
        header.setForeground(Theme.INK);
        header.setFont(Theme.mono(style == Font.BOLD ? Font.BOLD : Font.PLAIN, 13f));
        header.setBorder(BorderFactory.createEmptyBorder(0, indent / 2, 0, 0));
        return line(header, null);
    }

    public JPanel makeTotal(String Acctitle, double total, int indent){
        JLabel title = new JLabel(Acctitle);
        title.setForeground(Theme.INK);
        title.setFont(Theme.mono(Font.PLAIN, 13f));
        title.setBorder(BorderFactory.createEmptyBorder(0, indent / 2, 0, 0));

        JLabel amount = new JLabel(Theme.money(total));
        amount.setForeground(Theme.INK);
        amount.setFont(Theme.mono(Font.PLAIN, 13f));
        amount.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 44));
        return line(title, amount);
    }

    public JComponent makeGap(int height){
        return (JComponent) Box.createVerticalStrut(height);
    }

    private JPanel line(JLabel left, JLabel right){
        JPanel line = new JPanel(new BorderLayout());
        line.setOpaque(false);
        line.setAlignmentX(Component.LEFT_ALIGNMENT);
        line.setPreferredSize(new Dimension(0, 24));
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        line.add(left, BorderLayout.WEST);
        if (right != null) {
            line.add(right, BorderLayout.EAST);
        }
        return line;
    }

    private static class Heading extends JComponent {

        private final String text;

        Heading(String text) {
            this.text = text;
            setFont(Theme.interWeight("ExtraBold", 20f));
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension((int) Math.ceil(Theme.width(getFont(), text)) + 4, 34);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Theme.aa(g);
            g2.setFont(getFont());
            g2.setColor(Theme.INK);
            g2.drawString(text, 0, 23);
            g2.setColor(Theme.BLUE);
            g2.fillRoundRect(0, getHeight() - 5, getWidth(), 3, 3, 3);
            g2.dispose();
        }
    }
}
