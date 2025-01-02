package org.crystal.geopoint.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.io.Serializable;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode(exclude = {"title"})
@Table(name = "geo_point")
// TODO: determine how object is equal to another object
public class GeoPoint implements Serializable {
    // TODO: change to UUID
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 100)
    @NotEmpty(message = "Title is required")
    private String title;

    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point point;
}
