package ltm.ntn.views;

import lombok.Getter;
import ltm.ntn.views.utils.SidebarButton;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

@Getter
public class HomeView extends JFrame {

    private CardLayout card;
    private JPanel contentPanel;

    private SidebarButton dashboardButton;
    private SidebarButton manageProductsButton;
    private SidebarButton manageCouponsButton;
    private SidebarButton manageInvoicesButton;
    private SidebarButton reportsButton;

    public HomeView() {
        super("Retail store management");
        setSize(1024, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBackground(new Color(240, 240, 240));

        JLabel titleLabel = new JLabel("Retail Store Management");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 40));

        JLabel todayLabel = new JLabel(LocalDate.now().toString());

        headerPanel.add(titleLabel);
        headerPanel.add(todayLabel);

        // Content (CardLayout)
        this.card = new CardLayout();
        this.contentPanel = new JPanel(card);
        contentPanel.setBackground(new Color(240, 240, 240));

        // Sidebar
        JPanel menuPanel = createSidebar();

        add(contentPanel, BorderLayout.CENTER);
        add(menuPanel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
    }

    private JPanel createSidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(84, 84, 84));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 30, 5, 30));

        dashboardButton = new SidebarButton("Dashboard");
        manageProductsButton = new SidebarButton("Manage Products");
        manageCouponsButton = new SidebarButton("Manage Coupons");
        manageInvoicesButton = new SidebarButton("Manage Invoices");
        reportsButton = new SidebarButton("Reports");

        panel.add(dashboardButton);
        panel.add(manageProductsButton);
        panel.add(manageCouponsButton);
        panel.add(manageInvoicesButton);
        panel.add(reportsButton);

        return panel;
    }

    public void showCard(String name) {
        card.show(contentPanel, name);
    }
}
