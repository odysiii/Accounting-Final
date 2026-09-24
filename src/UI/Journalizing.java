package UI;


import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Backend.JournalEntry;
import Backend.Ledger;


public class Journalizing extends JPanel{

    private DefaultTableModel model;
    private JournalGrid journalTable;
    private EntryForm form = new EntryForm("Add Journal Entry", "Input transaction details below", "Finish & Save Journal");
    private double initialCapital = 0;
    private double freightOut = 0;
    private double freightIn = 0;
    private double discount = 0;

    public Journalizing(String fileName){

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

        journalTable = new JournalGrid(model);

        form.getAddButton().addActionListener(e -> {
            String error = form.validateInput();
            if (error != null) {
                JOptionPane.showMessageDialog(this, error);
                return;
            }
            JTextField debitField = form.debitInput();
            JTextField creditField = form.creditInput();
            if (checkTitle(debitField) && checkTitle(creditField)) {
                model.addRow(addtransaction(debitField, creditField, form.getDateField(), form.getNoteField()));
                form.clear();
            }else
                JOptionPane.showMessageDialog(this, "Error Account Titles");
        });

        form.getFinishButton().addActionListener(e -> new SavingJournals(Ledger.getUnAdjustedEntries(), this).setVisible(true));

        setOpaque(false);
        setLayout(new BorderLayout(24, 0));
        setBorder(Theme.pagePad());
        add(journalTable.toCard(), BorderLayout.CENTER);
        add(form, BorderLayout.EAST);
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

            discount += AmountPrompt.ask(this, "Sales Discount", "Input Discount Amount");

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
            freightIn += AmountPrompt.ask(this, "Freight In", "Input Freight In Amount");
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
