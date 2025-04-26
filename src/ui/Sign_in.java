package ui;

import database.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Sign_in {
    JFrame frame = new JFrame("Sign In");

    public Sign_in() {
        frame.setLayout(null);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Employee Payroll System", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setBounds(0, 25, 800, 50);
        frame.add(label);

        JLabel username = new JLabel("Username");
        username.setFont(new Font("Arial", Font.BOLD, 18));
        username.setBounds(200, 120, 100, 30);
        frame.add(username);

        JTextField t1 = new JTextField();
        t1.setBounds(310, 120, 200, 30);
        frame.add(t1);

        JLabel password = new JLabel("Password");
        password.setFont(new Font("Arial", Font.BOLD, 18));
        password.setBounds(200, 170, 100, 30);
        frame.add(password);

        JPasswordField t2 = new JPasswordField();
        t2.setBounds(310, 170, 200, 30);
        frame.add(t2);

        JButton signin = new JButton("SIGN IN");
        signin.setBounds(300, 250, 200, 30);
        frame.add(signin);

        signin.addActionListener(e -> {
            String userId = t1.getText();
            String pass = new String(t2.getPassword());

            if (validateLogin(userId, pass)) {
                JOptionPane.showMessageDialog(frame, "LOGIN SUCCESSFUL");
                frame.dispose();
                // Next Page Launch here (ex: DataEntryPage)
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Credentials");
            }
        });

        JButton signup = new JButton("SIGN UP");
        signup.setBounds(550, 500, 200, 30);
        signup.addActionListener(e -> {
            new SignUp();
            frame.dispose();
        });
        frame.add(signup);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private boolean validateLogin(String userId, String password) {
        Connection con = DBConnection.getConnection();
        if (con == null) {
            JOptionPane.showMessageDialog(frame, "Database connection failed");
            return false;
        }
        try {
            String query = "SELECT * FROM employee WHERE user_id = ? AND password = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, userId);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // If record found, login success

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        new Sign_in();
    }
}
