package com.appdynamics.boxoffice.web.rest;

import com.appdynamics.boxoffice.domain.Showtime;
import com.appdynamics.boxoffice.service.ShowtimeService;
import com.appdynamics.boxoffice.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.appdynamics.boxoffice.domain.Showtime}.
 */
@RestController
@RequestMapping("/api")
public class ShowtimeResource {

    private final Logger log = LoggerFactory.getLogger(ShowtimeResource.class);

    private static final String ENTITY_NAME = "boXofficeOnlineShowtime";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShowtimeService showtimeService;

    public ShowtimeResource(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    /**
     * {@code POST  /showtimes} : Create a new showtime.
     *
     * @param showtime the showtime to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new showtime, or with status {@code 400 (Bad Request)} if the showtime has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/showtimes")
    public ResponseEntity<Showtime> createShowtime(@RequestBody Showtime showtime) throws URISyntaxException {
        log.debug("REST request to save Showtime : {}", showtime);
        if (showtime.getId() != null) {
            throw new BadRequestAlertException("A new showtime cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Showtime result = showtimeService.save(showtime);
        return ResponseEntity.created(new URI("/api/showtimes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /showtimes} : Updates an existing showtime.
     *
     * @param showtime the showtime to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated showtime,
     * or with status {@code 400 (Bad Request)} if the showtime is not valid,
     * or with status {@code 500 (Internal Server Error)} if the showtime couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/showtimes")
    public ResponseEntity<Showtime> updateShowtime(@RequestBody Showtime showtime) throws URISyntaxException {
        log.debug("REST request to update Showtime : {}", showtime);
        if (showtime.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Showtime result = showtimeService.save(showtime);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, showtime.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /showtimes} : get all the showtimes.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of showtimes in body.
     */
    @GetMapping("/showtimes")
    public ResponseEntity<List<Showtime>> getAllShowtimes(Pageable pageable) {
        log.debug("REST request to get a page of Showtimes");
        Page<Showtime> page = showtimeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /showtimes/:id} : get the "id" showtime.
     *
     * @param id the id of the showtime to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the showtime, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/showtimes/{id}")
    public ResponseEntity<Showtime> getShowtime(@PathVariable Long id) {
        log.debug("REST request to get Showtime : {}", id);
        Optional<Showtime> showtime = showtimeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(showtime);
    }

    /**
     * {@code DELETE  /showtimes/:id} : delete the "id" showtime.
     *
     * @param id the id of the showtime to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/showtimes/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        log.debug("REST request to delete Showtime : {}", id);
        showtimeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
