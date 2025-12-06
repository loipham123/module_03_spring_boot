package com.sqc.acedemy.bai_4.service;

import com.sqc.acedemy.bai_4.entity.Department;

import java.util.List;

public interface IDepartmentService {
    List<Department> getAll();
    Department getById(Integer id);
    Department create(Department department);
    Department update(Integer id, Department department);
    void delete(Integer id);
}