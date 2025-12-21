package com.inventory.gui;

import com.inventory.dao.ProductDAO;
import com.inventory.dao.StockDAO;
import com.inventory.model.Product;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductPanelModern extends JPanel {

    private final ProductDAO dao = new ProductDAO();
    private final StockDAO stockDAO = new StockDAO();

    private final DefaultTableModel model;
    private final JTable table;

    private final JTextField searchField;
    private final JTextField nameField;
    private final JSpinner qtySpinner;
    private final JSpinner minQtySpinner; // 🔥 sipariş eşiği
    private final JFormattedTextField priceField;

    // Satış (USER)
    private final JSpinner saleSpinner;
    private final JButton sellBtn;
    private final JPanel salePanel;

    // Admin CRUD
    private final JPanel formPanel;
    private final JButton addBtn;
    private final JButton updateBtn;
    private final JButton deleteBtn;

    private int selectedProductId = -1;
    private boolean adminMode = false;

    public ProductPanelModern() {

        setLayout(new BorderLayout());
        setBackground(new Color(0x1e272c));

        /* ---------- SEARCH ---------- */
        JPanel searchPanel = new JPanel(new BorderLayout(8, 8));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.setOpaque(false);

        searchField = new JTextField();
        JButton searchBtn = new JButton("Ara");

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);
        add(searchPanel, BorderLayout.NORTH);

        /* ---------- TABLE ---------- */
        model = new DefaultTableModel(
                new Object[]{"ID", "Ürün", "Stok", "Fiyat"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, center);
        ((DefaultTableCellRenderer) table.getTableHeader()
                .getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        add(new JScrollPane(table), BorderLayout.CENTER);

        /* ---------- ADMIN FORM ---------- */
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Ürün Adı"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Stok"), gbc);
        gbc.gridx = 1;
        qtySpinner = new JSpinner(new SpinnerNumberModel(1, 0, 10000, 1));
        formPanel.add(qtySpinner, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Sipariş Eşiği"), gbc);
        gbc.gridx = 1;
        minQtySpinner = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
        formPanel.add(minQtySpinner, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Fiyat"), gbc);
        gbc.gridx = 1;
        priceField = new JFormattedTextField(java.text.NumberFormat.getNumberInstance());
        formPanel.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;

        JPanel btnRow = new JPanel();
        addBtn = new JButton("Ekle");
        updateBtn = new JButton("Güncelle");
        deleteBtn = new JButton("Sil");

        btnRow.add(addBtn);
        btnRow.add(updateBtn);
        btnRow.add(deleteBtn);
        formPanel.add(btnRow, gbc);

        add(formPanel, BorderLayout.EAST);

        /* ---------- SALES ---------- */
        salePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        salePanel.setOpaque(false);

        salePanel.add(new JLabel("Satış Adedi:"));
        saleSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        salePanel.add(saleSpinner);

        sellBtn = new JButton("Satış Yap");
        salePanel.add(sellBtn);
        add(salePanel, BorderLayout.SOUTH);

        refreshTable(dao.getAllProducts());

        searchBtn.addActionListener(e -> search());
        addBtn.addActionListener(e -> addProduct());
        updateBtn.addActionListener(e -> updateProduct());
        deleteBtn.addActionListener(e -> deleteProduct());
        sellBtn.addActionListener(e -> sellProduct());

        table.getSelectionModel().addListSelectionListener(
                (ListSelectionEvent e) -> {
                    if (!e.getValueIsAdjusting()) {
                        int row = table.getSelectedRow();
                        if (row >= 0) {
                            selectedProductId = (int) model.getValueAt(row, 0);
                            if (adminMode) populateForm(row);
                        }
                    }
                }
        );

        setAdminMode(false);
    }

    public void setAdminMode(boolean admin) {
        this.adminMode = admin;
        formPanel.setVisible(admin);
        salePanel.setVisible(!admin);
    }

    private void refreshTable(List<Product> products) {
        model.setRowCount(0);
        for (Product p : products) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getQuantity(),
                    p.getPrice()
            });
        }
        selectedProductId = -1;
    }

    private void search() {
        String q = searchField.getText().trim();
        if (q.isEmpty()) refreshTable(dao.getAllProducts());
        else refreshTable(dao.searchByName(q));
    }

    private void populateForm(int row) {
        nameField.setText(model.getValueAt(row, 1).toString());
        qtySpinner.setValue(model.getValueAt(row, 2));
        priceField.setValue(model.getValueAt(row, 3));
    }

    private void addProduct() {
        dao.addProduct(
                nameField.getText(),
                (int) qtySpinner.getValue(),
                ((Number) priceField.getValue()).doubleValue(),
                (int) minQtySpinner.getValue()
        );
        refreshTable(dao.getAllProducts());
    }

    private void updateProduct() {
        if (selectedProductId == -1) return;

        dao.updateProduct(new Product(
                selectedProductId,
                nameField.getText(),
                (int) qtySpinner.getValue(),
                ((Number) priceField.getValue()).doubleValue()
        ));
        refreshTable(dao.getAllProducts());
    }

    private void deleteProduct() {
        if (selectedProductId == -1) return;
        dao.deleteProduct(selectedProductId);
        refreshTable(dao.getAllProducts());
    }

    private void sellProduct() {
        if (selectedProductId == -1) return;

        try {
            stockDAO.sellProduct(selectedProductId, (int) saleSpinner.getValue());
            refreshTable(dao.getAllProducts());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
