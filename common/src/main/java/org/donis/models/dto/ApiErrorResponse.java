package org.donis.models.dto;

public record ApiErrorResponse(
    int errorCode,
    String description) {

}