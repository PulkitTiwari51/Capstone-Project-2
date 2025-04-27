package ui;

import java.awt.*;
import javax.swing.*;
import model.Employee;
import model.Salary;
import service.EmployeeService;
import service.SalaryService;
import java.sql.*;

public class EmployeeDataEntryPage {

    JFrame frame;
    JTextField firstNameField, lastNameField, phoneField, positionField;
    JTextField basicSalaryField, bonusField, overtimeField;
    JButton submitButton, saveButton, editButton;
    boolean isEditable = false;

    private String userId;
    private String email;
    private String password;
    private String role = "Employee";

    public EmployeeDataEntryPage(String userId, String email, String password) {
        this.userId = userId;
        this.email = email;
        this.password = password;

        frame = new JFrame("Employee Data Entry");
        frame.setSize(500, 700);
        frame.setLayout(new GridLayout(12, 2, 10, 10));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        frame.add(firstNameField);

        frame.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        frame.add(lastNameField);

        frame.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        frame.add(phoneField);

        frame.add(new JLabel("Position:"));
        positionField = new JTextField();
        frame.add(positionField);

        frame.add(new JLabel("Basic Salary:"));
        basicSalaryField = new JTextField();
        frame.add(basicSalaryField);

        frame.add(new JLabel("Bonus:"));
        bonusField = new JTextField();
        frame.add(bonusField);

        frame.add(new JLabel("Overtime Hours:"));
        overtimeField = new JTextField();
        frame.add(overtimeField);

        editButton = new JButton("Edit Info");
        saveButton = new JButton("Save Info");
        submitButton = new JButton("Submit");

        frame.add(editButton);
        frame.add(saveButton);
        frame.add(submitButton);

        editButton.addActionListener(e -> enableEditing());
        saveButton.addActionListener(e -> saveEmployeeData());
        submitButton.addActionListener(e -> submitEmployeeData());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        loadEmployeeData();
    }

    private void enableEditing() {
        setFieldsEditable(true);
    }

    private void setFieldsEditable(boolean editable) {
        firstNameField.setEditable(editable);
        lastNameField.setEditable(editable);
        phoneField.setEditable(editable);
        positionField.setEditable(editable);
        basicSalaryField.setEditable(editable);
        bonusField.setEditable(editable);
        overtimeField.setEditable(editable);
        isEditable = editable;
    }

    private void saveEmployeeData() {
        try {
            saveEmployeeAndSalary();
            JOptionPane.showMessageDialog(frame, "Saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error saving data: " + e.getMessage());
        }
    }

    private void submitEmployeeData() {
        try {
            saveEmployeeAndSalary();
            JOptionPane.showMessageDialog(frame, "Saved successfully! Redirecting to Payslip...");
            frame.dispose();
            new PayslipViewPage(userId);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error saving data: " + e.getMessage());
        }
    }

    private void saveEmployeeAndSalary() throws Exception {
        Employee emp = new Employee();
        emp.setFirstName(firstNameField.getText());
        emp.setLastName(lastNameField.getText());
        emp.setEmail(email);
        emp.setPhone(phoneField.getText());
        emp.setPosition(positionField.getText());
        emp.setUserId(userId);
        emp.setPassword(password);
        emp.setRole(role);

        EmployeeService empService = new EmployeeService();
        boolean empUpdated = empService.updateOrAddEmployee(emp);

        if (empUpdated) {
            Salary salary = new Salary();
            salary.setEmpId(getEmployeeIdByUserId(userId));
            salary.setBasicSalary(Double.parseDouble(basicSalaryField.getText()));
            salary.setBonus(Double.parseDouble(bonusField.getText()));
            salary.setOvertime(Double.parseDouble(overtimeField.getText()));

            SalaryService salaryService = new SalaryService();
            salaryService.addOrUpdateSalary(salary);
        } else {
            throw new Exception("Employee save failed");
        }
    }

    private void loadEmployeeData() {
        try {
            Connection con = database.DBConnection.getConnection();

            String empQuery = "SELECT * FROM employee WHERE user_id = ?";
            PreparedStatement empPs = con.prepareStatement(empQuery);
            empPs.setString(1, userId);

            ResultSet empRs = empPs.executeQuery();
            if (empRs.next()) {
                firstNameField.setText(empRs.getString("first_name"));
                lastNameField.setText(empRs.getString("last_name"));
                phoneField.setText(empRs.getString("phone"));
                positionField.setText(empRs.getString("position"));
                setFieldsEditable(false);
            } else {
                setFieldsEditable(true);
            }
            empRs.close();
            empPs.close();

            int empId = getEmployeeIdByUserId(userId);
            if (empId != -1) {
                String salaryQuery = "SELECT * FROM salary WHERE emp_id = ?";
                PreparedStatement salPs = con.prepareStatement(salaryQuery);
                salPs.setInt(1, empId);

                ResultSet salRs = salPs.executeQuery();
                if (salRs.next()) {
                    basicSalaryField.setText(String.valueOf(salRs.getDouble("basic_salary")));
                    bonusField.setText(String.valueOf(salRs.getDouble("bonus")));
                    overtimeField.setText(String.valueOf(salRs.getDouble("overtime")));
                }
                salRs.close();
                salPs.close();
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getEmployeeIdByUserId(String userId) {
        try {
            Connection con = database.DBConnection.getConnection();
            String query = "SELECT emp_id FROM employee WHERE user_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("emp_id");
            }

            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
