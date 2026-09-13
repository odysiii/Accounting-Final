package UI;
import java.awt.BorderLayout;

import javax.swing.*;



public class Frame extends JFrame {

    public Frame(String title){

        this.setLayout(new BorderLayout());
        this.setTitle(title);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

}
