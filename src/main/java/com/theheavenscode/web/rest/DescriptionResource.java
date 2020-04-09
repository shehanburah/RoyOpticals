package com.theheavenscode.web.rest;

import com.theheavenscode.domain.Description;
import com.theheavenscode.repository.DescriptionRepository;
import com.theheavenscode.repository.search.DescriptionSearchRepository;
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
 * REST controller for managing {@link com.theheavenscode.domain.Description}.
 */
@RestController
@RequestMapping("/api")
public class DescriptionResource {

    private final Logger log = LoggerFactory.getLogger(DescriptionResource.class);

    private static final String ENTITY_NAME = "description";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DescriptionRepository descriptionRepository;

    private final DescriptionSearchRepository descriptionSearchRepository;

    public DescriptionResource(DescriptionRepository descriptionRepository, DescriptionSearchRepository descriptionSearchRepository) {
        this.descriptionRepository = descriptionRepository;
        this.descriptionSearchRepository = descriptionSearchRepository;
    }

    /**
     * {@code POST  /descriptions} : Create a new description.
     *
     * @param description the description to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new description, or with status {@code 400 (Bad Request)} if the description has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/descriptions")
    public ResponseEntity<Description> createDescription(@RequestBody Description description) throws URISyntaxException {
        log.debug("REST request to save Description : {}", description);
        if (description.getId() != null) {
            throw new BadRequestAlertException("A new description cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Description result = descriptionRepository.save(description);
        descriptionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/descriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /descriptions} : Updates an existing description.
     *
     * @param description the description to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated description,
     * or with status {@code 400 (Bad Request)} if the description is not valid,
     * or with status {@code 500 (Internal Server Error)} if the description couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/descriptions")
    public ResponseEntity<Description> updateDescription(@RequestBody Description description) throws URISyntaxException {
        log.debug("REST request to update Description : {}", description);
        if (description.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Description result = descriptionRepository.save(description);
        descriptionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, description.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /descriptions} : get all the descriptions.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of descriptions in body.
     */
    @GetMapping("/descriptions")
    public ResponseEntity<List<Description>> getAllDescriptions(Pageable pageable) {
        log.debug("REST request to get a page of Descriptions");
        Page<Description> page = descriptionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /descriptions/:id} : get the "id" description.
     *
     * @param id the id of the description to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the description, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/descriptions/{id}")
    public ResponseEntity<Description> getDescription(@PathVariable String id) {
        log.debug("REST request to get Description : {}", id);
        Optional<Description> description = descriptionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(description);
    }

    /**
     * {@code DELETE  /descriptions/:id} : delete the "id" description.
     *
     * @param id the id of the description to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/descriptions/{id}")
    public ResponseEntity<Void> deleteDescription(@PathVariable String id) {
        log.debug("REST request to delete Description : {}", id);
        descriptionRepository.deleteById(id);
        descriptionSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/descriptions?query=:query} : search for the description corresponding
     * to the query.
     *
     * @param query the query of the description search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/descriptions")
    public ResponseEntity<List<Description>> searchDescriptions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Descriptions for query {}", query);
        Page<Description> page = descriptionSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
