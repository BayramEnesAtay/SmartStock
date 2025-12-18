package com.inventory.gui;

import com.inventory.dao.ProductDAO;
import com.inventory.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Lists pending orders when stock falls below a threshold.  Simulates
 * automatic order triggers by computing the difference between a
 * threshold and the current stock of each product.  Uses a dark
 * theme.
 */
public class OrderPanelModern extends JPanel {
    private final ProductDAO dao = new ProductDAO();
    private final DefaultTableModel model;
    private static final int THRESHOLD = 30;

    public OrderPanelModern() {
        setLayout(new BorderLayout());
        setBackground(new Color(0x1e272c));
        model = new DefaultTableModel(new Object[]{"Ürün", "Mevcut Stok", "Sipariş Miktarı"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
        JButton refresh = new JButton("Yenile");
        refresh.putClientProperty("JButton.buttonType", "roundRect");
        refresh.addActionListener(e -> loadOrders());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.setOpaque(false);
        top.add(refresh);
        add(top, BorderLayout.NORTH);
        loadOrders();
    }
    private void loadOrders() {
        model.setRowCount(0);
        List<Product> products = dao.getAllProducts();
        for (Product p : products) {
            if (p.getQuantity() < THRESHOLD) {
                int orderQty = THRESHOLD - p.getQuantity();
                model.addRow(new Object[]{p.getName(), p.getQuantity(), orderQty});
            }
        }
    }
}