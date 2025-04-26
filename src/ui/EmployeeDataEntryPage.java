package ui;

import model.Employee;
import model.Salary;
import service.EmployeeService;
import service.SalaryService;

import javax.swing.*;
import java.awt.*;

public class EmployeeDataEntryPage {

    JFrame frame;
    JTextField firstNameField, lastNameField, emailField, phoneField, positionField, userIdField, passwordField,
            roleField;
    JTextField basicSalaryField, bonusField, overtimeField;
    JButton submitButton;

    public EmployeeDataEntryPage() {
        frame = new JFrame("Add New Employee");
        frame.setSize(600, 700);
        frame.setLayout(new GridLayout(14, 2, 10, 10));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Employee Details
        frame.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        frame.add(firstNameField);

        frame.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        frame.add(lastNameField);

        frame.add(new JLabel("Email:"));
        emailField = new JTextField();
        frame.add(emailField);

        frame.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        frame.add(phoneField);

        frame.add(new JLabel("Position:"));
        positionField = new JTextField();
        frame.add(positionField);

        frame.add(new JLabel("User ID:"));
        userIdField = new JTextField();
        frame.add(userIdField);

        frame.add(new JLabel("Password:"));
        passwordField = new JTextField();
        frame.add(passwordField);

        frame.add(new JLabel("Role:"));
        roleField = new JTextField("Employee"); // default role
        frame.add(roleField);

        // Salary Details
        frame.add(new JLabel("Basic Salary:"));
        basicSalaryField = new JTextField();
        frame.add(basicSalaryField);

        frame.add(new JLabel("Bonus:"));
        bonusField = new JTextField();
        frame.add(bonusField);

        frame.add(new JLabel("Overtime:"));
        overtimeField = new JTextField();
        frame.add(overtimeField);

        submitButton = new JButton("Submit");
        frame.add(new JLabel()); // Empty label for spacing
        frame.add(submitButton);

        submitButton.addActionListener(e -> {
            addEmployeeWithSalary();
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addEmployeeWithSalary() {
        try {
            Employee emp = new Employee();
            emp.setFirstName(firstNameField.getText());
            emp.setLastName(lastNameField.getText());
            emp.setEmail(emailField.getText());
            emp.setPhone(phoneField.getText());
            emp.setPosition(positionField.getText());
            emp.setUserId(userIdField.getText());
            emp.setPassword(passwordField.getText());
            emp.setRole(roleField.getText());

            EmployeeService empService = new EmployeeService();
            boolean empAdded = empService.addEmployee(emp);

            if (empAdded) {
                Salary salary = new Salary();
                salary.setEmpId(getEmployeeIdByUserId(emp.getUserId()));
                salary.setBasicSalary(Double.parseDouble(basicSalaryField.getText()));
                salary.setBonus(Double.parseDouble(bonusField.getText()));
                salary.setOvertime(Double.parseDouble(overtimeField.getText()));

                SalaryService salaryService = new SalaryService();
                salaryService.addSalary(salary);

                JOptionPane.showMessageDialog(frame, "Employee and Salary added successfully!");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Error adding Employee. Please try again.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Invalid input. Please fill all fields correctly.");
        }
    }

    private int getEmployeeIdByUserId(String userId) {
        try {
            java.sql.Connection con = database.DBConnection.getConnection();
            String query = "SELECT emp_id FROM employee WHERE user_id = ?";
            java.sql.PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userId);

            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("emp_id");
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static void main(String[] args) {
        new EmployeeDataEntryPage();
    }
}
