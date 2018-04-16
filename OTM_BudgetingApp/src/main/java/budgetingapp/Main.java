package budgetingapp;

import data.Database;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import logic.PlanHandler;
import ui.SceneController;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Database db = new Database("jdbc:sqlite:database.db");
        PlanHandler ph = new PlanHandler(db);
        SceneController sc = new SceneController(stage, ph);

        sc.initialScene();
        stage.setTitle("BudgetingApp");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
