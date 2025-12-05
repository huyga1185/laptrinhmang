package ltm.ntn.controllers;

import lombok.Getter;
import ltm.ntn.views.products.ManageProductsView;
import ltm.ntn.views.products.ProductAddView;
import ltm.ntn.views.products.ProductDetailView;

public class ProductController {
    @Getter
    private final ManageProductsView manageProductsView;
    @Getter
    private final ProductAddView productAddView;
    @Getter
    private final ProductDetailView productDetailView;

    public ProductController() {
        this.manageProductsView = new ManageProductsView();
        this.productAddView = new ProductAddView(manageProductsView);
        this.productDetailView = new ProductDetailView(manageProductsView);
    }
}
