package com.sqc.acedemy.bai_4.repository;

import com.sqc.acedemy.bai_4.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDepartmentRepository extends JpaRepository<Department, Integer> {
}
