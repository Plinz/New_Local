package com.newlocal.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newlocal.domain.Location;
import com.newlocal.repository.LocationRepository;
import com.newlocal.repository.search.LocationSearchRepository;
import com.newlocal.service.dto.LocationDTO;
import com.newlocal.service.mapper.LocationMapper;

/**
 * Service Implementation for managing Location.
 */
@Service
@Transactional
public class LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationService.class);

    private LocationRepository locationRepository;

    private LocationMapper locationMapper;

    private LocationSearchRepository locationSearchRepository;
    
    private LocalizationUtils localizationUtils;

    public LocationService(LocationRepository locationRepository, LocationMapper locationMapper, LocationSearchRepository locationSearchRepository, LocalizationUtils localizationUtils) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
        this.locationSearchRepository = locationSearchRepository;
        this.localizationUtils = localizationUtils;
    }

    /**
     * Save a location.
     *
     * @param locationDTO the entity to save
     * @return the persisted entity
     */
    public LocationDTO save(LocationDTO locationDTO) {
        log.debug("Request to save Location : {}", locationDTO);
        
        if (locationDTO.getLat() != null && locationDTO.getLon() != null){
        	locationDTO = localizationUtils.fillEntityFromLonLat(locationDTO);
        } else if ((locationDTO.getZip() != null && !locationDTO.getZip().isEmpty()) || (locationDTO.getCity() != null && !locationDTO.getCity().isEmpty())){
        	locationDTO = localizationUtils.fillEntityFromZipOrCity(locationDTO);
        }
        if (locationDTO.getZip() != null && !locationDTO.getZip().isEmpty()){
	        Location location = locationMapper.toEntity(locationDTO);
	        location = locationRepository.save(location);
	        LocationDTO result = locationMapper.toDto(location);
	        locationSearchRepository.save(location);
	        return result;
        }
        return null;
    }

    /**
     * Get all the locations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LocationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Locations");
        return locationRepository.findAll(pageable)
            .map(locationMapper::toDto);
    }


    /**
     * Get one location by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<LocationDTO> findOne(Long id) {
        log.debug("Request to get Location : {}", id);
        return locationRepository.findById(id)
            .map(locationMapper::toDto);
    }

    /**
     * Delete the location by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Location : {}", id);
        locationRepository.deleteById(id);
        locationSearchRepository.deleteById(id);
    }

    /**
     * Search for the location corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LocationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Locations for query {}", query);
        return locationSearchRepository.search(queryStringQuery(query), pageable)
            .map(locationMapper::toDto);
    }

    /**
     * Get the location of current user
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public LocationDTO findByCurrentUser() {
        log.debug("Request to get Location of current user");
        List<LocationDTO> res = locationRepository.findByUserIsCurrentUser()
        		.stream().map(LocationDTO::new).collect(Collectors.toList());
        return res.isEmpty()?null:res.get(0);
    }
}
