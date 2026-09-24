package UI;

import java.awt.*;

import javax.swing.JPanel;

public class GradientPanel extends JPanel {

    private static final float[] STOPS = { 0f, 0.55f, 1f };
    private static final Color[] COLORS = { new Color(0xE3EBF8), new Color(0xF7F5F5), new Color(0xA7ADBC) };

    public GradientPanel() {
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(new LinearGradientPaint(0, 0, 0, Math.max(1, getHeight()), STOPS, COLORS));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
}
