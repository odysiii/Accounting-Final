package UI;

import java.awt.*;
import java.io.File;

import javax.swing.*;

public class CompanyName extends Frame {

    
    private static Font poppins = loadFont(GetPath.getPath() + "Poppins-Medium.ttf", 15f);
    private static String companyName;
    private static String yearEndDate;
    private static String asOfDate;

    public CompanyName() {

    super("Company Name");
        
    
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());
    this.setSize(350, 420);
    this.setResizable(false);
    this.setLocationRelativeTo(null);

    //Blue
    JPanel TitlePanel = new JPanel();
    TitlePanel.setBackground(new Color(0, 37, 204));
    TitlePanel.setPreferredSize(new Dimension(700, 55)); 
    TitlePanel.setLayout(new BorderLayout());

    //Sky Blue
    JPanel ContentPanel = new JPanel();
    ContentPanel.setBackground(new Color(125, 216, 255));
    ContentPanel.setPreferredSize(new Dimension(700, 250));
    ContentPanel.setLayout(null);

    this.add(TitlePanel, BorderLayout.NORTH);
    this.add(ContentPanel); 

    JLabel inputCN = new JLabel();
    inputCN.setText("Input Company Name: ");
    inputCN.setFont(poppins);
    inputCN.setForeground(Color.BLACK);
    inputCN.setHorizontalAlignment(SwingConstants.LEFT);
    inputCN.setBounds(43, 35, 200, 30);
    ContentPanel.add(inputCN);
                     
    //TextBox
    RoundedTextField companyNTF = new RoundedTextField();
    companyNTF.setCornerRadius(15);
    companyNTF.setBounds(42, 65, 242, 40); // <-- adjusted to fit frame
    ContentPanel.add(companyNTF);

    JLabel inputAsOf = new JLabel();
    inputAsOf.setText("Input As of Date: ");
    inputAsOf.setFont(poppins);
    inputAsOf.setForeground(Color.BLACK);
    inputAsOf.setHorizontalAlignment(SwingConstants.LEFT);
    inputAsOf.setBounds(43, 110, 200, 30);
    ContentPanel.add(inputAsOf);

    RoundedTextField aoTF = new RoundedTextField();
    aoTF.setCornerRadius(15);
    aoTF.setBounds(42, 136, 239, 40); // <-- adjusted to fit frame
    ContentPanel.add(aoTF);

    JLabel inputFor = new JLabel();
    inputFor.setText("Input For the Year ended Date: ");
    inputFor.setFont(poppins);
    inputFor.setForeground(Color.BLACK);
    inputFor.setHorizontalAlignment(SwingConstants.LEFT);
    inputFor.setBounds(43, 174, 300, 30);
    ContentPanel.add(inputFor);

    RoundedTextField fty = new RoundedTextField();
    fty.setCornerRadius(15);
    fty.setBounds(42, 200, 239, 40); // <-- adjusted to fit frame
    ContentPanel.add(fty);


    JPanel ButtonsPanel = new JPanel();
    ButtonsPanel.setBackground(new Color(125, 216, 255));
    ButtonsPanel.setPreferredSize(new Dimension(700, 65));
    this.add(ButtonsPanel, BorderLayout.SOUTH);

    //Save Button
    RoundedButton confirm = new RoundedButton();
    confirm.setText("Confirm");
    confirm.setCornerRadius(30);
    confirm.setPreferredSize(new Dimension(150, 40)); 
    confirm.setFont(new Font("Arial", Font.BOLD, 14)); 
    confirm.setFocusable(false); 
    confirm.setForeground(Color.WHITE);
    ButtonsPanel.add(confirm);
    confirm.addActionListener(e -> {
        companyName = companyNTF.getText();
        yearEndDate = fty.getText();
        asOfDate = aoTF.getText();
        new Journalizing(null);
    });
    this.setVisible(true);
    }

    public static String getCompanyName() {
      return companyName;
    }
    public static String getAsOfDate() {
      return asOfDate;
    }
    public static String getYearEndDate() {
      return yearEndDate;
    }

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
}