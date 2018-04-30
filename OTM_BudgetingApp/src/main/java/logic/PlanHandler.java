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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PlanHandler {

    private PlanDao pDao;
    private CategoryDao cDao;
    private ExpenseDao eDao;

    public PlanHandler(Database db) {
        this.pDao = new PlanDao(db);
        this.cDao = new CategoryDao(db);
        this.eDao = new ExpenseDao(db);
    }

    // 1 - PLANS
    /**
     * Get a list of all the plans in the database, to be used for populating a
     * ListView object.
     *
     * @return A list of the names of all the Plans in the database
     * @see dao.PlanDao#findAll()
     */
    public ObservableList<String> getAllPlans() {
        ObservableList<String> items = FXCollections.observableArrayList();
        try {
            ArrayList<Plan> planList = pDao.findAll();
            if (!planList.isEmpty()) {
                planList.stream().map(p -> p.getName()).forEach(n -> items.add(n));
            }
        } catch (SQLException ex) {
            /* Do nothing - an empty list will be returned at the end of the method */
        }

        return items;
    }

    /**
     * Validate the input, ask PlanDao to create a new Plan in the database,
     * then return the newly created Plan.
     *
     * @param name The desired name as inputted by the user. Cannot be blank.
     * @param budget The budget of the Plan as inputted by the user. Cannot be
     * blank, must be parse-able to Double.
     * @return A newly created Plan with the given name and budget. Null if
     * input was invalid or there was an error creating the Plan.
     * @see dao.PlanDao#save(java.lang.String, double)
     */
    public Plan createPlan(String name, String budget) {
        if (name.equals("") || budget.equals("")) {
            return null;
        } else {
            double budgetAsDouble = 0;

            try {
                budgetAsDouble = Double.parseDouble(budget);
            } catch (Exception ex) {
                return null;
            }

            try {
                pDao.save(name, budgetAsDouble);
                return pDao.findOneByName(name);
            } catch (SQLException ex) {
                return null;
            }
        }
    }

    /**
     * Ask PlanDao to fetch a Plan with the given name from the database.
     *
     * @param name The name of the Plan to be searched as selected in the UI.
     * @return An existing Plan with a name corresponding to the given
     * parameter. Null if none was found or an error occurred.
     * @see dao.PlanDao#findOneByName(java.lang.String)
     */
    public Plan openPlan(String name) {
        try {
            Plan p = pDao.findOneByName(name);
            return p;
        } catch (SQLException ex) {
            return null;
        }
    }

    /**
     * Ask PlanDao to find a Plan with the given name from the database, then
     * delete the Plan. Also deletes all the Categories and Expenses associated
     * with the Plan.
     *
     * @param name The name of the Plan to be deleted as selected in the UI.
     * @return True if the Plan was successfully deleted. False if the Plan does
     * not exist or if an error occurred.
     * @see dao.PlanDao#delete(java.lang.Integer)
     * @see dao.CategoryDao#deleteAllByPlanId(java.lang.Integer)
     * @see dao.ExpenseDao#deleteAllByPlanId(int)
     */
    public boolean deletePlan(String name) {
        try {
            Plan p = pDao.findOneByName(name);
            if (p != null) {
                eDao.deleteAllByPlanId(p.getId());
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

    /**
     * Ask CategoryDao to find all the categories within a given Plan and sum
     * their allocations together.
     *
     * @param p The Plan to be examined as selected in the UI.
     * @return The amount of funds currently allocated to all Categories in
     * total within the Plan.
     * @see dao.CategoryDao#findAllByPlanId(int)
     */
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

    /**
     * Request the list of all Categories associated with the given Plan, then
     * sum the amounts of all the Expenses within the categories.
     *
     * @param p The Plan to be examined as selected in the UI.
     * @return The amount of funds actually used by all the Categories within
     * the Plan
     * @see logic.PlanHandler#getAllocated(data.Plan)
     * @see dao.CategoryDao#findAllByPlanId(int)
     * @see dao.ExpenseDao#findAllByCategory(int)
     */
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

    // 2 - CATEGORIES
    /**
     * Get a list of all the Categories in the database associated with the
     * given Plan, to be used for populating a ListView object.
     *
     * @param planId The id of the Plan the Categories should belong to.
     * @return A list of the names of all the Plans in the database
     * @see dao.CategoryDao#findAllByPlanId(int)
     */
    public ObservableList<String> getAllCategories(int planId) {
        ObservableList<String> items = FXCollections.observableArrayList();
        try {
            ArrayList<Category> categoryList = cDao.findAllByPlanId(planId);
            if (!categoryList.isEmpty()) {
                categoryList.stream().map(p -> p.getName()).forEach(n -> items.add(n));
            }
        } catch (SQLException ex) {
            /* Do nothing - an empty list will be returned at the end of the method */
        }

        return items;
    }

    /**
     * Validate the input and ask CategoryDao to create a new Category in the
     * database.
     *
     * @param name The desired name for the Category as inputted by the user.
     * Cannot be blank.
     * @param allocation The allocation of funds for the Category. Cannot be
     * blank, must be parse-able to Double.
     * @param p The Plan the Category is to be associated with.
     * @return True if the Category was created successfully. False if a
     * category with the same name within the Plan already exists or if an error
     * occurred.
     * @see dao.CategoryDao#save(java.lang.String, double, data.Plan)
     */
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

            try {
                return cDao.save(name, allocationAsDouble, p);
            } catch (SQLException ex) {
                return false;
            }
        }
    }

    /**
     * Ask CategoryDao to fetch a Category with the given name and Plan from the
     * database.
     *
     * @param name The name of the Category to be searched for as selected in
     * the UI.
     * @param p The Plan the Category should be associated with.
     * @return An existing Category with the name and Plan corresponding to the
     * given parameters. Null if none was found or an error occurred.
     * @see dao.CategoryDao#findOneByNameAndPlanId(java.lang.String, int)
     */
    public Category selectedCategory(String name, Plan p) {
        try {
            if (!getAllCategories(p.getId()).isEmpty()) {
                Category c = cDao.findOneByNameAndPlanId(name, p.getId());
                return c;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            return null;
        }
    }

    /**
     * Ask CategoryDao to find a Category with the given name and Plan from the
     * database, then delete the Category. Also deletes all the Expenses
     * associated with the Category.
     *
     * @param name The name of the Category to be deleted as selected in the UI.
     * @param p The Plan the Category is associated with.
     * @return True if the Category was successfully deleted. False if the
     * Category deos not exist or if an error occurred.
     * @see dao.CategoryDao#delete(java.lang.Integer)
     * @see dao.ExpenseDao#deleteAllByCategoryId(int)
     */
    public boolean deleteCategory(String name, Plan p) {
        try {
            Category c = cDao.findOneByNameAndPlanId(name, p.getId());
            if (c != null) {
                eDao.deleteAllByCategoryId(c.getId());
                cDao.delete(c.getId());
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
    }

    // 3 - EXPENSES
    /**
     * Get a list of all the Expenses in the database associated with the given
     * Category, to be used for populating a ListView object.
     *
     * @param categoryId The id of the Category the Expenses should belong to.
     * @return A list of the names and amounts of all the Expenses belonging to
     * the given Category in the database.
     * @see dao.ExpenseDao#findAllByCategory(int)
     */
    public ObservableList<String> getAllExpenses(int categoryId) {
        ObservableList<String> items = FXCollections.observableArrayList();
        try {
            ArrayList<Expense> expenseList = eDao.findAllByCategory(categoryId);
            if (!expenseList.isEmpty()) {
                expenseList.stream().forEach(e -> items.add(e.getName() + "\t" + e.getAmount()));
            }
        } catch (SQLException ex) {
            /* Do nothing - an empty list will be returned at the end of the method */
        }

        return items;
    }

    /**
     * Validate the input and ask ExpenseDao to create a new Expense in the
     * database.
     *
     * @param name The desired name for the Expense as inputted by the user.
     * Cannot be blank.
     * @param amount The amount of the Expense as inputted by the user. Cannot
     * be blank, must be parse-able to Double.
     * @param c The Category the Expense should be associated with.
     * @return True if the Expense was created successfully. False if an Expense
     * with the same name already exists within the Category or if an error
     * occurred.
     * @see dao.ExpenseDao#save(java.lang.String, double, data.Category)
     */
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

            try {
                eDao.save(name, amountAsDouble, c);
                return true;
            } catch (SQLException ex) {
                return false;
            }
        }
    }

    /**
     * Ask ExpenseDao to find an Expense with the given name and Category from
     * the database, then delete the Expense.
     *
     * @param name The name of the Expense to be deleted as selected in the UI.
     * @param c The Category the Expense belongs to.
     * @return True if the Expense was successfully deleted. False if the
     * Expense does not exist or if an error occurred.
     * @see dao.ExpenseDao#delete(int)
     */
    public boolean deleteExpense(String name, Category c) {
        try {
            if (!getAllExpenses(c.getId()).isEmpty()) {
                String[] split = name.split("\t");
                Expense e = eDao.findOneByNameAndCategoryId(split[0], c.getId());
                if (e != null) {
                    eDao.delete(e.getId());
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
    }

    /**
     * Request the list of all the Expenses associated with the give Category,
     * then sum the amounts the Expenses.
     *
     * @param c The Category to be examined as selected in the UI.
     * @return The total amount of funds actually used by all the Expenses
     * within the Category.
     * @see dao.ExpenseDao#findAllByCategory(int)
     */
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
