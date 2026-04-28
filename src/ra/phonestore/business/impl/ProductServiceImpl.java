package ra.phonestore.business.impl;

import ra.phonestore.business.IProductService;
import ra.phonestore.dao.IProductDAO;
import ra.phonestore.dao.impl.ProductDAOImpl;
import ra.phonestore.model.Product;

import java.util.List;

public class ProductServiceImpl implements IProductService {
    private final IProductDAO productDAO = new ProductDAOImpl();

    @Override
    public List<Product> getAll() {
        return productDAO.getAll();
    }

    @Override
    public Product getById(Integer id) {
        return productDAO.getById(id);
    }

    @Override
    public boolean add(Product product) {
        return productDAO.add(product);
    }

    @Override
    public boolean update(Product product) {
        return productDAO.update(product);
    }

    @Override
    public boolean delete(Integer id) {
        return productDAO.delete(id);
    }

    @Override
    public List<Product> searchByBrand(String brand) {
        return productDAO.searchByBrand(brand);
    }

    @Override
    public List<Product> searchByPriceRange(double min, double max) {
        return productDAO.searchByPriceRange(min, max);
    }

    @Override
    public List<Product> searchByStock(int minStock) {
        return productDAO.searchByStock(minStock);
    }

    @Override
    public List<Product> searchByName(String name) {
        return productDAO.searchByName(name);
    }

    @Override
    public List<Product> getDeletedList() {
        return productDAO.getDeletedList();
    }

    @Override
    public boolean restore(Integer id) {
        return productDAO.restore(id);
    }

    @Override
    public boolean permanentDelete(Integer id) {
        return productDAO.permanentDelete(id);
    }

    @Override
    public List<Double> getPricePercentiles() {
        return productDAO.getPricePercentiles();
    }
}

