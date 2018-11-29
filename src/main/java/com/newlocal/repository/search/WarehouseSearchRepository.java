package com.newlocal.repository.search;

import com.newlocal.domain.Warehouse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Warehouse entity.
 */
public interface WarehouseSearchRepository extends ElasticsearchRepository<Warehouse, Long> {
}
