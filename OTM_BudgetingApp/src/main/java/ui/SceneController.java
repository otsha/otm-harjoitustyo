package ui;

import dao.PlanDao;
import data.Database;
import data.Plan;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class SceneController {

    private double width;
    private double height;
    private Stage stage;
    private Database db;
    private Insets insets;

    public SceneController(Stage s, Database db) {
        this.width = 640;
        this.height = 480;
        this.stage = s;
        this.db = db;
        this.insets = new Insets(20, 20, 20, 20);
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
    public void initialScene() throws SQLException {
        PlanDao pDao = new PlanDao(db);

        BorderPane view = setup();
        Scene scene = new Scene(view);

        // Setup the plan opening view
        VBox openingMenu = new VBox();

        // List all the existing plans in the database
        ListView<String> planListView = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();
        ArrayList<Plan> planList = pDao.findAll();
        if (!planList.isEmpty()) {
            planList.stream().map(p -> p.getName()).forEach(n -> items.add(n));
        }
        planListView.setItems(items);
        planListView.setPrefHeight(height / 2);

        // Add buttons for opening or deleting the selected plan
        HBox openOrDelete = new HBox();

        // Opening the selected plan
        Button openPlan = new Button("Open...");
        openPlan.setOnAction((event) -> {
            try {
                if (!items.isEmpty()) {
                    Plan p = pDao.findOneByName(planListView.getSelectionModel().selectedItemProperty().getValue());
                    editPlan(p);
                }
            } catch (SQLException ex) {
                System.out.println("error");
            }
        });

        // Deleting the selected plan
        Button deletePlan = new Button("Delete");
        deletePlan.setOnAction((event) -> {
            try {
                Plan p = pDao.findOneByName(planListView.getSelectionModel().selectedItemProperty().getValue());
                pDao.delete(p.getId());
                // Refresh the scene after deleting an item to show the updated listing
                initialScene();
            } catch (SQLException ex) {
                System.out.println("error");
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
        PlanDao pDao = new PlanDao(db);

        BorderPane view = setup();
        Scene scene = new Scene(view);

        // Add a back button
        Button backToMainMenu = new Button("<");
        backToMainMenu.setOnAction((event) -> {
            try {
                initialScene();
            } catch (SQLException ex) {
                System.out.println("error");
            }
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
            if (planName.getText().equals("") || budgetAmount.getText().equals("")) {
                inputErrorLabel.setText("The name and/or the budget amount cannot be empty.");
            } else {
                Plan p = new Plan(0, planName.getText(), Double.parseDouble(budgetAmount.getText()));

                try {
                    pDao.saveOrUpdate(p);
                } catch (SQLException ex) {

                }

                editPlan(p);
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
            try {
                initialScene();
            } catch (SQLException ex) {
                System.out.println("error");
            }
        });
        view.setTop(backToMainMenu);

        // Add content to second scene
        Label l = new Label(p.getName() + ", Budjetti: " + p.getBudget());
        view.setCenter(l);

        stage.setScene(scene);
    }

}
