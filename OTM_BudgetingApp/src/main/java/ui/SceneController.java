package ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SceneController {
    
    private double width;
    private double height;
    private Stage stage;
    
    public SceneController(Stage s) {
        this.width = 640;
        this.height = 480;
        this.stage = s;
    }
    
    public void initialScene() {
        // Setup initial scene
        BorderPane initialView = new BorderPane();
        initialView.setPrefWidth(width);
        initialView.setPrefHeight(height);
        initialView.setPadding(new Insets(20, 20, 20, 20));
        Scene initialScene = new Scene(initialView);
        
        // Add content to initial scene
        VBox formCreatePlan = new VBox();
        
        Label planNameLabel = new Label("Name your plan:");
        TextField planName = new TextField();
        Label budgetAmountLabel = new Label("What's your budget?");
        TextField budgetAmount = new TextField();
        Button createNewPlan = new Button("Create");

        createNewPlan.setOnAction((event) -> {
            editPlan();
        });
        
        formCreatePlan.getChildren().addAll(planNameLabel, planName, budgetAmountLabel, budgetAmount, createNewPlan);
        initialView.setCenter(formCreatePlan);
        
        // initialScene.getStylesheets().add("/styles/Styles.css");
        stage.setScene(initialScene);
    }
    
    public void editPlan() {
        // Setup second scene
        BorderPane secondView = new BorderPane();
        secondView.setPrefWidth(width);
        secondView.setPrefHeight(height);
        Scene secondScene = new Scene(secondView);

        // Add content to second scene
        Label l = new Label("Hello world!");
        secondView.setCenter(l);
        
        stage.setScene(secondScene);
    }
    
}
