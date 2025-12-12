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
        boolean isInsert = invoice.getId() == null;

        String sql;
        if (isInsert) {
            sql = "INSERT INTO invoices (id, coupon_id, invoice_code, total_amount) VALUES (?, ?, ?, ?)";
            invoice.setId(Utils.createUUID()); // tạo ID mới
        } else {
            sql = "UPDATE invoices SET total_amount = ? WHERE id = ?";
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            if (isInsert) {
                ps.setString(1, invoice.getId());
                ps.setString(2, invoice.getCouponId());
                ps.setString(3, invoice.getInvoiceCode());
                ps.setDouble(4, invoice.getTotalAmount());
            } else {
                ps.setDouble(1, invoice.getTotalAmount());
                ps.setString(2, invoice.getId());
            }

            ps.executeUpdate();
        }

        return invoice; // luôn trả về ID đã set
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
