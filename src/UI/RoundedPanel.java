package UI;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;


public class RoundedPanel extends JPanel {

    public static final int TOP = 0;
    public static final int LEFT = 1;

    private Color fillColor;
    private int cornerRadius;
    private int shadowSize;
    private Color borderColor;
    private Color accentColor;
    private int accentSize;
    private int accentSide = TOP;
    private boolean clipChildren;

    public RoundedPanel() {
        fillColor = new Color(125,216,255); // Background color
        setOpaque(false); // Prevent default background

    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        repaint();
    }

    public Color getFillColor() {
        return fillColor;
    }


    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        repaint();
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setShadowSize(int shadowSize) {
        this.shadowSize = shadowSize;
        revalidate();
        repaint();
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    public void setAccent(Color color, int size) {
        this.accentColor = color;
        this.accentSize = size;
        revalidate();
        repaint();
    }

    public void setAccentSide(int side) {
        this.accentSide = side;
        revalidate();
        repaint();
    }

    // Children are cut off at the rounded corners instead of poking out of them.
    public void setClipChildren(boolean clipChildren) {
        this.clipChildren = clipChildren;
        repaint();
    }

    private Rectangle shapeArea() {
        return Theme.shapeBounds(getWidth(), getHeight(), shadowSize);
    }

    private Shape shape() {
        Rectangle r = shapeArea();
        return new RoundRectangle2D.Float(r.x, r.y, r.width, r.height, cornerRadius, cornerRadius);
    }

    @Override
    public Insets getInsets() {
        Insets in = super.getInsets();
        int offset = shadowSize / 3;
        int top = in.top + shadowSize - offset;
        int left = in.left + shadowSize;
        if (accentColor != null) {
            if (accentSide == TOP) {
                top += accentSize;
            } else {
                left += accentSize;
            }
        }
        return new Insets(top, left, in.bottom + shadowSize + offset, in.right + shadowSize);
    }

    @Override
    public void paint(Graphics g) {
        if (shadowSize > 0) {
            Graphics2D g2 = Theme.aa(g);
            Rectangle r = shapeArea();
            Theme.shadow(g2, r.x, r.y, r.width, r.height, cornerRadius, shadowSize, 40);
            g2.dispose();
        }
        if (clipChildren) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.clip(shape());
            super.paint(g2);
            g2.dispose();
        } else {
            super.paint(g);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Theme.aa(g);

        // Draw rounded background
        Shape shape = shape();
        g2.setColor(getBackground());
        g2.fill(shape);

        if (accentColor != null) {
            Rectangle r = shapeArea();
            g2.clip(shape);
            g2.setColor(accentColor);
            if (accentSide == TOP) {
                g2.fillRect(r.x, r.y, r.width, accentSize);
            } else {
                g2.fillRect(r.x, r.y, accentSize, r.height);
            }
        }

        g2.dispose();
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);
        if (borderColor != null) {
            Rectangle r = shapeArea();
            Graphics2D g2 = Theme.aa(g);
            g2.setColor(borderColor);
            g2.draw(new RoundRectangle2D.Float(r.x + 0.5f, r.y + 0.5f, r.width - 1, r.height - 1, cornerRadius, cornerRadius));
            g2.dispose();
        }
    }


}
