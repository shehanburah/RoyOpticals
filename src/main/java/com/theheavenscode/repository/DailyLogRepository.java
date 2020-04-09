package com.theheavenscode.repository;
import com.theheavenscode.domain.DailyLog;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the DailyLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DailyLogRepository extends MongoRepository<DailyLog, String> {

}
