package com.sqc.acedemy.bai_4.repository;

import com.sqc.acedemy.bai_4.model.Gender;
import com.sqc.acedemy.bai_4.dto.EmployeeSearchRequest;
import com.sqc.acedemy.bai_4.exception.ApiException;
import com.sqc.acedemy.bai_4.exception.ErrorCode;
import com.sqc.acedemy.bai_4.model.Employee;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class EmployeeRepository implements IEmployeeRepository {

    @Override
    public List<Employee> findAll() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee";

        try (Connection conn = BaseRepository.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                list.add(mapRowToEmployee(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Employee findById(String id) {
        String sql = "SELECT * FROM employee WHERE id = ?";
        try (Connection conn = BaseRepository.getConnection();
             PreparedStatement prepared = conn.prepareStatement(sql)) {

            prepared.setString(1, id);
            ResultSet rs = prepared.executeQuery();
            if (rs.next()) {
                return mapRowToEmployee(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND);
    }

    @Override
    public Employee save(Employee employee) {
        if (employee.getId() == null || employee.getId().isEmpty()) {
            // INSERT
            employee.setId(UUID.randomUUID().toString());
            String sql = "INSERT INTO employee(id, name, dob, gender, salary, phone, department_id) VALUES(?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = BaseRepository.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                setPreparedStatement(ps, employee);
                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // UPDATE
            String sql = "UPDATE employee SET name = ?, dob = ?, gender = ?, salary = ?, phone = ?, department_id = ? WHERE id = ?";

            try (Connection conn = BaseRepository.getConnection();
                 PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

                preparedStatement.setString(1, employee.getName());
                preparedStatement.setDate(2, Date.valueOf(employee.getDob()));
                preparedStatement.setString(3, employee.getGender().name());
                preparedStatement.setDouble(4, employee.getSalary());
                preparedStatement.setString(5, employee.getPhone());
                preparedStatement.setInt(6, employee.getDepartmentId());
                preparedStatement.setString(7, employee.getId());

                int updated = preparedStatement.executeUpdate();
                if (updated == 0) {
                    throw new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return employee;
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM employee WHERE id = ?";
        try (Connection conn = BaseRepository.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            int deleted = ps.executeUpdate();
            if (deleted == 0) {
                throw new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND);
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Employee> search(EmployeeSearchRequest request) {
        List<Employee> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM employee WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (request.getName() != null && !request.getName().isEmpty()) {
            sql.append(" AND name LIKE ?");
            params.add("%" + request.getName() + "%");
        }
        if (request.getDobFrom() != null) {
            sql.append(" AND dob >= ?");
            params.add(Date.valueOf(request.getDobFrom()));
        }
        if (request.getDobTo() != null) {
            sql.append(" AND dob <= ?");
            params.add(Date.valueOf(request.getDobTo()));
        }
        if (request.getGender() != null) {
            sql.append(" AND gender = ?");
            params.add(request.getGender().name());
        }
        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            sql.append(" AND phone LIKE ?");
            params.add("%" + request.getPhone() + "%");
        }
        if (request.getDepartmentId() != null) {
            sql.append(" AND department_id = ?");
            params.add(request.getDepartmentId());
        }
        if (request.getSalaryRange() != null && !request.getSalaryRange().isEmpty()) {
            switch (request.getSalaryRange()) {
                case "lt5":
                    sql.append(" AND salary < 5000000"); break;
                case "5-10":
                    sql.append(" AND salary BETWEEN 5000000 AND 10000000"); break;
                case "10-20":
                    sql.append(" AND salary BETWEEN 10000001 AND 20000000"); break;
                case "gt20":
                    sql.append(" AND salary > 20000000"); break;
            }
        }

        try (Connection conn = BaseRepository.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(mapRowToEmployee(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


    // mapRowToEmployee → ResultSet → Employee
    private Employee mapRowToEmployee(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getString("id"),
                rs.getString("name"),
                rs.getDate("dob").toLocalDate(),
                Gender.valueOf(rs.getString("gender")),
                rs.getDouble("salary"),
                rs.getString("phone"),
                rs.getInt("department_id")
        );
    }

    // setPreparedStatement → Employee → PreparedStatement
    private void setPreparedStatement(PreparedStatement ps, Employee employee) throws SQLException {
        ps.setString(1, employee.getId());
        ps.setString(2, employee.getName());
        ps.setDate(3, Date.valueOf(employee.getDob()));
        ps.setString(4, employee.getGender().name());
        ps.setDouble(5, employee.getSalary());
        ps.setString(6, employee.getPhone());
        ps.setInt(7, employee.getDepartmentId());
    }
}