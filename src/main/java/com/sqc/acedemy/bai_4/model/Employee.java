package com.sqc.acedemy.bai_4.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {
    String id;
    String name;
    LocalDate dob;
    Gender gender;
    double salary;
    String phone;
    Integer departmentId;
}
