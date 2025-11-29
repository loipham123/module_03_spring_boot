package com.sqc.acedemy.bai_4.service;

import com.sqc.acedemy.bai_4.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class JsonResponse {

    // Success 200
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return buildResponse(HttpStatus.OK, data, "Success");
    }

    // Created 201
    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return buildResponse(HttpStatus.CREATED, data, "Created");
    }

    // No Content 204
    public static ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }

    // Bad Request 400
    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message) {
        return buildResponse(HttpStatus.BAD_REQUEST, null, message);
    }

    // Internal Server Error 500
    public static <T> ResponseEntity<ApiResponse<T>> internalError(String message) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, message);
    }

    // Hàm helper chung
    private static <T> ResponseEntity<ApiResponse<T>> buildResponse(HttpStatus status, T data, String message) {
        return ResponseEntity.status(status)
                .body(ApiResponse.<T>builder()
                        .code(status.value())
                        .message(message)
                        .data(data)
                        .build()
                );
    }
}
