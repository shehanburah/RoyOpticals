package com.theheavenscode.web.rest;

import com.theheavenscode.domain.MonthlyLog;
import com.theheavenscode.repository.MonthlyLogRepository;
import com.theheavenscode.repository.search.MonthlyLogSearchRepository;
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
 * REST controller for managing {@link com.theheavenscode.domain.MonthlyLog}.
 */
@RestController
@RequestMapping("/api")
public class MonthlyLogResource {

    private final Logger log = LoggerFactory.getLogger(MonthlyLogResource.class);

    private static final String ENTITY_NAME = "monthlyLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonthlyLogRepository monthlyLogRepository;

    private final MonthlyLogSearchRepository monthlyLogSearchRepository;

    public MonthlyLogResource(MonthlyLogRepository monthlyLogRepository, MonthlyLogSearchRepository monthlyLogSearchRepository) {
        this.monthlyLogRepository = monthlyLogRepository;
        this.monthlyLogSearchRepository = monthlyLogSearchRepository;
    }

    /**
     * {@code POST  /monthly-logs} : Create a new monthlyLog.
     *
     * @param monthlyLog the monthlyLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new monthlyLog, or with status {@code 400 (Bad Request)} if the monthlyLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/monthly-logs")
    public ResponseEntity<MonthlyLog> createMonthlyLog(@RequestBody MonthlyLog monthlyLog) throws URISyntaxException {
        log.debug("REST request to save MonthlyLog : {}", monthlyLog);
        if (monthlyLog.getId() != null) {
            throw new BadRequestAlertException("A new monthlyLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MonthlyLog result = monthlyLogRepository.save(monthlyLog);
        monthlyLogSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/monthly-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /monthly-logs} : Updates an existing monthlyLog.
     *
     * @param monthlyLog the monthlyLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monthlyLog,
     * or with status {@code 400 (Bad Request)} if the monthlyLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the monthlyLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/monthly-logs")
    public ResponseEntity<MonthlyLog> updateMonthlyLog(@RequestBody MonthlyLog monthlyLog) throws URISyntaxException {
        log.debug("REST request to update MonthlyLog : {}", monthlyLog);
        if (monthlyLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MonthlyLog result = monthlyLogRepository.save(monthlyLog);
        monthlyLogSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, monthlyLog.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /monthly-logs} : get all the monthlyLogs.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monthlyLogs in body.
     */
    @GetMapping("/monthly-logs")
    public ResponseEntity<List<MonthlyLog>> getAllMonthlyLogs(Pageable pageable) {
        log.debug("REST request to get a page of MonthlyLogs");
        Page<MonthlyLog> page = monthlyLogRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /monthly-logs/:id} : get the "id" monthlyLog.
     *
     * @param id the id of the monthlyLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monthlyLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/monthly-logs/{id}")
    public ResponseEntity<MonthlyLog> getMonthlyLog(@PathVariable String id) {
        log.debug("REST request to get MonthlyLog : {}", id);
        Optional<MonthlyLog> monthlyLog = monthlyLogRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(monthlyLog);
    }

    /**
     * {@code DELETE  /monthly-logs/:id} : delete the "id" monthlyLog.
     *
     * @param id the id of the monthlyLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/monthly-logs/{id}")
    public ResponseEntity<Void> deleteMonthlyLog(@PathVariable String id) {
        log.debug("REST request to delete MonthlyLog : {}", id);
        monthlyLogRepository.deleteById(id);
        monthlyLogSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/monthly-logs?query=:query} : search for the monthlyLog corresponding
     * to the query.
     *
     * @param query the query of the monthlyLog search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/monthly-logs")
    public ResponseEntity<List<MonthlyLog>> searchMonthlyLogs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MonthlyLogs for query {}", query);
        Page<MonthlyLog> page = monthlyLogSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
