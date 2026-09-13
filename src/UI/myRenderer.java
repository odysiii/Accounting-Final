package UI;

import java.awt.*;
import java.io.File;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class myRenderer implements TableCellRenderer {

    private int verticalAlign = SwingConstants.TOP;
    private int horizontalAlign = SwingConstants.LEFT;
    private static Font poppins = loadFont(GetPath.getPath() + "Poppins-Medium.ttf", 15f);
    private Color background = null;
    private Color foreground = null;

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        String text = value == null ? "" : value.toString();

        JTextArea area = new JTextArea(text);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(poppins);
        area.setOpaque(true);

        // Set colors properly
        if (isSelected) {
            area.setBackground(table.getSelectionBackground());
            area.setForeground(table.getSelectionForeground());
        } else {
            area.setBackground(background != null ? background : table.getBackground());
            area.setForeground(foreground != null ? foreground : table.getForeground());
        }

        // ---- Horizontal alignment simulation ----
        if (horizontalAlign == SwingConstants.RIGHT) {
            area.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        } else {
            area.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        }

        // ---- Vertical alignment ----
        // Use border insets to simulate top/center/bottom spacing
        int top = 0, bottom = 0;
        if (verticalAlign == SwingConstants.TOP) {
            top = 5; bottom = 0;
        } else if (verticalAlign == SwingConstants.CENTER) {
            top = 10; bottom = 10;
        } else if (verticalAlign == SwingConstants.BOTTOM) {
            top = 25; bottom = 5;
        }
        area.setBorder(BorderFactory.createEmptyBorder(top, 5, bottom, 5));

        return area;
    }

    // --- Controls ---
    public void setVerticalAlignment(int align) {
        this.verticalAlign = align;
    }

    public void setHorizontalAlignment(int align) {
        this.horizontalAlign = align;
    }

    public void setBackground(Color color) {
        this.background = color;
    }

    public void setForeground(Color color) {
        this.foreground = color;
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
