package com.sqc.acedemy.bai_4;

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
    EMPLOYEE_NOT_FOUND(40403, "Employee is not exist", HttpStatus.NOT_FOUND)
    ;
    int code;
    String message;
    HttpStatus status;
}
