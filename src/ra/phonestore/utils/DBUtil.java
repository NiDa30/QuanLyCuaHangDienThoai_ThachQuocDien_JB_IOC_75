package ra.phonestore.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://localhost:5432/Qly_CuaHangDT";
    private static final String USER = "postgres";
    private static final String PASS = "123";

    public static Connection getConnection() {
        Connection conn = null;
        try {

            Class.forName("org.postgresql.Driver");

            conn = DriverManager.getConnection(URL, USER, PASS);
        }
        catch (ClassNotFoundException e) {
        System.err.println(" LỖI: Chưa nạp được Driver JAR vào dự án!");
        e.printStackTrace();
    } catch (SQLException e) {
        System.err.println(" LỖI: Driver đã nhận nhưng thông số kết nối sai!");
        e.printStackTrace();
    }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
