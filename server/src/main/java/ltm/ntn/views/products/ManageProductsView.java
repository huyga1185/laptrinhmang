package ltm.ntn.views.products;

import lombok.Getter;
import lombok.Setter;
import ltm.ntn.models.pojo.Product;
import ltm.ntn.views.utils.ProductItemRenderer;

import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class ManageProductsView extends JPanel {
    @Setter
    private Consumer<Product> onItemSelected;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private JPanel listPanel;

    private JList<Product> productList;
    private DefaultListModel<Product> listModel;

    private JButton btnAddProduct; // để controller bắt sự kiện

    public ManageProductsView() {

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        createListPanel();

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
    }

    private void createListPanel() {

        listPanel = new JPanel(new BorderLayout());

        JLabel header = new JLabel("Manage Product");
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setHorizontalAlignment(SwingConstants.CENTER);
        listPanel.add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());

        btnAddProduct = new JButton("➕ Add Product");
        btnAddProduct.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnAddProduct.setPreferredSize(new Dimension(200, 40));

        JPanel topBtnPanel = new JPanel();
        topBtnPanel.add(btnAddProduct);

        centerPanel.add(topBtnPanel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        productList = new JList<>(listModel);
        productList.setCellRenderer(new ProductItemRenderer());
        productList.setFixedCellHeight(80);

        centerPanel.add(new JScrollPane(productList), BorderLayout.CENTER);

        listPanel.add(centerPanel, BorderLayout.CENTER);
        productList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (onItemSelected != null) {
                        Product selected = productList.getSelectedValue();
                        onItemSelected.accept(selected);
                    }
                }
            }
        });

    }

    public void addProducts(List<Product> products) {
        listModel.clear();
        for (Product p : products) {
            listModel.addElement(p);
        }
    }

    public void addProduct(Product product) {
        if (product != null) {
            listModel.addElement(product);
        }
    }

    public void removeProduct(String productId) {
        for (int i = 0; i < listModel.size(); i++) {
            Product p = listModel.get(i);
            if (p.getId().equals(productId)) {
                listModel.remove(i);
                return;
            }
        }
    }

    public void updateProduct(Product updatedProduct) {
        for (int i = 0; i < listModel.size(); i++) {
            Product p = listModel.get(i);
            if (p.getId().equals(updatedProduct.getId())) {
                listModel.set(i, updatedProduct);
                return;
            }
        }
    }
}
