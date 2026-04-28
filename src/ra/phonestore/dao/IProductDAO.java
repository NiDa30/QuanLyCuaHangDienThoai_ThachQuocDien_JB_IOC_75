package ra.phonestore.dao;

import java.util.List;

import ra.phonestore.model.Product;

public interface IProductDAO extends IGenericDAO<Product, Integer> {
    List<Product> searchByBrand(String brand);
    List<Product> searchByPriceRange(double min, double max);
    List<Product> searchByStock(int minStock);
    List<Product> searchByName(String name);
    List<Product> getDeletedList();
    boolean restore(Integer id);
    boolean permanentDelete(Integer id);
    List<Double> getPricePercentiles();
}
