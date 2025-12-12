package com.sqc.acedemy.bai_4.service;

import com.sqc.acedemy.bai_4.exception.ApiException;
import com.sqc.acedemy.bai_4.exception.ErrorCode;
import com.sqc.acedemy.bai_4.entity.Department;
import com.sqc.acedemy.bai_4.repository.IDepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService implements IDepartmentService {

    @Autowired
    private IDepartmentRepository repository;

    @Override
    public List<Department> getAll() {
        return repository.findAll();
    }

    @Override
    public Department getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.DEPARTMENT_NOT_FOUND));
    }

    @Override
    public Department create(Department department) {
        return repository.save(department);
    }

    @Override
    public Department update(Integer id, Department department) {
        // Kiểm tra tồn tại
        Department existing = getById(id);

        department.setId(id);
        return repository.save(department);
    }

    @Override
    public void delete(Integer id) {
        // Kiểm tra tồn tại
        getById(id);

        try {
            repository.deleteById(id);
        } catch (Exception e) {
            // Department đang được tham chiếu bởi bảng Employee
            throw new ApiException(ErrorCode.DEPARTMENT_IN_USE);
        }
    }
}
