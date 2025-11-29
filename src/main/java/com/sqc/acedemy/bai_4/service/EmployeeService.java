package com.sqc.acedemy.bai_4.service;

import com.sqc.acedemy.bai_4.dto.EmployeeSearchRequest;
import com.sqc.acedemy.bai_4.exception.ApiException;
import com.sqc.acedemy.bai_4.exception.ErrorCode;
import com.sqc.acedemy.bai_4.model.Employee;
import com.sqc.acedemy.bai_4.repository.IEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService implements IEmployeeService{
    @Autowired
    private IEmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(String id) {
        Employee e = employeeRepository.findById(id);
        if (e == null) {
            throw new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }
        return e;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        employee.setId(UUID.randomUUID().toString());
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(String id, Employee updatedEmp) {
        Employee existingEmp = getEmployeeById(id); // Check tồn tại

        existingEmp.setName(updatedEmp.getName());
        existingEmp.setDob(updatedEmp.getDob());
        existingEmp.setGender(updatedEmp.getGender());
        existingEmp.setSalary(updatedEmp.getSalary());
        existingEmp.setPhone(updatedEmp.getPhone());

        return employeeRepository.save(existingEmp);
    }

    @Override
    public void deleteEmployee(String id) {
        getEmployeeById(id); // Check tồn tại
        employeeRepository.delete(id);
    }

    @Override
    public List<Employee> search(EmployeeSearchRequest request) {
        return employeeRepository.search(request);
    }
}
