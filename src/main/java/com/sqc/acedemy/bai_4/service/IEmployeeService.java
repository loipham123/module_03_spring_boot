package com.sqc.acedemy.bai_4.service;

import com.sqc.acedemy.bai_4.dto.EmployeeSearchRequest;
import com.sqc.acedemy.bai_4.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IEmployeeService {

    Page<Employee> getAllEmployees(Pageable pageable);

    Employee getEmployeeById(String id);

    Employee createEmployee(Employee employee);

    Employee updateEmployee(String id, Employee employee);

    void deleteEmployee(String id);

    Employee updateAvatar(UUID id, MultipartFile file);

    Page<Employee> search(EmployeeSearchRequest request, Pageable  pageable);
}
