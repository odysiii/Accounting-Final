package UI;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class RoundedTextField extends JTextField {

    private int cornerRadius;
    private Color backgroundColor = Color.WHITE;
    private String placeholder = "";
    private Icon trailingIcon;
    private boolean error;

    public RoundedTextField(){

        // Important: allow text to be drawn inside
        setOpaque(false);

        // Add padding inside text area so text doesn't touch rounded edges
        setBorder(new EmptyBorder(8, 16, 8, 16));

        setFont(Theme.inter(Font.PLAIN, 14f));
        setForeground(Theme.INK);
        setCaretColor(Theme.INK);

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });

        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                typed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                typed();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                typed();
            }
        });
    }

    private void typed() {
        if (error && !getText().isEmpty()) {
            error = false;
        }
        repaint();
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

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    public void setTrailingIcon(Icon icon) {
        this.trailingIcon = icon;
        setBorder(new EmptyBorder(8, 16, 8, 16 + icon.getIconWidth() + 8));
        repaint();
    }

    public void setError(boolean error) {
        this.error = error;
        repaint();
    }

      @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Theme.aa(g);

        // Draw background (behind text)
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        if (error || hasFocus()) {
            g2.setColor(error ? new Color(0xD93025) : Theme.BLUE);
            g2.setStroke(new BasicStroke(1.6f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
        }

        super.paintComponent(g);

        if (getText().isEmpty() && !placeholder.isEmpty()) {
            g2.setFont(getFont());
            g2.setColor(Theme.HINT);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(placeholder, getInsets().left, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
        }

        if (trailingIcon != null) {
            trailingIcon.paintIcon(this, g2, getWidth() - trailingIcon.getIconWidth() - 16,
                    (getHeight() - trailingIcon.getIconHeight()) / 2);
        }

        g2.dispose();
    }

}
