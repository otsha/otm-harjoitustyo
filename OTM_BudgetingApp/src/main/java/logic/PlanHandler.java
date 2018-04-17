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

    // PLANS
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
            } else {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
    }

    public double getUsed(Plan p) {
        // This will use the expense class at a later stage in development
        try {
            ArrayList<Category> categories = cDao.findAllByPlanId(p.getId());
            double used = categories.stream()
                    .mapToDouble(c -> c.getAllocated())
                    .sum();
            
            return used;
        } catch (SQLException ex) {
            return 0.0;
        }        
    }

    // CATEGORIES
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

    public boolean createCategory(String name, String allocation, Plan p) {
        if (name.equals("") || allocation.equals("")) {
            return false;
        } else {
            double allocationAsDouble = 0;

            try {
                allocationAsDouble = Double.parseDouble(allocation);
            } catch (Exception ex) {
                return false;
            }

            Category c = new Category(0, name, allocationAsDouble, p);

            try {
                cDao.saveCategory(c);
                return true;
            } catch (SQLException ex) {
                return false;
            }
        }
    }
    
    public Category editCategory(ListView<String> list, Plan p) {
        try {
            if (!getAllPlans().isEmpty()) {
                Category c = cDao.findOneByNameAndPlanId(list.getSelectionModel().selectedItemProperty().getValue(), p.getId());

                return c;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            return null;
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
