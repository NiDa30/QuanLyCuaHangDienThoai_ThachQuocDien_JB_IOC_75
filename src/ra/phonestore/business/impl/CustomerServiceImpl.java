package ra.phonestore.business.impl;

import ra.phonestore.business.ICustomerService;
import ra.phonestore.dao.ICustomerDAO;
import ra.phonestore.dao.impl.CustomerDAOImpl;
import ra.phonestore.model.Customer;

import java.util.List;

public class CustomerServiceImpl implements ICustomerService {
    private final ICustomerDAO customerDAO = new CustomerDAOImpl();

    @Override
    public List<Customer> getAll() {
        return customerDAO.getAll();
    }

    @Override
    public Customer getById(Integer id) {
        return customerDAO.getById(id);
    }

    @Override
    public boolean add(Customer customer) {
        return customerDAO.add(customer);
    }

    @Override
    public boolean update(Customer customer) {
        return customerDAO.update(customer);
    }

    @Override
    public boolean delete(Integer id) {
        return customerDAO.delete(id);
    }

    @Override
    public Customer getByPhone(String phone) {
        return customerDAO.getByPhone(phone);
    }

    @Override
    public List<Customer> getDeletedList() {
        return customerDAO.getDeletedList();
    }

    @Override
    public boolean restore(Integer id) {
        return customerDAO.restore(id);
    }

    @Override
    public boolean permanentDelete(Integer id) {
        return customerDAO.permanentDelete(id);
    }
}
