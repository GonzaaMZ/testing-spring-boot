package com.api.rest.service;

import com.api.rest.models.EmpleadoEntity;

import java.util.List;
import java.util.Optional;

public interface EmpleadoService {

    EmpleadoEntity save(EmpleadoEntity empleado);

    List<EmpleadoEntity> findAll();

    Optional<EmpleadoEntity> getById(Long id);

    EmpleadoEntity update(EmpleadoEntity empleadoEntity);

    void delete(Long id);

}
