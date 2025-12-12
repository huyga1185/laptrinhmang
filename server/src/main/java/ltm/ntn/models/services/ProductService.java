package ltm.ntn.models.services;

import lombok.extern.slf4j.Slf4j;
import ltm.ntn.models.dao.ProductDAO;
import ltm.ntn.models.dao.interfaces.IProductDAO;
import ltm.ntn.models.pojo.Product;
import ltm.ntn.models.services.interfaces.IProductService;
import ltm.ntn.share.DBConnection;

import java.sql.Connection;

@Slf4j
public class ProductService implements IProductService {
    private final IProductDAO productDAO;

    public ProductService() {
        productDAO = new ProductDAO();
    }

    @Override
    public Product createProduct(Product product) {
        try (Connection connection = DBConnection.getConnection()) {
            product = productDAO.save(connection, product);
            return product;
        } catch (Exception e) {
            log.error("Could not create product: ", e);
            throw new RuntimeException("Could not create product.");
        }
    }

    @Override
    public Product findProductById(String id) {
        try {
            return productDAO.getProductById(id);
        } catch (Exception e) {
            log.error("Could not find product: ", e);
            throw new RuntimeException("Could not find product.");
        }
    }

    @Override
    public Product findProductBySku(String sku) {
        return null;
    }

    @Override
    public int getTotalSoldProducts() {
        return 0;
    }
    @Override
    public boolean deleteProduct(String id) {
        try {
            return productDAO.deleteById(id);
        } catch (Exception e) {
            log.error("Could not delete product: ", e);
            throw new RuntimeException("Could not delete product.");
        }
    }


    public void saveProduct(Product currentProduct) {
    }
}
