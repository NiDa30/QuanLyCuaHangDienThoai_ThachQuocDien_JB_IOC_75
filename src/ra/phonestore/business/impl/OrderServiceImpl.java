package ra.phonestore.business.impl;

import ra.phonestore.business.IOrderService;
import ra.phonestore.dao.IOrderDAO;
import ra.phonestore.dao.impl.OrderDAOImpl;
import ra.phonestore.model.Order;
import ra.phonestore.model.OrderDetail;
import ra.phonestore.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderServiceImpl implements IOrderService {
    private final IOrderDAO orderDAO = new OrderDAOImpl();

    @Override
    public List<Order> getAll() {
        return orderDAO.getAll();
    }

    @Override
    public Order getById(Integer id) {
        return orderDAO.getById(id);
    }

    @Override
    public boolean add(Order order) {
        return orderDAO.add(order);
    }

    @Override
    public boolean update(Order order) {
        return orderDAO.update(order);
    }

    @Override
    public boolean delete(Integer id) {
        return orderDAO.delete(id);
    }

    @Override
    public boolean addWithDetails(Order order, List<OrderDetail> details) {
        Connection conn = DBUtil.getConnection();
        try {
            conn.setAutoCommit(false);

            // 1. Thêm Order
            String sqlOrder = "INSERT INTO orders (customer_id, total_amount) VALUES (?, ?) RETURNING id";
            PreparedStatement pstmtOrder = conn.prepareStatement(sqlOrder);
            pstmtOrder.setInt(1, order.getCustomerId());
            pstmtOrder.setDouble(2, order.getTotalAmount());
            ResultSet rsOrder = pstmtOrder.executeQuery();
            
            int orderId = -1;
            if (rsOrder.next()) {
                orderId = rsOrder.getInt(1);
            }

            if (orderId == -1) {
                conn.rollback();
                return false;
            }

            // 2. Thêm Order Details
            String sqlDetail = "INSERT INTO order_detail (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmtDetail = conn.prepareStatement(sqlDetail);
            for (OrderDetail d : details) {
                pstmtDetail.setInt(1, orderId);
                pstmtDetail.setInt(2, d.getProductId());
                pstmtDetail.setInt(3, d.getQuantity());
                pstmtDetail.setDouble(4, d.getUnitPrice());
                pstmtDetail.addBatch();
                
                // 3. Cập nhật tồn kho (Stock)
                String sqlUpdateStock = "UPDATE product SET stock = stock - ? WHERE id = ?";
                PreparedStatement pstmtStock = conn.prepareStatement(sqlUpdateStock);
                pstmtStock.setInt(1, d.getQuantity());
                pstmtStock.setInt(2, d.getProductId());
                pstmtStock.executeUpdate();
            }
            pstmtDetail.executeBatch();

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public List<Order> searchByCustomerName(String name) {
        return orderDAO.searchByCustomerName(name);
    }

    @Override
    public List<Order> searchByDate(String date) {
        return orderDAO.searchByDate(date);
    }

    @Override
    public List<String[]> getRevenueStats(String type) {
        return orderDAO.getRevenueStats(type);
    }

    @Override
    public double getRevenueByPeriod(String start, String end) {
        return orderDAO.getRevenueByPeriod(start, end);
    }
}
