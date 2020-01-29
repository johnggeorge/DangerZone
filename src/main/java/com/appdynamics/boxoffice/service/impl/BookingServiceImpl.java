package com.appdynamics.boxoffice.service.impl;

import com.appdynamics.boxoffice.service.BookingService;
import com.appdynamics.boxoffice.domain.Booking;
import com.appdynamics.boxoffice.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Booking}.
 */
@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Save a booking.
     *
     * @param booking the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Booking save(Booking booking) {
        log.debug("Request to save Booking : {}", booking);
        return bookingRepository.save(booking);
    }

    /**
     * Get all the bookings.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Booking> findAll() {
        log.debug("Request to get all Bookings");
        return bookingRepository.findAll();
    }


    /**
     * Get one booking by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Booking> findOne(Long id) {
        log.debug("Request to get Booking : {}", id);
        return bookingRepository.findById(id);
    }

    /**
     * Delete the booking by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Booking : {}", id);
        bookingRepository.deleteById(id);
    }
}
