package otsha.otm_budgetingapp;

import dao.PlanDao;
import data.Database;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import ui.SceneController;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Database db = new Database("jdbc:sqlite:database.db");
        SceneController sc = new SceneController(stage, db);
        
        PlanDao pDao = new PlanDao(db);
        pDao.findAll().forEach(p -> System.out.println(p.getName() + ", " + p.getAmount()));
        
        sc.initialScene();
        stage.setTitle("BudgetingApp");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}