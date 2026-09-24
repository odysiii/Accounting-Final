package UI;

import java.awt.*;
import java.util.HashMap;

import Backend.AccountTitle;
import Backend.Ledger;

public class BalanceSheet extends StatementView {

    public BalanceSheet(Journalizing journalizing){

        super("Balance Sheet", "STATEMENT OF FINANCIAL POSITION", "As of " + CompanyName.getAsOfDate());

        HashMap<String,  AccountTitle> accounts = Ledger.getAccounts();

                //Assets
                double totalAssets = 0;
                body.add(makeHeader("ASSETS", Font.BOLD, 19, 60));
                body.add(makeGap(4));
                    //Current Assets
                    body.add(makeHeader("Current", Font.PLAIN, 18, 80));
                    for (AccountTitle accountTitle : accounts.values()) {
                        if (accountTitle.getElement().equals("Current Asset")) {
                            body.add(makeAccTitle(accountTitle, 100));
                            totalAssets += accountTitle.computeEndingBal();
                        }
                    }
                    //Non Current Assets
                    body.add(makeHeader("Non Current", Font.PLAIN, 18, 80));
                    for (AccountTitle accountTitle : accounts.values()) {
                        if (accountTitle.getElement().equals("Non Current Asset")) {
                            body.add(makeAccTitle(accountTitle, 100));
                            totalAssets += accountTitle.computeEndingBal();
                        }
                    }
                    body.add(makeTotal("TOTAL ASSETS", totalAssets, 60));
                    body.add(makeGap(18));

                //LIABILITIES AND OWNER'S EQUITY
                body.add(makeHeader("LIABILITIES AND OWNER'S EQUITY", Font.BOLD, 19, 60));
                body.add(makeGap(4));
                    //Liabilities
                    body.add(makeHeader("LIABILITIES", Font.PLAIN, 18, 80));
                    //Current Liabilities
                    body.add(makeHeader("Current:", Font.PLAIN, 18, 80));
                    for (AccountTitle accountTitle : accounts.values()) {
                        if (accountTitle.getElement().equals("Current Liability")) {
                            body.add(makeAccTitle(accountTitle, 100));
                        }

                    }
                    //Non Current Liabilities
                    body.add(makeHeader("Non Current:", Font.PLAIN, 18, 80));
                    for (AccountTitle accountTitle : accounts.values()) {
                        if (accountTitle.getElement().equals("Non Current Liability")) {
                            body.add(makeAccTitle(accountTitle, 100));
                        }
                    }
                    //Equity
                    body.add(makeGap(8));
                    body.add(makeHeader("EQUITY", Font.PLAIN, 18, 80));
                    double capital = 0;
                    for (AccountTitle accountTitle : accounts.values()) {
                        if (accountTitle.getElement().equals("Equity")) {
                            if (accountTitle.getTitle().equals("Drawings"))
                                continue;
                            capital += accountTitle.computeEndingBal();
                        }
                    }
                    //initial inv
                    body.add(makeTotal("Initial investment", journalizing.getInitialCapital(), 100));

                    //addtl inv
                    body.add(makeTotal("Add: Additional investment", capital - journalizing.getInitialCapital(), 100));

                                //net income
                                body.add(makeTotal("Net Income", IncomeStatement.getNetIncome(), 150));

                    //total
                    body.add(makeTotal("Total", capital, 100));
                    double withdrawal = 0;
                     for (AccountTitle accountTitle : accounts.values()) {
                        if (accountTitle.getTitle().equals("Drawings")) {
                            withdrawal += accountTitle.computeEndingBal();
                        }
                    }
                    body.add(makeTotal("Less: Withdrawal", withdrawal, 100));

                                //net loss
                                body.add(makeTotal("Net Loss", IncomeStatement.getNetLoss(), 150));

                    body.add(makeGap(8));
                    body.add(makeTotal("Equity, end", capital - withdrawal, 100));
    }
}
