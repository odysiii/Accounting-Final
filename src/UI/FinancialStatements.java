package UI;

import java.awt.*;

import javax.swing.*;

// Income Statement and Balance Sheet side by side. The Income Statement is built first because the Balance Sheet reads its net income.
public class FinancialStatements extends JPanel {

    public FinancialStatements(Journalizing journalizing) {

        setOpaque(false);
        setLayout(new GridLayout(1, 2, 34, 0));
        setBorder(Theme.pagePad());

        IncomeStatement incomeStatement = new IncomeStatement(journalizing);
        BalanceSheet balanceSheet = new BalanceSheet(journalizing);

        add(incomeStatement);
        add(balanceSheet);
    }
}
