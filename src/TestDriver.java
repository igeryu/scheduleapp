
import domain.Person;
import domain.PersonDAO;
import forms.AddPersonFrame;
import forms.AddPersonStage;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.DBConnectionPool;

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
    
    public static void main (String[] args) {
        
        
//        AddPersonFrame frame = new AddPersonFrame();
//        frame.setVisible(true);
        
        launch(args);
        
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage stage = new Stage();
        Pane layout = new VBox(30);
        Scene scene = new Scene(layout, 200, 200);
        stage.setScene(scene);
        
        Button addPersonButton = new Button("Test 'Add Person'");
        addPersonButton.setOnAction(e -> (new AddPersonStage()).display());
        layout.getChildren().add(addPersonButton);
        
        stage.show();
    }
    
}
