package UI;


import java.awt.*;
import java.io.File;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Backend.JournalEntry;
import Backend.Ledger;


public class Adjustment extends TabFrame{

    private static Font poppins = loadFont(GetPath.getPath() + "Poppins-Medium.ttf", 15f);
    private static Font poppins1 = loadFont(GetPath.getPath() + "Poppins-SemiBold.ttf", 15f);
    JTable journalTable = new JTable();
    private static Border border = BorderFactory.createLineBorder(Color.gray, 1);

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

    private DefaultTableModel model;
    private RoundedButton finishButton = new RoundedButton();
    private double initialCapital = 0;
    private double freightOut = 0;
    
    @SuppressWarnings("unused")
    public Adjustment(Journalizing journalizing){
        super("Journalizing", journalizing);

        this.model = journalizing.getModel();
        
        for (JButton button : this.getAllButtons()) {
          button.addActionListener(e -> this.dispose());
        }
        this.removeActionListener(getAdjustmentsTab());

        JPanel addJournal = new JPanel();
        addJournal.setBackground(new Color(0x7DD8FF));
        addJournal.setPreferredSize(new Dimension(350,0));
        addJournal.setLayout(new BoxLayout(addJournal, BoxLayout.Y_AXIS));

            JPanel inputDate = new JPanel();
            
            inputDate.setMaximumSize(new Dimension(280,120));
            inputDate.setOpaque(false);
            inputDate.setLayout(new FlowLayout());

                JLabel date = new JLabel("Date");
                date.setFont(poppins);
                date.setOpaque(false);
                date.setPreferredSize(new Dimension(280, 30));
                
                RoundedTextField dateField = new RoundedTextField();
                dateField.setPreferredSize(new Dimension(280,50));
                dateField.setCornerRadius(15);

                inputDate.add(date);
                inputDate.add(dateField);

            JPanel inputDebit = new JPanel();
            inputDebit.setOpaque(false);
            inputDebit.setMaximumSize(new Dimension(280,120));
            inputDebit.setLayout(new FlowLayout());

                JLabel debit = new JLabel("Debit to : Amount");
                debit.setFont(poppins);
                debit.setOpaque(false);
                debit.setPreferredSize(new Dimension(280, 30));
                
                RoundedTextField debitField = new RoundedTextField();
                debitField.setPreferredSize(new Dimension(280,50));
                debitField.setCornerRadius(15);

                inputDebit.add(debit);
                inputDebit.add(debitField);

            JPanel inputCredit = new JPanel();
            inputCredit.setOpaque(false);
            inputCredit.setMaximumSize(new Dimension(280,120));
            inputCredit.setLayout(new FlowLayout());

                JLabel credit = new JLabel("Credit to : Amount");
                credit.setFont(poppins);
                credit.setOpaque(false);
                credit.setPreferredSize(new Dimension(280, 30));
                
                RoundedTextField creditField = new RoundedTextField();
                creditField.setPreferredSize(new Dimension(280,50));
                creditField.setCornerRadius(15);

                inputCredit.add(credit);
                inputCredit.add(creditField);


            JPanel inputNote = new JPanel();
            inputNote.setOpaque(false);
            inputNote.setMaximumSize(new Dimension(280,120));
            inputNote.setLayout(new FlowLayout());

                JLabel note = new JLabel("Note");
                note.setFont(poppins);
                note.setOpaque(false);
                note.setPreferredSize(new Dimension(280, 30));
                
                RoundedTextField noteField = new RoundedTextField();
                noteField.setPreferredSize(new Dimension(280,50));
                noteField.setCornerRadius(15);

                inputNote.add(note);
                inputNote.add(noteField);

            RoundedButton addButton = new RoundedButton();
            addButton.setText("Add");
            addButton.setCornerRadius(50);
            addButton.setFont(poppins);
            addButton.setMaximumSize(new Dimension(150, 50));
            addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            addButton.setFocusable(false);
            addButton.setForeground(Color.white);
            addButton.setBackground(Color.white);
            addButton.addActionListener(e -> journalizing.getModel().addRow(addtransaction(debitField, creditField, dateField, noteField)));

            finishButton.setText("Finish");
            finishButton.setCornerRadius(50);
            finishButton.setFont(poppins);
            finishButton.setMaximumSize(new Dimension(150, 50));
            finishButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            finishButton.setFocusable(false);
            finishButton.setForeground(Color.white);
            finishButton.setBackground(Color.white);
            finishButton.addActionListener(e -> {
              Ledger.clearAccounts();
              Ledger.makeLedgers(Ledger.getEntries(), Ledger.getAccounts());
              TabFrame.setDoneAdjustment(1);
              Ledger.writeCsvFile(SavingJournals.getFileName());
              Ledger.writeFormattedFile(SavingJournals.getFileName());
            });

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
        addJournal.add(Box.createVerticalStrut(60));
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

        if(debit[0].equals("Capital") || credit[0].equals("Capital")){
            if(initialCapital == 0){
                initialCapital = Double.parseDouble(credit[1]);
            }
        }

        if (credit[0].equals("Sales")) {
            COGSandFO cogsFrame = new COGSandFO(this);
            cogsFrame.setVisible(true);

            String cogsNotes = "To record cost of goods sold";

            String cogsParticulars = "Cost of Goods Sold\n   Inventory\n      " + cogsNotes;
            double amount = COGSandFO.getCogs();
            freightOut += COGSandFO.getCogs();

            JournalEntry cogsEntry = new JournalEntry(date, "Cost of Goods Sold", "Inventory", amount, amount, cogsNotes);
            Ledger.addEntry(cogsEntry);

            cogsTransaction[0] = date;
            cogsTransaction[1] = cogsParticulars;
            cogsTransaction[2] = String.valueOf(amount);
            cogsTransaction[3] = String.valueOf(amount);

            model.addRow(cogsTransaction);

        }

        JournalEntry entry = new JournalEntry(date, debit[0], credit[0], debitAmount, creditAmount, note);

        Ledger.addEntry(entry);

        return transaction;
    }

    public double getInitialCapital(){
        return initialCapital;
    }
    public double getFreightOut() {
      return freightOut;
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
}
