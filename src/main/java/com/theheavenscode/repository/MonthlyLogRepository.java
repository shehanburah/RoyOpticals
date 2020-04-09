package com.theheavenscode.repository;
import com.theheavenscode.domain.MonthlyLog;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the MonthlyLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonthlyLogRepository extends MongoRepository<MonthlyLog, String> {

}
