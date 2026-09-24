package UI;

import java.awt.*;
import java.util.Map;

import javax.swing.*;

import Backend.AccountTitle;

// Shared look of the Unadjusted and Adjusted trial balance screens.
public class TrialBalanceView extends JPanel {

    private static final double SPLIT_ONE = 0.6;
    private static final double SPLIT_TWO = 0.8;

    private final Map<String, AccountTitle> accounts;
    private double totalDebit = 0;
    private double totalCredit = 0;
    // Width taken by the vertical scrollbar, so the header and total rows line up with the scrolled rows.
    private int scrollbarInset = 0;
    private final java.util.List<JPanel> fixedRows = new java.util.ArrayList<>();

    private final ScrollPanel tableContent = new ScrollPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            int one = (int) (getWidth() * SPLIT_ONE);
            int two = (int) (getWidth() * SPLIT_TWO);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, one, getHeight());
            g.setColor(Theme.DEBIT_BG);
            g.fillRect(one, 0, two - one, getHeight());
            g.setColor(Theme.CREDIT_BG);
            g.fillRect(two, 0, getWidth() - two, getHeight());
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            return new Dimension(size.width, Math.max(size.height, 130));
        }
    };

    public TrialBalanceView(String title, Map<String, AccountTitle> accounts, String totalLabel) {
        this.accounts = accounts;

        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(Theme.pagePad());

        tableContent.setLayout(new BoxLayout(tableContent, BoxLayout.Y_AXIS));
        addAccount("Current Asset");
        addAccount("Non Current Asset");
        addAccount("Current Liability");
        addAccount("Non Current Liability");
        addAccount("Equity");
        addAccount("Income");
        addAccount("Expense");
        tableContent.add(Box.createVerticalGlue());

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 16, 0));
        heading.add(headingLabel(CompanyName.getCompanyName(), Font.BOLD, 24f));
        heading.add(Box.createVerticalStrut(6));
        heading.add(headingLabel(title, Font.BOLD, 17f));
        heading.add(headingLabel("For the year ended: " + CompanyName.getYearEndDate(), Font.BOLD, 17f));

        top.add(heading);
        JPanel columnHead = row(56, cell("Particulars", Theme.inter(Font.PLAIN, 16f), new Color(0x8A8A8A), null, SwingConstants.LEFT, 50),
                cell("Debit", Theme.inter(Font.PLAIN, 16f), Theme.DEBIT_TEXT, null, SwingConstants.CENTER, 0),
                cell("Credit", Theme.inter(Font.PLAIN, 16f), Theme.CREDIT_TEXT, null, SwingConstants.CENTER, 0));
        columnHead.putClientProperty("bg", new Color[] { Theme.HEAD_GRAY_TB, Theme.DEBIT_HEAD, Theme.CREDIT_HEAD });
        fixedRows.add(columnHead);
        top.add(columnHead);

        JPanel balancePanel = row(62, cell(totalLabel, Theme.inter(Font.BOLD, 16f), Color.WHITE, null, SwingConstants.LEFT, 30),
                cell(Theme.money(totalDebit), Theme.mono(Font.BOLD, 17f), Color.WHITE, null, SwingConstants.CENTER, 0),
                cell(Theme.money(totalCredit), Theme.mono(Font.BOLD, 17f), Color.WHITE, null, SwingConstants.CENTER, 0));
        balancePanel.putClientProperty("bg", new Color[] { Theme.BLUE, Theme.BLUE, Theme.BLUE });
        fixedRows.add(balancePanel);

        JScrollPane scrollPane = new JScrollPane(tableContent);
        Theme.slim(scrollPane);
        scrollPane.setOpaque(true);
        scrollPane.setBackground(Theme.CREDIT_BG);

        RoundedPanel card = new RoundedPanel();
        card.setBackground(Color.WHITE);
        card.setCornerRadius(40);
        card.setShadowSize(14);
        card.setClipChildren(true);
        card.setLayout(new BorderLayout());
        card.add(top, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        card.add(balancePanel, BorderLayout.SOUTH);

        JScrollBar bar = scrollPane.getVerticalScrollBar();
        Runnable sync = () -> {
            int inset = bar.isVisible() ? bar.getWidth() : 0;
            if (inset != scrollbarInset) {
                scrollbarInset = inset;
                for (JPanel fixed : fixedRows) {
                    fixed.revalidate();
                    fixed.repaint();
                }
            }
        };
        java.awt.event.ComponentAdapter watcher = new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                sync.run();
            }

            @Override
            public void componentHidden(java.awt.event.ComponentEvent e) {
                sync.run();
            }

            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                sync.run();
            }
        };
        bar.addComponentListener(watcher);
        scrollPane.getViewport().addComponentListener(watcher);

        add(card, BorderLayout.CENTER);
    }

    private JLabel headingLabel(String text, int style, float size) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.inter(style, size));
        label.setForeground(Theme.INK);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    // Three cells on the same 60 / 20 / 20 split as the tinted columns behind them. Fixed rows (column head and
    // total) are painted from the "bg" client property and stop short of the scrollbar so they stay aligned.
    private JPanel row(int height, JComponent first, JComponent second, JComponent third) {
        JPanel row = new JPanel(null) {
            private int usable() {
                return getClientProperty("bg") == null ? getWidth() : getWidth() - scrollbarInset;
            }

            @Override
            public void doLayout() {
                int one = (int) (usable() * SPLIT_ONE);
                int two = (int) (usable() * SPLIT_TWO);
                getComponent(0).setBounds(0, 0, one, getHeight());
                getComponent(1).setBounds(one, 0, two - one, getHeight());
                getComponent(2).setBounds(two, 0, usable() - two, getHeight());
            }

            @Override
            protected void paintComponent(Graphics g) {
                Color[] bg = (Color[]) getClientProperty("bg");
                if (bg != null) {
                    int one = (int) (usable() * SPLIT_ONE);
                    int two = (int) (usable() * SPLIT_TWO);
                    g.setColor(bg[0]);
                    g.fillRect(0, 0, one, getHeight());
                    g.setColor(bg[1]);
                    g.fillRect(one, 0, two - one, getHeight());
                    g.setColor(bg[2]);
                    g.fillRect(two, 0, getWidth() - two, getHeight());
                }
            }
        };
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, height));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        row.add(first);
        row.add(second);
        row.add(third);
        return row;
    }

    private JLabel cell(String text, Font font, Color color, Color background, int align, int leftPad) {
        JLabel cell = new JLabel(text, align);
        cell.setFont(font);
        cell.setForeground(color);
        cell.setBorder(BorderFactory.createEmptyBorder(0, leftPad, 0, 0));
        if (background != null) {
            cell.setOpaque(true);
            cell.setBackground(background);
        }
        return cell;
    }

    private void addAccount(String element){
        for (AccountTitle accountTitle : accounts.values()) {
            if (accountTitle.getElement().equals(element)) {
                Font mono = Theme.mono(Font.BOLD, 14f);
                Font name = Theme.inter(Font.BOLD, 14f);
                if (accountTitle.getSide().equals("Debit")) {
                    tableContent.add(row(46, cell(accountTitle.getTitle(), name, Theme.INK, null, SwingConstants.LEFT, 56),
                            cell(Theme.money(accountTitle.computeEndingBal()), mono, Theme.INK, null, SwingConstants.CENTER, 0),
                            cell("", mono, Theme.INK, null, SwingConstants.CENTER, 0)));
                    totalDebit += accountTitle.computeEndingBal();
                }else{
                    tableContent.add(row(46, cell(accountTitle.getTitle(), name, Theme.INK, null, SwingConstants.LEFT, 56),
                            cell("", mono, Theme.INK, null, SwingConstants.CENTER, 0),
                            cell(Theme.money(accountTitle.computeEndingBal()), mono, Theme.INK, null, SwingConstants.CENTER, 0)));
                    totalCredit += accountTitle.computeEndingBal();
                }
            }
        }
    }
}
