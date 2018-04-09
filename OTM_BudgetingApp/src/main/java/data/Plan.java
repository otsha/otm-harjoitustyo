/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author haaotso
 */
public class Plan {
    private int id;
    private String name;
    private double budget;
    
    public Plan(int id, String name, double budget) {
        this.id = id;
        this.name = name;
        this.budget = budget;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public double getAmount() {
        return budget;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setBudget(double budget) {
        this.budget = budget;
    }
}
