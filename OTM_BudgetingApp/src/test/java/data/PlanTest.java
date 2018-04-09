/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author haaotso
 */
public class PlanTest {
    
    private Plan p;
    
    public PlanTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.p = new Plan(1, "test", 1000.0);
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void planIsInitializedCorrectly() {
        assertEquals(1, p.getId());
        assertEquals("test", p.getName());
        assertEquals(1000.0, p.getBudget(), 0);
    }
    
    @Test
    public void setNameSetsANewName() {
        p.setName("hello");
        assertEquals(p.getName(), "hello");
    }
    
    @Test
    public void setBudgetSetsANewBudget() {
        p.setBudget(500.0);
        assertEquals(500.0, p.getBudget(), 0);
    }
    
    @Test
    public void setIdSetsANewId() {
        p.setId(2);
        assertEquals(2, p.getId());
    }
}
