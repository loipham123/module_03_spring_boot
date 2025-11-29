package com.sqc.acedemy.bai_4.repository;

import com.sqc.acedemy.bai_4.model.Department;

import java.util.List;

public interface IDepartmentRepository {
    List<Department> findAll();
    Department findById(Integer id);
    Department save(Department department);
    boolean delete(Integer id);
}
