package org.galatea.starter.entrypoint;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyStock {
  @JsonProperty("1. open")  public String open;
  @JsonProperty("2. high")  public String high;
  @JsonProperty("3. low")  public String low;
  @JsonProperty("4. close")  public String close;
  @JsonProperty("5. volume")public String volume;
  //@JsonProperty("8. adjusted close")public String adjustedClose;
  //@JsonProperty("5. adjusted close")  public String adjustedClose;
  //@JsonProperty("6. volume")  public String volume;

}
