package com.inventory.gui;

import com.inventory.dao.UserDAO;
import com.inventory.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Dark themed login panel.  Splits the view into a decorative area and
 * a form.  Uses email and password fields.  Includes a link to the
 * registration panel.  Successfully authenticated users are passed
 * back via a callback.
 */
public class LoginPanelModern extends JPanel {
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JLabel messageLabel;
    private final Consumer<User> loginSuccessHandler;
    private final Runnable signUpHandler;

    public LoginPanelModern(Consumer<User> loginSuccessHandler, Runnable signUpHandler) {
        this.loginSuccessHandler = loginSuccessHandler;
        this.signUpHandler = signUpHandler;
        setLayout(new BorderLayout());
        setOpaque(true);

        // Left decorative area
        JPanel left = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                Color top = new Color(0x263238);
                Color bottom = new Color(0x11171a);
                g2.setPaint(new GradientPaint(0, 0, top, 0, getHeight(), bottom));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        left.setPreferredSize(new Dimension(250, 0));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel logo = new JLabel("📦");
        logo.setFont(new Font("SansSerif", Font.PLAIN, 48));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel title = new JLabel("Akıllı Stok");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subtitle = new JLabel("Yönetim Sistemi");
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(subtitle.getFont().deriveFont(Font.PLAIN, 16f));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(Box.createVerticalGlue());
        left.add(logo);
        left.add(Box.createVerticalStrut(10));
        left.add(title);
        left.add(subtitle);
        left.add(Box.createVerticalGlue());

        // Form area
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel header = new JLabel("Giriş Yap");
        header.setFont(header.getFont().deriveFont(Font.BOLD, 22f));
        header.setForeground(Color.WHITE);
        form.add(header, gbc);
        gbc.gridwidth = 1;
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
        gbc.gridwidth = 2;
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttons.setOpaque(false);
        JButton loginBtn = new JButton("Giriş");
        loginBtn.putClientProperty("JButton.buttonType", "roundRect");
        JButton signUpBtn = new JButton("Kayıt Ol");
        signUpBtn.putClientProperty("JButton.buttonType", "roundRect");
        buttons.add(loginBtn);
        buttons.add(signUpBtn);
        form.add(buttons, gbc);
        gbc.gridy++;
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(new Color(0xEF5350));
        form.add(messageLabel, gbc);
        add(left, BorderLayout.WEST);
        add(form, BorderLayout.CENTER);
        // Actions
        loginBtn.addActionListener(e -> authenticate());
        signUpBtn.addActionListener(e -> signUpHandler.run());
    }

    private void authenticate() {
        String email = emailField.getText().trim();
        String pass = new String(passwordField.getPassword());
        UserDAO dao = new UserDAO();
        User user = dao.authenticate(email, pass);
        if (user != null) {
            messageLabel.setText("");
            loginSuccessHandler.accept(user);
        } else {
            messageLabel.setText("Giriş başarısız. E‑posta veya şifre yanlış.");
        }
    }
}