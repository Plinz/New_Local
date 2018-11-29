package com.newlocal.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ImageSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ImageSearchRepositoryMockConfiguration {

    @MockBean
    private ImageSearchRepository mockImageSearchRepository;

}
