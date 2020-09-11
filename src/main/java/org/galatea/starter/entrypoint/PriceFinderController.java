package org.galatea.starter.entrypoint;

//import com.fasterxml.jackson.databind.node.ObjectNode;
//import com.fasterxml.jackson.databind.node.ValueNode;
import java.io.IOException;
import java.util.Iterator;
//import java.util.List;
//import java.util.Map.Entry;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.aspect4log.Log;
import net.sf.aspect4log.Log.Level;
//import org.galatea.starter.service.HalService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//import org.springframework.web.bind.annotation.ResponseBody;
/**
 * REST Controller that listens to http endpoints and allows the caller to send text to be
 * processed.
 */
@RequiredArgsConstructor
@Slf4j
@Log(enterLevel = Level.INFO, exitLevel = Level.INFO)
@RestController
public class PriceFinderController extends BaseRestController{

  @NonNull
  // @GetMapping to link http GET request to this method
  @GetMapping(value = "${webservice.avpath}", produces = {MediaType.APPLICATION_JSON_VALUE})
  /*
   * Send the received text to the HalService to be processed and send the result out.
   */

  public String endpoint(
      @RequestParam(value = "stock") final String stock,
      @RequestParam(value = "days") final int days) throws IOException {

    return RequestProcessor.requestProcess(stock, days);
  }
}