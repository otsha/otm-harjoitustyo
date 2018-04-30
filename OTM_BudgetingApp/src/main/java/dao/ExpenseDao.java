package dao;

import data.Category;
import data.Database;
import data.Expense;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ExpenseDao {

    private Database db;

    public ExpenseDao(Database db) {
        this.db = db;
    }

    /**
     * @deprecated Use findOneByNameAndCategoryId for most cases. Attempts to
     * find an Expense from the database with an id corresponding to the given
     * key.
     * @param key The id of the Expense to be searched for
     * @return Expense with an id corresponding the key parameter. Null if none
     * was found.
     * @throws SQLException
     * @see dao.ExpenseDao#findOneByNameAndCategoryId(java.lang.String, int)
     */
    public Expense findOne(int key) throws SQLException {
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Expense WHERE id = ?;");
        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            CategoryDao cDao = new CategoryDao(db);
            Expense e = new Expense(rs.getInt("id"), rs.getString("name"), rs.getDouble("amount"), cDao.findOne(rs.getInt("category_id")));

            db.disconnect(conn, stmt, rs);

            return e;
        } else {
            db.disconnect(conn, stmt, rs);

            return null;
        }
    }

    /**
     * Connects to the database and attempts to find an Expense with the given
     * 'name' and 'category_id' columns.
     *
     * @param name The name of the Expense to be searched for
     * @param categoryId The Category the Expense to be searched for is
     * associated with.
     * @return An Expense with the corresponding name and Category. Null if none
     * was found.
     * @throws SQLException
     */
    public Expense findOneByNameAndCategoryId(String name, int categoryId) throws SQLException {
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Expense WHERE name=? AND category_id=?;");
        stmt.setString(1, name);
        stmt.setInt(2, categoryId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            CategoryDao cDao = new CategoryDao(db);
            Expense e = new Expense(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("amount"),
                    cDao.findOne(rs.getInt("category_id")));

            db.disconnect(conn, stmt, rs);
            return e;
        } else {
            db.disconnect(conn, stmt, rs);
            return null;
        }
    }

    /**
     * Connects to the database and searches for any and all the Expenses with
     * the given value in their 'category_id' column.
     *
     * @param categoryId The id the Expenses should be associated with.
     * @return A list of all the Expenses associated with the given Category
     * @throws SQLException
     */
    public ArrayList<Expense> findAllByCategory(int categoryId) throws SQLException {
        Connection conn = db.getConnection();
        ArrayList<Expense> list = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Expense WHERE category_id = ?;");
        stmt.setInt(1, categoryId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            CategoryDao cDao = new CategoryDao(db);
            Expense e = new Expense(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("amount"),
                    cDao.findOne(categoryId));
            list.add(e);
        }

        db.disconnect(conn, stmt, rs);
        return list;
    }

    /**
     * Attempts to create a new Expense in the database.
     *
     * @param name The desired name for the Expense as inputted by the user
     * @param amount The amount of the Expense as inmutted by the user
     * @param c The Category the Expense should belong to
     * @return True if the Expense was successfully created and saved. False if
     * an Expense with the same 'name' and 'category_id' columns already exists.
     * @throws SQLException
     */
    public boolean save(String name, double amount, Category c) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement doesThisExist = conn.prepareStatement("SELECT * FROM Expense WHERE category_id = ? and name = ?;");
        doesThisExist.setInt(1, c.getId());
        doesThisExist.setString(2, name);
        ResultSet rs = doesThisExist.executeQuery();
        doesThisExist.close();

        if (!rs.next()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Expense (name, amount, category_id) VALUES (?, ?, ?);");
            stmt.setString(1, name);
            stmt.setDouble(2, amount);
            stmt.setInt(3, c.getId());

            stmt.executeUpdate();
            stmt.close();
            db.disconnect(conn, stmt, rs);
            return true;
        } else {
            db.disconnect(conn, doesThisExist, rs);
            return false;
        }
    }

    /**
     * Attempts to delete an Expense with the given id from the database.
     *
     * @param key The id of the Expense to be deleted
     * @throws SQLException
     * @see dao.ExpenseDao#deleteAllByCategoryId(int)
     * @see dao.ExpenseDao#deleteAllByPlanId(int)
     */
    public void delete(int key) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Expense WHERE id = ?;");
        stmt.setInt(1, key);

        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    /**
     * Deletes all the Expenses from the database where the 'category_id' colun
     * corresponds with the given key.
     *
     * @param categoryId The id of the Category to be nuked
     * @throws SQLException
     * @see dao.ExpenseDao#delete(int)
     * @see dao.ExpenseDao#deleteAllByPlanId(int)
     */
    public void deleteAllByCategoryId(int categoryId) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Expense WHERE category_id = ?;");
        stmt.setInt(1, categoryId);

        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    /**
     * Deletes all the Expenses from the database where by going through all the
     * Categories in the plan and nuking them.
     *
     * @param planId The plan to be nuked.
     * @throws SQLException
     * @see dao.ExpenseDao#delete(int)
     * @see dao.ExpenseDao#deleteAllByCategoryId(int)
     */
    public void deleteAllByPlanId(int planId) throws SQLException {
        CategoryDao cDao = new CategoryDao(db);
        ArrayList<Category> categories = cDao.findAllByPlanId(planId);
        categories.stream().forEach(c -> {
            try {
                deleteAllByCategoryId(c.getId());
            } catch (SQLException ex) {
            }
        });
    }
}
