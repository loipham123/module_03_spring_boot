package com.sqc.acedemy.bai_4.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Department {
    Integer id;
    String name;
    String code;
}