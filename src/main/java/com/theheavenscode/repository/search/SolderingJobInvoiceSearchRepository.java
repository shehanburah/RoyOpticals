package com.theheavenscode.repository.search;
import com.theheavenscode.domain.SolderingJobInvoice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link SolderingJobInvoice} entity.
 */
public interface SolderingJobInvoiceSearchRepository extends ElasticsearchRepository<SolderingJobInvoice, String> {
}
