package com.appdynamics.boxoffice.web.rest;

import com.appdynamics.boxoffice.domain.Theater;
import com.appdynamics.boxoffice.service.TheaterService;
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
 * REST controller for managing {@link com.appdynamics.boxoffice.domain.Theater}.
 */
@RestController
@RequestMapping("/api")
public class TheaterResource {

    private final Logger log = LoggerFactory.getLogger(TheaterResource.class);

    private static final String ENTITY_NAME = "boXofficeOnlineTheater";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TheaterService theaterService;

    public TheaterResource(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    /**
     * {@code POST  /theaters} : Create a new theater.
     *
     * @param theater the theater to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new theater, or with status {@code 400 (Bad Request)} if the theater has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/theaters")
    public ResponseEntity<Theater> createTheater(@RequestBody Theater theater) throws URISyntaxException {
        log.debug("REST request to save Theater : {}", theater);
        if (theater.getId() != null) {
            throw new BadRequestAlertException("A new theater cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Theater result = theaterService.save(theater);
        return ResponseEntity.created(new URI("/api/theaters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /theaters} : Updates an existing theater.
     *
     * @param theater the theater to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated theater,
     * or with status {@code 400 (Bad Request)} if the theater is not valid,
     * or with status {@code 500 (Internal Server Error)} if the theater couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/theaters")
    public ResponseEntity<Theater> updateTheater(@RequestBody Theater theater) throws URISyntaxException {
        log.debug("REST request to update Theater : {}", theater);
        if (theater.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Theater result = theaterService.save(theater);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, theater.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /theaters} : get all the theaters.
     *

     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of theaters in body.
     */
    @GetMapping("/theaters")
    public ResponseEntity<List<Theater>> getAllTheaters(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Theaters");
        Page<Theater> page;
        if (eagerload) {
            page = theaterService.findAllWithEagerRelationships(pageable);
        } else {
            page = theaterService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /theaters/:id} : get the "id" theater.
     *
     * @param id the id of the theater to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the theater, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/theaters/{id}")
    public ResponseEntity<Theater> getTheater(@PathVariable Long id) {
        log.debug("REST request to get Theater : {}", id);
        Optional<Theater> theater = theaterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(theater);
    }

    /**
     * {@code DELETE  /theaters/:id} : delete the "id" theater.
     *
     * @param id the id of the theater to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/theaters/{id}")
    public ResponseEntity<Void> deleteTheater(@PathVariable Long id) {
        log.debug("REST request to delete Theater : {}", id);
        theaterService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
