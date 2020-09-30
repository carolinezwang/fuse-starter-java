package org.galatea.starter.entrypoint;

//package org.galatea.starter.domain.rpsy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.ASpringTest;
import org.galatea.starter.StockDataRepository;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@RequiredArgsConstructor
@Slf4j
@Category(org.galatea.starter.IntegrationTestCategory.class)
@SpringBootTest
public class MongoDbSpringIntegrationTest extends ASpringTest {

  @Autowired
  private StockDataRepository stockDataRepository;

  @Test
  public void testMongo() throws Exception {
    // create a test object to place in new empty repository
    ObjectMapper mapper = new ObjectMapper();
    String test_output_to_map = "{\n"
        + "      \"1. open\": \"0\",\n"
        + "      \"2. high\": \"0\",\n"
        + "      \"3. low\": \"0\",\n"
        + "      \"4. close\": \"0\",\n"
        + "      \"5. volume\": \"0\"\n"
        + "    },\n";
    JsonNode test_ds = mapper.readTree(test_output_to_map);
    DailyStock dailyStock = mapper.treeToValue(test_ds, DailyStock.class);

    MongoDAO user = new MongoDAO("test","2020-09-28",dailyStock);
    stockDataRepository.save(user);

    //test if object is 1) the only object in repository 2)the object is present in the repository 3) the information in the object is as expected
    assertEquals(1, stockDataRepository.findAll().size());
    assertTrue(stockDataRepository.findOneByDate("2020-09-28").isPresent());
    assertEquals("test", stockDataRepository.findOneByDate("2020-09-28").get().stock);

  }

}