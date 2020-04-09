package com.theheavenscode.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PettyCashStockSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PettyCashStockSearchRepositoryMockConfiguration {

    @MockBean
    private PettyCashStockSearchRepository mockPettyCashStockSearchRepository;

}
