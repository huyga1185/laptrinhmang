package ltm.ntn.models.dao;

import ltm.ntn.models.dao.interfaces.IInvoiceDAO;
import ltm.ntn.models.pojo.Invoice;
import ltm.ntn.share.DBConnection;
import ltm.ntn.share.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO implements IInvoiceDAO {
    @Override
    public Invoice save(Connection connection, Invoice invoice) throws Exception {
        String sql;
        if (invoice.getId() == null)
            sql = "INSERT INTO invoices (id, coupon_id, invoice_code, total_amount) VALUES (?, ?, ?, ?)";
        else
            sql = "UPDATE invoices SET total_amount = ? WHERE id = ?";

        if (connection == null)
            throw new Exception("Connection is null");

        try(
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            if (invoice.getId() == null) {
                invoice.setId(Utils.createUUID());
                preparedStatement.setString(1, invoice.getId());
                preparedStatement.setString(2, invoice.getCouponId());
                preparedStatement.setString(3, invoice.getInvoiceCode());
                preparedStatement.setDouble(4, invoice.getTotalAmount());
            } else {
                preparedStatement.setDouble(1, invoice.getTotalAmount());
                preparedStatement.setString(2, invoice.getId());
            }

            preparedStatement.executeUpdate();
        }
        return invoice;
    }

    public static Invoice mapResultSetToInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setId(rs.getString("id"));
        invoice.setCouponId(rs.getString("coupon_id"));
        invoice.setInvoiceCode(rs.getString("invoice_code"));
        invoice.setTotalAmount(rs.getDouble("total_amount"));
        invoice.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return invoice;
    }

    @Override
    public Invoice getInvoiceById(String id) throws Exception {
        String sql = "SELECT * FROM invoices WHERE id = ?";
        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next())
                return mapResultSetToInvoice(rs);
        }
        return null;
    }

    @Override
    public Invoice getInvoiceByInvoiceCode(String invoiceCode) throws Exception {
        String sql = "SELECT * FROM invoices WHERE invoice_code = ?";
        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, invoiceCode);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next())
                return mapResultSetToInvoice(rs);
        }
        return null;
    }

    @Override
    public List<Invoice> getAllInvoices() throws Exception {
        String sql = "SELECT * FROM invoices";
        List<Invoice> invoices = new ArrayList<>();
        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Invoice invoice = mapResultSetToInvoice(rs);
                invoices.add(invoice);
            }
        }
        return invoices;
    }
}
