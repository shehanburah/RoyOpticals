package com.theheavenscode.web.rest;

import com.theheavenscode.domain.PettyCashStock;
import com.theheavenscode.repository.PettyCashStockRepository;
import com.theheavenscode.repository.search.PettyCashStockSearchRepository;
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
 * REST controller for managing {@link com.theheavenscode.domain.PettyCashStock}.
 */
@RestController
@RequestMapping("/api")
public class PettyCashStockResource {

    private final Logger log = LoggerFactory.getLogger(PettyCashStockResource.class);

    private static final String ENTITY_NAME = "pettyCashStock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PettyCashStockRepository pettyCashStockRepository;

    private final PettyCashStockSearchRepository pettyCashStockSearchRepository;

    public PettyCashStockResource(PettyCashStockRepository pettyCashStockRepository, PettyCashStockSearchRepository pettyCashStockSearchRepository) {
        this.pettyCashStockRepository = pettyCashStockRepository;
        this.pettyCashStockSearchRepository = pettyCashStockSearchRepository;
    }

    /**
     * {@code POST  /petty-cash-stocks} : Create a new pettyCashStock.
     *
     * @param pettyCashStock the pettyCashStock to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pettyCashStock, or with status {@code 400 (Bad Request)} if the pettyCashStock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/petty-cash-stocks")
    public ResponseEntity<PettyCashStock> createPettyCashStock(@RequestBody PettyCashStock pettyCashStock) throws URISyntaxException {
        log.debug("REST request to save PettyCashStock : {}", pettyCashStock);
        if (pettyCashStock.getId() != null) {
            throw new BadRequestAlertException("A new pettyCashStock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PettyCashStock result = pettyCashStockRepository.save(pettyCashStock);
        pettyCashStockSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/petty-cash-stocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /petty-cash-stocks} : Updates an existing pettyCashStock.
     *
     * @param pettyCashStock the pettyCashStock to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pettyCashStock,
     * or with status {@code 400 (Bad Request)} if the pettyCashStock is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pettyCashStock couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/petty-cash-stocks")
    public ResponseEntity<PettyCashStock> updatePettyCashStock(@RequestBody PettyCashStock pettyCashStock) throws URISyntaxException {
        log.debug("REST request to update PettyCashStock : {}", pettyCashStock);
        if (pettyCashStock.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PettyCashStock result = pettyCashStockRepository.save(pettyCashStock);
        pettyCashStockSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pettyCashStock.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /petty-cash-stocks} : get all the pettyCashStocks.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pettyCashStocks in body.
     */
    @GetMapping("/petty-cash-stocks")
    public ResponseEntity<List<PettyCashStock>> getAllPettyCashStocks(Pageable pageable) {
        log.debug("REST request to get a page of PettyCashStocks");
        Page<PettyCashStock> page = pettyCashStockRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /petty-cash-stocks/:id} : get the "id" pettyCashStock.
     *
     * @param id the id of the pettyCashStock to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pettyCashStock, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/petty-cash-stocks/{id}")
    public ResponseEntity<PettyCashStock> getPettyCashStock(@PathVariable String id) {
        log.debug("REST request to get PettyCashStock : {}", id);
        Optional<PettyCashStock> pettyCashStock = pettyCashStockRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pettyCashStock);
    }

    /**
     * {@code DELETE  /petty-cash-stocks/:id} : delete the "id" pettyCashStock.
     *
     * @param id the id of the pettyCashStock to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/petty-cash-stocks/{id}")
    public ResponseEntity<Void> deletePettyCashStock(@PathVariable String id) {
        log.debug("REST request to delete PettyCashStock : {}", id);
        pettyCashStockRepository.deleteById(id);
        pettyCashStockSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/petty-cash-stocks?query=:query} : search for the pettyCashStock corresponding
     * to the query.
     *
     * @param query the query of the pettyCashStock search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/petty-cash-stocks")
    public ResponseEntity<List<PettyCashStock>> searchPettyCashStocks(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PettyCashStocks for query {}", query);
        Page<PettyCashStock> page = pettyCashStockSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
