package ltm.ntn.models.dao;

import ltm.ntn.models.dao.interfaces.IInvoiceItemDAO;
import ltm.ntn.models.pojo.Invoice;
import ltm.ntn.models.pojo.InvoiceItem;
import ltm.ntn.share.DBConnection;
import ltm.ntn.share.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InvoiceItemDAO implements IInvoiceItemDAO {
    @Override
    public InvoiceItem save(Connection connection, InvoiceItem invoiceItem) throws Exception {
        String sql = "INSERT INTO invoice_items(id, invoice_id, product_id, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?, ?)";
        try (
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            invoiceItem.setId(Utils.createUUID());
            preparedStatement.setString(1, invoiceItem.getId());
            preparedStatement.setString(2, invoiceItem.getInvoiceId());
            preparedStatement.setString(3, invoiceItem.getProductId());
            preparedStatement.setInt(4, invoiceItem.getQuantity());
            preparedStatement.setDouble(5, invoiceItem.getUnitPrice());
            preparedStatement.setDouble(6, invoiceItem.getTotalPrice());


            preparedStatement.executeUpdate();
        }
        return invoiceItem;
    }

    public static InvoiceItem mapResultSetToInvoiceItem(ResultSet rs) throws Exception {
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setId(rs.getString("id"));
        invoiceItem.setInvoiceId(rs.getString("invoice_id"));
        invoiceItem.setProductId(rs.getString("product_id"));
        invoiceItem.setQuantity(rs.getInt("quantity"));
        invoiceItem.setUnitPrice(rs.getDouble("unit_price"));
        invoiceItem.setTotalPrice(rs.getDouble("total_price"));
        return invoiceItem;
    }

    private List<InvoiceItem> getInvoiceItems(String condition, String sql) throws Exception {
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, condition);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                InvoiceItem invoiceItem = mapResultSetToInvoiceItem(resultSet);
                invoiceItems.add(invoiceItem);
            }
        }
        return invoiceItems;
    }

    @Override
    public List<InvoiceItem> getAllInvoiceItemsByInvoiceCode(String invoiceCode) throws Exception {
        InvoiceDAO invoiceDAO = new InvoiceDAO();
        Invoice invoice = invoiceDAO.getInvoiceById(invoiceCode);
        return getAllInvoiceItemsByInvoiceId(invoice.getId());
    }

    @Override
    public List<InvoiceItem> getAllInvoiceItemsByInvoiceId(String invoiceId) throws Exception {
        return getInvoiceItems(invoiceId, "SELECT * FROM invoice_items WHERE invoice_id = ?");
    }

    @Override
    public List<InvoiceItem> getAllInvoiceItemsByProductId(String productId) throws Exception {
        return getInvoiceItems(productId, "SELECT * FROM invoice_items WHERE product_id = ?");
    }

    @Override
    public List<InvoiceItem> getAllInvoiceItems() throws Exception {
        return getInvoiceItems("0", "SELECT * FROM invoice_items WHERE 0 = ?");
    }

    @Override
    public InvoiceItem getInvoiceItemById(String id) throws Exception {
        String sql =  "SELECT * FROM invoice_items WHERE id = ?";
        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return mapResultSetToInvoiceItem(resultSet);
        }
        return null;
    }

    @Override
    public boolean isProductUsed(String productId) throws Exception {
        String sql = "SELECT EXISTS(SELECT 1 FROM invoice_items WHERE product_id = ?)";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getBoolean(1);
            }
        }
        return false;
    }

}
