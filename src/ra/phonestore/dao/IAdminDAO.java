package ra.phonestore.dao;

import ra.phonestore.model.Admin;

public interface IAdminDAO {
    Admin login(String username, String password);
}
