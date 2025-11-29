package com.sqc.acedemy.bai_4.repository;

import com.sqc.acedemy.bai_4.model.Department;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DepartmentRepository implements IDepartmentRepository {

    @Override
    public List<Department> findAll() {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM department";

        try (Connection conn = BaseRepository.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Department(rs.getInt("id"), rs.getString("name")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Department findById(Integer id) {
        String sql = "SELECT * FROM department WHERE id = ?";
        try (Connection conn = BaseRepository.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Department(rs.getInt("id"), rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Department save(Department department) {
        if (department.getId() == null) {
            // INSERT
            String sql = "INSERT INTO department(name) VALUES(?)";
            try (Connection conn = BaseRepository.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, department.getName());
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    department.setId(rs.getInt(1));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // UPDATE
            String sql = "UPDATE department SET name = ? WHERE id = ?";
            try (Connection conn = BaseRepository.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, department.getName());
                ps.setInt(2, department.getId());
                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return department;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM department WHERE id = ?";
        try (Connection conn = BaseRepository.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
