package org.crystal.geopoint.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crystal.geopoint.dto.GeoPointDTO;
import org.crystal.geopoint.dto.PageDTO;
import org.crystal.geopoint.service.GeoPointService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/geo-points")
@RequiredArgsConstructor
@Slf4j
public class GeoPointController {

    private final GeoPointService geoPointService;

    @GetMapping
    public PageDTO<GeoPointDTO> getAllGeoPoint(Pageable pageable) {
        log.info("Getting all geo points");
        return geoPointService.getAllGeoPoint(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GeoPointDTO saveGeoPoint(@RequestBody @Valid GeoPointDTO request) {
        log.info("Saving new geo point {} ", request);
        return geoPointService.saveGeoPoint(request);
    }

    @DeleteMapping("/{geoPointId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGeoPoint(@PathVariable("geoPointId") String geoPointId) {
        log.info("Deleting geo point with id {}", geoPointId);
        geoPointService.deleteGeoPoint(geoPointId);
    }

    @GetMapping("/compute-distance")
    public double computeDistance(@RequestParam(value = "geoPointIdA", required = true) String geoPointIdA,
                                  @RequestParam(value = "geoPointIdB", required = true) String geoPointIdB) {
        log.info("Computing distance between {} and {}", geoPointIdA, geoPointIdB);
        return geoPointService.computeDistance(geoPointIdA, geoPointIdB);
    }
}
