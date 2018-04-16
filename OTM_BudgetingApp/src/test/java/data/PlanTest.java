package data;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlanTest {

    private Plan p;

    @Before
    public void setUp() {
        this.p = new Plan(1, "test", 1000.0);
    }

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
