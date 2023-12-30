package com.api.rest.controller;

import com.api.rest.models.EmpleadoEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmpleadoControllerRestTemplateTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

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
    @Order(1)
    void testGuardarEmpleado(){
        ResponseEntity<EmpleadoEntity> response = testRestTemplate.postForEntity("http://localhost:8080/api/empleado", empleado, EmpleadoEntity.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        EmpleadoEntity empleadoResponse = response.getBody();
        Assertions.assertNotNull(empleadoResponse);
        Assertions.assertEquals(1L, empleadoResponse.getId());
        Assertions.assertEquals("Gonzalo", empleadoResponse.getNombre());
        Assertions.assertEquals("Amaya", empleadoResponse.getApellido());
        Assertions.assertEquals("gonzaloamaya@gmail.com", empleadoResponse.getEmail());

    }

    @Test
    @Order(2)
    void testListarEmpleados(){
        ResponseEntity<EmpleadoEntity[]> response = testRestTemplate.getForEntity("http://localhost:8080/api/empleado", EmpleadoEntity[].class);

        List<EmpleadoEntity> empleadoList = Arrays.asList(response.getBody());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        Assertions.assertEquals(1, empleadoList.size());
        Assertions.assertEquals(1L, empleadoList.get(0).getId());
        Assertions.assertEquals("Gonzalo", empleadoList.get(0).getNombre());
    }

    @Test
    @Order(3)
    void testObtenerEmpleado(){

        ResponseEntity<EmpleadoEntity> response = testRestTemplate.getForEntity("http://localhost:8080/api/empleado/1", EmpleadoEntity.class);
        EmpleadoEntity empleadoResponse = response.getBody();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        Assertions.assertEquals(1L, empleadoResponse.getId());
        Assertions.assertEquals("Gonzalo", empleadoResponse.getNombre());
    }

    @Test
    @Order(4)
    void testEliminarEmpleado(){
        ResponseEntity<EmpleadoEntity[]> response = testRestTemplate.getForEntity("http://localhost:8080/api/empleado", EmpleadoEntity[].class);

        List<EmpleadoEntity> empleadoList = Arrays.asList(response.getBody());
        Assertions.assertEquals(1, empleadoList.size());

        Map<String, Long> pathVariable = new HashMap<>();
        pathVariable.put("id", 1L);
        ResponseEntity<Void> exchange = testRestTemplate.exchange("http://localhost:8080/api/empleado/{id}", HttpMethod.DELETE, null, Void.class, pathVariable);

        Assertions.assertEquals(HttpStatus.OK, exchange.getStatusCode());
        Assertions.assertFalse(exchange.hasBody());

        response = testRestTemplate.getForEntity("http://localhost:8080/api/empleado", EmpleadoEntity[].class);
        empleadoList = Arrays.asList(response.getBody());
        Assertions.assertEquals(0, empleadoList.size());

        ResponseEntity<EmpleadoEntity> responseDetalle = testRestTemplate.getForEntity("http://localhost:8080/api/empleado/2", EmpleadoEntity.class );
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseDetalle.getStatusCode());
        Assertions.assertFalse(responseDetalle.hasBody());

    }

}
