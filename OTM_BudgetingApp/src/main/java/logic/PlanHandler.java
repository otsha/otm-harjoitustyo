package logic;

import dao.CategoryDao;
import dao.PlanDao;
import data.Category;
import data.Database;
import data.Plan;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class PlanHandler {
    
    private PlanDao pDao;
    private CategoryDao cDao;
    
    public PlanHandler(Database db) {
        this.pDao = new PlanDao(db);
        this.cDao = new CategoryDao(db);
    }
    
    public ObservableList<String> getAllPlans() {

        // Return all plans in the database as an ObservableList
        ObservableList<String> items = FXCollections.observableArrayList();
        try {
            ArrayList<Plan> planList = pDao.findAll();
            if (!planList.isEmpty()) {
                planList.stream().map(p -> p.getName()).forEach(n -> items.add(n));
            }
        } catch (SQLException ex) {
            
        }
        
        return items;
    }
    
    public Plan createPlan(String name, String budget) {
        if (name.equals("") || budget.equals("")) {
            return null;
        } else {
            Plan p = new Plan(0, name, Double.parseDouble(budget));
            
            try {
                pDao.saveOrUpdate(p);
                try {
                    return pDao.findOneByName(p.getName());
                } catch (SQLException ex) {
                    return null;
                }
            } catch (SQLException ex) {
                return null;
            }
        }
    }
    
    public Plan openPlan(ListView<String> list) {
        try {
            if (!getAllPlans().isEmpty()) {
                Plan p = pDao.findOneByName(list.getSelectionModel().selectedItemProperty().getValue());
                return p;
            }
        } catch (SQLException ex) {
            return null;
        }
        return null;
    }
    
    public boolean deletePlan(ListView<String> list) {
        try {
            if (!getAllPlans().isEmpty()) {
                Plan p = pDao.findOneByName(list.getSelectionModel().selectedItemProperty().getValue());
                cDao.deleteAllByPlanId(p.getId());
                pDao.delete(p.getId());
                
                return true;
            }
        } catch (SQLException ex) {
            return false;
        }
        
        return false;
    }
    
    public ObservableList<String> getAllCategories(int planId) {

        // Return all plans in the database as an ObservableList
        ObservableList<String> items = FXCollections.observableArrayList();
        try {
            ArrayList<Category> categoryList = cDao.findAllByPlanId(planId);
            if (!categoryList.isEmpty()) {
                categoryList.stream().map(p -> p.getName()).forEach(n -> items.add(n));
            }
        } catch (SQLException ex) {
            
        }
        
        return items;
    }
    
    public boolean createCategory(String name, Plan p) {
        if (name.equals("")) {
            return false;
        } else {
            Category c = new Category(0, name, 10.0, p);
            
            try {
                cDao.saveCategory(c);
                return true;
            } catch (SQLException ex) {
                return false;
            }
        }
    }
    
    public boolean deleteCategory(ListView<String> list, Plan p) {
        try {
            if (!getAllPlans().isEmpty()) {
                Category c = cDao.findOneByNameAndPlanId(list.getSelectionModel().selectedItemProperty().getValue(), p.getId());
                cDao.delete(c.getId());
                
                return true;
            }
        } catch (SQLException ex) {
            return false;
        }
        
        return false;
    }
}
