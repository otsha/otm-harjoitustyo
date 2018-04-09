package otsha.otm_budgetingapp;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane view = new BorderPane();
        
        Scene scene = new Scene(view);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("BudgetingApp");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
