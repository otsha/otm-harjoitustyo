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
import static org.junit.Assert.assertFalse;
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
        pDao.save("testPlan", 10);
        Plan p = pDao.findOne(1);

        cDao.save("testCategory", 5, p);
        Category c = cDao.findOne(1);

        eDao.save("testExpense", 2.2, c);

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
            assertEquals(-1, 1);
            assertEquals("fail", "testExpense");
            assertEquals(5000, 2.2, 0);
            assertEquals(-1, c.getId());
        }
    }

    @Test
    public void expenseIsNotSavedIfOneWithIdenticalNameAlreadyExistsWithinCategory() throws SQLException {
        pDao.save("testPlan", 10);
        Plan p = pDao.findOne(1);

        cDao.save("testCategory", 5, p);
        Category c = cDao.findOne(1);

        eDao.save("testExpense", 2.2, c);

        assertFalse(eDao.save("testExpense", 5.1, c));
    }

    @Test
    public void expenseIsFetchedProperly() throws SQLException {
        pDao.save("testPlan", 10);
        Plan p = pDao.findOne(1);

        cDao.save("testCategory", 5, p);
        Category c = cDao.findOne(1);

        eDao.save("testExpense", 2.2, c);

        Expense x = eDao.findOne(1);

        assertEquals(1, x.getId());
        assertEquals("testExpense", x.getName());
        assertEquals(2.2, x.getAmount(), 0);
        assertEquals(1, x.getCategory().getId());
    }

    @Test
    public void expenseIsFetchedProperlyByNameAndCategoryId() throws SQLException {
        pDao.save("testPlan", 10);
        Plan p = pDao.findOne(1);

        cDao.save("testCategory", 5, p);
        Category c = cDao.findOne(1);

        eDao.save("testExpense", 2.2, c);

        Expense x = eDao.findOneByNameAndCategoryId("testExpense", c.getId());

        assertEquals(1, x.getId());
        assertEquals("testExpense", x.getName());
        assertEquals(2.2, x.getAmount(), 0);
        assertEquals(1, x.getCategory().getId());
    }

    @Test
    public void findAllByCategoryIdFindsAllExpenses() throws SQLException {
        pDao.save("testPlan", 10);
        Plan p = pDao.findOne(1);

        cDao.save("testCategory", 5, p);
        Category c = cDao.findOne(1);

        eDao.save("testExpenseOne", 2.2, c);

        eDao.save("testExpenseTwo", 4.1, c);

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
        pDao.save("testPlan", 10);
        Plan p = pDao.findOne(1);

        cDao.save("testCategory", 5, p);
        Category c = cDao.findOne(1);

        eDao.save("testExpense", 2.2, c);

        eDao.delete(1);

        assertEquals(null, eDao.findOne(1));
    }

    @Test
    public void expensesAreDeletedCorrectlyWhenDeletingByCategoryId() throws SQLException {
        pDao.save("testPlan", 10);
        Plan p = pDao.findOne(1);

        cDao.save("testCategory", 5, p);
        Category c = cDao.findOne(1);

        eDao.save("testExpenseOne", 2.2, c);

        eDao.save("testExpenseTwo", 4.1, c);

        eDao.deleteAllByCategoryId(1);

        assertTrue(eDao.findAllByCategory(1).isEmpty());
    }

    @Test
    public void expensesAreDeletedCorrectlyWhenDeletingByPlanid() throws SQLException {
        pDao.save("testPlan", 10);
        Plan p = pDao.findOne(1);

        cDao.save("testCategory", 5, p);
        Category c = cDao.findOne(1);

        eDao.save("testExpenseOne", 2.2, c);

        eDao.save("testExpenseTwo", 4.1, c);

        eDao.deleteAllByPlanId(1);

        assertTrue(eDao.findAllByCategory(1).isEmpty());
    }

    @Test
    public void findOneReturnsNullIfExpenseDoesNotExist() throws SQLException {
        Expense e = eDao.findOne(1);

        assertEquals(null, e);
    }

    @Test
    public void findOneByNameAndCategoryIdReturnsNullIfExpenseDoesNotExist() throws SQLException {
        Expense e = eDao.findOneByNameAndCategoryId("testExpense", 1);

        assertEquals(null, e);
    }
}
