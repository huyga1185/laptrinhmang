package ltm.ntn.models.dao;

import ltm.ntn.models.dao.interfaces.IProductDAO;
import ltm.ntn.models.pojo.Product;
import ltm.ntn.share.DBConnection;
import ltm.ntn.share.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IProductDAO {
    @Override
    public Product save(Connection connection, Product product) throws Exception {
        String sql;
        if (product.getId() == null)
            sql = "INSERT INTO products (id, sku, name, description, price, quantity, is_active) VALUES (?, ?, ?, ?, ?, ?, ?)";
        else
            sql = "UPDATE products SET name = ?, description = ?, price = ?, quantity = ?, is_active = ?, sold = ? WHERE id = ?";

        try (
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {

            if (product.getId() == null) {
                product.setId(Utils.createUUID());
                preparedStatement.setString(1, product.getId());
                preparedStatement.setString(2, product.getSku());
                preparedStatement.setString(3, product.getName());
                preparedStatement.setString(4, product.getDescription());
                preparedStatement.setDouble(5, product.getPrice());
                preparedStatement.setInt(6, product.getQuantity());
                preparedStatement.setBoolean(7, product.isActive());
            } else {
                preparedStatement.setString(1, product.getName());
                preparedStatement.setString(2, product.getDescription());
                preparedStatement.setDouble(3, product.getPrice());
                preparedStatement.setInt(4, product.getQuantity());
                preparedStatement.setBoolean(5, product.isActive());
                preparedStatement.setInt(6, product.getSold());
                preparedStatement.setString(7, product.getId());
            }

            preparedStatement.executeUpdate();
        }

        return product;
    }

    private static Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getString("id"));
        product.setSku(rs.getString("sku"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getDouble("price"));
        product.setQuantity(rs.getInt("quantity"));
        product.setActive(rs.getBoolean("is_active"));
        product.setSold(rs.getInt("sold"));
        return product;
    }

    private Product getProduct(String id, String sql) throws Exception {
        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next())
                    return mapResultSetToProduct(rs);
            }
        }
        return null;
    }

    @Override
    public Product getProductById(String id) throws Exception {
        String sql = "SELECT * FROM products WHERE id = ?";
        return getProduct(id, sql);
    }

    @Override
    public Product getProductBySKU(String sku) throws Exception {
        String sql = "SELECT * FROM products WHERE sku = ?";
        return getProduct(sku, sql);
    }

    @Override
    public void delete(String id) throws Exception {
        String sql =  "DELETE FROM products WHERE id = ?";
        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, id);
            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected == 0)
                throw new SQLException("Could not delete product.");
        }
    }

    private List<Product> getProducts(String sql) throws Exception {
        List<Product> products = new ArrayList<>();
        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next())
                    products.add(mapResultSetToProduct(rs));
            }
        }
        return products;
    }

    @Override
    public List<Product> getAllProducts() throws Exception {
        return getProducts("SELECT * FROM products");
    }

    @Override
    public List<Product> getAllActiveProducts() throws Exception {
        return getProducts("SELECT * FROM products WHERE is_active = 1");
    }

    @Override
    public List<Product> getAllInactiveProducts() throws Exception {
        return getProducts("SELECT * FROM products WHERE is_active = 0");
    }
}