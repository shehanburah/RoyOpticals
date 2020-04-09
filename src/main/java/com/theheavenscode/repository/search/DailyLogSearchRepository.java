package com.theheavenscode.repository.search;
import com.theheavenscode.domain.DailyLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DailyLog} entity.
 */
public interface DailyLogSearchRepository extends ElasticsearchRepository<DailyLog, String> {
}
