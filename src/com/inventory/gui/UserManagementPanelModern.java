package com.inventory.gui;

import com.inventory.dao.UserDAO;
import com.inventory.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserManagementPanelModern extends JPanel {

    private final UserDAO dao = new UserDAO();
    private final DefaultTableModel model;
    private final JTable table;

    private final JTextField nameField;
    private final JTextField emailField;
    private final JPasswordField passwordField;

    public UserManagementPanelModern() {

        setLayout(new BorderLayout());
        setBackground(new Color(0x1e272c));

        /* ---------- TABLE ---------- */
        model = new DefaultTableModel(
                new Object[]{"ID", "Ad", "Email", "Rol"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        /* ---------- FORM (USER EKLE) ---------- */
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0x455a64)),
                "Yeni Kullanıcı (USER)",
                0, 0, null, Color.LIGHT_GRAY
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(label("Ad Soyad"), gbc);
        nameField = new JTextField(15);
        gbc.gridx = 1;
        form.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        form.add(label("Email"), gbc);
        emailField = new JTextField(15);
        gbc.gridx = 1;
        form.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        form.add(label("Şifre"), gbc);
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        form.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;

        JButton addBtn = new JButton("Kullanıcı Ekle");
        addBtn.putClientProperty("JButton.buttonType", "roundRect");
        addBtn.addActionListener(e -> addUser());

        form.add(addBtn, gbc);
        add(form, BorderLayout.EAST);

        refreshTable();
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.LIGHT_GRAY);
        return l;
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<User> users = dao.getAllUsers();
        for (User u : users) {
            model.addRow(new Object[]{
                    u.getUserId(),
                    u.getName(),
                    u.getEmail(),
                    u.getRole()
            });
        }
    }

    private void addUser() {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tüm alanları doldurun.",
                    "Uyarı",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = dao.addUserByAdmin(name, email, pass);

        if (!success) {
            JOptionPane.showMessageDialog(this,
                    "Bu e-posta zaten kullanılıyor.",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        refreshTable();
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
    }
}
