package UI;

import java.awt.*;
import java.io.File;

import javax.swing.*;

public class COGSandFO extends JDialog{

    private static Font poppins = loadFont(GetPath.getPath() + "Poppins-Medium.ttf", 15f);

    private static double cogs;
    private static double freightOut;

    public COGSandFO(JFrame parent) {
        
        super(parent, "Enter Cogs", true);

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

        add(TitlePanel, BorderLayout.NORTH);
        add(ContentPanel); 

        JLabel InputCOGS = new JLabel();
        InputCOGS.setText("Input Cost of Goods Sold: ");
        InputCOGS.setFont(poppins); 
        InputCOGS.setForeground(Color.BLACK);
        InputCOGS.setHorizontalAlignment(SwingConstants.LEFT);
        InputCOGS.setBounds(43, 47, 200, 30);
        ContentPanel.add(InputCOGS);
                        
        //TextBox
        RoundedTextField COGSField = new RoundedTextField();
        COGSField.setBounds(42, 79, 242, 30); // <-- adjusted to fit frame
        COGSField.setCornerRadius(15);
        ContentPanel.add(COGSField);
        

        JLabel InputFO = new JLabel();
        InputFO.setText("Input Freight out: ");
        InputFO.setFont(poppins); 
        InputFO.setForeground(Color.BLACK);
        InputFO.setHorizontalAlignment(SwingConstants.LEFT);
        InputFO.setBounds(43, 147, 200, 30);
        ContentPanel.add(InputFO);

        RoundedTextField FOField = new RoundedTextField();
        FOField.setBounds(42, 182, 239, 30); // <-- adjusted to fit frame
        FOField.setCornerRadius(15);
        ContentPanel.add(FOField);

        JPanel ButtonsPanel = new JPanel();
        ButtonsPanel.setBackground(new Color(125, 216, 255));
        ButtonsPanel.setPreferredSize(new Dimension(700, 75));
        add(ButtonsPanel, BorderLayout.SOUTH);

        //Save Button
        RoundedButton confirm = new RoundedButton();
        confirm.setText("Confirm");
        confirm.setPreferredSize(new Dimension(150, 32)); 
        confirm.setFont(poppins); 
        confirm.setForeground(Color.WHITE);
        confirm.setCornerRadius(15);
        confirm.setFocusable(false); 
        confirm.addActionListener(e -> {
            cogs = Double.valueOf(COGSField.getText());
            freightOut = Double.valueOf(FOField.getText());
            this.dispose();
        });
        ButtonsPanel.add(confirm);

    }

    public static double getCogs() {
      return cogs;
    }
    public static double getFreightOut() {
      return freightOut;
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