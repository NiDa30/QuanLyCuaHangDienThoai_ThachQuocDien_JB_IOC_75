package ra.phonestore.dao;

import ra.phonestore.model.User;

public interface IUserDAO {
    User login(String username, String password);
}
