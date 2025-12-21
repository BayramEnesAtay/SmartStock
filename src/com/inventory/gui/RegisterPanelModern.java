package com.inventory.gui;

import com.inventory.dao.UserDAO;

import javax.swing.*;
import java.awt.*;

public class RegisterPanelModern extends JPanel {

    private final JTextField nameField;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmField;
    private final JLabel messageLabel;
    private final Runnable backHandler;

    public RegisterPanelModern(Runnable backHandler) {
        this.backHandler = backHandler;

        setLayout(new BorderLayout());
        setOpaque(true);

        /* ---------- LEFT ---------- */
        JPanel left = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(
                        0, 0, new Color(0x263238),
                        0, getHeight(), new Color(0x11171a)
                ));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };

        left.setPreferredSize(new Dimension(250, 0));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel("📝");
        icon.setFont(new Font("SansSerif", Font.PLAIN, 48));
        icon.setForeground(Color.WHITE);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Kayıt Ol");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        left.add(Box.createVerticalGlue());
        left.add(icon);
        left.add(Box.createVerticalStrut(10));
        left.add(title);
        left.add(Box.createVerticalGlue());

        /* ---------- FORM ---------- */
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel header = new JLabel("Yeni Hesap Oluştur");
        header.setFont(header.getFont().deriveFont(Font.BOLD, 22f));
        header.setForeground(Color.WHITE);
        form.add(header, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        form.add(new JLabel("Ad Soyad"), gbc);
        nameField = new JTextField(15);
        gbc.gridx = 1;
        form.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        form.add(new JLabel("Email"), gbc);
        emailField = new JTextField(15);
        gbc.gridx = 1;
        form.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        form.add(new JLabel("Şifre"), gbc);
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        form.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        form.add(new JLabel("Şifre Tekrar"), gbc);
        confirmField = new JPasswordField(15);
        gbc.gridx = 1;
        form.add(confirmField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setOpaque(false);

        JButton registerBtn = new JButton("Kayıt Ol");
        JButton backBtn = new JButton("Geri");

        buttons.add(registerBtn);
        buttons.add(backBtn);
        form.add(buttons, gbc);

        gbc.gridy++;
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(new Color(0xEF5350));
        form.add(messageLabel, gbc);

        add(left, BorderLayout.WEST);
        add(form, BorderLayout.CENTER);

        /* ---------- ACTIONS ---------- */
        registerBtn.addActionListener(e -> register());
        backBtn.addActionListener(e -> backHandler.run());
    }

    private void register() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String pass = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            messageLabel.setText("Tüm alanları doldurun.");
            return;
        }

        if (!pass.equals(confirm)) {
            messageLabel.setText("Şifreler eşleşmiyor.");
            return;
        }

        UserDAO dao = new UserDAO();
        boolean success = dao.register(name, email, pass);

        if (!success) {
            messageLabel.setText("Bu e-posta zaten kayıtlı.");
            clearForm();
            return;
        }
        clearForm();
        messageLabel.setForeground(new Color(0x66BB6A));
        messageLabel.setText("Kayıt başarılı! Giriş yapabilirsiniz.");
    }
    private void clearForm()
    {
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmField.setText("");
    }
}
