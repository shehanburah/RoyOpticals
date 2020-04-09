package com.theheavenscode.repository.search;
import com.theheavenscode.domain.Description;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Description} entity.
 */
public interface DescriptionSearchRepository extends ElasticsearchRepository<Description, String> {
}
