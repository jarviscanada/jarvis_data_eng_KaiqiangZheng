package ca.jrvs.apps.stockquote.data.entity;

public class Position {

  private String ticker;      // Corresponding to the symbol in the database
  private int numOfShares;    // Number of shares owned
  private double valuePaid;   // Total cost (Total Value Paid)

  // --- Getters and Setters ---
  public String getTicker() { return ticker; }
  public void setTicker(String ticker) { this.ticker = ticker; }

  public int getNumOfShares() { return numOfShares; }
  public void setNumOfShares(int numOfShares) { this.numOfShares = numOfShares; }

  public double getValuePaid() { return valuePaid; }
  public void setValuePaid(double valuePaid) { this.valuePaid = valuePaid; }
}
