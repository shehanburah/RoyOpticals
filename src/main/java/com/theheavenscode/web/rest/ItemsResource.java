package com.theheavenscode.web.rest;

import com.theheavenscode.domain.Items;
import com.theheavenscode.repository.ItemsRepository;
import com.theheavenscode.repository.search.ItemsSearchRepository;
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
 * REST controller for managing {@link com.theheavenscode.domain.Items}.
 */
@RestController
@RequestMapping("/api")
public class ItemsResource {

    private final Logger log = LoggerFactory.getLogger(ItemsResource.class);

    private static final String ENTITY_NAME = "items";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemsRepository itemsRepository;

    private final ItemsSearchRepository itemsSearchRepository;

    public ItemsResource(ItemsRepository itemsRepository, ItemsSearchRepository itemsSearchRepository) {
        this.itemsRepository = itemsRepository;
        this.itemsSearchRepository = itemsSearchRepository;
    }

    /**
     * {@code POST  /items} : Create a new items.
     *
     * @param items the items to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new items, or with status {@code 400 (Bad Request)} if the items has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/items")
    public ResponseEntity<Items> createItems(@RequestBody Items items) throws URISyntaxException {
        log.debug("REST request to save Items : {}", items);
        if (items.getId() != null) {
            throw new BadRequestAlertException("A new items cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Items result = itemsRepository.save(items);
        itemsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /items} : Updates an existing items.
     *
     * @param items the items to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated items,
     * or with status {@code 400 (Bad Request)} if the items is not valid,
     * or with status {@code 500 (Internal Server Error)} if the items couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/items")
    public ResponseEntity<Items> updateItems(@RequestBody Items items) throws URISyntaxException {
        log.debug("REST request to update Items : {}", items);
        if (items.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Items result = itemsRepository.save(items);
        itemsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, items.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /items} : get all the items.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of items in body.
     */
    @GetMapping("/items")
    public ResponseEntity<List<Items>> getAllItems(Pageable pageable) {
        log.debug("REST request to get a page of Items");
        Page<Items> page = itemsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /items/:id} : get the "id" items.
     *
     * @param id the id of the items to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the items, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/items/{id}")
    public ResponseEntity<Items> getItems(@PathVariable String id) {
        log.debug("REST request to get Items : {}", id);
        Optional<Items> items = itemsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(items);
    }

    /**
     * {@code DELETE  /items/:id} : delete the "id" items.
     *
     * @param id the id of the items to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItems(@PathVariable String id) {
        log.debug("REST request to delete Items : {}", id);
        itemsRepository.deleteById(id);
        itemsSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/items?query=:query} : search for the items corresponding
     * to the query.
     *
     * @param query the query of the items search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/items")
    public ResponseEntity<List<Items>> searchItems(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Items for query {}", query);
        Page<Items> page = itemsSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
