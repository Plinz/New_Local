package com.newlocal.repository.search;

import com.newlocal.domain.PurchaseDone;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PurchaseDone entity.
 */
public interface PurchaseDoneSearchRepository extends ElasticsearchRepository<PurchaseDone, Long> {
}
