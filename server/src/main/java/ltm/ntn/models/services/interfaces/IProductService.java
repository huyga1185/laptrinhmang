package ltm.ntn.models.services.interfaces;

import ltm.ntn.models.pojo.Product;

import java.util.List;

public interface IProductService {
    Product saveProduct(Product product);
    int deleteProduct(String id);
    Product findProductById(String id);
    Product findProductBySku(String sku);
    List<Product> findAllProducts();
    int getTotalSoldProducts();
    int countProducts();
    int countActiveProducts();
    int countLowStockProducts();
    List<Product> findAllActiveProducts();
}
