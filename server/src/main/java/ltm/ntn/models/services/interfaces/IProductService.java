package ltm.ntn.models.services.interfaces;

import ltm.ntn.models.pojo.Product;

public interface IProductService {
    Product createProduct(Product product);
    Product findProductById(String id);
    Product findProductBySku(String sku);
    int getTotalSoldProducts();
}
