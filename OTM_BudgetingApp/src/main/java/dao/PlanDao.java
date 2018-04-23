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

    public void disconnect(Connection conn, PreparedStatement stmt, ResultSet rs) throws SQLException {
        rs.close();
        stmt.close();
        conn.close();
    }

    public ArrayList<Plan> findAll() throws SQLException {
        Connection conn = db.getConnection();
        ArrayList<Plan> list = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Plan;");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            list.add(new Plan(rs.getInt("id"), rs.getString("name"), rs.getDouble("budget")));
        }

        disconnect(conn, stmt, rs);

        return list;
    }

    public Plan findOne(int key) throws SQLException {
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Plan WHERE id=?;");
        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Plan p = new Plan(rs.getInt("id"), rs.getString("name"), rs.getDouble("budget"));

            disconnect(conn, stmt, rs);
            return p;
        } else {
            disconnect(conn, stmt, rs);
            return null;
        }
    }

    public Plan findOneByName(String name) throws SQLException {
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Plan WHERE name=?;");
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Plan p = new Plan(rs.getInt("id"), rs.getString("name"), rs.getDouble("budget"));

            disconnect(conn, stmt, rs);
            return p;
        } else {
            disconnect(conn, stmt, rs);
            return null;
        }
    }

    public boolean save(Plan p) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement doesThisExist = conn.prepareStatement("SELECT * FROM Plan WHERE name = ?;");
        doesThisExist.setString(1, p.getName());
        ResultSet rs = doesThisExist.executeQuery();

        if (!rs.next()) {
            PreparedStatement savePlan = conn.prepareStatement("INSERT INTO Plan (name, budget) VALUES (?, ?);");
            savePlan.setString(1, p.getName());
            savePlan.setDouble(2, p.getBudget());

            savePlan.executeUpdate();

            savePlan.close();
            conn.close();
            
            return true;
        } else {
            disconnect(conn, doesThisExist, rs);
            return false;
        }
    }

    public void delete(Integer key) throws SQLException {
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Plan WHERE id = ?");
        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
}
