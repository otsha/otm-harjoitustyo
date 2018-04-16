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
public class CategoryTest {
    
    private Category c;

    @Before
    public void setUp() {
        this.c = new Category(1, "testCategory", 10, new Plan(1, "testPlan", 20));
    }
    
    @Test
    public void categoryIsInitializedCorrectly() {
        assertEquals(1, c.getId());
        assertEquals("testCategory", c.getName());
        assertEquals(10.0, c.getAllocated(), 0);
        
        assertEquals(1, c.getPlan().getId());
        assertEquals("testPlan", c.getPlan().getName());
        assertEquals(20.0, c.getPlan().getBudget(), 0);
    }

    @Test
    public void setNameSetsANewName() {
        c.setName("hello");
        assertEquals(c.getName(), "hello");
    }

    @Test
    public void setAllocatedSetsANewAllocation() {
        c.setAllocated(500.0);
        assertEquals(500.0, c.getAllocated(), 0);
    }

    @Test
    public void setIdSetsANewId() {
        c.setId(2);
        assertEquals(2, c.getId());
    }
}
