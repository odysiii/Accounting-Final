package UI;
import java.awt.*;

import javax.swing.*;

public class HomeFrame extends JPanel {

    public HomeFrame() {

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel subtitleOne = subtitle("Streamline journal entries, ledger postings, trial balances, and audit-ready");
        JLabel subtitleTwo = subtitle("financial reporting in one seamless, intelligent workspace.");

        int shadow = 9;

        RoundedButton createNewJournal = new RoundedButton();
        createNewJournal.setText("Create New Journal");
        createNewJournal.setIcon(Icons.get(Icons.Type.PLUS_CIRCLE, 24, Color.WHITE));
        createNewJournal.setFont(Theme.inter(Font.BOLD, 16f));
        createNewJournal.setCornerRadius(16);
        Theme.stylePrimary(createNewJournal);
        createNewJournal.setShadowSize(shadow);
        createNewJournal.setPreferredSize(new Dimension(266 + 2 * shadow, 60 + 2 * shadow));
        createNewJournal.addActionListener(e -> AppFrame.get().startNewJournal());

        RoundedButton viewSavedJournals = new RoundedButton();
        viewSavedJournals.setText("View Saved Journals");
        viewSavedJournals.setIcon(Icons.get(Icons.Type.LIST, 24, Theme.BLUE));
        viewSavedJournals.setFont(Theme.inter(Font.BOLD, 16f));
        viewSavedJournals.setCornerRadius(16);
        viewSavedJournals.setFillOriginal(Color.WHITE);
        viewSavedJournals.setFillOver(new Color(0xF1F5FD));
        viewSavedJournals.setFillClick(new Color(0xDDE7FA));
        viewSavedJournals.setForeground(Theme.BLUE);
        viewSavedJournals.setFocusable(false);
        viewSavedJournals.setBorderColor(new Color(0xD5D9E2));
        viewSavedJournals.setShadowSize(shadow);
        viewSavedJournals.setPreferredSize(new Dimension(266 + 2 * shadow, 60 + 2 * shadow));
        viewSavedJournals.addActionListener(e -> AppFrame.get().goSaved());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        buttons.setOpaque(false);
        buttons.add(createNewJournal);
        buttons.add(viewSavedJournals);

        center.add(new Hero());
        center.add(Box.createVerticalStrut(30));
        center.add(subtitleOne);
        center.add(Box.createVerticalStrut(6));
        center.add(subtitleTwo);
        center.add(Box.createVerticalStrut(52));
        center.add(buttons);

        add(center);
    }

    private JLabel subtitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.inter(Font.PLAIN, 20f));
        label.setForeground(new Color(0x8A858E));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    // "Master Your Financial Workflow with Accounting Cycle", the last two words in blue.
    private static class Hero extends JComponent {

        private static final Color FROM = new Color(0x2B86F5);
        private static final Color TO = new Color(0x0050D8);

        Hero() {
            setFont(Theme.interWeight("ExtraBold", 68f));
            setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(900, 228);
        }

        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Theme.aa(g);
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int lineHeight = 76;
            int baseline = fm.getAscent() + 4;

            drawCentered(g2, fm, baseline, "Master Your Financial", null);
            drawCentered(g2, fm, baseline + lineHeight, "Workflow with ", "Accounting");
            drawCentered(g2, fm, baseline + 2 * lineHeight, null, "Cycle");
            g2.dispose();
        }

        private void drawCentered(Graphics2D g2, FontMetrics fm, int y, String black, String blue) {
            int blackWidth = black == null ? 0 : fm.stringWidth(black);
            int blueWidth = blue == null ? 0 : fm.stringWidth(blue);
            int x = (getWidth() - blackWidth - blueWidth) / 2;
            if (black != null) {
                g2.setColor(Theme.INK);
                g2.drawString(black, x, y);
            }
            if (blue != null) {
                Theme.gradientText(g2, blue, x + blackWidth, y, FROM, TO);
            }
        }
    }
}
