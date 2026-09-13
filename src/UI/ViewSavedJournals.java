package UI;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;

import Backend.Ledger;


public class ViewSavedJournals {

    private static Font poppins = loadFont(GetPath.getPath() + "Poppins-Medium.ttf", 15f);
    private static Font lexend = loadFont(GetPath.getPath() + "Lexend-VariableFont_wght.ttf", 20f);
    private static Font anton = loadFont(GetPath.getPath() + "Anton-Regular.ttf", 70f);
    
    private static File folder = new File(GetPath.getCsvPath());
    private static File[] csvFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
    public ViewSavedJournals() {

        Border border = BorderFactory.createLineBorder(Color.gray, 2);

        JFrame SavedJournals_frame = new JFrame();
        SavedJournals_frame.setVisible(true);
        SavedJournals_frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        SavedJournals_frame.setLayout(new BorderLayout());
        
        JPanel header = new JPanel();

        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(border);

        JPanel center = new JPanel();
        center.setLayout(new BorderLayout());


        JLabel SavedJournals_label = new JLabel("SAVED JOURNALS");
        SavedJournals_label.setFont(anton);
        SavedJournals_label.setForeground(Color.BLACK);
        SavedJournals_label.setBackground(Color.red);
        SavedJournals_label.setHorizontalAlignment(SwingConstants.LEFT);
        SavedJournals_label.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 0));

        JPanel Files_panel = new JPanel();
        Files_panel.setLayout(new BoxLayout(Files_panel, BoxLayout.Y_AXIS));
        Files_panel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));

        JScrollPane scrollPane = new JScrollPane(Files_panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(11, 0));
        
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
    
        protected void configureScrollBarColors() {
        this.thumbColor = new Color(211, 211, 211); // scroller color
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(thumbColor);
        g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
        g2.dispose();
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
        JButton button = super.createDecreaseButton(orientation);
        button.setBackground(new Color(245, 245, 245));  // arrow
        return button;
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
        JButton button = super.createIncreaseButton(orientation);
        button.setBackground(new Color(245, 245, 245));
        return button;
        }

        });

        for (File csvFile : csvFiles) {

            String fileName = csvFile.getName();
            String shortenFileName = fileName.replace(".txt", "");

            RoundedPanel files = new RoundedPanel();
            files.setLayout(new BorderLayout());
            files.setBackground(new Color(125,216,255));
            files.setPreferredSize(new Dimension(1200, 85));
            files.setCornerRadius(20);
            files.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));
            files.setBorder(BorderFactory.createEmptyBorder(23, 5, 23, 30));

            RoundedPanel buttonPanel = new RoundedPanel();
            buttonPanel.setLayout(new BorderLayout());
            buttonPanel.setBackground(new Color(125,216,255));

            RoundedButton openButton = new RoundedButton();
            openButton.setText("OPEN");
            openButton.setFont(poppins);
            openButton.setForeground(Color.WHITE);
            openButton.setCornerRadius(45);
            openButton.setPreferredSize(new Dimension(100,80));
            openButton.setMaximumSize(new Dimension(50,80));
            openButton.setFocusable(false);
            openButton.setBackground(Color.WHITE);
            openButton.setFont(lexend);
            openButton.addActionListener(e -> {
                System.out.println("File Name: " + fileName);
                Journalizing journalizing = new Journalizing(fileName);
                journalizing.setVisible(false);
                Ledger.makeLedgers(Ledger.getUnAdjustedEntries(), Ledger.getUnAdjustedAccounts());
                new LedgersFrame(Ledger.getUnAdjustedAccounts(), journalizing);
                SavedJournals_frame.dispose();
            });

            JLabel filename = new JLabel(shortenFileName);
            filename.setFont(lexend);
            filename.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 30));
            filename.setForeground(Color.BLACK);

            buttonPanel.add(openButton, BorderLayout.WEST);

            files.add(buttonPanel, BorderLayout.EAST);
            files.add(filename, BorderLayout.WEST);

            Files_panel.add(files);
            Files_panel.add(Box.createVerticalStrut(10));
        }

        center.add(SavedJournals_label, BorderLayout.NORTH);
        center.add(scrollPane, BorderLayout.CENTER);
        
        
        SavedJournals_frame.add(header, BorderLayout.NORTH);
        SavedJournals_frame.add(center, BorderLayout.CENTER);

        SavedJournals_frame.repaint();
        SavedJournals_frame.revalidate();

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

   public static ArrayList<String[]> loadFiles(String fileName) {
        ArrayList<String[]> allDetails = new ArrayList<>();
        String path = GetPath.getCsvPath() + fileName;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allDetails.add(line.split(",")); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allDetails;
    }


}
  

