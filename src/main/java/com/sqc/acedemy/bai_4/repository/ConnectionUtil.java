package com.sqc.acedemy.bai_4.repository;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

@Repository
public class ConnectionUtil {
    // Đối tượng sessionFactory được sử dụng để quản lý phiên làm việc (Session) trong Hibernate
    public static SessionFactory sessionFactory;

    // Khối khởi tạo tĩnh sẽ được thực hiện khi lớp ConnectionUtil được nạp vào bộ nhớ
    static {
        try {
            // Cấu hình Hibernate và tạo sessionFactory
            sessionFactory = new Configuration()
                    .configure("hibernate.conf.xml") // Đọc cấu hình từ tệp hibernate.conf.xml
                    .buildSessionFactory(); // Tạo sessionFactory từ cấu hình đã được định nghĩa

        } catch (HibernateException e) {
            // Nếu có lỗi trong quá trình cấu hình hoặc tạo sessionFactory
            // In ra thông báo lỗi bằng cách hiển thị stack trace
            e.printStackTrace();
        }
    }
}
