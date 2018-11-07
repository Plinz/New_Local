package com.newlocal.repository.search;

import com.newlocal.domain.Grade;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Grade entity.
 */
public interface GradeSearchRepository extends ElasticsearchRepository<Grade, Long> {
}
