package ra.phonestore;

import ra.phonestore.presentation.AuthPresentation;
import ra.phonestore.presentation.CustomerPresentation;
import ra.phonestore.presentation.OrderPresentation;
import ra.phonestore.presentation.ProductPresentation;
import ra.phonestore.utils.InputMethods;

public class Main {
    public static void main(String[] args) {
        if (AuthPresentation.login()) {
            displayMainMenu();
        } else {
            System.err.println("Truy cập bị từ chối!");
        }
    }

    public static void displayMainMenu() {
        while (true) {
            System.out.println("\n***************** QUẢN LÝ CỬA HÀNG ĐIỆN THOẠI *****************");
            System.out.println("1. Quản lý điện thoại");
            System.out.println("2. Quản lý khách hàng");
            System.out.println("3. Quản lý thông tin mua bán");
            System.out.println("0. Thoát");
            System.out.print("Lựa chọn của bạn: ");
            int choice = InputMethods.getInt();
            switch (choice) {
                case 1:
                    ProductPresentation.displayMenu();
                    break;
                case 2:
                    CustomerPresentation.displayMenu();
                    break;
                case 3:
                    OrderPresentation.displayMenu();
                    break;
                case 0:
                    System.out.println("Cảm ơn bạn đã sử dụng hệ thống. Hẹn gặp lại!");
                    System.exit(0);
                default:
                    System.err.println("Lựa chọn không hợp lệ, vui lòng chọn lại (1, 2, 3 hoặc 0).");
            }

        }
    }
}