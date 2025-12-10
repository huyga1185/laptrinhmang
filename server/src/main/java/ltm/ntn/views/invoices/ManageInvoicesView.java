package ltm.ntn.views.invoices;

import lombok.Getter;
import ltm.ntn.share.dto.responses.gettings.GetInvoiceResponse;
import ltm.ntn.views.utils.InvoiceItemRenderer;

import javax.swing.*;
import java.awt.*;

@Getter
public class ManageInvoicesView extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private JPanel listPanel;

    private InvoiceAddView invoiceAddView;
    private InvoiceDetailView invoiceDetailView;

    private JList<GetInvoiceResponse> invoiceList;
    private DefaultListModel<GetInvoiceResponse> invoiceListModel;

    @Getter
    private JButton btnAddInvoice; // nút Add Invoice expose ra ngoài

    // Constructor nhận các view từ controller
    public ManageInvoicesView(InvoiceAddView addView, InvoiceDetailView detailView) {
        this.invoiceAddView = addView;
        this.invoiceDetailView = detailView;

        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        createListPanel();

        // Add các panel vào CardLayout
        cardPanel.add(listPanel, "list");
        cardPanel.add(invoiceAddView, "add");
        cardPanel.add(invoiceDetailView, "detail");

        add(cardPanel, BorderLayout.CENTER);

        cardLayout.show(cardPanel, "list");
    }

    private void createListPanel() {

        listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel header = new JLabel("Manage Invoices");
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        listPanel.add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        listPanel.add(centerPanel, BorderLayout.CENTER);

        // ===== Add Invoice Button =====
        btnAddInvoice = new JButton("➕ Add Invoice");
        btnAddInvoice.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnAddInvoice.setPreferredSize(new Dimension(170, 40));
        btnAddInvoice.setBackground(new Color(60, 120, 200));
        btnAddInvoice.setForeground(Color.WHITE);
        btnAddInvoice.setFocusPainted(false);
        btnAddInvoice.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel topBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBtnPanel.add(btnAddInvoice);
        centerPanel.add(topBtnPanel, BorderLayout.NORTH);

        // ===== HEADER ROW =====
        JPanel headerRow = new JPanel(new GridLayout(1, 5, 15, 0));
        headerRow.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        headerRow.setBackground(new Color(230, 230, 230));
        Font headFont = new Font("Segoe UI", Font.BOLD, 13);
        headerRow.add(new JLabel("Invoice Code"));
        headerRow.add(new JLabel("Coupon"));
        headerRow.add(new JLabel("Type"));
        headerRow.add(new JLabel("Discount"));
        headerRow.add(new JLabel("Created At"));

        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.add(headerRow, BorderLayout.NORTH);

        invoiceListModel = new DefaultListModel<>();
        invoiceList = new JList<>(invoiceListModel);
        invoiceList.setCursor(new Cursor(Cursor.HAND_CURSOR));
        invoiceList.setCellRenderer(new InvoiceItemRenderer());
        invoiceList.setFixedCellHeight(35);

        JScrollPane scrollPane = new JScrollPane(invoiceList);
        listWrapper.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(listWrapper, BorderLayout.CENTER);
    }

    // =====================================================================
    //  PUBLIC FUNCTION SUPPORT CONTROLLER
    // =====================================================================

    public void clearInvoices() {
        invoiceListModel.clear();
    }

    public void addInvoice(GetInvoiceResponse invoice) {
        invoiceListModel.addElement(invoice);
    }

    public GetInvoiceResponse getSelectedInvoice() {
        return invoiceList.getSelectedValue();
    }

    // Panel switching (controller sẽ gọi)
    public void showListPanel() {
        cardLayout.show(cardPanel, "list");
    }

    public void showAddPanel() {
        cardLayout.show(cardPanel, "add");
    }

    public void showDetailPanel() {
        cardLayout.show(cardPanel, "detail");
    }
}
