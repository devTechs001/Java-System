package gui;

import dbConnection.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

public class signupScreen extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JTextField emailField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JFormattedTextField phoneNumberField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton signUpButton;
    private JButton loginButton;

    public signupScreen() {
        setTitle("Sign Up");
        setUndecorated(true);
        setSize(800, 600); // Increased size for better visibility
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(800,50));
        topPanel.setBackground(new Color(25,67,45));
        topPanel.setLayout(null);
        JLabel titleLabel = new JLabel("Create an Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(250, 0, 400, 50);
        add(topPanel,BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setPreferredSize(new Dimension(800,600));
        mainPanel.setBackground(new Color(26, 13, 53));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username Field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(25);
        usernameField.setPreferredSize(new Dimension(250, 25));
        usernameField.setToolTipText("Enter your username");
        usernameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Add border
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        // Email Field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emailLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(emailLabel, gbc);

        emailField = new JTextField(25);
        emailField.setPreferredSize(new Dimension(250, 25));
        emailField.setForeground(Color.BLACK);
        emailField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);

        // First Name Field
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        firstNameLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(firstNameLabel, gbc);

        firstNameField = new JTextField(25);
        firstNameField.setPreferredSize(new Dimension(250, 25));
        firstNameField.setToolTipText("Enter your first name");
        firstNameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        gbc.gridx = 1;
        mainPanel.add(firstNameField, gbc);

        // Last Name Field
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        lastNameLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(lastNameLabel, gbc);

        lastNameField = new JTextField(25);
        lastNameField.setPreferredSize(new Dimension(250, 25));
        lastNameField.setToolTipText("Enter your last name");
        lastNameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        gbc.gridx = 1;
        mainPanel.add(lastNameField, gbc);

        // Phone Number Field
        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        phoneNumberLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(phoneNumberLabel, gbc);

        try {
            MaskFormatter phoneFormatter = new MaskFormatter("###-###-####");
            phoneFormatter.setPlaceholderCharacter('_');
            phoneNumberField = new JFormattedTextField(phoneFormatter);
            phoneNumberField.setPreferredSize(new Dimension(250, 25));
            phoneNumberField.setToolTipText("Enter your phone number");
            phoneNumberField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            gbc.gridx = 1;
            mainPanel.add(phoneNumberField, gbc);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Password Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(25);
        passwordField.setPreferredSize(new Dimension(250, 25));
        passwordField.setToolTipText("Enter a strong password");
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        // Confirm Password Field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 7;
        mainPanel.add(confirmPasswordLabel, gbc);

        confirmPasswordField = new JPasswordField(25);
        confirmPasswordField.setPreferredSize(new Dimension(250, 25));
        confirmPasswordField.setToolTipText("Re-enter your password");
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        gbc.gridx = 1;
        mainPanel.add(confirmPasswordField, gbc);

        // Sign Up Button
        signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Arial", Font.BOLD, 16));
        signUpButton.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(signUpButton, gbc);

        // Login Button
        loginButton = new JButton("Already a member? Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setContentAreaFilled(false);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> {
            this.dispose();
            new Login_Screen();
        });
        gbc.gridy = 9;
        mainPanel.add(loginButton, gbc);

        add(mainPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(800, 50));
        bottomPanel.setBackground(new Color(18, 12, 53));
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Sign Up")) {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String phoneNumber = phoneNumberField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (username.isEmpty() || email.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showError("All fields are required.");
                return;
            }

            if (!firstName.matches("^[a-zA-Z]+$")) {
                showError("First name must contain only alphabetic characters.");
                return;
            }

            if (!lastName.matches("^[a-zA-Z]+$")) {
                showError("Last name must contain only alphabetic characters.");
                return;
            }

            if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                showError("Please enter a valid email address.");
                return;
            }

//            if (!phoneNumber.matches("^\\d{3}-\\d{3}-\\d{4}$")) {
//                showError("Please enter a valid phone number in the format ###-###-####.");
//                return;
//            }

            if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$")) {
                showError("Password must be at least 8 characters long and contain an uppercase letter, a lowercase letter, a number, and a special character.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showError("Passwords do not match.");
                return;
            }

            try (Connection conn = DatabaseConnection.getConnection()) {
                String checkUserSql = "SELECT COUNT(*) FROM users WHERE username = ?";
                PreparedStatement checkUserStmt = conn.prepareStatement(checkUserSql);
                checkUserStmt.setString(1, username);
                ResultSet rs = checkUserStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    showError("Username already exists. Please choose a different username.");
                    return;
                }

                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                String sql = "INSERT INTO users (username, email, password, first_name, last_name, phone_number, role) VALUES (?, ?, ?, ?, ?, ?, 'member')";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, email);
                pstmt.setString(3, hashedPassword);
                pstmt.setString(4, firstName);
                pstmt.setString(5, lastName);
                pstmt.setString(6, phoneNumber);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "User registered successfully! Please login.");
                this.dispose();
                new Login_Screen();
                clearFields();
            } catch (SQLException ex) {
                ex.printStackTrace();
                showError("User already exists or an error occurred.");
            }
        }
    }
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    private void clearFields() {
        usernameField.setText("");
        emailField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        phoneNumberField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }
}