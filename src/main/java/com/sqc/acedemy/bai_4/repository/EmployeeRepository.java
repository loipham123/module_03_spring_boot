package com.sqc.acedemy.bai_4.repository;

import com.sqc.acedemy.bai_4.entity.Gender;
import com.sqc.acedemy.bai_4.dto.EmployeeSearchRequest;
import com.sqc.acedemy.bai_4.exception.ApiException;
import com.sqc.acedemy.bai_4.exception.ErrorCode;
import com.sqc.acedemy.bai_4.entity.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class EmployeeRepository implements IEmployeeRepository {

    @Override
    public List<Employee> findAll() {
        Session session= ConnectionUtil.sessionFactory.openSession();
        List<Employee> employee =session.createQuery("FROM Employee",Employee.class).getResultList();
        session.close();
        return employee;
    }

    @Override
    public Employee findById(String id) {
        Session session = ConnectionUtil.sessionFactory.openSession();

        Employee employee =(Employee)session.createQuery("FROM Employee WHERE id = :id")
                .setParameter("id",id)
                .getSingleResult();
        session.close();
        return employee;
    }

    @Override
    public Employee save(Employee employee) {
        Transaction tx = null;

        try(Session session = ConnectionUtil.sessionFactory.openSession()){
            tx = session.beginTransaction();

            if(employee.getId()== null ||  employee.getId().isEmpty()){
                employee.setId(UUID.randomUUID().toString());
                session.persist(employee);
            }else {
                Employee existing  = session.get(Employee.class,employee.getId());
                if(existing  == null){
                    throw new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND);
                }
                session.merge(employee);
            }

            tx.commit();
        } catch (Exception e){
            if (tx != null) tx.rollback();
            throw e;
        }

        return employee;
    }


    @Override
    public boolean delete(String id) {
        Transaction tx = null;
        try(Session session = ConnectionUtil.sessionFactory.openSession()){
            tx = session.beginTransaction();

            Employee employee = session.get(Employee.class, id);
            if(employee == null){
                throw new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND);
            }
            session.remove(employee);
            tx.commit();
            return true;
        }catch (Exception e){
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<Employee> search(EmployeeSearchRequest req) {
        try (Session session = ConnectionUtil.sessionFactory.openSession()) {

            StringBuilder hql = new StringBuilder("FROM Employee e WHERE 1=1");

            var params = new java.util.HashMap<String, Object>();

            if (req.getName() != null && !req.getName().isEmpty()) {
                hql.append(" AND e.name LIKE :name");
                params.put("name", "%" + req.getName() + "%");
            }

            if (req.getDobFrom() != null) {
                hql.append(" AND e.dob >= :dobFrom");
                params.put("dobFrom", req.getDobFrom());
            }

            if (req.getDobTo() != null) {
                hql.append(" AND e.dob <= :dobTo");
                params.put("dobTo", req.getDobTo());
            }

            if (req.getGender() != null) {
                hql.append(" AND e.gender = :gender");
                params.put("gender", req.getGender());
            }

            if (req.getPhone() != null && !req.getPhone().isEmpty()) {
                hql.append(" AND e.phone LIKE :phone");
                params.put("phone", "%" + req.getPhone() + "%");
            }

            if (req.getDepartmentId() != null) {
                hql.append(" AND e.departmentId = :dept");
                params.put("dept", req.getDepartmentId());
            }

            var query = session.createQuery(hql.toString(), Employee.class);

            params.forEach(query::setParameter);

            return query.getResultList();
        }
    }

    @Override
    public Employee updateAvatar(UUID id, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ApiException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        Transaction tx = null;

        try (Session session = ConnectionUtil.sessionFactory.openSession()) {
            tx = session.beginTransaction();

            // 1️⃣ Tìm employee theo ID
            Employee employee = session.get(Employee.class, id.toString());
            if (employee == null) {
                throw new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND);
            }

            // 2️⃣ Tạo thư mục lưu hình
            String uploadDir = "src/main/resources/static/images";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            // 3️⃣ Đặt tên file theo UUID
            String fileName = id + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            // 4️⃣ Lưu file thật vào hệ thống
            Files.write(filePath, file.getBytes());

            // 5️⃣ Lưu đường dẫn tương đối vào DB
            String relativePath = "/images/" + fileName;
            employee.setAvatar(relativePath);

            session.merge(employee);
            tx.commit();

            return employee;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Lỗi khi cập nhật avatar: " + e.getMessage(), e);
        }
    }

}