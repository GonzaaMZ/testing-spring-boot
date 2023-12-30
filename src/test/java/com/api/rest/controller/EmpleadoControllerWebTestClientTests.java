package com.api.rest.controller;

import com.api.rest.models.EmpleadoEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.*;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmpleadoControllerWebTestClientTests {

    @Autowired
    private WebTestClient webTestClient;

    private EmpleadoEntity empleado;

    private final String url = "http://localhost:8080/api/empleado";


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
    @Order(1)
    void testGuardarCliente() {
        webTestClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(empleado)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(empleado.getId())
                .jsonPath("$.nombre").isEqualTo(empleado.getNombre())
                .jsonPath("$.apellido").isEqualTo(empleado.getApellido())
                .jsonPath("$.email").isEqualTo(empleado.getEmail());
    }

    @Test
    @Order(2)
    void testObtenerEmpleadoById() {
        webTestClient.get().uri(url + "/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.nombre").isEqualTo("Gonzalo")
                .jsonPath("$.apellido").isEqualTo("Amaya")
                .jsonPath("$.email").isEqualTo("gonzaloamaya@gmail.com");
    }

    @Test
    @Order(3)
    void testListarEmpleados() {
        webTestClient.get().uri(url)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").value(hasSize(1))
                .jsonPath("$[0].nombre").isEqualTo("Gonzalo")
                .jsonPath("$[0].apellido").isEqualTo("Amaya")
                .jsonPath("$[0].email").isEqualTo("gonzaloamaya@gmail.com");
    }

    @Test
    @Order(4)
    void testObtenerListadoEmpleados() {
        webTestClient.get().uri(url).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(EmpleadoEntity.class)
                .consumeWith(response -> {
                    List<EmpleadoEntity> empleadosList = response.getResponseBody();
                    Assertions.assertEquals(1, empleadosList.size());
                    Assertions.assertNotNull(empleadosList);
                });
    }


    @Test
    @Order(5)
    void testActualizarEmpleado() {
        EmpleadoEntity empleadoActualizado = EmpleadoEntity.builder()
                .nombre("Gonza")
                .email("gamaya99@gmail.com")
                .apellido("Amaya")
                .build();
        webTestClient.put().uri(url + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(empleadoActualizado)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @Order(6)
    void testEliminarEmpleado() {
        webTestClient.get().uri(url).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(EmpleadoEntity.class)
                .hasSize(1);

        webTestClient.delete().uri(url + "/1").exchange().expectStatus().isOk();

        webTestClient.get().uri(url).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(EmpleadoEntity.class)
                .hasSize(0);

        webTestClient.get().uri(url + "/1").exchange().expectStatus().isNotFound();
    }


}
