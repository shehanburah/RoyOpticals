package com.theheavenscode.repository.search;
import com.theheavenscode.domain.PettyCashStock;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PettyCashStock} entity.
 */
public interface PettyCashStockSearchRepository extends ElasticsearchRepository<PettyCashStock, String> {
}
