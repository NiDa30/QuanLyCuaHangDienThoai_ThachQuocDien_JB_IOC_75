package ra.phonestore.business.impl;

import ra.phonestore.business.IBrandService;
import ra.phonestore.dao.IBrandDAO;
import ra.phonestore.dao.impl.BrandDAOImpl;
import ra.phonestore.model.Brand;

import java.util.List;

public class BrandServiceImpl implements IBrandService {
    private final IBrandDAO brandDAO = new BrandDAOImpl();

    @Override
    public List<Brand> getAll() {
        return brandDAO.getAll();
    }

    @Override
    public Brand getById(Integer id) {
        return brandDAO.getById(id);
    }

    @Override
    public boolean add(Brand brand) {
        return brandDAO.add(brand);
    }

    @Override
    public boolean update(Brand brand) {
        return brandDAO.update(brand);
    }

    @Override
    public boolean delete(Integer id) {
        return brandDAO.delete(id);
    }
}
