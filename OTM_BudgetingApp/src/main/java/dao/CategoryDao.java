package dao;

import data.Category;
import data.Database;
import data.Plan;
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

    /**
     * @deprecated Use findAllByPlanId instead for most cases. Connects to the
     * database and searches for any and all the categories saved (regardless of
     * their plan)
     * @return All the Categories in the database
     * @throws SQLException
     * @see dao.CategoryDao#findAllByPlanId(int)
     */
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

        db.disconnect(conn, stmt, rs);

        return list;
    }

    /**
     * Connects to the database and searches for all the Categories where
     * 'plan_id' equals the given key.
     *
     * @param key The id of the Plan the Categories should be connected with
     * @return A list of all the Categories associated with the given Plan
     * @throws SQLException
     */
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

        db.disconnect(conn, stmt, rs);

        return list;
    }

    /**
     * Connects to the database and attempts to find a category with an id
     * corresponding to the parameter.
     *
     * @param key The id of the Category to be searched for
     * @return A Category with an id corresponding to the given key. Null if
     * none was found.
     * @throws SQLException
     * @see dao.CategoryDao#findOneByNameAndPlanId(java.lang.String, int)
     */
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

            db.disconnect(conn, stmt, rs);
            return c;
        } else {
            db.disconnect(conn, stmt, rs);
            return null;
        }
    }

    /**
     * Connects to the database and attempts to find a Category with the given
     * name and 'plan_id'
     *
     * @param name The name of the Category to be searched for
     * @param planId The id of the Plan the Category is associated with
     * @return A Category with a name and Plan corresponding to the given
     * parameters
     * @throws SQLException
     */
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

            db.disconnect(conn, stmt, rs);
            return c;
        } else {
            db.disconnect(conn, stmt, rs);
            return null;
        }
    }

    /**
     * Attempts to create a new Category in the database
     *
     * @param name The desired name of the plan as inputted by the user
     * @param allocation The desired allocation of the plan as inputted by the
     * user
     * @param p The Plan the Category is to be associated with
     * @return True if the Category was successfully saved into the database.
     * False if a Category with the same name and plan_id already exists.
     * @throws SQLException
     */
    public boolean save(String name, double allocation, Plan p) throws SQLException {
        Connection conn = db.getConnection();

        PreparedStatement doesThisExist = conn.prepareStatement("SELECT * FROM Category WHERE plan_id = ? and name = ?;");
        doesThisExist.setInt(1, p.getId());
        doesThisExist.setString(2, name);
        ResultSet rs = doesThisExist.executeQuery();

        if (!rs.next()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Category (name, allocated, plan_id) VALUES (?, ?, ?);");
            stmt.setString(1, name);
            stmt.setDouble(2, allocation);
            stmt.setInt(3, p.getId());

            stmt.executeUpdate();

            stmt.close();
            db.disconnect(conn, doesThisExist, rs);

            return true;
        } else {
            db.disconnect(conn, doesThisExist, rs);

            return false;
        }

    }

    /**
     * Attempts to delete a Category with the given id from the database.
     *
     * @param key The id of the Category to be deleted
     * @throws SQLException
     * @see dao.CategoryDao#deleteAllByPlanId(java.lang.Integer)
     */
    public void delete(Integer key) throws SQLException {
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Category WHERE id = ?");
        stmt.setInt(1, key);
        stmt.executeUpdate();

        db.disconnect(conn, stmt);
    }

    /**
     * Deletes all the Categories from the database where plan_id matches with
     * the given key.
     *
     * @param key The id of the Plan where all Categories are to be deleted from
     * @throws SQLException
     * @see dao.CategoryDao#delete(java.lang.Integer)
     */
    public void deleteAllByPlanId(Integer key) throws SQLException {
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Category WHERE plan_id = ?");
        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
}
