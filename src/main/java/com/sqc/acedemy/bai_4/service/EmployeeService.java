package com.sqc.acedemy.bai_4.service;

import com.sqc.acedemy.bai_4.dto.EmployeeSearchRequest;
import com.sqc.acedemy.bai_4.exception.ApiException;
import com.sqc.acedemy.bai_4.exception.ErrorCode;
import com.sqc.acedemy.bai_4.entity.Employee;
import com.sqc.acedemy.bai_4.repository.IEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService implements IEmployeeService{
    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    private IEmployeeRepository employeeRepository;

    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
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
    @Transactional
    public Employee createEmployee(Employee employee) {
        // 1. Tạo ID và lưu DB
        employee.setId(UUID.randomUUID().toString());
        Employee savedEmployee = employeeRepository.save(employee);

        // 2. 💡 Gửi mail chào mừng (chạy bất đồng bộ)
        String subject = "👋 Chào mừng đến với Công ty!";
        String body = String.format(
                "Xin chào %s,\n\n" +
                        "Hồ sơ của bạn đã được tạo thành công trong hệ thống Quản lý Nhân sự với ID: %s. \n" +
                        "Chúng tôi rất vui mừng chào đón bạn gia nhập đội ngũ.",
                savedEmployee.getName(), savedEmployee.getId()
        );

        emailService.sendEmail(
                savedEmployee.getEmail(),
                subject,
                body
        );

        return savedEmployee;
    }

    @Override
    @Transactional
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
    @Transactional
    public void deleteEmployee(String id) {
        getEmployeeById(id); // Check tồn tại
        employeeRepository.delete(id);
    }

    @Override
    @Transactional
    public List<Employee> search(EmployeeSearchRequest request) {
        return employeeRepository.search(request);
    }

    @Override
    public Employee updateAvatar(UUID id, MultipartFile file) {

        // Repository yêu cầu String → convert UUID → String
        Employee employee = employeeRepository.findById(id.toString());
        if (employee == null) {
            throw new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }

        String oldAvatar = employee.getAvatar();

        String avatarPath = fileStorageService.store(id, file);

        if (oldAvatar != null && !oldAvatar.equals(avatarPath)) {
            fileStorageService.delete(oldAvatar);
        }

        employee.setAvatar(avatarPath);
        return employeeRepository.save(employee);
    }


}
