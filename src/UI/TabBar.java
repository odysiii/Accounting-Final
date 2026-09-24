package UI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.EnumMap;

import javax.swing.*;

// The six journal-workflow tabs shown once a journal has been saved.
public class TabBar extends JPanel {

    private final EnumMap<AppFrame.Tab, TabButton> buttons = new EnumMap<>(AppFrame.Tab.class);

    public TabBar(AppFrame app) {
        setOpaque(false);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(11, 20, 11, 20));

        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 1;
        gc.anchor = GridBagConstraints.CENTER;
        for (AppFrame.Tab tab : AppFrame.Tab.values()) {
            TabButton button = new TabButton(tab, () -> app.showTab(tab));
            buttons.put(tab, button);
            add(button, gc);
        }
    }

    public void setActive(AppFrame.Tab active) {
        setVisible(active != null);
        for (AppFrame.Tab tab : buttons.keySet()) {
            buttons.get(tab).setActive(tab == active);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(new Color(255, 255, 255, 120));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(new Color(0xDDE3EF));
        g.fillRect(0, getHeight() - 1, getWidth(), 1);
    }

    private static class TabButton extends JComponent {

        private static final int SHADOW = 4;

        private final AppFrame.Tab tab;
        private boolean active;
        private boolean hover;

        TabButton(AppFrame.Tab tab, Runnable action) {
            this.tab = tab;
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    action.run();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        void setActive(boolean active) {
            this.active = active;
            repaint();
        }

        private Font font() {
            return Theme.jakartaWeight("SemiBold", 15f);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension((int) Math.ceil(Theme.width(font(), tab.label)) + 20 + 8 + 2 * 20 + 2 * SHADOW + 4, 40 + 2 * SHADOW);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Theme.aa(g);
            Rectangle r = Theme.shapeBounds(getWidth(), getHeight(), SHADOW);

            if (active) {
                Theme.shadow(g2, r.x, r.y, r.width, r.height, 30, SHADOW, 16);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(r.x, r.y, r.width, r.height, 30, 30));
                g2.setColor(new Color(0xDCE2EE));
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(r.x + 0.75f, r.y + 0.75f, r.width - 1.5f, r.height - 1.5f, 30, 30));
            } else if (hover) {
                g2.setColor(new Color(255, 255, 255, 150));
                g2.fill(new RoundRectangle2D.Float(r.x, r.y, r.width, r.height, 30, 30));
            }

            Color color = active ? Theme.BLUE : new Color(0x6B6E76);
            g2.setFont(font());
            FontMetrics fm = g2.getFontMetrics();
            int contentWidth = 20 + 8 + (int) Math.ceil(Theme.width(font(), tab.label));
            int x = r.x + (r.width - contentWidth) / 2;
            int mid = r.y + r.height / 2;
            Icons.get(tab.icon, 20, color).paintIcon(this, g2, x, mid - 10);
            g2.setColor(color);
            Theme.draw(g2, tab.label, x + 28, mid + (fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
        }
    }
}
