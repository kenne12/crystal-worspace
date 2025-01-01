package org.crystal.geopoint.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.crystal.geopoint.model.GeoPoint;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

public record GeoPointDTO(String id,
                          @NotEmpty(message = "Title is required")
                          @NotNull(message = "Longitude is required")
                          String title,
                          @NotNull(message = "Longitude is required")
                          Double longitude,
                          @NotNull(message = "Longitude is required")
                          Double latitude) {

    public static GeoPoint toEntity(GeoPointDTO geoPointDTO) {
        if (geoPointDTO == null) {
            throw new IllegalArgumentException("GeoPointDTO is null");
        }
        return GeoPoint.builder()
                .id(geoPointDTO.id())
                .title(geoPointDTO.title())
                .point(new GeometryFactory().createPoint(new Coordinate(geoPointDTO.longitude(), geoPointDTO.latitude())))
                .build();
    }

    public static GeoPointDTO fromEntity(GeoPoint geoPoint) {
        if (geoPoint == null) {
            throw new IllegalArgumentException("GeoPoint is null");
        }
        return new GeoPointDTO(geoPoint.getId(), geoPoint.getTitle(), geoPoint.getPoint().getX(), geoPoint.getPoint().getY());
    }
}
