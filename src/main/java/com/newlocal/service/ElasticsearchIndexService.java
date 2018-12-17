package com.newlocal.service;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import javax.persistence.ManyToMany;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newlocal.domain.Cart;
import com.newlocal.domain.Category;
import com.newlocal.domain.Grade;
import com.newlocal.domain.Holding;
import com.newlocal.domain.Image;
import com.newlocal.domain.Location;
import com.newlocal.domain.ProductType;
import com.newlocal.domain.Purchase;
import com.newlocal.domain.Stock;
import com.newlocal.domain.User;
import com.newlocal.domain.Warehouse;
import com.newlocal.repository.CartRepository;
import com.newlocal.repository.CategoryRepository;
import com.newlocal.repository.GradeRepository;
import com.newlocal.repository.HoldingRepository;
import com.newlocal.repository.ImageRepository;
import com.newlocal.repository.LocationRepository;
import com.newlocal.repository.ProductTypeRepository;
import com.newlocal.repository.PurchaseRepository;
import com.newlocal.repository.StockRepository;
import com.newlocal.repository.UserRepository;
import com.newlocal.repository.WarehouseRepository;
import com.newlocal.repository.search.CartSearchRepository;
import com.newlocal.repository.search.CategorySearchRepository;
import com.newlocal.repository.search.GradeSearchRepository;
import com.newlocal.repository.search.HoldingSearchRepository;
import com.newlocal.repository.search.ImageSearchRepository;
import com.newlocal.repository.search.LocationSearchRepository;
import com.newlocal.repository.search.ProductTypeSearchRepository;
import com.newlocal.repository.search.PurchaseSearchRepository;
import com.newlocal.repository.search.StockSearchRepository;
import com.newlocal.repository.search.UserSearchRepository;
import com.newlocal.repository.search.WarehouseSearchRepository;

@Service
@Transactional(readOnly = true)
public class ElasticsearchIndexService {

    private static final Lock reindexLock = new ReentrantLock();

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

    private final CartRepository cartRepository;

    private final CartSearchRepository cartSearchRepository;

    private final CategoryRepository categoryRepository;

    private final CategorySearchRepository categorySearchRepository;

    private final GradeRepository gradeRepository;

    private final GradeSearchRepository gradeSearchRepository;

    private final HoldingRepository holdingRepository;

    private final HoldingSearchRepository holdingSearchRepository;

    private final ImageRepository imageRepository;

    private final ImageSearchRepository imageSearchRepository;

    private final LocationRepository locationRepository;

    private final LocationSearchRepository locationSearchRepository;

    private final ProductTypeRepository productTypeRepository;

    private final ProductTypeSearchRepository productTypeSearchRepository;

    private final PurchaseRepository purchaseRepository;

    private final PurchaseSearchRepository purchaseSearchRepository;

    private final StockRepository stockRepository;

    private final StockSearchRepository stockSearchRepository;

    private final WarehouseRepository warehouseRepository;

    private final WarehouseSearchRepository warehouseSearchRepository;

    private final UserRepository userRepository;

    private final UserSearchRepository userSearchRepository;

    private final ElasticsearchOperations elasticsearchOperations;

    public ElasticsearchIndexService(
        UserRepository userRepository,
        UserSearchRepository userSearchRepository,
        CartRepository cartRepository,
        CartSearchRepository cartSearchRepository,
        CategoryRepository categoryRepository,
        CategorySearchRepository categorySearchRepository,
        GradeRepository gradeRepository,
        GradeSearchRepository gradeSearchRepository,
        HoldingRepository holdingRepository,
        HoldingSearchRepository holdingSearchRepository,
        ImageRepository imageRepository,
        ImageSearchRepository imageSearchRepository,
        LocationRepository locationRepository,
        LocationSearchRepository locationSearchRepository,
        ProductTypeRepository productTypeRepository,
        ProductTypeSearchRepository productTypeSearchRepository,
        PurchaseRepository purchaseRepository,
        PurchaseSearchRepository purchaseSearchRepository,
        StockRepository stockRepository,
        StockSearchRepository stockSearchRepository,
        WarehouseRepository warehouseRepository,
        WarehouseSearchRepository warehouseSearchRepository,
        ElasticsearchOperations elasticsearchOperations) {
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
        this.cartRepository = cartRepository;
        this.cartSearchRepository = cartSearchRepository;
        this.categoryRepository = categoryRepository;
        this.categorySearchRepository = categorySearchRepository;
        this.gradeRepository = gradeRepository;
        this.gradeSearchRepository = gradeSearchRepository;
        this.holdingRepository = holdingRepository;
        this.holdingSearchRepository = holdingSearchRepository;
        this.imageRepository = imageRepository;
        this.imageSearchRepository = imageSearchRepository;
        this.locationRepository = locationRepository;
        this.locationSearchRepository = locationSearchRepository;
        this.productTypeRepository = productTypeRepository;
        this.productTypeSearchRepository = productTypeSearchRepository;
        this.purchaseRepository = purchaseRepository;
        this.purchaseSearchRepository = purchaseSearchRepository;
        this.stockRepository = stockRepository;
        this.stockSearchRepository = stockSearchRepository;
        this.warehouseRepository = warehouseRepository;
        this.warehouseSearchRepository = warehouseSearchRepository;
        this.elasticsearchOperations = elasticsearchOperations;
        
        this.reindexAll();
    }

    @Async
    @Timed
    public void reindexAll() {
        if (reindexLock.tryLock()) {
            try {
                reindexForClass(Cart.class, cartRepository, cartSearchRepository);
                reindexForClass(Category.class, categoryRepository, categorySearchRepository);
                reindexForClass(Grade.class, gradeRepository, gradeSearchRepository);
                reindexForClass(Holding.class, holdingRepository, holdingSearchRepository);
                reindexForClass(Image.class, imageRepository, imageSearchRepository);
                reindexForClass(Location.class, locationRepository, locationSearchRepository);
                reindexForClass(ProductType.class, productTypeRepository, productTypeSearchRepository);
                reindexForClass(Purchase.class, purchaseRepository, purchaseSearchRepository);
                reindexForClass(Stock.class, stockRepository, stockSearchRepository);
                reindexForClass(Warehouse.class, warehouseRepository, warehouseSearchRepository);
                reindexForClass(User.class, userRepository, userSearchRepository);

                log.info("Elasticsearch: Successfully performed reindexing");
            } finally {
                reindexLock.unlock();
            }
        } else {
            log.info("Elasticsearch: concurrent reindexing attempt");
        }
    }

    private <T, ID extends Serializable> void reindexForClass(Class<T> entityClass, JpaRepository<T, ID> jpaRepository,
                                                              ElasticsearchRepository<T, ID> elasticsearchRepository) {
    	elasticsearchOperations.deleteIndex(entityClass);
    	elasticsearchOperations.createIndex(entityClass);
    	elasticsearchOperations.putMapping(entityClass);
        log.info("Start Reindex");
        if (jpaRepository.count() > 0) {
            // if a JHipster entity field is the owner side of a many-to-many relationship, it should be loaded manually
            List<Method> relationshipGetters = Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.getType().equals(Set.class))
                .filter(field -> field.getAnnotation(ManyToMany.class) != null)
                .filter(field -> field.getAnnotation(ManyToMany.class).mappedBy().isEmpty())
                .filter(field -> field.getAnnotation(JsonIgnore.class) == null)
                .map(field -> {
                    try {
                        return new PropertyDescriptor(field.getName(), entityClass).getReadMethod();
                    } catch (IntrospectionException e) {
                        log.error("Error retrieving getter for class {}, field {}. Field will NOT be indexed",
                            entityClass.getSimpleName(), field.getName(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            log.info("Debug 1");
            int size = 75;
            for (int i = 0; i <= jpaRepository.count() / size; i++) {
                Pageable page = PageRequest.of(i, size);
                log.info("Indexing page {} of {}, size {}", i, jpaRepository.count() / size, size);
                Page<T> results = jpaRepository.findAll(page);
                results.map(result -> {
                    relationshipGetters.forEach(method -> {
                        try {
                            ((Set) method.invoke(result)).size();
                        } catch (Exception ex) {
                            log.error(ex.getMessage());
                        }
                    });
                    return result;
                });
                log.info("Start 2 + "+i);
                elasticsearchRepository.saveAll(results);
            }
        }
        log.info("Elasticsearch: Indexed all rows for {}", entityClass.getSimpleName());
    }
}
