package com.sqc.acedemy.bai_4.controller;

import com.sqc.acedemy.bai_4.exception.ApiException;
import com.sqc.acedemy.bai_4.model.Employee;
import com.sqc.acedemy.bai_4.exception.ErrorCode;
import com.sqc.acedemy.bai_4.model.Gender;
import com.sqc.acedemy.bai_4.service.JsonResponse;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.stream.Collectors;
import com.sqc.acedemy.bai_4.dto.ApiResponse;
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

    private static final UUID IT_DEPT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID MKT_DEPT_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    private final List<Employee> employees = new ArrayList<>(
            List.of(
                    Employee.builder().id(UUID.randomUUID()).name("Loi").gender(Gender.MALE)
                            .dob(LocalDate.of(2004, 2, 10)).salary(25000.0).phone("123456789")
                            .departmentId(IT_DEPT_ID).build(),

                    Employee.builder().id(UUID.randomUUID()).name("Linh").gender(Gender.FEMALE)
                            .dob(LocalDate.of(2004, 2, 12)).salary(8000.0).phone("987654321")
                            .departmentId(MKT_DEPT_ID).build(),

                    Employee.builder().id(UUID.randomUUID()).name("Vy").gender(Gender.FEMALE)
                            .dob(LocalDate.of(2005, 2, 15)).salary(4500.0).phone("999888777")
                            .departmentId(IT_DEPT_ID).build()
            )
    );

    @GetMapping
    public ResponseEntity<ApiResponse<List<Employee>>> getEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dobFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dobTo,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) String salaryRange,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) UUID departmentId
    ) {

        List<Employee> filteredList = employees.stream()
                // 1. Lọc theo Tên (name)
                .filter(e -> name == null || e.getName().toLowerCase().contains(name.toLowerCase()))

                // 2. Lọc theo Khoảng Ngày sinh (dobFrom, dobTo)
                .filter(e -> dobFrom == null || !e.getDob().isBefore(dobFrom)) // >= dobFrom
                .filter(e -> dobTo == null || !e.getDob().isAfter(dobTo))      // <= dobTo

                // 3. Lọc theo Giới tính (gender)
                .filter(e -> gender == null || e.getGender().equals(gender))

                // 4. Lọc theo Số điện thoại (phone)
                .filter(e -> phone == null || e.getPhone().contains(phone))

                // 5. Lọc theo Bộ phận (departmentId)
                .filter(e -> departmentId == null || departmentId.equals(e.getDepartmentId()))

                // 6. Lọc theo Khoảng lương (salaryRange)
                .filter(e -> {
                    if (salaryRange == null) return true;
                    double salary = e.getSalary() / 1000.0; // Tính theo nghìn (k)

                    return switch (salaryRange.toLowerCase()) {
                        case "lt5" -> salary < 5;
                        case "5-10" -> salary >= 5 && salary <= 10;
                        case "10-20" -> salary > 10 && salary <= 20;
                        case "gt20" -> salary > 20;
                        default -> false; // Bỏ qua nếu tham số không hợp lệ
                    };
                })
                .collect(Collectors.toList());

        return JsonResponse.ok(filteredList);
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
            e.setDepartmentId(update.getDepartmentId());

            return JsonResponse.ok(e);
        }

        throw new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID id) {
        boolean removed = employees.removeIf(e -> e.getId().equals(id));

        if (removed) {
            return JsonResponse.noContent();
        }

        throw new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND);
    }
}