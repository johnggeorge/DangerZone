package com.appdynamics.boxoffice.service.impl;

import com.appdynamics.boxoffice.service.ShowtimeService;
import com.appdynamics.boxoffice.domain.Showtime;
import com.appdynamics.boxoffice.repository.ShowtimeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Showtime}.
 */
@Service
@Transactional
public class ShowtimeServiceImpl implements ShowtimeService {

    private final Logger log = LoggerFactory.getLogger(ShowtimeServiceImpl.class);

    private final ShowtimeRepository showtimeRepository;

    public ShowtimeServiceImpl(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    /**
     * Save a showtime.
     *
     * @param showtime the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Showtime save(Showtime showtime) {
        log.debug("Request to save Showtime : {}", showtime);
        return showtimeRepository.save(showtime);
    }

    /**
     * Get all the showtimes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Showtime> findAll(Pageable pageable) {
        log.debug("Request to get all Showtimes");
        return showtimeRepository.findAll(pageable);
    }


    /**
     * Get one showtime by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Showtime> findOne(Long id) {
        log.debug("Request to get Showtime : {}", id);
        return showtimeRepository.findById(id);
    }

    /**
     * Delete the showtime by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Showtime : {}", id);
        showtimeRepository.deleteById(id);
    }
}
