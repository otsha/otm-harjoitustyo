package dao;

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
import org.junit.Before;
import org.junit.Test;

public class PlanDaoTest {

    private Database db;
    private Connection conn;
    private PlanDao pDao;

    @Before
    public void setUp() throws SQLException {

        // Database and connection setup
        this.db = new Database("jdbc:sqlite:test.db");
        this.conn = db.getConnection();

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
    public void planIsSavedProperly() throws SQLException {
        pDao.save("test", 10);

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Plan WHERE id = 1");
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Plan q = new Plan(rs.getInt("id"), rs.getString("name"), rs.getDouble("budget"));
            assertEquals(1, q.getId());
            assertEquals("test", q.getName());
            assertEquals((double) 10, q.getBudget(), 0);
        } else {
            assertEquals(-1, 1);
            assertEquals("fail", "test");
            assertEquals(11, 10, 0);
        }
    }
    
    @Test
    public void planIsNotSavedIfOneWithIdenticalNameAlreadyExists() throws SQLException {
        pDao.save("test", 10);
        
        assertFalse(pDao.save("test", 15));
    }

    @Test
    public void planIsFetchedProperlyById() throws SQLException {
        pDao.save("test", 10);
        Plan q = pDao.findOne(1);

        assertEquals(1, q.getId());
        assertEquals("test", q.getName());
        assertEquals((double) 10, q.getBudget(), 0);
    }

    @Test
    public void planIsFetchedProperlyByName() throws SQLException {
        pDao.save("test", 10);
        Plan q = pDao.findOneByName("test");

        assertEquals(1, q.getId());
        assertEquals("test", q.getName());
        assertEquals((double) 10, q.getBudget(), 0);
    }

    @Test
    public void findAllFindsAllPlans() throws SQLException {
        pDao.save("ptest", 10);
        pDao.save("qtest", 20);

        ArrayList<Plan> list = pDao.findAll();

        assertEquals(1, list.get(0).getId());
        assertEquals("ptest", list.get(0).getName());
        assertEquals((double) 10, list.get(0).getBudget(), 0);

        assertEquals(2, list.get(1).getId());
        assertEquals("qtest", list.get(1).getName());
        assertEquals((double) 20, list.get(1).getBudget(), 0);
    }

    @Test
    public void planIsDeletedCorrectly() throws SQLException {
        pDao.save("test", 10);

        pDao.delete(1);

        assertEquals(null, pDao.findOne(1));
    }

    @Test
    public void findOneReturnsNullIfPlanDoesNotExist() throws SQLException {
        Plan q = pDao.findOne(1);

        assertEquals(null, q);
    }

    @Test
    public void findOneByNameReturnsNullIfPlanDoesNotExist() throws SQLException {
        Plan q = pDao.findOneByName("test");

        assertEquals(null, q);
    }
}
