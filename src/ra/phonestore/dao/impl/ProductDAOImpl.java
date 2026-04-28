package ra.phonestore.dao.impl;

import ra.phonestore.dao.IProductDAO;
import ra.phonestore.model.Product;
import ra.phonestore.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements IProductDAO {

    @Override
    // Lấy danh sách tất cả sản phẩm
    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT p.*, b.name as brand_name FROM product p " +
                         "LEFT JOIN brand b ON p.brand_id = b.id " +
                         "WHERE p.is_deleted = FALSE ORDER BY p.id ASC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return list;
    }

    @Override
    // Lấy sản phẩm theo ID
    public Product getById(Integer id) {
        Product p = null;
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT p.*, b.name as brand_name FROM product p " +
                         "LEFT JOIN brand b ON p.brand_id = b.id " +
                         "WHERE p.id = ? AND p.is_deleted = FALSE";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                p = mapResultSetToProduct(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return p;
    }

    @Override
    public boolean add(Product p) {
        boolean success = false;
        Connection conn = DBUtil.getConnection();
        try {
            // Đổi 'brand' thành 'brand_id' để khớp với database.sql
            String sql = "INSERT INTO product (name, brand_id, price, stock, description) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, p.getName());
            pstmt.setInt(2, p.getBrandId());
            pstmt.setDouble(3, p.getPrice());
            pstmt.setInt(4, p.getStock());
            pstmt.setString(5, p.getDescription());
            success = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return success;
    }

    @Override
    public boolean update(Product p) {
        boolean success = false;
        Connection conn = DBUtil.getConnection();
        try {
            // Đổi 'brand' thành 'brand_id'
            String sql = "UPDATE product SET name = ?, brand_id = ?, price = ?, stock = ?, description = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, p.getName());
            pstmt.setInt(2, p.getBrandId());
            pstmt.setDouble(3, p.getPrice());
            pstmt.setInt(4, p.getStock());
            pstmt.setString(5, p.getDescription());
            pstmt.setInt(6, p.getId());
            success = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return success;
    }

    @Override
    public boolean delete(Integer id) {
        boolean success = false;
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "UPDATE product SET is_deleted = TRUE WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            success = pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return success;
    }

    @Override
    // Tìm kiếm sản phẩm theo hãng
    public List<Product> searchByBrand(String brandName) {
        List<Product> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        try {
            // Đổi 'brand' thành 'brand_id' để khớp với database.sql
            String sql = "SELECT p.*, b.name as brand_name FROM product p " +
                         "LEFT JOIN brand b ON p.brand_id = b.id " +
                         "WHERE p.is_deleted = FALSE AND p.brand_id IN (SELECT id FROM brand WHERE name ILIKE ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, "%" + brandName + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return list;
    }

    @Override
    // Tìm kiếm sản phẩm theo khoảng giá
    public List<Product> searchByPriceRange(double min, double max) {
        List<Product> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT p.*, b.name as brand_name FROM product p " +
                         "LEFT JOIN brand b ON p.brand_id = b.id " +
                         "WHERE p.is_deleted = FALSE AND p.price BETWEEN ? AND ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, min);

            pstmt.setDouble(2, max);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return list;
    }

    @Override
    // Tìm kiếm sản phẩm theo số lượng tồn kho
    public List<Product> searchByStock(int minStock) {
        List<Product> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT p.*, b.name as brand_name FROM product p " +
                         "LEFT JOIN brand b ON p.brand_id = b.id " +
                         "WHERE p.is_deleted = FALSE AND p.stock >= ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, minStock);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return list;
    }

    @Override
    // Tìm kiếm sản phẩm theo tên
    public List<Product> searchByName(String name) {
        List<Product> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT p.*, b.name as brand_name FROM product p " +
                         "LEFT JOIN brand b ON p.brand_id = b.id " +
                         "WHERE p.is_deleted = FALSE AND p.name ILIKE ? ORDER BY p.id ASC";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return list;
    }

    @Override
    // Lấy danh sách sản phẩm đã xóa
    public List<Product> getDeletedList() {
        List<Product> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT p.*, b.name as brand_name FROM product p " +
                         "LEFT JOIN brand b ON p.brand_id = b.id " +
                         "WHERE p.is_deleted = TRUE ORDER BY p.id ASC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return list;
    }

    @Override
    // Khôi phục sản phẩm
    public boolean restore(Integer id) {
        boolean success = false;
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "UPDATE product SET is_deleted = FALSE WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            success = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return success;
    }

    @Override
    // Xóa vĩnh viễn sản phẩm
    public boolean permanentDelete(Integer id) {
        boolean success = false;
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "DELETE FROM product WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            success = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return success;
    }

    @Override
    // Lấy các điểm giá trị quan trọng (percentiles) để phân loại sản phẩm
    public List<Double> getPricePercentiles() {
        List<Double> percentiles = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        try {
            // Tính toán 6 điểm mốc (0%, 20%, 40%, 60%, 80%, 100%) để chia thành 5 khoảng
            String sql = "SELECT percentile_cont(ARRAY[0, 0.2, 0.4, 0.6, 0.8, 1.0]) WITHIN GROUP (ORDER BY price) FROM product WHERE is_deleted = FALSE";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Array sqlArray = rs.getArray(1);
                if (sqlArray != null) {
                    Double[] values = (Double[]) sqlArray.getArray();
                    for (Double val : values) {
                        percentiles.add(val);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return percentiles;
    }

    // Map ResultSet sang Product
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {

        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setBrandId(rs.getInt("brand_id")); 
        p.setBrandName(rs.getString("brand_name")); // Lấy tên brand từ cột alias
        p.setPrice(rs.getDouble("price"));
        p.setStock(rs.getInt("stock"));
        p.setDescription(rs.getString("description"));
        p.setDeleted(rs.getBoolean("is_deleted"));
        return p;
    }

}
