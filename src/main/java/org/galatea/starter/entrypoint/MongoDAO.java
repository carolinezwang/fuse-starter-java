package org.galatea.starter.entrypoint;

import org.springframework.data.mongodb.core.mapping.Document;

@Document // to map embedded object DailyStock as part of class
public class MongoDAO {
  public String stock;
  public String date;
  public DailyStock ds;

  public MongoDAO(String stock, String date, DailyStock ds) {
    this.stock = stock;
    this.date = date;
    this.ds = ds;
  }
}
