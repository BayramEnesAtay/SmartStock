package com.inventory.gui;

import com.inventory.dao.UserDAO;
import com.inventory.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Admin panel for managing user accounts.  Displays a table of
 * registered users and provides a form to add new users.  A dark
 * colour palette is used to match the overall theme.
 */
public class UserManagementPanelModern extends JPanel {
    private final UserDAO dao = new UserDAO();
    private final DefaultTableModel model;
    private final JTable table;
    private final JTextField nameField;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JComboBox<String> roleCombo;
    private int selectedUserId = -1;

    public UserManagementPanelModern() {
        setLayout(new BorderLayout());
        setBackground(new Color(0x1e272c));
        // Table
        model = new DefaultTableModel(new Object[]{"ID", "Ad", "Email", "Rol"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0x455a64)), "Yeni Kullanıcı", 0, 0, null, Color.LIGHT_GRAY));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Ad Soyad"); nameLabel.setForeground(Color.LIGHT_GRAY);
        form.add(nameLabel, gbc);
        nameField = new JTextField(15);
        nameField.putClientProperty("JComponent.roundRect", true);
        gbc.gridx = 1;
        form.add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel emailLabel = new JLabel("Email"); emailLabel.setForeground(Color.LIGHT_GRAY);
        form.add(emailLabel, gbc);
        emailField = new JTextField(15);
        emailField.putClientProperty("JComponent.roundRect", true);
        gbc.gridx = 1;
        form.add(emailField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passLabel = new JLabel("Şifre"); passLabel.setForeground(Color.LIGHT_GRAY);
        form.add(passLabel, gbc);
        passwordField = new JPasswordField(15);
        passwordField.putClientProperty("JComponent.roundRect", true);
        gbc.gridx = 1;
        form.add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel roleLabel = new JLabel("Rol"); roleLabel.setForeground(Color.LIGHT_GRAY);
        form.add(roleLabel, gbc);
        roleCombo = new JComboBox<>(new String[]{"Kullanıcı", "Admin"});
        gbc.gridx = 1;
        form.add(roleCombo, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        buttonRow.setOpaque(false);
        JButton addBtn = new JButton("Ekle");
        addBtn.putClientProperty("JButton.buttonType", "roundRect");
        JButton deleteBtn = new JButton("Sil");
        deleteBtn.putClientProperty("JButton.buttonType", "roundRect");
        buttonRow.add(addBtn);
        buttonRow.add(deleteBtn);
        form.add(buttonRow, gbc);
        add(form, BorderLayout.EAST);
        // Load
        refreshTable();
        // Listeners
        addBtn.addActionListener(e -> addUser());
        deleteBtn.addActionListener(e -> deleteUser());
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) selectedUserId = (Integer) model.getValueAt(row, 0);
                else selectedUserId = -1;
            }
        });
    }
    private void refreshTable() {
        model.setRowCount(0);
        List<User> users = dao.getAllUsers();
        for (User u : users) {
            String roleName = u.getRoleId() == 1 ? "Admin" : "Kullanıcı";
            model.addRow(new Object[]{u.getId(), u.getName(), u.getEmail(), roleName});
        }
    }
    private void addUser() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String pass = new String(passwordField.getPassword());
        int roleId = roleCombo.getSelectedIndex() == 1 ? 1 : 2;
        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) return;
        User newUser = dao.register(name, email, pass, roleId);
        if (newUser == null) {
            JOptionPane.showMessageDialog(this, "Bu e‑posta zaten kullanılıyor.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        refreshTable();
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
    }
    private void deleteUser() {
        if (selectedUserId == -1) return;
        dao.deleteUser(selectedUserId);
        refreshTable();
    }
}