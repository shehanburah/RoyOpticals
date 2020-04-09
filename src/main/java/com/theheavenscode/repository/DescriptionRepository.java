package com.theheavenscode.repository;
import com.theheavenscode.domain.Description;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Description entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DescriptionRepository extends MongoRepository<Description, String> {

}
