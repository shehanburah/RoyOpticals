package com.theheavenscode.repository.search;
import com.theheavenscode.domain.Prescription;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Prescription} entity.
 */
public interface PrescriptionSearchRepository extends ElasticsearchRepository<Prescription, String> {
}
