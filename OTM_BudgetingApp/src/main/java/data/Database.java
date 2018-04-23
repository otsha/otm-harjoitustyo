package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {

    private String address;

    public Database(String databaseAddress) {
        this.address = databaseAddress;
        initTables();
    }
    
    /**
     * Tries to connect to the database initialised in the object constructor.
     * @return Connection
     * @throws SQLException 
     */
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(address);
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

        }
    }

}
