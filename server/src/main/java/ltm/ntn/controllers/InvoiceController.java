package ltm.ntn.controllers;

import lombok.Getter;
import ltm.ntn.share.dto.responses.gettings.GetInvoiceResponse;
import ltm.ntn.views.invoices.InvoiceAddView;
import ltm.ntn.views.invoices.InvoiceDetailView;
import ltm.ntn.views.invoices.ManageInvoicesView;

import java.util.List;

public class InvoiceController {

    @Getter
    private final InvoiceAddView invoiceAddView;
    @Getter
    private final InvoiceDetailView invoiceDetailView;
    @Getter
    private final ManageInvoicesView manageInvoicesView;

    public InvoiceController() {
        this.invoiceAddView = new InvoiceAddView();
        this.invoiceDetailView = new InvoiceDetailView();
        this.manageInvoicesView = new ManageInvoicesView(invoiceAddView, invoiceDetailView);

        initEvents();
    }

    private void initEvents() {
        // Double-click list item → detail
        manageInvoicesView.getInvoiceList().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    GetInvoiceResponse selected = manageInvoicesView.getSelectedInvoice();
                    if (selected != null) showInvoiceDetail(selected);
                }
            }
        });

        // Click Add Invoice button
        manageInvoicesView.getBtnAddInvoice().addActionListener(e -> showAddInvoice());

        // Back buttons
        invoiceAddView.getBtnBack().addActionListener(e -> manageInvoicesView.showListPanel());
        invoiceDetailView.getBtnBack().addActionListener(e -> manageInvoicesView.showListPanel());

        // Create button
        invoiceAddView.getBtnCreate().addActionListener(e -> createInvoice());
    }

    public void loadInvoices(List<GetInvoiceResponse> invoices) {
        manageInvoicesView.clearInvoices();
        invoices.forEach(manageInvoicesView::addInvoice);
        manageInvoicesView.showListPanel();
    }

    private void showAddInvoice() {
        invoiceAddView.resetFullForm();
        manageInvoicesView.showAddPanel(); // Sử dụng CardLayout nội bộ
    }

    private void showInvoiceDetail(GetInvoiceResponse invoice) {
        invoiceDetailView.loadData(invoice);
        manageInvoicesView.showDetailPanel();
    }

    private void createInvoice() {
        System.out.println("Create invoice clicked");
        // TODO: gọi service tạo invoice
    }
}
