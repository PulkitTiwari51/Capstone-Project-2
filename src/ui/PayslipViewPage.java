package ui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PayslipViewPage {

    JFrame frame;
    JTextField userIdField;
    JButton viewButton;
    JTextArea resultArea;

    public PayslipViewPage() {
        frame = new JFrame("View Payslip");
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JLabel userIdLabel = new JLabel("Enter User ID:");
        userIdField = new JTextField(20);
        viewButton = new JButton("View Payslip");

        topPanel.add(userIdLabel);
        topPanel.add(userIdField);
        topPanel.add(viewButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        viewButton.addActionListener(e -> {
            fetchPayslip(userIdField.getText());
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void fetchPayslip(String userId) {
        try {
            Connection con = database.DBConnection.getConnection();
            String query = "SELECT e.first_name, e.last_name, e.position, s.basic_salary, s.bonus, s.overtime, d.tax, d.other_deductions "
                    +
                    "FROM employee e " +
                    "JOIN salary s ON e.emp_id = s.emp_id " +
                    "LEFT JOIN deduction d ON e.emp_id = d.emp_id " +
                    "WHERE e.user_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String position = rs.getString("position");
                double basicSalary = rs.getDouble("basic_salary");
                double bonus = rs.getDouble("bonus");
                double overtime = rs.getDouble("overtime");
                double tax = rs.getDouble("tax");
                double otherDeductions = rs.getDouble("other_deductions");

                double grossSalary = basicSalary + bonus + overtime;
                double totalDeductions = tax + otherDeductions;
                double netSalary = grossSalary - totalDeductions;

                resultArea.setText("Payslip for " + firstName + " " + lastName + " (" + position + ")\n\n"
                        + "Basic Salary: " + basicSalary + "\n"
                        + "Bonus: " + bonus + "\n"
                        + "Overtime: " + overtime + "\n"
                        + "-----------------------------\n"
                        + "Gross Salary: " + grossSalary + "\n"
                        + "Tax Deduction: " + tax + "\n"
                        + "Other Deductions: " + otherDeductions + "\n"
                        + "-----------------------------\n"
                        + "Net Salary: " + netSalary);
            } else {
                resultArea.setText("No records found for User ID: " + userId);
            }

            rs.close();
            ps.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            resultArea.setText("Error retrieving payslip.");
        }
    }

    public static void main(String[] args) {
        new PayslipViewPage();
    }
}
