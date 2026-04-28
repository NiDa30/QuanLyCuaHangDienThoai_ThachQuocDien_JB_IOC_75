package ra.phonestore.dao.impl;

import ra.phonestore.dao.ICustomerDAO;
import ra.phonestore.model.Customer;
import ra.phonestore.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements ICustomerDAO {

    @Override
    public List<Customer> getAll() {
        List<Customer> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM customer WHERE is_deleted = FALSE ORDER BY id ASC");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return list;
    }

    @Override
    public Customer getById(Integer id) {
        Customer c = null;
        Connection conn = DBUtil.getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM customer WHERE id = ? AND is_deleted = FALSE");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                c = mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return c;
    }

    @Override
    public Customer getByPhone(String phone) {
        Customer c = null;
        Connection conn = DBUtil.getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM customer WHERE phone = ?");
            pstmt.setString(1, phone);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                c = mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return c;
    }

    @Override
    public boolean add(Customer c) {
        boolean success = false;
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "INSERT INTO customer (name, phone, address, email) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, c.getName());
            pstmt.setString(2, c.getPhone());
            pstmt.setString(3, c.getAddress());
            pstmt.setString(4, c.getEmail());
            success = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return success;
    }

    @Override
    public boolean update(Customer c) {
        boolean success = false;
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "UPDATE customer SET name = ?, phone = ?, address = ?, email = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, c.getName());
            pstmt.setString(2, c.getPhone());
            pstmt.setString(3, c.getAddress());
            pstmt.setString(4, c.getEmail());
            pstmt.setInt(5, c.getId());
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
            String sql = "UPDATE customer SET is_deleted = TRUE WHERE id = ?";
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
    public List<Customer> getDeletedList() {
        List<Customer> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM customer WHERE is_deleted = TRUE ORDER BY id ASC");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return list;
    }

    @Override
    public boolean restore(Integer id) {
        boolean success = false;
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "UPDATE customer SET is_deleted = FALSE WHERE id = ?";
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
    public boolean permanentDelete(Integer id) {
        boolean success = false;
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "DELETE FROM customer WHERE id = ?";
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

    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {


        Customer c = new Customer();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setPhone(rs.getString("phone"));
        c.setAddress(rs.getString("address"));
        c.setEmail(rs.getString("email"));
        c.setDeleted(rs.getBoolean("is_deleted"));
        return c;
    }

}
