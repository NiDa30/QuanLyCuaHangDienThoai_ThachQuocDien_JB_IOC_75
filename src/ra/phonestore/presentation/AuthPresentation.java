package ra.phonestore.presentation;

import ra.phonestore.dao.IAdminDAO;
import ra.phonestore.dao.impl.AdminDAOImpl;
import ra.phonestore.model.Admin;
import ra.phonestore.utils.InputMethods;

public class AuthPresentation {
    private static final IAdminDAO adminDAO = new AdminDAOImpl();

    public static boolean login() {
        while (true) {
            System.out.println("\n========= HỆ THỐNG QUẢN LÝ CỬA HÀNG =========");
            System.out.println("1. Đăng nhập Admin");
            System.out.println("2. Thoát");
            System.out.println("=============================================");
            System.out.print("Nhập lựa chọn: ");
            int choice = InputMethods.getInt();

            switch (choice) {
                case 1:
                    return handleAdminLogin();
                case 2:
                    System.out.println("Hẹn gặp lại!");
                    System.exit(0);
                default:
                    System.err.println("Lựa chọn không hợp lệ!");
            }
        }
    }
    
    // Chức năng đăng nhập Admin
    private static boolean handleAdminLogin() {
        while (true) {
            System.out.println("\n========= ĐĂNG NHẬP TÀI KHOẢN QUẢN TRỊ =========");
            System.out.print("Tài khoản: ");
            String username = InputMethods.getString();
            if (username.equals("0")) return false;

            System.out.print("Mật khẩu : ");
            String password = InputMethods.getString();
            System.out.println("======================================");

            Admin admin = adminDAO.login(username, password);
            if (admin != null) {
                System.out.println("Đăng nhập thành công! Chào mừng " + admin.getUsername());
                return true;
            } else {
                System.err.println("Sai tài khoản hoặc mật khẩu! Vui lòng thử lại.");
            }
        }
    }
}
