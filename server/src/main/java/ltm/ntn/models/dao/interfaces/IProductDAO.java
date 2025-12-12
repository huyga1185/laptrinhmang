package ltm.ntn.models.dao.interfaces;

import ltm.ntn.models.pojo.Product;

import java.sql.Connection;
import java.util.List;

public interface IProductDAO {
    Product save(Connection connection, Product product) throws Exception;
    Product getProductById(String id) throws Exception;
    Product getProductBySKU(String sku) throws Exception;
    void delete(String id) throws Exception;
    List<Product> getAllProducts() throws Exception;
    List<Product> getAllActiveProducts() throws Exception;
    List<Product> getAllInactiveProducts() throws Exception;
    boolean deleteById(String id) throws Exception;

}
