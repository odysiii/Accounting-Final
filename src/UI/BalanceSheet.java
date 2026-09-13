package UI;

import java.awt.*;
import java.io.File;
import java.util.HashMap;

import javax.swing.*;

import Backend.AccountTitle;
import Backend.Ledger;

public class BalanceSheet extends TabFrame {

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

    public BalanceSheet(Journalizing journalizing){

        super("Balance Sheet", journalizing);
        for (JButton button : this.getAllButtons()) {
            button.addActionListener(e -> this.dispose());
        }
        this.removeActionListener(getFinancialStatementTab());

        HashMap<String,  AccountTitle> accounts = Ledger.getAccounts();

        JPanel content = new JPanel();
    
        content.setLayout(new BorderLayout());

            //Financial Statement Tabs
            JPanel FSTab = new JPanel(new GridLayout(1,1));
            FSTab.setPreferredSize(new Dimension(0, 50));

                JButton balanceSheetTab = new JButton("Balance Sheet");
                balanceSheetTab.setFont(poppins);
                balanceSheetTab.setPreferredSize(new Dimension(650, 38)); 
                balanceSheetTab.setBackground(new Color(0x2196F3));
                balanceSheetTab.setForeground(Color.black);
                balanceSheetTab.setFocusable(false);

                JButton incomeStatementTab = new JButton("Income Statement");
                incomeStatementTab.setFont(poppins);
                incomeStatementTab.setBackground(Color.LIGHT_GRAY);
                incomeStatementTab.setPreferredSize(new Dimension(650, 38)); 
                incomeStatementTab.setForeground(Color.gray);
                incomeStatementTab.setFocusable(false);
                incomeStatementTab.addActionListener(e -> {
                    new IncomeStatement(journalizing);
                    this.dispose();
                    });

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
                yellowPanel.setBounds(65, 0, 1405, 125); 
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

                JLabel sofp = new JLabel("STATEMENT OF FINANCIAL POSITION");
                sofp.setForeground(Color.BLACK);
                sofp.setFont(poppins1);
                sofp.setAlignmentX(Component.CENTER_ALIGNMENT);
                yellowPanel.add(sofp);
                yellowPanel.add(Box.createVerticalStrut(-3));

                JLabel date = new JLabel("AS OF " + CompanyName.getAsOfDate());
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
                   
                //Assets
                double totalAssets = 0;
                grayPanel.add(makeHeader("ASSETS", Font.BOLD, 19, 60));
                grayPanel.add(Box.createVerticalStrut(15));
                    //Current Assets
                    grayPanel.add(makeHeader("Current", Font.PLAIN, 18, 80));
                    grayPanel.add(Box.createVerticalStrut(5));
                    for (AccountTitle accountTitle : accounts.values()) {       
                        if (accountTitle.getElement().equals("Current Asset")) {
                            grayPanel.add(makeAccTitle(accountTitle));
                            totalAssets += accountTitle.computeEndingBal();
                            grayPanel.add(Box.createVerticalStrut(5));
                        }
                    }
                    //Non Current Assets
                    grayPanel.add(makeHeader("Non Current", Font.PLAIN, 18, 80));
                    grayPanel.add(Box.createVerticalStrut(5));
                    for (AccountTitle accountTitle : accounts.values()) {       
                        if (accountTitle.getElement().equals("Non Current Asset")) {
                            grayPanel.add(makeAccTitle(accountTitle));
                            totalAssets += accountTitle.computeEndingBal();
                            grayPanel.add(Box.createVerticalStrut(5));
                        }
                    }
                    grayPanel.add(Box.createVerticalStrut(5));
                    grayPanel.add(makeTotal("TOTAL ASSETS", totalAssets, 80));
                    grayPanel.add(Box.createVerticalStrut(20));

                //LIABILITIES AND OWNER'S EQUITY
                grayPanel.add(makeHeader("LIABILITIES AND OWNER'S EQUITY", Font.BOLD, 19, 60));
                grayPanel.add(Box.createVerticalStrut(15));
                    //Liabilities
                    grayPanel.add(makeHeader("LIABILITIES", Font.PLAIN, 18, 80));
                    grayPanel.add(Box.createVerticalStrut(5));
                    //Current Liabilities
                    grayPanel.add(makeHeader("Current:", Font.PLAIN, 18, 80));
                    grayPanel.add(Box.createVerticalStrut(5));
                    for (AccountTitle accountTitle : accounts.values()) {       
                        if (accountTitle.getElement().equals("Current Liability")) {
                            grayPanel.add(makeAccTitle(accountTitle));
                            grayPanel.add(Box.createVerticalStrut(5));
                        }
                        
                    }
                    //Non Current Liabilities
                    grayPanel.add(makeHeader("Non Current:", Font.PLAIN, 18, 80));
                    grayPanel.add(Box.createVerticalStrut(5));
                    for (AccountTitle accountTitle : accounts.values()) {       
                        if (accountTitle.getElement().equals("Non Current Liability")) {
                            grayPanel.add(makeAccTitle(accountTitle));
                            grayPanel.add(Box.createVerticalStrut(5));
                        }
                    }
                    //Equity
                    grayPanel.add(Box.createVerticalStrut(10));
                    grayPanel.add(makeHeader("EQUITY", Font.PLAIN, 18, 80));
                    grayPanel.add(Box.createVerticalStrut(5));
                    double capital = 0;
                    for (AccountTitle accountTitle : accounts.values()) {       
                        if (accountTitle.getElement().equals("Equity")) {
                            if (accountTitle.getTitle().equals("Drawings"))
                                continue;
                            capital += accountTitle.computeEndingBal();
                        }
                    }
                    //initial inv
                    grayPanel.add(makeTotal("Initial investment", journalizing.getInitialCapital(), 100));
                    grayPanel.add(Box.createVerticalStrut(5));

                    //addtl inv
                    grayPanel.add(makeTotal("Add: Additional invesment", capital - journalizing.getInitialCapital(), 100));
                    grayPanel.add(Box.createVerticalStrut(5));

                                //net income
                                grayPanel.add(makeTotal("Net income", IncomeStatement.getNetIncome(), 150));
                                grayPanel.add(Box.createVerticalStrut(5));

                    //total
                    grayPanel.add(makeTotal("Total", capital, 100));
                    double withdrawal = 0;
                     for (AccountTitle accountTitle : accounts.values()) {       
                        if (accountTitle.getTitle().equals("Drawings")) {
                            withdrawal += accountTitle.computeEndingBal();
                        }
                    }
                    grayPanel.add(makeTotal("Less: Withdrawal", withdrawal, 100));
                    grayPanel.add(Box.createVerticalStrut(5));

                                //net loss
                                grayPanel.add(makeTotal("Net Loss", IncomeStatement.getNetLoss(), 150));
                                grayPanel.add(Box.createVerticalStrut(5));

                    grayPanel.add(makeTotal("Equity, end", capital - withdrawal,100));

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

        accTitle.add(Box.createRigidArea(new Dimension(100,0)));
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
}
