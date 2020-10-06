package org.galatea.starter.entrypoint;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.LocalDate;
import java.util.Optional;
import org.galatea.starter.StockDataRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RequestProcessor {
  @Autowired
  StockDataRepository stockDataRepository;

  public Map<String, ArrayNode> checkMongoDB (String stock, int days) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode newRoot = mapper.createObjectNode();
    ArrayNode jsonArray = newRoot.putArray(String.format("%s Daily Stock Prices for Last %d Days",stock, days));
    Map<String, ArrayNode> ans = new HashMap<String, ArrayNode>();

    //loop through the last n days (starting from today) and see if all the information is in the database
    //expect more recent days to not be in the repository, so for-loop should break earlier on rather than later if info is missing
    LocalDate today = LocalDate.now();
    for (LocalDate date = today; date.isBefore(today.plusDays(days)); date = date.minusDays(1)) {
      String dateToFind = date.toString();

      //find stock and date combo in database
      Optional<MongoDAO> stockData = stockDataRepository.findFirstByStockAndDate(stock,dateToFind);

      if (stockData.isPresent()) {
        //if yes, format mongoDAO as object node, add to array node, and continue loop
        MongoDAO user = stockData.get();

        ObjectNode objNode = mapper.createObjectNode();
        objNode.putPOJO(String.format("%s Stock Price on %s", user.stock, user.date), user.ds);
        jsonArray.add(objNode);
      }
      else{ //if not present, break and return accessAV (which will go to alpha vantage and add any missing information)
        ans = accessAV(stock, days);
        return ans;
      }
    }

    ans.put(String.format("%s Daily Stock Prices for Last %d Days", stock, days), jsonArray);
    return ans;


  }

  public Map<String, ArrayNode> accessAV(String stock, int days) throws IOException, NullPointerException {
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
      MongoDAO MDAO = new MongoDAO(stock,date,ds);
      ObjectNode objNode = mapper.createObjectNode();
      objNode.putPOJO(String.format("%s Stock Price on %s",MDAO.stock, MDAO.date), MDAO.ds);
      jsonArray.add(objNode);

      //check if mongo object is already in data base before adding it (so no duplicates)
      Optional<MongoDAO> stockData = stockDataRepository.findFirstByStockAndDate(stock,date);

      if (!stockData.isPresent()) {
        stockDataRepository.insert(MDAO);
      }

      //decrement counter for loop
      counter -= 1;
    }
    //return json output as a hashmap with a header
    Map<String, ArrayNode> ans = new HashMap<String, ArrayNode>();
    ans.put(String.format("%s Daily Stock Prices for Last %d Days",stock, days),jsonArray);
    return ans;
    }

}
