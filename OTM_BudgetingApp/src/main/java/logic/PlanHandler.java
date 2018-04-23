package logic;

import dao.CategoryDao;
import dao.ExpenseDao;
import dao.PlanDao;
import data.Category;
import data.Database;
import data.Expense;
import data.Plan;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class PlanHandler {

    private PlanDao pDao;
    private CategoryDao cDao;
    private ExpenseDao eDao;

    public PlanHandler(Database db) {
        this.pDao = new PlanDao(db);
        this.cDao = new CategoryDao(db);
        this.eDao = new ExpenseDao(db);
    }

    // 1 Plans
    // 1.1 Get all plans in the database as an ObservableList
    public ObservableList<String> getAllPlans() {
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

    // 1.2 Validate the input, then create a new plan in the database and return it
    public Plan createPlan(String name, String budget) {
        if (name.equals("") || budget.equals("")) {
            return null;
        } else {
            Plan p = new Plan(0, name, Double.parseDouble(budget));

            try {
                if (pDao.findOneByName(name) == null) {
                    pDao.saveOrUpdate(p);
                    try {
                        return pDao.findOneByName(p.getName());
                    } catch (SQLException ex) {
                        return null;
                    }
                } else {
                    return null;
                }
            } catch (SQLException ex) {
                return null;
            }
        }
    }

    // 1.3 Return the plan selected in the given ListView object
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

    // 1.4 Delete the plan selected in the given ListView object
    public boolean deletePlan(ListView<String> list) {
        try {
            if (!getAllPlans().isEmpty()) {
                Plan p = pDao.findOneByName(list.getSelectionModel().selectedItemProperty().getValue());
                if (p != null) {
                    eDao.deleteAllByPlanId(p.getId());
                    cDao.deleteAllByPlanId(p.getId());
                    pDao.delete(p.getId());
                }

                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
    }

    // 1.5 Get the amount of money currently ALLOCATED to categories within the plan
    public double getAllocated(Plan p) {
        try {
            ArrayList<Category> categories = cDao.findAllByPlanId(p.getId());
            double allocated = categories.stream()
                    .mapToDouble(c -> c.getAllocated())
                    .sum();

            return allocated;
        } catch (SQLException ex) {
            return 0.0;
        }
    }

    // 1.6 Get the amount of money currently USED by categories within the plan
    public double getUsed(Plan p) {
        double used = 0;
        try {
            ArrayList<Expense> allExpenses = new ArrayList<>();
            ArrayList<Category> categories = cDao.findAllByPlanId(p.getId());
            categories.stream().forEach(c -> {
                try {
                    ArrayList<Expense> expenses = eDao.findAllByCategory(c.getId());
                    expenses.stream().forEach(e -> allExpenses.add(e));
                } catch (SQLException ex) {

                }

            });

            if (!allExpenses.isEmpty()) {
                used = allExpenses.stream().mapToDouble(e -> e.getAmount()).sum();
            }

            return used;
        } catch (SQLException ex) {
            return used;
        }
    }

    // 2 CATEGORIES
    // 2.1 Return all the categories in the database as an ObservableList
    public ObservableList<String> getAllCategories(int planId) {
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

    // 2.2 Validate the input and create a category in the database with the given parameters
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

    // 2.3 Return the category selected in the given ListView
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

    // 2.4 Delete the category selected in the given ListView
    public boolean deleteCategory(ListView<String> list, Plan p) {
        try {
            if (!getAllPlans().isEmpty()) {
                Category c = cDao.findOneByNameAndPlanId(list.getSelectionModel().selectedItemProperty().getValue(), p.getId());
                if (c != null) {
                    eDao.deleteAllByCategoryId(c.getId());
                    cDao.delete(c.getId());
                }
                return true;
            }
        } catch (SQLException ex) {
            return false;
        }

        return false;
    }

    // 3 Expenses
    // 3.1 Return all the expenses in the database associated with the given Plan as an ObservableList
    public ObservableList<String> getAllExpenses(int categoryId) {
        ObservableList<String> items = FXCollections.observableArrayList();
        try {
            ArrayList<Expense> expenseList = eDao.findAllByCategory(categoryId);
            if (!expenseList.isEmpty()) {
                expenseList.stream().forEach(e -> items.add(e.getName() + "\t" + e.getAmount()));
            }
        } catch (SQLException ex) {

        }

        return items;
    }

    // 3.2 Validate the input and create a category in the database with the given parameters
    public boolean createExpense(String name, String amount, Category c) {
        if (name.equals("") || amount.equals("")) {
            return false;
        } else {
            double amountAsDouble = 0;

            try {
                amountAsDouble = Double.parseDouble(amount);
            } catch (Exception ex) {
                return false;
            }

            Expense e = new Expense(0, name, amountAsDouble, c);

            try {
                eDao.save(e);
                return true;
            } catch (SQLException ex) {
                return false;
            }
        }
    }

    // 3.3 Delete the selected expense in the given ListView
    public boolean deleteExpense(ListView<String> list, Category c) {
        try {
            if (!getAllExpenses(c.getId()).isEmpty()) {
                String[] split = list.getSelectionModel().selectedItemProperty().getValue().split("\t");
                Expense e = eDao.findOneByNameAndCategoryId(split[0], c.getId());
                if (e != null) {
                    eDao.delete(e.getId());
                }
                return true;
            }
        } catch (SQLException ex) {
            return false;
        }
        return false;
    }
    
    // 3.4 Get the total expenses currently in this category
    public double usedByCategory(Category c) {
        double used = 0.0;
        
        try {
            ArrayList<Expense> expenses = eDao.findAllByCategory(c.getId());
            used = expenses.stream().mapToDouble(e -> e.getAmount()).sum();
            return used;
        } catch (SQLException ex) {
            return used;
        }
    }
}
