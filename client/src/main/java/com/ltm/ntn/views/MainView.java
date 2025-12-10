package com.ltm.ntn.views;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class MainView extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;

    private JButton btnViewProducts;
    private JButton btnCreateInvoice;
    private JButton btnViewInvoices;

    public MainView() {
        super("Client - Quản lý Bán hàng");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Sidebar menu
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // Content panel với CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel, BorderLayout.CENTER);

        // Header
        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);
    }

    private JPanel createSidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(52, 73, 94));
        panel.setPreferredSize(new Dimension(250, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel title = new JLabel("MENU");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        btnViewProducts = createMenuButton("📦 Xem Sản Phẩm");
        btnCreateInvoice = createMenuButton("➕ Tạo Hóa Đơn");
        btnViewInvoices = createMenuButton("📋 Xem Hóa Đơn");

        panel.add(btnViewProducts);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnCreateInvoice);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnViewInvoices);

        return panel;
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setBackground(new Color(41, 128, 185));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(52, 152, 219));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(41, 128, 185));
            }
        });

        return btn;
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("HỆ THỐNG QUẢN LÝ BÁN HÀNG - CLIENT");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(52, 73, 94));
        panel.add(title, BorderLayout.WEST);

        return panel;
    }

    public void showCard(String cardName) {
        cardLayout.show(contentPanel, cardName);
    }
}
