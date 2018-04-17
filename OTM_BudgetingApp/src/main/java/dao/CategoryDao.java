package dao;

import data.Category;
import data.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryDao {

    private Database db;

    public CategoryDao(Database db) {
        this.db = db;
    }

    public void disconnect(Connection conn, PreparedStatement stmt, ResultSet rs) throws SQLException {
        rs.close();
        stmt.close();
        conn.close();
    }

    public ArrayList<Category> findAll() throws SQLException {
        Connection conn = db.getConnection();
        ArrayList<Category> list = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Category;");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            PlanDao pDao = new PlanDao(db);
            Category c = new Category(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("allocated"),
                    pDao.findOne(rs.getInt("plan_id"))
            );
            list.add(c);
        }

        disconnect(conn, stmt, rs);

        return list;
    }

    public ArrayList<Category> findAllByPlanId(int key) throws SQLException {
        Connection conn = db.getConnection();
        ArrayList<Category> list = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Category WHERE plan_id=?;");
        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            PlanDao pDao = new PlanDao(db);
            Category c = new Category(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("allocated"),
                    pDao.findOne(rs.getInt("plan_id"))
            );
            list.add(c);
        }

        disconnect(conn, stmt, rs);

        return list;
    }

    public Category findOne(int key) throws SQLException {
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Category WHERE id=?;");
        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            PlanDao pDao = new PlanDao(db);
            Category c = new Category(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("allocated"),
                    pDao.findOne(rs.getInt("plan_id")));

            disconnect(conn, stmt, rs);
            return c;
        } else {
            disconnect(conn, stmt, rs);
            return null;
        }
    }

    public Category findOneByNameAndPlanId(String name, int planId) throws SQLException {
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Category WHERE name=? AND plan_id=?;");
        stmt.setString(1, name);
        stmt.setInt(2, planId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            PlanDao pDao = new PlanDao(db);
            Category c = new Category(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("allocated"),
                    pDao.findOne(rs.getInt("plan_id")));

            disconnect(conn, stmt, rs);
            return c;
        } else {
            disconnect(conn, stmt, rs);
            return null;
        }
    }

    public void saveCategory(Category c) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Category (name, allocated, plan_id) VALUES (?, ?, ?);");
        stmt.setString(1, c.getName());
        stmt.setDouble(2, c.getAllocated());
        stmt.setInt(3, c.getPlan().getId());

        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    public void delete(Integer key) throws SQLException {
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Category WHERE id = ?");
        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    public void deleteAllByPlanId(Integer key) throws SQLException {
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Category WHERE plan_id = ?");
        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
}
