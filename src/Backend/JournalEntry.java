package Backend;

public class JournalEntry {

  private String date;
  private String debitAccountTitle;
  private String creditAccountTitle;
  private double debitValue;
  private double creditValue;
  private String note;

  public JournalEntry(String date, String debitTitle, String creditTitle, Double debitValue, Double creditValue ,String note){
    this.date = date;
    this.debitAccountTitle = debitTitle;
    this.creditAccountTitle = creditTitle;
    this.note = note;
    this.debitValue = debitValue;
    this.creditValue = creditValue;
  }

  public String getDate() {
    return date;
  }
  public String getDebitAccountTitle() {
    return debitAccountTitle;
  }
  public String getCreditAccountTitle() {
    return creditAccountTitle;
  }
  public double getDebitValue() {
    return debitValue;
  }
  public double getCreditValue() {
    return creditValue;
  }
  public String getNote() {
    return note;
  }
  
  public String toCsv(){
    return date+","+debitAccountTitle+","+debitValue+","+creditAccountTitle+","+creditValue+","+note+"\n";
  }
}
