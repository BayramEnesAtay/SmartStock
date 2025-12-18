package com.inventory.gui;

import com.inventory.dao.UserDAO;
import com.inventory.model.User;

import javax.swing.*;
import java.awt.*;
// import java.awt.event.ItemEvent; // removed because role selection removed
import java.util.function.Consumer;

/**
 * Sign up panel with dynamic admin key field.  Allows entering name,
 * email, password and selecting a role.  If admin is selected, an
 * additional admin key field appears.  On successful registration
 * the newly created {@link User} is passed to the callback.
 */
public class RegisterPanelModern extends JPanel {
    private final JTextField nameField;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmField;
    // Removed role selection and admin key; sign up creates regular users only
    private final JLabel messageLabel;
    private final Consumer<User> registerSuccessHandler;
    private final Runnable backHandler;
    // Admin key removed because admin registration is disabled

    public RegisterPanelModern(Consumer<User> registerSuccessHandler, Runnable backHandler) {
        this.registerSuccessHandler = registerSuccessHandler;
        this.backHandler = backHandler;
        setLayout(new BorderLayout());
        setOpaque(true);
        // Left decoration similar to login
        JPanel left = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, new Color(0x263238), 0, getHeight(), new Color(0x11171a)));
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
        JLabel header = new JLabel("Kayıt Ol");
        header.setForeground(Color.WHITE);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 26f));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(Box.createVerticalGlue());
        left.add(icon);
        left.add(Box.createVerticalStrut(10));
        left.add(header);
        left.add(Box.createVerticalGlue());
        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel title = new JLabel("Yeni Hesap Oluştur");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        form.add(title, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel nameLabel = new JLabel("Ad Soyad");
        nameLabel.setForeground(Color.LIGHT_GRAY);
        form.add(nameLabel, gbc);
        nameField = new JTextField(15);
        nameField.putClientProperty("JComponent.roundRect", true);
        gbc.gridx = 1;
        form.add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setForeground(Color.LIGHT_GRAY);
        form.add(emailLabel, gbc);
        emailField = new JTextField(15);
        emailField.putClientProperty("JComponent.roundRect", true);
        gbc.gridx = 1;
        form.add(emailField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passLabel = new JLabel("Şifre");
        passLabel.setForeground(Color.LIGHT_GRAY);
        form.add(passLabel, gbc);
        passwordField = new JPasswordField(15);
        passwordField.putClientProperty("JComponent.roundRect", true);
        gbc.gridx = 1;
        form.add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel confirmLabel = new JLabel("Şifre Tekrar");
        confirmLabel.setForeground(Color.LIGHT_GRAY);
        form.add(confirmLabel, gbc);
        confirmField = new JPasswordField(15);
        confirmField.putClientProperty("JComponent.roundRect", true);
        gbc.gridx = 1;
        form.add(confirmField, gbc);
        // Role selection removed; only regular users can register
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttons.setOpaque(false);
        JButton registerBtn = new JButton("Kayıt Ol");
        registerBtn.putClientProperty("JButton.buttonType", "roundRect");
        JButton backBtn = new JButton("Geri");
        backBtn.putClientProperty("JButton.buttonType", "roundRect");
        buttons.add(registerBtn);
        buttons.add(backBtn);
        form.add(buttons, gbc);
        gbc.gridy++;
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(new Color(0xEF5350));
        form.add(messageLabel, gbc);
        add(left, BorderLayout.WEST);
        add(form, BorderLayout.CENTER);
        // actions
        registerBtn.addActionListener(e -> register());
        backBtn.addActionListener(e -> backHandler.run());
    }
    private void register() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String pass = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());
        // Only regular user registration allowed; no role selection
        if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            messageLabel.setText("Lütfen tüm alanları doldurun.");
            return;
        }
        if (!pass.equals(confirm)) {
            messageLabel.setText("Şifreler eşleşmiyor.");
            return;
        }
        int roleId = 2; // always user
        UserDAO dao = new UserDAO();
        User newUser = dao.register(name, email, pass, roleId);
        if (newUser == null) {
            messageLabel.setText("Bu e‑posta zaten kullanılıyor.");
            return;
        }
        messageLabel.setForeground(new Color(0x66BB6A));
        messageLabel.setText("Kayıt başarılı! Giriş yapabilirsiniz.");
        registerSuccessHandler.accept(newUser);
    }
}