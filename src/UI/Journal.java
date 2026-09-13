package UI;

import java.awt.*;
import javax.swing.*;


public class Journal extends TabFrame{

  public Journal(Journalizing journalizing){

    super("Journal", journalizing);
    for (JButton button : this.getAllButtons()) {
      button.addActionListener(e -> this.dispose());
    }
    this.removeActionListener(getJournalTab());

    JScrollPane scrollPane = new JScrollPane(journalizing.getJournalTable());

    add(scrollPane, BorderLayout.CENTER);
    
  }
}
