package com.theheavenscode.web.rest;

import com.theheavenscode.RoyOpticalsApp;
import com.theheavenscode.domain.Items;
import com.theheavenscode.repository.ItemsRepository;
import com.theheavenscode.repository.search.ItemsSearchRepository;
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
 * Integration tests for the {@link ItemsResource} REST controller.
 */
@SpringBootTest(classes = RoyOpticalsApp.class)
public class ItemsResourceIT {

    private static final String DEFAULT_INVOICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_QUANTITY = 1D;
    private static final Double UPDATED_QUANTITY = 2D;

    private static final Double DEFAULT_RATE = 1D;
    private static final Double UPDATED_RATE = 2D;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    @Autowired
    private ItemsRepository itemsRepository;

    /**
     * This repository is mocked in the com.theheavenscode.repository.search test package.
     *
     * @see com.theheavenscode.repository.search.ItemsSearchRepositoryMockConfiguration
     */
    @Autowired
    private ItemsSearchRepository mockItemsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restItemsMockMvc;

    private Items items;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItemsResource itemsResource = new ItemsResource(itemsRepository, mockItemsSearchRepository);
        this.restItemsMockMvc = MockMvcBuilders.standaloneSetup(itemsResource)
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
    public static Items createEntity() {
        Items items = new Items()
            .invoiceId(DEFAULT_INVOICE_ID)
            .description(DEFAULT_DESCRIPTION)
            .quantity(DEFAULT_QUANTITY)
            .rate(DEFAULT_RATE)
            .amount(DEFAULT_AMOUNT);
        return items;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Items createUpdatedEntity() {
        Items items = new Items()
            .invoiceId(UPDATED_INVOICE_ID)
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .rate(UPDATED_RATE)
            .amount(UPDATED_AMOUNT);
        return items;
    }

    @BeforeEach
    public void initTest() {
        itemsRepository.deleteAll();
        items = createEntity();
    }

    @Test
    public void createItems() throws Exception {
        int databaseSizeBeforeCreate = itemsRepository.findAll().size();

        // Create the Items
        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isCreated());

        // Validate the Items in the database
        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeCreate + 1);
        Items testItems = itemsList.get(itemsList.size() - 1);
        assertThat(testItems.getInvoiceId()).isEqualTo(DEFAULT_INVOICE_ID);
        assertThat(testItems.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testItems.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testItems.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testItems.getAmount()).isEqualTo(DEFAULT_AMOUNT);

        // Validate the Items in Elasticsearch
        verify(mockItemsSearchRepository, times(1)).save(testItems);
    }

    @Test
    public void createItemsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemsRepository.findAll().size();

        // Create the Items with an existing ID
        items.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        // Validate the Items in the database
        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeCreate);

        // Validate the Items in Elasticsearch
        verify(mockItemsSearchRepository, times(0)).save(items);
    }


    @Test
    public void getAllItems() throws Exception {
        // Initialize the database
        itemsRepository.save(items);

        // Get all the itemsList
        restItemsMockMvc.perform(get("/api/items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(items.getId())))
            .andExpect(jsonPath("$.[*].invoiceId").value(hasItem(DEFAULT_INVOICE_ID)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }
    
    @Test
    public void getItems() throws Exception {
        // Initialize the database
        itemsRepository.save(items);

        // Get the items
        restItemsMockMvc.perform(get("/api/items/{id}", items.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(items.getId()))
            .andExpect(jsonPath("$.invoiceId").value(DEFAULT_INVOICE_ID))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.doubleValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    public void getNonExistingItems() throws Exception {
        // Get the items
        restItemsMockMvc.perform(get("/api/items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateItems() throws Exception {
        // Initialize the database
        itemsRepository.save(items);

        int databaseSizeBeforeUpdate = itemsRepository.findAll().size();

        // Update the items
        Items updatedItems = itemsRepository.findById(items.getId()).get();
        updatedItems
            .invoiceId(UPDATED_INVOICE_ID)
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .rate(UPDATED_RATE)
            .amount(UPDATED_AMOUNT);

        restItemsMockMvc.perform(put("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItems)))
            .andExpect(status().isOk());

        // Validate the Items in the database
        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeUpdate);
        Items testItems = itemsList.get(itemsList.size() - 1);
        assertThat(testItems.getInvoiceId()).isEqualTo(UPDATED_INVOICE_ID);
        assertThat(testItems.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testItems.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testItems.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testItems.getAmount()).isEqualTo(UPDATED_AMOUNT);

        // Validate the Items in Elasticsearch
        verify(mockItemsSearchRepository, times(1)).save(testItems);
    }

    @Test
    public void updateNonExistingItems() throws Exception {
        int databaseSizeBeforeUpdate = itemsRepository.findAll().size();

        // Create the Items

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemsMockMvc.perform(put("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        // Validate the Items in the database
        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Items in Elasticsearch
        verify(mockItemsSearchRepository, times(0)).save(items);
    }

    @Test
    public void deleteItems() throws Exception {
        // Initialize the database
        itemsRepository.save(items);

        int databaseSizeBeforeDelete = itemsRepository.findAll().size();

        // Delete the items
        restItemsMockMvc.perform(delete("/api/items/{id}", items.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Items in Elasticsearch
        verify(mockItemsSearchRepository, times(1)).deleteById(items.getId());
    }

    @Test
    public void searchItems() throws Exception {
        // Initialize the database
        itemsRepository.save(items);
        when(mockItemsSearchRepository.search(queryStringQuery("id:" + items.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(items), PageRequest.of(0, 1), 1));
        // Search the items
        restItemsMockMvc.perform(get("/api/_search/items?query=id:" + items.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(items.getId())))
            .andExpect(jsonPath("$.[*].invoiceId").value(hasItem(DEFAULT_INVOICE_ID)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }
}
