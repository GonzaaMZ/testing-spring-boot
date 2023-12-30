package com.api.rest.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.api.rest.models.EmpleadoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmpleadoRepositoryTests {


    @Autowired
    private EmpleadoRepository empleadoRepository;

    private EmpleadoEntity empleado;

    @BeforeEach
    void setup(){
        empleado = EmpleadoEntity.builder()
                .nombre("Gonzalo")
                .apellido("Amaya")
                .email("gonzaloamaya@gmail.com")
                .build();
    }

    @DisplayName("Test guardar empleado")
    @Test
    void testGuardarEmpleado() {

        //Give
        EmpleadoEntity empleado1 = EmpleadoEntity.builder()
                .nombre("Gonzalo")
                .apellido("Amaya")
                .email("gamaya@gmail.com")
                .build();

        //When
        EmpleadoEntity empleadoDB = empleadoRepository.save(empleado1);

        //Then
        assertThat(empleadoDB).isNotNull();
        assertThat(empleadoDB.getId()).isGreaterThan(0);
    }

    @DisplayName("Test Listar Empleados")
    @Test
    void listarEmpleados(){
        EmpleadoEntity empleado1 = EmpleadoEntity.builder()
                .nombre("Pepe")
                .apellido("Argento")
                .email("pepearg@gmail.com")
                .build();

        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado);

        List<EmpleadoEntity> empleadoList = empleadoRepository.findAll();

        assertThat(empleadoList).isNotNull();
        assertThat(empleadoList).size().isEqualTo(2);
    }

    @DisplayName("Test Obtener empleado por id")
    @Test
    void obtenerEmpleadoById(){
        empleadoRepository.save(empleado);

        EmpleadoEntity empleadoEntity = empleadoRepository.findById(empleado.getId()).get();

        assertThat(empleadoEntity).isNotNull();
        assertThat(empleadoEntity.getEmail()).isEqualTo("gonzaloamaya@gmail.com");
    }

    @DisplayName("Test actualizar empleado")
    @Test
    void actualizarEmpleado(){
        empleadoRepository.save(empleado);

        EmpleadoEntity empleadoGuardado = empleadoRepository.findById(empleado.getId()).get();

        empleadoGuardado.setEmail("gamaya99@gmail.com");
        empleadoGuardado.setNombre("Gonza");

        EmpleadoEntity empleadoActualizado = empleadoRepository.save(empleadoGuardado);

        assertThat(empleadoActualizado.getEmail()).isEqualTo("gamaya99@gmail.com");
        assertThat(empleadoActualizado.getNombre()).isEqualTo("Gonza");
    }

    @DisplayName("Test eliminar empleado")
    @Test
    void eliminarEmpleado(){
        empleadoRepository.save(empleado);

        empleadoRepository.deleteById(empleado.getId());

        Optional<EmpleadoEntity> empleadoEliminado = empleadoRepository.findById(empleado.getId());

        assertThat(empleadoEliminado).isEmpty();
    }

}
