package com.sqc.acedemy.bai_4.repository;

import com.sqc.acedemy.bai_4.entity.Employee;
import com.sqc.acedemy.bai_4.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IEmployeeRepository extends JpaRepository<Employee, String> {

    @Query("""
        SELECT e FROM Employee e
        LEFT JOIN FETCH e.department d
        WHERE 
            (:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND (:dobFrom IS NULL OR e.dob >= :dobFrom)
        AND (:dobTo IS NULL OR e.dob <= :dobTo)
        AND (:gender IS NULL OR e.gender = :gender)
        AND (:phone IS NULL OR e.phone LIKE CONCAT('%', :phone, '%'))
        AND (:departmentId IS NULL OR d.id = :departmentId)
        AND (:salaryFrom IS NULL OR e.salary >= :salaryFrom)
        AND (:salaryTo IS NULL OR e.salary <= :salaryTo)
    """)
    List<Employee> search(
            @Param("name") String name,
            @Param("dobFrom") LocalDate dobFrom,
            @Param("dobTo") LocalDate dobTo,
            @Param("gender") Gender gender,
            @Param("phone") String phone,
            @Param("departmentId") Integer departmentId,
            @Param("salaryFrom") Double salaryFrom,
            @Param("salaryTo") Double salaryTo
    );
}
