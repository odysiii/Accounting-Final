package UI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class RoundedButton extends JButton {

    private Color fill;
    private Color fillOriginal;
    private Color fillOver;
    private Color fillClick;
    private int cornerRadius;  

    public RoundedButton() {
        fillOriginal = new Color(21, 0, 105);   // normal 
        fillOver = new Color(52, 117, 201);       // hover 
        fillClick = new Color(9, 178, 230);    // clicked 
        fill = fillOriginal;

        // Turn off default button painting
        setOpaque(false);  //off solid rectangle color, makikita na yung sinet
        setContentAreaFilled(false); //remove yung sa bg na color ng shape
        setBorderPainted(false); //pantanggal ng border 
      
    
        // Hover and click effects
        addMouseListener(new MouseAdapter() {
     
             public void mouseExited(MouseEvent e) {
                fill = fillOriginal;
                repaint();
            }

            public void mouseEntered(MouseEvent e) {
                fill = fillOver;
                repaint();
            }

            public void mousePressed(MouseEvent e) {
                fill = fillClick;
                repaint();
            }

            public void mouseReleased(MouseEvent e) {
                fill = fillOver; // assume still hovering
                repaint();
            }
        });
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public void setFillOriginal(Color color) {
        this.fillOriginal = color;
        this.fill = color;
        repaint();
    }

    public void setFillOver(Color color) {
        this.fillOver = color;
    }

    public void setFillClick(Color color) {
        this.fillClick = color;
    }



    @Override
    protected void paintComponent(Graphics g) { //pang customize mismo ng button
        Graphics2D g2 = (Graphics2D) g.create(); //powerful ver ng graphics, supports shapes, antialiasing 
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //antialiasig == pang smooth ng edge ng button

        // Draw the rounded rectangle background
        g2.setColor(fill); //Sets the color for filling shapes, normal, hover, clicked
        //method sa g2d for rectangle w rounded corners
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Pang lagay ng text
        super.paintComponent(g);

        g2.dispose(); //clean up
    }
}




  

