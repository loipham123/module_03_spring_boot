package com.sqc.acedemy;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {
    int id;
    String name;
    LocalDate dod;
    Gender gender;
    double salary;
    String phone;

    public enum  Gender {
        MALE, FEMALE, OTHER
    }
}
