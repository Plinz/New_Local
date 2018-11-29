package com.newlocal.repository.search;

import com.newlocal.domain.Cart;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Cart entity.
 */
public interface CartSearchRepository extends ElasticsearchRepository<Cart, Long> {
}
