package com.theheavenscode.repository.search;
import com.theheavenscode.domain.Items;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Items} entity.
 */
public interface ItemsSearchRepository extends ElasticsearchRepository<Items, String> {
}
