package com.newlocal.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of PurchaseDoneSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PurchaseDoneSearchRepositoryMockConfiguration {

    @MockBean
    private PurchaseDoneSearchRepository mockPurchaseDoneSearchRepository;

}
