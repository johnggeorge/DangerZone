package com.appdynamics.boxoffice.web.rest;

import com.appdynamics.boxoffice.BoXofficeOnlineApp;
import com.appdynamics.boxoffice.domain.Theater;
import com.appdynamics.boxoffice.repository.TheaterRepository;
import com.appdynamics.boxoffice.service.TheaterService;
import com.appdynamics.boxoffice.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.appdynamics.boxoffice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TheaterResource} REST controller.
 */
@SpringBootTest(classes = BoXofficeOnlineApp.class)
public class TheaterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private TheaterRepository theaterRepository;

    @Mock
    private TheaterRepository theaterRepositoryMock;

    @Mock
    private TheaterService theaterServiceMock;

    @Autowired
    private TheaterService theaterService;

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

    private MockMvc restTheaterMockMvc;

    private Theater theater;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TheaterResource theaterResource = new TheaterResource(theaterService);
        this.restTheaterMockMvc = MockMvcBuilders.standaloneSetup(theaterResource)
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
    public static Theater createEntity(EntityManager em) {
        Theater theater = new Theater()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS);
        return theater;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Theater createUpdatedEntity(EntityManager em) {
        Theater theater = new Theater()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS);
        return theater;
    }

    @BeforeEach
    public void initTest() {
        theater = createEntity(em);
    }

    @Test
    @Transactional
    public void createTheater() throws Exception {
        int databaseSizeBeforeCreate = theaterRepository.findAll().size();

        // Create the Theater
        restTheaterMockMvc.perform(post("/api/theaters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(theater)))
            .andExpect(status().isCreated());

        // Validate the Theater in the database
        List<Theater> theaterList = theaterRepository.findAll();
        assertThat(theaterList).hasSize(databaseSizeBeforeCreate + 1);
        Theater testTheater = theaterList.get(theaterList.size() - 1);
        assertThat(testTheater.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTheater.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    public void createTheaterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = theaterRepository.findAll().size();

        // Create the Theater with an existing ID
        theater.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTheaterMockMvc.perform(post("/api/theaters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(theater)))
            .andExpect(status().isBadRequest());

        // Validate the Theater in the database
        List<Theater> theaterList = theaterRepository.findAll();
        assertThat(theaterList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTheaters() throws Exception {
        // Initialize the database
        theaterRepository.saveAndFlush(theater);

        // Get all the theaterList
        restTheaterMockMvc.perform(get("/api/theaters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(theater.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllTheatersWithEagerRelationshipsIsEnabled() throws Exception {
        TheaterResource theaterResource = new TheaterResource(theaterServiceMock);
        when(theaterServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restTheaterMockMvc = MockMvcBuilders.standaloneSetup(theaterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restTheaterMockMvc.perform(get("/api/theaters?eagerload=true"))
        .andExpect(status().isOk());

        verify(theaterServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllTheatersWithEagerRelationshipsIsNotEnabled() throws Exception {
        TheaterResource theaterResource = new TheaterResource(theaterServiceMock);
            when(theaterServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restTheaterMockMvc = MockMvcBuilders.standaloneSetup(theaterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restTheaterMockMvc.perform(get("/api/theaters?eagerload=true"))
        .andExpect(status().isOk());

            verify(theaterServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getTheater() throws Exception {
        // Initialize the database
        theaterRepository.saveAndFlush(theater);

        // Get the theater
        restTheaterMockMvc.perform(get("/api/theaters/{id}", theater.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(theater.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    public void getNonExistingTheater() throws Exception {
        // Get the theater
        restTheaterMockMvc.perform(get("/api/theaters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTheater() throws Exception {
        // Initialize the database
        theaterService.save(theater);

        int databaseSizeBeforeUpdate = theaterRepository.findAll().size();

        // Update the theater
        Theater updatedTheater = theaterRepository.findById(theater.getId()).get();
        // Disconnect from session so that the updates on updatedTheater are not directly saved in db
        em.detach(updatedTheater);
        updatedTheater
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS);

        restTheaterMockMvc.perform(put("/api/theaters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTheater)))
            .andExpect(status().isOk());

        // Validate the Theater in the database
        List<Theater> theaterList = theaterRepository.findAll();
        assertThat(theaterList).hasSize(databaseSizeBeforeUpdate);
        Theater testTheater = theaterList.get(theaterList.size() - 1);
        assertThat(testTheater.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTheater.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void updateNonExistingTheater() throws Exception {
        int databaseSizeBeforeUpdate = theaterRepository.findAll().size();

        // Create the Theater

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTheaterMockMvc.perform(put("/api/theaters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(theater)))
            .andExpect(status().isBadRequest());

        // Validate the Theater in the database
        List<Theater> theaterList = theaterRepository.findAll();
        assertThat(theaterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTheater() throws Exception {
        // Initialize the database
        theaterService.save(theater);

        int databaseSizeBeforeDelete = theaterRepository.findAll().size();

        // Delete the theater
        restTheaterMockMvc.perform(delete("/api/theaters/{id}", theater.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Theater> theaterList = theaterRepository.findAll();
        assertThat(theaterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
