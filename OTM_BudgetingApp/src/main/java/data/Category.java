package data;

public class Category {
    
    private int id;
    private String name;
    private double allocated;
    private final Plan plan;
    
    public Category(int id, String name, double allocated, Plan plan) {
        this.id = id;
        this.name = name;
        this.allocated = allocated;
        this.plan = plan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAllocated() {
        return allocated;
    }

    public void setAllocated(double allocated) {
        this.allocated = allocated;
    }

    public Plan getPlan() {
        return plan;
    }
}
