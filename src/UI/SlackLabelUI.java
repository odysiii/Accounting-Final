package UI;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalLabelUI;

// Labels report a few extra pixels of width: the bundled fonts paint slightly wider than Swing measures them,
// which would otherwise clip the end of a label. Registered for every JLabel in AppFrame.
public class SlackLabelUI extends MetalLabelUI {

    private static final SlackLabelUI INSTANCE = new SlackLabelUI();

    public static ComponentUI createUI(JComponent c) {
        return INSTANCE;
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        Dimension size = super.getPreferredSize(c);
        String text = ((JLabel) c).getText();
        if (text != null && !text.isEmpty()) {
            size.width += size.width / 30 + 2;
        }
        return size;
    }
}
