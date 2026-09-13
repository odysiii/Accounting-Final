package UI;

import java.awt.*;
import java.io.File;
import java.util.HashMap;

import javax.swing.*;

import Backend.AccountTitle;
import Backend.Ledger;

public class IncomeStatement extends TabFrame {

    private static Font poppins = loadFont(GetPath.getPath() + "Poppins-ExtraBold.ttf", 20f);
    private static Font poppins1 = loadFont(GetPath.getPath() + "Poppins-Bold.ttf", 15f);

    
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

    private static double netIncome;
    private static double netLoss = 0;
    public IncomeStatement(Journalizing journalizing){

        super("Income Statement", journalizing);
        for (JButton button : this.getAllButtons()) {
            button.addActionListener(e -> this.dispose());
        }
        this.removeActionListener(getFinancialStatementTab());

        HashMap<String, AccountTitle> accounts = Ledger.getAccounts();

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());

            //Financial Statement Tabs
            JPanel FSTab = new JPanel(new GridLayout());
            FSTab.setPreferredSize(new Dimension(0, 50));

                JButton balanceSheetTab = new JButton("Balance Sheet");
                balanceSheetTab.setFont(poppins);
                balanceSheetTab.setPreferredSize(new Dimension(700, 38)); 
                balanceSheetTab.setBackground(Color.LIGHT_GRAY);
                balanceSheetTab.setForeground(Color.GRAY);
                balanceSheetTab.setFocusable(false);
                balanceSheetTab.addActionListener(e -> {
                    new BalanceSheet(journalizing);
                    this.dispose();
                });

                JButton incomeStatementTab = new JButton("Income Statement");
                incomeStatementTab.setFont(poppins);
                incomeStatementTab.setBackground(new Color(0x2196F3));
                incomeStatementTab.setPreferredSize(new Dimension(700, 38)); 
                incomeStatementTab.setForeground(Color.BLACK);
                incomeStatementTab.setFocusable(false);
            

            FSTab.add(balanceSheetTab);
            FSTab.add(incomeStatementTab);

            //And2 yung yellow and gray panel
            JPanel contentPanel = new JPanel(); 
            contentPanel.setLayout(new BorderLayout());
            contentPanel.setPreferredSize(new Dimension(0, 790));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 70, 10, 70));

                //for header
                JPanel yellowPanel = new JPanel();
                yellowPanel.setBackground(new Color(255, 255, 102));
                yellowPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                yellowPanel.setBounds(65, 0, 1405, 95); 
                yellowPanel.setLayout(new BoxLayout(yellowPanel, BoxLayout.Y_AXIS));
                contentPanel.add(yellowPanel);
                yellowPanel.add(Box.createVerticalStrut(17)); //space i2 from top ng yellow panel to company name para gumitna
            contentPanel.add(yellowPanel, BorderLayout.NORTH);

                JLabel cn = new JLabel(CompanyName.getCompanyName());
                cn.setForeground(Color.BLACK);
                cn.setFont(poppins1);
                cn.setAlignmentX(Component.CENTER_ALIGNMENT); 
                yellowPanel.add(cn);
                yellowPanel.add(Box.createVerticalStrut(-3)); //cn to sofp

                JLabel sofp = new JLabel("STATEMENT OF PROFIT OR LOSS");
                sofp.setForeground(Color.BLACK);
                sofp.setFont(poppins1);
                sofp.setAlignmentX(Component.CENTER_ALIGNMENT);
                yellowPanel.add(sofp);
                yellowPanel.add(Box.createVerticalStrut(-3));

                JLabel date = new JLabel("For the Year ended " + CompanyName.getYearEndDate());
                date.setForeground(Color.BLACK);
                date.setFont(poppins1);
                date.setAlignmentX(Component.CENTER_ALIGNMENT);
                yellowPanel.add(date);
                
                //for mismong content ng balance sheet
                JPanel grayPanel = new JPanel();
                grayPanel.setBackground(new Color(0xF6F1E9));
                grayPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                grayPanel.setBounds(65, 95, 1405, 700); 
                grayPanel.setLayout(new BoxLayout(grayPanel, BoxLayout.Y_AXIS));
                grayPanel.add(Box.createVerticalStrut(30));
            contentPanel.add(grayPanel, BorderLayout.CENTER);

            //puro text na
            double sales = 0;
            for (AccountTitle accountTitle : accounts.values()) {
                System.out.println(accountTitle.getTitle());
                if (accountTitle.getElement().equals("Income")) {
                    if (accountTitle.getTitle().equals("Sales Returns and Allowances"))
                        continue;
                    grayPanel.add(makeAccTitle(accountTitle));
                    grayPanel.add(Box.createVerticalStrut(10));
                    sales += accountTitle.computeEndingBal();
                }
            }

            double totalCogs = 0;
            for (AccountTitle accountTitle : accounts.values()) {
                    if (accountTitle.getTitle().equals("Cost of Goods Sold")) {
                        totalCogs += accountTitle.computeEndingBal();
                    }
                }
            grayPanel.add(makeTotal("Less Cost of Goods Sold", totalCogs, 60));
            grayPanel.add(Box.createVerticalStrut(10));

            double SRA = 0;
            for (AccountTitle accountTitle : accounts.values()) {
                    if (accountTitle.getTitle().equals("Sales Returns and Allowances")) {
                        SRA += accountTitle.computeEndingBal();
                    }
                }
            grayPanel.add(makeTotal("Less Sales Returns and Allowances", SRA, 60));
            grayPanel.add(Box.createVerticalStrut(10));

            grayPanel.add(makeTotal("Less Sales Discounts", journalizing.getDiscount(), 60));
            grayPanel.add(Box.createVerticalStrut(10));

            double grossProfit = sales - totalCogs - SRA - journalizing.getDiscount();
            grayPanel.add(makeTotal("Gross Profit", grossProfit, 60));
            grayPanel.add(Box.createVerticalStrut(10));

            grayPanel.add(makeHeader("Less: Operating Expense", Font.PLAIN, 18, 60));
            grayPanel.add(Box.createVerticalStrut(10));

                grayPanel.add(makeTotal("Freight-out", journalizing.getFreightOut(), 80));
                grayPanel.add(Box.createVerticalStrut(10));

                grayPanel.add(makeTotal("Freight-In", journalizing.getFreightIn(), 80));
                grayPanel.add(Box.createVerticalStrut(10));

                double expense = 0;
                for (AccountTitle accountTitle : accounts.values()) {
                    if (accountTitle.getElement().equals("Expense")) {
                        grayPanel.add(makeAccTitle(accountTitle));
                        grayPanel.add(Box.createVerticalStrut(10));
                        expense += accountTitle.computeEndingBal();
                    }
                }
                netIncome = grossProfit - expense - journalizing.getFreightIn() - journalizing.getFreightOut();
                if (grossProfit < expense) {
                    netLoss = grossProfit - expense;
                }


            grayPanel.add(makeTotal("NET INCOME/ NET LOSS", netIncome, 60));

        JScrollPane scrollPane = new JScrollPane(contentPanel);

        content.add(FSTab, BorderLayout.NORTH);
        content.add(scrollPane, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        repaint();
        revalidate();
        setVisible(true);
    }

    public JPanel makeAccTitle(AccountTitle account){
        JPanel accTitle = new JPanel();
        accTitle.setLayout(new BoxLayout(accTitle, BoxLayout.X_AXIS));
        accTitle.setOpaque(false);

        JLabel title = new JLabel(account.getTitle());
            title.setForeground(Color.BLACK);
            title.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel amount = new JLabel(String.valueOf(account.computeEndingBal()));
            amount.setForeground(Color.BLACK);
            amount.setFont(new Font("Arial", Font.PLAIN, 18));

        accTitle.add(Box.createRigidArea(new Dimension(80,0)));
        accTitle.add(title);
        accTitle.add(Box.createHorizontalGlue());
        accTitle.add(amount);
        accTitle.add(Box.createRigidArea(new Dimension(300,0)));
        return accTitle;
    }

    public JPanel makeHeader(String text, int style, int size, int indent){
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setOpaque(false);
    
            JLabel header = new JLabel(text);
            header.setForeground(Color.BLACK);
            header.setFont(new Font("Arial", style, size));
            header.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.setBorder(BorderFactory.createEmptyBorder(0,indent,0,0));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, header.getPreferredSize().height));
        headerPanel.add(header);

        return headerPanel;
    }

    public JPanel makeTotal(String Acctitle, double total, int indent){
        JPanel accTitle = new JPanel();
        accTitle.setLayout(new BoxLayout(accTitle, BoxLayout.X_AXIS));
        accTitle.setOpaque(false);

        JLabel title = new JLabel(Acctitle);
            title.setForeground(Color.BLACK);
            title.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel amount = new JLabel(String.valueOf(total));
            amount.setForeground(Color.BLACK);
            amount.setFont(new Font("Arial", Font.PLAIN, 18));

        accTitle.add(Box.createRigidArea(new Dimension(indent,0)));
        accTitle.add(title);
        accTitle.add(Box.createHorizontalGlue());
        accTitle.add(amount);
        accTitle.add(Box.createRigidArea(new Dimension(300,0)));
        return accTitle;
    }

    public static double getNetIncome() {
      return netIncome;
    }
    
    public static double getNetLoss() {
      return netLoss;
    }
}
