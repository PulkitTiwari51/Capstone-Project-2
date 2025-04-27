package ui;

import database.DBConnection;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;

public class SignUp {
    JFrame frame = new JFrame("Sign Up");

    public SignUp() {
        frame.setLayout(null);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Register New Employee", JLabel.CENTER);
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

        JLabel email = new JLabel("Email");
        email.setFont(new Font("Arial", Font.BOLD, 18));
        email.setBounds(200, 220, 100, 30);
        frame.add(email);

        JTextField t3 = new JTextField();
        t3.setBounds(310, 220, 200, 30);
        frame.add(t3);

        JButton signup = new JButton("REGISTER");
        signup.setBounds(300, 300, 200, 30);
        signup.addActionListener(e -> {
            String userId = t1.getText();
            String pass = new String(t2.getPassword());
            String mail = t3.getText();
            if (registerUser(userId, pass, mail)) {
                JOptionPane.showMessageDialog(frame, "Registration Successful!");
                new Sign_in();
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Registration Failed!");
            }
        });
        frame.add(signup);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private boolean registerUser(String userId, String password, String email) {
        Connection con = null;
        PreparedStatement stmt = null;
        
        try {
            con = DBConnection.getConnection();
            if (con == null) {
                System.out.println("Database connection failed");
                return false;
            }
            
            // Simpler query with only required fields
            String query = "INSERT INTO employee (user_id, password, email) VALUES (?, ?, ?)";
            stmt = con.prepareStatement(query);
            stmt.setString(1, userId);
            stmt.setString(2, password);
            stmt.setString(3, email);
    
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            // More detailed error message
            System.out.println("SQL Error in registration: " + e.getMessage());
            JOptionPane.showMessageDialog(frame, "Registration Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // Proper resource cleanup
            try {
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
