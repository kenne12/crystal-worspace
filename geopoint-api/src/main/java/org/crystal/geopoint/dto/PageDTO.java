package org.crystal.geopoint.dto;

import java.util.List;

public record PageDTO<T>(List<T> content, Pagination page) {

    public static record Pagination(int number, int size, long totalElements, int totalPages) {
    }
}



