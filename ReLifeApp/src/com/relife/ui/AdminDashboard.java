package com.relife.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.relife.db.DatabaseHelper;

public class AdminDashboard extends JFrame {
    private JTable userTable;
    private JTabbedPane adminTabs;

    public AdminDashboard() {
        setTitle("ReLife Admin Dashboard 👨‍💼");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // 🟣 Header
        JLabel header = new JLabel("ReLife Admin Panel", SwingConstants.CENTER);
        header.setFont(new Font("Helvetica", Font.BOLD, 24));
        header.setOpaque(true);
        header.setBackground(new Color(38, 0, 77));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(1200, 60));
        add(header, BorderLayout.NORTH);

        // 🟠 Tabs
        adminTabs = new JTabbedPane();
        adminTabs.setFont(new Font("Helvetica", Font.BOLD, 13));

        adminTabs.addTab("👥 View All Users", createUsersPanelWithDelete());
        adminTabs.addTab("🎓 View All Qualifications", createQualificationsPanel());
        adminTabs.addTab("💾 Saved Jobs", createSavedJobsPanel());
        adminTabs.addTab("📄 Applied Jobs", createAppliedJobsPanel());
        adminTabs.addTab("🔎 Search User", createSearchUserPanel());


        add(adminTabs, BorderLayout.CENTER);

        // 🔹 Footer Buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton btnLogout = new JButton("🚪 Logout");
        btnLogout.setFont(new Font("Helvetica", Font.BOLD, 13));
        btnLogout.setBackground(new Color(200, 50, 50));
        btnLogout.setForeground(Color.WHITE);
        controlPanel.add(btnLogout);
        add(controlPanel, BorderLayout.SOUTH);

        btnLogout.addActionListener(e -> {
            new AdminLogin().setVisible(true);
            dispose();
        });
    }

    // ============================================================================
    // USERS PANEL WITH DELETE FUNCTIONALITY
    // ============================================================================
    
    private JPanel createUsersPanelWithDelete() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        String[] columns = {"User ID", "Username", "Email", "Phone", "Gender", "Created At"};
        String[][] data = DatabaseHelper.getAllUsers();
        
        userTable = new JTable(data, columns);
        styleTable(userTable);
        
        // ✅ ADD DELETE BUTTON
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));
        
        JButton deleteBtn = new JButton("🗑️ Delete Selected User");
        deleteBtn.setFont(new Font("Helvetica", Font.BOLD, 12));
        deleteBtn.setBackground(new Color(220, 50, 50));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> deleteSelectedUser());
        buttonPanel.add(deleteBtn);
        
        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setFont(new Font("Helvetica", Font.BOLD, 12));
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.addActionListener(e -> refreshUsersTable());
        buttonPanel.add(refreshBtn);
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(userTable), BorderLayout.CENTER);
        
        return panel;
    }

    // ============================================================================
    // DELETE USER METHOD
    // ============================================================================
    
    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "❌ Please select a user to delete.", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = Integer.parseInt(userTable.getValueAt(selectedRow, 0).toString());
        String username = userTable.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this, 
            "⚠️ Are you sure you want to DELETE user:\n\n" +
            "Username: " + username + "\n" +
            "User ID: " + userId + "\n\n" +
            "This action CANNOT be undone!\n" +
            "The user will be unable to login.",
            "Confirm User Deletion", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // ✅ SOFT DELETE USER
            if (DatabaseHelper.softDeleteUser(userId)) {
                JOptionPane.showMessageDialog(this, 
                    "✅ User '" + username + "' has been successfully deleted!\n\n" +
                    "• Account status: DELETED\n" +
                    "• Login access: REVOKED\n" +
                    "• Data: Preserved in database",
                    "User Deleted", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                refreshUsersTable();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Failed to delete user. Please try again.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ============================================================================
    // REFRESH USERS TABLE
    // ============================================================================
    
    private void refreshUsersTable() {
        String[] columns = {"User ID", "Username", "Email", "Phone", "Gender", "Created At"};
        String[][] data = DatabaseHelper.getAllUsers();
        
        DefaultTableModel model = new DefaultTableModel(data, columns);
        userTable.setModel(model);
        styleTable(userTable);
        
        System.out.println("✅ Users table refreshed");
    }

    // ============================================================================
    // OTHER PANELS (Keep existing code)
    // ============================================================================

    private JPanel createQualificationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"Username", "Degree", "Field of Study", "Institution", "Graduation Year", "Created At"};
        String[][] data = DatabaseHelper.getAllUserQualifications();
        JTable table = new JTable(data, columns);
        styleTable(table);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSavedJobsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] columnNames = {
            "User ID", "Name", "Email", "Phone", 
            "Job Title", "Company", "Location", "Salary", "Type", "Posted Date"
        };

        java.util.List<String[]> data = DatabaseHelper.getAllSavedJobs();
        String[][] tableData = data.toArray(new String[0][columnNames.length]);

        JTable table = new JTable(tableData, columnNames);
        styleTable(table);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAppliedJobsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] columnNames = {
            "User ID", "Name", "Email", "Phone", 
            "Job Title", "Company", "Status", "Applied At"
        };

        java.util.List<String[]> data = DatabaseHelper.getAllAppliedJobs();
        String[][] tableData = data.toArray(new String[0][columnNames.length]);

        JTable table = new JTable(tableData, columnNames);
        styleTable(table);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setFont(new Font("Helvetica", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(220, 220, 220));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true));
    }
    private JPanel createSearchUserPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        JPanel searchArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchArea.setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Search by:");
        lbl.setFont(new Font("Helvetica", Font.BOLD, 13));
        searchArea.add(lbl);

        JComboBox<String> searchType = new JComboBox<>(new String[]{"User ID", "Username"});
        searchType.setFont(new Font("Helvetica", Font.PLAIN, 13));
        searchArea.add(searchType);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 28));
        searchField.setFont(new Font("Helvetica", Font.PLAIN, 13));
        searchArea.add(searchField);

        JButton searchBtn = new JButton("🔍 Search");
        searchBtn.setFont(new Font("Helvetica", Font.BOLD, 13));
        searchArea.add(searchBtn);

        panel.add(searchArea, BorderLayout.NORTH);

        // TABLE
        String[] columns = {"User ID", "Username", "Email", "Phone", "Gender", "Created At"};
        DefaultTableModel searchModel = new DefaultTableModel(columns, 0);
        JTable searchTable = new JTable(searchModel);
        styleTable(searchTable);

        JScrollPane scroll = new JScrollPane(searchTable);
        panel.add(scroll, BorderLayout.CENTER);

        // ✅ Search Action
        searchBtn.addActionListener(e -> {
            String mode = (String) searchType.getSelectedItem();
            String value = searchField.getText().trim();

            if (value.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please enter a value to search.");
                return;
            }

            String[][] result = null;

            if (mode.equals("User ID")) {
                try {
                    int id = Integer.parseInt(value);
                    result = DatabaseHelper.searchUserById(id);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "User ID must be a number.");
                    return;
                }
            } else {
                result = DatabaseHelper.searchUserByName(value);
            }

            if (result == null || result.length == 0) {
                JOptionPane.showMessageDialog(panel, "No user found.");
            } else {
                searchModel.setDataVector(result, columns);
            }
        });

        return panel;
    }

}