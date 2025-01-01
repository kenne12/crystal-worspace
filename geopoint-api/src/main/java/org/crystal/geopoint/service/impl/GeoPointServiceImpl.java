package org.crystal.geopoint.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crystal.geopoint.dto.GeoPointDTO;
import org.crystal.geopoint.dto.PageDTO;
import org.crystal.geopoint.model.GeoPoint;
import org.crystal.geopoint.repository.GeoPointRepository;
import org.crystal.geopoint.service.GeoPointService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class GeoPointServiceImpl implements GeoPointService {

    private final GeoPointRepository geoPointRepository;

    @Override
    public GeoPointDTO saveGeoPoint(GeoPointDTO request) {
        return GeoPointDTO.fromEntity(geoPointRepository.save(GeoPointDTO.toEntity(request)));
    }

    @Override
    public void deleteGeoPoint(String geoPointId) {
        Optional<GeoPoint> optionalPoint = geoPointRepository.findById(geoPointId);
        if (optionalPoint.isEmpty()) {
            log.error("Geo point not found with id => {}", geoPointId);
            throw new EntityNotFoundException(String.format("GeoPoint not found with id => %s", geoPointId));
        }
        geoPointRepository.delete(optionalPoint.get());
    }

    @Override
    public PageDTO<GeoPointDTO> getAllGeoPoint(Pageable pageable) {
        Page<GeoPoint> all = geoPointRepository.findAll(pageable);

        return new PageDTO<>(
                all.getContent().stream().map(GeoPointDTO::fromEntity).toList(),
                new PageDTO.Pagination(all.getNumber(), all.getSize(), all.getTotalElements(),all.getTotalPages())
        );
    }

    @Override
    public double computeDistance(String geoPointIdA, String geoPointIdB) {

        geoPointRepository.findById(geoPointIdA)
                .orElseThrow(() -> new EntityNotFoundException(String.format("GeoPoint not found with id => %s", geoPointIdA)));

        geoPointRepository.findById(geoPointIdB)
                .orElseThrow(() -> new EntityNotFoundException(String.format("GeoPoint not found with id => %s", geoPointIdB)));

        return geoPointRepository.findDistance(geoPointIdA, geoPointIdB) / 1000;
    }
}
