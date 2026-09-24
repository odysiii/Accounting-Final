package UI;

import java.awt.*;

import javax.swing.*;

// A panel that fills its scroll pane's width, and its height too when the content is shorter.
public class ScrollPanel extends JPanel implements Scrollable {

    public ScrollPanel() {
        setOpaque(false);
    }

    public ScrollPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return orientation == SwingConstants.VERTICAL ? Math.max(visibleRect.height - 20, 16) : visibleRect.width;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return getParent() instanceof JViewport && ((JViewport) getParent()).getHeight() > getPreferredSize().height;
    }

    // Flow layout that wraps onto new rows and reports the wrapped height.
    public static class WrapLayout extends FlowLayout {

        public WrapLayout(int align, int hgap, int vgap) {
            super(align, hgap, vgap);
        }

        @Override
        public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }

        @Override
        public Dimension minimumLayoutSize(Container target) {
            Dimension min = layoutSize(target, false);
            min.width -= getHgap() + 1;
            return min;
        }

        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                Container container = target;
                while (container.getSize().width == 0 && container.getParent() != null) {
                    container = container.getParent();
                }
                int targetWidth = container.getSize().width;
                if (targetWidth == 0) {
                    targetWidth = Integer.MAX_VALUE;
                }

                int hgap = getHgap();
                int vgap = getVgap();
                Insets insets = target.getInsets();
                int horizontal = insets.left + insets.right + hgap * 2;
                int maxWidth = targetWidth - horizontal;

                Dimension dim = new Dimension(0, 0);
                int rowWidth = 0;
                int rowHeight = 0;

                for (int i = 0; i < target.getComponentCount(); i++) {
                    Component member = target.getComponent(i);
                    if (!member.isVisible()) {
                        continue;
                    }
                    Dimension d = preferred ? member.getPreferredSize() : member.getMinimumSize();
                    if (rowWidth + d.width > maxWidth) {
                        addRow(dim, rowWidth, rowHeight);
                        rowWidth = 0;
                        rowHeight = 0;
                    }
                    if (rowWidth != 0) {
                        rowWidth += hgap;
                    }
                    rowWidth += d.width;
                    rowHeight = Math.max(rowHeight, d.height);
                }
                addRow(dim, rowWidth, rowHeight);

                dim.width += horizontal;
                dim.height += insets.top + insets.bottom + vgap * 2;

                if (SwingUtilities.getAncestorOfClass(JScrollPane.class, target) != null && target.isValid()) {
                    dim.width -= hgap + 1;
                }
                return dim;
            }
        }

        private void addRow(Dimension dim, int rowWidth, int rowHeight) {
            dim.width = Math.max(dim.width, rowWidth);
            if (dim.height > 0) {
                dim.height += getVgap();
            }
            dim.height += rowHeight;
        }
    }
}
