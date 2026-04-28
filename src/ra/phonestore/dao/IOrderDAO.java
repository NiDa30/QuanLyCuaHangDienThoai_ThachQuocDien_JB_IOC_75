package ra.phonestore.dao;

import java.util.List;

import ra.phonestore.model.Order;
import ra.phonestore.model.OrderDetail;

public interface IOrderDAO extends IGenericDAO<Order, Integer> {
    boolean addDetails(List<OrderDetail> details);
    List<Order> searchByCustomerName(String name);
    List<Order> searchByDate(String date); // format YYYY-MM-DD
    double getRevenueByPeriod(String start, String end);
    List<String[]> getRevenueStats(String type); // type: day, month, year
}
