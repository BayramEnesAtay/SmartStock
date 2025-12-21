package com.inventory.gui;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Stylish navigation sidebar.
 * Buttons are shown/hidden based on admin privilege.
 */
public class NavigationSidebarModern extends JPanel {

    private final Consumer<String> navHandler;
    private final Map<String, JButton> buttons = new LinkedHashMap<>();
    private String current;

    public NavigationSidebarModern(Consumer<String> navHandler) {
        this.navHandler = navHandler;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(0x263238));
        setBorder(BorderFactory.createEmptyBorder(20, 5, 20, 5));
    }

    /* =====================================================
       ADMIN / USER AYARI
       ===================================================== */
    public void setAdmin(boolean isAdmin) {

        removeAll();
        buttons.clear();

        // Herkes görebilir
        addButton("📦 Ürünler", "products");

        if (isAdmin) {
            addButton("🧾 Siparişler", "orders");
            addButton("👥 Kullanıcılar", "users");
        }

        addButton("📊 Analiz", "analysis");

        add(Box.createVerticalGlue());

        addButton("🔓 Çıkış Yap", "logout");

        revalidate();
        repaint();
    }

    /* =====================================================
       BUTTON
       ===================================================== */
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

    /* =====================================================
       SELECTION
       ===================================================== */
    private void select(String key) {

        buttons.forEach((k, btn) -> {
            if (k.equals(key)) {
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
