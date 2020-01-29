package com.appdynamics.boxoffice.service.impl;

import com.appdynamics.boxoffice.service.TheaterService;
import com.appdynamics.boxoffice.domain.Theater;
import com.appdynamics.boxoffice.repository.TheaterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Theater}.
 */
@Service
@Transactional
public class TheaterServiceImpl implements TheaterService {

    private final Logger log = LoggerFactory.getLogger(TheaterServiceImpl.class);

    private final TheaterRepository theaterRepository;

    public TheaterServiceImpl(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    /**
     * Save a theater.
     *
     * @param theater the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Theater save(Theater theater) {
        log.debug("Request to save Theater : {}", theater);
        return theaterRepository.save(theater);
    }

    /**
     * Get all the theaters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Theater> findAll(Pageable pageable) {
        log.debug("Request to get all Theaters");
        return theaterRepository.findAll(pageable);
    }

    /**
     * Get all the theaters with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Theater> findAllWithEagerRelationships(Pageable pageable) {
        return theaterRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one theater by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Theater> findOne(Long id) {
        log.debug("Request to get Theater : {}", id);
        return theaterRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the theater by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Theater : {}", id);
        theaterRepository.deleteById(id);
    }
}
