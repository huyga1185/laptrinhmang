package ltm.ntn.views.coupons;

import ltm.ntn.models.pojo.Coupon;
import ltm.ntn.share.enums.DiscountType;
import ltm.ntn.views.utils.CouponItemRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ManageCouponsView extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private JPanel listPanel;
    private CouponDetailView couponDetailView;
    private CouponAddView couponAddView;

    private JList<Coupon> couponList;
    private DefaultListModel<Coupon> listModel;

    public ManageCouponsView() {

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        createListPanel();

        couponDetailView = new CouponDetailView(this);
        couponAddView = new CouponAddView(this);

        cardPanel.add(listPanel, "list");
        cardPanel.add(couponDetailView, "detail");
        cardPanel.add(couponAddView, "add");

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);

        cardLayout.show(cardPanel, "list");
    }

    private void createListPanel() {

        listPanel = new JPanel(new BorderLayout());

        JLabel header = new JLabel("Manage Coupons");
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        listPanel.add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());

        JButton btnAdd = new JButton("➕ Thêm coupon");
        btnAdd.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnAdd.setPreferredSize(new Dimension(200, 40));
        btnAdd.setBackground(new Color(60, 120, 200));
        btnAdd.setForeground(Color.WHITE);

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAdd);

        centerPanel.add(btnPanel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        couponList = new JList<>(listModel);

        couponList.setCellRenderer(new CouponItemRenderer());
        couponList.setFixedCellHeight(80);

        JScrollPane scrollPane = new JScrollPane(couponList);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        listPanel.add(centerPanel, BorderLayout.CENTER);

        loadDemoCoupons();

        btnAdd.addActionListener(e -> {
            couponAddView.resetForm();
            cardLayout.show(cardPanel, "add");
        });

        couponList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Coupon c = couponList.getSelectedValue();
                    if (c != null) {
                        couponDetailView.setCoupon(c);
                        cardLayout.show(cardPanel, "detail");
                    }
                }
            }
        });
    }

    private void loadDemoCoupons() {
        listModel.clear();

        listModel.addElement(new Coupon(
                "1","SALE10", DiscountType.PERCENTAGE,
                10, java.time.LocalDate.now(),
                java.time.LocalDate.now().plusDays(30),
                100, 10, 0
        ));

        listModel.addElement(new Coupon(
                "2","FREESHIP", DiscountType.FIXED_AMOUNT,
                30, java.time.LocalDate.now(),
                java.time.LocalDate.now().plusDays(7),
                0, 5, 0
        ));
    }

    public void addCoupon(Coupon c) {
        listModel.addElement(c);
    }

    public void refreshList() {
        listPanel.revalidate();
        listPanel.repaint();
    }

    public void showListPanel() {
        cardLayout.show(cardPanel, "list");
    }
}