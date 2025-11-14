package com.sqc.acedemy;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final List<Employee> employees = new ArrayList<>(
            Arrays.asList(
                    Employee.builder()
                            .id(1)
                            .name("Loi")
                            .gender(Employee.Gender.MALE)
                            .dod(LocalDate.of(2004,2,10))
                            .salary(20000.0)
                            .phone("123456789")
                            .build(),
                    Employee.builder().id(2).build()
            )
    );
}
