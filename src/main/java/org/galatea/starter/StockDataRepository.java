package org.galatea.starter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.galatea.starter.entrypoint.MongoDAO;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockDataRepository extends MongoRepository<MongoDAO, String>{
  Optional<MongoDAO> findOneByStockAndDate(String stock, String date);
  //MongoDAO findOne(Query query, Class<MongoDAO> mongoDAOClass);
  //boolean exists(Query query, Class<MongoDAO> mongoDAOClass);
  //MongoDAO insert(MongoDAO mdao);

}
