package com.sqc.acedemy.bai_4.service;

import com.sqc.acedemy.bai_4.dto.EmployeeSearchRequest;
import com.sqc.acedemy.bai_4.exception.ApiException;
import com.sqc.acedemy.bai_4.exception.ErrorCode;
import com.sqc.acedemy.bai_4.entity.Employee;
import com.sqc.acedemy.bai_4.repository.IEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService implements IEmployeeService {

    @Autowired
    private IEmployeeRepository employeeRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private EmailService emailService;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(String id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND));
    }

    @Override
    @Transactional
    public Employee createEmployee(Employee employee) {

        Employee saved = employeeRepository.save(employee);

        emailService.sendEmail(
                saved.getEmail(),
                "Chào mừng!",
                "Xin chào " + saved.getName()
        );

        return saved;
    }

    @Override
    @Transactional
    public Employee updateEmployee(String id, Employee updated) {
        Employee existing = getEmployeeById(id);

        existing.setName(updated.getName());
        existing.setDob(updated.getDob());
        existing.setGender(updated.getGender());
        existing.setSalary(updated.getSalary());
        existing.setPhone(updated.getPhone());
        existing.setDepartment(updated.getDepartment());

        return employeeRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteEmployee(String id) {
        getEmployeeById(id); // kiểm tra tồn tại
        employeeRepository.deleteById(id);
    }

    @Override
    public List<Employee> search(EmployeeSearchRequest req) {
        return employeeRepository.search(
                req.getName(),
                req.getDobFrom(),
                req.getDobTo(),
                req.getGender(),
                req.getPhone(),
                req.getDepartmentId(),
                req.getSalaryFrom(),
                req.getSalaryTo()
        );
    }

    @Override
    @Transactional
    public Employee updateAvatar(UUID id, MultipartFile file) {

        Employee emp = employeeRepository.findById(id.toString())
                .orElseThrow(() -> new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND));

        String oldPath = emp.getAvatar();
        String newPath = fileStorageService.store(id, file);

        if (oldPath != null && !oldPath.equals(newPath)) {
            fileStorageService.delete(oldPath);
        }

        emp.setAvatar(newPath);
        return employeeRepository.save(emp);
    }
}
