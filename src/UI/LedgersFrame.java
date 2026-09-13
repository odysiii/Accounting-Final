package UI;

import javax.swing.*;
import Backend.AccountTitle;

import java.awt.*;
import java.io.File;
import java.util.HashMap;

public class LedgersFrame extends TabFrame{

    private static Font poppins = loadFont(GetPath.getPath() + "Poppins-Bold.ttf", 15f);
    private static Font poppins1 = loadFont(GetPath.getPath() + "Poppins-SemiBold.ttf", 12f);
    private static Font poppins2 = loadFont(GetPath.getPath() + "Poppins-Medium.ttf", 12f);
    

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

    public LedgersFrame(HashMap<String, AccountTitle> accounts, Journalizing journalizing){
        super("Ledgers", journalizing);
        for (JButton button : this.getAllButtons()) {
            button.addActionListener(e -> this.dispose());
        }
        this.removeActionListener(getLedgersTab());

        JPanel center = new JPanel();
        center.setLayout(new FlowLayout(FlowLayout.LEFT,30,30));
        center.setBorder(BorderFactory.createEmptyBorder(50,30,50,30));

        int numberofAccounts = accounts.size();
        int rows = (int) Math.ceil(numberofAccounts / 5.0);
        center.setPreferredSize(new Dimension(5 * 200 + 4 * 30, rows * 200 + (rows - 1) * 30));

        for (AccountTitle accountTitle : accounts.values()) {
            center.add(createTAccount(accountTitle));
        }

        JScrollPane centerScrollPane = new JScrollPane(center);
        centerScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        centerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(centerScrollPane, BorderLayout.CENTER);
    }

    public static JPanel createTAccount(AccountTitle accountTitle){
        JPanel tAccount = new JPanel(new BorderLayout());
        tAccount.setPreferredSize(new Dimension(150,150));
        tAccount.setMaximumSize(new Dimension(150,150));
        tAccount.setMinimumSize(new Dimension(150,150));
            JLabel title = new JLabel(accountTitle.getTitle(), SwingConstants.CENTER);
            title.setFont(poppins);
            title.setOpaque(true);
            title.setPreferredSize(new Dimension(150,25));
            title.setBackground(new Color(0x7db1ff));
            tAccount.add(title, BorderLayout.NORTH);
        
            JPanel body = new JPanel(new GridLayout(0,2));
                
               int maxIndex = Math.max(accountTitle.getDebitValues().size(), accountTitle.getCreditValues().size());

                for (int i = 0; i < maxIndex; i++) {
                    // Debit cell
                    if (i < accountTitle.getDebitValues().size()) {
                        body.add(makecell(String.valueOf(accountTitle.getDebitValues().get(i)), new Color(0xbfecac)));
                    } else {
                        body.add(makecell("", new Color(0xbfecac)));
                    }

                    // Credit cell
                    if (i < accountTitle.getCreditValues().size()) {
                        body.add(makecell(String.valueOf(accountTitle.getCreditValues().get(i)), new Color(0xff7d7d)));
                    } else {
                        body.add(makecell("", new Color(0xff7d7d)));
                    }
                }

            tAccount.add(body, BorderLayout.CENTER);

            JLabel endingBalance = new JLabel("EB: " + String.valueOf(accountTitle.computeEndingBal()));
            endingBalance.setOpaque(true);
            endingBalance.setFont(poppins1);
            endingBalance.setPreferredSize(new Dimension(150,25));
            endingBalance.setBackground(Color.YELLOW);

            if (accountTitle.getSide().equals("Debit")) {
                endingBalance.setHorizontalAlignment(SwingConstants.LEFT);
            }else{
                endingBalance.setHorizontalAlignment(SwingConstants.RIGHT);
            }
            tAccount.add(endingBalance, BorderLayout.SOUTH);
        return tAccount;
    }

    public static JLabel makecell(String value, Color color){
        JLabel cell = new JLabel(value, SwingConstants.CENTER);
        cell.setOpaque(true);
        cell.setBackground(color);
        cell.setFont(poppins2);
        cell.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        return cell;
    }

}
