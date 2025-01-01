package org.crystal.geopoint.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseError(String message, Set<String> errors) {
}
