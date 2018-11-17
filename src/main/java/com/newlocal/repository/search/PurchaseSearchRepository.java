package com.newlocal.repository.search;

import com.newlocal.domain.Purchase;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Purchase entity.
 */
public interface PurchaseSearchRepository extends ElasticsearchRepository<Purchase, Long> {
}
