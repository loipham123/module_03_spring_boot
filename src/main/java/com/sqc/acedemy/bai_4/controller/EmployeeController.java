package com.sqc.acedemy.bai_4.controller;

import com.sqc.acedemy.bai_4.dto.EmployeeSearchRequest;
import com.sqc.acedemy.bai_4.dto.PageResponse;
import com.sqc.acedemy.bai_4.entity.Employee;
import com.sqc.acedemy.bai_4.service.EmailService;
import com.sqc.acedemy.bai_4.service.IEmployeeService;
import com.sqc.acedemy.bai_4.service.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import com.sqc.acedemy.bai_4.dto.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.thymeleaf.context.Context;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private IEmployeeService employeeService; // Chỉ gọi Service, không gọi Repository

    @Autowired
    private EmailService emailService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<Employee>>> getAllEmployees(
            @ModelAttribute EmployeeSearchRequest request,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponse.<PageResponse<Employee>>builder()
                        .data(new PageResponse<>(employeeService.search(request, pageable)))
                        .build()
        );
    }

    @GetMapping("/{id}")
    public Object getEmployeeById(@PathVariable String id) {
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
    public Object search(@ModelAttribute EmployeeSearchRequest request, Pageable pageable) {
        return JsonResponse.ok(
                new PageResponse<>(employeeService.search(request, pageable))
        );
    }


    @PostMapping("/{id}/avatar")
    public ResponseEntity<?> uploadAvatar(@PathVariable("id") UUID id,
                                          @RequestParam("file") MultipartFile file) {
        return JsonResponse.ok(employeeService.updateAvatar(id, file));


    }

    @PostMapping("/{id}/send-email")
    public ResponseEntity<String> sendEmployeeInfo(@PathVariable String id) {
        Employee emp = employeeService.getEmployeeById(id);

        if (emp.getEmail() == null || emp.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Employee chưa có email");
        }

        // Chuẩn bị dữ liệu Thymeleaf
        Context context = new Context();
        context.setVariable("name", emp.getName());
        context.setVariable("email", emp.getEmail());
        context.setVariable("dob", emp.getDob());
        context.setVariable("gender", emp.getGender());
        context.setVariable("salary", emp.getSalary());
        context.setVariable("phone", emp.getPhone());
        context.setVariable("departmentId", emp.getDepartment());
        context.setVariable("avatar", emp.getAvatar() != null ? emp.getAvatar() : null);

        emailService.sendHtmlEmail(
                emp.getEmail(),
                "Thông tin nhân viên " + emp.getName(),
                "employee_info",  // tên file: employee_info.html
                context
        );

        return ResponseEntity.ok("Đã gửi thông tin nhân viên tới " + emp.getEmail());
    }


}


