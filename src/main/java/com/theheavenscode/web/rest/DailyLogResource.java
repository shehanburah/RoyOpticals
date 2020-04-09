package com.theheavenscode.web.rest;

import com.theheavenscode.domain.DailyLog;
import com.theheavenscode.repository.DailyLogRepository;
import com.theheavenscode.repository.search.DailyLogSearchRepository;
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
 * REST controller for managing {@link com.theheavenscode.domain.DailyLog}.
 */
@RestController
@RequestMapping("/api")
public class DailyLogResource {

    private final Logger log = LoggerFactory.getLogger(DailyLogResource.class);

    private static final String ENTITY_NAME = "dailyLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DailyLogRepository dailyLogRepository;

    private final DailyLogSearchRepository dailyLogSearchRepository;

    public DailyLogResource(DailyLogRepository dailyLogRepository, DailyLogSearchRepository dailyLogSearchRepository) {
        this.dailyLogRepository = dailyLogRepository;
        this.dailyLogSearchRepository = dailyLogSearchRepository;
    }

    /**
     * {@code POST  /daily-logs} : Create a new dailyLog.
     *
     * @param dailyLog the dailyLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dailyLog, or with status {@code 400 (Bad Request)} if the dailyLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/daily-logs")
    public ResponseEntity<DailyLog> createDailyLog(@RequestBody DailyLog dailyLog) throws URISyntaxException {
        log.debug("REST request to save DailyLog : {}", dailyLog);
        if (dailyLog.getId() != null) {
            throw new BadRequestAlertException("A new dailyLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DailyLog result = dailyLogRepository.save(dailyLog);
        dailyLogSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/daily-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /daily-logs} : Updates an existing dailyLog.
     *
     * @param dailyLog the dailyLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dailyLog,
     * or with status {@code 400 (Bad Request)} if the dailyLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dailyLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/daily-logs")
    public ResponseEntity<DailyLog> updateDailyLog(@RequestBody DailyLog dailyLog) throws URISyntaxException {
        log.debug("REST request to update DailyLog : {}", dailyLog);
        if (dailyLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DailyLog result = dailyLogRepository.save(dailyLog);
        dailyLogSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dailyLog.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /daily-logs} : get all the dailyLogs.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dailyLogs in body.
     */
    @GetMapping("/daily-logs")
    public ResponseEntity<List<DailyLog>> getAllDailyLogs(Pageable pageable) {
        log.debug("REST request to get a page of DailyLogs");
        Page<DailyLog> page = dailyLogRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /daily-logs/:id} : get the "id" dailyLog.
     *
     * @param id the id of the dailyLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dailyLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/daily-logs/{id}")
    public ResponseEntity<DailyLog> getDailyLog(@PathVariable String id) {
        log.debug("REST request to get DailyLog : {}", id);
        Optional<DailyLog> dailyLog = dailyLogRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dailyLog);
    }

    /**
     * {@code DELETE  /daily-logs/:id} : delete the "id" dailyLog.
     *
     * @param id the id of the dailyLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/daily-logs/{id}")
    public ResponseEntity<Void> deleteDailyLog(@PathVariable String id) {
        log.debug("REST request to delete DailyLog : {}", id);
        dailyLogRepository.deleteById(id);
        dailyLogSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/daily-logs?query=:query} : search for the dailyLog corresponding
     * to the query.
     *
     * @param query the query of the dailyLog search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/daily-logs")
    public ResponseEntity<List<DailyLog>> searchDailyLogs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DailyLogs for query {}", query);
        Page<DailyLog> page = dailyLogSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
