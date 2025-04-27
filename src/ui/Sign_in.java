package ui;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class Sign_in {

    JFrame frame1;
    JTextField tt1;
    JPasswordField tt2;
    JButton loginButton, registerButton;

    public Sign_in() {
        frame1 = new JFrame("Login");
        frame1.setSize(400, 300);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setLayout(new GridLayout(5, 2, 10, 10));

        frame1.add(new JLabel("User ID:"));
        tt1 = new JTextField();
        frame1.add(tt1);

        frame1.add(new JLabel("Password:"));
        tt2 = new JPasswordField();
        frame1.add(tt2);

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        frame1.add(loginButton);
        frame1.add(registerButton);

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> {
            frame1.dispose();
            new SignUp();
        });

        frame1.setLocationRelativeTo(null);
        frame1.setVisible(true);
    }

    private void login() {
        String userId = tt1.getText();
        String password = new String(tt2.getPassword());

        if (userId.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame1, "Please fill all fields");
            return;
        }

        try (Connection con = database.DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM employee WHERE user_id = ? AND password = ?")) {
            ps.setString(1, userId);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    JOptionPane.showMessageDialog(frame1, "LOGIN SUCCESSFUL");
                    String email = rs.getString("email");
                    frame1.dispose();
                    new EmployeeDataEntryPage(userId, email, password);
                } else {
                    JOptionPane.showMessageDialog(frame1, "Invalid User ID or Password");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame1, "Database connection error: " + ex.getMessage());
        }
    }
}
