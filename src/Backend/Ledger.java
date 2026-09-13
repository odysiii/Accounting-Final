package Backend;
import java.io.*;
import java.util.*;

import UI.GetPath;


public class Ledger {

    @SuppressWarnings("unused")
    private static ArrayList<JournalEntry> Entries = new ArrayList<>();
    private static ArrayList<JournalEntry> unAdjustedEntries = new ArrayList<>();
    private static HashMap<String,  AccountTitle> accounts = new HashMap<>();
    private static HashMap<String,  AccountTitle> unAdjustedAccounts = new HashMap<>();

    @SuppressWarnings("unused")

    public static void makeLedgers(ArrayList<JournalEntry> Entries, HashMap<String,  AccountTitle> accounts){
        for (JournalEntry entry : Entries) {
            String debitTitle = entry.getDebitAccountTitle();
            AccountTitle debitAccount = accounts.computeIfAbsent(debitTitle, AccountTitle::new);
            debitAccount.setDebitValue(entry.getDebitValue());

            String creditTitle = entry.getCreditAccountTitle();
            AccountTitle creditAccount = accounts.computeIfAbsent(creditTitle, AccountTitle::new);
            creditAccount.setCreditValue(entry.getCreditValue());
        }   

        for (AccountTitle account : accounts.values()) {
            System.out.println(account.getTitle());
            System.out.println("Debit: " + account.getDebitValues());
            System.out.println("Credit: " + account.getCreditValues());
            System.out.println("Ending Balance: " + account.computeEndingBal());
            System.out.println();
        }
    }

    public void writeCsvFile(String fileName, JournalEntry journalEntry){
        String path = "D:/Personal Codes/Java/Accounting System Backend/csvFiles/";
        fileName += ".txt";
        try {
            FileWriter writer = new FileWriter(path + fileName, true);
            writer.write(journalEntry.toCsv() + "\n");
            writer.close();
        } catch (Exception e) {
            System.out.println("File not found");
        }
    }

    public static HashMap<String, AccountTitle> getAccounts() {
      return accounts;
    }
    public static ArrayList<JournalEntry> getEntries() {
      return Entries;
    }
    public static HashMap<String, AccountTitle> getUnAdjustedAccounts() {
      return unAdjustedAccounts;
    }
    public static ArrayList<JournalEntry> getUnAdjustedEntries() {
      return unAdjustedEntries;
    }

    public static void clearAccounts(){
        accounts.clear();
    }
    public static void addEntry(JournalEntry entry){
        Entries.add(entry);
    }
    public static void addUnadjustedEntries(JournalEntry entry){
        unAdjustedEntries.add(entry);
    }

    public static void writeCsvFile(String filename){
        String path = GetPath.getCsvPath() + "\\";
        String fileName = filename + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path + fileName))) {
            for (JournalEntry entry : Entries) {
                writer.write(entry.toCsv());
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        System.out.println("Csv file writing compeleted");
    }
    
    public static void writeFormattedFile(String filename){
        String path = GetPath.getFormatPath();
        filename += ".txt";
        try {
            FileWriter writer = new FileWriter(path + filename);
            writer.write(String.format("%-10s %-50s %15s %15s%n",
                    "DATE", "ACCOUNT TITLES", "DEBIT", "CREDIT"));
            writer.write("---------------------------------------------------------------------------------------------\n");
            
            for (JournalEntry entry : Entries) {
                
                writer.write(String.format("%-10s %-50s %15.2f %15s%n",
                        entry.getDate(), entry.getDebitAccountTitle(), entry.getDebitValue(), ""));

                writer.write(String.format("%-10s %-50s %15s %15.2f%n",
                        "", "     " + entry.getCreditAccountTitle(), "", entry.getCreditValue()));

                writer.write(String.format("%-10s %-50s%n%n", "", "          " + entry.getNote()));
            }

            writer.close();
            System.out.println("Journal successfully saved in files.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
