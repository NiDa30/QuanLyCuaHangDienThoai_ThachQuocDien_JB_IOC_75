package ra.phonestore.scratch;

import ra.phonestore.utils.DBUtil;
import java.sql.Connection;
import java.sql.Statement;

public class UpdateDatabase {
    public static void main(String[] args) {
        Connection conn = DBUtil.getConnection();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                stmt.execute("ALTER TABLE product ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT FALSE");
                stmt.execute("ALTER TABLE customer ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT FALSE");
                System.out.println("Đã cập nhật database thành công (đã thêm cột is_deleted cho product và customer)!");

            } catch (Exception e) {
                System.err.println("Lỗi khi cập nhật database:");
                e.printStackTrace();
            } finally {
                DBUtil.closeConnection(conn);
            }
        }
    }
}
