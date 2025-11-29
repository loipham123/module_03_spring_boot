package com.sqc.acedemy.bai_4.repository;
import com.sqc.acedemy.bai_4.dto.EmployeeSearchRequest;
import com.sqc.acedemy.bai_4.model.Employee;

import java.util.List;

public interface IEmployeeRepository {
    List<Employee> findAll();
    Employee findById(String id);
    Employee save(Employee employee);
    boolean delete(String id);
    List<Employee> search(EmployeeSearchRequest request);
}
