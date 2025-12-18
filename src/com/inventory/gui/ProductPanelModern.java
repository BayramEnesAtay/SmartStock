package com.inventory.gui;

import com.inventory.dao.ProductDAO;
import com.inventory.model.Product;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Product listing and management panel.  Shows a search bar and
 * zebra‑striped table of products.  When in admin mode the form
 * fields and Add/Update/Delete buttons are visible; in user mode
 * these controls are hidden so that the panel functions as a read‑only
 * catalogue.  Uses a dark colour scheme to match the application.
 */
public class ProductPanelModern extends JPanel {
    private final ProductDAO dao = new ProductDAO();
    private final DefaultTableModel model;
    private final JTable table;
    private final JTextField searchField;
    private final JTextField nameField;
    private final JSpinner qtySpinner;
    private final JFormattedTextField priceField;
    private final JPanel formPanel;
    private final JButton addBtn;
    private final JButton updateBtn;
    private final JButton deleteBtn;
    private int selectedProductId = -1;
    private boolean adminMode;

    public ProductPanelModern() {
        setLayout(new BorderLayout());
        setBackground(new Color(0x1e272c));
        // search bar
        JPanel search = new JPanel(new BorderLayout(8, 8));
        search.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        search.setOpaque(false);
        searchField = new JTextField();
        searchField.putClientProperty("JComponent.roundRect", true);
        JButton searchBtn = new JButton("Ara");
        searchBtn.putClientProperty("JButton.buttonType", "roundRect");
        search.add(searchField, BorderLayout.CENTER);
        search.add(searchBtn, BorderLayout.EAST);
        add(search, BorderLayout.NORTH);
        // table
        model = new DefaultTableModel(new Object[]{"ID", "Ürün", "Adet", "Fiyat"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(false);
        // apply zebra stripes via FlatLaf property (will be applied automatically in dark theme)
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
        // form for admin
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0x455a64)), "Ürün Formu", 0, 0, null, Color.LIGHT_GRAY));
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Ürün Adı") {{ setForeground(Color.LIGHT_GRAY); }}, gbc);
        nameField = new JTextField(15);
        nameField.putClientProperty("JComponent.roundRect", true);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Adet") {{ setForeground(Color.LIGHT_GRAY); }}, gbc);
        qtySpinner = new JSpinner(new SpinnerNumberModel(1, 0, 10000, 1));
        gbc.gridx = 1;
        formPanel.add(qtySpinner, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Fiyat") {{ setForeground(Color.LIGHT_GRAY); }}, gbc);
        priceField = new JFormattedTextField(java.text.NumberFormat.getNumberInstance());
        priceField.setColumns(10);
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        btnRow.setOpaque(false);
        addBtn = new JButton("Ekle");
        addBtn.putClientProperty("JButton.buttonType", "roundRect");
        updateBtn = new JButton("Güncelle");
        updateBtn.putClientProperty("JButton.buttonType", "roundRect");
        deleteBtn = new JButton("Sil");
        deleteBtn.putClientProperty("JButton.buttonType", "roundRect");
        btnRow.add(addBtn);
        btnRow.add(updateBtn);
        btnRow.add(deleteBtn);
        formPanel.add(btnRow, gbc);
        add(formPanel, BorderLayout.EAST);
        // load data
        refreshTable(dao.getAllProducts());
        // listeners
        searchBtn.addActionListener(e -> search());
        addBtn.addActionListener(e -> addProduct());
        updateBtn.addActionListener(e -> updateProduct());
        deleteBtn.addActionListener(e -> deleteProduct());
        table.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) populateForm();
        });
    }
    public void setAdminMode(boolean admin) {
        this.adminMode = admin;
        formPanel.setVisible(admin);
        addBtn.setVisible(admin);
        updateBtn.setVisible(admin);
        deleteBtn.setVisible(admin);
        // If not admin, clear selection to prevent editing
        if (!admin) {
            table.clearSelection();
        }
    }
    private void refreshTable(List<Product> products) {
        model.setRowCount(0);
        for (Product p : products) {
            model.addRow(new Object[]{p.getId(), p.getName(), p.getQuantity(), p.getPrice()});
        }
    }
    private void search() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) refreshTable(dao.getAllProducts());
        else refreshTable(dao.searchByName(query));
    }
    private void populateForm() {
        if (!adminMode) return;
        int row = table.getSelectedRow();
        if (row >= 0) {
            selectedProductId = (Integer) model.getValueAt(row, 0);
            nameField.setText((String) model.getValueAt(row, 1));
            qtySpinner.setValue((Integer) model.getValueAt(row, 2));
            priceField.setValue((Double) model.getValueAt(row, 3));
        } else {
            clearForm();
        }
    }
    private void addProduct() {
        String name = nameField.getText().trim();
        int qty = ((Number) qtySpinner.getValue()).intValue();
        Number priceNum = (Number) priceField.getValue();
        double price = priceNum != null ? priceNum.doubleValue() : 0.0;
        if (name.isEmpty()) return;
        dao.addProduct(name, qty, price);
        refreshTable(dao.getAllProducts());
        clearForm();
    }
    private void updateProduct() {
        if (selectedProductId == -1) return;
        String name = nameField.getText().trim();
        int qty = ((Number) qtySpinner.getValue()).intValue();
        Number priceNum = (Number) priceField.getValue();
        double price = priceNum != null ? priceNum.doubleValue() : 0.0;
        dao.updateProduct(new Product(selectedProductId, name, qty, price));
        refreshTable(dao.getAllProducts());
        clearForm();
    }
    private void deleteProduct() {
        if (selectedProductId == -1) return;
        dao.deleteProduct(selectedProductId);
        refreshTable(dao.getAllProducts());
        clearForm();
    }
    private void clearForm() {
        selectedProductId = -1;
        nameField.setText("");
        qtySpinner.setValue(1);
        priceField.setValue(null);
    }
}