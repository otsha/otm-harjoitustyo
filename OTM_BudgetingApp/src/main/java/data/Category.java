package data;

public class Category {
    
    public int id;
    public String name;
    public double allocated;
    public Plan plan;
    
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

    public void setPlan(Plan plan) {
        this.plan = plan;
    }
    
}
