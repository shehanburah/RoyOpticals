package com.theheavenscode.repository.search;
import com.theheavenscode.domain.Invoice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Invoice} entity.
 */
public interface InvoiceSearchRepository extends ElasticsearchRepository<Invoice, String> {
}
