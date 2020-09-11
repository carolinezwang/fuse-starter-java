package org.galatea.starter.entrypoint;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MetaData {
  @JsonProperty("1. Information") public String Information;
  @JsonProperty("2. Symbol") public String Symbol;
  @JsonProperty("3. Last Refreshed") public String LastRefreshed;
  @JsonProperty("4. Output Size") public String OutputSize;
  @JsonProperty("5. Time Zone") public String TimeZone;
}
