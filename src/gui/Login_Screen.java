package gui;

import dbConnection.DatabaseConnection; // Import your DatabaseConnection class
import org.mindrot.jbcrypt.BCrypt; // Import BCrypt for password hashing

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login_Screen extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public Login_Screen() {
        this.setSize(600, 450);
        this.setUndecorated(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        // Top Panel
        JPanel tPnel = new JPanel();
        tPnel.setBackground(new Color(23, 56, 34));
        tPnel.setLayout(new FlowLayout()); // Use FlowLayout for the title
        JLabel tLabel = new JLabel("Login");
        tLabel.setFont(new Font("Arial", Font.BOLD, 24));
        tLabel.setForeground(Color.WHITE);
        tPnel.add(tLabel);
        this.add(tPnel, BorderLayout.NORTH);

        // Main Panel
        JPanel mPanel = new JPanel();
        mPanel.setBackground(new Color(26, 65, 43));
        this.add(mPanel, BorderLayout.CENTER);
        mPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username Label
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        mPanel.add(usernameLabel, gbc);

        // Username TextField
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        mPanel.add(usernameField, gbc);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        mPanel.add(passwordLabel, gbc);

        // Password TextField
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        mPanel.add(passwordField, gbc);

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mPanel.add(loginButton, gbc);

        // Sign Up Label
        JLabel signUpLabel = new JLabel("Don't have an account? Sign Up");
        signUpLabel.setForeground(Color.WHITE);
        signUpLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new signupScreen(); // Open the SignUp screen
                dispose(); // Close the login screen
            }
        });
        gbc.gridx = 1;
        mPanel.add(signUpLabel, gbc);

        // Reset Password Button
        JButton resetPasswordButton = new JButton("Reset Password");
        resetPasswordButton.addActionListener(e ->{
            this.dispose();
            new PasswordResetScreen();
        });

        gbc.gridx = 1;
        gbc.gridy = 3;
        mPanel.add(resetPasswordButton, gbc);

        // Bottom Panel
        JPanel bPanel = new JPanel();
        bPanel.setPreferredSize(new Dimension(600, 50));
        bPanel.setBackground(new Color(12, 24, 23));
        this.add(bPanel, BorderLayout.SOUTH);

        // Make the frame visible
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Login")) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            // Validate input fields
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Both fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check credentials against the database
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "SELECT password FROM users WHERE username = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username); // Corrected line
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    // Verify the password
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        JOptionPane.showMessageDialog(this, "Login successful!");
                        // Proceed to the main application or dashboard
                        new LibraryDashboard(); // Uncomment and implement this
                        dispose(); // Close the login screen
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "User  not found.", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error occurred.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}