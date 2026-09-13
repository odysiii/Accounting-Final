package UI;
import java.awt.*;
import java.io.File;

import javax.swing.*;
import javax.swing.border.Border;

public class HomeFrame extends Frame {

    private static Font lexend = loadFont(GetPath.getPath() + "Lexend-VariableFont_wght.ttf", 30f);
    private static Font poppins = loadFont(GetPath.getPath() + "Poppins-ExtraBold.ttf", 100f);

    private static int done = 0;
    @SuppressWarnings("unused")
    public HomeFrame() {

        super("Home");
        Border border = BorderFactory.createLineBorder(Color.gray, 2);


        JPanel header = new JPanel();
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(border);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(new Color(0x7DD8FF));
        
        JLabel title = new JLabel("ACCOUNTING CYCLE");
        title.setFont(poppins);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.black);


        RoundedButton createNewJournal = new RoundedButton();
        createNewJournal.setFillOriginal(new Color(0x42A5F5));   //orig
        createNewJournal.setFillOver(new Color(0x1E88E5));    //hover   
        createNewJournal.setFillClick(new Color(0xBBDEFB));  //click 
        createNewJournal.setCornerRadius(40);
        createNewJournal.setText("Create New Journal");
        createNewJournal.setFont(lexend);
        createNewJournal.setMaximumSize(new Dimension(700, 100));
        createNewJournal.setAlignmentX(Component.CENTER_ALIGNMENT);
        createNewJournal.setBackground(Color.WHITE);
        createNewJournal.setForeground(Color.black);
        createNewJournal.setFocusable(false);
        createNewJournal.addActionListener(e -> {
            new CompanyName();
        });

        RoundedButton viewSavedJournals = new RoundedButton();
        viewSavedJournals.setFillOriginal(new Color(0x42A5F5));   //orig
        viewSavedJournals.setFillOver(new Color(0x1E88E5));    //hover   
        viewSavedJournals.setFillClick(new Color(0xBBDEFB));  //click 
        viewSavedJournals.setCornerRadius(40);
        viewSavedJournals.setText("View Saved Journals");
        viewSavedJournals.setFont(lexend);
        viewSavedJournals.setMaximumSize(new Dimension(700, 100));
        viewSavedJournals.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewSavedJournals.setBackground(Color.WHITE);
        viewSavedJournals.setForeground(Color.black);
        viewSavedJournals.setFocusable(false);
        viewSavedJournals.addActionListener(e -> {
            new ViewSavedJournals(); 
            this.dispose();
        });

        center.add(Box.createVerticalStrut(60));
        center.add(title);
        center.add(Box.createVerticalStrut(40));
        center.add(createNewJournal);
        center.add(Box.createVerticalStrut(40));
        center.add(viewSavedJournals);

        this.add(header, BorderLayout.NORTH);
        this.add(center, BorderLayout.CENTER);

        this.repaint();
        this.revalidate();
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

    public static int getDone() {
      return done;
    }


}
