package com.theheavenscode.web.rest;

import com.theheavenscode.RoyOpticalsApp;
import com.theheavenscode.domain.SolderingJobInvoice;
import com.theheavenscode.repository.SolderingJobInvoiceRepository;
import com.theheavenscode.repository.search.SolderingJobInvoiceSearchRepository;
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
 * Integration tests for the {@link SolderingJobInvoiceResource} REST controller.
 */
@SpringBootTest(classes = RoyOpticalsApp.class)
public class SolderingJobInvoiceResourceIT {

    private static final String DEFAULT_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_ID = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final LocalDate DEFAULT_ORDER_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ORDER_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_DELIVERED = false;
    private static final Boolean UPDATED_DELIVERED = true;

    private static final LocalDate DEFAULT_DELIVERY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DELIVERY_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private SolderingJobInvoiceRepository solderingJobInvoiceRepository;

    /**
     * This repository is mocked in the com.theheavenscode.repository.search test package.
     *
     * @see com.theheavenscode.repository.search.SolderingJobInvoiceSearchRepositoryMockConfiguration
     */
    @Autowired
    private SolderingJobInvoiceSearchRepository mockSolderingJobInvoiceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restSolderingJobInvoiceMockMvc;

    private SolderingJobInvoice solderingJobInvoice;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SolderingJobInvoiceResource solderingJobInvoiceResource = new SolderingJobInvoiceResource(solderingJobInvoiceRepository, mockSolderingJobInvoiceSearchRepository);
        this.restSolderingJobInvoiceMockMvc = MockMvcBuilders.standaloneSetup(solderingJobInvoiceResource)
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
    public static SolderingJobInvoice createEntity() {
        SolderingJobInvoice solderingJobInvoice = new SolderingJobInvoice()
            .customerId(DEFAULT_CUSTOMER_ID)
            .price(DEFAULT_PRICE)
            .orderDate(DEFAULT_ORDER_DATE)
            .delivered(DEFAULT_DELIVERED)
            .deliveryDate(DEFAULT_DELIVERY_DATE);
        return solderingJobInvoice;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SolderingJobInvoice createUpdatedEntity() {
        SolderingJobInvoice solderingJobInvoice = new SolderingJobInvoice()
            .customerId(UPDATED_CUSTOMER_ID)
            .price(UPDATED_PRICE)
            .orderDate(UPDATED_ORDER_DATE)
            .delivered(UPDATED_DELIVERED)
            .deliveryDate(UPDATED_DELIVERY_DATE);
        return solderingJobInvoice;
    }

    @BeforeEach
    public void initTest() {
        solderingJobInvoiceRepository.deleteAll();
        solderingJobInvoice = createEntity();
    }

    @Test
    public void createSolderingJobInvoice() throws Exception {
        int databaseSizeBeforeCreate = solderingJobInvoiceRepository.findAll().size();

        // Create the SolderingJobInvoice
        restSolderingJobInvoiceMockMvc.perform(post("/api/soldering-job-invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solderingJobInvoice)))
            .andExpect(status().isCreated());

        // Validate the SolderingJobInvoice in the database
        List<SolderingJobInvoice> solderingJobInvoiceList = solderingJobInvoiceRepository.findAll();
        assertThat(solderingJobInvoiceList).hasSize(databaseSizeBeforeCreate + 1);
        SolderingJobInvoice testSolderingJobInvoice = solderingJobInvoiceList.get(solderingJobInvoiceList.size() - 1);
        assertThat(testSolderingJobInvoice.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testSolderingJobInvoice.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testSolderingJobInvoice.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testSolderingJobInvoice.isDelivered()).isEqualTo(DEFAULT_DELIVERED);
        assertThat(testSolderingJobInvoice.getDeliveryDate()).isEqualTo(DEFAULT_DELIVERY_DATE);

        // Validate the SolderingJobInvoice in Elasticsearch
        verify(mockSolderingJobInvoiceSearchRepository, times(1)).save(testSolderingJobInvoice);
    }

    @Test
    public void createSolderingJobInvoiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = solderingJobInvoiceRepository.findAll().size();

        // Create the SolderingJobInvoice with an existing ID
        solderingJobInvoice.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restSolderingJobInvoiceMockMvc.perform(post("/api/soldering-job-invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solderingJobInvoice)))
            .andExpect(status().isBadRequest());

        // Validate the SolderingJobInvoice in the database
        List<SolderingJobInvoice> solderingJobInvoiceList = solderingJobInvoiceRepository.findAll();
        assertThat(solderingJobInvoiceList).hasSize(databaseSizeBeforeCreate);

        // Validate the SolderingJobInvoice in Elasticsearch
        verify(mockSolderingJobInvoiceSearchRepository, times(0)).save(solderingJobInvoice);
    }


    @Test
    public void getAllSolderingJobInvoices() throws Exception {
        // Initialize the database
        solderingJobInvoiceRepository.save(solderingJobInvoice);

        // Get all the solderingJobInvoiceList
        restSolderingJobInvoiceMockMvc.perform(get("/api/soldering-job-invoices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solderingJobInvoice.getId())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].delivered").value(hasItem(DEFAULT_DELIVERED.booleanValue())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }
    
    @Test
    public void getSolderingJobInvoice() throws Exception {
        // Initialize the database
        solderingJobInvoiceRepository.save(solderingJobInvoice);

        // Get the solderingJobInvoice
        restSolderingJobInvoiceMockMvc.perform(get("/api/soldering-job-invoices/{id}", solderingJobInvoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(solderingJobInvoice.getId()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.delivered").value(DEFAULT_DELIVERED.booleanValue()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    public void getNonExistingSolderingJobInvoice() throws Exception {
        // Get the solderingJobInvoice
        restSolderingJobInvoiceMockMvc.perform(get("/api/soldering-job-invoices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateSolderingJobInvoice() throws Exception {
        // Initialize the database
        solderingJobInvoiceRepository.save(solderingJobInvoice);

        int databaseSizeBeforeUpdate = solderingJobInvoiceRepository.findAll().size();

        // Update the solderingJobInvoice
        SolderingJobInvoice updatedSolderingJobInvoice = solderingJobInvoiceRepository.findById(solderingJobInvoice.getId()).get();
        updatedSolderingJobInvoice
            .customerId(UPDATED_CUSTOMER_ID)
            .price(UPDATED_PRICE)
            .orderDate(UPDATED_ORDER_DATE)
            .delivered(UPDATED_DELIVERED)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restSolderingJobInvoiceMockMvc.perform(put("/api/soldering-job-invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSolderingJobInvoice)))
            .andExpect(status().isOk());

        // Validate the SolderingJobInvoice in the database
        List<SolderingJobInvoice> solderingJobInvoiceList = solderingJobInvoiceRepository.findAll();
        assertThat(solderingJobInvoiceList).hasSize(databaseSizeBeforeUpdate);
        SolderingJobInvoice testSolderingJobInvoice = solderingJobInvoiceList.get(solderingJobInvoiceList.size() - 1);
        assertThat(testSolderingJobInvoice.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testSolderingJobInvoice.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testSolderingJobInvoice.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testSolderingJobInvoice.isDelivered()).isEqualTo(UPDATED_DELIVERED);
        assertThat(testSolderingJobInvoice.getDeliveryDate()).isEqualTo(UPDATED_DELIVERY_DATE);

        // Validate the SolderingJobInvoice in Elasticsearch
        verify(mockSolderingJobInvoiceSearchRepository, times(1)).save(testSolderingJobInvoice);
    }

    @Test
    public void updateNonExistingSolderingJobInvoice() throws Exception {
        int databaseSizeBeforeUpdate = solderingJobInvoiceRepository.findAll().size();

        // Create the SolderingJobInvoice

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSolderingJobInvoiceMockMvc.perform(put("/api/soldering-job-invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solderingJobInvoice)))
            .andExpect(status().isBadRequest());

        // Validate the SolderingJobInvoice in the database
        List<SolderingJobInvoice> solderingJobInvoiceList = solderingJobInvoiceRepository.findAll();
        assertThat(solderingJobInvoiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SolderingJobInvoice in Elasticsearch
        verify(mockSolderingJobInvoiceSearchRepository, times(0)).save(solderingJobInvoice);
    }

    @Test
    public void deleteSolderingJobInvoice() throws Exception {
        // Initialize the database
        solderingJobInvoiceRepository.save(solderingJobInvoice);

        int databaseSizeBeforeDelete = solderingJobInvoiceRepository.findAll().size();

        // Delete the solderingJobInvoice
        restSolderingJobInvoiceMockMvc.perform(delete("/api/soldering-job-invoices/{id}", solderingJobInvoice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SolderingJobInvoice> solderingJobInvoiceList = solderingJobInvoiceRepository.findAll();
        assertThat(solderingJobInvoiceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SolderingJobInvoice in Elasticsearch
        verify(mockSolderingJobInvoiceSearchRepository, times(1)).deleteById(solderingJobInvoice.getId());
    }

    @Test
    public void searchSolderingJobInvoice() throws Exception {
        // Initialize the database
        solderingJobInvoiceRepository.save(solderingJobInvoice);
        when(mockSolderingJobInvoiceSearchRepository.search(queryStringQuery("id:" + solderingJobInvoice.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(solderingJobInvoice), PageRequest.of(0, 1), 1));
        // Search the solderingJobInvoice
        restSolderingJobInvoiceMockMvc.perform(get("/api/_search/soldering-job-invoices?query=id:" + solderingJobInvoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solderingJobInvoice.getId())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].delivered").value(hasItem(DEFAULT_DELIVERED.booleanValue())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }
}
