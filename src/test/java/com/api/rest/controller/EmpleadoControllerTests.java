package com.api.rest.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import com.api.rest.models.EmpleadoEntity;
import com.api.rest.service.EmpleadoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest
public class EmpleadoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @Autowired
    private ObjectMapper objectMapper;

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


    @Test
    void testGuardarEmpleado() throws Exception {
        //given
        given(empleadoService.save(any(EmpleadoEntity.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(post("/api/empleado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleado))
        );

        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido", is(empleado.getApellido())))
                .andExpect(jsonPath("$.email", is(empleado.getEmail())));
    }


    @Test
    void testListarEmpleados() throws Exception {
        List<EmpleadoEntity> listEmpleados = new ArrayList<>();
        listEmpleados.add(EmpleadoEntity.builder().nombre("Pepe").apellido("Argento").email("peparg@gmail.com").build());
        listEmpleados.add(EmpleadoEntity.builder().nombre("David").apellido("Gonzalez").email("gonzalez@gmail.com").build());
        listEmpleados.add(EmpleadoEntity.builder().nombre("German").apellido("Garcia").email("ggarcia@gmail.com").build());

        given(empleadoService.findAll()).willReturn(listEmpleados);

        ResultActions response = mockMvc.perform(get("/api/empleado"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listEmpleados.size())));
    }

    @Test
    void testObtenerEmpleadoNoEncontrado() throws Exception {
        //given
        Long empleadoId = 1L;
        given(empleadoService.getById(empleadoId)).willReturn(Optional.empty());

        //when
        ResultActions response = mockMvc.perform(get("/api/empleado/{id}", empleadoId));

        //then
        response.andExpect(status().isNotFound());

    }

    @Test
    void testActulizarEmpleado() throws Exception {
        //given
        Long empleadoId = 1L;
        EmpleadoEntity empleadoActualizado = EmpleadoEntity.builder()
                        .nombre("Gonza")
                        .email("gonzalito@gmail.com")
                        .build();

        given(empleadoService.getById(empleadoId)).willReturn(Optional.of(empleado));
        given(empleadoService.update(any(EmpleadoEntity.class)))
                .willAnswer(invocation -> invocation.getArgument(0));
        //when
        ResultActions response = mockMvc.perform(put("/api/empleado/{id}", empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));

        //then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(empleadoActualizado.getNombre())))
                .andExpect(jsonPath("$.apellido", is(empleadoActualizado.getApellido())))
                .andExpect(jsonPath("$.email", is(empleadoActualizado.getEmail())));
    }

    @Test
    void testActualizarEmpleadoNulo() throws Exception {
        //given
        Long empleadoId = 1L;
        EmpleadoEntity empleadoActualizado = EmpleadoEntity.builder()
                .nombre("Gonza")
                .email("gonzalito@gmail.com")
                .build();

        given(empleadoService.getById(empleadoId)).willReturn(Optional.empty());
        given(empleadoService.update(any(EmpleadoEntity.class)))
                .willAnswer(invocation -> invocation.getArgument(0));
        //when
        ResultActions response = mockMvc.perform(put("/api/empleado/{id}", empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void test() throws Exception {
        //given

        //when

        //then
    }

    @Test
    void testEliminarEmpleado() throws Exception {
        //given
        Long empleadoId = 1L;
        willDoNothing().given(empleadoService).delete(empleadoId);

        //when
        ResultActions response = mockMvc.perform(delete("/api/empleado/{id}", empleadoId));
        //then
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
