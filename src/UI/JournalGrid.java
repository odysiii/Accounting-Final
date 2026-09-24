package UI;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class JournalGrid extends JTable {

    private static final int DATE = 0;
    private static final int PARTICULARS = 1;
    private static final int DEBIT = 2;
    private static final int CREDIT = 3;

    private final TableModelListener scrollToEnd = e -> SwingUtilities.invokeLater(() -> {
        int last = getRowCount() - 1;
        if (last >= 0) {
            scrollRectToVisible(getCellRect(last, 0, true));
        }
    });

    public JournalGrid(DefaultTableModel model) {
        super(model);

        setRowHeight(78);
        setShowGrid(false);
        setShowHorizontalLines(true);
        setGridColor(new Color(0xE3E3E3));
        setIntercellSpacing(new Dimension(0, 1));
        setRowSelectionAllowed(false);
        setColumnSelectionAllowed(false);
        setCellSelectionEnabled(false);
        setFocusable(false);
        setOpaque(false);
        setFillsViewportHeight(true);
        setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);

        int[] widths = { 110, 460, 200, 200 };
        for (int i = 0; i < widths.length; i++) {
            getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
            getColumnModel().getColumn(i).setCellRenderer(new Cell(i));
            getColumnModel().getColumn(i).setHeaderRenderer(new Head(i));
        }

        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setResizingAllowed(false);
        getTableHeader().setPreferredSize(new Dimension(0, 54));
    }

    public RoundedPanel toCard() {
        RoundedPanel card = new RoundedPanel();
        card.setBackground(Color.WHITE);
        card.setCornerRadius(40);
        card.setShadowSize(14);
        card.setAccent(Theme.BLUE, 14);
        card.setClipChildren(true);
        card.setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(this);
        Theme.slim(scrollPane);
        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        getModel().addTableModelListener(scrollToEnd);
    }

    @Override
    public void removeNotify() {
        getModel().removeTableModelListener(scrollToEnd);
        super.removeNotify();
    }

    // Debit and credit columns are tinted all the way down, even below the last row.
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int x = 0;
        for (int c = 0; c < getColumnCount(); c++) {
            int width = getColumnModel().getColumn(c).getWidth();
            g2.setColor(c == DEBIT ? Theme.DEBIT_BG : c == CREDIT ? Theme.CREDIT_BG : Color.WHITE);
            g2.fillRect(x, 0, width, getHeight());
            x += width;
        }
        g2.dispose();
        super.paintComponent(g);
    }

    private static class Head extends DefaultTableCellRenderer {

        private final int column;

        Head(int column) {
            this.column = column;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {

            JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value == null ? "" : value.toString().toUpperCase(),
                    false, false, row, col);
            lbl.setOpaque(true);
            lbl.setBackground(column == DEBIT ? Theme.DEBIT_HEAD : column == CREDIT ? Theme.CREDIT_HEAD : Theme.HEAD_GRAY);
            lbl.setForeground(Theme.INK);
            lbl.setFont(Theme.inter(Font.BOLD, 12f));
            lbl.setBorder(BorderFactory.createEmptyBorder(0, 26, 0, 26));
            lbl.setHorizontalAlignment(column == DATE || column == PARTICULARS ? JLabel.LEFT : JLabel.CENTER);
            return lbl;
        }
    }

    private static class Cell extends JComponent implements TableCellRenderer {

        private final int column;
        private String text = "";

        Cell(int column) {
            this.column = column;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
            text = value == null ? "" : value.toString();
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Theme.aa(g);
            int first = 27;
            int second = 47;
            int third = 68;

            if (column == DATE) {
                g2.setFont(Theme.mono(Font.BOLD, 13f));
                g2.setColor(Theme.INK);
                g2.drawString(text.trim(), 26, first);
            } else if (column == PARTICULARS) {
                String[] lines = text.split("\n", 3);
                g2.setFont(Theme.interWeight("SemiBold", 13f));
                g2.setColor(Theme.INK);
                g2.drawString(lines[0].trim(), 26, first);
                if (lines.length > 1) {
                    g2.drawString(lines[1].trim(), 38, second);
                }
                if (lines.length > 2) {
                    g2.setFont(Theme.mono(Font.ITALIC, 11f));
                    g2.setColor(new Color(0x3C3C3C));
                    g2.drawString(lines[2].trim(), 60, third);
                }
            } else {
                String amount = Theme.money(text);
                g2.setFont(Theme.mono(Font.BOLD, 13f));
                g2.setColor(Theme.INK);
                int x = (getWidth() - g2.getFontMetrics().stringWidth(amount)) / 2;
                g2.drawString(amount, x, column == DEBIT ? first : second);
            }
            g2.dispose();
        }
    }
}
