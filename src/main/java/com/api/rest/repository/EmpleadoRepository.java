package com.api.rest.repository;

import com.api.rest.models.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, Long> {

    Optional<EmpleadoEntity> findByEmail(String email);

}
