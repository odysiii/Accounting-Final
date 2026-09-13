package UI;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.Border;

import Backend.AccountTitle;
import Backend.Ledger;

public class ATB extends TabFrame {

  private static Font poppins = loadFont(GetPath.getPath() + "Poppins-ExtraBold.ttf", 20f);
  private static Font poppins1 = loadFont(GetPath.getPath() + "Poppins-SemiBold.ttf", 15f);
  private double totalDebit = 0;
  private double totalCredit = 0;

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

  private Border border = BorderFactory.createLineBorder(Color.GRAY, 1);
  private final HashMap<String, AccountTitle> accounts = new HashMap<>(Ledger.getAccounts());
  private JPanel tableContent = new JPanel();

  public ATB(Journalizing journalizing) {
    super("Unadjusted Trial Balance", journalizing);
    for (JButton button : this.getAllButtons()) {
      button.addActionListener(e -> this.dispose());
    }
    this.removeActionListener(getAdjustedTBTab());

    JPanel contentPanel = new JPanel(new BorderLayout());

    JPanel header = new JPanel();
    header.setPreferredSize(new Dimension(0, 103));
    header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
    header.setBackground(new Color(255, 255, 102));
    header.setBorder(border);

    JLabel cn = new JLabel(CompanyName.getCompanyName());
    JLabel UTB = new JLabel("Adjusted Trial Balance");
    JLabel date = new JLabel("For the year ended: " + CompanyName.getYearEndDate());

    for (JLabel label : new JLabel[] { cn, UTB, date }) {
      label.setFont(poppins);
      label.setAlignmentX(Component.CENTER_ALIGNMENT);
      header.add(Box.createVerticalStrut(5));
      header.add(label);
    }

    
    tableContent.setLayout(new BoxLayout(tableContent, BoxLayout.Y_AXIS));
    addAccount("Current Asset");
    addAccount("Non Current Asset");
    addAccount("Current Liability");
    addAccount("Non Current Liability");
    addAccount("Equity");
    addAccount("Income");
    addAccount("Expense");

    JPanel columnHeader = makeRow("Particulars", "Debit", "Credit",
                             new Color(173, 216, 230),
                             new Color(144, 238, 144),
                             new Color(240, 128, 128),
                             true);

    JScrollPane scrollPane = new JScrollPane(tableContent);
    scrollPane.setColumnHeaderView(columnHeader);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);

    contentPanel.add(header, BorderLayout.NORTH);
    contentPanel.add(scrollPane, BorderLayout.CENTER);

    JPanel balancePanel = new JPanel(new GridLayout(1, 3));

    JPanel equalsPanel = new JPanel();
      JLabel equalsLabel = new JLabel("Debit = Credit");
      equalsLabel.setFont(poppins);
      equalsPanel.add(equalsLabel);
    balancePanel.add(equalsPanel);
    
      JPanel totalDebitPanel = new JPanel();
      totalDebitPanel.setBackground(new Color(144, 238, 144));
        JLabel totalDebitLabel = new JLabel(String.valueOf(totalDebit));
        totalDebitLabel.setFont(poppins);
      totalDebitPanel.add(totalDebitLabel);
    balancePanel.add(totalDebitPanel);

      JPanel totalCreditPanel = new JPanel();
        JLabel totalCreditLabel = new JLabel(String.valueOf(totalCredit));
        totalCreditPanel.setBackground(new Color(240, 128, 128));
        totalCreditLabel.setFont(poppins);
      totalCreditPanel.add(totalCreditLabel);
    balancePanel.add(totalCreditPanel);

    add(contentPanel, BorderLayout.CENTER);
    add(balancePanel, BorderLayout.SOUTH);
  }

  private JPanel makeRow(String title, String debit, String credit,
                         Color c1, Color c2, Color c3, boolean isHeader) {

    JPanel row = new JPanel(new GridLayout(1, 3));
    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
    row.setPreferredSize(new Dimension(900, 60));

    row.add(makeCell(title, c1 != null ? c1 : new Color(0xF5F5F0), isHeader));
    row.add(makeCell(debit, c2 != null ? c2 : new Color(0xF5F5F0), isHeader));
    row.add(makeCell(credit, c3 != null ? c3 : new Color(0xF5F5F0), isHeader));

    return row;
  }

  private JPanel makeCell(String text, Color bg, boolean bold) {
    JPanel cell = new JPanel(new GridBagLayout());
    cell.setBackground(bg);
    cell.setBorder(border);
    JLabel label = new JLabel(text);
    label.setFont(poppins1);
    cell.add(label);
    return cell;
  }

  private void addAccount(String element){
    for (AccountTitle accountTitle : accounts.values()) {
      if (accountTitle.getElement().equals(element)) {
        if (accountTitle.getSide().equals("Debit")) {
          tableContent.add(makeRow(accountTitle.getTitle(), String.valueOf(accountTitle.computeEndingBal()), " ", null, null, null, false));
          totalDebit += accountTitle.computeEndingBal();
        }else{
          tableContent.add(makeRow(accountTitle.getTitle(), " ", String.valueOf(accountTitle.computeEndingBal()), null, null, null, false));
          totalCredit += accountTitle.computeEndingBal();
        }
      }
    }
  }
}
