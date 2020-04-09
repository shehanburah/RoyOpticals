package com.theheavenscode.web.rest;

import com.theheavenscode.RoyOpticalsApp;
import com.theheavenscode.domain.PettyCashStock;
import com.theheavenscode.repository.PettyCashStockRepository;
import com.theheavenscode.repository.search.PettyCashStockSearchRepository;
import com.theheavenscode.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static com.theheavenscode.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PettyCashStockResource} REST controller.
 */
@SpringBootTest(classes = RoyOpticalsApp.class)
public class PettyCashStockResourceIT {

    private static final String DEFAULT_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BRAND_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_SIZE = 1;
    private static final Integer UPDATED_SIZE = 2;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;

    private static final Integer DEFAULT_DISCOUNTED_PRICE = 1;
    private static final Integer UPDATED_DISCOUNTED_PRICE = 2;

    private static final LocalDate DEFAULT_STOCK_IN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STOCK_IN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EST_NEXT_STOCK_IN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EST_NEXT_STOCK_IN_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private PettyCashStockRepository pettyCashStockRepository;

    /**
     * This repository is mocked in the com.theheavenscode.repository.search test package.
     *
     * @see com.theheavenscode.repository.search.PettyCashStockSearchRepositoryMockConfiguration
     */
    @Autowired
    private PettyCashStockSearchRepository mockPettyCashStockSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restPettyCashStockMockMvc;

    private PettyCashStock pettyCashStock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PettyCashStockResource pettyCashStockResource = new PettyCashStockResource(pettyCashStockRepository, mockPettyCashStockSearchRepository);
        this.restPettyCashStockMockMvc = MockMvcBuilders.standaloneSetup(pettyCashStockResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PettyCashStock createEntity() {
        PettyCashStock pettyCashStock = new PettyCashStock()
            .itemName(DEFAULT_ITEM_NAME)
            .brandName(DEFAULT_BRAND_NAME)
            .modelNumber(DEFAULT_MODEL_NUMBER)
            .type(DEFAULT_TYPE)
            .size(DEFAULT_SIZE)
            .remarks(DEFAULT_REMARKS)
            .quantity(DEFAULT_QUANTITY)
            .price(DEFAULT_PRICE)
            .discountedPrice(DEFAULT_DISCOUNTED_PRICE)
            .stockInDate(DEFAULT_STOCK_IN_DATE)
            .estNextStockInDate(DEFAULT_EST_NEXT_STOCK_IN_DATE);
        return pettyCashStock;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PettyCashStock createUpdatedEntity() {
        PettyCashStock pettyCashStock = new PettyCashStock()
            .itemName(UPDATED_ITEM_NAME)
            .brandName(UPDATED_BRAND_NAME)
            .modelNumber(UPDATED_MODEL_NUMBER)
            .type(UPDATED_TYPE)
            .size(UPDATED_SIZE)
            .remarks(UPDATED_REMARKS)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .discountedPrice(UPDATED_DISCOUNTED_PRICE)
            .stockInDate(UPDATED_STOCK_IN_DATE)
            .estNextStockInDate(UPDATED_EST_NEXT_STOCK_IN_DATE);
        return pettyCashStock;
    }

    @BeforeEach
    public void initTest() {
        pettyCashStockRepository.deleteAll();
        pettyCashStock = createEntity();
    }

    @Test
    public void createPettyCashStock() throws Exception {
        int databaseSizeBeforeCreate = pettyCashStockRepository.findAll().size();

        // Create the PettyCashStock
        restPettyCashStockMockMvc.perform(post("/api/petty-cash-stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pettyCashStock)))
            .andExpect(status().isCreated());

        // Validate the PettyCashStock in the database
        List<PettyCashStock> pettyCashStockList = pettyCashStockRepository.findAll();
        assertThat(pettyCashStockList).hasSize(databaseSizeBeforeCreate + 1);
        PettyCashStock testPettyCashStock = pettyCashStockList.get(pettyCashStockList.size() - 1);
        assertThat(testPettyCashStock.getItemName()).isEqualTo(DEFAULT_ITEM_NAME);
        assertThat(testPettyCashStock.getBrandName()).isEqualTo(DEFAULT_BRAND_NAME);
        assertThat(testPettyCashStock.getModelNumber()).isEqualTo(DEFAULT_MODEL_NUMBER);
        assertThat(testPettyCashStock.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPettyCashStock.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testPettyCashStock.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testPettyCashStock.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testPettyCashStock.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testPettyCashStock.getDiscountedPrice()).isEqualTo(DEFAULT_DISCOUNTED_PRICE);
        assertThat(testPettyCashStock.getStockInDate()).isEqualTo(DEFAULT_STOCK_IN_DATE);
        assertThat(testPettyCashStock.getEstNextStockInDate()).isEqualTo(DEFAULT_EST_NEXT_STOCK_IN_DATE);

        // Validate the PettyCashStock in Elasticsearch
        verify(mockPettyCashStockSearchRepository, times(1)).save(testPettyCashStock);
    }

    @Test
    public void createPettyCashStockWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pettyCashStockRepository.findAll().size();

        // Create the PettyCashStock with an existing ID
        pettyCashStock.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restPettyCashStockMockMvc.perform(post("/api/petty-cash-stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pettyCashStock)))
            .andExpect(status().isBadRequest());

        // Validate the PettyCashStock in the database
        List<PettyCashStock> pettyCashStockList = pettyCashStockRepository.findAll();
        assertThat(pettyCashStockList).hasSize(databaseSizeBeforeCreate);

        // Validate the PettyCashStock in Elasticsearch
        verify(mockPettyCashStockSearchRepository, times(0)).save(pettyCashStock);
    }


    @Test
    public void getAllPettyCashStocks() throws Exception {
        // Initialize the database
        pettyCashStockRepository.save(pettyCashStock);

        // Get all the pettyCashStockList
        restPettyCashStockMockMvc.perform(get("/api/petty-cash-stocks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pettyCashStock.getId())))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].modelNumber").value(hasItem(DEFAULT_MODEL_NUMBER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].discountedPrice").value(hasItem(DEFAULT_DISCOUNTED_PRICE)))
            .andExpect(jsonPath("$.[*].stockInDate").value(hasItem(DEFAULT_STOCK_IN_DATE.toString())))
            .andExpect(jsonPath("$.[*].estNextStockInDate").value(hasItem(DEFAULT_EST_NEXT_STOCK_IN_DATE.toString())));
    }
    
    @Test
    public void getPettyCashStock() throws Exception {
        // Initialize the database
        pettyCashStockRepository.save(pettyCashStock);

        // Get the pettyCashStock
        restPettyCashStockMockMvc.perform(get("/api/petty-cash-stocks/{id}", pettyCashStock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pettyCashStock.getId()))
            .andExpect(jsonPath("$.itemName").value(DEFAULT_ITEM_NAME))
            .andExpect(jsonPath("$.brandName").value(DEFAULT_BRAND_NAME))
            .andExpect(jsonPath("$.modelNumber").value(DEFAULT_MODEL_NUMBER))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.discountedPrice").value(DEFAULT_DISCOUNTED_PRICE))
            .andExpect(jsonPath("$.stockInDate").value(DEFAULT_STOCK_IN_DATE.toString()))
            .andExpect(jsonPath("$.estNextStockInDate").value(DEFAULT_EST_NEXT_STOCK_IN_DATE.toString()));
    }

    @Test
    public void getNonExistingPettyCashStock() throws Exception {
        // Get the pettyCashStock
        restPettyCashStockMockMvc.perform(get("/api/petty-cash-stocks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updatePettyCashStock() throws Exception {
        // Initialize the database
        pettyCashStockRepository.save(pettyCashStock);

        int databaseSizeBeforeUpdate = pettyCashStockRepository.findAll().size();

        // Update the pettyCashStock
        PettyCashStock updatedPettyCashStock = pettyCashStockRepository.findById(pettyCashStock.getId()).get();
        updatedPettyCashStock
            .itemName(UPDATED_ITEM_NAME)
            .brandName(UPDATED_BRAND_NAME)
            .modelNumber(UPDATED_MODEL_NUMBER)
            .type(UPDATED_TYPE)
            .size(UPDATED_SIZE)
            .remarks(UPDATED_REMARKS)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .discountedPrice(UPDATED_DISCOUNTED_PRICE)
            .stockInDate(UPDATED_STOCK_IN_DATE)
            .estNextStockInDate(UPDATED_EST_NEXT_STOCK_IN_DATE);

        restPettyCashStockMockMvc.perform(put("/api/petty-cash-stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPettyCashStock)))
            .andExpect(status().isOk());

        // Validate the PettyCashStock in the database
        List<PettyCashStock> pettyCashStockList = pettyCashStockRepository.findAll();
        assertThat(pettyCashStockList).hasSize(databaseSizeBeforeUpdate);
        PettyCashStock testPettyCashStock = pettyCashStockList.get(pettyCashStockList.size() - 1);
        assertThat(testPettyCashStock.getItemName()).isEqualTo(UPDATED_ITEM_NAME);
        assertThat(testPettyCashStock.getBrandName()).isEqualTo(UPDATED_BRAND_NAME);
        assertThat(testPettyCashStock.getModelNumber()).isEqualTo(UPDATED_MODEL_NUMBER);
        assertThat(testPettyCashStock.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPettyCashStock.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testPettyCashStock.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testPettyCashStock.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testPettyCashStock.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testPettyCashStock.getDiscountedPrice()).isEqualTo(UPDATED_DISCOUNTED_PRICE);
        assertThat(testPettyCashStock.getStockInDate()).isEqualTo(UPDATED_STOCK_IN_DATE);
        assertThat(testPettyCashStock.getEstNextStockInDate()).isEqualTo(UPDATED_EST_NEXT_STOCK_IN_DATE);

        // Validate the PettyCashStock in Elasticsearch
        verify(mockPettyCashStockSearchRepository, times(1)).save(testPettyCashStock);
    }

    @Test
    public void updateNonExistingPettyCashStock() throws Exception {
        int databaseSizeBeforeUpdate = pettyCashStockRepository.findAll().size();

        // Create the PettyCashStock

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPettyCashStockMockMvc.perform(put("/api/petty-cash-stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pettyCashStock)))
            .andExpect(status().isBadRequest());

        // Validate the PettyCashStock in the database
        List<PettyCashStock> pettyCashStockList = pettyCashStockRepository.findAll();
        assertThat(pettyCashStockList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PettyCashStock in Elasticsearch
        verify(mockPettyCashStockSearchRepository, times(0)).save(pettyCashStock);
    }

    @Test
    public void deletePettyCashStock() throws Exception {
        // Initialize the database
        pettyCashStockRepository.save(pettyCashStock);

        int databaseSizeBeforeDelete = pettyCashStockRepository.findAll().size();

        // Delete the pettyCashStock
        restPettyCashStockMockMvc.perform(delete("/api/petty-cash-stocks/{id}", pettyCashStock.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PettyCashStock> pettyCashStockList = pettyCashStockRepository.findAll();
        assertThat(pettyCashStockList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PettyCashStock in Elasticsearch
        verify(mockPettyCashStockSearchRepository, times(1)).deleteById(pettyCashStock.getId());
    }

    @Test
    public void searchPettyCashStock() throws Exception {
        // Initialize the database
        pettyCashStockRepository.save(pettyCashStock);
        when(mockPettyCashStockSearchRepository.search(queryStringQuery("id:" + pettyCashStock.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(pettyCashStock), PageRequest.of(0, 1), 1));
        // Search the pettyCashStock
        restPettyCashStockMockMvc.perform(get("/api/_search/petty-cash-stocks?query=id:" + pettyCashStock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pettyCashStock.getId())))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].modelNumber").value(hasItem(DEFAULT_MODEL_NUMBER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].discountedPrice").value(hasItem(DEFAULT_DISCOUNTED_PRICE)))
            .andExpect(jsonPath("$.[*].stockInDate").value(hasItem(DEFAULT_STOCK_IN_DATE.toString())))
            .andExpect(jsonPath("$.[*].estNextStockInDate").value(hasItem(DEFAULT_EST_NEXT_STOCK_IN_DATE.toString())));
    }
}
