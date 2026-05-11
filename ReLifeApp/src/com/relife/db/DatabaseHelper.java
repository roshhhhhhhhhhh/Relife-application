package com.relife.db;

import java.sql.*;
import java.util.*;

import com.relife.model.User;


public class DatabaseHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/relife_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root17";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver loaded successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL Driver not found: " + e.getMessage());
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create users table with all needed columns
        	String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
        		    "id INT AUTO_INCREMENT PRIMARY KEY, " +
        		    "username VARCHAR(100) UNIQUE NOT NULL, " +
        		    "email VARCHAR(100) UNIQUE NOT NULL, " +
        		    "phone VARCHAR(10) NOT NULL, " +
        		    "gender VARCHAR(20) NOT NULL, " +
        		    "password VARCHAR(100) NOT NULL, " +
        		    "bio VARCHAR(500) DEFAULT '', " +
        		    "location VARCHAR(100) DEFAULT '', " +
        		    "experience_years INT DEFAULT 0, " +
        		    "profile_picture VARCHAR(255) DEFAULT '', " +
        		    "status VARCHAR(20) DEFAULT 'ACTIVE', " +              // ✅ NEW
        		    "deleted_at TIMESTAMP NULL, " +                         // ✅ NEW
        		    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
    		stmt.executeUpdate(createUsersTable);
     // (safe) add 'last_login' column if not present - ignore error if exists
    		try {
    		    String alterStatus = "ALTER TABLE users ADD COLUMN status VARCHAR(20) DEFAULT 'ACTIVE'";
    		    stmt.executeUpdate(alterStatus);
    		    System.out.println("✅ Added status column to users table");
    		} catch (SQLException ex) {
    		    System.out.println("ℹ️ Status column already exists");
    		}
    		try {
    		    String alterDeleted = "ALTER TABLE users ADD COLUMN deleted_at TIMESTAMP NULL";
    		    stmt.executeUpdate(alterDeleted);
    		    System.out.println("✅ Added deleted_at column to users table");
    		} catch (SQLException ex) {
    		    System.out.println("ℹ️ Deleted_at column already exists");
    		}
            try {
                String alter = "ALTER TABLE users ADD COLUMN last_login DATETIME NULL";
                stmt.executeUpdate(alter);
            } catch (SQLException ex) {
                // ignore — column likely already exists
                System.out.println("last_login column exists or ALTER failed: " + ex.getMessage());
            }

            
            String createSavedJobsTable = "CREATE TABLE IF NOT EXISTS saved_jobs (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "job_title VARCHAR(100) NOT NULL, " +
                "company VARCHAR(100) NOT NULL, " +
                "location VARCHAR(100) NOT NULL, " +
                "salary VARCHAR(50) NOT NULL, " +
                "job_type VARCHAR(50) NOT NULL, " +
                "date_posted VARCHAR(100) NOT NULL, " +
                "saved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)";
            stmt.executeUpdate(createSavedJobsTable);
            
            String createApplicationsTable = "CREATE TABLE IF NOT EXISTS job_applications (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "job_title VARCHAR(100) NOT NULL, " +
                "company VARCHAR(100) NOT NULL, " +
                "status VARCHAR(50) DEFAULT 'Applied', " +
                "applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)";
            stmt.executeUpdate(createApplicationsTable);
            
            String createSkillsTable = "CREATE TABLE IF NOT EXISTS user_skills (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "skill_name VARCHAR(100) NOT NULL, " +
                "proficiency_level VARCHAR(50) NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)";
            stmt.executeUpdate(createSkillsTable);
            
            String createQualificationsTable = "CREATE TABLE IF NOT EXISTS user_qualifications (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "degree VARCHAR(100) NOT NULL, " +
                "field_of_study VARCHAR(100) NOT NULL, " +
                "institution VARCHAR(100) NOT NULL, " +
                "graduation_year INT NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)";
            stmt.executeUpdate(createQualificationsTable);
            
            String createAdminTable = "CREATE TABLE IF NOT EXISTS admins (" +
            	    "id INT AUTO_INCREMENT PRIMARY KEY, " +
            	    "username VARCHAR(100) UNIQUE NOT NULL, " +
            	    "password VARCHAR(100) NOT NULL, " +
            	    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            	stmt.executeUpdate(createAdminTable);

            	// Insert default admin (if not exists)
            	String insertAdmin = "INSERT IGNORE INTO admins (username, password) VALUES ('admin', 'admin123')";
            	stmt.executeUpdate(insertAdmin);
            	
            	

            
            System.out.println("✅ Database tables created/verified successfully!");
            
        } catch (SQLException e) {
            System.out.println("❌ Database initialization error: " + e.getMessage());
        }
    }
    
    public static boolean registerUser(String username, String email, String phone, String gender, String password) {
        String sql = "INSERT INTO users (username, email, phone, gender, password) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.setString(4, gender);
            pstmt.setString(5, password);
            
            pstmt.executeUpdate();
            System.out.println("✅ User registered: " + username);
            return true;
            
        } catch (SQLException e) {
            System.out.println("❌ Registration error: " + e.getMessage());
            return false;
        }
    }
    
    public static int loginUser(String username, String password) {
        System.out.println("DEBUG: Attempting login for username: " + username);
        
        // ✅ CHANGE 1: Query now INCLUDES status column
        String sql = "SELECT id, password, status FROM users WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // ✅ CHANGE 2: Get status from database
                String status = rs.getString("status");
                System.out.println("DEBUG: User status = " + status);
                
                // ✅ CHANGE 3: Check if account is DELETED
                if ("DELETED".equals(status)) {
                    System.out.println("❌ User account has been deleted!");
                    return -2;  // ✅ Return -2 to indicate DELETED account
                }
                
                // Continue with normal password check
                String storedPassword = rs.getString("password");
                if (storedPassword.equals(password)) {
                    int userId = rs.getInt("id");
                    System.out.println("✅ Login successful for: " + username);
                    return userId;  // ✅ Return positive number = SUCCESS
                }
            }
            System.out.println("❌ Invalid credentials!");
            return -1;  // ✅ Return -1 = Wrong password or user not found
            
        } catch (SQLException e) {
            System.out.println("❌ Login error: " + e.getMessage());
            return -1;
        }
    }

    public static boolean softDeleteUser(int userId) {
        System.out.println("\n🔄 Soft deleting user ID: " + userId);
        
        // ✅ UPDATE status to DELETED and record when it was deleted
        String sql = "UPDATE users SET status = 'DELETED', deleted_at = NOW() WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✅ User ID " + userId + " marked as DELETED");
                return true;
            } else {
                System.out.println("❌ User ID " + userId + " not found");
                return false;
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error soft deleting user: " + e.getMessage());
            return false;
        }
    }

    public static boolean adminLogin(String username, String password) {
        String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("✅ Admin login successful!");
                return true;
            } else {
                System.out.println("❌ Invalid admin credentials!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error logging in admin: " + e.getMessage());
            return false;
        }
    }

    
    public static String getUserName(int userId) {
        String sql = "SELECT username FROM users WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String name = rs.getString("username");
                if (name != null && !name.trim().isEmpty()) {
                    System.out.println("✅ Got username: " + name);
                    return name.trim();
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error getting username: " + e.getMessage());
        }
        return "User";
    }

    public static String getUserEmail(int userId) {
        String sql = "SELECT email FROM users WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String email = rs.getString("email");
                System.out.println("✅ Got email: " + email);
                return email != null ? email : "";
            }
        } catch (SQLException e) {
            System.out.println("❌ Error getting email: " + e.getMessage());
        }
        return "";
    }
    
    public static boolean saveJob(int userId, String jobTitle, String company, String location, String salary, String jobType, String datePosted) {
        String checkSql = "SELECT id FROM saved_jobs WHERE user_id = ? AND job_title = ? AND company = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            
            checkStmt.setInt(1, userId);
            checkStmt.setString(2, jobTitle);
            checkStmt.setString(3, company);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("❌ Job already saved!");
                return false;
            }
            
            String sql = "INSERT INTO saved_jobs (user_id, job_title, company, location, salary, job_type, date_posted) VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                pstmt.setString(2, jobTitle);
                pstmt.setString(3, company);
                pstmt.setString(4, location);
                pstmt.setString(5, salary);
                pstmt.setString(6, jobType);
                pstmt.setString(7, datePosted);
                
                pstmt.executeUpdate();
                System.out.println("✅ Job saved: " + jobTitle);
                return true;
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error saving job: " + e.getMessage());
            return false;
        }
    }
    
    public static String[][] getSavedJobs(int userId) {
        String sql = "SELECT job_title, company, location, salary, job_type, date_posted FROM saved_jobs WHERE user_id = ? ORDER BY saved_at DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            List<String[]> jobList = new ArrayList<>();
            
            while (rs.next()) {
                String[] job = new String[6];
                job[0] = rs.getString("job_title");
                job[1] = rs.getString("company");
                job[2] = rs.getString("location");
                job[3] = rs.getString("salary");
                job[4] = rs.getString("job_type");
                job[5] = rs.getString("date_posted");
                jobList.add(job);
            }
            
            System.out.println("✅ Retrieved " + jobList.size() + " saved jobs");
            return jobList.toArray(new String[0][6]);
            
        } catch (SQLException e) {
            System.out.println("❌ Error fetching saved jobs: " + e.getMessage());
            return new String[0][6];
        }
    }
    
    public static boolean removeSavedJob(int userId, String jobTitle, String company) {
        String sql = "DELETE FROM saved_jobs WHERE user_id = ? AND job_title = ? AND company = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, jobTitle);
            pstmt.setString(3, company);
            
            pstmt.executeUpdate();
            System.out.println("✅ Job removed: " + jobTitle);
            return true;
            
        } catch (SQLException e) {
            System.out.println("❌ Error removing job: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean applyForJob(int userId, String jobTitle, String company) {
        String checkSql = "SELECT id FROM job_applications WHERE user_id = ? AND job_title = ? AND company = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            
            checkStmt.setInt(1, userId);
            checkStmt.setString(2, jobTitle);
            checkStmt.setString(3, company);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("❌ Already applied!");
                return false;
            }
            
            String sql = "INSERT INTO job_applications (user_id, job_title, company) VALUES (?, ?, ?)";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                pstmt.setString(2, jobTitle);
                pstmt.setString(3, company);
                
                pstmt.executeUpdate();
                System.out.println("✅ Application submitted: " + jobTitle);
                return true;
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error applying: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean saveUserProfile(int userId, String bio, String location, int experienceYears, String profilePicture) {
        String sql = "UPDATE users SET bio = ?, location = ?, experience_years = ?, profile_picture = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, bio);
            pstmt.setString(2, location);
            pstmt.setInt(3, experienceYears);
            pstmt.setString(4, profilePicture);
            pstmt.setInt(5, userId);
            
            pstmt.executeUpdate();
            System.out.println("✅ Profile updated!");
            return true;
            
        } catch (SQLException e) {
            System.out.println("❌ Error updating profile: " + e.getMessage());
            return false;
        }
    }

    public static boolean addUserSkill(int userId, String skillName, String proficiencyLevel) {
        String sql = "INSERT INTO user_skills (user_id, skill_name, proficiency_level) VALUES (?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, skillName);
            pstmt.setString(3, proficiencyLevel);
            
            pstmt.executeUpdate();
            System.out.println("✅ Skill added!");
            return true;
            
        } catch (SQLException e) {
            System.out.println("❌ Error adding skill: " + e.getMessage());
            return false;
        }
    }

    public static String[][] getUserSkills(int userId) {
        String sql = "SELECT skill_name, proficiency_level FROM user_skills WHERE user_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            List<String[]> skillsList = new ArrayList<>();
            
            while (rs.next()) {
                String[] skill = new String[2];
                skill[0] = rs.getString("skill_name");
                skill[1] = rs.getString("proficiency_level");
                skillsList.add(skill);
            }
            
            return skillsList.toArray(new String[0][2]);
            
        } catch (SQLException e) {
            System.out.println("❌ Error fetching skills: " + e.getMessage());
            return new String[0][2];
        }
    }

    public static boolean removeUserSkill(int userId, String skillName) {
        String sql = "DELETE FROM user_skills WHERE user_id = ? AND skill_name = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, skillName);
            
            pstmt.executeUpdate();
            System.out.println("✅ Skill removed!");
            return true;
            
        } catch (SQLException e) {
            System.out.println("❌ Error removing skill: " + e.getMessage());
            return false;
        }
    }

    public static boolean addUserQualification(int userId, String degree, String fieldOfStudy, String institution, int graduationYear) {
        String sql = "INSERT INTO user_qualifications (user_id, degree, field_of_study, institution, graduation_year) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, degree);
            pstmt.setString(3, fieldOfStudy);
            pstmt.setString(4, institution);
            pstmt.setInt(5, graduationYear);
            
            pstmt.executeUpdate();
            System.out.println("✅ Qualification added!");
            return true;
            
        } catch (SQLException e) {
            System.out.println("❌ Error adding qualification: " + e.getMessage());
            return false;
        }
    }

    public static String[][] getUserQualifications(int userId) {
        String sql = "SELECT degree, field_of_study, institution, graduation_year FROM user_qualifications WHERE user_id = ? ORDER BY graduation_year DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            List<String[]> qualsList = new ArrayList<>();
            
            while (rs.next()) {
                String[] qual = new String[4];
                qual[0] = rs.getString("degree");
                qual[1] = rs.getString("field_of_study");
                qual[2] = rs.getString("institution");
                qual[3] = String.valueOf(rs.getInt("graduation_year"));
                qualsList.add(qual);
            }
            
            return qualsList.toArray(new String[0][4]);
            
        } catch (SQLException e) {
            System.out.println("❌ Error fetching qualifications: " + e.getMessage());
            return new String[0][4];
        }
    }

    public static boolean removeUserQualification(int userId, String degree, String institution) {
        String sql = "DELETE FROM user_qualifications WHERE user_id = ? AND degree = ? AND institution = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, degree);
            pstmt.setString(3, institution);
            
            pstmt.executeUpdate();
            System.out.println("✅ Qualification removed!");
            return true;
            
        } catch (SQLException e) {
            System.out.println("❌ Error removing qualification: " + e.getMessage());
            return false;
        }
    }
 // Add this method to DatabaseHelper.java class

    public static boolean updateUserPhone(int userId, String phone) {
        String sql = "UPDATE users SET phone = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, phone);
            pstmt.setInt(2, userId);
            
            pstmt.executeUpdate();
            System.out.println("✅ Phone updated!");
            return true;
            
        } catch (SQLException e) {
            System.out.println("❌ Error updating phone: " + e.getMessage());
            return false;
        }
    }
    public static String[][] getAllUsers() {
        // ✅ ONLY select ACTIVE users (exclude DELETED)
        String sql = "SELECT id, username, email, phone, gender, created_at FROM users WHERE status = 'ACTIVE' ORDER BY created_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            List<String[]> userList = new ArrayList<>();
            while (rs.next()) {
                userList.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("gender"),
                    rs.getString("created_at")
                });
            }
            return userList.toArray(new String[0][6]);
        } catch (SQLException e) {
            System.out.println("❌ Error fetching users: " + e.getMessage());
            return new String[0][6];
        }
    }
    public static String[][] getAllUserQualifications() {
        String sql = "SELECT u.username, q.degree, q.field_of_study, q.institution, q.graduation_year, q.created_at " +
                     "FROM user_qualifications q " +
                     "JOIN users u ON q.user_id = u.id " +
                     "ORDER BY q.created_at DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            List<String[]> data = new ArrayList<>();
            while (rs.next()) {
                data.add(new String[]{
                    rs.getString("username"),
                    rs.getString("degree"),
                    rs.getString("field_of_study"),
                    rs.getString("institution"),
                    String.valueOf(rs.getInt("graduation_year")),
                    rs.getString("created_at")
                });
            }
            return data.toArray(new String[0][6]);
        } catch (SQLException e) {
            System.out.println("❌ Error fetching qualifications: " + e.getMessage());
            return new String[0][6];
        }
    }
    public static boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            System.out.println("✅ User deleted with ID: " + userId);
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Error deleting user: " + e.getMessage());
            return false;
        }
    }
    public static String[] getUserProfile(int userId) {
        System.out.println("\n========== DEBUG: getUserProfile START ==========");
        System.out.println("Querying profile for userId: " + userId);
        
        String sql = "SELECT username, email, phone, bio, location, experience_years FROM users WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            System.out.println("Connection established: " + (conn != null ? "YES" : "NO"));
            System.out.println("SQL Query: " + sql);
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            System.out.println("Query executed, checking ResultSet...");
            
            if (rs.next()) {
                String username = rs.getString("username");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String bio = rs.getString("bio");
                String location = rs.getString("location");
                int experience_years = rs.getInt("experience_years");
                
                System.out.println("✅ Data retrieved:");
                System.out.println("   - username: " + username);
                System.out.println("   - email: " + email);
                System.out.println("   - phone: " + phone);
                System.out.println("   - bio: " + (bio != null && !bio.isEmpty() ? bio.substring(0, Math.min(50, bio.length())) + "..." : "(empty)"));
                System.out.println("   - location: " + location);
                System.out.println("   - experience_years: " + experience_years);
                
                String[] profile = new String[]{
                    username != null ? username : "User",
                    email != null ? email : "",
                    phone != null ? phone : "",
                    bio != null ? bio : "",
                    location != null ? location : "",
                    String.valueOf(experience_years)
                };
                
                System.out.println("========== DEBUG: getUserProfile END (SUCCESS) ==========\n");
                return profile;
            } else {
                System.out.println("❌ No user found with ID: " + userId);
                System.out.println("========== DEBUG: getUserProfile END (NO DATA) ==========\n");
                return new String[]{"User", "", "", "", "", "0"};
            }
            
        } catch (SQLException e) {
            System.out.println("❌ SQL Exception: " + e.getMessage());
            e.printStackTrace();
            System.out.println("========== DEBUG: getUserProfile END (ERROR) ==========\n");
            return new String[]{"User", "", "", "", "", "0"};
        }
    }
    public static boolean updateUserPhone(int userId, String phone) {
        System.out.println("\nDEBUG: updateUserPhone called - userId: " + userId + ", phone: " + phone);
        
        String sql = "UPDATE users SET phone = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, phone);
            pstmt.setInt(2, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✅ Phone updated! Rows affected: " + rowsAffected);
            return true;
            
        } catch (SQLException e) {
            System.out.println("❌ Error updating phone: " + e.getMessage());
            return false;
        }
    }
    public static boolean updateLastLogin(int userId) {
        String sql = "UPDATE users SET last_login = NOW() WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Error updating last_login: " + e.getMessage());
            return false;
        }
    }

 // REPLACE the getUserActivityStats() method in DatabaseHelper.java with this FIXED version

    /**
     * Returns basic activity stats for a user as a Map.
     * Keys: name, email, last_login, jobs_applied, jobs_saved, qualifications, skills, registered_at
     */
    public static Map<String, String> getUserActivityStats(int userId) {
        System.out.println("\n========== getUserActivityStats() START ==========");
        System.out.println("Fetching stats for userId: " + userId);
        
        Map<String, String> stats = new HashMap<>();
        
        try (Connection conn = getConnection()) {
            System.out.println("✅ Database connection established");
            
            // 1. Get username + email + timestamps
            System.out.println("\n1️⃣ Fetching user basic info...");
            String uSql = "SELECT username, email, created_at, last_login FROM users WHERE id = ?";
            try (PreparedStatement pst = conn.prepareStatement(uSql)) {
                pst.setInt(1, userId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        String name = rs.getString("username");
                        String email = rs.getString("email");
                        Timestamp created = rs.getTimestamp("created_at");
                        Timestamp last = rs.getTimestamp("last_login");
                        
                        stats.put("name", name != null ? name : "User");
                        stats.put("email", email != null ? email : "");
                        stats.put("registered_at", created != null ? created.toString() : "");
                        stats.put("last_login", last != null ? last.toString() : "Never");
                        
                        System.out.println("   ✅ name: " + stats.get("name"));
                        System.out.println("   ✅ email: " + stats.get("email"));
                        System.out.println("   ✅ last_login: " + stats.get("last_login"));
                    } else {
                        System.out.println("   ❌ No user found with ID: " + userId);
                        stats.put("name", "User");
                        stats.put("email", "");
                        stats.put("last_login", "Never");
                    }
                }
            } catch (SQLException e) {
                System.out.println("   ❌ Error fetching user info: " + e.getMessage());
                e.printStackTrace();
            }

            // 2. Count jobs applied
            System.out.println("\n2️⃣ Counting jobs applied...");
            String aSql = "SELECT COUNT(*) AS cnt FROM job_applications WHERE user_id = ?";
            try (PreparedStatement pst = conn.prepareStatement(aSql)) {
                pst.setInt(1, userId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt("cnt");
                        stats.put("jobs_applied", String.valueOf(count));
                        System.out.println("   ✅ jobs_applied: " + count);
                    } else {
                        stats.put("jobs_applied", "0");
                        System.out.println("   ✅ jobs_applied: 0 (no results)");
                    }
                }
            } catch (SQLException e) {
                System.out.println("   ❌ Error counting jobs applied: " + e.getMessage());
                stats.put("jobs_applied", "0");
            }

            // 3. Count jobs saved
            System.out.println("\n3️⃣ Counting jobs saved...");
            String sSql = "SELECT COUNT(*) AS cnt FROM saved_jobs WHERE user_id = ?";
            try (PreparedStatement pst = conn.prepareStatement(sSql)) {
                pst.setInt(1, userId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt("cnt");
                        stats.put("jobs_saved", String.valueOf(count));
                        System.out.println("   ✅ jobs_saved: " + count);
                    } else {
                        stats.put("jobs_saved", "0");
                        System.out.println("   ✅ jobs_saved: 0 (no results)");
                    }
                }
            } catch (SQLException e) {
                System.out.println("   ❌ Error counting jobs saved: " + e.getMessage());
                stats.put("jobs_saved", "0");
            }

            // 4. Count qualifications
            System.out.println("\n4️⃣ Counting qualifications...");
            String qSql = "SELECT COUNT(*) AS cnt FROM user_qualifications WHERE user_id = ?";
            try (PreparedStatement pst = conn.prepareStatement(qSql)) {
                pst.setInt(1, userId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt("cnt");
                        stats.put("qualifications", String.valueOf(count));
                        System.out.println("   ✅ qualifications: " + count);
                    } else {
                        stats.put("qualifications", "0");
                        System.out.println("   ✅ qualifications: 0 (no results)");
                    }
                }
            } catch (SQLException e) {
                System.out.println("   ❌ Error counting qualifications: " + e.getMessage());
                stats.put("qualifications", "0");
            }

            // 5. Count skills
            System.out.println("\n5️⃣ Counting skills...");
            String skSql = "SELECT COUNT(*) AS cnt FROM user_skills WHERE user_id = ?";
            try (PreparedStatement pst = conn.prepareStatement(skSql)) {
                pst.setInt(1, userId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt("cnt");
                        stats.put("skills", String.valueOf(count));
                        System.out.println("   ✅ skills: " + count);
                    } else {
                        stats.put("skills", "0");
                        System.out.println("   ✅ skills: 0 (no results)");
                    }
                }
            } catch (SQLException e) {
                System.out.println("   ❌ Error counting skills: " + e.getMessage());
                stats.put("skills", "0");
            }

            System.out.println("\n📊 Final Stats Summary:");
            for (Map.Entry<String, String> entry : stats.entrySet()) {
                System.out.println("   → " + entry.getKey() + ": " + entry.getValue());
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Main database connection error: " + e.getMessage());
            e.printStackTrace();
            
            // Set defaults on error
            stats.put("name", "User");
            stats.put("email", "");
            stats.put("last_login", "Never");
            stats.put("jobs_applied", "0");
            stats.put("jobs_saved", "0");
            stats.put("qualifications", "0");
            stats.put("skills", "0");
        }
        
        System.out.println("========== getUserActivityStats() END ==========\n");
        return stats;
    }
    public static List<String[]> getAllSavedJobs() {
        String sql = """
            SELECT u.id, u.username, u.email, u.phone, 
                   s.job_title, s.company, s.location, s.salary, s.job_type, s.date_posted 
            FROM saved_jobs s 
            JOIN users u ON s.user_id = u.id 
            ORDER BY s.saved_at DESC
        """;

        List<String[]> results = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                results.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("job_title"),
                    rs.getString("company"),
                    rs.getString("location"),
                    rs.getString("salary"),
                    rs.getString("job_type"),
                    rs.getString("date_posted")
                });
            }
            System.out.println("✅ Admin fetched " + results.size() + " saved jobs.");
        } catch (SQLException e) {
            System.out.println("❌ Error fetching saved jobs for admin: " + e.getMessage());
        }
        return results;
    }

    // ✅ Fetch all applied jobs with user details
    public static List<String[]> getAllAppliedJobs() {
        String sql = """
            SELECT u.id, u.username, u.email, u.phone, 
                   a.job_title, a.company, a.status, a.applied_at 
            FROM job_applications a 
            JOIN users u ON a.user_id = u.id 
            ORDER BY a.applied_at DESC
        """;

        List<String[]> results = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                results.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("job_title"),
                    rs.getString("company"),
                    rs.getString("status"),
                    rs.getString("applied_at")
                });
            }
            System.out.println("✅ Admin fetched " + results.size() + " applied jobs.");
        } catch (SQLException e) {
            System.out.println("❌ Error fetching applied jobs for admin: " + e.getMessage());
        }
        return results;
    }
 // Add this method to DatabaseHelper.java to debug what's in your database

    public static void diagnosticCheckDatabase(int userId) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("DATABASE DIAGNOSTIC CHECK - userId: " + userId);
        System.out.println("=".repeat(70));
        
        try (Connection conn = getConnection()) {
            
            // Check 1: User exists?
            System.out.println("\n1️⃣ CHECKING USERS TABLE:");
            String userSql = "SELECT * FROM users WHERE id = ?";
            try (PreparedStatement pst = conn.prepareStatement(userSql)) {
                pst.setInt(1, userId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("   ✅ User found!");
                        System.out.println("      - username: " + rs.getString("username"));
                        System.out.println("      - email: " + rs.getString("email"));
                        System.out.println("      - created_at: " + rs.getTimestamp("created_at"));
                        System.out.println("      - last_login: " + rs.getTimestamp("last_login"));
                    } else {
                        System.out.println("   ❌ User NOT found in database!");
                    }
                }
            }
            
            // Check 2: Job applications
            System.out.println("\n2️⃣ CHECKING JOB_APPLICATIONS TABLE:");
            String appSql = "SELECT COUNT(*) as cnt FROM job_applications WHERE user_id = ?";
            try (PreparedStatement pst = conn.prepareStatement(appSql)) {
                pst.setInt(1, userId);
                try (ResultSet rs = pst.executeQuery()) {
                    rs.next();
                    int count = rs.getInt("cnt");
                    System.out.println("   📊 Total applications: " + count);
                    if (count > 0) {
                        String detailSql = "SELECT * FROM job_applications WHERE user_id = ?";
                        try (PreparedStatement pst2 = conn.prepareStatement(detailSql)) {
                            pst2.setInt(1, userId);
                            try (ResultSet rs2 = pst2.executeQuery()) {
                                while (rs2.next()) {
                                    System.out.println("      - " + rs2.getString("job_title") + " at " + rs2.getString("company") + " (" + rs2.getTimestamp("applied_at") + ")");
                                }
                            }
                        }
                    }
                }
            }
            
            // Check 3: Saved jobs
            System.out.println("\n3️⃣ CHECKING SAVED_JOBS TABLE:");
            String savedSql = "SELECT COUNT(*) as cnt FROM saved_jobs WHERE user_id = ?";
            try (PreparedStatement pst = conn.prepareStatement(savedSql)) {
                pst.setInt(1, userId);
                try (ResultSet rs = pst.executeQuery()) {
                    rs.next();
                    int count = rs.getInt("cnt");
                    System.out.println("   📊 Total saved jobs: " + count);
                    if (count > 0) {
                        String detailSql = "SELECT * FROM saved_jobs WHERE user_id = ?";
                        try (PreparedStatement pst2 = conn.prepareStatement(detailSql)) {
                            pst2.setInt(1, userId);
                            try (ResultSet rs2 = pst2.executeQuery()) {
                                while (rs2.next()) {
                                    System.out.println("      - " + rs2.getString("job_title") + " at " + rs2.getString("company") + " (" + rs2.getTimestamp("saved_at") + ")");
                                }
                            }
                        }
                    }
                }
            }
            
            // Check 4: Skills
            System.out.println("\n4️⃣ CHECKING USER_SKILLS TABLE:");
            String skillsSql = "SELECT COUNT(*) as cnt FROM user_skills WHERE user_id = ?";
            try (PreparedStatement pst = conn.prepareStatement(skillsSql)) {
                pst.setInt(1, userId);
                try (ResultSet rs = pst.executeQuery()) {
                    rs.next();
                    int count = rs.getInt("cnt");
                    System.out.println("   📊 Total skills: " + count);
                    if (count > 0) {
                        String detailSql = "SELECT * FROM user_skills WHERE user_id = ?";
                        try (PreparedStatement pst2 = conn.prepareStatement(detailSql)) {
                            pst2.setInt(1, userId);
                            try (ResultSet rs2 = pst2.executeQuery()) {
                                while (rs2.next()) {
                                    System.out.println("      - " + rs2.getString("skill_name") + " (" + rs2.getString("proficiency_level") + ")");
                                }
                            }
                        }
                    }
                }
            }
            
            // Check 5: Qualifications
            System.out.println("\n5️⃣ CHECKING USER_QUALIFICATIONS TABLE:");
            String qualsSql = "SELECT COUNT(*) as cnt FROM user_qualifications WHERE user_id = ?";
            try (PreparedStatement pst = conn.prepareStatement(qualsSql)) {
                pst.setInt(1, userId);
                try (ResultSet rs = pst.executeQuery()) {
                    rs.next();
                    int count = rs.getInt("cnt");
                    System.out.println("   📊 Total qualifications: " + count);
                    if (count > 0) {
                        String detailSql = "SELECT * FROM user_qualifications WHERE user_id = ?";
                        try (PreparedStatement pst2 = conn.prepareStatement(detailSql)) {
                            pst2.setInt(1, userId);
                            try (ResultSet rs2 = pst2.executeQuery()) {
                                while (rs2.next()) {
                                    System.out.println("      - " + rs2.getString("degree") + " in " + rs2.getString("field_of_study") + " from " + rs2.getString("institution"));
                                }
                            }
                        }
                    }
                }
            }
            
            System.out.println("\n" + "=".repeat(70) + "\n");
            
        } catch (SQLException e) {
            System.out.println("❌ Error during diagnostic: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static String[][] searchUserById(int userId) {
        String query = "SELECT id, username, email, phone, gender, created_at FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            List<String[]> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new String[]{
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("gender"),
                        rs.getString("created_at")
                });
            }

            return list.toArray(new String[0][]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String[][] searchUserByName(String name) {
        String query = "SELECT id, username, email, phone, gender, created_at FROM users WHERE username LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            List<String[]> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new String[]{
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("gender"),
                        rs.getString("created_at")
                });
            }

            return list.toArray(new String[0][]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}