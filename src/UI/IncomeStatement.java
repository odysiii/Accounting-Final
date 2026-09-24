package UI;

import java.awt.*;
import java.util.HashMap;

import Backend.AccountTitle;
import Backend.Ledger;

public class IncomeStatement extends StatementView {

    private static double netIncome;
    private static double netLoss = 0;
    public IncomeStatement(Journalizing journalizing){

        super("Income Statement", "STATEMENT OF PROFIT OR LOSS", "For the year ended " + CompanyName.getYearEndDate());

        HashMap<String, AccountTitle> accounts = Ledger.getAccounts();

            //puro text na
            double sales = 0;
            for (AccountTitle accountTitle : accounts.values()) {
                if (accountTitle.getElement().equals("Income")) {
                    if (accountTitle.getTitle().equals("Sales Returns and Allowances"))
                        continue;
                    body.add(makeAccTitle(accountTitle, 80));
                    sales += accountTitle.computeEndingBal();
                }
            }

            double totalCogs = 0;
            for (AccountTitle accountTitle : accounts.values()) {
                    if (accountTitle.getTitle().equals("Cost of Goods Sold")) {
                        totalCogs += accountTitle.computeEndingBal();
                    }
                }
            body.add(makeTotal("Less Cost of Goods Sold", totalCogs, 60));

            double SRA = 0;
            for (AccountTitle accountTitle : accounts.values()) {
                    if (accountTitle.getTitle().equals("Sales Returns and Allowances")) {
                        SRA += accountTitle.computeEndingBal();
                    }
                }
            body.add(makeTotal("Less Sales Returns and Allowances", SRA, 60));

            body.add(makeTotal("Less Sales Discounts", journalizing.getDiscount(), 60));

            double grossProfit = sales - totalCogs - SRA - journalizing.getDiscount();
            body.add(makeTotal("Gross Profit", grossProfit, 60));

            body.add(makeHeader("Less: Operating Expense", Font.PLAIN, 18, 60));

                body.add(makeTotal("Freight-out", journalizing.getFreightOut(), 200));

                body.add(makeTotal("Freight-In", journalizing.getFreightIn(), 200));

                double expense = 0;
                for (AccountTitle accountTitle : accounts.values()) {
                    if (accountTitle.getElement().equals("Expense")) {
                        body.add(makeAccTitle(accountTitle, 200));
                        expense += accountTitle.computeEndingBal();
                    }
                }
                netIncome = grossProfit - expense - journalizing.getFreightIn() - journalizing.getFreightOut();
                if (grossProfit < expense) {
                    netLoss = grossProfit - expense;
                }

            body.add(makeGap(12));
            body.add(makeTotal("NET INCOME / NET LOSS", netIncome, 40));
    }

    public static double getNetIncome() {
      return netIncome;
    }

    public static double getNetLoss() {
      return netLoss;
    }

    public static void reset() {
      netIncome = 0;
      netLoss = 0;
    }
}
