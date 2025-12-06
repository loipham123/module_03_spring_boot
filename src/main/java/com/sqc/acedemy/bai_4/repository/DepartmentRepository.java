package com.sqc.acedemy.bai_4.repository;

import com.sqc.acedemy.bai_4.entity.Department;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class DepartmentRepository implements IDepartmentRepository {

    @Override
    public List<Department> findAll() {
        try (Session session = ConnectionUtil.sessionFactory.openSession()) {
            return session.createQuery("FROM Department", Department.class)
                    .getResultList();
        }
    }

    @Override
    public Department findById(Integer id) {
        try (Session session = ConnectionUtil.sessionFactory.openSession()) {
            return session.get(Department.class, id);
        }
    }

    @Override
    public Department save(Department department) {
        Transaction tx = null;

        try (Session session = ConnectionUtil.sessionFactory.openSession()) {
            tx = session.beginTransaction();

            if (department.getId() == null) {
                // INSERT
                session.persist(department);
            } else {
                // UPDATE
                Department existing = session.get(Department.class, department.getId());
                if (existing == null) {
                    throw new RuntimeException("Department not found");
                }
                session.merge(department);
            }

            tx.commit();
            return department;
        }
        catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public boolean delete(Integer id) {
        Transaction tx = null;

        try (Session session = ConnectionUtil.sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Department department = session.get(Department.class, id);
            if (department == null) {
                return false;
            }

            session.remove(department);
            tx.commit();
            return true;
        }
        catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}
