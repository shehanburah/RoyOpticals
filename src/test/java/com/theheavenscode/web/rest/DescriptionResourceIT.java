package com.theheavenscode.web.rest;

import com.theheavenscode.RoyOpticalsApp;
import com.theheavenscode.domain.Description;
import com.theheavenscode.repository.DescriptionRepository;
import com.theheavenscode.repository.search.DescriptionSearchRepository;
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
 * Integration tests for the {@link DescriptionResource} REST controller.
 */
@SpringBootTest(classes = RoyOpticalsApp.class)
public class DescriptionResourceIT {

    private static final String DEFAULT_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_INVOICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SPH_LEFT = "AAAAAAAAAA";
    private static final String UPDATED_SPH_LEFT = "BBBBBBBBBB";

    private static final String DEFAULT_CYL_LEFT = "AAAAAAAAAA";
    private static final String UPDATED_CYL_LEFT = "BBBBBBBBBB";

    private static final String DEFAULT_AXIS_LEFT = "AAAAAAAAAA";
    private static final String UPDATED_AXIS_LEFT = "BBBBBBBBBB";

    private static final String DEFAULT_READING_LEFT = "AAAAAAAAAA";
    private static final String UPDATED_READING_LEFT = "BBBBBBBBBB";

    private static final String DEFAULT_SPH_RIGHT = "AAAAAAAAAA";
    private static final String UPDATED_SPH_RIGHT = "BBBBBBBBBB";

    private static final String DEFAULT_CYL_RIGHT = "AAAAAAAAAA";
    private static final String UPDATED_CYL_RIGHT = "BBBBBBBBBB";

    private static final String DEFAULT_AXIS_RIGHT = "AAAAAAAAAA";
    private static final String UPDATED_AXIS_RIGHT = "BBBBBBBBBB";

    private static final String DEFAULT_READING_RIGHT = "AAAAAAAAAA";
    private static final String UPDATED_READING_RIGHT = "BBBBBBBBBB";

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELEVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELEVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ORDER_NO = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NO = "BBBBBBBBBB";

    private static final String DEFAULT_FRAME = "AAAAAAAAAA";
    private static final String UPDATED_FRAME = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL_NO = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_NO = "BBBBBBBBBB";

    private static final String DEFAULT_SIZE = "AAAAAAAAAA";
    private static final String UPDATED_SIZE = "BBBBBBBBBB";

    private static final String DEFAULT_COLOUR = "AAAAAAAAAA";
    private static final String UPDATED_COLOUR = "BBBBBBBBBB";

    private static final String DEFAULT_L_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_L_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_L_SIZE = "AAAAAAAAAA";
    private static final String UPDATED_L_SIZE = "BBBBBBBBBB";

    private static final String DEFAULT_PD = "AAAAAAAAAA";
    private static final String UPDATED_PD = "BBBBBBBBBB";

    private static final String DEFAULT_INSET = "AAAAAAAAAA";
    private static final String UPDATED_INSET = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    @Autowired
    private DescriptionRepository descriptionRepository;

    /**
     * This repository is mocked in the com.theheavenscode.repository.search test package.
     *
     * @see com.theheavenscode.repository.search.DescriptionSearchRepositoryMockConfiguration
     */
    @Autowired
    private DescriptionSearchRepository mockDescriptionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restDescriptionMockMvc;

    private Description description;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DescriptionResource descriptionResource = new DescriptionResource(descriptionRepository, mockDescriptionSearchRepository);
        this.restDescriptionMockMvc = MockMvcBuilders.standaloneSetup(descriptionResource)
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
    public static Description createEntity() {
        Description description = new Description()
            .customerId(DEFAULT_CUSTOMER_ID)
            .invoiceId(DEFAULT_INVOICE_ID)
            .sphLeft(DEFAULT_SPH_LEFT)
            .cylLeft(DEFAULT_CYL_LEFT)
            .axisLeft(DEFAULT_AXIS_LEFT)
            .readingLeft(DEFAULT_READING_LEFT)
            .sphRight(DEFAULT_SPH_RIGHT)
            .cylRight(DEFAULT_CYL_RIGHT)
            .axisRight(DEFAULT_AXIS_RIGHT)
            .readingRight(DEFAULT_READING_RIGHT)
            .orderDate(DEFAULT_ORDER_DATE)
            .deleveryDate(DEFAULT_DELEVERY_DATE)
            .orderNo(DEFAULT_ORDER_NO)
            .frame(DEFAULT_FRAME)
            .modelNo(DEFAULT_MODEL_NO)
            .size(DEFAULT_SIZE)
            .colour(DEFAULT_COLOUR)
            .lType(DEFAULT_L_TYPE)
            .lSize(DEFAULT_L_SIZE)
            .pd(DEFAULT_PD)
            .inset(DEFAULT_INSET)
            .name(DEFAULT_NAME)
            .remarks(DEFAULT_REMARKS);
        return description;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Description createUpdatedEntity() {
        Description description = new Description()
            .customerId(UPDATED_CUSTOMER_ID)
            .invoiceId(UPDATED_INVOICE_ID)
            .sphLeft(UPDATED_SPH_LEFT)
            .cylLeft(UPDATED_CYL_LEFT)
            .axisLeft(UPDATED_AXIS_LEFT)
            .readingLeft(UPDATED_READING_LEFT)
            .sphRight(UPDATED_SPH_RIGHT)
            .cylRight(UPDATED_CYL_RIGHT)
            .axisRight(UPDATED_AXIS_RIGHT)
            .readingRight(UPDATED_READING_RIGHT)
            .orderDate(UPDATED_ORDER_DATE)
            .deleveryDate(UPDATED_DELEVERY_DATE)
            .orderNo(UPDATED_ORDER_NO)
            .frame(UPDATED_FRAME)
            .modelNo(UPDATED_MODEL_NO)
            .size(UPDATED_SIZE)
            .colour(UPDATED_COLOUR)
            .lType(UPDATED_L_TYPE)
            .lSize(UPDATED_L_SIZE)
            .pd(UPDATED_PD)
            .inset(UPDATED_INSET)
            .name(UPDATED_NAME)
            .remarks(UPDATED_REMARKS);
        return description;
    }

    @BeforeEach
    public void initTest() {
        descriptionRepository.deleteAll();
        description = createEntity();
    }

    @Test
    public void createDescription() throws Exception {
        int databaseSizeBeforeCreate = descriptionRepository.findAll().size();

        // Create the Description
        restDescriptionMockMvc.perform(post("/api/descriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(description)))
            .andExpect(status().isCreated());

        // Validate the Description in the database
        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeCreate + 1);
        Description testDescription = descriptionList.get(descriptionList.size() - 1);
        assertThat(testDescription.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testDescription.getInvoiceId()).isEqualTo(DEFAULT_INVOICE_ID);
        assertThat(testDescription.getSphLeft()).isEqualTo(DEFAULT_SPH_LEFT);
        assertThat(testDescription.getCylLeft()).isEqualTo(DEFAULT_CYL_LEFT);
        assertThat(testDescription.getAxisLeft()).isEqualTo(DEFAULT_AXIS_LEFT);
        assertThat(testDescription.getReadingLeft()).isEqualTo(DEFAULT_READING_LEFT);
        assertThat(testDescription.getSphRight()).isEqualTo(DEFAULT_SPH_RIGHT);
        assertThat(testDescription.getCylRight()).isEqualTo(DEFAULT_CYL_RIGHT);
        assertThat(testDescription.getAxisRight()).isEqualTo(DEFAULT_AXIS_RIGHT);
        assertThat(testDescription.getReadingRight()).isEqualTo(DEFAULT_READING_RIGHT);
        assertThat(testDescription.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testDescription.getDeleveryDate()).isEqualTo(DEFAULT_DELEVERY_DATE);
        assertThat(testDescription.getOrderNo()).isEqualTo(DEFAULT_ORDER_NO);
        assertThat(testDescription.getFrame()).isEqualTo(DEFAULT_FRAME);
        assertThat(testDescription.getModelNo()).isEqualTo(DEFAULT_MODEL_NO);
        assertThat(testDescription.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testDescription.getColour()).isEqualTo(DEFAULT_COLOUR);
        assertThat(testDescription.getlType()).isEqualTo(DEFAULT_L_TYPE);
        assertThat(testDescription.getlSize()).isEqualTo(DEFAULT_L_SIZE);
        assertThat(testDescription.getPd()).isEqualTo(DEFAULT_PD);
        assertThat(testDescription.getInset()).isEqualTo(DEFAULT_INSET);
        assertThat(testDescription.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDescription.getRemarks()).isEqualTo(DEFAULT_REMARKS);

        // Validate the Description in Elasticsearch
        verify(mockDescriptionSearchRepository, times(1)).save(testDescription);
    }

    @Test
    public void createDescriptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = descriptionRepository.findAll().size();

        // Create the Description with an existing ID
        description.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restDescriptionMockMvc.perform(post("/api/descriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(description)))
            .andExpect(status().isBadRequest());

        // Validate the Description in the database
        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Description in Elasticsearch
        verify(mockDescriptionSearchRepository, times(0)).save(description);
    }


    @Test
    public void getAllDescriptions() throws Exception {
        // Initialize the database
        descriptionRepository.save(description);

        // Get all the descriptionList
        restDescriptionMockMvc.perform(get("/api/descriptions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(description.getId())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].invoiceId").value(hasItem(DEFAULT_INVOICE_ID)))
            .andExpect(jsonPath("$.[*].sphLeft").value(hasItem(DEFAULT_SPH_LEFT)))
            .andExpect(jsonPath("$.[*].cylLeft").value(hasItem(DEFAULT_CYL_LEFT)))
            .andExpect(jsonPath("$.[*].axisLeft").value(hasItem(DEFAULT_AXIS_LEFT)))
            .andExpect(jsonPath("$.[*].readingLeft").value(hasItem(DEFAULT_READING_LEFT)))
            .andExpect(jsonPath("$.[*].sphRight").value(hasItem(DEFAULT_SPH_RIGHT)))
            .andExpect(jsonPath("$.[*].cylRight").value(hasItem(DEFAULT_CYL_RIGHT)))
            .andExpect(jsonPath("$.[*].axisRight").value(hasItem(DEFAULT_AXIS_RIGHT)))
            .andExpect(jsonPath("$.[*].readingRight").value(hasItem(DEFAULT_READING_RIGHT)))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].deleveryDate").value(hasItem(DEFAULT_DELEVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO)))
            .andExpect(jsonPath("$.[*].frame").value(hasItem(DEFAULT_FRAME)))
            .andExpect(jsonPath("$.[*].modelNo").value(hasItem(DEFAULT_MODEL_NO)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)))
            .andExpect(jsonPath("$.[*].colour").value(hasItem(DEFAULT_COLOUR)))
            .andExpect(jsonPath("$.[*].lType").value(hasItem(DEFAULT_L_TYPE)))
            .andExpect(jsonPath("$.[*].lSize").value(hasItem(DEFAULT_L_SIZE)))
            .andExpect(jsonPath("$.[*].pd").value(hasItem(DEFAULT_PD)))
            .andExpect(jsonPath("$.[*].inset").value(hasItem(DEFAULT_INSET)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));
    }
    
    @Test
    public void getDescription() throws Exception {
        // Initialize the database
        descriptionRepository.save(description);

        // Get the description
        restDescriptionMockMvc.perform(get("/api/descriptions/{id}", description.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(description.getId()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID))
            .andExpect(jsonPath("$.invoiceId").value(DEFAULT_INVOICE_ID))
            .andExpect(jsonPath("$.sphLeft").value(DEFAULT_SPH_LEFT))
            .andExpect(jsonPath("$.cylLeft").value(DEFAULT_CYL_LEFT))
            .andExpect(jsonPath("$.axisLeft").value(DEFAULT_AXIS_LEFT))
            .andExpect(jsonPath("$.readingLeft").value(DEFAULT_READING_LEFT))
            .andExpect(jsonPath("$.sphRight").value(DEFAULT_SPH_RIGHT))
            .andExpect(jsonPath("$.cylRight").value(DEFAULT_CYL_RIGHT))
            .andExpect(jsonPath("$.axisRight").value(DEFAULT_AXIS_RIGHT))
            .andExpect(jsonPath("$.readingRight").value(DEFAULT_READING_RIGHT))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.deleveryDate").value(DEFAULT_DELEVERY_DATE.toString()))
            .andExpect(jsonPath("$.orderNo").value(DEFAULT_ORDER_NO))
            .andExpect(jsonPath("$.frame").value(DEFAULT_FRAME))
            .andExpect(jsonPath("$.modelNo").value(DEFAULT_MODEL_NO))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE))
            .andExpect(jsonPath("$.colour").value(DEFAULT_COLOUR))
            .andExpect(jsonPath("$.lType").value(DEFAULT_L_TYPE))
            .andExpect(jsonPath("$.lSize").value(DEFAULT_L_SIZE))
            .andExpect(jsonPath("$.pd").value(DEFAULT_PD))
            .andExpect(jsonPath("$.inset").value(DEFAULT_INSET))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS));
    }

    @Test
    public void getNonExistingDescription() throws Exception {
        // Get the description
        restDescriptionMockMvc.perform(get("/api/descriptions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateDescription() throws Exception {
        // Initialize the database
        descriptionRepository.save(description);

        int databaseSizeBeforeUpdate = descriptionRepository.findAll().size();

        // Update the description
        Description updatedDescription = descriptionRepository.findById(description.getId()).get();
        updatedDescription
            .customerId(UPDATED_CUSTOMER_ID)
            .invoiceId(UPDATED_INVOICE_ID)
            .sphLeft(UPDATED_SPH_LEFT)
            .cylLeft(UPDATED_CYL_LEFT)
            .axisLeft(UPDATED_AXIS_LEFT)
            .readingLeft(UPDATED_READING_LEFT)
            .sphRight(UPDATED_SPH_RIGHT)
            .cylRight(UPDATED_CYL_RIGHT)
            .axisRight(UPDATED_AXIS_RIGHT)
            .readingRight(UPDATED_READING_RIGHT)
            .orderDate(UPDATED_ORDER_DATE)
            .deleveryDate(UPDATED_DELEVERY_DATE)
            .orderNo(UPDATED_ORDER_NO)
            .frame(UPDATED_FRAME)
            .modelNo(UPDATED_MODEL_NO)
            .size(UPDATED_SIZE)
            .colour(UPDATED_COLOUR)
            .lType(UPDATED_L_TYPE)
            .lSize(UPDATED_L_SIZE)
            .pd(UPDATED_PD)
            .inset(UPDATED_INSET)
            .name(UPDATED_NAME)
            .remarks(UPDATED_REMARKS);

        restDescriptionMockMvc.perform(put("/api/descriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDescription)))
            .andExpect(status().isOk());

        // Validate the Description in the database
        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeUpdate);
        Description testDescription = descriptionList.get(descriptionList.size() - 1);
        assertThat(testDescription.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testDescription.getInvoiceId()).isEqualTo(UPDATED_INVOICE_ID);
        assertThat(testDescription.getSphLeft()).isEqualTo(UPDATED_SPH_LEFT);
        assertThat(testDescription.getCylLeft()).isEqualTo(UPDATED_CYL_LEFT);
        assertThat(testDescription.getAxisLeft()).isEqualTo(UPDATED_AXIS_LEFT);
        assertThat(testDescription.getReadingLeft()).isEqualTo(UPDATED_READING_LEFT);
        assertThat(testDescription.getSphRight()).isEqualTo(UPDATED_SPH_RIGHT);
        assertThat(testDescription.getCylRight()).isEqualTo(UPDATED_CYL_RIGHT);
        assertThat(testDescription.getAxisRight()).isEqualTo(UPDATED_AXIS_RIGHT);
        assertThat(testDescription.getReadingRight()).isEqualTo(UPDATED_READING_RIGHT);
        assertThat(testDescription.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testDescription.getDeleveryDate()).isEqualTo(UPDATED_DELEVERY_DATE);
        assertThat(testDescription.getOrderNo()).isEqualTo(UPDATED_ORDER_NO);
        assertThat(testDescription.getFrame()).isEqualTo(UPDATED_FRAME);
        assertThat(testDescription.getModelNo()).isEqualTo(UPDATED_MODEL_NO);
        assertThat(testDescription.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testDescription.getColour()).isEqualTo(UPDATED_COLOUR);
        assertThat(testDescription.getlType()).isEqualTo(UPDATED_L_TYPE);
        assertThat(testDescription.getlSize()).isEqualTo(UPDATED_L_SIZE);
        assertThat(testDescription.getPd()).isEqualTo(UPDATED_PD);
        assertThat(testDescription.getInset()).isEqualTo(UPDATED_INSET);
        assertThat(testDescription.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDescription.getRemarks()).isEqualTo(UPDATED_REMARKS);

        // Validate the Description in Elasticsearch
        verify(mockDescriptionSearchRepository, times(1)).save(testDescription);
    }

    @Test
    public void updateNonExistingDescription() throws Exception {
        int databaseSizeBeforeUpdate = descriptionRepository.findAll().size();

        // Create the Description

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDescriptionMockMvc.perform(put("/api/descriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(description)))
            .andExpect(status().isBadRequest());

        // Validate the Description in the database
        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Description in Elasticsearch
        verify(mockDescriptionSearchRepository, times(0)).save(description);
    }

    @Test
    public void deleteDescription() throws Exception {
        // Initialize the database
        descriptionRepository.save(description);

        int databaseSizeBeforeDelete = descriptionRepository.findAll().size();

        // Delete the description
        restDescriptionMockMvc.perform(delete("/api/descriptions/{id}", description.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Description> descriptionList = descriptionRepository.findAll();
        assertThat(descriptionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Description in Elasticsearch
        verify(mockDescriptionSearchRepository, times(1)).deleteById(description.getId());
    }

    @Test
    public void searchDescription() throws Exception {
        // Initialize the database
        descriptionRepository.save(description);
        when(mockDescriptionSearchRepository.search(queryStringQuery("id:" + description.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(description), PageRequest.of(0, 1), 1));
        // Search the description
        restDescriptionMockMvc.perform(get("/api/_search/descriptions?query=id:" + description.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(description.getId())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].invoiceId").value(hasItem(DEFAULT_INVOICE_ID)))
            .andExpect(jsonPath("$.[*].sphLeft").value(hasItem(DEFAULT_SPH_LEFT)))
            .andExpect(jsonPath("$.[*].cylLeft").value(hasItem(DEFAULT_CYL_LEFT)))
            .andExpect(jsonPath("$.[*].axisLeft").value(hasItem(DEFAULT_AXIS_LEFT)))
            .andExpect(jsonPath("$.[*].readingLeft").value(hasItem(DEFAULT_READING_LEFT)))
            .andExpect(jsonPath("$.[*].sphRight").value(hasItem(DEFAULT_SPH_RIGHT)))
            .andExpect(jsonPath("$.[*].cylRight").value(hasItem(DEFAULT_CYL_RIGHT)))
            .andExpect(jsonPath("$.[*].axisRight").value(hasItem(DEFAULT_AXIS_RIGHT)))
            .andExpect(jsonPath("$.[*].readingRight").value(hasItem(DEFAULT_READING_RIGHT)))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].deleveryDate").value(hasItem(DEFAULT_DELEVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO)))
            .andExpect(jsonPath("$.[*].frame").value(hasItem(DEFAULT_FRAME)))
            .andExpect(jsonPath("$.[*].modelNo").value(hasItem(DEFAULT_MODEL_NO)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)))
            .andExpect(jsonPath("$.[*].colour").value(hasItem(DEFAULT_COLOUR)))
            .andExpect(jsonPath("$.[*].lType").value(hasItem(DEFAULT_L_TYPE)))
            .andExpect(jsonPath("$.[*].lSize").value(hasItem(DEFAULT_L_SIZE)))
            .andExpect(jsonPath("$.[*].pd").value(hasItem(DEFAULT_PD)))
            .andExpect(jsonPath("$.[*].inset").value(hasItem(DEFAULT_INSET)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));
    }
}
