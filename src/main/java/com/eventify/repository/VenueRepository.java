package com.eventify.repository;

import com.eventify.model.VenueModel;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface VenueRepository extends JpaRepository<VenueModel, Long> {
    Page<VenueModel> findByNombreContaining(String nombre, Pageable pageable);
    boolean existsByNombre(String nombre);
    boolean existsByNombreAndIdNot(String nombre, Long id);

}
