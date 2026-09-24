package UI;

import java.awt.*;

import javax.swing.*;

import Backend.Ledger;

// The one window of the app: a header with Home / Create / View, the workflow tabs, and whichever screen is current.
public class AppFrame extends JFrame {

    static {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        UIManager.put("LabelUI", "UI.SlackLabelUI");
        UIManager.put("OptionPane.messageFont", Theme.inter(Font.PLAIN, 14f));
        UIManager.put("OptionPane.buttonFont", Theme.inter(Font.BOLD, 13f));
    }

    public enum Section {
        HOME, CREATE, VIEW
    }

    public enum Tab {
        JOURNAL("Journal", Icons.Type.BOOK),
        LEDGERS("Ledgers", Icons.Type.GRID),
        UNADJUSTED("Unadjusted Trial Balance", Icons.Type.SCALE),
        ADJUSTMENTS("Adjustments", Icons.Type.SLIDERS),
        ADJUSTED("Adjusted Trial Balance", Icons.Type.SCALE),
        STATEMENTS("Financial Statements", Icons.Type.DOC);

        final String label;
        final Icons.Type icon;

        Tab(String label, Icons.Type icon) {
            this.label = label;
            this.icon = icon;
        }
    }

    private static AppFrame instance;
    private static int doneAdjustment = 0;

    private final HeaderBar header = new HeaderBar(this);
    private final TabBar tabBar = new TabBar(this);
    private final JPanel content = new JPanel(new BorderLayout());

    private Journalizing journalizing;
    private boolean sessionActive = false;
    private int savedCount = -1;

    public AppFrame() {
        super("Accounting Cycle");
        instance = this;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1024, 680));
        setSize(1366, 800);
        setExtendedState(MAXIMIZED_BOTH);

        JPanel north = new JPanel();
        north.setOpaque(false);
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        north.add(header);
        north.add(tabBar);

        content.setOpaque(false);

        GradientPanel root = new GradientPanel();
        root.setLayout(new BorderLayout());
        root.add(north, BorderLayout.NORTH);
        root.add(content, BorderLayout.CENTER);
        setContentPane(root);

        show(new HomeFrame(), Section.HOME, null, null, true, null);
    }

    public static AppFrame get() {
        return instance;
    }

    public static void setDoneAdjustment(int done) {
        doneAdjustment = done;
    }

    // ---- navigation used by the header and the screens ----

    public void goHome() {
        if (!confirmLeave()) {
            return;
        }
        endSession();
        show(new HomeFrame(), Section.HOME, null, null, true, null);
    }

    public void goSaved() {
        if (!confirmLeave()) {
            return;
        }
        endSession();
        refreshSaved();
    }

    public void refreshSaved() {
        show(new ViewSavedJournals(), Section.VIEW, "Saved Journals", "Accounting Cycle", false, null);
    }

    public void startNewJournal() {
        if (!confirmLeave()) {
            return;
        }
        new CompanyName(this, () -> {
            resetSession();
            journalizing = new Journalizing(null);
            sessionActive = true;
            show(journalizing, Section.CREATE, "General Journal Ledger", "Accounting Cycle", false, null);
        }).setVisible(true);
    }

    public void openSavedJournal(String fileName) {
        if (!confirmLeave()) {
            return;
        }
        resetSession();
        // Saved files do not keep the company details, so the file name stands in for the company name.
        CompanyName.setDetails(fileName.replace(".txt", ""), "", "");
        journalizing = new Journalizing(fileName);
        Ledger.makeLedgers(Ledger.getUnAdjustedEntries(), Ledger.getUnAdjustedAccounts());
        sessionActive = true;
        markSaved();
        showTab(Tab.LEDGERS);
    }

    public void showTab(Tab tab) {
        JComponent view;
        switch (tab) {
            case JOURNAL:
                view = new Journal(journalizing);
                break;
            case LEDGERS:
                view = new LedgersFrame(doneAdjustment == 1 ? Ledger.getAccounts() : Ledger.getUnAdjustedAccounts(), journalizing);
                break;
            case UNADJUSTED:
                view = new UTB(journalizing);
                break;
            case ADJUSTMENTS:
                view = new Adjustment(journalizing);
                break;
            case ADJUSTED:
                view = new ATB(journalizing);
                break;
            default:
                view = new FinancialStatements(journalizing);
                break;
        }
        show(view, Section.CREATE, null, null, false, tab);
    }

    // ---- unsaved-work tracking ----

    public void markSaved() {
        savedCount = Ledger.getEntries().size();
    }

    private boolean isDirty() {
        int entries = Ledger.getEntries().size();
        return sessionActive && entries > 0 && entries != savedCount;
    }

    private boolean confirmLeave() {
        if (!isDirty()) {
            return true;
        }
        return JOptionPane.showConfirmDialog(this, "This journal has unsaved changes.\nDiscard them and continue?",
                "Unsaved Changes", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }

    private void endSession() {
        sessionActive = false;
        journalizing = null;
    }

    // Ledger and the totals kept in UI classes are static, so a new journal starts by emptying them.
    private void resetSession() {
        Ledger.getEntries().clear();
        Ledger.getUnAdjustedEntries().clear();
        Ledger.getAccounts().clear();
        Ledger.getUnAdjustedAccounts().clear();
        doneAdjustment = 0;
        savedCount = -1;
        SavingJournals.reset();
        IncomeStatement.reset();
        COGSandFO.reset();
    }

    private void show(JComponent view, Section section, String title, String subtitle, boolean smallBrand, Tab tab) {
        content.removeAll();
        content.add(view, BorderLayout.CENTER);
        header.setState(section, title, subtitle, smallBrand);
        tabBar.setActive(tab);
        content.revalidate();
        content.repaint();
        getContentPane().revalidate();
        getContentPane().repaint();
    }
}
