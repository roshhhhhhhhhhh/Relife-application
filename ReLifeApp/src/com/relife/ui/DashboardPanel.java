package com.relife.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import com.relife.db.DatabaseHelper;
import com.relife.model.User;

@SuppressWarnings("serial")
public class DashboardPanel extends JPanel {
    private ReLifeApp app;
    private JPanel contentPanel;
    private CardLayout contentLayout;
    private JPanel jobCardsContainerPanel;
    private JTextField globalSearchField;
    private JTextField globalLocationField;
    private JComboBox<String> jobTypeFilter;
    private JComboBox<String> experienceFilter;
    private JComboBox<String> salaryFilter;
    private JComboBox<String> industryFilter;
    private List<String[]> savedJobs = new ArrayList<>();
    
    private String[][] allJobs = {
        {"Senior Java Developer", "Google India", "Bangalore, Karnataka", "₹15-25 LPA", "Full-time", "2 days ago", "Technology", "Senior Level"},
        {"Frontend Developer", "Amazon", "Hyderabad, Telangana", "₹12-20 LPA", "Full-time", "1 week ago", "Technology", "Mid Level"},
        {"Data Scientist", "Microsoft", "Pune, Maharashtra", "₹18-30 LPA", "Full-time", "3 days ago", "Technology", "Senior Level"},
        {"UI/UX Designer", "Flipkart", "Bangalore, Karnataka", "₹10-18 LPA", "Full-time", "5 days ago", "Technology", "Mid Level"},
        {"DevOps Engineer", "Infosys", "Chennai, Tamil Nadu", "₹8-15 LPA", "Contract", "1 day ago", "Technology", "Mid Level"},
        {"Product Manager", "TCS", "Mumbai, Maharashtra", "₹20-35 LPA", "Full-time", "2 weeks ago", "Technology", "Senior Level"},
        {"Backend Developer", "Wipro", "Noida, Uttar Pradesh", "₹10-16 LPA", "Full-time", "4 days ago", "Technology", "Mid Level"},
        {"Mobile App Developer", "Paytm", "Gurgaon, Haryana", "₹12-22 LPA", "Full-time", "1 week ago", "Technology", "Mid Level"},
        {"Junior Developer", "Startup Inc", "Bangalore, Karnataka", "₹3-6 LPA", "Internship", "1 day ago", "Technology", "Entry Level"},
        {"Sales Manager", "HDFC Bank", "Mumbai, Maharashtra", "₹8-12 LPA", "Full-time", "3 days ago", "Finance", "Mid Level"},
        {"Financial Analyst", "ICICI Bank", "Bangalore, Karnataka", "₹6-10 LPA", "Full-time", "5 days ago", "Finance", "Entry Level"},
        {"Marketing Executive", "HUL", "Mumbai, Maharashtra", "₹5-8 LPA", "Full-time", "1 week ago", "Marketing", "Entry Level"},
        {"Content Writer", "Zomato", "Bangalore, Karnataka", "₹4-7 LPA", "Freelance", "2 days ago", "Marketing", "Entry Level"},
        {"HR Manager", "Reliance", "Mumbai, Maharashtra", "₹12-18 LPA", "Full-time", "1 week ago", "Finance", "Senior Level"},
        {"Business Analyst", "Accenture", "Pune, Maharashtra", "₹10-15 LPA", "Full-time", "4 days ago", "Technology", "Mid Level"}
    };

    public DashboardPanel(ReLifeApp app) {
        this.app = app;
        System.out.println("DEBUG DASHBOARD: Current user = " + app.getCurrentUser());
        setBackground(new Color(38, 0, 77));
        setLayout(new BorderLayout(0, 0));
        add(createHeaderPanel(), BorderLayout.NORTH);

        JPanel mainContent = new JPanel(new BorderLayout(15, 0));
        mainContent.setBackground(new Color(38, 0, 77));
        mainContent.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainContent.add(createSidebar(), BorderLayout.WEST);

        contentLayout = new CardLayout();
        contentPanel = new JPanel(contentLayout) {
            @Override
            public void doLayout() {
                super.doLayout();
                // Refresh profile panel every time it's shown
                CardLayout layout = (CardLayout) getLayout();
                Component[] components = getComponents();
                for (Component comp : components) {
                    if (comp instanceof JPanel) {
                        try {
                            // Check if this is the profile panel by checking if we're showing it
                            int index = 0;
                            for (Component c : getComponents()) {
                                if (c == comp) break;
                                index++;
                            }
                            // If this might be profile panel (index 6), optionally refresh
                        } catch (Exception e) {
                            // Ignore
                        }
                    }
                }
            }
        };
        contentPanel.setBackground(new Color(245, 245, 245));



        contentPanel.add(createHomePanel(), "HOME");
        contentPanel.add(createAddDonationPanel(), "RENEWBOX");
        contentPanel.add(createFindResourcesPanel(), "RESOURCES");
        contentPanel.add(createSearchJobsPanel(), "JOBS");
        contentPanel.add(createSavedJobsPanel(), "SAVED_JOBS");
        contentPanel.add(createMentorshipPanel(), "MENTORSHIP");
        contentPanel.add(createProfilePanel(), "PROFILE");

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(new Color(245, 245, 245));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainContent.add(scrollPane, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);
        contentLayout.show(contentPanel, "HOME");
    }

    private Component createSavedJobsPanel() {
        JPanel savedJobsPanel = new JPanel(new BorderLayout(15, 15));
        savedJobsPanel.setBackground(new Color(245, 245, 245));
        savedJobsPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(245, 245, 245));
        JLabel title = new JLabel("⭐ Saved Jobs");
        title.setFont(new Font("Helvetica", Font.BOLD, 24));
        header.add(title, BorderLayout.WEST);
        JButton backBtn = createButton("← Back", new Color(100, 100, 100));
        backBtn.addActionListener(e -> contentLayout.show(contentPanel, "HOME"));
        backBtn.setMaximumSize(new Dimension(100, 40));
        header.add(backBtn, BorderLayout.EAST);
        savedJobsPanel.add(header, BorderLayout.NORTH);

        JPanel savedJobsContainer = new JPanel();
        savedJobsContainer.setLayout(new BoxLayout(savedJobsContainer, BoxLayout.Y_AXIS));
        savedJobsContainer.setBackground(new Color(245, 245, 245));
        
        JLabel loadingLabel = new JLabel("Loading...");
        loadingLabel.setFont(new Font("Helvetica", Font.PLAIN, 14));
        loadingLabel.setForeground(new Color(100, 100, 100));
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        savedJobsContainer.add(Box.createVerticalGlue());
        savedJobsContainer.add(loadingLabel);
        savedJobsContainer.add(Box.createVerticalGlue());
        
        JScrollPane scrollPane = new JScrollPane(savedJobsContainer);
        scrollPane.setBackground(new Color(245, 245, 245));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        savedJobsPanel.add(scrollPane, BorderLayout.CENTER);
        
        return savedJobsPanel;
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout(25, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Component[] comps = getComponents();
                for (Component comp : comps) {
                    if (comp instanceof JLabel) {
                        JLabel label = (JLabel) comp;
                        if (label.getText() != null && label.getText().contains("Welcome back")) {
                            String username = "User";
                            if (app.getCurrentUser() != null) {
                                String userName = app.getCurrentUser().getName();
                                if (userName != null && !userName.trim().isEmpty()) {
                                    username = userName.trim();
                                }
                            }
                            label.setText("Welcome back, " + username + " 👋");
                            break;
                        }
                    }
                }
            }
        };
        
        header.setBackground(new Color(38, 0, 77));
        header.setBorder(new EmptyBorder(15, 25, 15, 25));
        header.setPreferredSize(new Dimension(0, 80));

        JLabel logo = new JLabel("🌱 ReLife");
        logo.setFont(new Font("Helvetica", Font.BOLD, 22));
        logo.setForeground(new Color(0, 102, 204));
        header.add(logo, BorderLayout.WEST);

        String username = "User";
        if (app.getCurrentUser() != null) {
            String userName = app.getCurrentUser().getName();
            if (userName != null && !userName.trim().isEmpty()) {
                username = userName.trim();
            }
        }
        
        System.out.println("DEBUG HEADER: Current username = " + username);
        
        JLabel welcome = new JLabel("Welcome back, " + username + " 👋");
        welcome.setFont(new Font("Helvetica", Font.PLAIN, 16));
        welcome.setForeground(new Color(235, 235, 245));
        welcome.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(welcome, BorderLayout.CENTER);

        String datetime = new SimpleDateFormat("MMM dd, yyyy | HH:mm").format(new Date());
        JLabel dateTime = new JLabel(datetime);
        dateTime.setFont(new Font("Helvetica", Font.PLAIN, 12));
        dateTime.setForeground(new Color(235, 235, 245));
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(new Color(38, 0, 77));
        rightPanel.add(dateTime);

        JButton logoutBtn = new JButton("🚪 Logout");
        logoutBtn.setFont(new Font("Helvetica", Font.BOLD, 11));
        logoutBtn.setBackground(new Color(200, 50, 50));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(new EmptyBorder(6, 12, 6, 12));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                null, 
                "Are you sure you want to logout?", 
                "Logout", 
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                app.setCurrentUser(null);
                app.showScreen("LOGIN");
            }
        });
        rightPanel.add(logoutBtn);

        header.add(rightPanel, BorderLayout.EAST);
        return header;
    }
       
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        sidebar.setPreferredSize(new Dimension(220, 0));

        JLabel catTitle = new JLabel("Categories");
        catTitle.setFont(new Font("Helvetica", Font.BOLD, 14));
        catTitle.setBorder(new EmptyBorder(15, 15, 15, 15));
        sidebar.add(catTitle);

        String[] categories = {"All Items", "Electronics", "Furniture", "Books", "Clothing", "Mentorship", "Housing"};
        for (String cat : categories) {
            JButton btn = new JButton(cat);
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(0, 102, 204));
            btn.setFocusPainted(false);
            btn.setBorder(new EmptyBorder(8, 15, 8, 15));
            btn.setBorderPainted(false);
            btn.setFont(new Font("Helvetica", Font.PLAIN, 12));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            sidebar.add(btn);
        }

        JButton jobsBtn = new JButton("💼 Search Jobs");
        jobsBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        jobsBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        jobsBtn.setBackground(Color.WHITE);
        jobsBtn.setForeground(new Color(0, 150, 100));
        jobsBtn.setFocusPainted(false);
        jobsBtn.setBorder(new EmptyBorder(8, 15, 8, 15));
        jobsBtn.setBorderPainted(false);
        jobsBtn.setFont(new Font("Helvetica", Font.BOLD, 12));
        jobsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jobsBtn.addActionListener(e -> contentLayout.show(contentPanel, "JOBS"));
        sidebar.add(jobsBtn);
        
        JButton savedJobsBtn = new JButton("⭐ Saved Jobs");
        savedJobsBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        savedJobsBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        savedJobsBtn.setBackground(Color.WHITE);
        savedJobsBtn.setForeground(new Color(255, 165, 0));
        savedJobsBtn.setFocusPainted(false);
        savedJobsBtn.setBorder(new EmptyBorder(8, 15, 8, 15));
        savedJobsBtn.setBorderPainted(false);
        savedJobsBtn.setFont(new Font("Helvetica", Font.BOLD, 12));
        savedJobsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        savedJobsBtn.addActionListener(e -> {
            refreshSavedJobs();
            contentLayout.show(contentPanel, "SAVED_JOBS");
        });
        sidebar.add(savedJobsBtn);
        
        JButton profileBtn = new JButton("👤 Profile");
        profileBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        profileBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        profileBtn.setBackground(Color.WHITE);
        profileBtn.setForeground(new Color(0, 102, 204));
        profileBtn.setFocusPainted(false);
        profileBtn.setBorder(new EmptyBorder(8, 15, 8, 15));
        profileBtn.setBorderPainted(false);
        profileBtn.setFont(new Font("Helvetica", Font.BOLD, 12));
        profileBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        profileBtn.addActionListener(e -> {
            System.out.println("DEBUG: Profile button clicked");
            refreshProfilePanel(); // ✅ Call refresh instead of just showing
        });
        sidebar.add(profileBtn);
        
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(new JSeparator());
        sidebar.add(Box.createVerticalStrut(15));

        JButton aboutBtn = new JButton("ℹ️ About ReLife");
        aboutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        aboutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        aboutBtn.setBackground(new Color(240, 248, 255));
        aboutBtn.setForeground(new Color(0, 102, 204));
        aboutBtn.setFocusPainted(false);
        aboutBtn.setBorder(new LineBorder(new Color(0, 102, 204), 2));
        aboutBtn.setFont(new Font("Helvetica", Font.BOLD, 12));
        aboutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        aboutBtn.addActionListener(e -> JOptionPane.showMessageDialog(sidebar, "🌱 About ReLife\n\nReLife is a Digital Second Chance Platform:\n\n✓ Donate unused items\n✓ Find job opportunities\n✓ Connect with mentors\n✓ Build communities\n\nJoin us in making a difference! 💚", "About ReLife", JOptionPane.INFORMATION_MESSAGE));
        sidebar.add(aboutBtn);
        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }
    private JPanel createHomePanel() {
        System.out.println("\n========== createHomePanel() called ==========");
        
        JPanel homePanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Refresh activity dashboard every time panel is painted
                // This ensures it updates when user logs in
            }
        };
        
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setBackground(new Color(245, 245, 245));
        homePanel.setBorder(new EmptyBorder(40, 60, 40, 60));

        JLabel title = new JLabel("Empower Lives with ReLife");
        title.setFont(new Font("Helvetica", Font.BOLD, 36));
        title.setForeground(new Color(0, 102, 204));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        homePanel.add(title);
        homePanel.add(Box.createVerticalStrut(15));

        JLabel subtitle = new JLabel("Donate items, share skills, find opportunities");
        subtitle.setFont(new Font("Helvetica", Font.PLAIN, 17));
        subtitle.setForeground(new Color(100, 100, 100));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        homePanel.add(subtitle);
        homePanel.add(Box.createVerticalStrut(30));

        // ✅ ACTIVITY DASHBOARD - Created dynamically each time
        homePanel.add(createActivityDashboardPanel());
        homePanel.add(Box.createVerticalStrut(30));

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(1, 3, 30, 0));
        btnPanel.setBackground(new Color(245, 245, 245));
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        btnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel btn1 = createHomeActionCard("🧺", "Add Products", "Share items");
        btn1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { contentLayout.show(contentPanel, "RENEWBOX"); }
        });
        btnPanel.add(btn1);

        JPanel btn2 = createHomeActionCard("🔍", "Find Resources", "Browse items");
        btn2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { contentLayout.show(contentPanel, "RESOURCES"); }
        });
        btnPanel.add(btn2);

        JPanel btn3 = createHomeActionCard("👥", "Find Mentors", "Connect experts");
        btn3.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { contentLayout.show(contentPanel, "MENTORSHIP"); }
        });
        btnPanel.add(btn3);

        homePanel.add(btnPanel);
        homePanel.add(Box.createVerticalGlue());
        
        System.out.println("========== createHomePanel() DONE ==========\n");
        return homePanel;
    }
    private JPanel createHomeActionCard(String emoji, String cardTitle, String desc) {
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.setColor(new Color(220, 220, 220));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setBorder(new EmptyBorder(25, 20, 25, 20));

        JLabel emoLabel = new JLabel(emoji);
        emoLabel.setFont(new Font("Helvetica", Font.PLAIN, 50));
        emoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(emoLabel);
        card.add(Box.createVerticalStrut(15));

        JLabel titleLabel = new JLabel(cardTitle);
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(0, 102, 204));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));

        JLabel descLabel = new JLabel("<html><center>" + desc + "</center></html>");
        descLabel.setFont(new Font("Helvetica", Font.PLAIN, 12));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setForeground(new Color(120, 120, 120));
        card.add(descLabel);

        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setOpaque(true);
                card.setBackground(new Color(245, 250, 255));
                card.repaint();
            }
            public void mouseExited(MouseEvent e) {
                card.setOpaque(false);
                card.repaint();
            }
        });
        return card;
    }

    private JPanel createAddDonationPanel() {
        JPanel donationPanel = new JPanel();
        donationPanel.setLayout(new BoxLayout(donationPanel, BoxLayout.Y_AXIS));
        donationPanel.setBackground(new Color(245, 245, 245));
        donationPanel.setBorder(new EmptyBorder(30, 150, 30, 150));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(245, 245, 245));
        JLabel title = new JLabel("🧺 Add Products");
        title.setFont(new Font("Helvetica", Font.BOLD, 26));
        title.setForeground(new Color(0, 102, 204));
        header.add(title, BorderLayout.WEST);
        JButton backBtn = createButton("← Back", new Color(100, 100, 100));
        backBtn.addActionListener(e -> contentLayout.show(contentPanel, "HOME"));
        backBtn.setMaximumSize(new Dimension(100, 40));
        header.add(backBtn, BorderLayout.EAST);
        donationPanel.add(header);
        donationPanel.add(Box.createVerticalStrut(20));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 25, 12, 25);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        addFormRow(form, gbc, "Item Name:", new JTextField(), 0);
        addFormRow(form, gbc, "Category:", new JComboBox<>(new String[]{"Electronics", "Furniture", "Books", "Clothing"}), 2);
        addFormRow(form, gbc, "Condition:", new JComboBox<>(new String[]{"Excellent", "Good", "Fair"}), 4);
        addFormRow(form, gbc, "Description:", createTextArea(), 6);
        addFormRow(form, gbc, "Location:", new JTextField(), 8);

        gbc.gridy = 10;
        form.add(createButton("📷 Upload Image", new Color(100, 100, 100)), gbc);

        JPanel btns = new JPanel(new GridLayout(1, 2, 15, 0));
        btns.setBackground(Color.WHITE);
        JButton submit = createButton("Add Products", new Color(0, 150, 100));
        submit.addActionListener(e -> JOptionPane.showMessageDialog(donationPanel, "Products posted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE));
        btns.add(submit);
        JButton cancel = createButton("Cancel", new Color(200, 50, 50));
        cancel.addActionListener(e -> contentLayout.show(contentPanel, "HOME"));
        btns.add(cancel);
        gbc.gridy = 11;
        form.add(btns, gbc);

        donationPanel.add(form);
        return donationPanel;
    }

    private JPanel createFindResourcesPanel() {
        JPanel resourcePanel = new JPanel(new BorderLayout(15, 15));
        resourcePanel.setBackground(new Color(245, 245, 245));
        resourcePanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(245, 245, 245));
        JLabel title = new JLabel("🔍 Find Resources");
        title.setFont(new Font("Helvetica", Font.BOLD, 24));
        header.add(title, BorderLayout.WEST);
        JButton backBtn = createButton("← Back", new Color(100, 100, 100));
        backBtn.addActionListener(e -> contentLayout.show(contentPanel, "HOME"));
        backBtn.setMaximumSize(new Dimension(100, 40));
        header.add(backBtn, BorderLayout.EAST);
        resourcePanel.add(header, BorderLayout.NORTH);

        String[] cols = {"Title", "Category", "Condition", "Location"};
        Object[][] data = {{"Dell Laptop", "Electronics", "Excellent", "Delhi"}, {"Office Chair", "Furniture", "Good", "Bangalore"}, {"Java Book", "Books", "Excellent", "Mumbai"}, {"Winter Jacket", "Clothing", "Good", "Hyderabad"}, {"Apartment", "Housing", "Furnished", "Pune"}};

        JTable table = new JTable(data, cols);
        table.setFont(new Font("Helvetica", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setForeground(Color.WHITE);
        resourcePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        return resourcePanel;
    }

    private JPanel createSearchJobsPanel() {
        JPanel jobsPanel = new JPanel(new BorderLayout(0, 0));
        jobsPanel.setBackground(new Color(243, 242, 239));
        jobsPanel.add(createJobSearchBar(), BorderLayout.NORTH);
        
        JPanel mainContent = new JPanel(new BorderLayout(15, 10));
        mainContent.setBackground(new Color(243, 242, 239));
        mainContent.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel filterPanel = createJobFilters();
        filterPanel.setPreferredSize(new Dimension(220, 500));
        mainContent.add(filterPanel, BorderLayout.WEST);
        
        jobCardsContainerPanel = new JPanel();
        jobCardsContainerPanel.setLayout(new BoxLayout(jobCardsContainerPanel, BoxLayout.Y_AXIS));
        jobCardsContainerPanel.setBackground(new Color(243, 242, 239));
        jobCardsContainerPanel.setBorder(new EmptyBorder(0, 0, 0, 10));
        loadJobCards(allJobs);
        
        JScrollPane scrollPane = new JScrollPane(jobCardsContainerPanel);
        scrollPane.setBackground(new Color(243, 242, 239));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainContent.add(scrollPane, BorderLayout.CENTER);
        jobsPanel.add(mainContent, BorderLayout.CENTER);
        return jobsPanel;
    }
    
    private void loadJobCards(String[][] jobs) {
        jobCardsContainerPanel.removeAll();
        if (jobs.length == 0) {
            JPanel noResultsPanel = new JPanel();
            noResultsPanel.setLayout(new BoxLayout(noResultsPanel, BoxLayout.Y_AXIS));
            noResultsPanel.setBackground(Color.WHITE);
            noResultsPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
            noResultsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
            JLabel noResultsLabel = new JLabel("😔 No jobs found");
            noResultsLabel.setFont(new Font("Helvetica", Font.BOLD, 18));
            noResultsLabel.setForeground(new Color(100, 100, 100));
            noResultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            noResultsPanel.add(noResultsLabel);
            jobCardsContainerPanel.add(noResultsPanel);
        } else {
            JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
            header.setOpaque(false);
            header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            JLabel resultsCount = new JLabel("Found " + jobs.length + " job" + (jobs.length != 1 ? "s" : ""));
            resultsCount.setFont(new Font("Helvetica", Font.BOLD, 14));
            resultsCount.setForeground(new Color(50, 50, 50));
            header.add(resultsCount);
            jobCardsContainerPanel.add(header);
            jobCardsContainerPanel.add(Box.createVerticalStrut(8));
            
            for (String[] job : jobs) {
                jobCardsContainerPanel.add(createJobCardUI(job));
                jobCardsContainerPanel.add(Box.createVerticalStrut(10));
            }
        }
        jobCardsContainerPanel.revalidate();
        jobCardsContainerPanel.repaint();
    }
    
    private void filterJobs() {
        String searchText = globalSearchField.getText().toLowerCase();
        String locationText = globalLocationField.getText().toLowerCase();
        if (searchText.equals("search job titles or keywords...")) searchText = "";
        if (locationText.equals("📍 location")) locationText = "";
        
        List<String[]> filtered = new ArrayList<>();
        for (String[] job : allJobs) {
            boolean match = true;
            if (!searchText.isEmpty() && !job[0].toLowerCase().contains(searchText) && !job[1].toLowerCase().contains(searchText)) match = false;
            if (!locationText.isEmpty() && !job[2].toLowerCase().contains(locationText)) match = false;
            if (!jobTypeFilter.getSelectedItem().equals("All Types") && !job[4].equals(jobTypeFilter.getSelectedItem())) match = false;
            if (!experienceFilter.getSelectedItem().equals("All Levels") && !job[7].equals(experienceFilter.getSelectedItem())) match = false;
            if (!industryFilter.getSelectedItem().equals("All Industries") && !job[6].equals(industryFilter.getSelectedItem())) match = false;
            if (!salaryFilter.getSelectedItem().equals("Any Salary")) {
                String sal = (String) salaryFilter.getSelectedItem();
                String jobSal = job[3];
                boolean salMatch = false;
                if (sal.equals("₹0 - ₹5 LPA") && (jobSal.contains("₹3") || jobSal.contains("₹4") || jobSal.contains("₹5"))) salMatch = true;
                else if (sal.equals("₹5 - ₹10 LPA") && (jobSal.contains("₹6") || jobSal.contains("₹8") || jobSal.contains("₹10"))) salMatch = true;
                else if (sal.equals("₹10 - ₹20 LPA") && (jobSal.contains("₹10") || jobSal.contains("₹12") || jobSal.contains("₹15"))) salMatch = true;
                else if (sal.equals("₹20+ LPA") && (jobSal.contains("₹20") || jobSal.contains("₹25") || jobSal.contains("₹30"))) salMatch = true;
                if (!salMatch) match = false;
            }
            if (match) filtered.add(job);
        }
        loadJobCards(filtered.toArray(new String[0][]));
    }
    
    private JPanel createJobSearchBar() {
        JPanel searchBar = new JPanel(new BorderLayout());
        searchBar.setBackground(Color.WHITE);
        searchBar.setBorder(new MatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        searchBar.setPreferredSize(new Dimension(0, 70));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        leftPanel.setBackground(Color.WHITE);
        
        JButton backBtn = new JButton("← Back");
        backBtn.setFont(new Font("Helvetica", Font.BOLD, 12));
        backBtn.setBackground(new Color(240, 240, 240));
        backBtn.setForeground(new Color(50, 50, 50));
        backBtn.setFocusPainted(false);
        backBtn.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        backBtn.setPreferredSize(new Dimension(80, 38));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> contentLayout.show(contentPanel, "HOME"));
        leftPanel.add(backBtn);
        
        globalSearchField = new JTextField("Search job titles or keywords...");
        globalSearchField.setPreferredSize(new Dimension(280, 38));
        globalSearchField.setFont(new Font("Helvetica", Font.PLAIN, 12));
        globalSearchField.setForeground(new Color(150, 150, 150));
        globalSearchField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(220, 220, 220), 1), new EmptyBorder(8, 12, 8, 12)));
        globalSearchField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (globalSearchField.getText().equals("Search job titles or keywords...")) {
                    globalSearchField.setText("");
                    globalSearchField.setForeground(new Color(50, 50, 50));
                }
            }
            public void focusLost(FocusEvent e) {
                if (globalSearchField.getText().isEmpty()) {
                    globalSearchField.setText("Search job titles or keywords...");
                    globalSearchField.setForeground(new Color(150, 150, 150));
                }
            }
        });
        globalSearchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { filterJobs(); }
        });
        leftPanel.add(globalSearchField);
        
        globalLocationField = new JTextField("📍 Location");
        globalLocationField.setPreferredSize(new Dimension(160, 38));
        globalLocationField.setFont(new Font("Helvetica", Font.PLAIN, 12));
        globalLocationField.setForeground(new Color(150, 150, 150));
        globalLocationField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(220, 220, 220), 1), new EmptyBorder(8, 12, 8, 12)));
        globalLocationField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (globalLocationField.getText().equals("📍 Location")) {
                    globalLocationField.setText("");
                    globalLocationField.setForeground(new Color(50, 50, 50));
                }
            }
            public void focusLost(FocusEvent e) {
                if (globalLocationField.getText().isEmpty()) {
                    globalLocationField.setText("📍 Location");
                    globalLocationField.setForeground(new Color(150, 150, 150));
                }
            }
        });
        globalLocationField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { filterJobs(); }
        });
        leftPanel.add(globalLocationField);
        searchBar.add(leftPanel, BorderLayout.CENTER);
        return searchBar;
    }
    
    private JPanel createJobFilters() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        filterPanel.setPreferredSize(new Dimension(200, 400));
        filterPanel.setMaximumSize(new Dimension(200, Integer.MAX_VALUE));
        filterPanel.setMinimumSize(new Dimension(200, 300));
        
        JLabel filterTitle = new JLabel("🔎 Filters");
        filterTitle.setFont(new Font("Helvetica", Font.BOLD, 14));
        filterTitle.setBorder(new EmptyBorder(15, 15, 10, 15));
        filterTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.add(filterTitle);
        
        JLabel jobTypeLabel = new JLabel("Job Type");
        jobTypeLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
        jobTypeLabel.setBorder(new EmptyBorder(10, 15, 5, 15));
        jobTypeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.add(jobTypeLabel);
        
        jobTypeFilter = new JComboBox<>(new String[]{"All Types", "Full-time", "Contract", "Freelance", "Internship"});
        jobTypeFilter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        jobTypeFilter.setBackground(new Color(245, 245, 245));
        jobTypeFilter.setFont(new Font("Helvetica", Font.PLAIN, 12));
        jobTypeFilter.addActionListener(e -> filterJobs());
        filterPanel.add(jobTypeFilter);
        filterPanel.add(Box.createVerticalStrut(12));
        
        JLabel experienceLabel = new JLabel("Experience");
        experienceLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
        experienceLabel.setBorder(new EmptyBorder(10, 15, 5, 15));
        experienceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.add(experienceLabel);
        
        experienceFilter = new JComboBox<>(new String[]{"All Levels", "Entry Level", "Mid Level", "Senior Level"});
        experienceFilter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        experienceFilter.setBackground(new Color(245, 245, 245));
        experienceFilter.setFont(new Font("Helvetica", Font.PLAIN, 12));
        experienceFilter.addActionListener(e -> filterJobs());
        filterPanel.add(experienceFilter);
        filterPanel.add(Box.createVerticalStrut(12));
        
        JLabel industryLabel = new JLabel("Industry");
        industryLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
        industryLabel.setBorder(new EmptyBorder(10, 15, 5, 15));
        industryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.add(industryLabel);
        
        industryFilter = new JComboBox<>(new String[]{"All Industries", "Technology", "Finance", "Marketing"});
        industryFilter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        industryFilter.setBackground(new Color(245, 245, 245));
        industryFilter.setFont(new Font("Helvetica", Font.PLAIN, 12));
        industryFilter.addActionListener(e -> filterJobs());
        filterPanel.add(industryFilter);
        filterPanel.add(Box.createVerticalStrut(12));
        
        JLabel salaryLabel = new JLabel("Salary");
        salaryLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
        salaryLabel.setBorder(new EmptyBorder(10, 15, 5, 15));
        salaryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.add(salaryLabel);
        
        salaryFilter = new JComboBox<>(new String[]{"Any Salary", "₹0 - ₹5 LPA", "₹5 - ₹10 LPA", "₹10 - ₹20 LPA", "₹20+ LPA"});
        salaryFilter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        salaryFilter.setBackground(new Color(245, 245, 245));
        salaryFilter.setFont(new Font("Helvetica", Font.PLAIN, 12));
        salaryFilter.addActionListener(e -> filterJobs());
        filterPanel.add(salaryFilter);
        filterPanel.add(Box.createVerticalStrut(15));
        
        JButton clearBtn = new JButton("Clear Filters");
        clearBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        clearBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        clearBtn.setBackground(new Color(200, 50, 50));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setFont(new Font("Helvetica", Font.BOLD, 12));
        clearBtn.setFocusPainted(false);
        clearBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearBtn.addActionListener(e -> {
            globalSearchField.setText("Search job titles or keywords...");
            globalLocationField.setText("📍 Location");
            jobTypeFilter.setSelectedItem("All Types");
            experienceFilter.setSelectedItem("All Levels");
            industryFilter.setSelectedItem("All Industries");
            salaryFilter.setSelectedItem("Any Salary");
            filterJobs();
        });
        filterPanel.add(clearBtn);
        filterPanel.add(Box.createVerticalGlue());
        return filterPanel;
    }
    
    private JPanel createJobCardUI(String[] job) {
        final String title = job[0];
        final String company = job[1];
        final String location = job[2];
        final String salary = job[3];
        final String jobType = job[4];
        final String datePosted = job[5];
        
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2d.setColor(new Color(230, 230, 230));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
            }
        };
        card.setLayout(new BorderLayout(10, 0));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(10, 10, 10, 10));
        card.setPreferredSize(new Dimension(0, 130));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(getScaledCompanyLogo(company, 40, 40));
        logoLabel.setPreferredSize(new Dimension(60, 110));
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        logoLabel.setVerticalAlignment(JLabel.TOP);
        card.add(logoLabel, BorderLayout.WEST);
        
        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setOpaque(false);
        details.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        JLabel jobTitle = new JLabel(title);
        jobTitle.setFont(new Font("Helvetica", Font.BOLD, 14));
        jobTitle.setForeground(new Color(0, 102, 204));
        details.add(jobTitle);
        
        JLabel companyLabel = new JLabel(company);
        companyLabel.setFont(new Font("Helvetica", Font.PLAIN, 11));
        companyLabel.setForeground(new Color(100, 100, 100));
        details.add(companyLabel);
        
        JPanel infoRow1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        infoRow1.setOpaque(false);
        infoRow1.add(new JLabel("📍 " + location));
        infoRow1.add(new JLabel("💰 " + salary));
        details.add(infoRow1);
        
        JPanel infoRow2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        infoRow2.setOpaque(false);
        infoRow2.add(new JLabel("📋 " + jobType));
        infoRow2.add(new JLabel("⏰ " + datePosted));
        details.add(infoRow2);
        
        card.add(details, BorderLayout.CENTER);
        
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        buttons.setOpaque(false);
        buttons.setPreferredSize(new Dimension(250, 110));
        
        JButton viewDetailsBtn = createButton("View Details", new Color(100, 150, 200));
        viewDetailsBtn.setPreferredSize(new Dimension(100, 34));
        viewDetailsBtn.setFont(new Font("Helvetica", Font.BOLD, 11));
        viewDetailsBtn.addActionListener(e -> showEnhancedJobDetailsDialog(title, company, location, salary, jobType, datePosted));
        buttons.add(viewDetailsBtn);
        
        JButton saveBtn = createButton("💾 Save", new Color(255, 165, 0));
        saveBtn.setPreferredSize(new Dimension(75, 34));
        saveBtn.setFont(new Font("Helvetica", Font.BOLD, 11));
        saveBtn.addActionListener(e -> {
            int userId = app.getCurrentUser().getUserId();
            
            // 🔴 NEW: SAVE TO DATABASE
            if (DatabaseHelper.saveJob(userId, title, company, location, salary, jobType, datePosted)) {
                saveBtn.setBackground(new Color(0, 150, 100));
                saveBtn.setText("✓ Saved");
                JOptionPane.showMessageDialog(card, "Job saved successfully!", "Saved", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(card, "Job already saved!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttons.add(saveBtn);
        
        JButton applyBtn = createButton("Apply", new Color(0, 102, 204));
        applyBtn.setPreferredSize(new Dimension(75, 34));
        applyBtn.setFont(new Font("Helvetica", Font.BOLD, 11));
        applyBtn.addActionListener(e -> {
            int userId = app.getCurrentUser().getUserId();
            
            // 🔴 NEW: SAVE APPLICATION TO DATABASE
            if (DatabaseHelper.applyForJob(userId, title, company)) {
                JOptionPane.showMessageDialog(card, "Application submitted!\n\nBest of luck! 🍀", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(card, "You have already applied to this job!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttons.add(applyBtn);
        
        card.add(buttons, BorderLayout.EAST);
        
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setOpaque(true);
                card.setBackground(new Color(250, 250, 250));
                card.repaint();
            }
            public void mouseExited(MouseEvent e) {
                card.setOpaque(false);
                card.repaint();
            }
        });
        return card;
    }
    
    private JPanel createSavedJobCard(String[] job) {
        final String title = job[0];
        final String company = job[1];
        final String location = job[2];
        final String salary = job[3];
        final String jobType = job[4];
        final String datePosted = job[5];
        
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2d.setColor(new Color(230, 230, 230));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
            }
        };
        card.setLayout(new BorderLayout(15, 0));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(getScaledCompanyLogo(company, 40, 40));
        logoLabel.setPreferredSize(new Dimension(60, 110));
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        logoLabel.setVerticalAlignment(JLabel.TOP);
        card.add(logoLabel, BorderLayout.WEST);
        
        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setOpaque(false);
        
        JLabel jobTitle = new JLabel(title);
        jobTitle.setFont(new Font("Helvetica", Font.BOLD, 16));
        jobTitle.setForeground(new Color(0, 102, 204));
        details.add(jobTitle);
        
        JLabel companyLabel = new JLabel(company);
        companyLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));
        companyLabel.setForeground(new Color(100, 100, 100));
        details.add(companyLabel);
        
        JPanel infoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        infoRow.setOpaque(false);
        infoRow.add(new JLabel("📍 " + location));
        infoRow.add(new JLabel("💰 " + salary));
        infoRow.add(new JLabel("📋 " + jobType));
        infoRow.add(new JLabel("⏰ " + datePosted));
        details.add(infoRow);
        card.add(details, BorderLayout.CENTER);
        
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        
        JButton removeBtn = createButton("Remove", new Color(200, 50, 50));
        removeBtn.addActionListener(e -> {
            int userId = app.getCurrentUser().getUserId();
             
            if (DatabaseHelper.removeSavedJob(userId, title, company)) {
                JOptionPane.showMessageDialog(card, "Job removed!", "Removed", JOptionPane.INFORMATION_MESSAGE);
                contentLayout.show(contentPanel, "SAVED_JOBS");
            }
        });
        buttons.add(removeBtn);
        
        JButton applyBtn = createButton("Apply", new Color(0, 102, 204));
        applyBtn.addActionListener(e -> JOptionPane.showMessageDialog(card, "Application submitted!\n\nBest of luck! 🍀", "Success", JOptionPane.INFORMATION_MESSAGE));
        buttons.add(applyBtn);
        
        card.add(buttons, BorderLayout.EAST);
        return card;
    }
    
    private void showEnhancedJobDetailsDialog(String title, String company, String location, String salary, String jobType, String datePosted) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Job Details - " + title);
        dialog.setSize(700, 650);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(true);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setPreferredSize(new Dimension(0, 100));
        
        JLabel headerLogoLabel = new JLabel();
        headerLogoLabel.setIcon(getScaledCompanyLogo(company, 60, 60));
        headerLogoLabel.setPreferredSize(new Dimension(80, 80));
        headerPanel.add(headerLogoLabel, BorderLayout.WEST);
        
        JPanel headerTextPanel = new JPanel();
        headerTextPanel.setLayout(new BoxLayout(headerTextPanel, BoxLayout.Y_AXIS));
        headerTextPanel.setBackground(Color.WHITE);
        headerTextPanel.setBorder(new EmptyBorder(0, 15, 0, 0));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 102, 204));
        headerTextPanel.add(titleLabel);
        
        JLabel companyLabel = new JLabel("at " + company);
        companyLabel.setFont(new Font("Helvetica", Font.PLAIN, 14));
        companyLabel.setForeground(new Color(100, 100, 100));
        headerTextPanel.add(companyLabel);
        
        headerPanel.add(headerTextPanel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JPanel jobInfoPanel = new JPanel();
        jobInfoPanel.setLayout(new BoxLayout(jobInfoPanel, BoxLayout.Y_AXIS));
        jobInfoPanel.setBackground(Color.WHITE);
        jobInfoPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(220, 220, 220), 1), new EmptyBorder(15, 15, 15, 15)));
        
        JLabel jobInfoTitle = new JLabel("Job Information");
        jobInfoTitle.setFont(new Font("Helvetica", Font.BOLD, 13));
        jobInfoTitle.setForeground(new Color(0, 102, 204));
        jobInfoPanel.add(jobInfoTitle);
        jobInfoPanel.add(Box.createVerticalStrut(10));
        
        jobInfoPanel.add(createDetailRow("Location:", location));
        jobInfoPanel.add(Box.createVerticalStrut(6));
        jobInfoPanel.add(createDetailRow("Salary:", salary));
        jobInfoPanel.add(Box.createVerticalStrut(6));
        jobInfoPanel.add(createDetailRow("Job Type:", jobType));
        jobInfoPanel.add(Box.createVerticalStrut(6));
        jobInfoPanel.add(createDetailRow("Posted:", datePosted));
        
        contentPanel.add(jobInfoPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        
        JPanel aboutCompanyPanel = new JPanel() {
            private ImageIcon logoIcon = getScaledCompanyLogo(company, 400, 300);
            
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                if (logoIcon != null) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.08f));
                    Image logoImage = logoIcon.getImage();
                    int x = getWidth() / 2 - 200;
                    int y = getHeight() / 2 - 150;
                    g2d.drawImage(logoImage, x, y, 400, 300, null);
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                }
            }
        };
        aboutCompanyPanel.setLayout(new BoxLayout(aboutCompanyPanel, BoxLayout.Y_AXIS));
        aboutCompanyPanel.setBackground(Color.WHITE);
        aboutCompanyPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(220, 220, 220), 1), new EmptyBorder(15, 15, 15, 15)));
        
        JLabel aboutTitle = new JLabel("🏢 About " + company);
        aboutTitle.setFont(new Font("Helvetica", Font.BOLD, 13));
        aboutTitle.setForeground(new Color(0, 102, 204));
        aboutCompanyPanel.add(aboutTitle);
        aboutCompanyPanel.add(Box.createVerticalStrut(10));
        
        String companyDesc = getCompanyDescription(company);
        JTextArea aboutText = new JTextArea(companyDesc);
        aboutText.setFont(new Font("Helvetica", Font.PLAIN, 11));
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);
        aboutText.setEditable(false);
        aboutText.setBackground(new Color(250, 250, 250));
        aboutText.setBorder(new EmptyBorder(10, 10, 10, 10));
        aboutText.setForeground(new Color(70, 70, 70));
        aboutCompanyPanel.add(aboutText);
        
        contentPanel.add(aboutCompanyPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        
        JPanel jobDescPanel = new JPanel();
        jobDescPanel.setLayout(new BoxLayout(jobDescPanel, BoxLayout.Y_AXIS));
        jobDescPanel.setBackground(Color.WHITE);
        jobDescPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(220, 220, 220), 1), new EmptyBorder(15, 15, 15, 15)));
        
        JLabel descTitle = new JLabel("📝 Job Description");
        descTitle.setFont(new Font("Helvetica", Font.BOLD, 13));
        descTitle.setForeground(new Color(0, 102, 204));
        jobDescPanel.add(descTitle);
        jobDescPanel.add(Box.createVerticalStrut(10));
        
        String jobDesc = getJobDescription(title);
        JTextArea descText = new JTextArea(jobDesc);
        descText.setFont(new Font("Helvetica", Font.PLAIN, 11));
        descText.setLineWrap(true);
        descText.setWrapStyleWord(true);
        descText.setEditable(false);
        descText.setBackground(new Color(250, 250, 250));
        descText.setBorder(new EmptyBorder(10, 10, 10, 10));
        descText.setForeground(new Color(70, 70, 70));
        jobDescPanel.add(descText);
        
        contentPanel.add(jobDescPanel);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomButtonPanel.setBackground(new Color(245, 245, 245));
        bottomButtonPanel.setBorder(new MatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));
        
        JButton applyDialogBtn = createButton("Apply Now", new Color(0, 102, 204));
        applyDialogBtn.setPreferredSize(new Dimension(120, 36));
        applyDialogBtn.setFont(new Font("Helvetica", Font.BOLD, 12));
        applyDialogBtn.addActionListener(e -> {
            int userId = app.getCurrentUser().getUserId();
            
            // 🔴 NEW: SAVE APPLICATION TO DATABASE
            if (DatabaseHelper.applyForJob(userId, title, company)) {
                JOptionPane.showMessageDialog(dialog, "Application submitted successfully!\n\nWe wish you the best of luck! 🍀", "Application Sent", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "You have already applied to this job!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        bottomButtonPanel.add(applyDialogBtn);
        
        JButton closeBtn = createButton("Close", new Color(100, 100, 100));
        closeBtn.setPreferredSize(new Dimension(100, 36));
        closeBtn.setFont(new Font("Helvetica", Font.BOLD, 12));
        closeBtn.addActionListener(e -> dialog.dispose());
        bottomButtonPanel.add(closeBtn);
        
        mainPanel.add(bottomButtonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    private JPanel createDetailRow(String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        row.setOpaque(false);
        
        JLabel labelPart = new JLabel(label);
        labelPart.setFont(new Font("Helvetica", Font.BOLD, 11));
        labelPart.setForeground(new Color(50, 50, 50));
        labelPart.setPreferredSize(new Dimension(70, 20));
        row.add(labelPart);
        
        JLabel valuePart = new JLabel(value);
        valuePart.setFont(new Font("Helvetica", Font.PLAIN, 11));
        valuePart.setForeground(new Color(100, 100, 100));
        row.add(valuePart);
        
        return row;
    }
    
    private String getJobDescription(String title) {
        Map<String, String> descriptions = new HashMap<>();
        descriptions.put("Senior Java Developer", "We are looking for an experienced Java developer with 5+ years of experience. You will work on large-scale applications, mentor junior developers, and contribute to system architecture decisions. Must have expertise in Spring Boot, Microservices, and Cloud technologies.");
        descriptions.put("Frontend Developer", "Build beautiful and responsive user interfaces using React/Angular. Work with our design team to create amazing user experiences. Strong knowledge of HTML5, CSS3, JavaScript ES6+ required.");
        descriptions.put("Data Scientist", "Analyze complex datasets, build machine learning models, and drive data-driven insights for business decisions. Experience with Python, TensorFlow, and statistical analysis is essential.");
        descriptions.put("UI/UX Designer", "Design intuitive user interfaces and conduct user research to create the best user experience. Proficiency in Figma, Adobe XD, and design thinking methodology required.");
        descriptions.put("DevOps Engineer", "Manage cloud infrastructure, CI/CD pipelines, and ensure system reliability and performance. Experience with Docker, Kubernetes, Jenkins, and AWS is mandatory.");
        descriptions.put("Product Manager", "Lead product strategy, work with cross-functional teams, and drive product roadmap. Must have 3+ years of product management experience.");
        descriptions.put("Backend Developer", "Develop robust backend systems and APIs using Java/Python/Node.js. Strong understanding of databases, REST APIs, and system design patterns required.");
        descriptions.put("Mobile App Developer", "Create mobile applications for iOS and Android platforms. Experience with Flutter or React Native is preferred.");
        descriptions.put("Junior Developer", "Great opportunity for freshers to start their career in software development. Mentorship and training will be provided. Basic knowledge of Java/Python required.");
        descriptions.put("Sales Manager", "Lead sales team, manage client relationships, and drive revenue growth. 5+ years of sales experience in IT industry preferred.");
        descriptions.put("Financial Analyst", "Analyze financial data, create reports, and provide insights for investment decisions. Strong Excel and financial modeling skills required.");
        descriptions.put("Marketing Executive", "Plan and execute marketing campaigns, manage social media presence, and engage with customers. Creative thinking and communication skills essential.");
        descriptions.put("Content Writer", "Create engaging content for blogs, social media, and marketing materials. Excellent writing skills and SEO knowledge required.");
        descriptions.put("HR Manager", "Manage HR operations, recruitment, employee relations, and company culture. 3+ years of HR management experience required.");
        descriptions.put("Business Analyst", "Gather requirements, analyze business processes, and provide solutions. Strong analytical and communication skills required.");
        
        return descriptions.getOrDefault(title, "Exciting opportunity to join our team and make an impact!");
    }

    private String getCompanyDescription(String company) {
        Map<String, String> descriptions = new HashMap<>();
        descriptions.put("Google India", "Google is a technology company that specializes in Internet services and products. We provide search, cloud, and productivity tools to billions of users worldwide. Google is known for its innovation culture and employee-centric approach.");
        descriptions.put("Amazon", "Amazon is an e-commerce and cloud services leader, serving millions of customers worldwide. AWS is the leading cloud platform. We're committed to customer obsession and operational excellence.");
        descriptions.put("Microsoft", "Microsoft develops software, cloud services, and hardware products used by billions globally. Azure cloud platform and enterprise solutions are our core strengths.");
        descriptions.put("Flipkart", "Flipkart is India's leading e-commerce platform, offering a wide range of products. We are transforming retail in India through technology and innovation.");
        descriptions.put("Infosys", "Infosys is a global IT services company providing digital transformation solutions. With a legacy of innovation, we serve clients across 50+ countries.");
        descriptions.put("TCS", "Tata Consultancy Services is a leading IT services and consulting company with a global presence. We deliver transformational services and solutions.");
        descriptions.put("Wipro", "Wipro provides IT services, consulting, and business process services worldwide. We are committed to client success and innovation.");
        descriptions.put("Paytm", "Paytm is a leading digital payments and commerce platform in India. We're revolutionizing financial inclusion through technology.");
        descriptions.put("Startup Inc", "A dynamic startup focused on innovative technology solutions. We foster a culture of creativity, collaboration, and continuous learning.");
        descriptions.put("HDFC Bank", "HDFC Bank is one of India's leading private sector banks with a strong presence across the country. We're committed to financial inclusion and innovation.");
        descriptions.put("ICICI Bank", "ICICI Bank is a major private banking institution in India with innovative digital banking solutions. We serve millions of customers across India.");
        descriptions.put("HUL", "Hindustan Unilever Limited is India's largest FMCG company. We operate in multiple categories serving diverse consumer needs.");
        descriptions.put("Zomato", "Zomato is India's leading food delivery and restaurant platform. We're transforming the way people discover and enjoy food.");
        descriptions.put("Reliance", "Reliance Industries is a major conglomerate in energy, telecom, and retail. We're a diversified multinational corporation with global reach.");
        descriptions.put("Accenture", "Accenture is a global professional services company providing consulting and technology services. We help clients transform through innovation.");
        
        return descriptions.getOrDefault(company, "A leading company in its industry.");
    }
    
    private ImageIcon getScaledCompanyLogo(String company, int width, int height) {
        try {
            String logoFileName = getCompanyLogoPath(company);
            
            // Try multiple paths
            String[] possiblePaths = {
                logoFileName,  // Relative to project root
                "bin/" + logoFileName,  // In bin folder
                "src/" + logoFileName,  // In src folder
                System.getProperty("user.dir") + "/" + logoFileName,  // Full path from working directory
                System.getProperty("user.dir") + "/bin/" + logoFileName,
                System.getProperty("user.dir") + "/src/" + logoFileName
            };
            
            for (String path : possiblePaths) {
                java.io.File file = new java.io.File(path);
                if (file.exists()) {
                    ImageIcon originalIcon = new ImageIcon(path);
                    Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaledImage);
                }
            }
            
            // If not found in any path, show message and use default
            System.out.println("Logo not found for: " + company);
            System.out.println("Looked in: images/" + logoFileName);
            return new ImageIcon(createDefaultLogoImage(width, height));
            
        } catch (Exception e) {
            System.out.println("Error loading logo: " + e.getMessage());
            return new ImageIcon(createDefaultLogoImage(width, height));
        }
    }
    
    private String getCompanyLogoPath(String company) {
        Map<String, String> logoMap = new HashMap<>();
        logoMap.put("Google India", "images/google.jpeg");
        logoMap.put("Amazon", "images/Amazon.jpg");
        logoMap.put("Microsoft", "images/microsoft.jpeg");
        logoMap.put("Flipkart", "images/flipkart.png");
        logoMap.put("Infosys", "images/infosys.png");
        logoMap.put("TCS", "images/tcs.png");
        logoMap.put("Wipro", "images/wipro.jpeg");
        logoMap.put("Paytm", "images/paytm.png");
        logoMap.put("Startup Inc", "images/startupinc.jpeg");
        logoMap.put("HDFC Bank", "images/hdfc.png");
        logoMap.put("ICICI Bank", "images/icici.png");
        logoMap.put("HUL", "images/hul.png");
        logoMap.put("Zomato", "images/zomato.png");
        logoMap.put("Reliance", "images/reliance.png");
        logoMap.put("Accenture", "images/accenture.png");
        
        return logoMap.getOrDefault(company, "images/default.png");
    }
    
    private Image createDefaultLogoImage(int width, int height) {
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(new Color(200, 200, 200));
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(new Color(100, 100, 100));
        g2d.setFont(new Font("Helvetica", Font.BOLD, 8));
        g2d.drawString("Logo", 5, height / 2);
        g2d.dispose();
        return img;
    }
    
    private void addFormRow(JPanel panel, GridBagConstraints gbc, String label, JComponent comp, int row) {
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridy = row;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        panel.add(comp, gbc);
    }
    
    private JTextArea createTextArea() {
        JTextArea ta = new JTextArea(4, 20);
        ta.setFont(new Font("Helvetica", Font.PLAIN, 12));
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        return ta;
    }
    
    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Helvetica", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(6, 15, 6, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    private JPanel createMentorshipPanel() {
        JPanel p = new JPanel();
        p.setBackground(new Color(245, 245, 245));
        JLabel l = new JLabel("🤝 Coming Soon!");
        l.setFont(new Font("Helvetica", Font.BOLD, 24));
        l.setForeground(new Color(0, 102, 204));
        p.add(l);
        return p;
    }
    private JPanel createProfilePanel() {
        System.out.println("\n========== createProfilePanel() called ==========");
        
        JPanel profilePanel = new JPanel(new BorderLayout(15, 15));
        profilePanel.setBackground(new Color(245, 245, 245));
        profilePanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(245, 245, 245));
        JLabel title = new JLabel("👤 My Profile");
        title.setFont(new Font("Helvetica", Font.BOLD, 24));
        header.add(title, BorderLayout.WEST);
        
        JButton backBtn = createButton("← Back", new Color(100, 100, 100));
        backBtn.addActionListener(e -> contentLayout.show(contentPanel, "HOME"));
        header.add(backBtn, BorderLayout.EAST);
        profilePanel.add(header, BorderLayout.NORTH);

        // Check current user
        if (app.getCurrentUser() == null) {
            System.out.println("❌ No user logged in!");
            JLabel noLoginLabel = new JLabel("Please login first to view profile");
            noLoginLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
            noLoginLabel.setHorizontalAlignment(SwingConstants.CENTER);
            noLoginLabel.setForeground(new Color(100, 100, 100));
            profilePanel.add(noLoginLabel, BorderLayout.CENTER);
            return profilePanel;
        }

        int userId = app.getCurrentUser().getUserId();
        System.out.println("✅ User logged in - UserId: " + userId);
        System.out.println("   Username: " + app.getCurrentUser().getName());

        // Create content with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Helvetica", Font.BOLD, 12));
        tabbedPane.setBackground(Color.WHITE);
        
        tabbedPane.addTab("📋 Basic Info", createBasicInfoPanel(userId));
        tabbedPane.addTab("🛠️ Skills", createSkillsPanel());
        tabbedPane.addTab("🎓 Qualifications", createQualificationsPanel());

        profilePanel.add(tabbedPane, BorderLayout.CENTER);
        System.out.println("========== createProfilePanel() DONE ==========\n");
        return profilePanel;
    }

      private void refreshSavedJobs() {
        // Find the saved jobs panel and refresh it
        Component[] components = contentPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                if (panel.getComponentCount() > 0) {
                    Component first = panel.getComponent(0);
                    if (first instanceof JPanel) {
                        JPanel headerPanel = (JPanel) first;
                        if (headerPanel.getComponentCount() > 0) {
                            Component headerLabel = headerPanel.getComponent(0);
                            if (headerLabel instanceof JLabel) {
                                JLabel label = (JLabel) headerLabel;
                                if (label.getText().contains("Saved Jobs")) {
                                    // Found it - rebuild the panel
                                    contentPanel.remove(panel);
                                    contentPanel.add(createUpdatedSavedJobsPanel(), "SAVED_JOBS");
                                    contentPanel.revalidate();
                                    contentPanel.repaint();
                                    contentLayout.show(contentPanel, "SAVED_JOBS");
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Component createUpdatedSavedJobsPanel() {
        JPanel savedJobsPanel = new JPanel(new BorderLayout(15, 15));
        savedJobsPanel.setBackground(new Color(245, 245, 245));
        savedJobsPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(245, 245, 245));
        JLabel title = new JLabel("⭐ Saved Jobs");
        title.setFont(new Font("Helvetica", Font.BOLD, 24));
        header.add(title, BorderLayout.WEST);
        JButton backBtn = createButton("← Back", new Color(100, 100, 100));
        backBtn.addActionListener(e -> contentLayout.show(contentPanel, "HOME"));
        backBtn.setMaximumSize(new Dimension(100, 40));
        header.add(backBtn, BorderLayout.EAST);
        savedJobsPanel.add(header, BorderLayout.NORTH);

        JPanel savedJobsContainer = new JPanel();
        savedJobsContainer.setLayout(new BoxLayout(savedJobsContainer, BoxLayout.Y_AXIS));
        savedJobsContainer.setBackground(new Color(245, 245, 245));
        
        int userId = app.getCurrentUser().getUserId();
        String[][] savedJobsFromDB = DatabaseHelper.getSavedJobs(userId);
        
        if (savedJobsFromDB.length == 0) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
            emptyPanel.setBackground(Color.WHITE);
            emptyPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
            emptyPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
            JLabel emptyLabel = new JLabel("😔 No saved jobs yet");
            emptyLabel.setFont(new Font("Helvetica", Font.BOLD, 18));
            emptyLabel.setForeground(new Color(100, 100, 100));
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyPanel.add(emptyLabel);
            savedJobsContainer.add(emptyPanel);
        } else {
            for (String[] job : savedJobsFromDB) {
                savedJobsContainer.add(createSavedJobCard(job));
                savedJobsContainer.add(Box.createVerticalStrut(10));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(savedJobsContainer);
        scrollPane.setBackground(new Color(245, 245, 245));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        savedJobsPanel.add(scrollPane, BorderLayout.CENTER);
        
        return savedJobsPanel;
    }
 // ============= PROFILE METHODS =============

 // REPLACE the createBasicInfoPanel method in DashboardPanel.java with this redesigned version

    private JPanel createBasicInfoPanel(int userId) {
        System.out.println("\n--- createBasicInfoPanel() for userId: " + userId);
        
        JPanel basicPanel = new JPanel(new BorderLayout());
        basicPanel.setBackground(new Color(245, 248, 252));
        basicPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Get profile data from database
        String[] profileData = DatabaseHelper.getUserProfile(userId);
        
        if (profileData == null || profileData.length < 6) {
            System.out.println("❌ Profile data is NULL or incomplete!");
            JLabel errorLabel = new JLabel("Error loading profile data");
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            basicPanel.add(errorLabel, BorderLayout.CENTER);
            return basicPanel;
        }

        String name = profileData[0];
        String email = profileData[1];
        String phone = profileData[2];
        String bio = profileData[3];
        String location = profileData[4];
        String experience = profileData[5];

        System.out.println("✅ Loaded profile data:");
        System.out.println("   Name: " + name);
        System.out.println("   Email: " + email);
        System.out.println("   Phone: " + phone);
        System.out.println("   Location: " + location);
        System.out.println("   Experience: " + experience + " years");

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 248, 252));
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // ============= HEADER CARD =============
        JPanel headerCard = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(10, 38, 71));
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
        headerCard.setLayout(new BorderLayout(20, 0));
        headerCard.setOpaque(false);
        headerCard.setBorder(new EmptyBorder(25, 25, 25, 25));
        headerCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        headerCard.setPreferredSize(new Dimension(0, 120));

        // Avatar circle with initials
        JPanel avatarPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 194, 203));
                g2d.fillOval(0, 0, getWidth(), getHeight());
                
                String initials = getInitials(name);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Helvetica", Font.BOLD, 28));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(initials)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(initials, x, y);
            }
        };
        avatarPanel.setOpaque(false);
        avatarPanel.setPreferredSize(new Dimension(80, 80));
        avatarPanel.setMaximumSize(new Dimension(80, 80));
        headerCard.add(avatarPanel, BorderLayout.WEST);

        // User info in header
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Helvetica", Font.BOLD, 24));
        nameLabel.setForeground(Color.WHITE);
        userInfoPanel.add(nameLabel);
        userInfoPanel.add(Box.createVerticalStrut(5));

        JLabel emailLabel = new JLabel(email);
        emailLabel.setFont(new Font("Helvetica", Font.PLAIN, 12));
        emailLabel.setForeground(new Color(200, 220, 230));
        userInfoPanel.add(emailLabel);
        userInfoPanel.add(Box.createVerticalStrut(3));

        JLabel phoneLabel = new JLabel("📱 " + phone);
        phoneLabel.setFont(new Font("Helvetica", Font.PLAIN, 11));
        phoneLabel.setForeground(new Color(200, 220, 230));
        userInfoPanel.add(phoneLabel);

        headerCard.add(userInfoPanel, BorderLayout.CENTER);
        contentPanel.add(headerCard);
        contentPanel.add(Box.createVerticalStrut(25));

        // ============= INFO CARDS GRID =============
        JPanel cardsGrid = new JPanel(new GridLayout(2, 2, 20, 20));
        cardsGrid.setOpaque(false);
        cardsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        // Card 1: Location
        cardsGrid.add(createInfoCard("📍 Location", location.isEmpty() ? "Not specified" : location, new Color(52, 152, 219)));

        // Card 2: Experience
        cardsGrid.add(createInfoCard("💼 Experience", experience + " years", new Color(46, 204, 113)));

        // Card 3: Email (detailed)
        cardsGrid.add(createInfoCard("✉️ Email Address", email, new Color(155, 89, 182)));

        // Card 4: Phone (detailed)
        cardsGrid.add(createInfoCard("☎️ Phone Number", phone.isEmpty() ? "Not specified" : phone, new Color(230, 126, 34)));

        contentPanel.add(cardsGrid);
        contentPanel.add(Box.createVerticalStrut(25));

        // ============= BIO SECTION =============
        JPanel bioSection = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2d.setColor(new Color(220, 220, 220));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }
        };
        bioSection.setLayout(new BorderLayout(0, 15));
        bioSection.setOpaque(false);
        bioSection.setBorder(new EmptyBorder(20, 20, 20, 20));
        bioSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JLabel bioTitleLabel = new JLabel("📝 About Me");
        bioTitleLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
        bioTitleLabel.setForeground(new Color(10, 38, 71));
        bioSection.add(bioTitleLabel, BorderLayout.NORTH);

        JTextArea bioArea = new JTextArea(bio.isEmpty() ? "No bio added yet. Click Edit Profile to add one." : bio);
        bioArea.setFont(new Font("Helvetica", Font.PLAIN, 12));
        bioArea.setLineWrap(true);
        bioArea.setWrapStyleWord(true);
        bioArea.setEditable(false);
        bioArea.setOpaque(false);
        bioArea.setForeground(bio.isEmpty() ? new Color(150, 150, 150) : new Color(60, 60, 60));
        bioArea.setBorder(null);
        bioSection.add(bioArea, BorderLayout.CENTER);

        contentPanel.add(bioSection);
        contentPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(new Color(245, 248, 252));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        basicPanel.add(scrollPane, BorderLayout.CENTER);

        // ============= EDIT BUTTON =============
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        buttonPanel.setBackground(new Color(245, 248, 252));
        buttonPanel.setBorder(new MatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        JButton editBtn = createButton("✏️ Edit Profile", new Color(0, 150, 100));
        editBtn.setPreferredSize(new Dimension(140, 40));
        editBtn.setFont(new Font("Helvetica", Font.BOLD, 13));
        editBtn.addActionListener(e -> showEditProfileDialog(userId, profileData));
        buttonPanel.add(editBtn);

        basicPanel.add(buttonPanel, BorderLayout.SOUTH);
        return basicPanel;
    }
    
    private JPanel createDisplayRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(15, 0));
        row.setOpaque(false);

        JLabel labelPart = new JLabel(label);
        labelPart.setFont(new Font("Helvetica", Font.BOLD, 12));
        labelPart.setForeground(new Color(0, 102, 204));
        labelPart.setPreferredSize(new Dimension(140, 25));
        row.add(labelPart, BorderLayout.WEST);

        JLabel valuePart = new JLabel(value);
        valuePart.setFont(new Font("Helvetica", Font.PLAIN, 12));
        valuePart.setForeground(new Color(50, 50, 50));
        row.add(valuePart, BorderLayout.CENTER);

        return row;
    }
 // REPLACE the showEditProfileDialog method in DashboardPanel.java with this

    private void showEditProfileDialog(int userId, String[] profileData) {
        System.out.println("\n--- showEditProfileDialog() for userId: " + userId);
        
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Edit Profile", true);
        dialog.setSize(650, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 248, 252));
        mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Header with title
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(10, 38, 71));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel headerTitle = new JLabel("✏️ Edit Your Profile");
        headerTitle.setFont(new Font("Helvetica", Font.BOLD, 20));
        headerTitle.setForeground(Color.WHITE);
        headerPanel.add(headerTitle, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(12, 0, 12, 0);

        String name = profileData[0];
        String email = profileData[1];
        String phone = profileData[2];
        String bio = profileData[3];
        String location = profileData[4];
        String experience = profileData[5];

        // Full Name (read-only)
        addFormSection(formPanel, gbc, "Full Name", name, false, nameField -> {});
        gbc.gridy++;

        // Email (read-only)
        addFormSection(formPanel, gbc, "Email Address", email, false, emailField -> {});
        gbc.gridy++;

        // Phone (editable)
        JTextField phoneField = new JTextField(phone);
        addFormSection(formPanel, gbc, "Phone Number", phone, true, field -> {
            phoneField.setText(field);
        });
        phoneField.setFont(new Font("Helvetica", Font.PLAIN, 12));
        phoneField.setPreferredSize(new Dimension(0, 40));
        phoneField.setBorder(createStyledTextFieldBorder());
        gbc.gridy++;
        formPanel.add(phoneField, gbc);
        gbc.gridy++;

        // Location (editable)
        JTextField locationField = new JTextField(location);
        addFormSection(formPanel, gbc, "Location", location, true, field -> {
            locationField.setText(field);
        });
        locationField.setFont(new Font("Helvetica", Font.PLAIN, 12));
        locationField.setPreferredSize(new Dimension(0, 40));
        locationField.setBorder(createStyledTextFieldBorder());
        gbc.gridy++;
        formPanel.add(locationField, gbc);
        gbc.gridy++;

        // Experience (editable)
        JLabel expLabel = new JLabel("Years of Experience");
        expLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
        expLabel.setForeground(new Color(10, 38, 71));
        gbc.gridy++;
        formPanel.add(expLabel, gbc);

        JSpinner expSpinner = new JSpinner(new SpinnerNumberModel(Integer.parseInt(experience), 0, 50, 1));
        expSpinner.setFont(new Font("Helvetica", Font.PLAIN, 12));
        expSpinner.setPreferredSize(new Dimension(0, 40));
        gbc.gridy++;
        formPanel.add(expSpinner, gbc);

        // Bio (editable)
        JLabel bioLabel = new JLabel("About Me");
        bioLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
        bioLabel.setForeground(new Color(10, 38, 71));
        gbc.gridy++;
        formPanel.add(bioLabel, gbc);

        JTextArea bioArea = new JTextArea(bio, 5, 30);
        bioArea.setFont(new Font("Helvetica", Font.PLAIN, 12));
        bioArea.setLineWrap(true);
        bioArea.setWrapStyleWord(true);
        bioArea.setBorder(createStyledTextFieldBorder());
        gbc.gridy++;
        formPanel.add(new JScrollPane(bioArea), gbc);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBackground(new Color(245, 248, 252));
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        buttonPanel.setBackground(new Color(245, 248, 252));
        buttonPanel.setBorder(new MatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        JButton saveBtn = new JButton("💾 Save Changes");
        saveBtn.setFont(new Font("Helvetica", Font.BOLD, 12));
        saveBtn.setBackground(new Color(0, 150, 100));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(new EmptyBorder(8, 20, 8, 20));
        saveBtn.setPreferredSize(new Dimension(150, 40));
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                saveBtn.setBackground(new Color(0, 180, 120));
            }
            public void mouseExited(MouseEvent e) {
                saveBtn.setBackground(new Color(0, 150, 100));
            }
        });
        saveBtn.addActionListener(e -> {
            String newPhone = phoneField.getText().trim();
            String newLocation = locationField.getText().trim();
            int newExp = (Integer) expSpinner.getValue();
            String newBio = bioArea.getText().trim();

            if (newPhone.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "❌ Phone number cannot be empty!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (DatabaseHelper.saveUserProfile(userId, newBio, newLocation, newExp, "")) {
                DatabaseHelper.updateUserPhone(userId, newPhone);
                System.out.println("✅ Profile saved!");
                JOptionPane.showMessageDialog(dialog, "✅ Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                refreshProfilePanel();
            } else {
                JOptionPane.showMessageDialog(dialog, "❌ Error saving profile!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(saveBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Helvetica", Font.BOLD, 12));
        cancelBtn.setBackground(new Color(150, 150, 150));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorder(new EmptyBorder(8, 20, 8, 20));
        cancelBtn.setPreferredSize(new Dimension(100, 40));
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                cancelBtn.setBackground(new Color(180, 180, 180));
            }
            public void mouseExited(MouseEvent e) {
                cancelBtn.setBackground(new Color(150, 150, 150));
            }
        });
        cancelBtn.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    // Helper method for styled form fields
    private void addFormSection(JPanel panel, GridBagConstraints gbc, String label, String value, boolean editable, java.util.function.Consumer<String> callback) {
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Helvetica", Font.BOLD, 12));
        labelComp.setForeground(new Color(10, 38, 71));
        panel.add(labelComp, gbc);
        gbc.gridy++;

        if (!editable) {
            JTextField field = new JTextField(value);
            field.setFont(new Font("Helvetica", Font.PLAIN, 12));
            field.setEditable(false);
            field.setBackground(new Color(240, 245, 250));
            field.setForeground(new Color(100, 100, 100));
            field.setPreferredSize(new Dimension(0, 40));
            field.setBorder(createStyledTextFieldBorder());
            panel.add(field, gbc);
            callback.accept(value);
        }
    }

    // Helper method for styled borders
    private javax.swing.border.Border createStyledTextFieldBorder() {
        return BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 220, 230), 1),
            new EmptyBorder(8, 10, 8, 10)
        );
    }
 // REPLACE the createSkillsPanel() method in DashboardPanel.java with this

    private JPanel createSkillsPanel() {
        System.out.println("\n--- createSkillsPanel() called");
        
        JPanel skillsPanel = new JPanel(new BorderLayout(0, 0));
        skillsPanel.setBackground(new Color(245, 248, 252));
        skillsPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        if (app.getCurrentUser() == null) {
            JLabel noUserLabel = new JLabel("Please login to view skills");
            noUserLabel.setFont(new Font("Helvetica", Font.PLAIN, 14));
            noUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
            skillsPanel.add(noUserLabel, BorderLayout.CENTER);
            return skillsPanel;
        }

        int userId = app.getCurrentUser().getUserId();
        String[][] skills = DatabaseHelper.getUserSkills(userId);

        JPanel mainContent = new JPanel(new BorderLayout(0, 0));
        mainContent.setBackground(new Color(245, 248, 252));
        mainContent.setBorder(new EmptyBorder(25, 30, 25, 30));

        // ============= SKILLS DISPLAY SECTION =============
        JPanel skillsDisplayPanel = new JPanel();
        skillsDisplayPanel.setLayout(new BoxLayout(skillsDisplayPanel, BoxLayout.Y_AXIS));
        skillsDisplayPanel.setBackground(new Color(245, 248, 252));

        if (skills.length > 0) {
            JLabel titleLabel = new JLabel("🛠️ Your Skills");
            titleLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
            titleLabel.setForeground(new Color(10, 38, 71));
            skillsDisplayPanel.add(titleLabel);
            skillsDisplayPanel.add(Box.createVerticalStrut(15));

            for (String[] skill : skills) {
                skillsDisplayPanel.add(createSkillCard(skill[0], skill[1], userId));
                skillsDisplayPanel.add(Box.createVerticalStrut(12));
            }
        } else {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
            emptyPanel.setBackground(Color.WHITE);
            emptyPanel.setBorder(new LineBorder(new Color(220, 220, 220), 1));
            emptyPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
            
            JLabel emptyLabel = new JLabel("No skills added yet");
            emptyLabel.setFont(new Font("Helvetica", Font.ITALIC, 13));
            emptyLabel.setForeground(new Color(150, 150, 150));
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyPanel.add(Box.createVerticalGlue());
            emptyPanel.add(emptyLabel);
            emptyPanel.add(Box.createVerticalGlue());
            skillsDisplayPanel.add(emptyPanel);
        }

        skillsDisplayPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(skillsDisplayPanel);
        scrollPane.setBackground(new Color(245, 248, 252));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        mainContent.add(scrollPane, BorderLayout.CENTER);

        // ============= ADD SKILL FORM SECTION =============
        JPanel addSkillPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2d.setColor(new Color(200, 220, 230));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }
        };
        addSkillPanel.setLayout(new GridBagLayout());
        addSkillPanel.setOpaque(false);
        addSkillPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel addSkillTitle = new JLabel("➕ Add New Skill");
        addSkillTitle.setFont(new Font("Helvetica", Font.BOLD, 13));
        addSkillTitle.setForeground(new Color(10, 38, 71));
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        addSkillPanel.add(addSkillTitle, gbc);

        JLabel skillNameLabel = new JLabel("Skill Name:");
        skillNameLabel.setFont(new Font("Helvetica", Font.BOLD, 11));
        skillNameLabel.setForeground(new Color(50, 50, 50));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        addSkillPanel.add(skillNameLabel, gbc);

        JTextField skillNameField = new JTextField();
        skillNameField.setFont(new Font("Helvetica", Font.PLAIN, 11));
        skillNameField.setPreferredSize(new Dimension(0, 35));
        skillNameField.setBorder(createStyledBorder());
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        addSkillPanel.add(skillNameField, gbc);

        JLabel proficiencyLabel = new JLabel("Proficiency:");
        proficiencyLabel.setFont(new Font("Helvetica", Font.BOLD, 11));
        proficiencyLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        addSkillPanel.add(proficiencyLabel, gbc);

        JComboBox<String> proficiencyBox = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Advanced", "Expert"});
        proficiencyBox.setFont(new Font("Helvetica", Font.PLAIN, 11));
        proficiencyBox.setBackground(Color.WHITE);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        addSkillPanel.add(proficiencyBox, gbc);

        JButton addSkillBtn = new JButton("➕ Add Skill");
        addSkillBtn.setFont(new Font("Helvetica", Font.BOLD, 11));
        addSkillBtn.setBackground(new Color(52, 152, 219));
        addSkillBtn.setForeground(Color.WHITE);
        addSkillBtn.setFocusPainted(false);
        addSkillBtn.setBorder(new EmptyBorder(6, 15, 6, 15));
        addSkillBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addSkillBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                addSkillBtn.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(MouseEvent e) {
                addSkillBtn.setBackground(new Color(52, 152, 219));
            }
        });
        addSkillBtn.addActionListener(e -> {
            String skillName = skillNameField.getText().trim();
            String proficiency = (String) proficiencyBox.getSelectedItem();

            if (skillName.isEmpty()) {
                JOptionPane.showMessageDialog(addSkillPanel, "❌ Please enter skill name!", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                if (DatabaseHelper.addUserSkill(userId, skillName, proficiency)) {
                    JOptionPane.showMessageDialog(addSkillPanel, "✅ Skill added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    skillNameField.setText("");
                    proficiencyBox.setSelectedIndex(0);
                    contentLayout.show(contentPanel, "PROFILE");
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(20, 15, 10, 15);
        addSkillPanel.add(addSkillBtn, gbc);

        mainContent.add(addSkillPanel, BorderLayout.SOUTH);
        skillsPanel.add(mainContent, BorderLayout.CENTER);
        return skillsPanel;
    }

    // Helper method to create skill cards
    private JPanel createSkillCard(String skillName, String proficiency, int userId) {
        Color[] colors = {
            new Color(52, 152, 219),
            new Color(46, 204, 113),
            new Color(155, 89, 182),
            new Color(230, 126, 34)
        };
        
        Color cardColor = colors[Math.abs(skillName.hashCode()) % colors.length];

        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2d.setColor(cardColor);
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            }
        };
        card.setLayout(new BorderLayout(15, 0));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(12, 15, 12, 15));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        // Left side - Skill info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel skillLabel = new JLabel(skillName);
        skillLabel.setFont(new Font("Helvetica", Font.BOLD, 13));
        skillLabel.setForeground(cardColor);
        infoPanel.add(skillLabel);
        infoPanel.add(Box.createVerticalStrut(3));

        JLabel profLabel = new JLabel("Proficiency: " + proficiency);
        profLabel.setFont(new Font("Helvetica", Font.PLAIN, 10));
        profLabel.setForeground(new Color(100, 100, 100));
        infoPanel.add(profLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        // Right side - Delete button
        JButton deleteBtn = new JButton("🗑️ Remove");
        deleteBtn.setFont(new Font("Helvetica", Font.BOLD, 10));
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorder(new EmptyBorder(5, 10, 5, 10));
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                deleteBtn.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(MouseEvent e) {
                deleteBtn.setBackground(new Color(231, 76, 60));
            }
        });
        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(card, "Are you sure you want to remove this skill?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (DatabaseHelper.removeUserSkill(userId, skillName)) {
                    JOptionPane.showMessageDialog(card, "✅ Skill removed!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    contentLayout.show(contentPanel, "PROFILE");
                }
            }
        });
        card.add(deleteBtn, BorderLayout.EAST);

        return card;
    }

    // ============= QUALIFICATIONS PANEL =============

    private JPanel createQualificationsPanel() {
        System.out.println("\n--- createQualificationsPanel() called");
        
        JPanel qualsPanel = new JPanel(new BorderLayout(0, 0));
        qualsPanel.setBackground(new Color(245, 248, 252));
        qualsPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        if (app.getCurrentUser() == null) {
            JLabel noUserLabel = new JLabel("Please login to view qualifications");
            noUserLabel.setFont(new Font("Helvetica", Font.PLAIN, 14));
            noUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
            qualsPanel.add(noUserLabel, BorderLayout.CENTER);
            return qualsPanel;
        }

        int userId = app.getCurrentUser().getUserId();
        String[][] qualifications = DatabaseHelper.getUserQualifications(userId);

        JPanel mainContent = new JPanel(new BorderLayout(0, 0));
        mainContent.setBackground(new Color(245, 248, 252));
        mainContent.setBorder(new EmptyBorder(25, 30, 25, 30));

        // ============= QUALIFICATIONS DISPLAY SECTION =============
        JPanel qualsDisplayPanel = new JPanel();
        qualsDisplayPanel.setLayout(new BoxLayout(qualsDisplayPanel, BoxLayout.Y_AXIS));
        qualsDisplayPanel.setBackground(new Color(245, 248, 252));

        if (qualifications.length > 0) {
            JLabel titleLabel = new JLabel("🎓 Your Qualifications");
            titleLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
            titleLabel.setForeground(new Color(10, 38, 71));
            qualsDisplayPanel.add(titleLabel);
            qualsDisplayPanel.add(Box.createVerticalStrut(15));

            for (String[] qual : qualifications) {
                qualsDisplayPanel.add(createQualificationCard(qual[0], qual[1], qual[2], qual[3], userId));
                qualsDisplayPanel.add(Box.createVerticalStrut(12));
            }
        } else {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
            emptyPanel.setBackground(Color.WHITE);
            emptyPanel.setBorder(new LineBorder(new Color(220, 220, 220), 1));
            emptyPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
            
            JLabel emptyLabel = new JLabel("No qualifications added yet");
            emptyLabel.setFont(new Font("Helvetica", Font.ITALIC, 13));
            emptyLabel.setForeground(new Color(150, 150, 150));
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyPanel.add(Box.createVerticalGlue());
            emptyPanel.add(emptyLabel);
            emptyPanel.add(Box.createVerticalGlue());
            qualsDisplayPanel.add(emptyPanel);
        }

        qualsDisplayPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(qualsDisplayPanel);
        scrollPane.setBackground(new Color(245, 248, 252));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        mainContent.add(scrollPane, BorderLayout.CENTER);

        // ============= ADD QUALIFICATION FORM SECTION =============
        JPanel addQualPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2d.setColor(new Color(200, 220, 230));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }
        };
        addQualPanel.setLayout(new GridBagLayout());
        addQualPanel.setOpaque(false);
        addQualPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JLabel addQualTitle = new JLabel("➕ Add New Qualification");
        addQualTitle.setFont(new Font("Helvetica", Font.BOLD, 13));
        addQualTitle.setForeground(new Color(10, 38, 71));
        gbc.gridy = 0;
        addQualPanel.add(addQualTitle, gbc);

        // Degree field
        JLabel degreeLabel = new JLabel("Degree:");
        degreeLabel.setFont(new Font("Helvetica", Font.BOLD, 11));
        degreeLabel.setForeground(new Color(50, 50, 50));
        gbc.gridy = 1;
        addQualPanel.add(degreeLabel, gbc);

        JTextField degreeField = new JTextField();
        degreeField.setFont(new Font("Helvetica", Font.PLAIN, 11));
        degreeField.setPreferredSize(new Dimension(0, 35));
        degreeField.setBorder(createStyledBorder());
        gbc.gridy = 2;
        addQualPanel.add(degreeField, gbc);

        // Field of Study
        JLabel fieldLabel = new JLabel("Field of Study:");
        fieldLabel.setFont(new Font("Helvetica", Font.BOLD, 11));
        fieldLabel.setForeground(new Color(50, 50, 50));
        gbc.gridy = 3;
        addQualPanel.add(fieldLabel, gbc);

        JTextField fieldField = new JTextField();
        fieldField.setFont(new Font("Helvetica", Font.PLAIN, 11));
        fieldField.setPreferredSize(new Dimension(0, 35));
        fieldField.setBorder(createStyledBorder());
        gbc.gridy = 4;
        addQualPanel.add(fieldField, gbc);

        // Institution
        JLabel institutionLabel = new JLabel("Institution/University:");
        institutionLabel.setFont(new Font("Helvetica", Font.BOLD, 11));
        institutionLabel.setForeground(new Color(50, 50, 50));
        gbc.gridy = 5;
        addQualPanel.add(institutionLabel, gbc);

        JTextField institutionField = new JTextField();
        institutionField.setFont(new Font("Helvetica", Font.PLAIN, 11));
        institutionField.setPreferredSize(new Dimension(0, 35));
        institutionField.setBorder(createStyledBorder());
        gbc.gridy = 6;
        addQualPanel.add(institutionField, gbc);

        // Graduation Year
        JLabel yearLabel = new JLabel("Graduation Year:");
        yearLabel.setFont(new Font("Helvetica", Font.BOLD, 11));
        yearLabel.setForeground(new Color(50, 50, 50));
        gbc.gridy = 7;
        addQualPanel.add(yearLabel, gbc);

        int currentYear = Year.now().getValue();
        JSpinner yearSpinner = new JSpinner(new SpinnerNumberModel(currentYear, 1990, currentYear + 5, 1));
        yearSpinner.setFont(new Font("Helvetica", Font.PLAIN, 11));
        gbc.gridy = 8;
        addQualPanel.add(yearSpinner, gbc);

        // Add button
        JButton addQualBtn = new JButton("➕ Add Qualification");
        addQualBtn.setFont(new Font("Helvetica", Font.BOLD, 11));
        addQualBtn.setBackground(new Color(46, 204, 113));
        addQualBtn.setForeground(Color.WHITE);
        addQualBtn.setFocusPainted(false);
        addQualBtn.setBorder(new EmptyBorder(6, 15, 6, 15));
        addQualBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addQualBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                addQualBtn.setBackground(new Color(39, 174, 96));
            }
            public void mouseExited(MouseEvent e) {
                addQualBtn.setBackground(new Color(46, 204, 113));
            }
        });
        addQualBtn.addActionListener(e -> {
            String degree = degreeField.getText().trim();
            String field = fieldField.getText().trim();
            String institution = institutionField.getText().trim();
            int year = (Integer) yearSpinner.getValue();

            if (degree.isEmpty() || field.isEmpty() || institution.isEmpty()) {
                JOptionPane.showMessageDialog(addQualPanel, "❌ Please fill all fields!", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                if (DatabaseHelper.addUserQualification(userId, degree, field, institution, year)) {
                    JOptionPane.showMessageDialog(addQualPanel, "✅ Qualification added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    degreeField.setText("");
                    fieldField.setText("");
                    institutionField.setText("");
                    yearSpinner.setValue(currentYear);
                    contentLayout.show(contentPanel, "PROFILE");
                }
            }
        });
        gbc.gridy = 9;
        gbc.insets = new Insets(20, 15, 10, 15);
        addQualPanel.add(addQualBtn, gbc);

        mainContent.add(addQualPanel, BorderLayout.SOUTH);
        qualsPanel.add(mainContent, BorderLayout.CENTER);
        return qualsPanel;
    }

    // Helper method to create qualification cards
    private JPanel createQualificationCard(String degree, String field, String institution, String year, int userId) {
        Color cardColor = new Color(155, 89, 182);

        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2d.setColor(cardColor);
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            }
        };
        card.setLayout(new BorderLayout(15, 0));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(12, 15, 12, 15));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Left side - Qualification info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel degreeLabel = new JLabel(degree + " in " + field);
        degreeLabel.setFont(new Font("Helvetica", Font.BOLD, 13));
        degreeLabel.setForeground(cardColor);
        infoPanel.add(degreeLabel);
        infoPanel.add(Box.createVerticalStrut(3));

        JLabel instLabel = new JLabel("📍 " + institution);
        instLabel.setFont(new Font("Helvetica", Font.PLAIN, 10));
        instLabel.setForeground(new Color(100, 100, 100));
        infoPanel.add(instLabel);
        infoPanel.add(Box.createVerticalStrut(2));

        JLabel yearLabel = new JLabel("📅 Graduated: " + year);
        yearLabel.setFont(new Font("Helvetica", Font.PLAIN, 10));
        yearLabel.setForeground(new Color(100, 100, 100));
        infoPanel.add(yearLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        // Right side - Delete button
        JButton deleteBtn = new JButton("🗑️ Remove");
        deleteBtn.setFont(new Font("Helvetica", Font.BOLD, 10));
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorder(new EmptyBorder(5, 10, 5, 10));
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                deleteBtn.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(MouseEvent e) {
                deleteBtn.setBackground(new Color(231, 76, 60));
            }
        });
        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(card, "Are you sure you want to remove this qualification?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (DatabaseHelper.removeUserQualification(userId, degree, institution)) {
                    JOptionPane.showMessageDialog(card, "✅ Qualification removed!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    contentLayout.show(contentPanel, "PROFILE");
                }
            }
        });
        card.add(deleteBtn, BorderLayout.EAST);

        return card;
    }

    // Helper method for styled borders
    private javax.swing.border.Border createStyledBorder() {
        return BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 220, 230), 1),
            new EmptyBorder(6, 10, 6, 10)
        );
    }
    private JPanel createSkillRow(String skillName, String proficiency, int userId) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(new Color(250, 250, 250));
        row.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel skillLabel = new JLabel("🔹 " + skillName + " (" + proficiency + ")");
        skillLabel.setFont(new Font("Helvetica", Font.PLAIN, 12));
        row.add(skillLabel, BorderLayout.CENTER);

        JButton removeBtn = createButton("Remove", new Color(200, 50, 50));
        removeBtn.setPreferredSize(new Dimension(80, 30));
        removeBtn.setFont(new Font("Helvetica", Font.BOLD, 10));
        removeBtn.addActionListener(e -> {
            if (DatabaseHelper.removeUserSkill(userId, skillName)) {
                JOptionPane.showMessageDialog(row, "✅ Skill removed!", "Success", JOptionPane.INFORMATION_MESSAGE);
                contentLayout.show(contentPanel, "PROFILE");
            }
        });
        row.add(removeBtn, BorderLayout.EAST);

        return row;
    }

    

    private JPanel createQualificationRow(String degree, String field, String institution, String year, int userId) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(new Color(250, 250, 250));
        row.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel qualLabel = new JLabel("🎓 " + degree + " in " + field + " from " + institution + " (" + year + ")");
        qualLabel.setFont(new Font("Helvetica", Font.PLAIN, 11));
        row.add(qualLabel, BorderLayout.CENTER);

        JButton removeBtn = createButton("Remove", new Color(200, 50, 50));
        removeBtn.setPreferredSize(new Dimension(80, 30));
        removeBtn.setFont(new Font("Helvetica", Font.BOLD, 10));
        removeBtn.addActionListener(e -> {
            if (DatabaseHelper.removeUserQualification(userId, degree, institution)) {
                JOptionPane.showMessageDialog(row, "✅ Qualification removed!", "Success", JOptionPane.INFORMATION_MESSAGE);
                contentLayout.show(contentPanel, "PROFILE");
            }
        });
        row.add(removeBtn, BorderLayout.EAST);

        return row;
        
    }
    private JPanel createActivityDashboardPanel() {
        System.out.println("\n========== createActivityDashboardPanel() START ==========");
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 5, 12, 0));
        panel.setBackground(new Color(245, 245, 245));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        panel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Check if user is logged in - CHECK AT RUNTIME
        User cu = app.getCurrentUser();
        System.out.println("DEBUG: Current user: " + (cu != null ? cu.getName() + " (ID:" + cu.getUserId() + ")" : "NULL"));
        
        if (cu == null) {
            System.out.println("❌ User is NULL - showing placeholder cards");
            
            // Placeholder cards
            panel.add(createActivityCard("--", "Username", false));
            panel.add(createActivityCard("--", "Last Login", false));
            panel.add(createActivityCard("--", "Jobs Applied", false));
            panel.add(createActivityCard("--", "Jobs Saved", false));
            panel.add(createActivityCard("--", "Skills", false));
            
            System.out.println("========== createActivityDashboardPanel() END (NO USER) ==========\n");
            return panel;
        }

        System.out.println("✅ User IS logged in: " + cu.getName());
        
        // Fetch activity stats from database
        int userId = cu.getUserId();
        System.out.println("Fetching stats for userId: " + userId);
        
        Map<String, String> stats = DatabaseHelper.getUserActivityStats(userId);
        
        System.out.println("Stats retrieved from database:");
        for (Map.Entry<String, String> entry : stats.entrySet()) {
            System.out.println("  → " + entry.getKey() + ": " + entry.getValue());
        }

        // Extract values with safe defaults
        String userName = stats.getOrDefault("name", "User");
        String lastLogin = formatLastLogin(stats.getOrDefault("last_login", "Never"));
        String jobsApplied = stats.getOrDefault("jobs_applied", "0");
        String jobsSaved = stats.getOrDefault("jobs_saved", "0");
        String skillsCount = stats.getOrDefault("skills", "0");

        System.out.println("\n✅ Formatted values:");
        System.out.println("  → Username: " + userName);
        System.out.println("  → Last Login: " + lastLogin);
        System.out.println("  → Jobs Applied: " + jobsApplied);
        System.out.println("  → Jobs Saved: " + jobsSaved);
        System.out.println("  → Skills: " + skillsCount);

        // Create 5 activity cards with REAL data
        panel.add(createActivityCard("👤 " + userName, "Username", true));
        panel.add(createActivityCard(lastLogin, "Last Login", true));
        panel.add(createActivityCard(jobsApplied, "Jobs Applied", true));
        panel.add(createActivityCard(jobsSaved, "Jobs Saved", true));
        panel.add(createActivityCard(skillsCount, "Skills", true));

        System.out.println("========== createActivityDashboardPanel() END (SUCCESS) ==========\n");
        return panel;
    }    private JPanel createActivityCard(String value, String label, boolean isActive) {
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                
                // Border
                Color borderColor = isActive ? new Color(52, 152, 219) : new Color(200, 200, 200);
                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            }
        };
        
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Helvetica", Font.BOLD, isActive ? 22 : 16));
        valueLabel.setForeground(isActive ? new Color(52, 152, 219) : new Color(150, 150, 150));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(valueLabel, BorderLayout.CENTER);

        // Label
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Helvetica", Font.PLAIN, 11));
        labelComponent.setForeground(isActive ? new Color(80, 80, 80) : new Color(150, 150, 150));
        labelComponent.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(labelComponent, BorderLayout.SOUTH);

        return card;
    }

    // ========== HELPER: Format last login time ==========

    private String formatLastLogin(String lastLoginStr) {
        if (lastLoginStr == null || lastLoginStr.isEmpty() || lastLoginStr.equals("Never")) {
            return "Now";
        }
        
        try {
            // Parse timestamp
            java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(lastLoginStr);
            java.util.Date date = new java.util.Date(timestamp.getTime());
            java.util.Date now = new java.util.Date();
            
            long diffInMillis = now.getTime() - date.getTime();
            long diffInSeconds = diffInMillis / 1000;
            long diffInMinutes = diffInSeconds / 60;
            long diffInHours = diffInMinutes / 60;
            long diffInDays = diffInHours / 24;
            
            if (diffInSeconds < 60) {
                return "Just now";
            } else if (diffInMinutes < 60) {
                return diffInMinutes + "m ago";
            } else if (diffInHours < 24) {
                return diffInHours + "h ago";
            } else if (diffInDays < 7) {
                return diffInDays + "d ago";
            } else {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd");
                return sdf.format(date);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error formatting time: " + e.getMessage());
            return "Recently";
        }
    }
    public void refreshProfilePanel() {
        System.out.println("DEBUG: refreshProfilePanel called");
        
        // Remove old profile panel
        contentPanel.remove(6); // Profile is at index 6
        
        // Add new one
        contentPanel.add(createProfilePanel(), "PROFILE");
        
        contentPanel.revalidate();
        contentPanel.repaint();
        
        // Show it
        contentLayout.show(contentPanel, "PROFILE");
    }
    private JPanel createInfoCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2d.setColor(accentColor);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
        titleLabel.setForeground(accentColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Helvetica", Font.PLAIN, 14));
        valueLabel.setForeground(new Color(40, 40, 40));
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(valueLabel);

        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setOpaque(true);
                card.setBackground(new Color(250, 250, 250));
                card.repaint();
            }
            public void mouseExited(MouseEvent e) {
                card.setOpaque(false);
                card.repaint();
            }
        });

        return card;
    }

    // Helper method to get initials
    private String getInitials(String name) {
        if (name == null || name.isEmpty()) return "U";
        String[] parts = name.trim().split("\\s+");
        if (parts.length >= 2) {
            return (parts[0].charAt(0) + "" + parts[parts.length - 1].charAt(0)).toUpperCase();
        }
        return (name.length() > 0 ? name.charAt(0) : "U") + "";
    }
 }