package com.sqc.acedemy.bai_4.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true)
public enum ErrorCode {
    STUDENT_NOT_FOUND(40401,"Student is not exist",HttpStatus.NOT_FOUND),
    TEACHER_NOT_FOUND(40402,"Teacher is not exist",HttpStatus.NOT_FOUND),
    EMPLOYEE_NOT_FOUND(40403, "Employee is not exist", HttpStatus.NOT_FOUND),
    DEPARTMENT_NOT_FOUND(40404, "Department is not exist", HttpStatus.NOT_FOUND),
    DEPARTMENT_IN_USE(40005, "Department is being used and cannot be deleted", HttpStatus.BAD_REQUEST),
    FILE_EMPTY(40001, "File must not be empty.", HttpStatus.BAD_REQUEST),
    FILE_INVALID_TYPE(40002, "Only image files are allowed.", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE(40003, "File size exceeds the maximum allowed.", HttpStatus.BAD_REQUEST),
    FILE_EXTENSION_MISSING(40004, "File extension is required.", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED(50002, "Failed to store uploaded file.", HttpStatus.INTERNAL_SERVER_ERROR)
    ;
    int code;
    String message;
    HttpStatus status;
}
