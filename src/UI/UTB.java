package UI;

import Backend.Ledger;

public class UTB extends TrialBalanceView {

  public UTB(Journalizing journalizing) {
    super("Unadjusted Trial Balance", Ledger.getUnAdjustedAccounts(), "TOTAL UNADJUSTED BALANCE");
  }
}
