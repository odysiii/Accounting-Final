package UI;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;


public class ViewSavedJournals extends JPanel {

    public ViewSavedJournals() {

        setOpaque(false);
        setLayout(new BorderLayout());

        JPanel Files_panel = new ScrollPanel(new ScrollPanel.WrapLayout(FlowLayout.LEFT, 24, 24));
        Files_panel.setBorder(BorderFactory.createEmptyBorder(24, 36, 24, 36));

        JScrollPane scrollPane = new JScrollPane(Files_panel);
        Theme.slim(scrollPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        File[] csvFiles = new File(GetPath.getCsvPath()).listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        if (csvFiles == null) {
            csvFiles = new File[0];
        }
        Arrays.sort(csvFiles);

        for (File csvFile : csvFiles) {

            String fileName = csvFile.getName();
            String shortenFileName = fileName.replace(".txt", "");
            Files_panel.add(makeCard(fileName, shortenFileName));
        }

        if (csvFiles.length == 0) {
            JLabel empty = new JLabel("No saved journals yet.");
            empty.setFont(Theme.inter(Font.PLAIN, 16f));
            empty.setForeground(Theme.GRAY_TEXT);
            Files_panel.add(empty);
        }

        add(scrollPane, BorderLayout.CENTER);
    }

    private RoundedPanel makeCard(String fileName, String shortenFileName) {
        int shadow = 10;

        RoundedPanel files = new RoundedPanel();
        files.setLayout(new BorderLayout());
        files.setBackground(Color.WHITE);
        files.setCornerRadius(34);
        files.setShadowSize(shadow);
        files.setAccent(Theme.BLUE, 9);
        files.setAccentSide(RoundedPanel.LEFT);
        files.setClipChildren(true);
        files.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 16));
        files.setPreferredSize(new Dimension(270 + 2 * shadow, 160 + 2 * shadow));

        RoundedPanel iconBox = new RoundedPanel();
        iconBox.setBackground(new Color(0xC9DBF6));
        iconBox.setCornerRadius(16);
        iconBox.setLayout(new GridBagLayout());
        iconBox.setPreferredSize(new Dimension(44, 44));
        iconBox.add(new JLabel(Icons.get(Icons.Type.BUILDING, 26, new Color(0x8FB0E8))));

        RoundedButton deleteButton = new RoundedButton();
        deleteButton.setIcon(Icons.get(Icons.Type.TRASH, 16, Color.WHITE));
        deleteButton.setCornerRadius(32);
        deleteButton.setPreferredSize(new Dimension(32, 32));
        Theme.stylePrimary(deleteButton);
        deleteButton.addActionListener(e -> {
            int answer = JOptionPane.showConfirmDialog(this, "Delete \"" + shortenFileName + "\"? This cannot be undone.",
                    "Delete Journal", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (answer == JOptionPane.YES_OPTION) {
                new File(GetPath.getCsvPath() + fileName).delete();
                new File(GetPath.getFormatPath() + fileName).delete();
                AppFrame.get().refreshSaved();
            }
        });

        JPanel deleteHolder = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        deleteHolder.setOpaque(false);
        deleteHolder.add(deleteButton);

        JPanel iconHolder = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        iconHolder.setOpaque(false);
        iconHolder.add(iconBox);

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.add(iconHolder, BorderLayout.WEST);
        topRow.add(deleteHolder, BorderLayout.EAST);
        topRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        topRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        JLabel filename = new JLabel(shortenFileName);
        filename.setFont(Theme.interWeight("SemiBold", 15f));
        filename.setForeground(Theme.INK);
        filename.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedButton openButton = new RoundedButton();
        openButton.setText("Open Journal");
        openButton.setIcon(Icons.get(Icons.Type.ARROW, 16, Color.WHITE));
        openButton.setHorizontalTextPosition(SwingConstants.LEFT);
        openButton.setIconTextGap(8);
        openButton.setFont(Theme.inter(Font.BOLD, 12f));
        openButton.setCornerRadius(12);
        Theme.stylePrimary(openButton);
        openButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        openButton.setPreferredSize(new Dimension(0, 34));
        openButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        openButton.addActionListener(e -> {
            System.out.println("File Name: " + fileName);
            AppFrame.get().openSavedJournal(fileName);
        });

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(topRow);
        content.add(Box.createVerticalStrut(10));
        content.add(filename);
        content.add(Box.createVerticalGlue());
        content.add(openButton);
        files.add(content, BorderLayout.CENTER);

        return files;
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
