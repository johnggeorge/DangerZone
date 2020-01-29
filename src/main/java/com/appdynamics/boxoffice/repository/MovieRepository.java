package com.appdynamics.boxoffice.repository;

import com.appdynamics.boxoffice.domain.Movie;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Movie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

}
