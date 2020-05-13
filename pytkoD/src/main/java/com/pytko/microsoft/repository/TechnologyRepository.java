package com.pytko.microsoft.repository;

import com.pytko.microsoft.domain.Technology;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Technology entity.
 */
@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Long>, JpaSpecificationExecutor<Technology> {

    @Query(value = "select distinct technology from Technology technology left join fetch technology.employyes left join fetch technology.resources",
        countQuery = "select count(distinct technology) from Technology technology")
    Page<Technology> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct technology from Technology technology left join fetch technology.employyes left join fetch technology.resources")
    List<Technology> findAllWithEagerRelationships();

    @Query("select technology from Technology technology left join fetch technology.employyes left join fetch technology.resources where technology.id =:id")
    Optional<Technology> findOneWithEagerRelationships(@Param("id") Long id);
}
