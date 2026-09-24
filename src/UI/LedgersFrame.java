package UI;

import javax.swing.*;
import Backend.AccountTitle;

import java.awt.*;
import java.util.HashMap;

public class LedgersFrame extends JPanel{

    public LedgersFrame(HashMap<String, AccountTitle> accounts, Journalizing journalizing){

        setOpaque(false);
        setLayout(new BorderLayout());

        JPanel center = new ScrollPanel(new ScrollPanel.WrapLayout(FlowLayout.LEFT, 22, 22));
        center.setBorder(BorderFactory.createEmptyBorder(18, 26, 18, 26));

        for (AccountTitle accountTitle : accounts.values()) {
            center.add(createTAccount(accountTitle));
        }

        JScrollPane centerScrollPane = new JScrollPane(center);
        Theme.slim(centerScrollPane);
        centerScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        centerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        centerScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(centerScrollPane, BorderLayout.CENTER);
    }

    public static JPanel createTAccount(AccountTitle accountTitle){
        int rows = Math.max(2, Math.max(accountTitle.getDebitValues().size(), accountTitle.getCreditValues().size()));
        int shadow = 10;

        RoundedPanel tAccount = new RoundedPanel();
        tAccount.setLayout(new BorderLayout());
        tAccount.setBackground(Color.WHITE);
        tAccount.setCornerRadius(30);
        tAccount.setShadowSize(shadow);
        tAccount.setClipChildren(true);
        tAccount.setPreferredSize(new Dimension(250 + 2 * shadow, 40 + 16 + rows * 26 + 40 + 2 * shadow));

            JLabel title = new JLabel(accountTitle.getTitle());
            title.setFont(Theme.inter(Font.BOLD, 13f));
            title.setForeground(Color.WHITE);
            title.setOpaque(true);
            title.setBackground(Theme.BLUE);
            title.setPreferredSize(new Dimension(250, 40));
            title.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 12));
            tAccount.add(title, BorderLayout.NORTH);

            JPanel debitColumn = column(Theme.DEBIT_BG);
            for (Double debit : accountTitle.getDebitValues()) {
                debitColumn.add(makecell(Theme.money(debit)));
            }
            JPanel creditColumn = column(Theme.CREDIT_BG);
            for (Double credit : accountTitle.getCreditValues()) {
                creditColumn.add(makecell(Theme.money(credit)));
            }

            JPanel body = new JPanel(new GridLayout(1, 2));
            body.add(debitColumn);
            body.add(creditColumn);
            tAccount.add(body, BorderLayout.CENTER);

            JLabel eb = new JLabel("EB:");
            eb.setFont(Theme.inter(Font.BOLD, 12f));
            eb.setForeground(Color.WHITE);
            JLabel endingBalance = new JLabel(Theme.money(accountTitle.computeEndingBal()));
            endingBalance.setFont(Theme.mono(Font.BOLD, 12f));
            endingBalance.setForeground(Color.WHITE);

            JPanel footer = new JPanel(new BorderLayout(10, 0));
            footer.setBackground(Theme.BLUE);
            footer.setPreferredSize(new Dimension(250, 40));
            footer.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
            footer.add(eb, BorderLayout.WEST);
            if (accountTitle.getSide().equals("Debit")) {
                footer.add(endingBalance, BorderLayout.CENTER);
            }else{
                endingBalance.setHorizontalAlignment(SwingConstants.RIGHT);
                footer.add(endingBalance, BorderLayout.CENTER);
            }
            tAccount.add(footer, BorderLayout.SOUTH);
        return tAccount;
    }

    private static JPanel column(Color color){
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setBackground(color);
        column.setBorder(BorderFactory.createEmptyBorder(10, 0, 6, 0));
        return column;
    }

    public static JLabel makecell(String value){
        JLabel cell = new JLabel(value, SwingConstants.CENTER);
        cell.setFont(Theme.mono(Font.BOLD, 13f));
        cell.setForeground(Theme.INK);
        cell.setAlignmentX(Component.CENTER_ALIGNMENT);
        cell.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        cell.setPreferredSize(new Dimension(100, 26));
        return cell;
    }

}
