package org.galatea.starter.entrypoint;

public class MongoDAO {
  public String stock;
  public String date;
  public DailyStock prices;

  public MongoDAO(String stock, String date, DailyStock ds) {
    this.stock = stock;
    this.date = date;
    this.prices = ds;
  }
}
