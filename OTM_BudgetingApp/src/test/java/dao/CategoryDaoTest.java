package dao;

import data.Category;
import data.Database;
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

public class CategoryDaoTest {

    private Database db;
    private Connection conn;
    private CategoryDao cDao;
    private PlanDao pDao;

    @Before
    public void setUp() throws SQLException {

        // Database and connection setup
        this.db = new Database("jdbc:sqlite:test.db");
        
        this.conn = db.getConnection();

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
    public void categoryIsSavedProperly() throws SQLException {
        Plan p = new Plan(1, "testPlan", 10);
        pDao.save(p);

        Category c = new Category(1, "testCategory", 5, p);
        cDao.save(c);

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Category WHERE id = 1");
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Category g = new Category(rs.getInt("id"), rs.getString("name"), rs.getDouble("allocated"), pDao.findOne(rs.getInt("plan_id")));
            assertEquals(1, g.getId());
            assertEquals("testCategory", g.getName());
            assertEquals((double) 5, g.getAllocated(), 0);
            assertEquals(1, g.getPlan().getId());
        } else {
            assertEquals(-1, c.getId());
            assertEquals("fail", c.getName());
            assertEquals(6, c.getAllocated(), 0);
            assertEquals(-1, c.getPlan().getId());
        }
    }
    
    @Test
    public void categoryIsNotSavedIfOneWithIdenticalNameAlreadyExists() throws SQLException {
        Plan p = new Plan(1, "testPlan", 10);
        pDao.save(p);
        
        Category c = new Category(1, "testCategory", 5, p);
        cDao.save(c);
        
        Category g = new Category(1, "testCategory", 10, p);
        assertFalse(cDao.save(g));
        
    }

    @Test
    public void categoryIsFetchedProperly() throws SQLException {
        Plan p = new Plan(1, "testPlan", 10);
        pDao.save(p);

        Category c = new Category(1, "testCategory", 5, p);
        cDao.save(c);

        Category g = cDao.findOne(1);
        assertEquals(1, g.getId());
        assertEquals("testCategory", g.getName());
        assertEquals((double) 5, g.getAllocated(), 0);
        assertEquals(1, g.getPlan().getId());
    }

    @Test
    public void categoryIsFetchedProperlyByNameAndPlanId() throws SQLException {
        Plan p = new Plan(1, "testPlan", 10);
        pDao.save(p);

        Category c = new Category(1, "testCategory", 5, p);
        cDao.save(c);

        Category g = cDao.findOneByNameAndPlanId("testCategory", 1);
        assertEquals(1, g.getId());
        assertEquals("testCategory", g.getName());
        assertEquals((double) 5, g.getAllocated(), 0);
        assertEquals(1, g.getPlan().getId());
    }

    @Test
    public void findAllFindsAllCategories() throws SQLException {
        Plan p = new Plan(1, "testPlan", 10);
        pDao.save(p);

        Category c = new Category(1, "testCategory", 5, p);
        cDao.save(c);

        Category g = new Category(2, "testCategoryTwo", 5, p);
        cDao.save(g);

        ArrayList<Category> list = cDao.findAll();

        assertEquals(1, list.get(0).getId());
        assertEquals("testCategory", list.get(0).getName());
        assertEquals((double) 5, list.get(0).getAllocated(), 0);
        assertEquals(1, list.get(0).getPlan().getId());

        assertEquals(2, list.get(1).getId());
        assertEquals("testCategoryTwo", list.get(1).getName());
        assertEquals((double) 5, list.get(1).getAllocated(), 0);
        assertEquals(1, list.get(1).getPlan().getId());
    }

    @Test
    public void findAllByPlanIdFindsAllCategories() throws SQLException {
        Plan p = new Plan(1, "testPlan", 10);
        pDao.save(p);

        Category c = new Category(1, "testCategory", 5, p);
        cDao.save(c);

        Category g = new Category(2, "testCategoryTwo", 5, p);
        cDao.save(g);

        ArrayList<Category> list = cDao.findAllByPlanId(p.getId());

        assertEquals(1, list.get(0).getId());
        assertEquals("testCategory", list.get(0).getName());
        assertEquals((double) 5, list.get(0).getAllocated(), 0);
        assertEquals(1, list.get(0).getPlan().getId());

        assertEquals(2, list.get(1).getId());
        assertEquals("testCategoryTwo", list.get(1).getName());
        assertEquals((double) 5, list.get(1).getAllocated(), 0);
        assertEquals(1, list.get(1).getPlan().getId());
    }

    @Test
    public void categoryIsDeletedCorrectly() throws SQLException {
        Plan p = new Plan(1, "testPlan", 10);
        pDao.save(p);

        Category c = new Category(1, "testCategory", 5, p);
        cDao.save(c);

        cDao.delete(p.getId());

        assertEquals(null, cDao.findOne(1));
    }

    @Test
    public void categoriesAreDeletedCorrectlyWhenDeletingByPlanId() throws SQLException {
        Plan p = new Plan(1, "testPlan", 10);
        pDao.save(p);

        Category c = new Category(1, "testCategory", 5, p);
        cDao.save(c);

        Category g = new Category(2, "testCategoryTwo", 5, p);
        cDao.save(g);

        cDao.deleteAllByPlanId(p.getId());

        assertTrue(cDao.findAllByPlanId(1).isEmpty());
    }

    @Test
    public void findOneReturnsNullIfCategoryDoesNotExist() throws SQLException {
        Category c = cDao.findOne(1);

        assertEquals(null, c);
    }

    @Test
    public void findOneByNameAndPlanIdReturnsNullIfCategoryDoesNotExist() throws SQLException {
        Plan p = new Plan(1, "testPlan", 10);
        pDao.save(p);

        Category c = cDao.findOneByNameAndPlanId("hello", 1);

        assertEquals(null, c);
    }
}
