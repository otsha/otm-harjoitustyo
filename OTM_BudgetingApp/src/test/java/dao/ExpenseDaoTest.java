package dao;

import data.Category;
import data.Database;
import data.Expense;
import data.Plan;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class ExpenseDaoTest {

    private Database db;
    private Connection conn;
    private ExpenseDao eDao;
    private CategoryDao cDao;
    private PlanDao pDao;

    @Before
    public void setUp() throws SQLException {

        // Database and connection setup
        this.db = new Database("jdbc:sqlite:test.db");
        this.conn = db.getConnection();

        PreparedStatement createPlanTable = conn.prepareStatement("CREATE TABLE Plan ("
                + "id integer PRIMARY KEY, "
                + "name varchar(255), "
                + "budget float);"
        );
        createPlanTable.execute();
        createPlanTable.close();

        PreparedStatement createCategoryTable = conn.prepareStatement("CREATE TABLE Category ("
                + "id integer PRIMARY KEY, "
                + "plan_id integer, "
                + "name varchar(255), "
                + "allocated float, "
                + "FOREIGN KEY (plan_id) REFERENCES Plan(id));"
        );
        createCategoryTable.execute();
        createCategoryTable.close();

        PreparedStatement createExpenseTable = conn.prepareStatement("CREATE TABLE Expense ("
                + "id integer PRIMARY KEY, "
                + "category_id integer, "
                + "name varchar(255), "
                + "amount float, "
                + "FOREIGN KEY (category_id) REFERENCES Category(id));"
        );
        createExpenseTable.execute();
        createExpenseTable.close();

        this.eDao = new ExpenseDao(db);
        this.cDao = new CategoryDao(db);
        this.pDao = new PlanDao(db);
    }

    @After
    public void tearDown() throws URISyntaxException, SQLException, IOException {

        // Delete the test database after a test is complete
        File dbFile = new File("test.db");
        dbFile.delete();
        conn.close();
    }

    @Test
    public void expenseIsSavedProperly() throws SQLException {
        Plan p = new Plan(1, "testPlan", 10);
        pDao.saveOrUpdate(p);

        Category c = new Category(1, "testCategory", 5, p);
        cDao.saveCategory(c);

        Expense e = new Expense(1, "testExpense", 2.2, c);
        eDao.save(e);

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Expense WHERE id = 1");
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Expense x = new Expense(rs.getInt("id"), rs.getString("name"), rs.getDouble("amount"), cDao.findOne(1));
            assertEquals(1, x.getId());
            assertEquals("testExpense", x.getName());
            assertEquals(2.2, x.getAmount(), 0);
            assertEquals(1, x.getCategory().getId());
            assertEquals(1, x.getCategory().getPlan().getId());
        } else {
            assertEquals(-1, e.getId());
            assertEquals("fail", e.getName());
            assertEquals(5000, e.getAmount(), 0);
            assertEquals(-1, e.getCategory().getId());
        }
    }

    @Test
    public void expenseIsFetchedProperly() throws SQLException {
        Plan p = new Plan(1, "testPlan", 10);
        pDao.saveOrUpdate(p);

        Category c = new Category(1, "testCategory", 5, p);
        cDao.saveCategory(c);

        Expense e = new Expense(1, "testExpense", 2.2, c);
        eDao.save(e);
        
        Expense x = eDao.findOne(1);
        
        assertEquals(1, x.getId());
        assertEquals("testExpense", x.getName());
        assertEquals(2.2, x.getAmount(), 0);
        assertEquals(1, x.getCategory().getId());
    }

    @Test
    public void findAllByCategoryIdFindsAllExpenses() throws SQLException {
        Plan p = new Plan(1, "testPlan", 10);
        pDao.saveOrUpdate(p);

        Category c = new Category(1, "testCategory", 5, p);
        cDao.saveCategory(c);

        Expense e = new Expense(1, "testExpenseOne", 2.2, c);
        eDao.save(e);

        Expense x = new Expense(2, "testExpenseTwo", 4.1, c);
        eDao.save(x);

        ArrayList<Expense> list = eDao.findAllByCategory(c.getId());

        assertEquals(1, list.get(0).getId());
        assertEquals("testExpenseOne", list.get(0).getName());
        assertEquals(2.2, list.get(0).getAmount(), 0);
        assertEquals(1, list.get(0).getCategory().getId());

        assertEquals(2, list.get(1).getId());
        assertEquals("testExpenseTwo", list.get(1).getName());
        assertEquals(4.1, list.get(1).getAmount(), 0);
        assertEquals(1, list.get(1).getCategory().getId());
    }

    @Test
    public void expenseIsDeletedCorrectly() throws SQLException {
        Plan p = new Plan(1, "testPlan", 10);
        pDao.saveOrUpdate(p);

        Category c = new Category(1, "testCategory", 5, p);
        cDao.saveCategory(c);

        Expense e = new Expense(1, "testExpense", 2.2, c);
        eDao.save(e);

        eDao.delete(1);

        assertEquals(null, eDao.findOne(1));
    }

    @Test
    public void expensesAreDeletedCorrectlyWhenDeletingByCategoryId() throws SQLException {
        Plan p = new Plan(1, "testPlan", 10);
        pDao.saveOrUpdate(p);

        Category c = new Category(1, "testCategory", 5, p);
        cDao.saveCategory(c);

        Expense e = new Expense(1, "testExpenseOne", 2.2, c);
        eDao.save(e);

        Expense x = new Expense(2, "testExpenseTwo", 4.1, c);
        eDao.save(x);

        eDao.deleteAllByCategoryId(1);

        assertTrue(eDao.findAllByCategory(1).isEmpty());
    }

    @Test
    public void findOneReturnsNullIfExpenseDoesNotExist() throws SQLException {
        Expense e = eDao.findOne(1);

        assertEquals(null, e);
    }

    @Test
    public void findOneByNameAndPlanIdReturnsNullIfCategoryDoesNotExist() throws SQLException {
        Plan p = new Plan(1, "testPlan", 10);
        pDao.saveOrUpdate(p);

        Category c = cDao.findOneByNameAndPlanId("hello", 1);

        assertEquals(null, c);
    }
}
