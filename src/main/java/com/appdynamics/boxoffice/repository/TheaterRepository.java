package com.appdynamics.boxoffice.repository;

import com.appdynamics.boxoffice.domain.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Theater entity.
 */
@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {

    @Query(value = "select distinct theater from Theater theater left join fetch theater.movies",
        countQuery = "select count(distinct theater) from Theater theater")
    Page<Theater> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct theater from Theater theater left join fetch theater.movies")
    List<Theater> findAllWithEagerRelationships();

    @Query("select theater from Theater theater left join fetch theater.movies where theater.id =:id")
    Optional<Theater> findOneWithEagerRelationships(@Param("id") Long id);

}
