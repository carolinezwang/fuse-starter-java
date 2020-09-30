package org.galatea.starter;


import java.util.Optional;
import org.galatea.starter.entrypoint.MongoDAO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockDataRepository extends MongoRepository<MongoDAO, String>{
  Optional<MongoDAO> findOneByStockAndDate(String stock, String date);
  Optional<MongoDAO> findOneByDate(String date);

}
