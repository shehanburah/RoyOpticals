package com.theheavenscode.web.rest;

import com.theheavenscode.domain.Prescription;
import com.theheavenscode.repository.PrescriptionRepository;
import com.theheavenscode.repository.search.PrescriptionSearchRepository;
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
 * REST controller for managing {@link com.theheavenscode.domain.Prescription}.
 */
@RestController
@RequestMapping("/api")
public class PrescriptionResource {

    private final Logger log = LoggerFactory.getLogger(PrescriptionResource.class);

    private static final String ENTITY_NAME = "prescription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrescriptionRepository prescriptionRepository;

    private final PrescriptionSearchRepository prescriptionSearchRepository;

    public PrescriptionResource(PrescriptionRepository prescriptionRepository, PrescriptionSearchRepository prescriptionSearchRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionSearchRepository = prescriptionSearchRepository;
    }

    /**
     * {@code POST  /prescriptions} : Create a new prescription.
     *
     * @param prescription the prescription to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prescription, or with status {@code 400 (Bad Request)} if the prescription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prescriptions")
    public ResponseEntity<Prescription> createPrescription(@RequestBody Prescription prescription) throws URISyntaxException {
        log.debug("REST request to save Prescription : {}", prescription);
        if (prescription.getId() != null) {
            throw new BadRequestAlertException("A new prescription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Prescription result = prescriptionRepository.save(prescription);
        prescriptionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/prescriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prescriptions} : Updates an existing prescription.
     *
     * @param prescription the prescription to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prescription,
     * or with status {@code 400 (Bad Request)} if the prescription is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prescription couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prescriptions")
    public ResponseEntity<Prescription> updatePrescription(@RequestBody Prescription prescription) throws URISyntaxException {
        log.debug("REST request to update Prescription : {}", prescription);
        if (prescription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Prescription result = prescriptionRepository.save(prescription);
        prescriptionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, prescription.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /prescriptions} : get all the prescriptions.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prescriptions in body.
     */
    @GetMapping("/prescriptions")
    public ResponseEntity<List<Prescription>> getAllPrescriptions(Pageable pageable) {
        log.debug("REST request to get a page of Prescriptions");
        Page<Prescription> page = prescriptionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prescriptions/:id} : get the "id" prescription.
     *
     * @param id the id of the prescription to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prescription, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prescriptions/{id}")
    public ResponseEntity<Prescription> getPrescription(@PathVariable String id) {
        log.debug("REST request to get Prescription : {}", id);
        Optional<Prescription> prescription = prescriptionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(prescription);
    }

    /**
     * {@code DELETE  /prescriptions/:id} : delete the "id" prescription.
     *
     * @param id the id of the prescription to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prescriptions/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable String id) {
        log.debug("REST request to delete Prescription : {}", id);
        prescriptionRepository.deleteById(id);
        prescriptionSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/prescriptions?query=:query} : search for the prescription corresponding
     * to the query.
     *
     * @param query the query of the prescription search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/prescriptions")
    public ResponseEntity<List<Prescription>> searchPrescriptions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Prescriptions for query {}", query);
        Page<Prescription> page = prescriptionSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
