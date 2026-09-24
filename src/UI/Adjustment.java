package UI;


import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Backend.JournalEntry;
import Backend.Ledger;


public class Adjustment extends JPanel{

    private DefaultTableModel model;
    private EntryForm form = new EntryForm("Add Journal Entry", "Input transaction details below", "Finish & Save Journal");
    private double initialCapital = 0;
    private double freightOut = 0;

    public Adjustment(Journalizing journalizing){

        this.model = journalizing.getModel();

        form.getAddButton().addActionListener(e -> {
            String error = form.validateInput();
            if (error != null) {
                JOptionPane.showMessageDialog(this, error);
                return;
            }
            JTextField debitField = form.debitInput();
            JTextField creditField = form.creditInput();
            if (!journalizing.checkTitle(debitField) || !journalizing.checkTitle(creditField)) {
                JOptionPane.showMessageDialog(this, "Error Account Titles");
                return;
            }
            journalizing.getModel().addRow(addtransaction(debitField, creditField, form.getDateField(), form.getNoteField()));
            form.clear();
        });

        form.getFinishButton().addActionListener(e -> {
          Ledger.clearAccounts();
          Ledger.makeLedgers(Ledger.getEntries(), Ledger.getAccounts());
          AppFrame.setDoneAdjustment(1);
          Ledger.writeCsvFile(SavingJournals.getFileName());
          Ledger.writeFormattedFile(SavingJournals.getFileName());
          AppFrame.get().markSaved();
          JOptionPane.showMessageDialog(this, "Adjusting entries have been posted.");
        });

        setOpaque(false);
        setLayout(new BorderLayout(24, 0));
        setBorder(Theme.pagePad());
        add(new JournalGrid(model).toCard(), BorderLayout.CENTER);
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

        if(debit[0].equals("Capital") || credit[0].equals("Capital")){
            if(initialCapital == 0){
                initialCapital = Double.parseDouble(credit[1]);
            }
        }

        if (credit[0].equals("Sales")) {
            COGSandFO cogsFrame = new COGSandFO(this);
            cogsFrame.setVisible(true);

            String cogsNotes = "To record cost of goods sold";

            String cogsParticulars = "Cost of Goods Sold\n   Inventories\n      " + cogsNotes;
            double amount = COGSandFO.getCogs();
            freightOut += COGSandFO.getCogs();

            JournalEntry cogsEntry = new JournalEntry(date, "Cost of Goods Sold", "Inventories", amount, amount, cogsNotes);
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
}
