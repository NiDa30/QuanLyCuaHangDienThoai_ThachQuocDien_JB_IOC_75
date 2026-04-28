package ra.phonestore.model;

import java.time.LocalDateTime;

public class Order {
    private int id;
    private int customerId;
    private LocalDateTime createdAt;
    private double totalAmount;

    public Order() {}

    public Order(int id, int customerId, LocalDateTime createdAt, double totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.createdAt = createdAt;
        this.totalAmount = totalAmount;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    @Override
    public String toString() {
        return String.format("| %-4d | Khách hàng: %-4d | Ngày tạo: %-19s | Tổng tiền: %12.2f |", 
                id, customerId, createdAt, totalAmount);
    }
}
