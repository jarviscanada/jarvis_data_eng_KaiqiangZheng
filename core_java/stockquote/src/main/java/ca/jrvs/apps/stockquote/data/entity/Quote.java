package ca.jrvs.apps.stockquote.data.entity;

//import java.sql.Date;
//import java.sql.Timestamp;
//
//public class Quote {
//
//  private String ticker; // id
//  private double open;
//  private double high;
//  private double low;
//  private double price;
//  private int volume;
//  private Date latestTradingDay;
//  private double previousClose;
//  private double change;
//  private String changePercent;
//  private Timestamp timestamp; // pulled time
//
//  public Quote() {}
//
//  // Getters and Setters
//  public String getTicker() { return ticker; }
//  public void setTicker(String ticker) { this.ticker = ticker; }
//  public double getOpen() { return open; }
//  public void setOpen(double open) { this.open = open; }
//  public double getHigh() { return high; }
//  public void setHigh(double high) { this.high = high; }
//  public double getLow() { return low; }
//  public void setLow(double low) { this.low = low; }
//  public double getPrice() { return price; }
//  public void setPrice(double price) { this.price = price; }
//  public int getVolume() { return volume; }
//  public void setVolume(int volume) { this.volume = volume; }
//  public Date getLatestTradingDay() { return latestTradingDay; }
//  public void setLatestTradingDay(Date latestTradingDay) { this.latestTradingDay = latestTradingDay; }
//  public double getPreviousClose() { return previousClose; }
//  public void setPreviousClose(double previousClose) { this.previousClose = previousClose; }
//  public double getChange() { return change; }
//  public void setChange(double change) { this.change = change; }
//  public String getChangePercent() { return changePercent; }
//  public void setChangePercent(String changePercent) { this.changePercent = changePercent; }
//  public Timestamp getTimestamp() { return timestamp; }
//  public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
//}

//DTO
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.sql.Timestamp;

// Ignore fields in JSON that we don't need
@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {

  @JsonProperty("01. symbol") // Map "01. symbol" from JSON to ticker
  private String ticker;

  @JsonProperty("02. open")
  private double open;

  @JsonProperty("03. high")
  private double high;

  @JsonProperty("04. low")
  private double low;

  @JsonProperty("05. price")
  private double price;

  @JsonProperty("06. volume")
  private int volume;

  @JsonProperty("07. latest trading day")
  private Date latestTradingDay; // Jackson will automatically try to parse the format yyyy-MM-dd

  @JsonProperty("08. previous close")
  private double previousClose;

  @JsonProperty("09. change")
  private double change;

  @JsonProperty("10. change percent")
  private String changePercent;

  private Timestamp timestamp; // This field is not in the API, we need to manually assign the current time when retrieving the data

  // --- Getters and Setters ---
  public String getTicker() { return ticker; }
  public void setTicker(String ticker) { this.ticker = ticker; }

  public double getOpen() { return open; }
  public void setOpen(double open) { this.open = open; }

  public double getHigh() { return high; }
  public void setHigh(double high) { this.high = high; }

  public double getLow() { return low; }
  public void setLow(double low) { this.low = low; }

  public double getPrice() { return price; }
  public void setPrice(double price) { this.price = price; }

  public int getVolume() { return volume; }
  public void setVolume(int volume) { this.volume = volume; }

  public Date getLatestTradingDay() { return latestTradingDay; }
  public void setLatestTradingDay(Date latestTradingDay) { this.latestTradingDay = latestTradingDay; }

  public double getPreviousClose() { return previousClose; }
  public void setPreviousClose(double previousClose) { this.previousClose = previousClose; }

  public double getChange() { return change; }
  public void setChange(double change) { this.change = change; }

  public String getChangePercent() { return changePercent; }
  public void setChangePercent(String changePercent) { this.changePercent = changePercent; }

  public Timestamp getTimestamp() { return timestamp; }
  public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}