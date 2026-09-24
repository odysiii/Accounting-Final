package UI;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Icons {

    public enum Type {
        HOME, FOLDER_PLUS, EYE, PLUS, PLUS_CIRCLE, LIST, BUILDING, CALENDAR, CLOSE, TRASH, ARROW, BOOK, GRID, SCALE, SLIDERS, DOC
    }

    public static Icon get(Type type, int size, Color color) {
        return new VectorIcon(type, size, color);
    }

    // Uses src/UI/Images/logo.png when it exists, otherwise a drawn stand-in.
    public static Icon logo(int size) {
        File file = new File(GetPath.getImagePath() + "logo.png");
        if (file.exists()) {
            try {
                BufferedImage source = ImageIO.read(file);
                int width = Math.max(1, source.getWidth() * size / source.getHeight());
                BufferedImage scaled = new BufferedImage(width, size, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = scaled.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.drawImage(source, 0, 0, width, size, null);
                g2.dispose();
                return new ImageIcon(scaled);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new LogoIcon(size);
    }

    private static class LogoIcon implements Icon {

        private final int size;

        LogoIcon(int size) {
            this.size = size;
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = Theme.aa(g);
            g2.translate(x, y);
            g2.scale(size / 48.0, size / 48.0);
            g2.setPaint(new GradientPaint(6, 42, new Color(0x0A2A6B), 42, 6, new Color(0x2B8CF0)));
            g2.setStroke(new BasicStroke(3.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.draw(new Arc2D.Double(4, 4, 40, 40, 40, 290, Arc2D.OPEN));
            g2.setColor(new Color(0x1D6FE8));
            g2.fillRoundRect(15, 26, 5, 10, 2, 2);
            g2.fillRoundRect(23, 20, 5, 16, 2, 2);
            g2.fillRoundRect(31, 13, 5, 23, 2, 2);
            g2.dispose();
        }
    }

    private static class VectorIcon implements Icon {

        private final Type type;
        private final int size;
        private final Color color;

        VectorIcon(Type type, int size, Color color) {
            this.type = type;
            this.size = size;
            this.color = color;
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = Theme.aa(g);
            g2.translate(x, y);
            g2.scale(size / 24.0, size / 24.0);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.9f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            draw(g2);
            g2.dispose();
        }

        private void draw(Graphics2D g) {
            switch (type) {
                case HOME: {
                    Path2D p = new Path2D.Double();
                    p.moveTo(12, 3);
                    p.lineTo(21.5, 11.5);
                    p.lineTo(19, 11.5);
                    p.lineTo(19, 20);
                    p.lineTo(14.5, 20);
                    p.lineTo(14.5, 14.5);
                    p.lineTo(9.5, 14.5);
                    p.lineTo(9.5, 20);
                    p.lineTo(5, 20);
                    p.lineTo(5, 11.5);
                    p.lineTo(2.5, 11.5);
                    p.closePath();
                    g.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g.fill(p);
                    g.draw(p);
                    break;
                }
                case FOLDER_PLUS: {
                    Area folder = new Area(new RoundRectangle2D.Double(2.5, 7, 19, 13.5, 3, 3));
                    folder.add(new Area(new RoundRectangle2D.Double(2.5, 4, 9, 6, 2.5, 2.5)));
                    Area plus = new Area(new Rectangle2D.Double(11.4, 10.6, 1.8, 6.4));
                    plus.add(new Area(new Rectangle2D.Double(9.2, 12.85, 6.2, 1.8)));
                    folder.subtract(plus);
                    g.fill(folder);
                    break;
                }
                case EYE: {
                    Path2D almond = new Path2D.Double();
                    almond.moveTo(1.5, 12);
                    almond.curveTo(5, 5.2, 19, 5.2, 22.5, 12);
                    almond.curveTo(19, 18.8, 5, 18.8, 1.5, 12);
                    almond.closePath();
                    Area eye = new Area(almond);
                    eye.subtract(new Area(new Ellipse2D.Double(7.4, 7.4, 9.2, 9.2)));
                    eye.add(new Area(new Ellipse2D.Double(9.6, 9.6, 4.8, 4.8)));
                    g.fill(eye);
                    break;
                }
                case PLUS:
                    g.setStroke(new BasicStroke(2.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g.draw(new Line2D.Double(12, 5, 12, 19));
                    g.draw(new Line2D.Double(5, 12, 19, 12));
                    break;
                case PLUS_CIRCLE:
                    g.draw(new Ellipse2D.Double(2.5, 2.5, 19, 19));
                    g.draw(new Line2D.Double(12, 7.5, 12, 16.5));
                    g.draw(new Line2D.Double(7.5, 12, 16.5, 12));
                    break;
                case LIST:
                    g.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    for (int i = 0; i < 3; i++) {
                        double row = 5 + i * 7;
                        g.draw(new RoundRectangle2D.Double(3, row - 2, 4, 4, 1.4, 1.4));
                        g.draw(new Line2D.Double(10.5, row, 21, row));
                    }
                    break;
                case BUILDING:
                    g.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g.draw(new RoundRectangle2D.Double(4.5, 3, 15, 18, 2, 2));
                    for (int r = 0; r < 3; r++) {
                        for (int c = 0; c < 2; c++) {
                            g.fill(new Rectangle2D.Double(8.2 + c * 5.4, 6.6 + r * 4.2, 1.9, 1.9));
                        }
                    }
                    break;
                case CALENDAR:
                    g.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g.draw(new RoundRectangle2D.Double(3, 5, 18, 16, 3.5, 3.5));
                    g.draw(new Line2D.Double(3, 10.2, 21, 10.2));
                    g.draw(new Line2D.Double(8, 3, 8, 7));
                    g.draw(new Line2D.Double(16, 3, 16, 7));
                    break;
                case CLOSE:
                    g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g.draw(new Line2D.Double(5.5, 5.5, 18.5, 18.5));
                    g.draw(new Line2D.Double(18.5, 5.5, 5.5, 18.5));
                    break;
                case TRASH: {
                    g.fill(new RoundRectangle2D.Double(4, 5.2, 16, 2.2, 1.1, 1.1));
                    g.fill(new RoundRectangle2D.Double(9, 2.8, 6, 3, 1.2, 1.2));
                    Area body = new Area(new RoundRectangle2D.Double(6, 8.6, 12, 12.4, 2.6, 2.6));
                    body.subtract(new Area(new Rectangle2D.Double(9.4, 11.2, 1.4, 6.6)));
                    body.subtract(new Area(new Rectangle2D.Double(13.2, 11.2, 1.4, 6.6)));
                    g.fill(body);
                    break;
                }
                case ARROW:
                    g.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g.draw(new Line2D.Double(4, 12, 19.5, 12));
                    g.draw(new Line2D.Double(13.5, 6, 19.5, 12));
                    g.draw(new Line2D.Double(13.5, 18, 19.5, 12));
                    break;
                case BOOK: {
                    g.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    Path2D p = new Path2D.Double();
                    p.moveTo(12, 6.5);
                    p.curveTo(9, 4.2, 5.5, 4.2, 3, 5);
                    p.lineTo(3, 19);
                    p.curveTo(5.5, 18.2, 9, 18.2, 12, 20.5);
                    p.curveTo(15, 18.2, 18.5, 18.2, 21, 19);
                    p.lineTo(21, 5);
                    p.curveTo(18.5, 4.2, 15, 4.2, 12, 6.5);
                    g.draw(p);
                    g.draw(new Line2D.Double(12, 6.5, 12, 20.5));
                    break;
                }
                case GRID:
                    g.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g.draw(new RoundRectangle2D.Double(3.5, 3.5, 6.5, 6.5, 2, 2));
                    g.draw(new RoundRectangle2D.Double(14, 3.5, 6.5, 6.5, 2, 2));
                    g.draw(new RoundRectangle2D.Double(3.5, 14, 6.5, 6.5, 2, 2));
                    g.draw(new RoundRectangle2D.Double(14, 14, 6.5, 6.5, 2, 2));
                    break;
                case SCALE:
                    g.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g.draw(new Line2D.Double(12, 3.5, 12, 20));
                    g.draw(new Line2D.Double(7.5, 20.5, 16.5, 20.5));
                    g.draw(new Line2D.Double(4.5, 6.5, 19.5, 6.5));
                    for (double px : new double[] { 4.5, 19.5 }) {
                        Path2D p = new Path2D.Double();
                        p.moveTo(px - 3.2, 13.5);
                        p.lineTo(px, 6.5);
                        p.lineTo(px + 3.2, 13.5);
                        g.draw(p);
                        g.draw(new Arc2D.Double(px - 3.2, 11.2, 6.4, 5, 180, 180, Arc2D.OPEN));
                    }
                    break;
                case SLIDERS: {
                    g.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    double[] xs = { 5, 12, 19 };
                    double[] knob = { 15, 8.5, 13 };
                    for (int i = 0; i < 3; i++) {
                        g.draw(new Line2D.Double(xs[i], 3, xs[i], knob[i] - 2));
                        g.draw(new Line2D.Double(xs[i], knob[i] + 2, xs[i], 21));
                        g.draw(new Line2D.Double(xs[i] - 2.6, knob[i], xs[i] + 2.6, knob[i]));
                    }
                    break;
                }
                case DOC: {
                    g.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    Path2D p = new Path2D.Double();
                    p.moveTo(6, 2.8);
                    p.lineTo(14, 2.8);
                    p.lineTo(19, 7.8);
                    p.lineTo(19, 21.2);
                    p.lineTo(6, 21.2);
                    p.closePath();
                    g.draw(p);
                    Path2D fold = new Path2D.Double();
                    fold.moveTo(14, 2.8);
                    fold.lineTo(14, 7.8);
                    fold.lineTo(19, 7.8);
                    g.draw(fold);
                    g.draw(new Line2D.Double(9, 12.5, 16, 12.5));
                    g.draw(new Line2D.Double(9, 16.5, 16, 16.5));
                    break;
                }
            }
        }
    }
}
