package otsha.otm_budgetingapp;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import ui.SceneController;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        SceneController sc = new SceneController(stage);
        
        sc.initialScene();
        stage.setTitle("BudgetingApp");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}