package com.theheavenscode.web.rest;

import com.theheavenscode.RoyOpticalsApp;
import com.theheavenscode.domain.Invoice;
import com.theheavenscode.repository.InvoiceRepository;
import com.theheavenscode.repository.search.InvoiceSearchRepository;
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


import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link InvoiceResource} REST controller.
 */
@SpringBootTest(classes = RoyOpticalsApp.class)
public class InvoiceResourceIT {

    private static final String DEFAULT_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_RX = 1;
    private static final Integer UPDATED_RX = 2;

    private static final Instant DEFAULT_DELEVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELEVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_ADVANCE = 1D;
    private static final Double UPDATED_ADVANCE = 2D;

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;

    private static final Boolean DEFAULT_DELIVERED = false;
    private static final Boolean UPDATED_DELIVERED = true;

    private static final Boolean DEFAULT_PAID = false;
    private static final Boolean UPDATED_PAID = true;

    @Autowired
    private InvoiceRepository invoiceRepository;

    /**
     * This repository is mocked in the com.theheavenscode.repository.search test package.
     *
     * @see com.theheavenscode.repository.search.InvoiceSearchRepositoryMockConfiguration
     */
    @Autowired
    private InvoiceSearchRepository mockInvoiceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceResource invoiceResource = new InvoiceResource(invoiceRepository, mockInvoiceSearchRepository);
        this.restInvoiceMockMvc = MockMvcBuilders.standaloneSetup(invoiceResource)
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
    public static Invoice createEntity() {
        Invoice invoice = new Invoice()
            .customerId(DEFAULT_CUSTOMER_ID)
            .rx(DEFAULT_RX)
            .deleveryDate(DEFAULT_DELEVERY_DATE)
            .orderDate(DEFAULT_ORDER_DATE)
            .advance(DEFAULT_ADVANCE)
            .balance(DEFAULT_BALANCE)
            .delivered(DEFAULT_DELIVERED)
            .paid(DEFAULT_PAID);
        return invoice;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createUpdatedEntity() {
        Invoice invoice = new Invoice()
            .customerId(UPDATED_CUSTOMER_ID)
            .rx(UPDATED_RX)
            .deleveryDate(UPDATED_DELEVERY_DATE)
            .orderDate(UPDATED_ORDER_DATE)
            .advance(UPDATED_ADVANCE)
            .balance(UPDATED_BALANCE)
            .delivered(UPDATED_DELIVERED)
            .paid(UPDATED_PAID);
        return invoice;
    }

    @BeforeEach
    public void initTest() {
        invoiceRepository.deleteAll();
        invoice = createEntity();
    }

    @Test
    public void createInvoice() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // Create the Invoice
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testInvoice.getRx()).isEqualTo(DEFAULT_RX);
        assertThat(testInvoice.getDeleveryDate()).isEqualTo(DEFAULT_DELEVERY_DATE);
        assertThat(testInvoice.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testInvoice.getAdvance()).isEqualTo(DEFAULT_ADVANCE);
        assertThat(testInvoice.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testInvoice.isDelivered()).isEqualTo(DEFAULT_DELIVERED);
        assertThat(testInvoice.isPaid()).isEqualTo(DEFAULT_PAID);

        // Validate the Invoice in Elasticsearch
        verify(mockInvoiceSearchRepository, times(1)).save(testInvoice);
    }

    @Test
    public void createInvoiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // Create the Invoice with an existing ID
        invoice.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate);

        // Validate the Invoice in Elasticsearch
        verify(mockInvoiceSearchRepository, times(0)).save(invoice);
    }


    @Test
    public void getAllInvoices() throws Exception {
        // Initialize the database
        invoiceRepository.save(invoice);

        // Get all the invoiceList
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].rx").value(hasItem(DEFAULT_RX)))
            .andExpect(jsonPath("$.[*].deleveryDate").value(hasItem(DEFAULT_DELEVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].advance").value(hasItem(DEFAULT_ADVANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].delivered").value(hasItem(DEFAULT_DELIVERED.booleanValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())));
    }
    
    @Test
    public void getInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.save(invoice);

        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoice.getId()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID))
            .andExpect(jsonPath("$.rx").value(DEFAULT_RX))
            .andExpect(jsonPath("$.deleveryDate").value(DEFAULT_DELEVERY_DATE.toString()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.advance").value(DEFAULT_ADVANCE.doubleValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.delivered").value(DEFAULT_DELIVERED.booleanValue()))
            .andExpect(jsonPath("$.paid").value(DEFAULT_PAID.booleanValue()));
    }

    @Test
    public void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.save(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
        updatedInvoice
            .customerId(UPDATED_CUSTOMER_ID)
            .rx(UPDATED_RX)
            .deleveryDate(UPDATED_DELEVERY_DATE)
            .orderDate(UPDATED_ORDER_DATE)
            .advance(UPDATED_ADVANCE)
            .balance(UPDATED_BALANCE)
            .delivered(UPDATED_DELIVERED)
            .paid(UPDATED_PAID);

        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInvoice)))
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testInvoice.getRx()).isEqualTo(UPDATED_RX);
        assertThat(testInvoice.getDeleveryDate()).isEqualTo(UPDATED_DELEVERY_DATE);
        assertThat(testInvoice.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testInvoice.getAdvance()).isEqualTo(UPDATED_ADVANCE);
        assertThat(testInvoice.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testInvoice.isDelivered()).isEqualTo(UPDATED_DELIVERED);
        assertThat(testInvoice.isPaid()).isEqualTo(UPDATED_PAID);

        // Validate the Invoice in Elasticsearch
        verify(mockInvoiceSearchRepository, times(1)).save(testInvoice);
    }

    @Test
    public void updateNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Create the Invoice

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Invoice in Elasticsearch
        verify(mockInvoiceSearchRepository, times(0)).save(invoice);
    }

    @Test
    public void deleteInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.save(invoice);

        int databaseSizeBeforeDelete = invoiceRepository.findAll().size();

        // Delete the invoice
        restInvoiceMockMvc.perform(delete("/api/invoices/{id}", invoice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Invoice in Elasticsearch
        verify(mockInvoiceSearchRepository, times(1)).deleteById(invoice.getId());
    }

    @Test
    public void searchInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.save(invoice);
        when(mockInvoiceSearchRepository.search(queryStringQuery("id:" + invoice.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(invoice), PageRequest.of(0, 1), 1));
        // Search the invoice
        restInvoiceMockMvc.perform(get("/api/_search/invoices?query=id:" + invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].rx").value(hasItem(DEFAULT_RX)))
            .andExpect(jsonPath("$.[*].deleveryDate").value(hasItem(DEFAULT_DELEVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].advance").value(hasItem(DEFAULT_ADVANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].delivered").value(hasItem(DEFAULT_DELIVERED.booleanValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())));
    }
}
