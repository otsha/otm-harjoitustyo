package ui;

import data.Category;
import data.Plan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.PlanHandler;

public class SceneController {

    private double width;
    private double height;
    private Stage stage;
    private Insets insets;
    private PlanHandler planHandler;

    public SceneController(Stage s, PlanHandler ph) {
        this.width = 640;
        this.height = 480;
        this.stage = s;
        this.insets = new Insets(20, 20, 20, 20);
        this.planHandler = ph;
    }

    // Setup an empty default view
    public BorderPane setup() {
        BorderPane view = new BorderPane();
        view.setPrefWidth(width);
        view.setPrefHeight(height);
        view.setPadding(insets);

        return view;
    }

    // THIS IS SHOWN WHEN THE APP FIRST STARTS
    // Managing (Creating, opening and deleting) budget plans
    public void initialScene() {
        BorderPane view = setup();
        Scene scene = new Scene(view);

        // Setup the plan opening view
        VBox openingMenu = new VBox();

        // List all the existing plans in the database
        ListView<String> planListView = new ListView<>();
        planListView.setItems(planHandler.getAllPlans());
        planListView.setPrefHeight(height / 2);

        // Add buttons for opening or deleting the selected plan
        HBox openOrDelete = new HBox();

        // Opening the selected plan
        Button openPlan = new Button("Open...");
        openPlan.setOnAction((event) -> {
            if (!planHandler.getAllPlans().isEmpty()) {
                Plan p = planHandler.openPlan(planListView.getSelectionModel().selectedItemProperty().getValue());
                if (p != null) {
                    editPlan(p);
                }
            }
        });

        // Deleting the selected plan
        Button deletePlan = new Button("Delete");

        deletePlan.setOnAction((event) -> {
            if (!planHandler.getAllPlans().isEmpty()) {
                if (planHandler.deletePlan(planListView.getSelectionModel().selectedItemProperty().getValue()) == true) {
                    initialScene();
                } else {
                    // <To-do: Display an error message here>
                }
            }
        });

        // Creating a new plan
        Button createPlan = new Button("New...");
        createPlan.setOnAction((event) -> {
            createPlan();
        });

        openOrDelete.getChildren().addAll(createPlan, openPlan, deletePlan);
        openingMenu.getChildren().addAll(planListView, openOrDelete);

        view.setCenter(openingMenu);

        stage.setScene(scene);
    }

    // CREATE A NEW BUDGET PLAN
    public void createPlan() {

        BorderPane view = setup();
        Scene scene = new Scene(view);

        // Add a back button
        Button backToMainMenu = new Button("<");
        backToMainMenu.setOnAction((event) -> {
            initialScene();
        });
        view.setTop(backToMainMenu);

        // Setup the budget plan creation form
        VBox formCreatePlan = new VBox();

        Label inputErrorLabel = new Label("");
        Label planNameLabel = new Label("Name your plan:");
        TextField planName = new TextField();
        Label budgetAmountLabel = new Label("What's your budget?");
        TextField budgetAmount = new TextField();
        Button createNewPlan = new Button("Create");

        // Handle creating a new plan
        createNewPlan.setOnAction((event) -> {
            Plan p = planHandler.createPlan(planName.getText(), budgetAmount.getText());
            if (p != null) {
                editPlan(p);
            } else {
                inputErrorLabel.setText("Plan could not be created. Either one of the fields is invalid or a plan with this name already exists.");
            }
        });

        formCreatePlan.getChildren().addAll(inputErrorLabel, planNameLabel, planName, budgetAmountLabel, budgetAmount, createNewPlan);
        view.setCenter(formCreatePlan);

        stage.setScene(scene);
    }

    // VIEW THE DETAILS OF A PLAN
    public void editPlan(Plan p) {

        BorderPane view = setup();
        Scene scene = new Scene(view);

        // Create the navigation menu
        HBox nav = new HBox();

        Button backToMainMenu = new Button("<");
        backToMainMenu.setOnAction((event) -> {
            initialScene();
        });

        Button viewChartsButton = new Button("View charts");
        viewChartsButton.setOnAction((event) -> {
            viewCharts(p);
        });

        nav.getChildren().addAll(backToMainMenu, viewChartsButton);
        view.setTop(nav);

        // Add labels for plan name, total budget, how much has been allocated & how much is remaining
        Label budget = new Label(p.getName() + ", Budget: " + p.getBudget());
        Label allocated = new Label("Allocated: " + planHandler.getAllocated(p));
        Label used = new Label("Used: " + planHandler.getUsed(p));
        Label categoryListLabel = new Label("Categories:");

        // Create and populate the category listing
        ListView<String> categoryListView = new ListView<>();
        categoryListView.setItems(planHandler.getAllCategories(p.getId()));
        categoryListView.setPrefHeight(height / 2);

        // Add a button for deleting categories
        Button deleteCategoryButton = new Button("Delete");
        deleteCategoryButton.setOnAction((event) -> {
            if (!planHandler.getAllCategories(p.getId()).isEmpty()) {
                if (planHandler.deleteCategory(categoryListView.getSelectionModel().selectedItemProperty().getValue(), p)) {
                    editPlan(p);
                }
            }
        });

        // Add a form for creating categories
        VBox createCategoryForm = new VBox();

        Label categoryNameLabel = new Label("Category name:");
        TextField categoryName = new TextField();
        Label categoryAllocationLabel = new Label("Category allocation:");
        TextField categoryAllocation = new TextField();

        Button createCategoryButton = new Button("Create");
        createCategoryButton.setOnAction((event) -> {
            if (planHandler.createCategory(categoryName.getText(), categoryAllocation.getText(), p)) {
                categoryListView.setItems(planHandler.getAllCategories(p.getId()));
                allocated.setText("Allocated: " + planHandler.getAllocated(p));
            }
        });

        createCategoryForm.getChildren().addAll(
                categoryNameLabel,
                categoryName,
                categoryAllocationLabel,
                categoryAllocation,
                createCategoryButton
        );

        VBox categories = new VBox();
        categories.getChildren().addAll(
                budget,
                allocated,
                used,
                categoryListLabel,
                categoryListView,
                deleteCategoryButton,
                createCategoryForm
        );
        view.setLeft(categories);

        // Viewing the details of a selected category
        VBox selectedCategory = new VBox();
        selectedCategory.setPadding(insets);

        Label selectedCategoryLabel = new Label();
        Label selectedCategoryAllocated = new Label();
        Label selectedCategoryUsed = new Label();
        Label expenseListLabel = new Label("Expenses in this category:");

        ListView<String> expenseListView = new ListView<>();
        expenseListView.setPrefHeight(height / 2);

        // Create a form for managing expenses
        VBox manageExpensesForm = new VBox();

        Button deleteExpenseButton = new Button("Delete");
        deleteExpenseButton.setOnAction((event) -> {
            String selectedCategoryName = categoryListView.getSelectionModel().selectedItemProperty().getValue();
            if (planHandler.selectedCategory(selectedCategoryName, p) != null) {
                String expenseName = expenseListView.getSelectionModel().selectedItemProperty().getValue();
                if (expenseName != null) {
                    if (planHandler.deleteExpense(expenseName, planHandler.selectedCategory(selectedCategoryName, p))) {
                        expenseListView.setItems(planHandler.getAllExpenses(planHandler.selectedCategory(selectedCategoryName, p).getId()));
                        used.setText("Used: " + planHandler.getUsed(p));
                        selectedCategoryUsed.setText("Used: " + planHandler.usedByCategory(planHandler.selectedCategory(selectedCategoryName, p)));
                    }
                }
            }
        });

        Label expenseNameLabel = new Label("Expense description:");
        TextField expenseName = new TextField();
        Label expenseAmountLabel = new Label("Amount:");
        TextField expenseAmount = new TextField();

        Button createExpenseButton = new Button("Add");
        createExpenseButton.setOnAction((event) -> {
            String selectedCategoryName = categoryListView.getSelectionModel().selectedItemProperty().getValue();
            if (planHandler.selectedCategory(selectedCategoryName, p) != null) {
                if (planHandler.createExpense(expenseName.getText(), expenseAmount.getText(), planHandler.selectedCategory(selectedCategoryName, p))) {
                    expenseListView.setItems(planHandler.getAllExpenses(planHandler.selectedCategory(selectedCategoryName, p).getId()));
                    used.setText("Used: " + planHandler.getUsed(p));
                    selectedCategoryUsed.setText("Used: " + planHandler.usedByCategory(planHandler.selectedCategory(selectedCategoryName, p)));
                }
            }
        });

        manageExpensesForm.getChildren().addAll(
                deleteExpenseButton,
                expenseNameLabel,
                expenseName,
                expenseAmountLabel,
                expenseAmount,
                createExpenseButton
        );

        selectedCategory.getChildren().addAll(
                selectedCategoryLabel,
                selectedCategoryAllocated,
                selectedCategoryUsed,
                expenseListLabel,
                expenseListView,
                manageExpensesForm
        );
        view.setCenter(selectedCategory);

        // Update the view based on the selected category
        categoryListView.getSelectionModel().selectedItemProperty().addListener((event) -> {
            String selectedCategoryName = categoryListView.getSelectionModel().selectedItemProperty().getValue();
            if (planHandler.selectedCategory(selectedCategoryName, p) != null) {
                expenseListView.setItems(planHandler.getAllExpenses(planHandler.selectedCategory(selectedCategoryName, p).getId()));
                selectedCategoryLabel.setText(planHandler.selectedCategory(selectedCategoryName, p).getName());
                selectedCategoryAllocated.setText("Allocated: " + planHandler.selectedCategory(selectedCategoryName, p).getAllocated());
                selectedCategoryUsed.setText("Used: " + planHandler.usedByCategory(planHandler.selectedCategory(selectedCategoryName, p)));
            } else {
                selectedCategoryLabel.setText("");
                selectedCategoryAllocated.setText("");
                selectedCategoryUsed.setText("");
            }
        });

        stage.setScene(scene);
    }

    // View visual data related to the plan
    public void viewCharts(Plan p) {

        BorderPane view = setup();
        Scene scene = new Scene(view);

        // Create the navigation menu
        HBox nav = new HBox();

        Button backToMainMenu = new Button("<");
        backToMainMenu.setOnAction((event) -> {
            initialScene();
        });

        Button backToEditing = new Button("Edit plan...");
        backToEditing.setOnAction((event) -> {
            editPlan(p);
        });

        // Create the data view for viewing all allocations
        VBox dataViewAllocations = new VBox();
        Label totalBudgetLabelAllocationView = new Label("Total budget: " + p.getBudget());
        Label unAllocatedLabel = new Label("Unallocated: " + (p.getBudget() - planHandler.getAllocated(p)));

        // Create a pie chart displaying fund allocation
        ObservableList<PieChart.Data> pieChartAllocationData = FXCollections.observableArrayList();
        ObservableList<String> categoryNames = planHandler.getAllCategories(p.getId());
        categoryNames.stream().forEach(cName -> {
            pieChartAllocationData.add(new PieChart.Data(cName, planHandler.selectedCategory(cName, p).getAllocated()));
        });

        PieChart allocationChart = new PieChart(pieChartAllocationData);
        allocationChart.setTitle("Fund allocation by category");
        dataViewAllocations.getChildren().addAll(
                totalBudgetLabelAllocationView,
                unAllocatedLabel,
                allocationChart
        );

        // Create the data view for viewing fund usage
        VBox dataViewUsages = new VBox();
        Label totalBudgetLabelUsageView = new Label("Total budget: " + p.getBudget());
        Label totalUsedLabel = new Label("Used: " + planHandler.getUsed(p));

        // Create a pie chart displaying fund usage
        ObservableList<PieChart.Data> pieChartUsageData = FXCollections.observableArrayList();
        categoryNames.stream().forEach(cName -> {
            pieChartUsageData.add(new PieChart.Data(cName, planHandler.getUsedByCategory(planHandler.selectedCategory(cName, p))));
        });

        PieChart usageChart = new PieChart(pieChartUsageData);
        usageChart.setTitle("Fund usage by category");
        dataViewUsages.getChildren().addAll(
                totalBudgetLabelUsageView,
                totalUsedLabel,
                usageChart
        );

        // View the allocation chart as default
        view.setCenter(dataViewAllocations);

        // Create a button for switching to the usage chart
        Button viewFundUsageButton = new Button("Fund usage");
        viewFundUsageButton.setOnAction((event) -> {
            view.setCenter(dataViewUsages);
        });

        // Create a button for switching to the allocation chart
        Button viewFundAllocationButton = new Button("Fund allocation");
        viewFundAllocationButton.setOnAction((event) -> {
            view.setCenter(dataViewAllocations);
        });

        // Populate the navigation menu
        nav.getChildren().addAll(
                backToMainMenu,
                backToEditing,
                viewFundAllocationButton,
                viewFundUsageButton
        );
        view.setTop(nav);

        stage.setScene(scene);
    }

}
