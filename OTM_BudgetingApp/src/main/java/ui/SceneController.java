package ui;

import data.Plan;
import javafx.geometry.Insets;
import javafx.scene.Scene;
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
            Plan p = planHandler.openPlan(planListView);
            if (p != null) {
                editPlan(p);
            }
        });

        // Deleting the selected plan
        Button deletePlan = new Button("Delete");

        deletePlan.setOnAction((event) -> {
            if (planHandler.deletePlan(planListView) == true) {
                initialScene();
            } else {
                // <To-do: Display an error message here>
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
                inputErrorLabel.setText("Plan could not be created. Either one of the fields is invalid or an error occurred.");
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

        // Add a back button
        Button backToMainMenu = new Button("<");
        backToMainMenu.setOnAction((event) -> {
            initialScene();
        });
        view.setTop(backToMainMenu);

        // Add labels for plan name, total budget, used & remaining
        Label budget = new Label(p.getName() + ", Budget: " + p.getBudget());
        Label used = new Label("Used: " + planHandler.getUsed(p));
        Label remaining = new Label("Remaining: " + (p.getBudget() - planHandler.getUsed(p)));
        Label categoryListLabel = new Label("Categories:");

        // Create and populate the category listing
        ListView<String> categoryListView = new ListView<>();
        categoryListView.setItems(planHandler.getAllCategories(p.getId()));
        categoryListView.setPrefHeight(height / 2);

        // Add a button for deleting categories
        Button deleteCategoryButton = new Button("Delete");
        deleteCategoryButton.setOnAction((event) -> {
            if (planHandler.deleteCategory(categoryListView, p)) {
                // Refresh the scene to update the category listing
                editPlan(p);
            }
        });

        // Add a form for creating categories
        VBox createCategoryForm = new VBox();

        Label categoryNameLabel = new Label("Category name:");
        TextField categoryName = new TextField();
        Label categoryAllocationLabel = new Label("Category allocation:");
        TextField categoryAllocation = new TextField();

        // Add a button for creating a category
        Button createCategoryButton = new Button("Create");
        createCategoryButton.setOnAction((event) -> {
            if (planHandler.createCategory(categoryName.getText(), categoryAllocation.getText(), p)) {
                // Refresh the scene to update the category listing
                editPlan(p);
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
                used,
                remaining,
                categoryListLabel,
                categoryListView,
                deleteCategoryButton,
                createCategoryForm
        );
        view.setLeft(categories);

        // Viewing the details of a selected category
        VBox selectedCategory = new VBox();

        Label selectedCategoryNameLabel = new Label();
        Label selectedCategoryAllocationLabel = new Label();

        selectedCategory.getChildren().addAll(
                selectedCategoryNameLabel,
                selectedCategoryAllocationLabel
        );
        view.setRight(selectedCategory);

        categoryListView.getSelectionModel().selectedItemProperty().addListener((event) -> {
            selectedCategoryNameLabel.setText(planHandler.editCategory(categoryListView, p).getName());
            selectedCategoryAllocationLabel.setText("Allocation: " + planHandler.editCategory(categoryListView, p).getAllocated());
        });

        stage.setScene(scene);
    }

}
