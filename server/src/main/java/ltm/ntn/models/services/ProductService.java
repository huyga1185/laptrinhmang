package ltm.ntn.models.services;

import lombok.extern.slf4j.Slf4j;
import ltm.ntn.models.dao.ProductDAO;
import ltm.ntn.models.dao.interfaces.IProductDAO;
import ltm.ntn.models.pojo.Product;
import ltm.ntn.models.services.interfaces.IInvoiceItemService;
import ltm.ntn.models.services.interfaces.IProductService;
import ltm.ntn.share.DBConnection;

import java.sql.Connection;
import java.util.List;

@Slf4j
public class ProductService implements IProductService {
    private final IProductDAO productDAO;
    private final IInvoiceItemService itemService;

    public ProductService() {
        this.productDAO = new ProductDAO();
        this.itemService = new InvoiceItemService();
    }

    @Override
    public Product saveProduct(Product product) {
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
    public List<Product> findAllProducts() {
        try {
            return productDAO.getAllProducts();
        } catch (Exception e) {
            log.error("Could not find all products: ", e);
            throw new RuntimeException("Could not find all products.");
        }
    }

    @Override
    public int getTotalSoldProducts() {
        List<Product> products = findAllProducts();
        int totalSoldProducts = 0;
        for (Product product : products)
            totalSoldProducts += product.getSold();
        return totalSoldProducts;
    }

    @Override
    public int countProducts() {
        List<Product> products = findAllProducts();
        return products.size();
    }

    @Override
    public int countActiveProducts() {
        List<Product> products = findAllProducts();
        int totalActiveProducts = 0;
        for (Product product : products) {
            if (product.isActive())
                totalActiveProducts++;
        }
        return totalActiveProducts;
    }

    @Override
    public int countLowStockProducts() {
        List<Product> products = findAllProducts();
        int totalLowStockProducts = 0;
        for (Product product : products) {
            if (product.getQuantity() < 10)
                totalLowStockProducts++;
        }
        return totalLowStockProducts;
    }

    @Override
    public int deleteProduct(String id) {
        if (id == null || id.isEmpty())
            throw new NullPointerException("id must not be empty");

        try {
            if (itemService.isProductUsed(id)) {
                // chỉ inactive
                productDAO.inactivateProductById(id);
                return 1;
            } else {
                // xoá cứng
                productDAO.delete(id);
                return 0;
            }
        } catch (Exception e) {
            log.error("Could not delete or inactivate product:", e);
            throw new RuntimeException("Could not delete product.");
        }
    }

}
