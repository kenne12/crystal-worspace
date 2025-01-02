package org.crystal.geopoint.repository;

import org.crystal.geopoint.model.GeoPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoPointRepository extends JpaRepository<GeoPoint, String> {

    @Query("SELECT ST_DistanceSphere (p1.point, p2.point) FROM GeoPoint p1 JOIN GeoPoint p2 ON p1.id = :pointIdA AND p2.id = :pointIdB")
    double findDistance(@Param("pointIdA") String pointIdA, @Param("pointIdB") String pointIdB);

    boolean existsById(@NonNull String geoPointId);
}
