package UI;

import java.awt.*;
import java.io.File;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RoundedTextField extends JTextField {

    private int cornerRadius;
    private Color backgroundColor = Color.WHITE;

    private static Font poppins = loadFont(GetPath.getPath() + "Poppins-Bold.ttf", 14f);
    

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
   

    public RoundedTextField(){

        // Important: allow text to be drawn inside
        setOpaque(false);

        // Add padding inside text area so text doesn't touch rounded edges
        setBorder(new EmptyBorder(8, 12, 8, 12));

        setFont(poppins);

        
    }

  
    // Setters for customization
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }

      @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background (behind text)
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        
        super.paintComponent(g);

        g2.dispose();
    }


    

   


}