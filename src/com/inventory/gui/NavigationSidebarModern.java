package com.inventory.gui;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Stylish navigation sidebar with emoji icons.  Displays different
 * buttons depending on the user's role.  Highlights the selected
 * button and notifies the navigation handler on click.
 */
public class NavigationSidebarModern extends JPanel {
    private final Consumer<String> navHandler;
    private final Map<String, JButton> buttons = new LinkedHashMap<>();
    private String current;
    private boolean adminMode;

    public NavigationSidebarModern(Consumer<String> navHandler) {
        this.navHandler = navHandler;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(0x263238));
        setBorder(BorderFactory.createEmptyBorder(20, 5, 20, 5));
    }
    public void setRole(int roleId) {
        removeAll();
        buttons.clear();
        adminMode = (roleId == 1);
        // Add product button for both roles
        addButton("📦 Ürünler", "products");
        if (adminMode) {
            addButton("🧾 Siparişler", "orders");
            addButton("👥 Kullanıcılar", "users");
        }
        addButton("📊 Analiz", "analysis");
        add(Box.createVerticalGlue());
        addButton("🔓 Çıkış Yap", "logout");
        revalidate();
        repaint();
    }
    private void addButton(String text, String key) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.setFocusPainted(false);
        btn.setForeground(Color.LIGHT_GRAY);
        btn.setBackground(new Color(0x37474F));
        btn.addActionListener(e -> select(key));
        buttons.put(key, btn);
        add(btn);
    }
    private void select(String key) {
        buttons.forEach((k, btn) -> {
            boolean selected = k.equals(key);
            if (selected) {
                btn.setBackground(new Color(0x009688));
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(new Color(0x37474F));
                btn.setForeground(Color.LIGHT_GRAY);
            }
        });
        current = key;
        navHandler.accept(key);
    }
}