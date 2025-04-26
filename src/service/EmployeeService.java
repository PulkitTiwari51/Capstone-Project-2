package service;

import database.DBConnection;
import model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmployeeService {

    public boolean addEmployee(Employee emp) {
        Connection con = DBConnection.getConnection();
        if (con == null)
            return false;
        try {
            String query = "INSERT INTO employee (first_name, last_name, email, phone, position, user_id, password, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, emp.getFirstName());
            stmt.setString(2, emp.getLastName());
            stmt.setString(3, emp.getEmail());
            stmt.setString(4, emp.getPhone());
            stmt.setString(5, emp.getPosition());
            stmt.setString(6, emp.getUserId());
            stmt.setString(7, emp.getPassword());
            stmt.setString(8, emp.getRole());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
