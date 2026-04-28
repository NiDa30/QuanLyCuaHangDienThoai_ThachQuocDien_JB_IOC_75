package ra.phonestore.business;

import ra.phonestore.model.Customer;
import java.util.List;


public interface ICustomerService extends IGenericService<Customer, Integer> {
    Customer getByPhone(String phone);
    List<Customer> getDeletedList();
    boolean restore(Integer id);
    boolean permanentDelete(Integer id);
}
