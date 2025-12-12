package com.sqc.acedemy.bai_4.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;


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
    @Column(name = "email", unique = true, nullable = false)
    String email;
    LocalDate dob;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    Gender gender;
    @Column(name = "salary")
    double salary;
    String phone;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonIgnore
    private Department department;
    String avatar;
}
