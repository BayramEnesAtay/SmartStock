package com.inventory.gui;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLaf;
import com.inventory.model.User;
import com.inventory.session.Session;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainFrameModern extends JFrame {

    private final CardLayout rootLayout = new CardLayout();
    private final JPanel rootPanel = new JPanel(rootLayout);

    private final NavigationSidebarModern sidebar;
    private final JPanel dashboardContent;
    private final CardLayout dashboardLayout;

    private final ProductPanelModern productPanel;
    private final UserManagementPanelModern userPanel;
    private final OrderPanelModern orderPanel;
    private final AnalysisPanelModern analysisPanel;

    public MainFrameModern() {

        configureLookAndFeel();

        setTitle("Akıllı Stok Yönetim Sistemi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 550));

        /* ---------- LOGIN ---------- */
        LoginPanelModern loginPanel =
                new LoginPanelModern(this::onLoginSuccess, this::showRegister);
        rootPanel.add(loginPanel, "login");

        /* ---------- REGISTER ---------- */
        RegisterPanelModern registerPanel =
                new RegisterPanelModern(this::showLogin);
        rootPanel.add(registerPanel, "register");

        /* ---------- DASHBOARD ---------- */
        JPanel dashboard = new JPanel(new BorderLayout());

        sidebar = new NavigationSidebarModern(this::onNavigate);
        dashboard.add(sidebar, BorderLayout.WEST);

        dashboardLayout = new CardLayout();
        dashboardContent = new JPanel(dashboardLayout);

        productPanel = new ProductPanelModern();
        userPanel = new UserManagementPanelModern();
        orderPanel = new OrderPanelModern();
        analysisPanel = new AnalysisPanelModern();

        dashboardContent.add(productPanel, "products");
        dashboardContent.add(orderPanel, "orders");
        dashboardContent.add(userPanel, "users");
        dashboardContent.add(analysisPanel, "analysis");

        dashboard.add(dashboardContent, BorderLayout.CENTER);

        rootPanel.add(dashboard, "dashboard");

        add(rootPanel);
        showLogin();
    }

    /* ---------- LAF ---------- */
    private void configureLookAndFeel() {
        try {
            Map<String, String> defs = new HashMap<>();
            defs.put("Theme.accentColor", "#009688");
            defs.put("Component.arc", "12");
            defs.put("Button.arc", "20");
            defs.put("TextComponent.arc", "10");
            FlatLaf.setGlobalExtraDefaults(defs);
            FlatDarculaLaf.setup();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /* ---------- NAV ---------- */
    private void showLogin() {
        rootLayout.show(rootPanel, "login");
    }

    private void showRegister() {
        rootLayout.show(rootPanel, "register");
    }

    /* ---------- LOGIN SUCCESS ---------- */
    private void onLoginSuccess(User user) {

        // 🔥 Session set
        Session.currentUser = user;

        boolean isAdmin = user.isAdmin();

        // Sidebar yetki
        sidebar.setAdmin(isAdmin);

        // Panel yetkileri
        productPanel.setAdminMode(isAdmin);

        // Dashboard göster
        rootLayout.show(rootPanel, "dashboard");

        // Varsayılan ekran
        onNavigate("products");
    }

    private void onNavigate(String key) {
        if ("logout".equals(key)) {
            Session.currentUser = null;
            showLogin();
        } else {
            dashboardLayout.show(dashboardContent, key);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrameModern().setVisible(true);
        });
    }
}
