package UI;

import java.awt.*;
import javax.swing.*;


public class Journal extends JPanel{

  public Journal(Journalizing journalizing){

    setOpaque(false);
    setLayout(new BorderLayout());
    setBorder(Theme.pagePad());

    add(new JournalGrid(journalizing.getModel()).toCard(), BorderLayout.CENTER);

  }
}
