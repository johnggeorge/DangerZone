package com.appdynamics.boxoffice.service;

import com.appdynamics.boxoffice.domain.Theater;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Theater}.
 */
public interface TheaterService {

    /**
     * Save a theater.
     *
     * @param theater the entity to save.
     * @return the persisted entity.
     */
    Theater save(Theater theater);

    /**
     * Get all the theaters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Theater> findAll(Pageable pageable);

    /**
     * Get all the theaters with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<Theater> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" theater.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Theater> findOne(Long id);

    /**
     * Delete the "id" theater.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
