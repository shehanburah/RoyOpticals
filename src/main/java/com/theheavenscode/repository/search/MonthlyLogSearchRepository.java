package com.theheavenscode.repository.search;
import com.theheavenscode.domain.MonthlyLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link MonthlyLog} entity.
 */
public interface MonthlyLogSearchRepository extends ElasticsearchRepository<MonthlyLog, String> {
}
