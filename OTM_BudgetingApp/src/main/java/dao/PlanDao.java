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

    /**
     * Connects to the database and searches for any and all plans saved.
     *
     * @return All the Plans in the database
     * @throws SQLException
     */
    public ArrayList<Plan> findAll() throws SQLException {
        Connection conn = db.getConnection();
        ArrayList<Plan> list = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Plan;");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            list.add(new Plan(rs.getInt("id"), rs.getString("name"), rs.getDouble("budget")));
        }

        db.disconnect(conn, stmt, rs);

        return list;
    }

    /**
     * Connects to the database and attempts to find a Plan with an id
     * corresponding to the parameter.
     *
     * @param key The id of the Plan to be searched for
     * @return A plan with an id corresponding to the key parameter
     * @throws SQLException
     * @see dao.PlanDao#findOneByName(java.lang.String)
     */
    public Plan findOne(int key) throws SQLException {
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Plan WHERE id=?;");
        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Plan p = new Plan(rs.getInt("id"), rs.getString("name"), rs.getDouble("budget"));

            db.disconnect(conn, stmt, rs);
            return p;
        } else {
            db.disconnect(conn, stmt, rs);
            return null;
        }
    }

    /**
     * Connects to the database and attempts to find a Plan with a name
     * corresponding to the parameter.
     *
     * @param name The name of the Plan to be searched for
     * @return A plan with a name corresponding to the name parameter
     * @throws SQLException
     * @see dao.PlanDao#findOne(int)
     */
    public Plan findOneByName(String name) throws SQLException {
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Plan WHERE name=?;");
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Plan p = new Plan(rs.getInt("id"), rs.getString("name"), rs.getDouble("budget"));

            db.disconnect(conn, stmt, rs);
            return p;
        } else {
            db.disconnect(conn, stmt, rs);
            return null;
        }
    }

    /**
     * Attempts to create a new Plan in the database.
     *
     * @param name The name of the plan as inputted by the user
     * @param budget The budget of the plan as inputted by the user
     * @return True if plan was successfully saved into the database. False if a
     * plan with the same name already exists.
     * @throws SQLException
     */
    public boolean save(String name, double budget) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement doesThisExist = conn.prepareStatement("SELECT * FROM Plan WHERE name = ?;");
        doesThisExist.setString(1, name);
        ResultSet rs = doesThisExist.executeQuery();

        if (!rs.next()) {
            PreparedStatement savePlan = conn.prepareStatement("INSERT INTO Plan (name, budget) VALUES (?, ?);");
            savePlan.setString(1, name);
            savePlan.setDouble(2, budget);

            savePlan.executeUpdate();

            db.disconnect(conn, savePlan);

            return true;
        } else {
            db.disconnect(conn, doesThisExist, rs);
            return false;
        }
    }

    /**
     * Deletes a plan from the database with an id corresponding to the
     * parameter.
     *
     * @param key The id of the Plan to be deleted
     * @throws SQLException
     */
    public void delete(Integer key) throws SQLException {
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Plan WHERE id = ?");
        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
}
