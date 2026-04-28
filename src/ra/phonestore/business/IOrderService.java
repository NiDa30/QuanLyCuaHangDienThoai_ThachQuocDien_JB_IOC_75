package ra.phonestore.business;

import ra.phonestore.model.Order;
import ra.phonestore.model.OrderDetail;
import java.util.List;

public interface IOrderService extends IGenericService<Order, Integer> {
    boolean addWithDetails(Order order, List<OrderDetail> details);
    List<Order> searchByCustomerName(String name);
    List<Order> searchByDate(String date);
    double getRevenueByPeriod(String start, String end);
    List<String[]> getRevenueStats(String type);
}
