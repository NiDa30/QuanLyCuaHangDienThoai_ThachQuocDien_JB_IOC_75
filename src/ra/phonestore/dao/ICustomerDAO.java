package ra.phonestore.dao;

import ra.phonestore.model.Customer;
import java.util.List;


public interface ICustomerDAO extends IGenericDAO<Customer, Integer> {
    Customer getByPhone(String phone);
    List<Customer> getDeletedList();
    boolean restore(Integer id);
    boolean permanentDelete(Integer id);
}
