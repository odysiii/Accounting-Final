package UI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EnumMap;

import javax.swing.*;

// White top bar: logo + title on the left, Home / Create / View on the right.
public class HeaderBar extends JPanel {

    private final Brand brand = new Brand();
    private final EnumMap<AppFrame.Section, NavItem> navItems = new EnumMap<>(AppFrame.Section.class);

    public HeaderBar(AppFrame app) {
        setBackground(Color.WHITE);
        setOpaque(true);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 84));
        setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 44));

        JPanel left = new JPanel(new GridBagLayout());
        left.setOpaque(false);
        left.add(new JLabel(Icons.logo(48)), new GridBagConstraints());
        GridBagConstraints brandSpot = new GridBagConstraints();
        brandSpot.insets = new Insets(0, 14, 0, 0);
        left.add(brand, brandSpot);

        JPanel nav = new JPanel(new GridBagLayout());
        nav.setOpaque(false);
        addNav(nav, AppFrame.Section.HOME, "Home", Icons.Type.HOME, app::goHome);
        addNav(nav, AppFrame.Section.CREATE, "Create", Icons.Type.FOLDER_PLUS, app::startNewJournal);
        addNav(nav, AppFrame.Section.VIEW, "View", Icons.Type.EYE, app::goSaved);

        add(left, BorderLayout.WEST);
        add(nav, BorderLayout.EAST);
    }

    private void addNav(JPanel nav, AppFrame.Section section, String text, Icons.Type icon, Runnable action) {
        NavItem item = new NavItem(text, icon, action);
        navItems.put(section, item);
        GridBagConstraints spot = new GridBagConstraints();
        spot.insets = new Insets(0, nav.getComponentCount() == 0 ? 0 : 36, 0, 0);
        nav.add(item, spot);
    }

    // With no subtitle the header shows the plain "Accounting Cycle" brand; otherwise a page title with the brand beneath it.
    public void setState(AppFrame.Section section, String title, String subtitle, boolean smallBrand) {
        for (AppFrame.Section s : navItems.keySet()) {
            navItems.get(s).setActive(s == section);
        }
        brand.set(title, subtitle, smallBrand);
        revalidate();
        repaint();
    }

    private static class Brand extends JComponent {

        private String title;
        private String subtitle;
        private boolean small = true;

        void set(String title, String subtitle, boolean small) {
            this.title = title;
            this.subtitle = subtitle;
            this.small = small;
            revalidate();
            repaint();
        }

        private Font titleFont() {
            if (title == null) {
                return Theme.jakarta(Font.BOLD, small ? 20f : 30f);
            }
            return Theme.interWeight("ExtraBold", 30f);
        }

        private Font subtitleFont() {
            return Theme.jakarta(Font.BOLD, 13f);
        }

        @Override
        public Dimension getPreferredSize() {
            String text = title == null ? "Accounting Cycle" : title;
            float width = Theme.width(titleFont(), text);
            if (subtitle != null) {
                width = Math.max(width, Theme.width(subtitleFont(), subtitle));
            }
            return new Dimension((int) Math.ceil(width) + 6, 60);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Theme.aa(g);
            g2.setFont(titleFont());
            FontMetrics fm = g2.getFontMetrics();
            if (title == null) {
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                Theme.gradientText(g2, "Accounting Cycle", 0, y, Theme.BLUE_LIGHT, Theme.BLUE);
            } else {
                int y = subtitle == null ? (getHeight() + fm.getAscent() - fm.getDescent()) / 2 : 33;
                g2.setColor(Theme.INK);
                Theme.draw(g2, title, 0, y);
                if (subtitle != null) {
                    g2.setFont(subtitleFont());
                    g2.setColor(Theme.BLUE_LIGHT);
                    Theme.draw(g2, subtitle, 2, 51);
                }
            }
            g2.dispose();
        }
    }

    private static class NavItem extends JComponent {

        private static final Color IDLE = new Color(0x6B6E76);

        private final String text;
        private final Icons.Type icon;
        private boolean active;
        private boolean hover;

        NavItem(String text, Icons.Type icon, Runnable action) {
            this.text = text;
            this.icon = icon;
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
            return Theme.jakartaWeight("SemiBold", 16f);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(22 + 9 + (int) Math.ceil(Theme.width(font(), text)) + 4, 40);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Theme.aa(g);
            Color color = active ? Theme.BLUE : hover ? Theme.INK : IDLE;
            Icons.get(icon, 22, color).paintIcon(this, g2, 0, (getHeight() - 22) / 2);
            g2.setFont(font());
            g2.setColor(color);
            FontMetrics fm = g2.getFontMetrics();
            Theme.draw(g2, text, 31, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
        }
    }
}
