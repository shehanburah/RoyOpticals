package com.theheavenscode.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link DailyLogSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DailyLogSearchRepositoryMockConfiguration {

    @MockBean
    private DailyLogSearchRepository mockDailyLogSearchRepository;

}
