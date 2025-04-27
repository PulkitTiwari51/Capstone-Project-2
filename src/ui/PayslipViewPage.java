package ui;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class PayslipViewPage {

    JFrame frame;
    JTextArea resultArea;
    JButton doneButton;

    public PayslipViewPage(String userId) {
        frame = new JFrame("View Payslip");
        frame.setSize(600, 450);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        frame.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        doneButton = new JButton("Done");
        frame.add(doneButton, BorderLayout.SOUTH);

        doneButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Thank you!");
            System.exit(0);  // closes program
        });

        fetchPayslip(userId);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void fetchPayslip(String userId) {
        try (Connection con = database.DBConnection.getConnection()) {
            String query = "SELECT e.first_name, e.last_name, e.position, s.basic_salary, s.bonus, s.overtime " +
                    "FROM employee e " +
                    "JOIN salary s ON e.emp_id = s.emp_id " +
                    "WHERE e.user_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String position = rs.getString("position");
                    double basicSalary = rs.getDouble("basic_salary");
                    double bonus = rs.getDouble("bonus");
                    double overtimeHours = rs.getDouble("overtime");

                    // Calculation
                    double overtimePay = overtimeHours * 500;
                    double grossSalary = basicSalary + bonus + overtimePay;
                    double tax = basicSalary * 0.05;
                    double netSalary = grossSalary - tax;

                    resultArea.setText("Payslip for " + firstName + " " + lastName + " (" + position + ")\n\n"
                            + "Basic Salary: ₹" + basicSalary + "\n"
                            + "Bonus: ₹" + bonus + "\n"
                            + "Overtime Pay: ₹" + overtimePay + "\n"
                            + "-----------------------------\n"
                            + "Gross Salary: ₹" + grossSalary + "\n"
                            + "Tax (5% of Basic): ₹" + tax + "\n"
                            + "-----------------------------\n"
                            + "Net Salary: ₹" + netSalary);
                } else {
                    resultArea.setText("No records found for User ID: " + userId);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            resultArea.setText("Error retrieving payslip.");
        }
    }
}
