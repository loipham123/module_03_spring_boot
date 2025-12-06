package com.sqc.acedemy.bai_4.repository;
import com.sqc.acedemy.bai_4.dto.EmployeeSearchRequest;
import com.sqc.acedemy.bai_4.entity.Employee;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IEmployeeRepository {
    List<Employee> findAll();
    Employee findById(String id);
    Employee save(Employee employee);
    boolean delete(String id);
    List<Employee> search(EmployeeSearchRequest request);
    Employee updateAvatar(UUID id, MultipartFile file);

}
