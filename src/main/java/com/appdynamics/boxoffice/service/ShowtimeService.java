package com.appdynamics.boxoffice.service;

import com.appdynamics.boxoffice.domain.Showtime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Showtime}.
 */
public interface ShowtimeService {

    /**
     * Save a showtime.
     *
     * @param showtime the entity to save.
     * @return the persisted entity.
     */
    Showtime save(Showtime showtime);

    /**
     * Get all the showtimes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Showtime> findAll(Pageable pageable);


    /**
     * Get the "id" showtime.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Showtime> findOne(Long id);

    /**
     * Delete the "id" showtime.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
