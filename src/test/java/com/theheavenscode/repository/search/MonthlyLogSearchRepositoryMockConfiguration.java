package com.theheavenscode.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link MonthlyLogSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class MonthlyLogSearchRepositoryMockConfiguration {

    @MockBean
    private MonthlyLogSearchRepository mockMonthlyLogSearchRepository;

}
