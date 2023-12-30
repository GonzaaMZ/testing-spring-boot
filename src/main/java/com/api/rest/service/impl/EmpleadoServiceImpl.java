package com.api.rest.service.impl;

import com.api.rest.exception.ResourceNotFoundException;
import com.api.rest.models.EmpleadoEntity;
import com.api.rest.repository.EmpleadoRepository;
import com.api.rest.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    public EmpleadoEntity save(EmpleadoEntity empleado) {
        Optional<EmpleadoEntity> existeEmpleado = empleadoRepository.findByEmail(empleado.getEmail());
        if(existeEmpleado.isPresent()) {
            throw new ResourceNotFoundException("Email ya existente: " + empleado.getEmail());
        }
        return empleadoRepository.save(empleado);
    }

    @Override
    public List<EmpleadoEntity> findAll() {
        return empleadoRepository.findAll();
    }

    @Override
    public Optional<EmpleadoEntity> getById(Long id) {
        return empleadoRepository.findById(id);
    }

    @Override
    public EmpleadoEntity update(EmpleadoEntity empleadoEntity) {
        return empleadoRepository.save(empleadoEntity);
    }

    @Override
    public void delete(Long id) {
        empleadoRepository.deleteById(id);
    }
}
