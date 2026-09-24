package UI;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;

import javax.swing.*;

// Undecorated, rounded pop-up card shown over a dimmed main window. setVisible(true) blocks until it closes.
public abstract class ModalCard extends JDialog {

    private static final int SHADOW = 26;

    protected final RoundedPanel card = new RoundedPanel();
    private Component previousGlass;

    public ModalCard(Window owner, String title, int cardWidth, int cardHeight, int arc) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setResizable(false);

        card.setBackground(Color.WHITE);
        card.setCornerRadius(arc);
        card.setShadowSize(SHADOW);
        card.setAccent(Theme.BLUE, 6);
        card.setClipChildren(true);
        setContentPane(card);
        setSize(cardWidth + 2 * SHADOW, cardHeight + 2 * SHADOW);

        getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    protected JButton closeButton() {
        JButton close = Theme.iconButton(Icons.get(Icons.Type.CLOSE, 22, new Color(0x5F6368)));
        close.addActionListener(e -> dispose());
        return close;
    }

    @Override
    public void setVisible(boolean visible) {
        Window owner = getOwner();
        if (visible) {
            setLocationRelativeTo(owner);
            dim(owner);
        }
        super.setVisible(visible);
        undim(owner);
    }

    private void dim(Window owner) {
        if (owner instanceof RootPaneContainer) {
            RootPaneContainer root = (RootPaneContainer) owner;
            previousGlass = root.getGlassPane();
            JComponent dimmer = new JComponent() {
                @Override
                protected void paintComponent(Graphics g) {
                    g.setColor(new Color(10, 25, 60, 95));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            dimmer.addMouseListener(new MouseAdapter() {
            });
            root.setGlassPane(dimmer);
            dimmer.setVisible(true);
        }
    }

    private void undim(Window owner) {
        if (previousGlass != null && owner instanceof RootPaneContainer) {
            RootPaneContainer root = (RootPaneContainer) owner;
            root.getGlassPane().setVisible(false);
            root.setGlassPane(previousGlass);
            previousGlass = null;
        }
    }
}
