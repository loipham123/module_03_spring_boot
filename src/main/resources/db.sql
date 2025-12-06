-- 1️⃣ Tạo database
CREATE DATABASE IF NOT EXISTS employee_management;
USE employee_management;

-- 2️⃣ Tạo bảng department
CREATE TABLE IF NOT EXISTS department
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

-- 3️⃣ Chèn dữ liệu vào department
INSERT INTO department (name) VALUES
                                  ('Quản lý'),
                                  ('Kế toán'),
                                  ('Sale-Marketing'),
                                  ('Sản xuất');

-- 4️⃣ Tạo bảng employee với UUID kiểu VARCHAR(36)
CREATE TABLE IF NOT EXISTS employee
(
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    dob DATE NOT NULL,
    gender ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
    salary DECIMAL(15,2) NOT NULL,
    phone VARCHAR(15),
    department_id INT,
    CONSTRAINT fk_department FOREIGN KEY (department_id) REFERENCES department(id)
);

-- 5️⃣ Chèn dữ liệu vào employee
INSERT INTO employee (id, name, dob, gender, salary, phone, department_id) VALUES
                                                                               (UUID(), 'Hoàng Văn Hải', '1990-01-15', 'MALE', 15000000.00, '0975123542', 1),
                                                                               (UUID(), 'Trần Thị Hoài', '1985-05-20', 'FEMALE', 14500000.00, '0967869868', 2),
                                                                               (UUID(), 'Lê Văn Sỹ', '1992-03-10', 'MALE', 15500000.00, '0988881110', 3),
                                                                               (UUID(), 'Phạm Duy Khánh', '1988-07-05', 'FEMALE', 14800000.00, '0986555333', 4),
                                                                               (UUID(), 'Hoàng Văn Quý', '1995-09-25', 'MALE', 15200000.00, '0973388668', 4);

ALTER TABLE employee MODIFY COLUMN gender VARCHAR(6) NOT NULL;
ALTER TABLE Employee MODIFY salary DOUBLE;

ALTER TABLE employee
ADD COLUMN avatar VARCHAR(512);

