package com.sqc.acedemy.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true)
public enum ErrorCode {
    STUDENT_NOT_FOUND(40401,"Student is not exist",HttpStatus.NOT_FOUND),
    TEACHER_NOT_FOUND(40402,"Teacher is not exist",HttpStatus.NOT_FOUND)
    ;
    int code;
    String message;
    HttpStatus status;
}
