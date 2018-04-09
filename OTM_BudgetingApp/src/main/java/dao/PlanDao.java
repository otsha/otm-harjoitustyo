package dao;

import data.Database;
import data.Plan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlanDao {
    
    private Database db;
    
    public PlanDao(Database db) {
        this.db = db;
    }
    
    public ArrayList<Plan> findAll() throws SQLException {
        Connection conn = db.getConnection();
        ArrayList<Plan> list = new ArrayList<>();
        
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Plan;");
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()) {
            list.add(new Plan(rs.getInt("id"), rs.getString("name"), rs.getDouble("budget")));
        }
        
        return list;
    }
}
