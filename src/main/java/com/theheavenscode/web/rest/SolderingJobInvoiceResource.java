package com.theheavenscode.web.rest;

import com.theheavenscode.domain.SolderingJobInvoice;
import com.theheavenscode.repository.SolderingJobInvoiceRepository;
import com.theheavenscode.repository.search.SolderingJobInvoiceSearchRepository;
import com.theheavenscode.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.theheavenscode.domain.SolderingJobInvoice}.
 */
@RestController
@RequestMapping("/api")
public class SolderingJobInvoiceResource {

    private final Logger log = LoggerFactory.getLogger(SolderingJobInvoiceResource.class);

    private static final String ENTITY_NAME = "solderingJobInvoice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SolderingJobInvoiceRepository solderingJobInvoiceRepository;

    private final SolderingJobInvoiceSearchRepository solderingJobInvoiceSearchRepository;

    public SolderingJobInvoiceResource(SolderingJobInvoiceRepository solderingJobInvoiceRepository, SolderingJobInvoiceSearchRepository solderingJobInvoiceSearchRepository) {
        this.solderingJobInvoiceRepository = solderingJobInvoiceRepository;
        this.solderingJobInvoiceSearchRepository = solderingJobInvoiceSearchRepository;
    }

    /**
     * {@code POST  /soldering-job-invoices} : Create a new solderingJobInvoice.
     *
     * @param solderingJobInvoice the solderingJobInvoice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new solderingJobInvoice, or with status {@code 400 (Bad Request)} if the solderingJobInvoice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/soldering-job-invoices")
    public ResponseEntity<SolderingJobInvoice> createSolderingJobInvoice(@RequestBody SolderingJobInvoice solderingJobInvoice) throws URISyntaxException {
        log.debug("REST request to save SolderingJobInvoice : {}", solderingJobInvoice);
        if (solderingJobInvoice.getId() != null) {
            throw new BadRequestAlertException("A new solderingJobInvoice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SolderingJobInvoice result = solderingJobInvoiceRepository.save(solderingJobInvoice);
        solderingJobInvoiceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/soldering-job-invoices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /soldering-job-invoices} : Updates an existing solderingJobInvoice.
     *
     * @param solderingJobInvoice the solderingJobInvoice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated solderingJobInvoice,
     * or with status {@code 400 (Bad Request)} if the solderingJobInvoice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the solderingJobInvoice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/soldering-job-invoices")
    public ResponseEntity<SolderingJobInvoice> updateSolderingJobInvoice(@RequestBody SolderingJobInvoice solderingJobInvoice) throws URISyntaxException {
        log.debug("REST request to update SolderingJobInvoice : {}", solderingJobInvoice);
        if (solderingJobInvoice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SolderingJobInvoice result = solderingJobInvoiceRepository.save(solderingJobInvoice);
        solderingJobInvoiceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, solderingJobInvoice.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /soldering-job-invoices} : get all the solderingJobInvoices.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of solderingJobInvoices in body.
     */
    @GetMapping("/soldering-job-invoices")
    public ResponseEntity<List<SolderingJobInvoice>> getAllSolderingJobInvoices(Pageable pageable) {
        log.debug("REST request to get a page of SolderingJobInvoices");
        Page<SolderingJobInvoice> page = solderingJobInvoiceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /soldering-job-invoices/:id} : get the "id" solderingJobInvoice.
     *
     * @param id the id of the solderingJobInvoice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the solderingJobInvoice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/soldering-job-invoices/{id}")
    public ResponseEntity<SolderingJobInvoice> getSolderingJobInvoice(@PathVariable String id) {
        log.debug("REST request to get SolderingJobInvoice : {}", id);
        Optional<SolderingJobInvoice> solderingJobInvoice = solderingJobInvoiceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(solderingJobInvoice);
    }

    /**
     * {@code DELETE  /soldering-job-invoices/:id} : delete the "id" solderingJobInvoice.
     *
     * @param id the id of the solderingJobInvoice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/soldering-job-invoices/{id}")
    public ResponseEntity<Void> deleteSolderingJobInvoice(@PathVariable String id) {
        log.debug("REST request to delete SolderingJobInvoice : {}", id);
        solderingJobInvoiceRepository.deleteById(id);
        solderingJobInvoiceSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/soldering-job-invoices?query=:query} : search for the solderingJobInvoice corresponding
     * to the query.
     *
     * @param query the query of the solderingJobInvoice search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/soldering-job-invoices")
    public ResponseEntity<List<SolderingJobInvoice>> searchSolderingJobInvoices(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SolderingJobInvoices for query {}", query);
        Page<SolderingJobInvoice> page = solderingJobInvoiceSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
