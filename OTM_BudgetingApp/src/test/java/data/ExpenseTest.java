package data;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpenseTest {

    private Expense e;

    @Before
    public void setUp() {
        this.e = new Expense(1, "testExpense", 4.2, new Category(1, "testCategory", 10, new Plan(1, "testPlan", 20)));
    }

    @Test
    public void expenseIsInitializedCorrectly() {
        assertEquals(1, e.getId());
        assertEquals("testExpense", e.getName());
        assertEquals(4.20, e.getAmount(), 0);

        assertEquals(1, e.getCategory().getId());
        assertEquals("testCategory", e.getCategory().getName());
        assertEquals(10.0, e.getCategory().getAllocated(), 0);

        assertEquals(1, e.getCategory().getPlan().getId());
        assertEquals("testPlan", e.getCategory().getPlan().getName());
        assertEquals(20.0, e.getCategory().getPlan().getBudget(), 0);
    }

    @Test
    public void setNameSetsANewName() {
        e.setName("hello");
        assertEquals(e.getName(), "hello");
    }

    @Test
    public void setAllocatedSetsANewAllocation() {
        e.setAmount(40.52);
        assertEquals(40.52, e.getAmount(), 0);
    }

    @Test
    public void setIdSetsANewId() {
        e.setId(2);
        assertEquals(2, e.getId());
    }
}
