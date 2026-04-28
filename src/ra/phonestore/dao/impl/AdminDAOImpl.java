package ra.phonestore.dao.impl;

import ra.phonestore.dao.IAdminDAO;
import ra.phonestore.model.Admin;
import ra.phonestore.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAOImpl implements IAdminDAO {
    @Override
    public Admin login(String username, String password) {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                return admin;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return null;
    }
}
