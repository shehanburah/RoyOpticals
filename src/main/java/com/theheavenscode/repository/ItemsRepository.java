package com.theheavenscode.repository;
import com.theheavenscode.domain.Items;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Items entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemsRepository extends MongoRepository<Items, String> {

}
