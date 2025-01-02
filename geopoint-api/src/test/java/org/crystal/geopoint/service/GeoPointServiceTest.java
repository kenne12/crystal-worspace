package org.crystal.geopoint.service;

import jakarta.persistence.EntityNotFoundException;
import org.crystal.geopoint.dto.GeoPointDTO;
import org.crystal.geopoint.dto.PageDTO;
import org.crystal.geopoint.model.GeoPoint;
import org.crystal.geopoint.repository.GeoPointRepository;
import org.crystal.geopoint.service.impl.GeoPointServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class GeoPointServiceTest {

    @Mock
    private  GeoPointRepository geoPointRepository;

    @InjectMocks
    private GeoPointServiceImpl geoPointService;

    private final GeometryFactory geometryFactory = new GeometryFactory();


    @Test
    void saveGeoPoint_ShouldSaveAndReturnGeoPointDTO() {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(2.3522, 48.8566));

        GeoPointDTO request = new GeoPointDTO(null, "GeoPoint A", 2.3522, 48.8566);
        GeoPoint entity = new GeoPoint(null, "GeoPoint A", point);
        GeoPoint savedEntity = new GeoPoint("123", "GeoPoint A", point);

        when(geoPointRepository.save(entity))
                .thenReturn(savedEntity);

        GeoPointDTO result = geoPointService.saveGeoPoint(request);

        assertNotNull(result);
        assertThat(request).usingRecursiveComparison().ignoringFields("id").isEqualTo(result);
        verify(geoPointRepository, times(1)).save(entity);
    }

    @Test
    public void deleteGeoPoint_ShouldDeleteGeoPoint_WhenGeoPointExists() {
        String geoPointId = "123";
        Point point = geometryFactory.createPoint(new Coordinate(2.3522, 48.8566));
        GeoPoint geoPoint = new GeoPoint("123", "GeoPoint A", point);

        when(geoPointRepository.findById(geoPointId)).thenReturn(Optional.of(geoPoint));

        assertDoesNotThrow(() -> geoPointService.deleteGeoPoint(geoPointId));

        verify(geoPointRepository, times(1)).delete(geoPoint);
    }

    @Test
    void deleteGeoPoint_ShouldThrowException_WhenGeoPointDoesNotExist() {
        String geoPointId = "123";

        when(geoPointRepository.findById(geoPointId))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> geoPointService.deleteGeoPoint(geoPointId));

        assertEquals("GeoPoint not found with id => " + geoPointId, exception.getMessage());
        verify(geoPointRepository, never()).delete(any());
    }

    @Test
    void getAllGeoPoint_ShouldReturnPageOfGeoPointDTO() {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(2.3522, 48.8566));

        Pageable pageable = PageRequest.of(0, 10);
        GeoPoint geoPoint = new GeoPoint("123", "GeoPoint A", point);
        Page<GeoPoint> geoPointPage = new PageImpl<>(List.of(geoPoint));
        Page<GeoPointDTO> expectedPage = geoPointPage.map(GeoPointDTO::fromEntity);

        when(geoPointRepository.findAll(pageable)).thenReturn(geoPointPage);

        PageDTO<GeoPointDTO> result = geoPointService.getAllGeoPoint(pageable);

        assertEquals(expectedPage.getTotalElements(), result.page().totalElements());
        assertEquals("GeoPoint A", result.content().getFirst().title());
        verify(geoPointRepository, times(1)).findAll(pageable);
    }

    @Test
    void computeDistance_ShouldReturnDistance_WhenGeoPointsExist() {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point pointA = geometryFactory.createPoint(new Coordinate(2.3522, 48.8566));
        Point pointB = geometryFactory.createPoint(new Coordinate(2.2945, 48.8584));

        GeoPoint geoPointA = new GeoPoint("123", "GeoPoint A", pointA);
        GeoPoint geoPointB = new GeoPoint("456", "GeoPoint B", pointB);
        double distanceInMeters = 3000;

        when(geoPointRepository.findAllById(List.of("123", "456"))).thenReturn(List.of(geoPointA, geoPointB));

        when(geoPointRepository.findDistance("123", "456")).thenReturn(distanceInMeters);

        double result = geoPointService.computeDistance("123", "456");

        assertEquals(3.0, result); // Distance in kilometers
        verify(geoPointRepository, times(1)).findDistance("123", "456");
    }

    @Test
    void computeDistance_ShouldThrowException_WhenGeoPointDoesNotExist() {
        String geoPointIdA = "123";
        String geoPointIdB = "456";

        when(geoPointRepository.findAllById(List.of(geoPointIdA, geoPointIdB))).thenReturn(List.of());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> geoPointService.computeDistance(geoPointIdA, geoPointIdB));

        assertEquals(String.format("GeoPoints not found with id => %s or %s", geoPointIdA, geoPointIdB), exception.getMessage());
        verify(geoPointRepository, never()).findDistance(anyString(), anyString());
    }

}