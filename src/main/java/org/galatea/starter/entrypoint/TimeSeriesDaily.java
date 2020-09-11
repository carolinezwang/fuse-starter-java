package org.galatea.starter.entrypoint;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TimeSeriesDaily {
  public String dateName;
  public DailyStock dailyStock;

  public TimeSeriesDaily(String fieldName, DailyStock ds) {
    this.dateName = fieldName;
    this.dailyStock = ds;
  }
}
