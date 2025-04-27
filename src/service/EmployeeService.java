package service;

import java.sql.*;
import model.Employee;

public class EmployeeService {

    public boolean updateOrAddEmployee(Employee emp) {
        try (Connection con = database.DBConnection.getConnection()) {
            String checkQuery = "SELECT * FROM employee WHERE user_id = ?";
            PreparedStatement checkPs = con.prepareStatement(checkQuery);
            checkPs.setString(1, emp.getUserId());
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                // Already exists, so update
                String updateQuery = "UPDATE employee SET first_name=?, last_name=?, phone=?, position=?, email=?, password=?, role=? WHERE user_id=?";
                PreparedStatement updatePs = con.prepareStatement(updateQuery);
                updatePs.setString(1, emp.getFirstName());
                updatePs.setString(2, emp.getLastName());
                updatePs.setString(3, emp.getPhone());
                updatePs.setString(4, emp.getPosition());
                updatePs.setString(5, emp.getEmail());
                updatePs.setString(6, emp.getPassword());
                updatePs.setString(7, emp.getRole());
                updatePs.setString(8, emp.getUserId());

                int rows = updatePs.executeUpdate();
                return rows > 0;
            } else {
                // New insert
                String insertQuery = "INSERT INTO employee (first_name, last_name, phone, position, email, password, role, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertPs = con.prepareStatement(insertQuery);
                insertPs.setString(1, emp.getFirstName());
                insertPs.setString(2, emp.getLastName());
                insertPs.setString(3, emp.getPhone());
                insertPs.setString(4, emp.getPosition());
                insertPs.setString(5, emp.getEmail());
                insertPs.setString(6, emp.getPassword());
                insertPs.setString(7, emp.getRole());
                insertPs.setString(8, emp.getUserId());

                int rows = insertPs.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
