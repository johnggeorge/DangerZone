package com.appdynamics.boxoffice.web.rest;

import com.appdynamics.boxoffice.BoXofficeOnlineApp;
import com.appdynamics.boxoffice.domain.Showtime;
import com.appdynamics.boxoffice.repository.ShowtimeRepository;
import com.appdynamics.boxoffice.service.ShowtimeService;
import com.appdynamics.boxoffice.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.appdynamics.boxoffice.web.rest.TestUtil.sameInstant;
import static com.appdynamics.boxoffice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ShowtimeResource} REST controller.
 */
@SpringBootTest(classes = BoXofficeOnlineApp.class)
public class ShowtimeResourceIT {

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private ShowtimeService showtimeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restShowtimeMockMvc;

    private Showtime showtime;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShowtimeResource showtimeResource = new ShowtimeResource(showtimeService);
        this.restShowtimeMockMvc = MockMvcBuilders.standaloneSetup(showtimeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Showtime createEntity(EntityManager em) {
        Showtime showtime = new Showtime()
            .time(DEFAULT_TIME);
        return showtime;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Showtime createUpdatedEntity(EntityManager em) {
        Showtime showtime = new Showtime()
            .time(UPDATED_TIME);
        return showtime;
    }

    @BeforeEach
    public void initTest() {
        showtime = createEntity(em);
    }

    @Test
    @Transactional
    public void createShowtime() throws Exception {
        int databaseSizeBeforeCreate = showtimeRepository.findAll().size();

        // Create the Showtime
        restShowtimeMockMvc.perform(post("/api/showtimes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(showtime)))
            .andExpect(status().isCreated());

        // Validate the Showtime in the database
        List<Showtime> showtimeList = showtimeRepository.findAll();
        assertThat(showtimeList).hasSize(databaseSizeBeforeCreate + 1);
        Showtime testShowtime = showtimeList.get(showtimeList.size() - 1);
        assertThat(testShowtime.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    @Transactional
    public void createShowtimeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = showtimeRepository.findAll().size();

        // Create the Showtime with an existing ID
        showtime.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShowtimeMockMvc.perform(post("/api/showtimes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(showtime)))
            .andExpect(status().isBadRequest());

        // Validate the Showtime in the database
        List<Showtime> showtimeList = showtimeRepository.findAll();
        assertThat(showtimeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllShowtimes() throws Exception {
        // Initialize the database
        showtimeRepository.saveAndFlush(showtime);

        // Get all the showtimeList
        restShowtimeMockMvc.perform(get("/api/showtimes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(showtime.getId().intValue())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))));
    }
    
    @Test
    @Transactional
    public void getShowtime() throws Exception {
        // Initialize the database
        showtimeRepository.saveAndFlush(showtime);

        // Get the showtime
        restShowtimeMockMvc.perform(get("/api/showtimes/{id}", showtime.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(showtime.getId().intValue()))
            .andExpect(jsonPath("$.time").value(sameInstant(DEFAULT_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingShowtime() throws Exception {
        // Get the showtime
        restShowtimeMockMvc.perform(get("/api/showtimes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShowtime() throws Exception {
        // Initialize the database
        showtimeService.save(showtime);

        int databaseSizeBeforeUpdate = showtimeRepository.findAll().size();

        // Update the showtime
        Showtime updatedShowtime = showtimeRepository.findById(showtime.getId()).get();
        // Disconnect from session so that the updates on updatedShowtime are not directly saved in db
        em.detach(updatedShowtime);
        updatedShowtime
            .time(UPDATED_TIME);

        restShowtimeMockMvc.perform(put("/api/showtimes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShowtime)))
            .andExpect(status().isOk());

        // Validate the Showtime in the database
        List<Showtime> showtimeList = showtimeRepository.findAll();
        assertThat(showtimeList).hasSize(databaseSizeBeforeUpdate);
        Showtime testShowtime = showtimeList.get(showtimeList.size() - 1);
        assertThat(testShowtime.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingShowtime() throws Exception {
        int databaseSizeBeforeUpdate = showtimeRepository.findAll().size();

        // Create the Showtime

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShowtimeMockMvc.perform(put("/api/showtimes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(showtime)))
            .andExpect(status().isBadRequest());

        // Validate the Showtime in the database
        List<Showtime> showtimeList = showtimeRepository.findAll();
        assertThat(showtimeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShowtime() throws Exception {
        // Initialize the database
        showtimeService.save(showtime);

        int databaseSizeBeforeDelete = showtimeRepository.findAll().size();

        // Delete the showtime
        restShowtimeMockMvc.perform(delete("/api/showtimes/{id}", showtime.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Showtime> showtimeList = showtimeRepository.findAll();
        assertThat(showtimeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
