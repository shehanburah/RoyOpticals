package com.theheavenscode.repository;
import com.theheavenscode.domain.SolderingJobInvoice;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the SolderingJobInvoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SolderingJobInvoiceRepository extends MongoRepository<SolderingJobInvoice, String> {

}
