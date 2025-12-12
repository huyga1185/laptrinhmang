package ltm.ntn.views.coupons;

import ltm.ntn.models.pojo.Coupon;
import ltm.ntn.views.utils.CouponItemRenderer;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class ManageCouponsView extends JPanel {

    @Setter
    private Consumer<Coupon> onItemSelected;   // 🔥 callback giống product

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private JPanel listPanel;
    private JList<Coupon> couponList;
    private DefaultListModel<Coupon> listModel;

    private JButton btnAdd;

    public ManageCouponsView() {

        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        createListPanel();
        cardPanel.add(listPanel, "list");

        add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "list");
    }

    private void createListPanel() {
        listPanel = new JPanel(new BorderLayout());

        // HEADER
        JLabel header = new JLabel("Manage Coupons");
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        listPanel.add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());

        // BUTTON ADD
        btnAdd = new JButton("➕ Thêm coupon");
        btnAdd.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnAdd.setPreferredSize(new Dimension(200, 40));

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAdd);
        centerPanel.add(btnPanel, BorderLayout.NORTH);

        // LIST + RENDERER
        listModel = new DefaultListModel<>();
        couponList = new JList<>(listModel);

        couponList.setCellRenderer(new CouponItemRenderer());
        couponList.setFixedCellHeight(70);

        // SCROLL
        JScrollPane scrollPane = new JScrollPane(couponList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        listPanel.add(centerPanel, BorderLayout.CENTER);

        // 🔥 DOUBLE CLICK SELECT ITEM
        couponList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Coupon selected = couponList.getSelectedValue();
                    if (selected != null && onItemSelected != null) {
                        onItemSelected.accept(selected);
                    }
                }
            }
        });
    }

    // =============== HELPERS ==================
    public void setCoupons(List<Coupon> coupons) {
        listModel.clear();
        for (Coupon c : coupons) listModel.addElement(c);
    }

    public void addCouponToList(Coupon c) {
        listModel.addElement(c);
    }

    public void updateCouponInList(Coupon updated) {
        for (int i = 0; i < listModel.size(); i++) {
            Coupon c = listModel.get(i);

            // So sánh theo ID hoặc SKU – tuỳ bạn dùng trường nào
            if (c.getId().equals(updated.getId())) {
                listModel.set(i, updated);    // refresh UI
                return;
            }
        }
    }

    public void removeCoupon(String couponId) {
        for (int i = 0; i < listModel.size(); i++) {
            Coupon c = listModel.get(i);
            if (c.getId().equals(couponId)) {
                listModel.remove(i);
                return;
            }
        }
    }

}