package com.theheavenscode.repository;
import com.theheavenscode.domain.PettyCashStock;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the PettyCashStock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PettyCashStockRepository extends MongoRepository<PettyCashStock, String> {

}
