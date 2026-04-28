package ra.phonestore.business;

import ra.phonestore.model.Product;
import java.util.List;

public interface IProductService extends IGenericService<Product, Integer> {
    List<Product> searchByBrand(String brand);
    List<Product> searchByPriceRange(double min, double max);
    List<Product> searchByStock(int minStock);
    List<Product> searchByName(String name);
    List<Product> getDeletedList();
    boolean restore(Integer id);
    boolean permanentDelete(Integer id);
    List<Double> getPricePercentiles();
}
