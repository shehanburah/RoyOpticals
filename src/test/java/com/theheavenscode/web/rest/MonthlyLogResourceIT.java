package com.theheavenscode.web.rest;

import com.theheavenscode.RoyOpticalsApp;
import com.theheavenscode.domain.MonthlyLog;
import com.theheavenscode.repository.MonthlyLogRepository;
import com.theheavenscode.repository.search.MonthlyLogSearchRepository;
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
 * Integration tests for the {@link MonthlyLogResource} REST controller.
 */
@SpringBootTest(classes = RoyOpticalsApp.class)
public class MonthlyLogResourceIT {

    private static final Integer DEFAULT_IDENTIFICATION = 1;
    private static final Integer UPDATED_IDENTIFICATION = 2;

    private static final String DEFAULT_REPORT_OBTAINED_BY = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_OBTAINED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    @Autowired
    private MonthlyLogRepository monthlyLogRepository;

    /**
     * This repository is mocked in the com.theheavenscode.repository.search test package.
     *
     * @see com.theheavenscode.repository.search.MonthlyLogSearchRepositoryMockConfiguration
     */
    @Autowired
    private MonthlyLogSearchRepository mockMonthlyLogSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restMonthlyLogMockMvc;

    private MonthlyLog monthlyLog;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MonthlyLogResource monthlyLogResource = new MonthlyLogResource(monthlyLogRepository, mockMonthlyLogSearchRepository);
        this.restMonthlyLogMockMvc = MockMvcBuilders.standaloneSetup(monthlyLogResource)
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
    public static MonthlyLog createEntity() {
        MonthlyLog monthlyLog = new MonthlyLog()
            .identification(DEFAULT_IDENTIFICATION)
            .reportObtainedBy(DEFAULT_REPORT_OBTAINED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .amount(DEFAULT_AMOUNT)
            .remark(DEFAULT_REMARK);
        return monthlyLog;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonthlyLog createUpdatedEntity() {
        MonthlyLog monthlyLog = new MonthlyLog()
            .identification(UPDATED_IDENTIFICATION)
            .reportObtainedBy(UPDATED_REPORT_OBTAINED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .amount(UPDATED_AMOUNT)
            .remark(UPDATED_REMARK);
        return monthlyLog;
    }

    @BeforeEach
    public void initTest() {
        monthlyLogRepository.deleteAll();
        monthlyLog = createEntity();
    }

    @Test
    public void createMonthlyLog() throws Exception {
        int databaseSizeBeforeCreate = monthlyLogRepository.findAll().size();

        // Create the MonthlyLog
        restMonthlyLogMockMvc.perform(post("/api/monthly-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(monthlyLog)))
            .andExpect(status().isCreated());

        // Validate the MonthlyLog in the database
        List<MonthlyLog> monthlyLogList = monthlyLogRepository.findAll();
        assertThat(monthlyLogList).hasSize(databaseSizeBeforeCreate + 1);
        MonthlyLog testMonthlyLog = monthlyLogList.get(monthlyLogList.size() - 1);
        assertThat(testMonthlyLog.getIdentification()).isEqualTo(DEFAULT_IDENTIFICATION);
        assertThat(testMonthlyLog.getReportObtainedBy()).isEqualTo(DEFAULT_REPORT_OBTAINED_BY);
        assertThat(testMonthlyLog.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testMonthlyLog.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testMonthlyLog.getRemark()).isEqualTo(DEFAULT_REMARK);

        // Validate the MonthlyLog in Elasticsearch
        verify(mockMonthlyLogSearchRepository, times(1)).save(testMonthlyLog);
    }

    @Test
    public void createMonthlyLogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = monthlyLogRepository.findAll().size();

        // Create the MonthlyLog with an existing ID
        monthlyLog.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonthlyLogMockMvc.perform(post("/api/monthly-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(monthlyLog)))
            .andExpect(status().isBadRequest());

        // Validate the MonthlyLog in the database
        List<MonthlyLog> monthlyLogList = monthlyLogRepository.findAll();
        assertThat(monthlyLogList).hasSize(databaseSizeBeforeCreate);

        // Validate the MonthlyLog in Elasticsearch
        verify(mockMonthlyLogSearchRepository, times(0)).save(monthlyLog);
    }


    @Test
    public void getAllMonthlyLogs() throws Exception {
        // Initialize the database
        monthlyLogRepository.save(monthlyLog);

        // Get all the monthlyLogList
        restMonthlyLogMockMvc.perform(get("/api/monthly-logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monthlyLog.getId())))
            .andExpect(jsonPath("$.[*].identification").value(hasItem(DEFAULT_IDENTIFICATION)))
            .andExpect(jsonPath("$.[*].reportObtainedBy").value(hasItem(DEFAULT_REPORT_OBTAINED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)));
    }
    
    @Test
    public void getMonthlyLog() throws Exception {
        // Initialize the database
        monthlyLogRepository.save(monthlyLog);

        // Get the monthlyLog
        restMonthlyLogMockMvc.perform(get("/api/monthly-logs/{id}", monthlyLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(monthlyLog.getId()))
            .andExpect(jsonPath("$.identification").value(DEFAULT_IDENTIFICATION))
            .andExpect(jsonPath("$.reportObtainedBy").value(DEFAULT_REPORT_OBTAINED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK));
    }

    @Test
    public void getNonExistingMonthlyLog() throws Exception {
        // Get the monthlyLog
        restMonthlyLogMockMvc.perform(get("/api/monthly-logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateMonthlyLog() throws Exception {
        // Initialize the database
        monthlyLogRepository.save(monthlyLog);

        int databaseSizeBeforeUpdate = monthlyLogRepository.findAll().size();

        // Update the monthlyLog
        MonthlyLog updatedMonthlyLog = monthlyLogRepository.findById(monthlyLog.getId()).get();
        updatedMonthlyLog
            .identification(UPDATED_IDENTIFICATION)
            .reportObtainedBy(UPDATED_REPORT_OBTAINED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .amount(UPDATED_AMOUNT)
            .remark(UPDATED_REMARK);

        restMonthlyLogMockMvc.perform(put("/api/monthly-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMonthlyLog)))
            .andExpect(status().isOk());

        // Validate the MonthlyLog in the database
        List<MonthlyLog> monthlyLogList = monthlyLogRepository.findAll();
        assertThat(monthlyLogList).hasSize(databaseSizeBeforeUpdate);
        MonthlyLog testMonthlyLog = monthlyLogList.get(monthlyLogList.size() - 1);
        assertThat(testMonthlyLog.getIdentification()).isEqualTo(UPDATED_IDENTIFICATION);
        assertThat(testMonthlyLog.getReportObtainedBy()).isEqualTo(UPDATED_REPORT_OBTAINED_BY);
        assertThat(testMonthlyLog.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testMonthlyLog.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testMonthlyLog.getRemark()).isEqualTo(UPDATED_REMARK);

        // Validate the MonthlyLog in Elasticsearch
        verify(mockMonthlyLogSearchRepository, times(1)).save(testMonthlyLog);
    }

    @Test
    public void updateNonExistingMonthlyLog() throws Exception {
        int databaseSizeBeforeUpdate = monthlyLogRepository.findAll().size();

        // Create the MonthlyLog

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonthlyLogMockMvc.perform(put("/api/monthly-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(monthlyLog)))
            .andExpect(status().isBadRequest());

        // Validate the MonthlyLog in the database
        List<MonthlyLog> monthlyLogList = monthlyLogRepository.findAll();
        assertThat(monthlyLogList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MonthlyLog in Elasticsearch
        verify(mockMonthlyLogSearchRepository, times(0)).save(monthlyLog);
    }

    @Test
    public void deleteMonthlyLog() throws Exception {
        // Initialize the database
        monthlyLogRepository.save(monthlyLog);

        int databaseSizeBeforeDelete = monthlyLogRepository.findAll().size();

        // Delete the monthlyLog
        restMonthlyLogMockMvc.perform(delete("/api/monthly-logs/{id}", monthlyLog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MonthlyLog> monthlyLogList = monthlyLogRepository.findAll();
        assertThat(monthlyLogList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MonthlyLog in Elasticsearch
        verify(mockMonthlyLogSearchRepository, times(1)).deleteById(monthlyLog.getId());
    }

    @Test
    public void searchMonthlyLog() throws Exception {
        // Initialize the database
        monthlyLogRepository.save(monthlyLog);
        when(mockMonthlyLogSearchRepository.search(queryStringQuery("id:" + monthlyLog.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(monthlyLog), PageRequest.of(0, 1), 1));
        // Search the monthlyLog
        restMonthlyLogMockMvc.perform(get("/api/_search/monthly-logs?query=id:" + monthlyLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monthlyLog.getId())))
            .andExpect(jsonPath("$.[*].identification").value(hasItem(DEFAULT_IDENTIFICATION)))
            .andExpect(jsonPath("$.[*].reportObtainedBy").value(hasItem(DEFAULT_REPORT_OBTAINED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)));
    }
}
