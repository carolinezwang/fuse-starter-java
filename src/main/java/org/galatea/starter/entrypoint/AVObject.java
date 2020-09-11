package org.galatea.starter.entrypoint;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AVObject {
  @JsonProperty("Meta Data") public MetaData metaData;
  @JsonProperty("Time Series (Daily)") public TimeSeriesDaily timeSeriesDaily;
  }


