package com.sqc.acedemy.bai_4.controller;

import com.sqc.acedemy.bai_4.exception.ApiException;
import com.sqc.acedemy.bai_4.dto.ApiResponse;
import com.sqc.acedemy.bai_4.model.Department;
import com.sqc.acedemy.bai_4.exception.ErrorCode;
import com.sqc.acedemy.bai_4.service.JsonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    // Dữ liệu mẫu (sử dụng UUID để liên kết với Employee sau này)
    private final List<Department> departmentList = new ArrayList<>(
            List.of(
                    Department.builder().id(UUID.fromString("11111111-1111-1111-1111-111111111111")).name("IT").code("IT").build(),
                    Department.builder().id(UUID.fromString("22222222-2222-2222-2222-222222222222")).name("Marketing").code("MKT").build()
            )
    );

    // Lấy danh sách (GET /departments)
    @GetMapping
    public ResponseEntity<ApiResponse<List<Department>>> getAll() {
        return JsonResponse.ok(departmentList);
    }

    // Lấy chi tiết theo ID (GET /departments/{id})
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Department>> getById(@PathVariable UUID id) {
        Department department = departmentList.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorCode.DEPARTMENT_NOT_FOUND));

        return JsonResponse.ok(department);
    }

    // Thêm mới (POST /departments)
    @PostMapping
    public ResponseEntity<ApiResponse<Department>> create(@RequestBody Department department) {
        department.setId(UUID.randomUUID());
        departmentList.add(department);
        return JsonResponse.created(department);
    }

    // Cập nhật (PUT /departments/{id})
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Department>> update(@PathVariable UUID id, @RequestBody Department update) {
        Optional<Department> existingDept = departmentList.stream().filter(d -> d.getId().equals(id)).findFirst();

        if (existingDept.isPresent()) {
            Department dept = existingDept.get();
            dept.setName(update.getName());
            dept.setCode(update.getCode());
            return JsonResponse.ok(dept);
        }

        throw new ApiException(ErrorCode.DEPARTMENT_NOT_FOUND);
    }

    // Xóa (DELETE /departments/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        boolean removed = departmentList.removeIf(d -> d.getId().equals(id));

        if (removed) {
            return JsonResponse.noContent();
        }

        throw new ApiException(ErrorCode.DEPARTMENT_NOT_FOUND);
    }
}
