package com.example.mushrooming.repository;

import com.example.mushrooming.model.Find;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface FindRepository extends JpaRepository<Find, Long> {
    Optional<Find> findFindById(Long id);
    List<Find> findFindsByLatBetweenAndLonBetween(Double lat, Double lat2, Double lon, Double lon2);

    void deleteFindsByCreatedDateBefore(Instant date);
}
