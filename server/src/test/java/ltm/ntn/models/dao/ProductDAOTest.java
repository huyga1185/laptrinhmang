package ltm.ntn.models.dao;

import lombok.extern.slf4j.Slf4j;
import ltm.ntn.models.pojo.Product;
import ltm.ntn.share.DBConnection;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class ProductDAOTest {
    @Test
    public void testSave() {
        Connection connection = null;
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            log.error("SQLException: ", e);
        }
        Product product = Product.builder()
                .name("test")
                .sku("test")
                .description("test")
                .price(100.0)
                .quantity(100)
                .isActive(true)
                .build();
        ProductDAO productDAO = new ProductDAO();
        try {
            productDAO.save(connection, product);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        assertNotNull(product.getId());
        product.setSku("test0");
        try {
            productDAO.save(connection, product);
        } catch (Exception ex) {
            log.error("save product failed", ex);
        }
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        assertEquals("test0", product.getSku());
    }
}
