package UI;

import javax.swing.*;

import Backend.Ledger;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public class TabFrame extends JFrame {
  
  
    private static Font poppins = loadFont(GetPath.getPath() + "Poppins-Bold.ttf", 12f);
    

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

  private static String companyName;
  private static String yearEnded;
  private static String asOfDate;

  private RoundedButton journalTab = new RoundedButton();
  private RoundedButton ledgersTab = new RoundedButton();
  private RoundedButton unadjustedTBTab = new RoundedButton();
  private RoundedButton adjustmentsTab = new RoundedButton();
  private RoundedButton adjustedTBTab = new RoundedButton();
  private RoundedButton financialStatementTab = new RoundedButton();
  private UTB utbFrame;
  private static int doneAdjustment = 0;

  public TabFrame(String title, Journalizing journalizing){
    setLayout(new BorderLayout());
    setTitle(title);
    setExtendedState(MAXIMIZED_BOTH);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);

    JPanel header = new JPanel();
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        header.setLayout(new GridLayout(1,6,0,0));
        add(header, BorderLayout.NORTH);

          journalTab.setText("Journal");
          journalTab.setHorizontalAlignment(SwingConstants.CENTER);
          journalTab.setFocusable(false);
          journalTab.setFont(poppins);
          journalTab.setCornerRadius(10);
          journalTab.setFillOriginal(new Color(0xFFAF00));
          journalTab.setFillClick(Color.white);
          journalTab.setFillOver(new Color(0xFFEAA2));
          journalTab.setForeground(Color.black);
          

          ledgersTab.setText("Ledgers");
          ledgersTab.setHorizontalAlignment(SwingConstants.CENTER);
          ledgersTab.setFocusable(false);
          ledgersTab.setFont(poppins);
          ledgersTab.setCornerRadius(10);
          ledgersTab.setFillOriginal(new Color(0xFFC500));
          ledgersTab.setFillClick(Color.white);
          ledgersTab.setFillOver(new Color(0xFFEAA2));
          ledgersTab.setForeground(Color.BLACK);

          unadjustedTBTab.setText("Unadjusted Trial Balance");
          unadjustedTBTab.setHorizontalAlignment(SwingConstants.CENTER);
          unadjustedTBTab.setFocusable(false);
          unadjustedTBTab.setFont(poppins);
          unadjustedTBTab.setCornerRadius(10);
          unadjustedTBTab.setFillOriginal(new Color(0xFFD600));
          unadjustedTBTab.setFillClick(Color.white);
          unadjustedTBTab.setFillOver(new Color(0xFFEAA2));
          unadjustedTBTab.setForeground(Color.black);
          
          adjustmentsTab.setText("Adjustments");
          adjustmentsTab.setHorizontalAlignment(SwingConstants.CENTER);
          adjustmentsTab.setFocusable(false);
          adjustmentsTab.setFont(poppins);
          adjustmentsTab.setCornerRadius(10);
          adjustmentsTab.setFillOriginal(new Color(0xFCED00));
          adjustmentsTab.setFillClick(Color.white);
          adjustmentsTab.setFillOver(new Color(0xFFEAA2));
          adjustmentsTab.setForeground(Color.BLACK);

          adjustedTBTab.setText("Adjusted Trial Balance");
          adjustedTBTab.setHorizontalAlignment(SwingConstants.CENTER);
          adjustedTBTab.setFocusable(false);
          adjustedTBTab.setFont(poppins);
          adjustedTBTab.setCornerRadius(10);
          adjustedTBTab.setFillOriginal(new Color(0xF9F380));
          adjustedTBTab.setFillClick(Color.white);
          adjustedTBTab.setFillOver(new Color(0xFFEAA2));
          adjustedTBTab.setForeground(Color.black);
           
          financialStatementTab.setText("Financial Statements");
          financialStatementTab.setHorizontalAlignment(SwingConstants.CENTER);
          financialStatementTab.setFocusable(false);
          financialStatementTab.setFont(poppins);
          financialStatementTab.setCornerRadius(10);
          financialStatementTab.setFillOriginal(new Color(0xFFFCA1));
          financialStatementTab.setFillClick(Color.white);
          financialStatementTab.setFillOver(new Color(0xFFEAA2));
          financialStatementTab.setForeground(Color.black);

          this.getJournalTab().addActionListener(e -> {new Journal(journalizing);});
          this.getLedgersTab().addActionListener(e -> {
            if (doneAdjustment == 1) {
              new LedgersFrame(Ledger.getAccounts(), journalizing);
            }else{
              new LedgersFrame(Ledger.getUnAdjustedAccounts(), journalizing);
            }
          });
          
          this.getUnadjustedTBTab().addActionListener(e -> {
            if (utbFrame == null) {
              utbFrame = new UTB(journalizing);
            }else{
              utbFrame.setVisible(true);
            }
          });
          this.getAdjustmentsTab().addActionListener(e -> new Adjustment(journalizing));
          this.getAdjustedTBTab().addActionListener(e -> new ATB(journalizing));
          this.getFinancialStatementTab().addActionListener(e -> new IncomeStatement(journalizing));

        header.add(journalTab);
        header.add(ledgersTab);
        header.add(unadjustedTBTab);
        header.add(adjustmentsTab);
        header.add(adjustedTBTab);
        header.add(financialStatementTab);
  }

  public static void setCompanyName(String cn) {
    companyName = cn;
  }
  public static void setYearEnded(String ye) {
    yearEnded = ye;
  }
  public static void setAsOfDate(String aod) {
    asOfDate = aod;
  }

  public static String getCompanyName() {
    return companyName;
  }
  public static String getYearEnded() {
    return yearEnded;
  }
  public static String getAsOfDate() {
    return asOfDate;
  }
  

  public JButton getJournalTab() {
    return journalTab;
  }
  public JButton getLedgersTab() {
    return ledgersTab;
  }
  public JButton getUnadjustedTBTab() {
    return unadjustedTBTab;
  }
  public JButton getAdjustmentsTab() {
    return adjustmentsTab;
  }
  public JButton getAdjustedTBTab() {
    return adjustedTBTab;
  }
  public JButton getFinancialStatementTab() {
    return financialStatementTab;
  }
  public static void setDoneAdjustment(int done) {
    doneAdjustment = done;
  }

  public void removeActionListener(JButton button){
    for (ActionListener actionListener : button.getActionListeners()) {
      button.removeActionListener(actionListener);
    }
  }
  public JButton[] getAllButtons(){
    JButton[] buttons = new JButton[6];
    buttons[0] = getJournalTab();
    buttons[1] = getLedgersTab();
    buttons[2] = getUnadjustedTBTab();
    buttons[3] = getAdjustmentsTab();
    buttons[4] = getAdjustedTBTab();
    buttons[5] = getFinancialStatementTab();
    return buttons;
  }
}
