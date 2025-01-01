package org.crystal.geopoint.service;

import org.crystal.geopoint.dto.GeoPointDTO;
import org.crystal.geopoint.dto.PageDTO;
import org.springframework.data.domain.Pageable;

public interface GeoPointService {

    GeoPointDTO saveGeoPoint(GeoPointDTO request);

    void deleteGeoPoint(String geoPointId);

    PageDTO<GeoPointDTO> getAllGeoPoint(Pageable pageable);

    double computeDistance(String geoPointIdA, String geoPointIdB);
}
