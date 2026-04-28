package ra.phonestore.presentation;

import ra.phonestore.business.ICustomerService;

import ra.phonestore.business.IOrderService;
import ra.phonestore.business.impl.CustomerServiceImpl;
import ra.phonestore.business.impl.OrderServiceImpl;
import ra.phonestore.business.IProductService;
import ra.phonestore.business.impl.ProductServiceImpl;
import ra.phonestore.model.Customer;
import ra.phonestore.model.Order;
import ra.phonestore.model.OrderDetail;
import ra.phonestore.model.Product;
import ra.phonestore.utils.InputMethods;

import java.util.ArrayList;
import java.util.List;


public class OrderPresentation {
    private static final IOrderService orderService = new OrderServiceImpl();
    private static final ICustomerService customerService = new CustomerServiceImpl();
    private static final IProductService productService = new ProductServiceImpl();


    public static void displayMenu() {

        while (true) {
            System.out.println("\n========== QUẢN LÝ THÔNG TIN MUA BÁN ==========");
            System.out.println("1. Hiển thị danh sách hóa đơn");
            System.out.println("2. Thêm đơn hàng mới");
            System.out.println("3. Tìm kiếm hóa đơn");
            System.out.println("4. Thống kê doanh thu");
            System.out.println("0. Quay lại");
            System.out.print("Vui lòng nhập lựa chọn của bạn: ");

            int choice = InputMethods.getInt();
            switch (choice) {
                case 1:
                    showList();
                    break;

                case 2:
                    addNewOrder();
                    break;

                case 3:
                    searchMenu();
                    break;
                case 4:
                    statisticMenu();
                    break;
                case 0:
                    return;
                default:
                    System.err.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    // Chức năng in danh sách hóa đơn
    private static void showList() {
        List<Order> list = orderService.getAll();
        displayOrderTable(list, "DANH SÁCH HÓA ĐƠN");
    }

    // Chức năng hiển thị danh sách hóa đơn
    private static void displayOrderTable(List<Order> list, String title) {
        if (list.isEmpty()) {
            System.out.println("Không tìm thấy hóa đơn nào.");
        } else {
            System.out.println("\n------------------------------------------- " + title + " -------------------------------------------");
            System.out.println("| ID   | Khách hàng           | Ngày tạo            | Tổng tiền (VNĐ) |");
            System.out.println("------------------------------------------------------------------------------------------------------------");
            for (Order o : list) {
                Customer c = customerService.getById(o.getCustomerId());
                String customerName = (c != null) ? c.getName() : "Khách lẻ (ID:" + o.getCustomerId() + ")";
                System.out.printf("| %-4d | %-20s | %-19s | %15.2f |\n", 
                        o.getId(), customerName, o.getCreatedAt(), o.getTotalAmount());
            }
            System.out.println("------------------------------------------------------------------------------------------------------------");
        }
    }


    // Chức năng thêm đơn hàng mới
    private static void addNewOrder() {
        System.out.println("\n--- TẠO ĐƠN HÀNG MỚI ---");

        // 1. Chọn khách hàng
        Customer customer = null;
        while (true) {
            System.out.print("Nhập ID khách hàng (nhập 0 để HỦY): ");
            int customerId = InputMethods.getInt();
            if (customerId == 0) {
                System.out.println("Đã hủy tạo đơn hàng.");
                return;
            }
            customer = customerService.getById(customerId);
            if (customer != null) break;
            System.err.println("Mã khách hàng không tồn tại. Vui lòng nhập lại!");
        }

        System.out.println("Khách hàng: " + customer.getName());

        // 2. Thêm sản phẩm vào đơn hàng
        List<OrderDetail> details = new ArrayList<>();
        double totalAmount = 0;

        while (true) {
            System.out.println("\n--- Thêm sản phẩm vào giỏ hàng ---");
            Product product = null;
            while (true) {
                System.out.print("Nhập ID sản phẩm (nhập 0 để HỦY): ");
                int productId = InputMethods.getInt();
                if (productId == 0) {
                    System.out.println("Đã hủy thao tác chọn sản phẩm.");
                    // Nếu đã có hàng trong giỏ, có thể hỏi muốn thanh toán không, 
                    // nhưng theo yêu cầu "thoát khỏi chức năng" nên ta thoát luôn.
                    return;
                }
                product = productService.getById(productId);
                if (product != null) break;
                System.err.println("Mã sản phẩm không tồn tại. Vui lòng nhập lại!");
            }

            System.out.println("Sản phẩm: " + product.getName() + " | Giá: " + product.getPrice() + " | Tồn kho: " + product.getStock());

            if (product.getStock() <= 0) {
                System.err.println("Sản phẩm này đã hết hàng!");
            } else {
                System.out.print("Nhập số lượng mua: ");
                int quantity = InputMethods.getPositiveInt();

                if (quantity > product.getStock()) {
                    System.err.println("Số lượng trong kho không đủ (chỉ còn " + product.getStock() + ").");
                } else {
                    // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
                    OrderDetail existingDetail = null;
                    for (OrderDetail d : details) {
                        if (d.getProductId() == product.getId()) {
                            existingDetail = d;
                            break;
                        }
                    }

                    if (existingDetail != null) {
                        if (existingDetail.getQuantity() + quantity > product.getStock()) {
                            System.err.println("Tổng số lượng trong giỏ hàng vượt quá tồn kho!");
                        } else {
                            existingDetail.setQuantity(existingDetail.getQuantity() + quantity);
                            System.out.println("Đã cập nhật số lượng cho sản phẩm " + product.getName());
                        }
                    } else {
                        OrderDetail detail = new OrderDetail(0, 0, product.getId(), quantity, product.getPrice());
                        details.add(detail);
                        System.out.println("Đã thêm " + product.getName() + " vào giỏ hàng.");
                    }
                }
            }

            System.out.print("Tiếp tục chọn sản phẩm? (y/n): ");
            if (!InputMethods.getBoolean()) break;
        }

        if (details.isEmpty()) {
            System.out.println("Hủy tạo đơn hàng vì không có sản phẩm nào.");
            return;
        }

        // Tính tổng tiền
        for (OrderDetail d : details) {
            totalAmount += d.getQuantity() * d.getUnitPrice();
        }

        // 3. Hiển thị tóm tắt và xác nhận
        System.out.println("\n--- TÓM TẮT ĐƠN HÀNG ---");
        System.out.println("Khách hàng: " + customer.getName());
        System.out.println("Danh sách sản phẩm:");
        for (OrderDetail d : details) {
            Product p = productService.getById(d.getProductId());
            System.out.printf("- %-20s x %-3d | Đơn giá: %12.2f | Thành tiền: %12.2f\n",
                    p.getName(), d.getQuantity(), d.getUnitPrice(), d.getQuantity() * d.getUnitPrice());
        }
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("TỔNG TIỀN THANH TOÁN: %15.2f VNĐ\n", totalAmount);

        System.out.print("Xác nhận lưu hóa đơn này vào CSDL? (y/n): ");
        if (InputMethods.getBoolean()) {
            Order newOrder = new Order(0, customer.getId(), null, totalAmount);
            if (orderService.addWithDetails(newOrder, details)) {
                System.out.println("LƯU HÓA ĐƠN THÀNH CÔNG!");
            } else {
                System.err.println("LƯU HÓA ĐƠN THẤT BẠI. Vui lòng kiểm tra lại kết nối hoặc dữ liệu.");
            }
        } else {
            System.out.println("Đã hủy lưu hóa đơn.");
        }
    }

    // Chức năng tìm kiếm 
    private static void searchMenu() {
        while (true) {
            System.out.println("\n========= MENU TÌM KIẾM HÓA ĐƠN =========");
            System.out.println("1. Tìm theo tên khách hàng");
            System.out.println("2. Tìm theo ngày/tháng/năm (theo ngày lập hóa đơn)");
            System.out.println("0. Quay lại menu hóa đơn");
            System.out.print("Lựa chọn: ");
            int choice = InputMethods.getInt();
            switch (choice) {
                case 1:
                    searchByCustomerName();
                    break;
                case 2:
                    searchByDate();
                    break;
                case 0:
                    return;
                default:
                    System.err.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    // Chức năng tìm kiếm theo tên khách hàng
    private static void searchByCustomerName() {
        System.out.print("Nhập tên khách hàng cần tìm (nhập 0 để HỦY): ");
        String name = InputMethods.getString();
        if (name.startsWith("0")) return;
        List<Order> list = orderService.searchByCustomerName(name);
        displayOrderTable(list, "KẾT QUẢ TÌM KIẾM THEO TÊN: " + name.toUpperCase());
    }

    // Chức năng tìm kiếm theo ngày
    private static void searchByDate() {
        System.out.print("Nhập ngày cần tìm (định dạng YYYY-MM-DD, nhập 0 để HỦY): ");
        String date = InputMethods.getString();
        if (date.startsWith("0")) return;
        // Có thể thêm bước validate định dạng ngày ở đây nếu cần
        List<Order> list = orderService.searchByDate(date);
        displayOrderTable(list, "KẾT QUẢ TÌM KIẾM THEO NGÀY: " + date);
    }

    // Chức năng thống kê doanh thu
    private static void statisticMenu() {
        while (true) {
            System.out.println("\n========= THỐNG KÊ DOANH THU =========");
            System.out.println("1. Doanh thu theo ngày");
            System.out.println("2. Doanh thu theo tháng");
            System.out.println("3. Doanh thu theo năm");
            System.out.println("0. Quay lại menu chính");
            System.out.print("Lựa chọn: ");
            int choice = InputMethods.getInt();
            switch (choice) {
                case 1:
                    showRevenueStats("day", "DOANH THU THEO NGÀY");
                    break;
                case 2:
                    showRevenueStats("month", "DOANH THU THEO THÁNG");
                    break;
                case 3:
                    showRevenueStats("year", "DOANH THU THEO NĂM");
                    break;
                case 0:
                    return;
                default:
                    System.err.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    // Chức năng hiển thị doanh thu theo ngày, tháng, năm
    private static void showRevenueStats(String type, String title) {
        List<String[]> stats = orderService.getRevenueStats(type);
        if (stats.isEmpty()) {
            System.out.println("Chưa có dữ liệu kinh doanh.");
        } else {
            System.out.println("\n--- " + title + " ---");
            System.out.println("---------------------------------------");
            System.out.println("| Thời gian    | Tổng doanh thu (VNĐ) |");
            System.out.println("---------------------------------------");
            for (String[] row : stats) {
                double total = Double.parseDouble(row[1]);
                System.out.printf("| %-12s | %20.2f |\n", row[0], total);
            }
            System.out.println("---------------------------------------");
        }
    }

}
