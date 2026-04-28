package ra.phonestore.presentation;

import ra.phonestore.business.ICustomerService;
import ra.phonestore.business.impl.CustomerServiceImpl;
import ra.phonestore.model.Customer;
import ra.phonestore.utils.InputMethods;

import java.util.List;


public class CustomerPresentation {
    private static final ICustomerService customerService = new CustomerServiceImpl();

    public static void displayMenu() {

        while (true) {
            System.out.println("\n========== QUẢN LÝ KHÁCH HÀNG ==========");

            System.out.println("1. Hiển thị danh sách khách hàng");
            System.out.println("2. Thêm khách hàng mới");
            System.out.println("3. Cập nhật thông tin khách hàng(không cập nhật ID)");
            System.out.println("4. Xóa khách hàng theo ID");
            System.out.println("5. Thùng rác (Khôi phục / Xóa vĩnh viễn)");
            System.out.println("0. Quay lại Menu chính");
            System.out.print("Vui lòng nhập lựa chọn của bạn: ");

            int choice = InputMethods.getInt();
            switch (choice) {
                case 1:
                    showList();
                    break;

                case 2:
                    addNewCustomer();
                    break;

                case 3:
                    updateCustomer();
                    break;

                case 4:
                    deleteCustomer();
                    break;
                case 5:
                    trashMenu();
                    break;

                case 0:
                    return;

                default:
                    System.err.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    // Chức năng hiển thị danh sách khách hàng
    private static void deleteCustomer() {
        System.out.println("\n--- XÓA KHÁCH HÀNG ---");
        Customer customer = null;
        int idToDelete = -1;

        while (true) {
            System.out.print("Nhập ID khách hàng muốn xóa (nhập 0 để HỦY): ");
            idToDelete = InputMethods.getInt();
            if (idToDelete == 0) return;
            customer = customerService.getById(idToDelete);
            if (customer != null) {
                break;
            }
            System.err.println("Mã khách hàng không tồn tại, vui lòng nhập lại!");
        }

        System.out.println("Thông tin khách hàng sắp xóa:");
        System.out.println(customer);
        System.out.print("Bạn có chắc chắn muốn xóa khách hàng này không? (y/n): ");
        if (InputMethods.getBoolean()) {
            if (customerService.delete(idToDelete)) {
                System.out.println("Xóa khách hàng thành công (đã chuyển vào thùng rác)!");
            } else {
                System.err.println("Xóa thất bại. Vui lòng kiểm tra lại kết nối.");
            }
        } else {
            System.out.println("Đã hủy thao tác xóa.");
        }
    }

    // Chức năng thùng rác khách hàng
    private static void trashMenu() {
        while (true) {
            List<Customer> deletedList = customerService.getDeletedList();
            System.out.println("\n========== THÙNG RÁC KHÁCH HÀNG ==========");
            if (deletedList.isEmpty()) {
                System.out.println("Thùng rác trống.");
            } else {
                System.out.println("\n------------------------------------------- DANH SÁCH KHÁCH HÀNG ĐÃ XÓA -------------------------------------------");
                System.out.println("| ID   | Họ tên               | Số điện thoại | Email                     | Địa chỉ                        |");
                System.out.println("------------------------------------------------------------------------------------------------------------");
                for (Customer c : deletedList) {
                    System.out.println(c);
                }
                System.out.println("------------------------------------------------------------------------------------------------------------");
            }

            System.out.println("1. Khôi phục khách hàng");
            System.out.println("2. Xóa vĩnh viễn");
            System.out.println("0. Quay lại");
            System.out.print("Vui lòng nhập lựa chọn của bạn: ");
            int choice = InputMethods.getInt();
            switch (choice) {
                case 1:
                    restoreCustomerAction();
                    break;
                case 2:
                    permanentDeleteCustomerAction();
                    break;
                case 0:
                    return;
                default:
                    System.err.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    // Chức năng khôi phục khách hàng
    private static void restoreCustomerAction() {
        System.out.print("Nhập ID khách hàng cần khôi phục (hoặc 0 để quay lại): ");
        int id = InputMethods.getInt();
        if (id == 0) return;

        if (customerService.restore(id)) {
            System.out.println("Khôi phục khách hàng thành công!");
        } else {
            System.err.println("Mã khách hàng không tồn tại trong danh sách đã xóa.");
        }
    }

    // Chức năng xóa vĩnh viễn khách hàng
    private static void permanentDeleteCustomerAction() {
        System.out.print("Nhập ID khách hàng cần xóa VĨNH VIỄN (nhập 0 để HỦY): ");
        int id = InputMethods.getInt();
        if (id == 0) return;

        // Kiểm tra xem ID có trong thùng rác không
        List<Customer> deletedList = customerService.getDeletedList();
        boolean exists = deletedList.stream().anyMatch(c -> c.getId() == id);

        if (!exists) {
            System.err.println("Mã khách hàng không tồn tại trong thùng rác!");
            return;
        }

        System.out.print("Bạn có chắc chắn muốn xóa VĨNH VIỄN khách hàng này không? (y/n): ");
        if (InputMethods.getBoolean()) {
            if (customerService.permanentDelete(id)) {
                System.out.println("Đã xóa vĩnh viễn khách hàng.");
            } else {
                System.err.println("Xóa thất bại. Khách hàng có thể đang liên kết với đơn hàng.");
            }
        } else {
            System.out.println("Đã hủy thao tác.");
        }
    }

    // Chức năng cập nhật thông tin khách hàng
    private static void updateCustomer() {

        System.out.println("\n--- CẬP NHẬT THÔNG TIN KHÁCH HÀNG ---");
        Customer customerToUpdate = null;
        int idToUpdate = -1;

        while (true) {
            System.out.print("Nhập ID khách hàng cần cập nhật (nhập 0 để HỦY): ");
            idToUpdate = InputMethods.getInt();
            if (idToUpdate == 0) return;
            customerToUpdate = customerService.getById(idToUpdate);
            if (customerToUpdate != null) {
                break;
            }
            System.err.println("Mã khách hàng không tồn tại, vui lòng nhập lại!");
        }

        // Hiển thị thông tin hiện tại
        System.out.println("Thông tin hiện tại của khách hàng:");
        System.out.println("Tên: " + customerToUpdate.getName());
        System.out.println("SĐT: " + customerToUpdate.getPhone());
        System.out.println("Email: " + customerToUpdate.getEmail());
        System.out.println("Địa chỉ: " + customerToUpdate.getAddress());

        // Nhập thông tin mới
        System.out.print("Nhập tên mới (để trống nếu không đổi): ");
        String name = InputMethods.getOptionalString();
        if (!name.isEmpty()) {
            customerToUpdate.setName(name);
        }

        System.out.print("Nhập số điện thoại mới (để trống nếu không đổi): ");
        String phone = InputMethods.getOptionalString();
        if (!phone.isEmpty()) {
            // Kiểm tra trùng SĐT nếu thay đổi SĐT
            if (!phone.equals(customerToUpdate.getPhone())) {
                if (customerService.getByPhone(phone) != null) {
                    System.err.println("Số điện thoại này đã tồn tại trên hệ thống. Không thể cập nhật SĐT này!");
                } else {
                    customerToUpdate.setPhone(phone);
                }
            }
        }

        System.out.print("Nhập email mới (để trống nếu không đổi): ");
        String email = InputMethods.getOptionalString();
        if (!email.isEmpty()) {
            customerToUpdate.setEmail(email);
        }

        System.out.print("Nhập địa chỉ mới (để trống nếu không đổi): ");
        String address = InputMethods.getOptionalString();
        if (!address.isEmpty()) {
            customerToUpdate.setAddress(address);
        }

        if (customerService.update(customerToUpdate)) {
            System.out.println("Cập nhật thông tin khách hàng thành công!");
        } else {
            System.err.println("Cập nhật thất bại. Vui lòng kiểm tra lại kết nối.");
        }
    }

    // Chức năng thêm khách hàng mới
    private static void addNewCustomer() {
        System.out.println("\n--- THÊM KHÁCH HÀNG MỚI (Nhập 0 để HỦY) ---");
        
        System.out.print("Nhập tên khách hàng: ");
        String name = InputMethods.getString();
        if (name.equals("0")) {
            System.out.println("Đã hủy thao tác thêm khách hàng.");
            return;
        }

        String phone;
        while (true) {
            System.out.print("Nhập số điện thoại (10 số, bắt đầu bằng 0): ");
            phone = InputMethods.getString();
            if (phone.equals("0")) {
                System.out.println("Đã hủy thao tác thêm khách hàng.");
                return;
            }
            if (phone.matches("^0[0-9]{9}$")) {
                if (customerService.getByPhone(phone) != null) {
                    System.err.println("Số điện thoại này đã tồn tại trên hệ thống!");
                } else {
                    break;
                }
            } else {
                System.err.println("Số điện thoại không đúng định dạng (phải có 10 chữ số và bắt đầu bằng số 0)!");
            }
        }

        String email;
        while (true) {
            System.out.print("Nhập email (phải có đuôi @gmail.com): ");
            email = InputMethods.getString();
            if (email.equals("0")) {
                System.out.println("Đã hủy thao tác thêm khách hàng.");
                return;
            }
            if (email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
                break;
            } else {
                System.err.println("Email không đúng định dạng Gmail (ví dụ: abc@gmail.com)!");
            }
        }

        System.out.print("Nhập địa chỉ: ");
        String address = InputMethods.getString();
        if (address.equals("0")) {
            System.out.println("Đã hủy thao tác thêm khách hàng.");
            return;
        }

        Customer newCustomer = new Customer(0, name, phone, email, address);
        if (customerService.add(newCustomer)) {
            System.out.println("Thêm khách hàng mới thành công!");
        } else {
            System.err.println("Thêm mới thất bại. Vui lòng kiểm tra lại kết nối.");
        }
    }

    // Chức năng hiển thị danh sách khách hàng
    private static void showList() {

        List<Customer> list = customerService.getAll();
        if (list.isEmpty()) {
            System.out.println("Danh sách khách hàng trống.");
        } else {
            System.out.println("\n------------------------------------------- DANH SÁCH KHÁCH HÀNG -------------------------------------------");
            System.out.println("| ID   | Họ tên               | Số điện thoại | Email                     | Địa chỉ                        |");
            System.out.println("------------------------------------------------------------------------------------------------------------");
            for (Customer c : list) {
                System.out.println(c);
            }
            System.out.println("------------------------------------------------------------------------------------------------------------");
        }
    }
}

