package com.api.rest.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import com.api.rest.exception.ResourceNotFoundException;
import com.api.rest.models.EmpleadoEntity;
import com.api.rest.repository.EmpleadoRepository;
import com.api.rest.service.impl.EmpleadoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceTests {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoServiceImpl empleadoService;

    private EmpleadoEntity empleado;

    @BeforeEach
    void setup() {
        empleado = EmpleadoEntity.builder()
                .id(1L)
                .nombre("Gonzalo")
                .apellido("Amaya")
                .email("gonzaloamaya@gmail.com")
                .build();
    }


    @DisplayName("Test guardar empleado - service")
    @Test
    void testGuardarEmpleado() {

        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.empty());
        given(empleadoRepository.save(empleado)).willReturn(empleado);

        EmpleadoEntity empleadoEntity = empleadoService.save(empleado);

        assertThat(empleadoEntity).isNotNull();
    }

    @DisplayName("Test guardar empleado con throw exception - service")
    @Test
    void testGuardarEmpleadoException() {

        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.of(empleado));

        assertThrows(ResourceNotFoundException.class, () -> {
            empleadoService.save(empleado);
        });

        verify(empleadoRepository, never()).save(any(EmpleadoEntity.class));


    }


    @DisplayName("Test listar empleados - service")
    @Test
    void testListarEmpleados() {

        EmpleadoEntity empleadoEntity = EmpleadoEntity.builder()
                .id(2L)
                .nombre("Pepe")
                .apellido("Argento")
                .email("pepearg@gmail.com")
                .build();

        given(empleadoRepository.findAll()).willReturn(List.of(empleadoEntity, empleado));

        List<EmpleadoEntity> empleados = empleadoService.findAll();

        assertThat(empleados).isNotNull();
        assertThat(empleados).size().isEqualTo(2);
    }

    @Test
    void testListarCollectionEmpleadosVacia() {
        EmpleadoEntity empleadoEntity = EmpleadoEntity.builder()
                .id(1L)
                .nombre("Pepe")
                .apellido("Argento")
                .email("pepearg@gmail.com")
                .build();

        given(empleadoRepository.findAll()).willReturn(Collections.emptyList());

        List<EmpleadoEntity> empleados = empleadoService.findAll();

        assertThat(empleados).isEmpty();
        assertThat(empleados).size().isEqualTo(0);
    }

    @Test
    void testObtenerEmpleadoById() {
        //given
        given(empleadoRepository.findById(1L)).willReturn(Optional.of(empleado));

        //when
        EmpleadoEntity empleado1 = empleadoService.getById(empleado.getId()).get();

        //then
        assertThat(empleado1).isNotNull();
    }

    @Test
    void testActualizarEmpleado(){
        given(empleadoRepository.save(empleado)).willReturn(empleado);
        empleado.setEmail("emailactualizado@gmail.com");
        empleado.setNombre("Gonza");

        EmpleadoEntity empleadoActualizado = empleadoService.update(empleado);

        assertThat(empleadoActualizado.getEmail()).isEqualTo("emailactualizado@gmail.com");
        assertThat(empleadoActualizado.getNombre()).isEqualTo("Gonza");
    }

    @Test
    void testEliminarEmpleado(){
        Long empleadoId = 1L;
        willDoNothing().given(empleadoRepository).deleteById(empleadoId);

        empleadoService.delete(empleadoId);

        verify(empleadoRepository, times(1)).deleteById(empleadoId);
    }


}
