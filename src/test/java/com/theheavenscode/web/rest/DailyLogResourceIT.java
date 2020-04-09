package com.theheavenscode.web.rest;

import com.theheavenscode.RoyOpticalsApp;
import com.theheavenscode.domain.DailyLog;
import com.theheavenscode.repository.DailyLogRepository;
import com.theheavenscode.repository.search.DailyLogSearchRepository;
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
 * Integration tests for the {@link DailyLogResource} REST controller.
 */
@SpringBootTest(classes = RoyOpticalsApp.class)
public class DailyLogResourceIT {

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_RX = 1;
    private static final Integer UPDATED_RX = 2;

    private static final String DEFAULT_INVOICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SOLDERING_JOB_INVOICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SOLDERING_JOB_INVOICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    @Autowired
    private DailyLogRepository dailyLogRepository;

    /**
     * This repository is mocked in the com.theheavenscode.repository.search test package.
     *
     * @see com.theheavenscode.repository.search.DailyLogSearchRepositoryMockConfiguration
     */
    @Autowired
    private DailyLogSearchRepository mockDailyLogSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restDailyLogMockMvc;

    private DailyLog dailyLog;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DailyLogResource dailyLogResource = new DailyLogResource(dailyLogRepository, mockDailyLogSearchRepository);
        this.restDailyLogMockMvc = MockMvcBuilders.standaloneSetup(dailyLogResource)
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
    public static DailyLog createEntity() {
        DailyLog dailyLog = new DailyLog()
            .createdDate(DEFAULT_CREATED_DATE)
            .rx(DEFAULT_RX)
            .invoiceId(DEFAULT_INVOICE_ID)
            .solderingJobInvoiceId(DEFAULT_SOLDERING_JOB_INVOICE_ID)
            .customer(DEFAULT_CUSTOMER)
            .customerId(DEFAULT_CUSTOMER_ID)
            .description(DEFAULT_DESCRIPTION)
            .amount(DEFAULT_AMOUNT);
        return dailyLog;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DailyLog createUpdatedEntity() {
        DailyLog dailyLog = new DailyLog()
            .createdDate(UPDATED_CREATED_DATE)
            .rx(UPDATED_RX)
            .invoiceId(UPDATED_INVOICE_ID)
            .solderingJobInvoiceId(UPDATED_SOLDERING_JOB_INVOICE_ID)
            .customer(UPDATED_CUSTOMER)
            .customerId(UPDATED_CUSTOMER_ID)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT);
        return dailyLog;
    }

    @BeforeEach
    public void initTest() {
        dailyLogRepository.deleteAll();
        dailyLog = createEntity();
    }

    @Test
    public void createDailyLog() throws Exception {
        int databaseSizeBeforeCreate = dailyLogRepository.findAll().size();

        // Create the DailyLog
        restDailyLogMockMvc.perform(post("/api/daily-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dailyLog)))
            .andExpect(status().isCreated());

        // Validate the DailyLog in the database
        List<DailyLog> dailyLogList = dailyLogRepository.findAll();
        assertThat(dailyLogList).hasSize(databaseSizeBeforeCreate + 1);
        DailyLog testDailyLog = dailyLogList.get(dailyLogList.size() - 1);
        assertThat(testDailyLog.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDailyLog.getRx()).isEqualTo(DEFAULT_RX);
        assertThat(testDailyLog.getInvoiceId()).isEqualTo(DEFAULT_INVOICE_ID);
        assertThat(testDailyLog.getSolderingJobInvoiceId()).isEqualTo(DEFAULT_SOLDERING_JOB_INVOICE_ID);
        assertThat(testDailyLog.getCustomer()).isEqualTo(DEFAULT_CUSTOMER);
        assertThat(testDailyLog.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testDailyLog.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDailyLog.getAmount()).isEqualTo(DEFAULT_AMOUNT);

        // Validate the DailyLog in Elasticsearch
        verify(mockDailyLogSearchRepository, times(1)).save(testDailyLog);
    }

    @Test
    public void createDailyLogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dailyLogRepository.findAll().size();

        // Create the DailyLog with an existing ID
        dailyLog.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restDailyLogMockMvc.perform(post("/api/daily-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dailyLog)))
            .andExpect(status().isBadRequest());

        // Validate the DailyLog in the database
        List<DailyLog> dailyLogList = dailyLogRepository.findAll();
        assertThat(dailyLogList).hasSize(databaseSizeBeforeCreate);

        // Validate the DailyLog in Elasticsearch
        verify(mockDailyLogSearchRepository, times(0)).save(dailyLog);
    }


    @Test
    public void getAllDailyLogs() throws Exception {
        // Initialize the database
        dailyLogRepository.save(dailyLog);

        // Get all the dailyLogList
        restDailyLogMockMvc.perform(get("/api/daily-logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dailyLog.getId())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].rx").value(hasItem(DEFAULT_RX)))
            .andExpect(jsonPath("$.[*].invoiceId").value(hasItem(DEFAULT_INVOICE_ID)))
            .andExpect(jsonPath("$.[*].solderingJobInvoiceId").value(hasItem(DEFAULT_SOLDERING_JOB_INVOICE_ID)))
            .andExpect(jsonPath("$.[*].customer").value(hasItem(DEFAULT_CUSTOMER)))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }
    
    @Test
    public void getDailyLog() throws Exception {
        // Initialize the database
        dailyLogRepository.save(dailyLog);

        // Get the dailyLog
        restDailyLogMockMvc.perform(get("/api/daily-logs/{id}", dailyLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dailyLog.getId()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.rx").value(DEFAULT_RX))
            .andExpect(jsonPath("$.invoiceId").value(DEFAULT_INVOICE_ID))
            .andExpect(jsonPath("$.solderingJobInvoiceId").value(DEFAULT_SOLDERING_JOB_INVOICE_ID))
            .andExpect(jsonPath("$.customer").value(DEFAULT_CUSTOMER))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    public void getNonExistingDailyLog() throws Exception {
        // Get the dailyLog
        restDailyLogMockMvc.perform(get("/api/daily-logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateDailyLog() throws Exception {
        // Initialize the database
        dailyLogRepository.save(dailyLog);

        int databaseSizeBeforeUpdate = dailyLogRepository.findAll().size();

        // Update the dailyLog
        DailyLog updatedDailyLog = dailyLogRepository.findById(dailyLog.getId()).get();
        updatedDailyLog
            .createdDate(UPDATED_CREATED_DATE)
            .rx(UPDATED_RX)
            .invoiceId(UPDATED_INVOICE_ID)
            .solderingJobInvoiceId(UPDATED_SOLDERING_JOB_INVOICE_ID)
            .customer(UPDATED_CUSTOMER)
            .customerId(UPDATED_CUSTOMER_ID)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT);

        restDailyLogMockMvc.perform(put("/api/daily-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDailyLog)))
            .andExpect(status().isOk());

        // Validate the DailyLog in the database
        List<DailyLog> dailyLogList = dailyLogRepository.findAll();
        assertThat(dailyLogList).hasSize(databaseSizeBeforeUpdate);
        DailyLog testDailyLog = dailyLogList.get(dailyLogList.size() - 1);
        assertThat(testDailyLog.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDailyLog.getRx()).isEqualTo(UPDATED_RX);
        assertThat(testDailyLog.getInvoiceId()).isEqualTo(UPDATED_INVOICE_ID);
        assertThat(testDailyLog.getSolderingJobInvoiceId()).isEqualTo(UPDATED_SOLDERING_JOB_INVOICE_ID);
        assertThat(testDailyLog.getCustomer()).isEqualTo(UPDATED_CUSTOMER);
        assertThat(testDailyLog.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testDailyLog.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDailyLog.getAmount()).isEqualTo(UPDATED_AMOUNT);

        // Validate the DailyLog in Elasticsearch
        verify(mockDailyLogSearchRepository, times(1)).save(testDailyLog);
    }

    @Test
    public void updateNonExistingDailyLog() throws Exception {
        int databaseSizeBeforeUpdate = dailyLogRepository.findAll().size();

        // Create the DailyLog

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDailyLogMockMvc.perform(put("/api/daily-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dailyLog)))
            .andExpect(status().isBadRequest());

        // Validate the DailyLog in the database
        List<DailyLog> dailyLogList = dailyLogRepository.findAll();
        assertThat(dailyLogList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DailyLog in Elasticsearch
        verify(mockDailyLogSearchRepository, times(0)).save(dailyLog);
    }

    @Test
    public void deleteDailyLog() throws Exception {
        // Initialize the database
        dailyLogRepository.save(dailyLog);

        int databaseSizeBeforeDelete = dailyLogRepository.findAll().size();

        // Delete the dailyLog
        restDailyLogMockMvc.perform(delete("/api/daily-logs/{id}", dailyLog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DailyLog> dailyLogList = dailyLogRepository.findAll();
        assertThat(dailyLogList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DailyLog in Elasticsearch
        verify(mockDailyLogSearchRepository, times(1)).deleteById(dailyLog.getId());
    }

    @Test
    public void searchDailyLog() throws Exception {
        // Initialize the database
        dailyLogRepository.save(dailyLog);
        when(mockDailyLogSearchRepository.search(queryStringQuery("id:" + dailyLog.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(dailyLog), PageRequest.of(0, 1), 1));
        // Search the dailyLog
        restDailyLogMockMvc.perform(get("/api/_search/daily-logs?query=id:" + dailyLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dailyLog.getId())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].rx").value(hasItem(DEFAULT_RX)))
            .andExpect(jsonPath("$.[*].invoiceId").value(hasItem(DEFAULT_INVOICE_ID)))
            .andExpect(jsonPath("$.[*].solderingJobInvoiceId").value(hasItem(DEFAULT_SOLDERING_JOB_INVOICE_ID)))
            .andExpect(jsonPath("$.[*].customer").value(hasItem(DEFAULT_CUSTOMER)))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }
}
