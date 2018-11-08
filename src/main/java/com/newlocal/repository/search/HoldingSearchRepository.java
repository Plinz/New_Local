package com.newlocal.repository.search;

import com.newlocal.domain.Holding;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Holding entity.
 */
public interface HoldingSearchRepository extends ElasticsearchRepository<Holding, Long> {
}
