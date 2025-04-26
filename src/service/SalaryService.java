package service;

import database.DBConnection;
import model.Salary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SalaryService {

    public boolean addSalary(Salary salary) {
        Connection con = DBConnection.getConnection();
        if (con == null)
            return false;
        try {
            String query = "INSERT INTO salary (emp_id, basic_salary, bonus, overtime) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, salary.getEmpId());
            stmt.setDouble(2, salary.getBasicSalary());
            stmt.setDouble(3, salary.getBonus());
            stmt.setDouble(4, salary.getOvertime());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
