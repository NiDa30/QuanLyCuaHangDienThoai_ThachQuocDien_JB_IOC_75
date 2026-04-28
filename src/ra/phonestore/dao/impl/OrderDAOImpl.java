package ra.phonestore.dao.impl;

import ra.phonestore.dao.IOrderDAO;
import ra.phonestore.model.Order;
import ra.phonestore.model.OrderDetail;
import ra.phonestore.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements IOrderDAO {

    @Override
    // Lấy danh sách tất cả hóa đơn
    public List<Order> getAll() {
        List<Order> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM orders ORDER BY id DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return list;
    }

    @Override
    // Lấy hóa đơn theo ID
    public Order getById(Integer id) {
        Order order = null;
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM orders WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                order = mapResultSetToOrder(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return order;
    }

    @Override
    public boolean add(Order order) {
        // This is usually handled within a transaction in addWithDetails
        return false;
    }

    @Override
    public boolean update(Order order) {
        return false; // Orders usually not updated this way
    }

    @Override
    public boolean delete(Integer id) {
        return false; // Orders usually not deleted
    }

    @Override
    public boolean addDetails(List<OrderDetail> details) {
        // This logic is usually better handled in a transaction in Service layer or inside a specific DAO method
        return false;
    }

    @Override
    // Tìm kiếm hóa đơn theo tên khách hàng
    public List<Order> searchByCustomerName(String name) {
        List<Order> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT o.* FROM orders o JOIN customer c ON o.customer_id = c.id WHERE c.name ILIKE ? ORDER BY o.id DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return list;
    }

    @Override
    // Tìm kiếm hóa đơn theo ngày
    public List<Order> searchByDate(String date) {
        List<Order> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM orders WHERE created_at::date = ?::date ORDER BY id DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return list;
    }

    @Override
    // Tính doanh thu theo khoảng thời gian
    public double getRevenueByPeriod(String start, String end) {
        double total = 0;
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT SUM(total_amount) FROM orders WHERE created_at::date BETWEEN ?::date AND ?::date";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, start);
            pstmt.setString(2, end);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return total;
    }

    @Override
    // Lấy doanh thu theo ngày, tháng, năm
    public List<String[]> getRevenueStats(String type) {
        List<String[]> stats = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        try {
            String format = "";
            switch (type.toLowerCase()) {
                case "day":
                    format = "YYYY-MM-DD";
                    break;
                case "month":
                    format = "YYYY-MM";
                    break;
                case "year":
                    format = "YYYY";
                    break;
                default:
                    return stats;
            }
            
            String sql = "SELECT TO_CHAR(created_at, ?) as period, SUM(total_amount) as total " +
                         "FROM orders GROUP BY period ORDER BY period DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, format);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                stats.add(new String[]{rs.getString("period"), rs.getString("total")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return stats;
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {

        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setCustomerId(rs.getInt("customer_id"));
        order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        order.setTotalAmount(rs.getDouble("total_amount"));
        return order;
    }
}
