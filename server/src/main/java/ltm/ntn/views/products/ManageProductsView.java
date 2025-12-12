package ltm.ntn.views.products;

import ltm.ntn.models.pojo.Product;
import ltm.ntn.models.services.ProductService;
import ltm.ntn.views.utils.ProductItemRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ManageProductsView extends JPanel {

    private final ProductService productService; // thêm dòng này

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private JPanel listPanel;
    private ProductDetailView productDetailView;
    private ProductAddView productAddView;

    private JList<Product> productList;
    private DefaultListModel<Product> listModel;

    public ManageProductsView() {

        productService = new ProductService(); // khởi tạo service

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        createListPanel();

        productDetailView = new ProductDetailView(this);
        productAddView = new ProductAddView(this);

        cardPanel.add(listPanel, "list");
        cardPanel.add(productDetailView, "detail");
        cardPanel.add(productAddView, "add");

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);

        cardLayout.show(cardPanel, "list");
    }

    private void createListPanel() {

        listPanel = new JPanel(new BorderLayout());

        JLabel header = new JLabel("Manage Product");
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        listPanel.add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());

        JButton btnAddProduct = new JButton("➕Add Product");
        btnAddProduct.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnAddProduct.setPreferredSize(new Dimension(200, 40));
        btnAddProduct.setBackground(new Color(60, 120, 200));
        btnAddProduct.setForeground(Color.WHITE);
        btnAddProduct.setFocusPainted(false);
        btnAddProduct.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel topBtnPanel = new JPanel();
        topBtnPanel.add(btnAddProduct);

        centerPanel.add(topBtnPanel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        productList = new JList<>(listModel);
        productList.setCursor(new Cursor(Cursor.HAND_CURSOR));

        productList.setCellRenderer(new ProductItemRenderer());
        productList.setFixedCellHeight(80);

        JScrollPane scrollPane = new JScrollPane(productList);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        listPanel.add(centerPanel, BorderLayout.CENTER);

        loadDemoProducts();

        btnAddProduct.addActionListener(e -> {
            productAddView.resetForm();
            cardLayout.show(cardPanel, "add");
        });

        productList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Product p = productList.getSelectedValue();
                    if (p != null) {
                        productDetailView.setProduct(p);
                        cardLayout.show(cardPanel, "detail");
                    }
                }
            }
        });
    }

    private void loadDemoProducts() {
        listModel.clear();
        List<Product> demo = new ArrayList<>();
        demo.add(new Product("1", "Laptop", "SKU001", "Laptop gaming", 1200.0, 10, 0, true));
        demo.add(new Product("2", "Mouse", "SKU002", "Wireless mouse", 25.5, 50, 0, true));
        demo.add(new Product("3", "Keyboard", "SKU003", "Mechanical keyboard", 80.0, 30, 0, false));

        demo.forEach(listModel::addElement);
    }

    public void refreshList() {
        listPanel.repaint();
        listPanel.revalidate();
    }

    public void addProduct(Product p) {
        listModel.addElement(p);
    }


    public void showListPanel() {
        cardLayout.show(cardPanel, "list");
    }
    // THÊM PHƯƠNG THỨC NÀY
    public ProductService getProductService() {
        return productService;
    }

}

