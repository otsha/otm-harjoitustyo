package logic;

import dao.CategoryDao;
import dao.ExpenseDao;
import dao.PlanDao;
import data.Category;
import data.Database;
import data.Plan;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class PlanHandlerTest {

    private Database db;
    private Connection conn;
    private PlanHandler planHandler;

    private PlanDao pDao;
    private CategoryDao cDao;
    private ExpenseDao eDao;

    @Before
    public void setUp() throws SQLException {

        // Set up the Database and DAOs.
        this.db = new Database("jdbc:sqlite:test.db");
        this.conn = db.getConnection();
        this.planHandler = new PlanHandler(db);

        this.pDao = new PlanDao(db);
        this.cDao = new CategoryDao(db);
        this.eDao = new ExpenseDao(db);

    }

    @After
    public void tearDown() throws SQLException {

        // Delete the test database after a test is complete
        File dbFile = new File("test.db");
        dbFile.delete();
        conn.close();
    }

    @Test
    public void getAllPlansGetsAllPlanNamesAsAnObservableList() throws SQLException {
        pDao.save("testPlanOne", 10.0);
        pDao.save("testPlanTwo", 20.0);

        ObservableList<String> items = planHandler.getAllPlans();
        assertTrue(items.contains("testPlanOne"));
        assertTrue(items.contains("testPlanTwo"));
    }

    @Test
    public void createPlanSavesAndReturnsANewPlan() throws SQLException {
        Plan p = planHandler.createPlan("testPlan", "10.0");
        assertEquals("testPlan", p.getName());
        assertEquals(10.0, p.getBudget(), 0);
    }

    @Test
    public void openPlanReturnsTheSelectedPlan() throws SQLException {
        pDao.save("testPlanOne", 10.0);
        pDao.save("testPlanTwo", 20.0);

        Plan p = planHandler.openPlan("testPlanOne");
        assertEquals("testPlanOne", p.getName());
        assertEquals(10.0, p.getBudget(), 0);
    }

    @Test
    public void deletePlanReturnsTrueIfPlanWasDeleted() throws SQLException {
        pDao.save("testPlanOne", 10.0);

        assertTrue(planHandler.deletePlan("testPlanOne"));
    }

    @Test
    public void deletePlanReturnsFalseIfPlanWasNotDeleted() throws SQLException {
        pDao.save("testPlanOne", 10.0);

        assertFalse(planHandler.deletePlan("testPlanTwo"));
    }

    @Test
    public void getAllocatedReturnsTheCorrectAmountOfFundsAllocated() throws SQLException {
        pDao.save("testPlanOne", 10.0);
        Plan p = pDao.findOneByName("testPlanOne");
        cDao.save("testCategoryOne", 2.0, p);
        cDao.save("testCategoryTwo", 6.0, p);

        assertEquals(8.0, planHandler.getAllocated(p), 0);
    }

    @Test
    public void getUsedReturnsTheCorrectAmountOfFundsUsedWIthinAPlan() throws SQLException {
        pDao.save("testPlanOne", 10.0);
        Plan p = pDao.findOneByName("testPlanOne");
        cDao.save("testCategoryOne", 2.0, p);
        cDao.save("testCategoryTwo", 6.0, p);
        Category c = cDao.findOne(1);
        Category g = cDao.findOne(2);
        eDao.save("testExpenseOne", 2.2, c);
        eDao.save("testExpenseTwo", 3.2, c);
        eDao.save("testExpenseThree", 4.19, g);

        assertEquals(9.59, planHandler.getUsed(p), 0);
    }

    @Test
    public void getAllCategoriesGetsAllCategoryNamesAsAnObservableList() throws SQLException {
        pDao.save("testPlanOne", 10.0);
        Plan p = pDao.findOne(1);
        cDao.save("testCategoryOne", 2.0, p);
        cDao.save("testCategoryTwo", 6.0, p);

        ObservableList<String> items = planHandler.getAllCategories(p.getId());
        assertTrue(items.contains("testCategoryOne"));
        assertTrue(items.contains("testCategoryTwo"));
    }

    @Test
    public void createCategoryReturnsTrueIfCategoryWasCreated() throws SQLException {
        pDao.save("testPlanOne", 10.0);
        Plan p = pDao.findOne(1);
        assertTrue(planHandler.createCategory("testCategoryOne", "2.0", p));
    }

    @Test
    public void createCategoryReturnsFalseIfCategoryWasNotCreated() throws SQLException {
        pDao.save("testPlanOne", 10.0);
        Plan p = pDao.findOne(1);
        assertFalse(planHandler.createCategory("", "", p));
    }

    @Test
    public void selectedCategoryReturnsTheSelectedCategory() throws SQLException {
        pDao.save("testPlanOne", 10.0);
        Plan p = pDao.findOne(1);
        cDao.save("testCategoryOne", 2.0, p);
        cDao.save("testCategoryTwo", 6.0, p);

        Category c = planHandler.selectedCategory("testCategoryOne", p);
        assertEquals("testCategoryOne", c.getName());
        assertEquals(2.0, c.getAllocated(), 0);
        assertEquals(1, c.getPlan().getId());
    }

    @Test
    public void deleteCategoryReturnsTrueIfCategoryWasDeleted() throws SQLException {
        pDao.save("testPlanOne", 10.0);
        Plan p = pDao.findOne(1);
        cDao.save("testCategoryOne", 2.0, p);

        assertTrue(planHandler.deleteCategory("testCategoryOne", p));
    }

    @Test
    public void deleteCategoryReturnsFalseIfCategoryWasNotDeleted() throws SQLException {
        pDao.save("testPlanOne", 10.0);
        Plan p = pDao.findOne(1);
        cDao.save("testCategoryOne", 2.0, p);

        assertFalse(planHandler.deleteCategory("testCategoryTwo", p));
    }

    @Test
    public void getUsedByCategoryReturnsTheCorrectAmountOfFundsUsedWIthinACategory() throws SQLException {
        pDao.save("testPlanOne", 10.0);
        Plan p = pDao.findOneByName("testPlanOne");
        cDao.save("testCategoryOne", 2.0, p);
        Category c = cDao.findOne(1);
        eDao.save("testExpenseOne", 2.2, c);
        eDao.save("testExpenseTwo", 3.2, c);
        eDao.save("testExpenseThree", 4.19, c);

        assertEquals(9.59, planHandler.getUsedByCategory(c), 0);
    }

    @Test
    public void getAllExpensesGetsAllExpenseNamesAndAmountsAsAnObservableList() throws SQLException {
        pDao.save("testPlanOne", 10.0);
        Plan p = pDao.findOne(1);
        cDao.save("testCategoryOne", 2.0, p);
        Category c = cDao.findOne(1);
        eDao.save("testExpenseOne", 1.19, c);
        eDao.save("testExpenseTwo", 0.55, c);

        ObservableList<String> items = planHandler.getAllExpenses(c.getId());
        assertTrue(items.contains("testExpenseOne" + "\t" + "1.19"));
        assertTrue(items.contains("testExpenseTwo" + "\t" + "0.55"));
    }

    @Test
    public void createExpenseReturnsTrueIfExpenseWasCreated() throws SQLException {
        pDao.save("testPlanOne", 10.0);
        Plan p = pDao.findOne(1);
        cDao.save("testCategoryOne", 2.0, p);
        Category c = cDao.findOne(1);

        assertTrue(planHandler.createExpense("testExpenseOne", "1.19", c));
    }

    @Test
    public void createExpenseReturnsFalseIfExpenseWasNotCreated() throws SQLException {
        pDao.save("testPlanOne", 10.0);
        Plan p = pDao.findOne(1);
        cDao.save("testCategoryOne", 2.0, p);
        Category c = cDao.findOne(1);
        assertFalse(planHandler.createExpense("", "", c));
    }

    @Test
    public void deleteExpenseReturnsTrueIfExpenseWasDeleted() throws SQLException {
        pDao.save("testPlanOne", 10.0);
        Plan p = pDao.findOne(1);
        cDao.save("testCategoryOne", 2.0, p);
        Category c = cDao.findOne(1);
        eDao.save("testExpenseOne", 1.19, c);

        assertTrue(planHandler.deleteExpense("testExpenseOne", c));
    }

    @Test
    public void deleteExpenseReturnsFalseIfExpenseWasNotDeleted() throws SQLException {
        pDao.save("testPlanOne", 10.0);
        Plan p = pDao.findOne(1);
        cDao.save("testCategoryOne", 2.0, p);
        Category c = cDao.findOne(1);

        assertFalse(planHandler.deleteExpense("testExpenseOne", c));
    }

    @Test
    public void usedByCategoryReturnsTheAmountOfFundsUsedByCategory() throws SQLException {
        pDao.save("testPlanOne", 10.0);
        Plan p = pDao.findOneByName("testPlanOne");
        cDao.save("testCategoryOne", 2.0, p);
        cDao.save("testCategoryTwo", 6.0, p);
        Category c = cDao.findOne(1);
        Category g = cDao.findOne(2);
        eDao.save("testExpenseOne", 2.2, c);
        eDao.save("testExpenseTwo", 3.2, c);
        eDao.save("testExpenseThree", 4.19, g);

        assertEquals(5.4, planHandler.usedByCategory(c), 0);
    }
}
