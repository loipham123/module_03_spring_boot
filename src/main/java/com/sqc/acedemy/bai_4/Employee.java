package com.sqc.acedemy.bai_4;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.UUID;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {
    UUID id;
    String name;
    LocalDate dob;
    Gender gender;
    double salary;
    String phone;
}
