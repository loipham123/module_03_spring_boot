package com.sqc.acedemy.bai_4.entity;

import jdk.jfr.Enabled;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    String id;
    String name;
    LocalDate dob;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    Gender gender;
    @Column(name = "salary")
    double salary;
    String phone;
    @Column(name = "department_id")
    Integer departmentId;
    String avatar;
}
