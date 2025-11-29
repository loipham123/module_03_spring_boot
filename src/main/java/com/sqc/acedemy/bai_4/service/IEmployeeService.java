package com.sqc.acedemy.bai_4.service;

import com.sqc.acedemy.bai_4.dto.EmployeeSearchRequest;
import com.sqc.acedemy.bai_4.model.Employee;

import java.util.List;

public interface IEmployeeService {
    List<Employee> getAllEmployees();
    Employee getEmployeeById(String id);
    Employee createEmployee(Employee employee);
    Employee updateEmployee(String id, Employee employee);
    void deleteEmployee(String id);


    List<Employee> search(EmployeeSearchRequest request);
}
