package UI;


import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import Backend.JournalEntry;
import Backend.Ledger;


public class Journalizing extends Frame{

    private static Font poppins = loadFont(GetPath.getPath() + "Poppins-Medium.ttf", 15f);
    private static Font poppins1 = loadFont(GetPath.getPath() + "Poppins-SemiBold.ttf", 15f);
    private static Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
    private DefaultTableModel model;
    private JTable journalTable = new JTable();
    private RoundedButton finishButton = new RoundedButton();
    private double initialCapital = 0;
    private double freightOut = 0;
    private double freightIn = 0;
    private double discount = 0;
    
    @SuppressWarnings("unused")
    public Journalizing(String fileName){

        super("Journalizing");

        JPanel header = new JPanel();
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(border);

        String[] col = {"Date", "Particulars", "Debit", "Credit"};
        //String[] row = {"Jan 1", "Cash\n   Capital\n      To record Initial Investment", "1,000,000", "\n1,000,000"};

        model = new DefaultTableModel(col, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        if (fileName != null) {
            System.out.println("File Name: " + fileName);
            ArrayList<String[]> details = ViewSavedJournals.loadFiles(fileName);
            String[] transaction = new String[4];
            for (String[] string : details) {

                if(string[3].equals("Capital")){
                    if(initialCapital == 0){
                        initialCapital = Double.parseDouble(string[4]);
                    }
                }
                transaction[0] = string[0];
                transaction[1] = string[1] + "\n   " + string[3] + "\n      " + string[5];
                transaction[2] = string[2];
                transaction[3] = string[4];
                model.addRow(transaction);
                JournalEntry entry = new JournalEntry(string[0], string[1], string[3], Double.valueOf(string[2]), Double.valueOf(string[4]), string[5]);
                Ledger.addUnadjustedEntries(entry);
                Ledger.addEntry(entry);
            }
        }

        JPanel addJournal = new JPanel();
        addJournal.setBackground(new Color(0x7DD8FF));
        addJournal.setPreferredSize(new Dimension(350,0));
        addJournal.setLayout(new BoxLayout(addJournal, BoxLayout.Y_AXIS));

            JPanel inputDate = new JPanel();
            
            inputDate.setMaximumSize(new Dimension(280,140));
            inputDate.setOpaque(false);
            inputDate.setLayout(new FlowLayout());

                JLabel date = new JLabel("Date");
                date.setFont(poppins);
                date.setOpaque(false);
                date.setPreferredSize(new Dimension(280, 30));
                date.setForeground(Color.black);
                
                RoundedTextField dateField = new RoundedTextField();
                dateField.setPreferredSize(new Dimension(280,50));
                dateField.setCornerRadius(15);

                inputDate.add(date);
                inputDate.add(dateField);

            JPanel inputDebit = new JPanel();
            inputDebit.setOpaque(false);
            inputDebit.setMaximumSize(new Dimension(280,140));
            inputDebit.setLayout(new FlowLayout());

                JLabel debit = new JLabel("Debit to : Amount");
                debit.setFont(poppins);
                debit.setOpaque(false);
                debit.setPreferredSize(new Dimension(280, 30));
                debit.setForeground(Color.black);
                
                RoundedTextField debitField = new RoundedTextField();
                debitField.setPreferredSize(new Dimension(280,50));
                debitField.setCornerRadius(15);

                inputDebit.add(debit);
                inputDebit.add(debitField);

            JPanel inputCredit = new JPanel();
            inputCredit.setOpaque(false);
            inputCredit.setMaximumSize(new Dimension(280,140));
            inputCredit.setLayout(new FlowLayout());

                JLabel credit = new JLabel("Credit to : Amount");
                credit.setFont(poppins);
                credit.setOpaque(false);
                credit.setPreferredSize(new Dimension(280, 30));
                credit.setForeground(Color.black);
                
                RoundedTextField creditField = new RoundedTextField();
                creditField.setPreferredSize(new Dimension(280,50));
                creditField.setCornerRadius(15);

                inputCredit.add(credit);
                inputCredit.add(creditField);


            JPanel inputNote = new JPanel();
            inputNote.setOpaque(false);
            inputNote.setMaximumSize(new Dimension(280,140));
            inputNote.setLayout(new FlowLayout());

                JLabel note = new JLabel("Note");
                note.setFont(poppins);
                note.setOpaque(false);
                note.setPreferredSize(new Dimension(280, 30));
                note.setForeground(Color.black);
                
                RoundedTextField noteField = new RoundedTextField();
                noteField.setPreferredSize(new Dimension(280,50));
                noteField.setCornerRadius(15);

                inputNote.add(note);
                inputNote.add(noteField);

            RoundedButton addButton = new RoundedButton();
            addButton.setCornerRadius(50);
            addButton.setText("Add");
            addButton.setFont(poppins);
            addButton.setMaximumSize(new Dimension(150, 50));
            addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            addButton.setFocusable(false);
            addButton.setForeground(Color.white);
            addButton.setBackground(Color.white);
            addButton.addActionListener(e -> {
                if (checkTitle(debitField) && checkTitle(creditField)) {
                    model.addRow(addtransaction(debitField, creditField, dateField, noteField));
                }else
                    JOptionPane.showMessageDialog(this, "Error Account Titles");
                dateField.setText("");
                debitField.setText("");
                creditField.setText("");
                noteField.setText("");
            });
            
 
            finishButton.setText("Finish");
            finishButton.setFont(poppins);
            finishButton.setCornerRadius(50);
            finishButton.setMaximumSize(new Dimension(150, 50));
            finishButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            finishButton.setFocusable(false);
            finishButton.setBackground(Color.white);
            finishButton.setForeground(Color.white);
            finishButton.addActionListener(e -> new SavingJournals(Ledger.getUnAdjustedEntries(), this));

        addJournal.add(Box.createVerticalStrut(20));
        addJournal.add(inputDate);
        addJournal.add(Box.createVerticalStrut(20));
        addJournal.add(inputDebit);
        addJournal.add(Box.createVerticalStrut(20));
        addJournal.add(inputCredit);
        addJournal.add(Box.createVerticalStrut(20));
        addJournal.add(inputNote);
        addJournal.add(Box.createVerticalStrut(40));
        addJournal.add(addButton);
        addJournal.add(Box.createVerticalStrut(40));
        addJournal.add(finishButton);

        journalTable.setModel(model);
        formatTable();

        journalTable.setRowSelectionAllowed(false);
        journalTable.setColumnSelectionAllowed(false);
        journalTable.setCellSelectionEnabled(false);

        journalTable.setRowHeight(75);

        myRenderer dateRenderer = new myRenderer();
        dateRenderer.setVerticalAlignment(SwingConstants.CENTER);
        dateRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        myRenderer particulars = new myRenderer();
        particulars.setVerticalAlignment(SwingConstants.TOP);
        particulars.setHorizontalAlignment(SwingConstants.LEFT);

        myRenderer debitAlign = new myRenderer();
        debitAlign.setVerticalAlignment(SwingConstants.TOP);
        debitAlign.setHorizontalAlignment(SwingConstants.RIGHT);

        myRenderer creditAlign = new myRenderer();
        creditAlign.setVerticalAlignment(SwingConstants.BOTTOM);
        creditAlign.setHorizontalAlignment(SwingConstants.RIGHT);

        journalTable.getColumnModel().getColumn(0).setCellRenderer(dateRenderer);
        journalTable.getColumnModel().getColumn(1).setCellRenderer(particulars);
        journalTable.getColumnModel().getColumn(2).setCellRenderer(debitAlign);
        journalTable.getColumnModel().getColumn(3).setCellRenderer(creditAlign);

        
        JScrollPane scrollPane = new JScrollPane(journalTable);

        this.add(header, BorderLayout.NORTH);
        this.add(addJournal, BorderLayout.EAST);
        this.add(scrollPane, BorderLayout.CENTER);

        this.repaint();
        this.revalidate();
    }

    public String[] addtransaction(JTextField debitField, JTextField creditField, JTextField dateField, JTextField notField){
        
        String[] transaction = new String[4];
        String[] cogsTransaction = new String[4];

        String date = dateField.getText();
        String[] debit = debitField.getText().split(":");
        String[] credit = creditField.getText().split(":");
        String note = notField.getText();

        String particulars = debit[0] + "\n" + "   " + credit[0] + "\n" + "       " + note;

        transaction[0] = date;
        transaction[1] = particulars;
        transaction[2] = debit[1];
        transaction[3] = credit[1];

        double debitAmount = Double.valueOf(debit[1]);
        double creditAmount = Double.valueOf(credit[1]);

        if(credit[0].equals("Capital")){
            if(initialCapital == 0){
                initialCapital = Double.parseDouble(credit[1]);
            }
        }

        if (credit[0].equals("Sales")) {
            COGSandFO cogsFrame = new COGSandFO(this);
            cogsFrame.setVisible(true);

            discount += Double.parseDouble(JOptionPane.showInputDialog("Enter discount amount: "));

            String cogsNotes = "To record cost of goods sold";

            String cogsParticulars = "Cost of Goods Sold\n   Inventory\n      " + cogsNotes;
            double amount = COGSandFO.getCogs();
            freightOut += COGSandFO.getFreightOut();

            JournalEntry cogsEntry = new JournalEntry(date, "Cost of Goods Sold", "Inventories", amount, amount, cogsNotes);
            Ledger.addUnadjustedEntries(cogsEntry);
            Ledger.addEntry(cogsEntry);

            cogsTransaction[0] = date;
            cogsTransaction[1] = cogsParticulars;
            cogsTransaction[2] = String.valueOf(amount);
            cogsTransaction[3] = String.valueOf(amount);

            model.addRow(cogsTransaction);
        }

        if (debit[0].equals("Inventories")) {
            freightIn += Double.parseDouble(JOptionPane.showInputDialog("Enter Freight In Amount: "));
        }

        JournalEntry entry = new JournalEntry(date, debit[0], credit[0], debitAmount, creditAmount, note);
        Ledger.addUnadjustedEntries(entry);
        Ledger.addEntry(entry);

        return transaction;
    }

    public JTable getJournalTable() {
      return journalTable;
    }
    public DefaultTableModel getModel() {
      return model;
    }
    public double getInitialCapital(){
        return initialCapital;
    }
    public double getFreightOut() {
      return freightOut;
    }
    public double getFreightIn() {
      return freightIn;
    }
    public double getDiscount() {
      return discount;
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

    public void formatTable(){
        journalTable.getColumnModel().getColumn(0).setHeaderRenderer(headerRenderer(getBackground()));
        journalTable.getColumnModel().getColumn(1).setHeaderRenderer(headerRenderer(getBackground()));
        journalTable.getColumnModel().getColumn(2).setHeaderRenderer(headerRenderer(new Color(144, 238, 144)));
        journalTable.getColumnModel().getColumn(3).setHeaderRenderer(headerRenderer(new Color(240, 128, 128)));
    }

    public DefaultTableCellRenderer headerRenderer(Color color) {
        DefaultTableCellRenderer header =  new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(color);
                lbl.setBorder(border);
                lbl.setFont(poppins1);
                lbl.setForeground(Color.BLACK); 
                lbl.setHorizontalAlignment(JLabel.CENTER);
                lbl.setOpaque(true);
                return lbl;
            }
        };
        header.setPreferredSize(new Dimension(header.getWidth(), 50));
        return header;
    }

    public boolean checkTitle(JTextField field){

        String text = field.getText();
        String[] title = text.split(":");
        
        String path = GetPath.getElementPath();
        String[] files = {"CurrentAssets", "NonCurrentAssets", "CurrentLiabilities", 
                          "NonCurrentLiabilities", "Equities", "Expenses", "Income"};

        for (String file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(path + file + ".txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.equals(title[0])) {
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}




