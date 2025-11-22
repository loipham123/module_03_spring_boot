package com.sqc.acedemy.bai_4;

import com.sqc.acedemy.ApiException;
import com.sqc.acedemy.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final List<Employee> employees = new ArrayList<>(
            List.of(
                    Employee.builder().id(UUID.randomUUID()).name("Loi").gender(Gender.MALE)
                            .dob(LocalDate.of(2004, 2, 10)).salary(20000.0).phone("123456789").build(),
                    Employee.builder().id(UUID.randomUUID()).name("Linh").gender(Gender.FEMALE)
                            .dob(LocalDate.of(2004, 2, 12)).salary(20000.0).phone("123456789").build(),
                    Employee.builder().id(UUID.randomUUID()).name("Vy").gender(Gender.FEMALE)
                            .dob(LocalDate.of(2005, 2, 15)).salary(20000.0).phone("123456789").build()
            )
    );

    @GetMapping
    public ResponseEntity<ApiResponse<List<Employee>>> getEmployees() {
        return JsonResponse.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Employee>> getById(@PathVariable UUID id) {
        Employee employee = employees.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND));

        return JsonResponse.ok(employee);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Employee>> create(@RequestBody Employee employee) {
        employee.setId(UUID.randomUUID());
        employees.add(employee);

        return JsonResponse.created(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Employee>> update(@PathVariable UUID id, @RequestBody Employee update) {
        Optional<Employee> existingEmployee = employees.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();

        if (existingEmployee.isPresent()) {
            Employee e = existingEmployee.get();

            e.setName(update.getName());
            e.setDob(update.getDob());
            e.setGender(update.getGender());
            e.setSalary(update.getSalary());
            e.setPhone(update.getPhone());

            return JsonResponse.ok(e);
        }

        throw new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID id) {
        // ...
        boolean removed = employees.removeIf(e -> e.getId().equals(id));

        if (removed) {
            return JsonResponse.noContent();
        }

        throw new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND);
    }
}