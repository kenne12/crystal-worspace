package org.crystal.geopoint.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode(exclude = {"title"})
@Table(name = "geo_point")
public class GeoPoint implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @NotEmpty(message = "Title is required")
    private String title;

    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point point;
}
