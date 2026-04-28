-- 1. Lưu ý: Với PostgreSQL, hãy tạo Database Qly_CuaHangDT trước trong pgAdmin hoặc psql, 
-- sau đó kết nối vào Database này rồi mới chạy script dưới đây.

-- 2. Tạo bảng admin (Quản trị viên) - model User
CREATE TABLE IF NOT EXISTS admin (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- 3. Tạo bảng brand (Nhãn hàng) - model Brand
CREATE TABLE IF NOT EXISTS brand (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

-- 4. Tạo bảng customer (Khách hàng) - model Customer
CREATE TABLE IF NOT EXISTS customer (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    address VARCHAR(255),
    is_deleted BOOLEAN DEFAULT FALSE
);

-- 5. Tạo bảng product (Điện thoại) - model Product
CREATE TABLE IF NOT EXISTS product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    brand_id INT NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    description TEXT,
    is_deleted BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_brand FOREIGN KEY (brand_id) REFERENCES brand(id)
);

-- 6. Tạo bảng orders (Đơn hàng) - model Order
CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer(id)
);

-- 7. Tạo bảng order_detail (Chi tiết đơn hàng) - model OrderDetail
CREATE TABLE IF NOT EXISTS order_detail (
    id SERIAL PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product(id)
);

-- Thêm dữ liệu mẫu (Sử dụng ON CONFLICT để tránh lỗi nếu chạy lại nhiều lần)
INSERT INTO admin (username, password) 
VALUES ('admin', 'admin123') 
ON CONFLICT (username) DO NOTHING;

INSERT INTO brand (name, description) 
VALUES 
    ('Apple', 'iPhone and iPad products'),
    ('Samsung', 'Galaxy phones and tablets')
ON CONFLICT DO NOTHING;

-- Thêm thêm nhãn hàng
INSERT INTO brand (name, description) VALUES
    ('Xiaomi', 'Sản phẩm giá rẻ cấu hình cao'),
    ('Oppo', 'Chuyên gia selfie'),
    ('Vivo', 'Âm nhạc và nhiếp ảnh'),
    ('Realme', 'Dành cho giới trẻ')
ON CONFLICT DO NOTHING;

-- Thêm 10 sản phẩm
INSERT INTO product (name, brand_id, price, stock, description) VALUES
    ('iPhone 15 Pro Max', 1, 32000000, 50, ' flagship Apple 2023'),
    ('iPhone 14', 1, 18000000, 30, 'iPhone tiêu chuẩn'),
    ('Galaxy S24 Ultra', 2, 30000000, 40, ' flagship Samsung 2024'),
    ('Galaxy A54', 2, 10000000, 100, 'Tầm trung Samsung'),
    ('Redmi Note 13', 3, 5000000, 200, 'Xiaomi tầm trung'),
    ('Xiaomi 14', 3, 22000000, 20, 'Xiaomi cao cấp'),
    ('Oppo Reno 11', 4, 12000000, 60, 'Oppo thiết kế đẹp'),
    ('Vivo V30', 5, 11000000, 45, 'Vivo chụp ảnh đẹp'),
    ('Realme 12 Pro', 6, 9000000, 80, 'Realme cấu hình tốt'),
    ('iPhone 13', 1, 15000000, 25, 'iPhone đời cũ giá tốt')
ON CONFLICT DO NOTHING;

-- Thêm 10 khách hàng
INSERT INTO customer (name, phone, address, email) VALUES
    ('Nguyễn Văn A', '0912345678', 'Hà Nội', 'anv@gmail.com'),
    ('Trần Thị B', '0987654321', 'TP.HCM', 'btt@gmail.com'),
    ('Lê Văn C', '0905112233', 'Đà Nẵng', 'clv@gmail.com'),
    ('Phạm Minh D', '0933445566', 'Cần Thơ', 'dpm@gmail.com'),
    ('Hoàng Thị E', '0977889900', 'Hải Phòng', 'eht@gmail.com'),
    ('Đặng Văn F', '0911222333', 'Nghệ An', 'fdv@gmail.com'),
    ('Bùi Thị G', '0944555666', 'Nam Định', 'gbt@gmail.com'),
    ('Võ Văn H', '0966777888', 'Huế', 'hvv@gmail.com'),
    ('Đỗ Minh I', '0922333444', 'Bình Dương', 'idm@gmail.com'),
    ('Ngô Thị K', '0955666777', 'Đồng Nai', 'knt@gmail.com')
ON CONFLICT DO NOTHING;

-- Thêm 20 hóa đơn (giả lập các ngày khác nhau)
INSERT INTO orders (customer_id, created_at, total_amount) VALUES
    (1, '2024-04-01 10:00:00', 32000000),
    (2, '2024-04-02 11:30:00', 18000000),
    (3, '2024-04-03 14:15:00', 30000000),
    (4, '2024-04-05 09:20:00', 10000000),
    (5, '2024-04-07 16:45:00', 5000000),
    (6, '2024-04-10 08:10:00', 22000000),
    (7, '2024-04-12 13:50:00', 12000000),
    (8, '2024-04-15 15:30:00', 11000000),
    (9, '2024-04-18 10:05:00', 9000000),
    (10, '2024-04-20 17:00:00', 15000000),
    (1, '2024-04-21 12:00:00', 15000000),
    (2, '2024-04-22 14:00:00', 5000000),
    (3, '2024-04-23 09:00:00', 10000000),
    (4, '2024-04-24 11:00:00', 32000000),
    (5, '2024-04-25 15:00:00', 18000000),
    (6, '2024-04-26 10:00:00', 30000000),
    (7, '2024-04-27 08:30:00', 5000000),
    (8, '2024-04-27 10:45:00', 9000000),
    (9, '2024-04-27 14:20:00', 22000000),
    (10, '2024-04-27 16:15:00', 12000000)
ON CONFLICT DO NOTHING;

-- Thêm chi tiết hóa đơn tương ứng
INSERT INTO order_detail (order_id, product_id, quantity, unit_price) VALUES
    (1, 1, 1, 32000000),
    (2, 2, 1, 18000000),
    (3, 3, 1, 30000000),
    (4, 4, 1, 10000000),
    (5, 5, 1, 5000000),
    (6, 6, 1, 22000000),
    (7, 7, 1, 12000000),
    (8, 8, 1, 11000000),
    (9, 9, 1, 9000000),
    (10, 10, 1, 15000000),
    (11, 10, 1, 15000000),
    (12, 5, 1, 5000000),
    (13, 4, 1, 10000000),
    (14, 1, 1, 32000000),
    (15, 2, 1, 18000000),
    (16, 3, 1, 30000000),
    (17, 5, 1, 5000000),
    (18, 9, 1, 9000000),
    (19, 6, 1, 22000000),
    (20, 7, 1, 12000000)
ON CONFLICT DO NOTHING;
