
import window.AddPersonStage;
import window.EditPersonStage;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.DBBuild;
import window.MainStage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author alanjohnson
 */
public class TestDriver extends Application {

  private static final Logger logger = Logger.getLogger(TestDriver.class.getName());

  public static void main(String[] args) {

    launch(args);

  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    //  DEBUG:
    logger.info("[TestDriver.start()] Testing database...");

    DBBuild.testDatabase();
    Stage stage = new Stage();
    stage.setTitle("Schedule Application");
    Pane layout = new VBox(30);
    Scene scene = new Scene(layout, 200, 200);
    stage.setScene(scene);

    Button addPersonButton = new Button("Test 'Add Person'");
    addPersonButton.setOnAction(e -> (new AddPersonStage()).display());
    layout.getChildren().add(addPersonButton);

    Button editPersonButton = new Button("Test 'Edit Person'");
    editPersonButton.setOnAction(e -> (new EditPersonStage()).display(null));
    layout.getChildren().add(editPersonButton);

    Button mainStageButton = new Button("Test Main Stage");
    mainStageButton.setOnAction(e -> (new MainStage()).display());
    layout.getChildren().add(mainStageButton);

    stage.show();
  }

}
