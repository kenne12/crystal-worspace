package org.crystal.geopoint.controller.e2e;

import org.crystal.geopoint.dto.GeoPointDTO;
import org.crystal.geopoint.dto.PageDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class GeoPointControllerIntegrationTest {

    private static final String BASE_URL = "/api/v1/geo-points";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgisContainer =
            new PostgreSQLContainer<>(
                    DockerImageName.parse("postgis/postgis:17-3.5")
                            .asCompatibleSubstituteFor("postgres")
            )
                    .withDatabaseName("test_db")
                    .withUsername("myuser")
                    .withPassword("mypassword")
                    .withInitScript("init_postgis.sql");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgisContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgisContainer::getUsername);
        registry.add("spring.datasource.password", postgisContainer::getPassword);
    }

    private GeoPointDTO pointA;
    private GeoPointDTO pointB;

    @BeforeEach
    public void setUp() {
        this.pointA = new GeoPointDTO(null, "GeoPoint A", 2.3522, 48.8566);
        this.pointB = new GeoPointDTO(null, "GeoPoint B", 3.0, 50.0);
    }

    @Test
    @Order(1)
    void shouldContainerStart() {
        assertThat(postgisContainer.isCreated()).isTrue();
        assertThat(postgisContainer.isRunning()).isTrue();
    }

    @Test
    @Order(2)
    @Rollback
    void shouldSaveGeoPoint() {
        GeoPointDTO request = new GeoPointDTO(null, "GeoPoint A", 2.3522, 48.8566);
        ResponseEntity<GeoPointDTO> response = testRestTemplate.postForEntity(BASE_URL, request, GeoPointDTO.class);
        GeoPointDTO responseContent = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseContent).usingRecursiveComparison().ignoringFields("id").isEqualTo(request);
    }

    @Test
    @Order(3)
    @Rollback
    void shouldNotSaveGeoPointInvalidRequest() {
        GeoPointDTO request = new GeoPointDTO(null, "", null, 48.8566);
        ResponseEntity<GeoPointDTO> response = testRestTemplate.postForEntity("/api/v1/geo-points", request, GeoPointDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(4)
    @Rollback
    void shouldGetAllGeoPoints() {
        GeoPointDTO geoPointRequestA = new GeoPointDTO(null, "GeoPoint A", 2.3522, 48.8566);
        GeoPointDTO geoPointRequestB = new GeoPointDTO(null, "GeoPoint B", 3.0, 50.0);

        testRestTemplate.postForEntity(BASE_URL, geoPointRequestA, GeoPointDTO.class);
        testRestTemplate.postForEntity(BASE_URL, geoPointRequestB, GeoPointDTO.class);

        ResponseEntity<PageDTO<GeoPointDTO>> response = testRestTemplate.exchange(
                BASE_URL + "?page=0&size=10",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(Objects.requireNonNull(response.getBody()).page().totalElements()).isGreaterThanOrEqualTo(2);
    }

    @Test
    @Order(5)
    @Rollback
    void shouldDeleteGeoPoint() {
        GeoPointDTO request = new GeoPointDTO(null, "GeoPoint A", 2.3522, 48.8566);
        GeoPointDTO savedPoint = testRestTemplate.postForEntity(BASE_URL, request, GeoPointDTO.class).getBody();

        assertThat(savedPoint).isNotNull();

        ResponseEntity<Void> response = testRestTemplate.exchange(
                BASE_URL + "/" + Objects.requireNonNull(savedPoint).id(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(6)
    @Rollback
    void shouldComputeDistanceBetweenGeoPoints() {
        GeoPointDTO savedPointA = testRestTemplate.postForEntity(BASE_URL, this.pointA, GeoPointDTO.class).getBody();
        GeoPointDTO savedPointB = testRestTemplate.postForEntity(BASE_URL, this.pointB, GeoPointDTO.class).getBody();

        assertThat(savedPointA).isNotNull();
        assertThat(savedPointB).isNotNull();

        String computeDistanceUrl = String.format("%s/compute-distance?geoPointIdA=%s&geoPointIdB=%s",
                BASE_URL, Objects.requireNonNull(savedPointA).id(), Objects.requireNonNull(savedPointB).id());

        ResponseEntity<Double> response = testRestTemplate.getForEntity(computeDistanceUrl, Double.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isGreaterThan(0);
    }

    @Test
    @Order(7)
    void shouldReturn404ForNonexistentGeoPoint() {
        String nonExistentId = "non-existent-id";

        ResponseEntity<Void> response = testRestTemplate.exchange(
                BASE_URL + "/" + nonExistentId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
