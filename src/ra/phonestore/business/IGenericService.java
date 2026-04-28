package ra.phonestore.business;

import java.util.List;

public interface IGenericService<T, ID> {
    List<T> getAll();
    T getById(ID id);
    boolean add(T t);
    boolean update(T t);
    boolean delete(ID id);
}
