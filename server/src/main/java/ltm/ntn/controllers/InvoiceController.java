package ltm.ntn.controllers;

import lombok.Getter;
import ltm.ntn.views.invoices.InvoiceAddView;
import ltm.ntn.views.invoices.InvoiceDetailView;
import ltm.ntn.views.invoices.ManageInvoicesView;

public class InvoiceController {
    @Getter
    private final ManageInvoicesView manageInvoicesView;
    @Getter
    private final InvoiceDetailView invoiceDetailView;
    @Getter
    private final InvoiceAddView invoiceAddView;

    public InvoiceController() {
        this.manageInvoicesView = new ManageInvoicesView();
        this.invoiceDetailView = new InvoiceDetailView();
        this.invoiceAddView = new InvoiceAddView();
    }
}
