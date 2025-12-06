package com.sqc.acedemy.bai_4.controller;

import com.sqc.acedemy.bai_4.dto.EmployeeSearchRequest;
import com.sqc.acedemy.bai_4.entity.Employee;
import com.sqc.acedemy.bai_4.service.IEmployeeService;
import com.sqc.acedemy.bai_4.service.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import com.sqc.acedemy.bai_4.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private IEmployeeService employeeService; // Chỉ gọi Service, không gọi Repository

    @GetMapping
    public ResponseEntity<ApiResponse<List<Employee>>> getAllEmployees() {
        return ResponseEntity.ok(ApiResponse.<List<Employee>>builder()
                .data(employeeService.getAllEmployees())
                .build());
    }

    @GetMapping("/{id}")
    public Object getEmployeeById(@PathVariable String id) {
        // Service đã lo việc check lỗi, Controller chỉ cần trả về
        return JsonResponse.ok(employeeService.getEmployeeById(id));
    }

    @PostMapping
    public Object createEmployee(@RequestBody Employee employee) {
        return JsonResponse.ok(employeeService.createEmployee(employee));
    }

    @PutMapping("/{id}")
    public Object updateEmployee(@PathVariable String id, @RequestBody Employee updatedEmp) {
        return JsonResponse.ok(employeeService.updateEmployee(id, updatedEmp));
    }

    @DeleteMapping("/{id}")
    public Object deleteEmployee(@PathVariable String id) {
        employeeService.deleteEmployee(id);
        return JsonResponse.noContent();
    }

    @GetMapping("/search")
    public Object search(@ModelAttribute EmployeeSearchRequest request) {
        List<Employee> result = employeeService.search(request);
        return JsonResponse.ok(result);
    }

    @PostMapping("/{id}/avatar")
    public ResponseEntity<?> uploadAvatar(@PathVariable("id") UUID id,
                                          @RequestParam("file") MultipartFile file) {
        return JsonResponse.ok(employeeService.updateAvatar(id, file));
    }
}