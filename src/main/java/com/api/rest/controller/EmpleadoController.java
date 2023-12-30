package com.api.rest.controller;

import com.api.rest.models.EmpleadoEntity;
import com.api.rest.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleado")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;


    @PostMapping
    public ResponseEntity<EmpleadoEntity> save(@RequestBody EmpleadoEntity empleado){
        return new ResponseEntity<>(empleadoService.save(empleado), HttpStatus.CREATED);
    }

    @GetMapping
    public List<EmpleadoEntity> listAll(){
        return empleadoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoEntity> getById(@PathVariable("id") Long id){
        return empleadoService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoEntity> update(@PathVariable("id") Long id,
                                                @RequestBody EmpleadoEntity empleado
    ){
        return empleadoService.getById(id)
                .map(empleadoGuardado -> {
                    empleadoGuardado.setNombre(empleado.getNombre());
                    empleadoGuardado.setApellido(empleado.getApellido());
                    empleadoGuardado.setEmail(empleado.getEmail());

                    EmpleadoEntity empleadoActualizado = empleadoService.update(empleadoGuardado);
                    return new ResponseEntity<>(empleadoActualizado, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        empleadoService.delete(id);
        return new ResponseEntity<String>("Empleado Eliminado exitosamente", HttpStatus.OK);
    }
}
