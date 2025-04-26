package service;

import database.DBConnection;
import model.Deduction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeductionService {

    public boolean addDeduction(Deduction deduction) {
        Connection con = DBConnection.getConnection();
        if (con == null)
            return false;
        try {
            String query = "INSERT INTO deduction (emp_id, tax, other_deductions) VALUES (?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, deduction.getEmpId());
            stmt.setDouble(2, deduction.getTax());
            stmt.setDouble(3, deduction.getOtherDeductions());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
