package Backend;
import java.io.*;
import java.util.ArrayList;

import UI.GetPath;

public class AccountTitle {

  private String title;
  private String element;
  private String side;
  private ArrayList<Double> debitValues = new ArrayList<>();
  private ArrayList<Double> creditValues = new ArrayList<>();
  private double endingBalance = 0;

  public AccountTitle(String title){
    this.title = title;
    setElement();
    setSide();
  }

  public void setDebitValue(double debitValue) {
    debitValues.add(debitValue);
  }
  public void setCreditValue(double creditValue) {
    creditValues.add(creditValue);
  }
  
  public void setElement() {
    String path = GetPath.getElementPath();
    String[][] files = {{"CurrentAssets.txt", "Current Asset"},
                        {"NonCurrentAssets.txt","Non Current Asset"},
                        {"CurrentLiabilities.txt", "Current Liability"},
                        {"NonCurrentLiabilities.txt", "Non Current Liability"},
                        {"Equities.txt", "Equity"},
                        {"Income.txt", "Income" },
                        {"Expenses.txt", "Expense"}
                       };
        
    for (String[] file : files) {
      try (BufferedReader br = new BufferedReader(new FileReader(path + file[0]))) {
        String line;
        while ((line = br.readLine()) != null) {
          if (line.equalsIgnoreCase(this.title)) {
            element = file[1];
            return;
          }
        }
      } catch (Exception e) {
        System.out.println(file[1] + " not found");
      }
    }
  
  }
  
  public void setSide(){
    if (this.title.equalsIgnoreCase("Drawings")) {
      side = "Debit";
    }else if (this.title.equalsIgnoreCase("Accumulated Depreciation")) {
      side = "Credit";
    }else if(this.title.equalsIgnoreCase("Sales Returns and Allowances")){
      side = "Debit";
    }else if (this.element.equalsIgnoreCase("Current Asset") || this.element.equals("Non Current Asset") ||this.element.equals("Expense")) {
      side = "Debit";
    }else{
      side = "Credit";
    }
  }

  public String getTitle() {
    return title;
  }
  public String getElement() {
    return element;
  }
  public String getSide() {
    return side;
  }
  public ArrayList<Double> getDebitValues() {
    return debitValues;
  }
  public ArrayList<Double> getCreditValues() {
    return creditValues;
  }

  public double computeDebitvals(){
    double sum = 0;
    for (Double debit : debitValues) {
      sum += debit;
    }
    return sum;
  }
  public double computeCreditvals(){
    double sum = 0;
    for (Double credit : creditValues) {
      sum += credit;
    }
    return sum;
  }
  public double computeEndingBal(){
    if (side.equals("Debit")) {
      endingBalance = computeDebitvals() - computeCreditvals();
    }else{
      endingBalance = computeCreditvals() - computeDebitvals();
    }
    return endingBalance;
  }
}
