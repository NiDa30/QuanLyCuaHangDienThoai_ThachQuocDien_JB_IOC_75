package ra.phonestore.dao.impl;

import ra.phonestore.dao.IBrandDAO;
import ra.phonestore.model.Brand;
import ra.phonestore.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BrandDAOImpl implements IBrandDAO {

    @Override
    public List<Brand> getAll() {
        List<Brand> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM brand ORDER BY id ASC");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Brand b = new Brand();
                b.setId(rs.getInt("id"));
                b.setName(rs.getString("name"));
                b.setDescription(rs.getString("description"));
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return list;
    }

    @Override
    public Brand getById(Integer id) {
        Brand b = null;
        Connection conn = DBUtil.getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM brand WHERE id = ?");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                b = new Brand();
                b.setId(rs.getInt("id"));
                b.setName(rs.getString("name"));
                b.setDescription(rs.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return b;
    }

    @Override
    public boolean add(Brand b) {
        boolean success = false;
        Connection conn = DBUtil.getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO brand (name, description) VALUES (?, ?)");
            pstmt.setString(1, b.getName());
            pstmt.setString(2, b.getDescription());
            success = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return success;
    }

    @Override
    public boolean update(Brand b) {
        boolean success = false;
        Connection conn = DBUtil.getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement("UPDATE brand SET name = ?, description = ? WHERE id = ?");
            pstmt.setString(1, b.getName());
            pstmt.setString(2, b.getDescription());
            pstmt.setInt(3, b.getId());
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
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM brand WHERE id = ?");
            pstmt.setInt(1, id);
            success = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return success;
    }
}
