package org.crystal.geopoint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.crystal.geopoint.dto.GeoPointDTO;
import org.crystal.geopoint.dto.PageDTO;
import org.crystal.geopoint.service.GeoPointService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(GeoPointController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class GeoPointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GeoPointService geoPointService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateGeoPoint() throws Exception {
        GeoPointDTO request = new GeoPointDTO(null, "GeoPoint A", 2.3522, 48.8566);
        GeoPointDTO savedGeoPoint = new GeoPointDTO("123", "GeoPoint A", 2.3522, 48.8566);

        when(geoPointService.saveGeoPoint(any(GeoPointDTO.class)))
                .thenReturn(savedGeoPoint);

        // Act & Assert
        mockMvc.perform(post("/api/v1/geo-points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(savedGeoPoint)));

        verify(geoPointService, times(1))
                .saveGeoPoint(any(GeoPointDTO.class));
    }

    @Test
    void shouldDeleteGeoPoint() throws Exception {
        String geoPointId = "123";

        doNothing().when(geoPointService).deleteGeoPoint(geoPointId);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/geo-points/{geoPointId}", geoPointId))
                .andExpect(status().isNoContent());

        verify(geoPointService, times(1))
                .deleteGeoPoint(geoPointId);
    }

    @Test
    void shouldReturnNotFoundWhenGeoPointDoesNotExist() throws Exception {
        String geoPointId = "123";

        doThrow(new EntityNotFoundException(String.format("GeoPoint not found with id => %s", geoPointId)))
                .when(geoPointService)
                .deleteGeoPoint(geoPointId);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/geo-points/{geoPointId}", geoPointId))
                .andExpect(status().isNotFound());

        verify(geoPointService, times(1)).deleteGeoPoint(geoPointId);
    }


    @Test
    void shouldGetAllGeoPoints() throws Exception {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        GeoPointDTO geoPointDTO = new GeoPointDTO("123", "GeoPoint A", 2.3522, 48.8566);
        PageDTO<GeoPointDTO> geoPointPage = new PageDTO<>(List.of(geoPointDTO), new PageDTO.Pagination(0, 10, 1,1));

        when(this.geoPointService.getAllGeoPoint(pageable)).thenReturn(geoPointPage);

        // Act & Assert
        mockMvc.perform(get("/api/v1/geo-points")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("123"))
                .andExpect(jsonPath("$.content[0].title").value("GeoPoint A"));

        verify(geoPointService, times(1)).getAllGeoPoint(any(Pageable.class));
    }

    @Test
    void shouldComputeDistanceBetweenGeoPoints() throws Exception {
        String geoPointIdA = "123";
        String geoPointIdB = "456";
        double distance = 3.0;

        when(geoPointService.computeDistance(geoPointIdA, geoPointIdB)).thenReturn(distance);

        // Act & Assert
        mockMvc.perform(get("/api/v1/geo-points/compute-distance")
                        .param("geoPointIdA", geoPointIdA)
                        .param("geoPointIdB", geoPointIdB))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(distance)));

        verify(geoPointService, times(1)).computeDistance(geoPointIdA, geoPointIdB);
    }

}