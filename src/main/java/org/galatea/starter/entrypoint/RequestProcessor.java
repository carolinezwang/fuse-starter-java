package org.galatea.starter.entrypoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class RequestProcessor {

  public static Map<String, ArrayNode> createAVPojo(String stock, int days) throws IOException, NullPointerException {
    //sends GET request to AlphaVantage and puts received data into "response"
    RestTemplate restTemplate = new RestTemplate();
    final String alphaVantageUrl =
        "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=";
    final String apiKey = "&apikey=795KUDT4Z4PZM2DP";
    ResponseEntity<String> response = restTemplate.getForEntity(alphaVantageUrl + stock + apiKey, String.class);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.readTree(response.getBody());

    //set the iterator at the "Time Series (Daily)" node and start traversing
    //this is the solution I found to deal with the dynamic date name fields in Json
    Iterator<Map.Entry<String, JsonNode>> fields = root.get("Time Series (Daily)").fields();
    Integer counter = days;

    ObjectNode newRoot = mapper.createObjectNode();
    ArrayNode jsonArray = newRoot.putArray(String.format("%s Daily Stock Prices for Last %d Days",stock, days));

    //uses counter to filter the necessary num of days requested
    //uses has.Next to make sure that the field to be traversed to exists
    //(ex. if the user requests a number of days that is longer than the amount of time a business has existed)
    while (counter > 0 && fields.hasNext()) {
      Map.Entry<String, JsonNode> field = fields.next();

      //how to deserialize the json output from AlphaVantage
      String date = field.getKey();
      JsonNode priceList = field.getValue();

      DailyStock ds = mapper.treeToValue(priceList, DailyStock.class);
      TimeSeriesDaily tsdAlpha = new TimeSeriesDaily(date, ds); // POJO instance from Alphavantage
      MongoDAO MDAO = new MongoDAO(stock,date,ds);
      ObjectNode objNode = mapper.createObjectNode();
      objNode.putPOJO(String.format("%s Stock Price on %s",MDAO.stock, MDAO.date), MDAO.prices);
      jsonArray.add(objNode);


      //decrement counter for loop
      counter -= 1;
    }
    Map<String, ArrayNode> ans = new HashMap<String, ArrayNode>();
    ans.put(String.format("%s Daily Stock Prices for Last %d Days",stock, days),jsonArray);
    return ans;
    }

  public static Map<String, ArrayNode> requestProcess(String stock, int days) throws IOException{
    Map avPojo = createAVPojo(stock, days);
    return avPojo;
  }
}
