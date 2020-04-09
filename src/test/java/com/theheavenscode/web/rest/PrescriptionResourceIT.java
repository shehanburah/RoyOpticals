package com.theheavenscode.web.rest;

import com.theheavenscode.RoyOpticalsApp;
import com.theheavenscode.domain.Prescription;
import com.theheavenscode.repository.PrescriptionRepository;
import com.theheavenscode.repository.search.PrescriptionSearchRepository;
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
import org.springframework.util.Base64Utils;
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
 * Integration tests for the {@link PrescriptionResource} REST controller.
 */
@SpringBootTest(classes = RoyOpticalsApp.class)
public class PrescriptionResourceIT {

    private static final String DEFAULT_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_INVOICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    /**
     * This repository is mocked in the com.theheavenscode.repository.search test package.
     *
     * @see com.theheavenscode.repository.search.PrescriptionSearchRepositoryMockConfiguration
     */
    @Autowired
    private PrescriptionSearchRepository mockPrescriptionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restPrescriptionMockMvc;

    private Prescription prescription;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PrescriptionResource prescriptionResource = new PrescriptionResource(prescriptionRepository, mockPrescriptionSearchRepository);
        this.restPrescriptionMockMvc = MockMvcBuilders.standaloneSetup(prescriptionResource)
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
    public static Prescription createEntity() {
        Prescription prescription = new Prescription()
            .customerId(DEFAULT_CUSTOMER_ID)
            .invoiceId(DEFAULT_INVOICE_ID)
            .name(DEFAULT_NAME)
            .date(DEFAULT_DATE)
            .description(DEFAULT_DESCRIPTION);
        return prescription;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prescription createUpdatedEntity() {
        Prescription prescription = new Prescription()
            .customerId(UPDATED_CUSTOMER_ID)
            .invoiceId(UPDATED_INVOICE_ID)
            .name(UPDATED_NAME)
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION);
        return prescription;
    }

    @BeforeEach
    public void initTest() {
        prescriptionRepository.deleteAll();
        prescription = createEntity();
    }

    @Test
    public void createPrescription() throws Exception {
        int databaseSizeBeforeCreate = prescriptionRepository.findAll().size();

        // Create the Prescription
        restPrescriptionMockMvc.perform(post("/api/prescriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prescription)))
            .andExpect(status().isCreated());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeCreate + 1);
        Prescription testPrescription = prescriptionList.get(prescriptionList.size() - 1);
        assertThat(testPrescription.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testPrescription.getInvoiceId()).isEqualTo(DEFAULT_INVOICE_ID);
        assertThat(testPrescription.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPrescription.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPrescription.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Prescription in Elasticsearch
        verify(mockPrescriptionSearchRepository, times(1)).save(testPrescription);
    }

    @Test
    public void createPrescriptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = prescriptionRepository.findAll().size();

        // Create the Prescription with an existing ID
        prescription.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrescriptionMockMvc.perform(post("/api/prescriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prescription)))
            .andExpect(status().isBadRequest());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Prescription in Elasticsearch
        verify(mockPrescriptionSearchRepository, times(0)).save(prescription);
    }


    @Test
    public void getAllPrescriptions() throws Exception {
        // Initialize the database
        prescriptionRepository.save(prescription);

        // Get all the prescriptionList
        restPrescriptionMockMvc.perform(get("/api/prescriptions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prescription.getId())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].invoiceId").value(hasItem(DEFAULT_INVOICE_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    public void getPrescription() throws Exception {
        // Initialize the database
        prescriptionRepository.save(prescription);

        // Get the prescription
        restPrescriptionMockMvc.perform(get("/api/prescriptions/{id}", prescription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(prescription.getId()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID))
            .andExpect(jsonPath("$.invoiceId").value(DEFAULT_INVOICE_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    public void getNonExistingPrescription() throws Exception {
        // Get the prescription
        restPrescriptionMockMvc.perform(get("/api/prescriptions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updatePrescription() throws Exception {
        // Initialize the database
        prescriptionRepository.save(prescription);

        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();

        // Update the prescription
        Prescription updatedPrescription = prescriptionRepository.findById(prescription.getId()).get();
        updatedPrescription
            .customerId(UPDATED_CUSTOMER_ID)
            .invoiceId(UPDATED_INVOICE_ID)
            .name(UPDATED_NAME)
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION);

        restPrescriptionMockMvc.perform(put("/api/prescriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPrescription)))
            .andExpect(status().isOk());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);
        Prescription testPrescription = prescriptionList.get(prescriptionList.size() - 1);
        assertThat(testPrescription.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testPrescription.getInvoiceId()).isEqualTo(UPDATED_INVOICE_ID);
        assertThat(testPrescription.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPrescription.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPrescription.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Prescription in Elasticsearch
        verify(mockPrescriptionSearchRepository, times(1)).save(testPrescription);
    }

    @Test
    public void updateNonExistingPrescription() throws Exception {
        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();

        // Create the Prescription

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrescriptionMockMvc.perform(put("/api/prescriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prescription)))
            .andExpect(status().isBadRequest());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Prescription in Elasticsearch
        verify(mockPrescriptionSearchRepository, times(0)).save(prescription);
    }

    @Test
    public void deletePrescription() throws Exception {
        // Initialize the database
        prescriptionRepository.save(prescription);

        int databaseSizeBeforeDelete = prescriptionRepository.findAll().size();

        // Delete the prescription
        restPrescriptionMockMvc.perform(delete("/api/prescriptions/{id}", prescription.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Prescription in Elasticsearch
        verify(mockPrescriptionSearchRepository, times(1)).deleteById(prescription.getId());
    }

    @Test
    public void searchPrescription() throws Exception {
        // Initialize the database
        prescriptionRepository.save(prescription);
        when(mockPrescriptionSearchRepository.search(queryStringQuery("id:" + prescription.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(prescription), PageRequest.of(0, 1), 1));
        // Search the prescription
        restPrescriptionMockMvc.perform(get("/api/_search/prescriptions?query=id:" + prescription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prescription.getId())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].invoiceId").value(hasItem(DEFAULT_INVOICE_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
