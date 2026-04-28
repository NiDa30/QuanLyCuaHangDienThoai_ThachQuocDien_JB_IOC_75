package ra.phonestore.model;

public class Product {
    private int id;
    private String name;
    private int brandId; // Liên kết tới Brand id
    private double price;
    private int stock;
    private String description;
    private boolean isDeleted;
    private String brandName; // Tên nhãn hàng (để hiển thị)



    public Product() {}

    public Product(int id, String name, int brandId, double price, int stock, String description) {
        this.id = id;
        this.name = name;
        this.brandId = brandId;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    public Product(int id, String name, int brandId, String brandName, double price, int stock, String description) {
        this.id = id;
        this.name = name;
        this.brandId = brandId;
        this.brandName = brandName;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getBrandId() { return brandId; }
    public void setBrandId(int brandId) { this.brandId = brandId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }



    @Override
    public String toString() {
        String displayBrand = (brandName != null) ? brandName : "ID: " + brandId;
        return String.format("| %-4d | %-25s | %-14s | %12.2f | %7d |", 
                id, name, displayBrand, price, stock);
    }

}
