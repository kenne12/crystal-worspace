package org.crystal.geopoint;

import org.crystal.geopoint.model.GeoPoint;
import org.crystal.geopoint.repository.GeoPointRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class CrystalTestApplication {

    private final GeometryFactory geometryFactory = new GeometryFactory();

    public static void main(String[] args) {
        SpringApplication.run(CrystalTestApplication.class, args);
    }

    @Bean
    @Transactional
    @Profile("!test")
    public CommandLineRunner commandLineRunner(GeoPointRepository geoPointRepository) {
        return args -> {
            geoPointRepository.deleteAll();
            GeoPoint geoPointA = geoPointRepository.save(new GeoPoint(null,"GeoPoint A", geometryFactory.createPoint(new Coordinate(2.3522, 48.8566))));
            GeoPoint geoPointB = geoPointRepository.save(new GeoPoint(null,"GeoPoint B", geometryFactory.createPoint(new Coordinate(2.4293, 48.6734))));

            GeoPoint geoPointC = geoPointRepository.save(new GeoPoint(null,"GeoPoint C", geometryFactory.createPoint(new Coordinate(2.3522, 48.8566))));
            GeoPoint geoPointD = geoPointRepository.save(new GeoPoint(null,"GeoPoint D", geometryFactory.createPoint(new Coordinate(2.3619, 48.8519))));

            double distanceA = geoPointRepository.findDistance(geoPointA.getId(), geoPointB.getId());
            double distanceB = geoPointRepository.findDistance(geoPointC.getId(), geoPointD.getId());

            System.out.println("Distance between => " + distanceA);
            System.out.println("Distance between => " + distanceB);
        };
    }
}
