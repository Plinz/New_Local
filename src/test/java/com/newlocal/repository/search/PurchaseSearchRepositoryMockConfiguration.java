package com.newlocal.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of PurchaseSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PurchaseSearchRepositoryMockConfiguration {

    @MockBean
    private PurchaseSearchRepository mockPurchaseSearchRepository;

}
