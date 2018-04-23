package dao;

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

    public void disconnect(Connection conn, PreparedStatement stmt, ResultSet rs) throws SQLException {
        rs.close();
        stmt.close();
        conn.close();
    }

    public Expense findOne(int key) throws SQLException {
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Expense WHERE id = ?;");
        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            CategoryDao cDao = new CategoryDao(db);
            Expense e = new Expense(rs.getInt("id"), rs.getString("name"), rs.getDouble("amount"), cDao.findOne(rs.getInt("category_id")));

            disconnect(conn, stmt, rs);

            return e;
        } else {
            disconnect(conn, stmt, rs);

            return null;
        }
    }

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

        disconnect(conn, stmt, rs);
        return list;
    }

    public void save(Expense e) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Expense (name, amount, category_id) VALUES (?, ?, ?);");
        stmt.setString(1, e.getName());
        stmt.setDouble(2, e.getAmount());
        stmt.setInt(3, e.getCategory().getId());

        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    public void delete(int key) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Expense WHERE id = ?;");
        stmt.setInt(1, key);

        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    public void deleteAllByCategoryId(int categoryId) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Expense WHERE category_id = ?;");
        stmt.setInt(1, categoryId);

        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
}
