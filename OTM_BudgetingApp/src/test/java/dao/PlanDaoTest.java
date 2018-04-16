/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author haaotso
 */
public class PlanDaoTest {

    private Database db;
    private Connection conn;
    private PlanDao pDao;

    @Before
    public void setUp() throws SQLException {

        // Database and connection setup
        this.db = new Database("jdbc:sqlite:test.db");
        this.conn = db.getConnection();

        PreparedStatement createPlanTable = conn.prepareStatement("CREATE TABLE Plan (id integer PRIMARY KEY, name varchar(255), budget float);");
        createPlanTable.execute();

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
        Plan p = new Plan(1, "test", 10);

        pDao.saveOrUpdate(p);

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Plan WHERE id = 1");
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Plan q = new Plan(rs.getInt("id"), rs.getString("name"), rs.getDouble("budget"));
            assertEquals(1, q.getId());
            assertEquals("test", q.getName());
            assertEquals((double) 10, q.getBudget(), 0);
        } else {
            assertEquals(-1, p.getId());
            assertEquals("fail", p.getName());
            assertEquals(11, p.getBudget(), 0);
        }
    }
    
    @Test
    public void planIsUpdatedProperly() throws SQLException {
        Plan p = new Plan(1, "test", 10);
        
        pDao.saveOrUpdate(p);
        
        p.setName("hello");
        p.setBudget(20);
        
        pDao.saveOrUpdate(p);
        
        Plan q = pDao.findOne(1);
        
        assertEquals(1, q.getId());
        assertEquals("hello", q.getName());
        assertEquals((double) 20, q.getBudget(), 0);
    }

    @Test
    public void planIsFetchedProperlyById() throws SQLException {
        Plan p = new Plan(1, "test", 10);

        pDao.saveOrUpdate(p);
        Plan q = pDao.findOne(1);

        assertEquals(1, q.getId());
        assertEquals("test", q.getName());
        assertEquals((double) 10, p.getBudget(), 0);
    }

    @Test
    public void planIsFetchedProperlyByName() throws SQLException {
        Plan p = new Plan(1, "test", 10);

        pDao.saveOrUpdate(p);
        Plan q = pDao.findOneByName("test");

        assertEquals(1, q.getId());
        assertEquals("test", q.getName());
        assertEquals((double) 10, p.getBudget(), 0);
    }

    @Test
    public void findAllFindsAllPlans() throws SQLException {
        Plan p = new Plan(1, "ptest", 10);
        Plan q = new Plan(2, "qtest", 20);

        pDao.saveOrUpdate(p);
        pDao.saveOrUpdate(q);

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
        Plan p = new Plan(1, "test", 10);
        
        pDao.saveOrUpdate(p);
        
        pDao.delete(p.getId());
        
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
