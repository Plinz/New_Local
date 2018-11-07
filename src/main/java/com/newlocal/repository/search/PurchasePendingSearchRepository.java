package com.newlocal.repository.search;

import com.newlocal.domain.PurchasePending;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PurchasePending entity.
 */
public interface PurchasePendingSearchRepository extends ElasticsearchRepository<PurchasePending, Long> {
}
