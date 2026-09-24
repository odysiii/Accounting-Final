package UI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class RoundedButton extends JButton {

    private Color fill;
    private Color fillOriginal;
    private Color fillOver;
    private Color fillClick;
    private Color borderColor;
    private int cornerRadius;
    private int shadowSize;

    public RoundedButton() {
        fillOriginal = new Color(21, 0, 105);   // normal
        fillOver = new Color(52, 117, 201);       // hover
        fillClick = new Color(9, 178, 230);    // clicked
        fill = fillOriginal;

        // Turn off default button painting
        setOpaque(false);  //off solid rectangle color, makikita na yung sinet
        setContentAreaFilled(false); //remove yung sa bg na color ng shape
        setBorderPainted(false); //pantanggal ng border
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setIconTextGap(10);


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

    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    // Reserve room around the button for a soft drop shadow (size the button to include it).
    public void setShadowSize(int size) {
        this.shadowSize = size;
        setBorder(BorderFactory.createEmptyBorder(0, 0, 2 * (size / 3), 0));
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) { //pang customize mismo ng button
        Graphics2D g2 = Theme.aa(g); //powerful ver ng graphics, supports shapes, antialiasing
        Rectangle r = Theme.shapeBounds(getWidth(), getHeight(), shadowSize);

        if (shadowSize > 0) {
            Theme.shadow(g2, r.x, r.y, r.width, r.height, cornerRadius, shadowSize, 34);
        }

        // Draw the rounded rectangle background
        g2.setColor(fill); //Sets the color for filling shapes, normal, hover, clicked
        //method sa g2d for rectangle w rounded corners
        g2.fill(new RoundRectangle2D.Float(r.x, r.y, r.width, r.height, cornerRadius, cornerRadius));

        if (borderColor != null) {
            g2.setColor(borderColor);
            g2.draw(new RoundRectangle2D.Float(r.x + 0.5f, r.y + 0.5f, r.width - 1, r.height - 1, cornerRadius, cornerRadius));
        }

        // Pang lagay ng text
        super.paintComponent(g);

        g2.dispose(); //clean up
    }
}
