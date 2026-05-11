package com.relife.ui;
import com.relife.model.User;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.relife.db.DatabaseHelper;


@SuppressWarnings("serial")
public class ReLifeApp extends JFrame {
    // Color Palette
    private static final Color PRIMARY = new Color(10, 38, 71);
    private static final Color ACCENT = new Color(0, 194, 203);
    private static final Color SECONDARY = new Color(92, 107, 192);
    private static final Color TEXT = new Color(224, 224, 224);
    private static final Color BACKGROUND = new Color(8, 24, 38);
    private static final Color LIGHT_BG = new Color(20, 40, 60);
    
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private User currentUser;


    private DashboardPanel dashboardPanel;

    public ReLifeApp() {
        setTitle("ReLife - Digital Second Chance Platform");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 850);
        setLocationRelativeTo(null);
        setResizable(true);
        DatabaseHelper.initializeDatabase();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        mainPanel.add(new LoginScreen(this), "LOGIN");
        mainPanel.add(new RegisterScreen(this), "REGISTER");
        
        // Create dashboard panel ONCE and keep it
        dashboardPanel = new DashboardPanel(this);
        mainPanel.add(dashboardPanel, "DASHBOARD");

        add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");
        setVisible(true);
    }
    public void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public static JButton createRoundedButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Helvetica", Font.BOLD, 13));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new RoundedBorder(bgColor, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });
        return btn;
    }

    public static JTextField createRoundedTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Helvetica", Font.PLAIN, 12));
        tf.setBorder(new RoundedBorder(Color.LIGHT_GRAY, 10));
        tf.setPreferredSize(new Dimension(0, 40));
        return tf;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReLifeApp());
    }
}

// ============= CUSTOM ROUNDED BORDER =============
@SuppressWarnings("serial")
class RoundedBorder extends AbstractBorder {
    private Color color;
    private int radius;

    public RoundedBorder(Color color, int radius) {
        this.color = color;
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(5, 10, 5, 10);
    }
}

//============= UPDATED: LoginScreen =============

@SuppressWarnings("serial")
class LoginScreen extends JPanel {
    private ReLifeApp app;
    private static final Color PRIMARY = new Color(10, 38, 71);
    private static final Color ACCENT = new Color(0, 194, 203);
    private static final Color SECONDARY = new Color(92, 107, 192);
    private static final Color TEXT = new Color(224, 224, 224);
    private static final Color BACKGROUND = new Color(8, 24, 38);
    private static final Color LIGHT_BG = new Color(20, 40, 60);

    public LoginScreen(ReLifeApp app) {
        this.app = app;
        setBackground(BACKGROUND);
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel header = new JLabel("Welcome to ReLife");
        header.setFont(new Font("Helvetica", Font.BOLD, 32));
        header.setForeground(ACCENT);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(LIGHT_BG);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(new LineBorder(PRIMARY, 2));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 40, 20, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JLabel logoLabel = new JLabel("🌱 ReLife");
        logoLabel.setFont(new Font("Helvetica", Font.BOLD, 40));
        logoLabel.setForeground(ACCENT);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 40, 10, 40);
        formPanel.add(logoLabel, gbc);

        JLabel taglineLabel = new JLabel("Your Digital Second Chance Platform");
        taglineLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));
        taglineLabel.setForeground(new Color(150, 150, 150));
        taglineLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 40, 30, 40);
        formPanel.add(taglineLabel, gbc);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Helvetica", Font.BOLD, 13));
        userLabel.setForeground(ACCENT);
        gbc.gridy = 2;
        gbc.insets = new Insets(15, 40, 8, 40);
        formPanel.add(userLabel, gbc);

        JTextField userField = createStyledTextField();
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 40, 15, 40);
        formPanel.add(userField, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Helvetica", Font.BOLD, 13));
        passLabel.setForeground(ACCENT);
        gbc.gridy = 4;
        gbc.insets = new Insets(15, 40, 8, 40);
        formPanel.add(passLabel, gbc);

        JPasswordField passField = createStyledPasswordField();
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 40, 25, 40);
        formPanel.add(passField, gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setBackground(LIGHT_BG);
        buttonPanel.setPreferredSize(new Dimension(0, 50));

     // In LoginScreen.java, find the login button action listener and update it

        JButton loginBtn = createStyledButton("Login", ACCENT);
        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());
            
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ Username is required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            } else if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ Password is required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            } else if (password.length() < 6) {
                JOptionPane.showMessageDialog(this, "❌ Password must be at least 6 characters long!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            } else {
                System.out.println("DEBUG: Starting login for username: " + username);
                
                int userId = DatabaseHelper.loginUser(username, password);
                System.out.println("DEBUG: userId returned: " + userId);
                
                // ✅ CHANGE THIS PART - Check for 3 different cases
                if (userId == -2) {
                    // ❌ CASE 1: Account DELETED
                    System.out.println("❌ User account has been deleted!");
                    JOptionPane.showMessageDialog(this, 
                        "⛔ ACCESS DENIED ⛔\n\n" +
                        "Your account has been permanently deleted by the administrator.\n\n" +
                        "• Account Status: DEACTIVATED\n" +
                        "• Access Level: REVOKED\n" +
                        "• Your Data: Preserved but no longer accessible\n\n" +
                        "If you believe this is an error, please contact the administrator.\n" +
                        "📧 Support Email: admin@relife.com",
                        "Account Deleted - Access Denied", 
                        JOptionPane.ERROR_MESSAGE);
                    
                    userField.setText("");
                    passField.setText("");
                    
                } else if (userId > 0) {
                    // ✅ CASE 2: Login SUCCESS
                    System.out.println("DEBUG: Login successful, getting name and email...");
                    
                    String userName = DatabaseHelper.getUserName(userId);
                    System.out.println("DEBUG: userName = " + userName);
                    
                    String userEmail = DatabaseHelper.getUserEmail(userId);
                    System.out.println("DEBUG: userEmail = " + userEmail);
                    
                    System.out.println("DEBUG: Creating User object with userId=" + userId + ", name=" + userName + ", email=" + userEmail);
                    User user = new User(userId, userName, userEmail);
                    app.setCurrentUser(user);
                    
                    System.out.println("DEBUG: Clearing fields and showing DASHBOARD");
                    userField.setText("");
                    passField.setText("");
                    
                    // ✅ Update last login time
                    System.out.println("DEBUG: Updating last login for userId: " + userId);
                    DatabaseHelper.updateLastLogin(userId);
                    
                    app.showScreen("DASHBOARD");
                    
                } else {
                    // ❌ CASE 3: Wrong password or user not found
                    System.out.println("DEBUG: Login FAILED - invalid credentials");
                    JOptionPane.showMessageDialog(this, 
                        "❌ LOGIN FAILED\n\n" +
                        "Invalid username or password.\n\n" +
                        "Please check your credentials and try again.\n" +
                        "If you don't have an account, please register.",
                        "Login Failed", 
                        JOptionPane.WARNING_MESSAGE);
                    passField.setText("");
                }
            }
        });
        buttonPanel.add(loginBtn);

     // FIND THIS CODE in LoginScreen (around line 150-200):
        JButton registerBtn = createStyledButton("Register", new Color(150, 150, 150));
        registerBtn.addActionListener(e -> app.showScreen("REGISTER"));
        buttonPanel.add(registerBtn);

        gbc.gridy = 6;
        gbc.insets = new Insets(20, 40, 20, 40);
        formPanel.add(buttonPanel, gbc);

        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setBackground(BACKGROUND);
        wrapperPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapperPanel.add(formPanel);
        
        add(wrapperPanel, BorderLayout.CENTER);
    }

    private JTextField createStyledTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Helvetica", Font.PLAIN, 12));
        tf.setBorder(new RoundedBorder(ACCENT, 8));
        tf.setPreferredSize(new Dimension(0, 40));
        tf.setBackground(new Color(30, 50, 70));
        tf.setForeground(new Color(224, 224, 224));
        tf.setCaretColor(ACCENT);
        return tf;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(new Font("Helvetica", Font.PLAIN, 12));
        pf.setBorder(new RoundedBorder(ACCENT, 8));
        pf.setPreferredSize(new Dimension(0, 40));
        pf.setBackground(new Color(30, 50, 70));
        pf.setForeground(new Color(224, 224, 224));
        pf.setCaretColor(ACCENT);
        return pf;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Helvetica", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(color, 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
                btn.setBorder(new LineBorder(color.darker(), 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
                btn.setBorder(new LineBorder(color, 2));
            }
        });
        return btn;
    }
}

//============= RegisterScreen =============

@SuppressWarnings("serial")
class RegisterScreen extends JPanel {
    private ReLifeApp app;
    private static final Color PRIMARY = new Color(10, 38, 71);
    private static final Color ACCENT = new Color(0, 194, 203);
    private static final Color BACKGROUND = new Color(8, 24, 38);
    private static final Color LIGHT_BG = new Color(20, 40, 60);

    public RegisterScreen(ReLifeApp app) {
        this.app = app;
        setBackground(BACKGROUND);
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel header = new JLabel("Create Your ReLife Account");
        header.setFont(new Font("Helvetica", Font.BOLD, 32));
        header.setForeground(ACCENT);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(LIGHT_BG);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(new LineBorder(PRIMARY, 2));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 40, 12, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JTextField nameField = addFormField(formPanel, gbc, "Full Name:", 0);
        JTextField emailField = addFormField(formPanel, gbc, "Email:", 2);
        JTextField phoneField = addFormField(formPanel, gbc, "Phone:", 4);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(new Font("Helvetica", Font.BOLD, 13));
        genderLabel.setForeground(ACCENT);
        gbc.gridy = 6;
        gbc.insets = new Insets(12, 40, 5, 40);
        formPanel.add(genderLabel, gbc);

        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Select", "Male", "Female", "Other"});
        genderBox.setFont(new Font("Helvetica", Font.PLAIN, 12));
        genderBox.setBackground(new Color(30, 50, 70));
        genderBox.setForeground(new Color(224, 224, 224));
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 40, 12, 40);
        formPanel.add(genderBox, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Helvetica", Font.BOLD, 13));
        passLabel.setForeground(ACCENT);
        gbc.gridy = 8;
        gbc.insets = new Insets(12, 40, 5, 40);
        formPanel.add(passLabel, gbc);

        JPasswordField passField = createStyledPasswordField();
        gbc.gridy = 9;
        gbc.insets = new Insets(0, 40, 12, 40);
        formPanel.add(passField, gbc);

        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setFont(new Font("Helvetica", Font.BOLD, 13));
        confirmLabel.setForeground(ACCENT);
        gbc.gridy = 10;
        gbc.insets = new Insets(12, 40, 5, 40);
        formPanel.add(confirmLabel, gbc);

        JPasswordField confirmField = createStyledPasswordField();
        gbc.gridy = 11;
        gbc.insets = new Insets(0, 40, 12, 40);
        formPanel.add(confirmField, gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setBackground(LIGHT_BG);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        JButton registerBtn = createStyledButton("Create Account", ACCENT);
        registerBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String gender = (String) genderBox.getSelectedItem();
            String password = new String(passField.getPassword());
            String confirmPassword = new String(confirmField.getPassword());

            String errorMessage = validateRegistration(name, email, phone, gender, password, confirmPassword);
            
            if (errorMessage.isEmpty()) {
                if (DatabaseHelper.registerUser(name, email, phone, gender, password)) {
                    JOptionPane.showMessageDialog(this, "✅ Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    nameField.setText("");
                    emailField.setText("");
                    phoneField.setText("");
                    genderBox.setSelectedIndex(0);
                    passField.setText("");
                    confirmField.setText("");
                    app.showScreen("LOGIN");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Registration failed!\nUsername or email already exists.", "Error", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, errorMessage, "Validation Error", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPanel.add(registerBtn);

        JButton backBtn = createStyledButton("Back to Login", new Color(150, 150, 150));
        backBtn.addActionListener(e -> app.showScreen("LOGIN"));
        buttonPanel.add(backBtn);

        gbc.gridy = 12;
        gbc.insets = new Insets(20, 40, 15, 40);
        formPanel.add(buttonPanel, gbc);

        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setBackground(BACKGROUND);
        wrapperPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapperPanel.add(formPanel);
        
        add(wrapperPanel, BorderLayout.CENTER);
    }

    private String validateRegistration(String name, String email, String phone, String gender, String password, String confirmPassword) {
        if (name.isEmpty()) return "❌ Full Name is required!";
        if (email.isEmpty()) return "❌ Email is required!";
        if (phone.isEmpty()) return "❌ Phone Number is required!";
        if (gender.equals("Select")) return "❌ Please select a Gender!";
        if (password.isEmpty()) return "❌ Password is required!";
        if (confirmPassword.isEmpty()) return "❌ Confirm Password is required!";
        
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            return "❌ Full Name should contain only letters and spaces!";
        }
        
        if (!isValidEmail(email)) {
            return "❌ Invalid Email Address!";
        }
        
        if (!isValidPhoneNumber(phone)) {
            return "❌ Invalid Phone Number!\nPhone should be 10 digits and contain only numbers.";
        }
        
        if (password.length() < 6) {
            return "❌ Password must be at least 6 characters long!";
        }
        
        if (!password.equals(confirmPassword)) {
            return "❌ Passwords do not match!";
        }
        
        return "";
    }

    private boolean isValidEmail(String email) {
        if (!email.contains("@")) return false;
        
        String[] validExtensions = {
            "@gmail.com", "@yahoo.com", "@outlook.com", "@hotmail.com", 
            "@edu.in", "@student.in", "@ac.in", "@org.in", "@co.in"
        };
        
        for (String ext : validExtensions) {
            if (email.endsWith(ext)) {
                String[] parts = email.split("@");
                if (parts.length == 2 && !parts[0].isEmpty() && parts[0].length() > 2) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("^[0-9]{10}$");
    }

    private JTextField addFormField(JPanel panel, GridBagConstraints gbc, String labelText, int gridy) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Helvetica", Font.BOLD, 13));
        label.setForeground(ACCENT);
        gbc.gridy = gridy;
        gbc.insets = new Insets(12, 40, 5, 40);
        panel.add(label, gbc);

        JTextField field = createStyledTextField();
        gbc.gridy = gridy + 1;
        gbc.insets = new Insets(0, 40, 12, 40);
        panel.add(field, gbc);
        return field;
    }

    private JTextField createStyledTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Helvetica", Font.PLAIN, 12));
        tf.setBorder(new RoundedBorder(ACCENT, 8));
        tf.setPreferredSize(new Dimension(0, 38));
        tf.setBackground(new Color(30, 50, 70));
        tf.setForeground(new Color(224, 224, 224));
        tf.setCaretColor(ACCENT);
        return tf;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(new Font("Helvetica", Font.PLAIN, 12));
        pf.setBorder(new RoundedBorder(ACCENT, 8));
        pf.setPreferredSize(new Dimension(0, 38));
        pf.setBackground(new Color(30, 50, 70));
        pf.setForeground(new Color(224, 224, 224));
        pf.setCaretColor(ACCENT);
        return pf;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Helvetica", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(color, 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
                btn.setBorder(new LineBorder(color.darker(), 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
                btn.setBorder(new LineBorder(color, 2));
            }
        });
        return btn;
    }
}

// ============= MODEL CLASSES =============

class Resource {
    private String title;
    private String description;
    private String category;
    private String condition;
    private String location;
    private String imagePath;

    public Resource(String title, String description, String category, String condition, String location) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.condition = condition;
        this.location = location;
        this.imagePath = "";
    }

    public String getTitle() { 
        return title; 
    }

    public String getDescription() { 
        return description; 
    }

    public String getCategory() { 
        return category; 
    }

    public String getCondition() { 
        return condition; 
    }

    public String getLocation() { 
        return location; 
    }

    public String getImagePath() { 
        return imagePath; 
    }
}