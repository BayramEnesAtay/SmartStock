package com.inventory.gui;

import com.inventory.dao.OrderDAO;
import com.inventory.model.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Displays orders created by database triggers.
 * Reads data directly from the orders table via OrderDAO.
 */
public class OrderPanelModern extends JPanel {

    private final OrderDAO orderDAO = new OrderDAO();
    private final DefaultTableModel model;

    public OrderPanelModern() {

        setLayout(new BorderLayout());
        setBackground(new Color(0x1e272c));

        model = new DefaultTableModel(
                new Object[]{"Sipariş ID", "Ürün", "Sipariş Miktarı", "Durum"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Yenile");
        refreshBtn.putClientProperty("JButton.buttonType", "roundRect");
        refreshBtn.addActionListener(e -> loadOrders());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.setOpaque(false);
        top.add(refreshBtn);

        add(top, BorderLayout.NORTH);

        loadOrders();
    }

    private void loadOrders() {
        model.setRowCount(0);

        List<Order> orders = orderDAO.getAllOrders();

        for (Order o : orders) {
            model.addRow(new Object[]{
                    o.getOrderId(),
                    o.getProductName(),
                    o.getOrderQuantity(),
                    o.getStatus()
            });
        }
    }
}
