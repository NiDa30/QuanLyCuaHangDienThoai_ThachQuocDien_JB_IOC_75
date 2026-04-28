package ra.phonestore.presentation;

import ra.phonestore.business.IBrandService;
import ra.phonestore.business.IProductService;
import ra.phonestore.business.impl.BrandServiceImpl;
import ra.phonestore.business.impl.ProductServiceImpl;
import ra.phonestore.model.Brand;
import ra.phonestore.model.Product;
import ra.phonestore.utils.InputMethods;

import java.util.List;

public class ProductPresentation {
    private static final IProductService productService = new ProductServiceImpl();
    private static final IBrandService brandService = new BrandServiceImpl();

    public static void displayMenu() {
        while (true) {
            System.out.println("\n========== QUẢN LÝ ĐIỆN THOẠI ==========");
            System.out.println("1. Hiển thị danh sách");
            System.out.println("2. Thêm sản phẩm mới");
            System.out.println("3. Cập nhật sản phẩm (không cập nhật ID)");
            System.out.println("4. Xóa sản phẩm (theo ID)");
            System.out.println("5. Thùng rác (Restore / Xóa vĩnh viễn)");
            System.out.println("6. Tìm kiếm theo Brand. Tìm kiếm gần đúng");
            System.out.println("7. Tìm kiếm theo khoảng giá(Price range)");
            System.out.println("8. Tìm kiếm điện thoại theo tên sản phẩm kèm Stock availability (Tồn kho)");
            System.out.println("0. Quay lại Menu chính");
            System.out.print("Lựa chọn: ");
            int choice = InputMethods.getInt();
            switch (choice) {
                case 1:
                    showList();
                    break;
                case 2:
                    addNewProduct();
                    break;
                case 3:
                    updateProduct();
                    break;
                case 4:
                    deleteProduct();
                    break;
                case 5:
                    restoreProduct();
                    break;
                case 6:
                    searchByBrand();
                    break;
                case 7:
                    searchByPriceRange();
                    break;
                case 8:
                    searchByNameAndStock();
                    break;
                case 0:
                    return;

                default:
                    System.err.println("Lựa chọn không hợp lệ!");
            }
        }
    }
// Chức năng in danh sách sản phẩm
    private static void showList() {
        List<Product> list = productService.getAll();
        if (list.isEmpty()) {
            System.out.println("Danh sách sản phẩm trống.");
        } else {
            System.out.println("\n---------------------------------- DANH SÁCH SẢN PHẨM ----------------------------------");
            System.out.println("| ID   | Tên sản phẩm              | Thương hiệu    | Giá bán      | Tồn kho |");
            System.out.println("----------------------------------------------------------------------------------------");
            for (Product p : list) {
                System.out.println(p);
            }
            System.out.println("----------------------------------------------------------------------------------------");
        }
    }

    // Chức năng thêm sản phẩm mới
    private static void addNewProduct() {
        System.out.println("\n--- THÊM SẢN PHẨM MỚI ---");
        
        List<Brand> brands = brandService.getAll();
        if (brands.isEmpty()) {
            System.err.println("Chưa có nhãn hàng nào trong hệ thống. Vui lòng thêm nhãn hàng trước!");
            return;
        }

        System.out.print("Nhập tên điện thoại (nhập 0 để HỦY): ");
        String name = InputMethods.getString();
        if (name.startsWith("0")) return;

        int brandId = -1;
        while (true) {
            System.out.println("Danh sách nhãn hàng hiện có:");
            for (Brand b : brands) {
                System.out.println(b.getId() + ". " + b.getName());
            }
            System.out.print("Chọn mã nhãn hàng (ID) hoặc nhập TÊN nhãn hàng mới (nhập 0 để HỦY): ");
            String input = InputMethods.getString();
            if (input.startsWith("0")) return;
            
            try {
                // Thử xem người dùng có nhập ID không
                int id = Integer.parseInt(input);
                if (brands.stream().anyMatch(b -> b.getId() == id)) {
                    brandId = id;
                    break;
                } else {
                    System.err.println("Mã nhãn hàng không tồn tại. Vui lòng chọn lại hoặc nhập tên nhãn mới!");
                }
            } catch (NumberFormatException e) {
                // Nếu không phải số, coi đó là tên nhãn hàng mới
                String newBrandName = input.trim();
                if (newBrandName.isEmpty()) {
                    System.err.println("Tên nhãn hàng không được để trống!");
                    continue;
                }
                
                // Kiểm tra xem tên nhãn này đã tồn tại chưa (không phân biệt hoa thường)
                Brand existingBrand = brands.stream()
                        .filter(b -> b.getName().equalsIgnoreCase(newBrandName))
                        .findFirst()
                        .orElse(null);
                
                if (existingBrand != null) {
                    brandId = existingBrand.getId();
                    System.out.println("Sử dụng nhãn hàng hiện có: " + existingBrand.getName() + " (ID: " + brandId + ")");
                    break;
                } else {
                    // Thêm mới nhãn hàng
                    Brand newBrand = new Brand(0, newBrandName, "Tự động thêm khi tạo sản phẩm");
                    if (brandService.add(newBrand)) {
                        // Lấy lại danh sách để có ID mới nhất hoặc giả định brandService.add cập nhật ID (thường DAO làm việc này)
                        // Cách chắc chắn nhất là fetch lại list hoặc trả về object từ add. 
                        // Ở đây ta getAll lại cho an tâm
                        List<Brand> updatedBrands = brandService.getAll();
                        brandId = updatedBrands.stream()
                                .filter(b -> b.getName().equalsIgnoreCase(newBrandName))
                                .findFirst()
                                .get().getId();
                        System.out.println("Đã tự động thêm nhãn hàng mới: " + newBrandName + " (ID: " + brandId + ")");
                        break;
                    } else {
                        System.err.println("Lỗi khi thêm nhãn hàng mới. Vui lòng thử lại!");
                    }
                }
            }
        }

        System.out.print("Nhập giá bán: ");
        double price = InputMethods.getPositiveDouble();

        System.out.print("Nhập số lượng tồn kho: ");
        int stock;
        while (true) {
            stock = InputMethods.getInt();
            if (stock >= 0) break;
            System.err.println("Số lượng tồn kho phải >= 0. Vui lòng nhập lại!");
        }

        System.out.print("Nhập mô tả sản phẩm (nhập 0 để HỦY): ");
        String description = InputMethods.getString();
        if (description.startsWith("0")) return;

        Product newProduct = new Product(0, name, brandId, price, stock, description);
        if (productService.add(newProduct)) {
            System.out.println("Thêm sản phẩm mới thành công!");
        } else {
            System.err.println("Thêm mới thất bại. Vui lòng kiểm tra lại kết nối.");
        }
    }

    // Chức năng cập nhật sản phẩm 
    private static void updateProduct() {
        System.out.println("\n--- CẬP NHẬT SẢN PHẨM ---");
        Product productToUpdate = null;
        int idToUpdate = -1;

        while (true) {
            System.out.print("Nhập ID sản phẩm cần cập nhật (nhập 0 để HỦY): ");
            idToUpdate = InputMethods.getInt();
            if (idToUpdate == 0) return;
            productToUpdate = productService.getById(idToUpdate);
            if (productToUpdate != null) {
                break;
            }
            System.err.println("Mã sản phẩm không tồn tại, vui lòng nhập lại!");
        }

        // Hiển thị thông tin hiện tại
        System.out.println("Thông tin hiện tại của sản phẩm:");
        System.out.println("Tên: " + productToUpdate.getName());
        System.out.println("Mã Brand: " + productToUpdate.getBrandId());
        System.out.println("Giá: " + productToUpdate.getPrice());
        System.out.println("Tồn kho: " + productToUpdate.getStock());
        System.out.println("Mô tả: " + productToUpdate.getDescription());

        // Nhập thông tin mới
        System.out.print("Nhập tên mới (để trống nếu không đổi): ");
        String name = InputMethods.getOptionalString();
        if (!name.isEmpty()) {
            productToUpdate.setName(name);
        }

        // Chọn nhãn hàng mới
        List<Brand> brands = brandService.getAll();
        if (!brands.isEmpty()) {
            while (true) {
                System.out.println("Danh sách nhãn hàng hiện có:");
                for (Brand b : brands) {
                    System.out.println(b.getId() + ". " + b.getName());
                }
                System.out.print("Chọn mã nhãn hàng mới (nhập 0 để không đổi): ");
                String input = InputMethods.getOptionalString();
                if (input.isEmpty() || input.equals("0")) break;
                
                try {
                    int brandId = Integer.parseInt(input);
                    if (brands.stream().anyMatch(b -> b.getId() == brandId)) {
                        productToUpdate.setBrandId(brandId);
                        break;
                    }
                    System.err.println("Mã nhãn hàng không tồn tại. Vui lòng chọn lại!");
                } catch (NumberFormatException e) {
                    System.err.println("Mã nhãn hàng phải là số!");
                }
            }
        }

        while (true) {
            System.out.print("Nhập giá mới (để trống nếu không đổi): ");
            String input = InputMethods.getOptionalString();
            if (input.isEmpty()) break;
            try {
                double price = Double.parseDouble(input);
                if (price > 0) {
                    productToUpdate.setPrice(price);
                    break;
                }
                System.err.println("Giá bán phải lớn hơn 0!");
            } catch (NumberFormatException e) {
                System.err.println("Giá bán phải là số!");
            }
        }

        while (true) {
            System.out.print("Nhập số lượng tồn kho mới (để trống nếu không đổi): ");
            String input = InputMethods.getOptionalString();
            if (input.isEmpty()) break;
            try {
                int stock = Integer.parseInt(input);
                if (stock >= 0) {
                    productToUpdate.setStock(stock);
                    break;
                }
                System.err.println("Số lượng tồn kho phải >= 0!");
            } catch (NumberFormatException e) {
                System.err.println("Số lượng tồn kho phải là số nguyên!");
            }
        }

        System.out.print("Nhập mô tả mới (để trống nếu không đổi): ");
        String description = InputMethods.getOptionalString();
        if (!description.isEmpty()) {
            productToUpdate.setDescription(description);
        }

        if (productService.update(productToUpdate)) {
            System.out.println("Cập nhật sản phẩm thành công!");
        } else {
            System.err.println("Cập nhật thất bại. Vui lòng kiểm tra lại kết nối.");
        }
    }

    // Chức năng xóa sản phẩm
    private static void deleteProduct() {
        System.out.println("\n--- XÓA SẢN PHẨM ---");
        System.out.print("Nhập ID sản phẩm muốn xóa (nhập 0 để HỦY): ");
        int idToDelete = InputMethods.getInt();
        if (idToDelete == 0) return;

        Product product = productService.getById(idToDelete);
        if (product == null) {
            System.err.println("Mã sản phẩm không tồn tại!");
            return;
        }

        if (productService.delete(idToDelete)) {
            System.out.println("Sản phẩm đã được chuyển vào thùng rác.");
        } else {
            System.err.println("Xóa thất bại. Sản phẩm có thể đang có trong đơn hàng hoặc lỗi kết nối.");
        }
    }

    // Chức năng thùng rác
    private static void trashMenu() {
        while (true) {
            List<Product> deletedList = productService.getDeletedList();
            System.out.println("\n========== THÙNG RÁC ==========");
            if (deletedList.isEmpty()) {
                System.out.println("Thùng rác trống.");
            } else {
                System.out.println("\n---------------------------------- DANH SÁCH ĐÃ XÓA ----------------------------------");
                System.out.println("| ID   | Tên sản phẩm              | Thương hiệu    | Giá bán      | Tồn kho |");
                System.out.println("--------------------------------------------------------------------------------------");
                for (Product p : deletedList) {
                    System.out.println(p);
                }
                System.out.println("--------------------------------------------------------------------------------------");
            }
            System.out.println("1. Khôi phục sản phẩm");
            System.out.println("2. Xóa vĩnh viễn");
            System.out.println("3. Quay lại");
            System.out.print("Lựa chọn: ");
            int choice = InputMethods.getInt();
            switch (choice) {
                case 1:
                    restoreProduct();
                    break;
                case 2:
                    permanentDeleteProduct();
                    break;
                case 3:
                    return;
                default:
                    System.err.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    // Chức năng khôi phục sản phẩm
    private static void restoreProduct() {
        System.out.print("Nhập ID sản phẩm cần khôi phục (nhập 0 để HỦY): ");
        int id = InputMethods.getInt();
        if (id == 0) return;
        if (productService.restore(id)) {
            System.out.println("Khôi phục sản phẩm thành công!");
        } else {
            System.err.println("ID không tồn tại trong thùng rác hoặc lỗi kết nối.");
        }
    }

    // Chức năng xóa vĩnh viễn sản phẩm
    private static void permanentDeleteProduct() {
        System.out.print("Nhập ID sản phẩm cần xóa vĩnh viễn (nhập 0 để HỦY): ");
        int id = InputMethods.getInt();
        if (id == 0) return;
        
        // Kiểm tra xem ID có trong thùng rác không
        List<Product> deletedList = productService.getDeletedList();
        boolean exists = deletedList.stream().anyMatch(p -> p.getId() == id);
        
        if (!exists) {
            System.err.println("Mã sản phẩm không tồn tại trong thùng rác!");
            return;
        }

        System.out.print("Bạn có chắc chắn muốn xóa VĨNH VIỄN sản phẩm này không? (y/n): ");
        if (InputMethods.getBoolean()) {
            if (productService.permanentDelete(id)) {
                System.out.println("Đã xóa vĩnh viễn sản phẩm.");
            } else {
                System.err.println("Xóa thất bại. Sản phẩm có thể đang được liên kết với dữ liệu khác.");
            }
        } else {
            System.out.println("Đã hủy thao tác.");
        }
    }

    // Chức năng tìm kiếm sản phẩm theo nhãn hàng
    private static void searchByBrand() {
        System.out.println("\n--- TÌM KIẾM THEO NHÃN HÀNG ---");
        System.out.print("Nhập tên nhãn hàng để tìm (nhập 0 để HỦY): ");
        String keyword = InputMethods.getString();
        if (keyword.startsWith("0")) return;

        List<Product> list = productService.searchByBrand(keyword);
        if (list.isEmpty()) {
            System.out.println("Không tìm thấy sản phẩm nào phù hợp.");
        } else {
            System.out.println("\n---------------------------------- KẾT QUẢ TÌM KIẾM ----------------------------------");
            System.out.println("| ID   | Tên sản phẩm              | Thương hiệu    | Giá bán      | Tồn kho |");
            System.out.println("--------------------------------------------------------------------------------------");
            for (Product p : list) {
                System.out.println(p);
            }
            System.out.println("--------------------------------------------------------------------------------------");
        }
    }

    // Chức năng tìm kiếm sản phẩm theo khoảng giá
    private static void searchByPriceRange() {
        System.out.println("\n--- TÌM KIẾM THEO KHOẢNG GIÁ ---");

        // 1. Lấy gợi ý khoảng giá dựa trên phân bố thực tế (Percentile)
        List<Double> percentiles = productService.getPricePercentiles();

        double min = 0, max = 0;
        boolean proceed = false;

        if (percentiles.size() >= 6) {
            System.out.println("Gợi ý các khoảng giá dựa trên dữ liệu thực tế:");
            for (int i = 0; i < 5; i++) {
                System.out.printf("%d. Từ %,.0f đến %,.0f VNĐ\n", (i + 1), percentiles.get(i), percentiles.get(i + 1));
            }
            System.out.println("6. Tự nhập khoảng giá tùy chỉnh");
            System.out.print("Lựa chọn của bạn (hoặc nhấn 0 để quay lại): ");
            int subChoice = InputMethods.getInt();

            if (subChoice >= 1 && subChoice <= 5) {
                min = percentiles.get(subChoice - 1);
                max = percentiles.get(subChoice);
                proceed = true;
            } else if (subChoice == 6) {
                System.out.print("Nhập giá bắt đầu: ");
                min = InputMethods.getDouble();
                System.out.print("Nhập giá kết thúc: ");
                max = InputMethods.getDouble();
                proceed = true;
            } else if (subChoice == 0) {
                return;
            } else {
                System.err.println("Lựa chọn không hợp lệ!");
            }
        } else {
            // Trường hợp ít dữ liệu, cho phép nhập thủ công luôn
            System.out.println("(Dữ liệu chưa đủ để tạo gợi ý tự động)");
            System.out.print("Nhập giá bắt đầu: ");
            min = InputMethods.getDouble();
            System.out.print("Nhập giá kết thúc: ");
            max = InputMethods.getDouble();
            proceed = true;
        }

        if (proceed) {
            if (min > max) {
                System.err.println("Giá bắt đầu phải nhỏ hơn giá kết thúc!");
                return;
            }

            List<Product> list = productService.searchByPriceRange(min, max);
            if (list.isEmpty()) {
                System.out.println("Không tìm thấy sản phẩm nào trong khoảng giá này.");
            } else {
                System.out.println("\n------------------------------------ KẾT QUẢ TÌM KIẾM ------------------------------------");
                System.out.println("| ID   | Tên sản phẩm              | Thương hiệu    | Giá bán      | Tồn kho |");
                System.out.println("------------------------------------------------------------------------------------------");
                for (Product p : list) {
                    System.out.println(p);
                }
                System.out.println("------------------------------------------------------------------------------------------");
            }
        }
    }

    // Chức năng tìm kiếm sản phẩm theo tên và số lượng tồn kho
    private static void searchByNameAndStock() {
        System.out.println("\n--- TÌM KIẾM THEO TÊN (KÈM TỒN KHO) ---");
        System.out.print("Nhập tên sản phẩm để tìm (nhập 0 để HỦY): ");
        String keyword = InputMethods.getString();
        if (keyword.startsWith("0")) return;

        List<Product> list = productService.searchByName(keyword);
        if (list.isEmpty()) {
            System.out.println("Không tìm thấy sản phẩm nào phù hợp.");
        } else {
            System.out.println("\n------------------------------------ KẾT QUẢ TÌM KIẾM ------------------------------------");
            System.out.println("| ID   | Tên sản phẩm              | Thương hiệu    | Giá bán      | Tồn kho | Trạng thái |");
            System.out.println("------------------------------------------------------------------------------------------");
            for (Product p : list) {
                String availability = p.getStock() > 0 ? "Còn hàng" : "Hết hàng";
                System.out.printf("%-80s | %-10s |\n", p.toString(), availability);
            }
            System.out.println("------------------------------------------------------------------------------------------");
        }
    }
}
