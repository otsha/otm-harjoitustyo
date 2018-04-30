package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

    private String address;

    public Database(String databaseAddress) {
        this.address = databaseAddress;
        initTables();
    }

    /**
     * Tries to connect to the database initialised in the object constructor.
     *
     * @return Connection
     * @throws SQLException
     */
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(address);
    }
    
    /**
     * Closes the given Connection, PreparedStatement and ResultSet
     * @param conn The Connection to be closed
     * @param stmt The PreparedStatement to be closed
     * @param rs The ResultSet to be closed
     * @throws SQLException 
     * @see data.Database#disconnect(java.sql.Connection, java.sql.PreparedStatement) 
     * @see java.sql.Connection#close() 
     * @see java.sql.PreparedStatement#close() 
     * @see java.sql.ResultSet#close() 
     */
    
    public void disconnect(Connection conn, PreparedStatement stmt, ResultSet rs) throws SQLException {
        rs.close();
        stmt.close();
        conn.close();
    }
    
    /**
     * Closes the given Connection and PreparedStatement. To be used with Updates or Queries where no ResultSet is used.
     * @param conn The Connection to be closed
     * @param stmt The PreparedStatement to be closed
     * @throws SQLException 
     * @see data.Database#disconnect(java.sql.Connection, java.sql.PreparedStatement) 
     * @see java.sql.Connection#close() 
     * @see java.sql.PreparedStatement#close() 
     */
    
    public void disconnect(Connection conn, PreparedStatement stmt) throws SQLException {
        stmt.close();
        conn.close();
    }

    private void initTables() {
        // The following methods will create required tables if they do not already exist in the database
        initPlans();
        initCategories();
        initExpenses();
    }

    private void initPlans() {
        try {
            Connection conn = getConnection();

            PreparedStatement createPlanTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS Plan ("
                    + "id integer PRIMARY KEY, "
                    + "name varchar(255), "
                    + "budget float);"
            );
            createPlanTable.execute();
            createPlanTable.close();

            conn.close();
        } catch (SQLException e) {
            /* Do nothing */
        }
    }

    private void initCategories() {
        try {
            Connection conn = getConnection();

            PreparedStatement createCategoryTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS Category ("
                    + "id integer PRIMARY KEY, "
                    + "plan_id integer, "
                    + "name varchar(255), "
                    + "allocated float, "
                    + "FOREIGN KEY (plan_id) REFERENCES Plan(id));"
            );
            createCategoryTable.execute();
            createCategoryTable.close();

            conn.close();
        } catch (SQLException e) {
            /* Do nothing */
        }
    }

    private void initExpenses() {
        try {
            Connection conn = getConnection();

            PreparedStatement createExpenseTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS Expense ("
                    + "id integer PRIMARY KEY, "
                    + "category_id integer, "
                    + "name varchar(255), "
                    + "amount float, "
                    + "FOREIGN KEY (category_id) REFERENCES Category(id));"
            );
            createExpenseTable.execute();
            createExpenseTable.close();

            conn.close();
        } catch (SQLException e) {
            /* Do nothing */
        }
    }

}
