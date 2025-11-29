package com.sqc.acedemy.bai_4.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseRepository {
    private static String jdbcURL = "jdbc:mysql://localhost:3306/employee_management";
    private static String jdbcUsername = "root";
    private static String jdbcPassword = "123456";
    private static Connection connection;

    /** Phương thức getConnection để lấy kết nối tới cơ sở dữ liệu. Phương thức này kiểm tra
     * nếu connection chưa được khởi tạo (connection == null) thì sẽ tạo một kết nối mới. */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Tải driver JDBC cho MySQL vào bộ nhớ, điều này cần thiết để có thể kết nối.
                Class.forName("com.mysql.jdbc.Driver");
                // Khởi tạo kết nối tới cơ sở dữ liệu với các thông tin đã cung cấp (URL, username, password).
                connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     *  Phương thức closeConnection để đóng kết nối tới cơ sở dữ liệu khi không cần sử dụng nữa.
     */
    public static void closeConnection() throws SQLException {
        connection.close();
        connection = null;
    }
}