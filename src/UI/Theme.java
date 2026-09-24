package UI;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class Theme {

    public static final Color BLUE = new Color(0x0058E0);
    public static final Color BLUE_HOVER = new Color(0x0048BA);
    public static final Color BLUE_CLICK = new Color(0x003A96);
    public static final Color BLUE_LIGHT = new Color(0x2B86F5);
    public static final Color INK = new Color(0x0B0B0B);
    public static final Color GRAY_TEXT = new Color(0x6E7280);
    public static final Color HINT = new Color(0xA8A8AD);
    public static final Color LINE = new Color(0xDCE0E8);

    public static final Color DEBIT_BG = new Color(0xCFE5DB);
    public static final Color DEBIT_HEAD = new Color(0xB9D3C5);
    public static final Color DEBIT_TEXT = new Color(0x2F5A40);
    public static final Color CREDIT_BG = new Color(0xE6CFCF);
    public static final Color CREDIT_HEAD = new Color(0xD5B8B8);
    public static final Color CREDIT_TEXT = new Color(0xB00000);

    public static final Color HEAD_GRAY = new Color(0xE9E2E2);
    public static final Color HEAD_GRAY_TB = new Color(0xD9D9D9);
    public static final Color FIELD = new Color(0xE9E9E9);
    public static final Color FIELD_BLUSH = new Color(0xF2E8E5);

    private static final HashMap<String, Font> cache = new HashMap<>();
    private static final FontRenderContext FRC = new FontRenderContext(null, true, true);
    private static final DecimalFormat WHOLE = new DecimalFormat("#,##0");
    private static final DecimalFormat CENTS = new DecimalFormat("#,##0.00");

    private static Font base(String file) {
        Font font = cache.get(file);
        if (font == null) {
            try {
                font = Font.createFont(Font.TRUETYPE_FONT, new File(GetPath.getPath() + file));
                cache.put(file, font);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return font;
    }

    private static Font make(String file, String fallback, int style, float size) {
        Font font = base(file);
        if (font == null) {
            return new Font(fallback, style, Math.round(size));
        }
        Map<TextAttribute, Object> kerning = new HashMap<>();
        kerning.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
        return font.deriveFont(size).deriveFont(kerning);
    }

    public static Font inter(int style, float size) {
        return make((style & Font.BOLD) != 0 ? "Inter-Bold.ttf" : "Inter-Regular.ttf", "SansSerif", style, size);
    }

    // weight: Medium, SemiBold or ExtraBold
    public static Font interWeight(String weight, float size) {
        return make("Inter-" + weight + ".ttf", "SansSerif", Font.BOLD, size);
    }

    public static Font jakarta(int style, float size) {
        return make((style & Font.BOLD) != 0 ? "PlusJakartaSans-Bold.ttf" : "PlusJakartaSans-Regular.ttf", "SansSerif", style, size);
    }

    // weight: Medium or SemiBold
    public static Font jakartaWeight(String weight, float size) {
        return make("PlusJakartaSans-" + weight + ".ttf", "SansSerif", Font.BOLD, size);
    }

    public static Font mono(int style, float size) {
        boolean bold = (style & Font.BOLD) != 0;
        boolean italic = (style & Font.ITALIC) != 0;
        String file = bold ? (italic ? "RobotoMono-BoldItalic.ttf" : "RobotoMono-Bold.ttf")
                : (italic ? "RobotoMono-Italic.ttf" : "RobotoMono-Regular.ttf");
        return make(file, "Monospaced", style, size);
    }

    public static String money(double value) {
        if (value == 0) {
            value = 0;
        }
        return Math.rint(value) == value ? WHOLE.format(value) : CENTS.format(value);
    }

    public static String money(String value) {
        try {
            return money(Double.parseDouble(value.trim().replace(",", "")));
        } catch (Exception e) {
            return value == null ? "" : value;
        }
    }

    public static Graphics2D aa(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        return g2;
    }

    // Area a shadowed component paints its shape in: leaves room for the shadow on every side.
    public static Rectangle shapeBounds(int width, int height, int shadow) {
        int offset = shadow / 3;
        return new Rectangle(shadow, shadow - offset, width - 2 * shadow, height - 2 * shadow);
    }

    public static void shadow(Graphics2D g2, int x, int y, int w, int h, int arc, int size, int alpha) {
        int offset = size / 3;
        int layer = Math.max(1, alpha / size);
        for (int i = size; i >= 1; i--) {
            g2.setColor(new Color(15, 35, 90, layer));
            g2.fillRoundRect(x - i, y - i + offset, w + 2 * i, h + 2 * i, arc + i, arc + i);
        }
    }

    // Width the text is painted at by draw(): fractional metrics, plus a minimum word gap.
    public static float width(Font font, String text) {
        float width = (float) font.getStringBounds(text, FRC).getWidth();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                width += spaceGap(font);
            }
        }
        return width;
    }

    public static void draw(Graphics2D g2, String text, float x, float y) {
        Font font = g2.getFont();
        float gap = spaceGap(font);
        if (gap == 0) {
            g2.drawString(text, x, y);
            return;
        }
        for (String word : text.split(" ", -1)) {
            g2.drawString(word, x, y);
            x += (float) font.getStringBounds(word + " ", FRC).getWidth() + gap;
        }
    }

    // Extra pixels added to each word gap when the font's space glyph is narrower than a quarter of an em.
    private static float spaceGap(Font font) {
        float space = (float) font.getStringBounds(" ", FRC).getWidth();
        return Math.max(0f, font.getSize2D() * 0.25f - space);
    }

    public static void gradientText(Graphics2D g2, String text, int x, int y, Color from, Color to) {
        FontMetrics fm = g2.getFontMetrics();
        g2.setPaint(new GradientPaint(x, y - fm.getAscent(), from, x + width(g2.getFont(), text), y, to));
        draw(g2, text, x, y);
    }

    public static Border pagePad() {
        return BorderFactory.createEmptyBorder(28, 36, 32, 36);
    }

    public static JButton iconButton(Icon icon) {
        JButton button = new JButton(icon);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setOpaque(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static void stylePrimary(RoundedButton button) {
        button.setFillOriginal(BLUE);
        button.setFillOver(BLUE_HOVER);
        button.setFillClick(BLUE_CLICK);
        button.setForeground(Color.WHITE);
        button.setFocusable(false);
    }

    public static void styleSecondary(RoundedButton button) {
        button.setFillOriginal(new Color(0xDDDDDD));
        button.setFillOver(new Color(0xD0D0D0));
        button.setFillClick(new Color(0xBEBEBE));
        button.setForeground(INK);
        button.setFocusable(false);
    }

    public static void slim(JScrollPane pane) {
        pane.setBorder(null);
        pane.setOpaque(false);
        pane.getViewport().setOpaque(false);
        pane.getVerticalScrollBar().setUI(new SlimBar());
        pane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        pane.getVerticalScrollBar().setOpaque(false);
        pane.getVerticalScrollBar().setUnitIncrement(16);
        pane.getHorizontalScrollBar().setUI(new SlimBar());
        pane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));
        pane.getHorizontalScrollBar().setOpaque(false);
    }

    private static class SlimBar extends BasicScrollBarUI {

        @Override
        protected void configureScrollBarColors() {
            thumbColor = new Color(0xBFC5D3);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return empty();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return empty();
        }

        private JButton empty() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumb) {
            Graphics2D g2 = aa(g);
            g2.setColor(thumbColor);
            g2.fillRoundRect(thumb.x + 2, thumb.y + 2, thumb.width - 4, thumb.height - 4, 8, 8);
            g2.dispose();
        }
    }
}
