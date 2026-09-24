package UI;

import java.util.HashMap;

import Backend.Ledger;

public class ATB extends TrialBalanceView {

  public ATB(Journalizing journalizing) {
    super("Adjusted Trial Balance", new HashMap<>(Ledger.getAccounts()), "TOTAL ADJUSTED BALANCE");
  }
}
