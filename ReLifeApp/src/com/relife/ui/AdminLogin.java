package com.relife.ui;

import javax.swing.*;
import com.relife.db.DatabaseHelper;

public class AdminLogin extends JFrame {
    public AdminLogin() {
        setTitle("Admin Login - ReLife");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblUser = new JLabel("Admin Username:");
        lblUser.setBounds(50, 50, 120, 25);
        add(lblUser);

        JTextField txtUser = new JTextField();
        txtUser.setBounds(180, 50, 150, 25);
        add(txtUser);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(50, 90, 120, 25);
        add(lblPass);

        JPasswordField txtPass = new JPasswordField();
        txtPass.setBounds(180, 90, 150, 25);
        add(txtPass);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(140, 140, 100, 30);
        add(btnLogin);

        btnLogin.addActionListener(e -> {
            String username = txtUser.getText();
            String password = new String(txtPass.getPassword());
            if (DatabaseHelper.adminLogin(username, password)) {
                JOptionPane.showMessageDialog(this, "Admin login successful!");
                new AdminDashboard().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin credentials.");
            }
        });
    }

    public static void main(String[] args) {
        new AdminLogin().setVisible(true);
    }
}
