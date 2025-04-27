package service;

import java.sql.*;
import model.Salary;

public class SalaryService {

    public boolean addOrUpdateSalary(Salary salary) {
        try (Connection con = database.DBConnection.getConnection()) {
            String checkQuery = "SELECT * FROM salary WHERE emp_id = ?";
            PreparedStatement checkPs = con.prepareStatement(checkQuery);
            checkPs.setInt(1, salary.getEmpId());
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                // Already exists, update
                String updateQuery = "UPDATE salary SET basic_salary=?, bonus=?, overtime=? WHERE emp_id=?";
                PreparedStatement updatePs = con.prepareStatement(updateQuery);
                updatePs.setDouble(1, salary.getBasicSalary());
                updatePs.setDouble(2, salary.getBonus());
                updatePs.setDouble(3, salary.getOvertime());
                updatePs.setInt(4, salary.getEmpId());

                int rows = updatePs.executeUpdate();
                return rows > 0;
            } else {
                // New insert
                String insertQuery = "INSERT INTO salary (emp_id, basic_salary, bonus, overtime) VALUES (?, ?, ?, ?)";
                PreparedStatement insertPs = con.prepareStatement(insertQuery);
                insertPs.setInt(1, salary.getEmpId());
                insertPs.setDouble(2, salary.getBasicSalary());
                insertPs.setDouble(3, salary.getBonus());
                insertPs.setDouble(4, salary.getOvertime());

                int rows = insertPs.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
