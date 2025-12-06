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
                //insert
                employee.setId(UUID.randomUUID().toString());
                //Khi gọi persist, Hibernate sẽ đưa entity vào persistence context (tạm gọi là “bộ nhớ tạm của session”),
                // và thực hiện insert vào database khi commit transaction.
                session.persist(employee);
            }else {
                Employee existing  = session.get(Employee.class,employee.getId());
                if(existing  == null){
                    throw new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND);
                }
                session.merge(employee);
            }
            tx.commit();
            return employee;
        }catch (Exception e){
            if (tx != null) {
                tx.rollback();
                throw e;
            }
        }
        return employee;
//        if (employee.getId() == null || employee.getId().isEmpty()) {
//            // INSERT
//            employee.setId(UUID.randomUUID().toString());
//            String sql = "INSERT INTO employee(id, name, dob, gender, salary, phone, department_id) VALUES(?, ?, ?, ?, ?, ?, ?)";
//
//            try (Connection conn = BaseRepository.getConnection();
//                 PreparedStatement ps = conn.prepareStatement(sql)) {
//
//                setPreparedStatement(ps, employee);
//                ps.executeUpdate();
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        } else {
//            // UPDATE
//            String sql = "UPDATE employee SET name = ?, dob = ?, gender = ?, salary = ?, phone = ?, department_id = ? WHERE id = ?";
//
//            try (Connection conn = BaseRepository.getConnection();
//                 PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
//
//                preparedStatement.setString(1, employee.getName());
//                preparedStatement.setDate(2, Date.valueOf(employee.getDob()));
//                preparedStatement.setString(3, employee.getGender().name());
//                preparedStatement.setDouble(4, employee.getSalary());
//                preparedStatement.setString(5, employee.getPhone());
//                preparedStatement.setInt(6, employee.getDepartmentId());
//                preparedStatement.setString(7, employee.getId());
//
//                int updated = preparedStatement.executeUpdate();
//                if (updated == 0) {
//                    throw new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND);
//                }
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return employee;re
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
//        String sql = "DELETE FROM employee WHERE id = ?";
//        try (Connection conn = BaseRepository.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setString(1, id);
//            int deleted = ps.executeUpdate();
//            if (deleted == 0) {
//                throw new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND);
//            }
//            return true;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
    }

    @Override
    public List<Employee> search(EmployeeSearchRequest request) {
        try (Session session = ConnectionUtil.sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM Employee e WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (request.getName() != null && !request.getName().isEmpty()) {
                hql.append(" AND e.name LIKE ?1");
                params.add("%" + request.getName() + "%");
            }
            if (request.getDobFrom() != null) {
                hql.append(" AND e.dob >= ?2");
                params.add(request.getDobFrom());
            }
            if (request.getDobTo() != null) {
                hql.append(" AND e.dob <= ?3");
                params.add(request.getDobTo());
            }
            if (request.getGender() != null) {
                hql.append(" AND e.gender = ?4");
                params.add(request.getGender());
            }
            if (request.getPhone() != null && !request.getPhone().isEmpty()) {
                hql.append(" AND e.phone LIKE ?5");
                params.add("%" + request.getPhone() + "%");
            }
            if (request.getDepartmentId() != null) {
                hql.append(" AND e.departmentId = ?6");
                params.add(request.getDepartmentId());
            }

            var query = session.createQuery(hql.toString(), Employee.class);
            for (int i = 0; i < params.size(); i++) {
                query.setParameter(i + 1, params.get(i));
            }

            return query.getResultList();
        }catch (Exception e){
            e.printStackTrace();
            return  new  ArrayList<>();
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