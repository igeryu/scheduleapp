package forms;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author alanjohnson
 */
public class AddPersonStage {
    
    public static void display () {
        Stage window = new Stage();
        window.setTitle("Add Person");
        Pane rootLayout = new VBox(20);
        Scene scene = new Scene(rootLayout, 600, 750);
        window.setScene(scene);
        
        // ===========================  Title Label  ==========================
        Pane titlePane = new StackPane();
        titlePane.getChildren().add(new Label("Add Person"));
        rootLayout.getChildren().add(titlePane);
        
        // ===========================  Input Area  ===========================
        GridPane inputPane = new GridPane();
        inputPane.setHgap(60);
        inputPane.setVgap(10);
        inputPane.setPadding(new Insets(0, 20, 0, 20));
        rootLayout.getChildren().add(inputPane);
        
        //      ========================  Labels  ========================
        Label workcenterLabel = new Label("Workcenter");
        workcenterLabel.setAlignment(Pos.CENTER);
        GridPane.setConstraints(workcenterLabel, 0, 0);
        
        Label shiftLabel = new Label("Shift");
        shiftLabel.setAlignment(Pos.CENTER);
        GridPane.setConstraints(shiftLabel, 1, 0);
        
        Label rankLabel = new Label("Rank");
        rankLabel.setAlignment(Pos.CENTER);
        GridPane.setConstraints(rankLabel, 2, 0);
        
        Label startDateLabel = new Label("Start Date");
        startDateLabel.setAlignment(Pos.CENTER);
        GridPane.setConstraints(workcenterLabel, 3, 0);
        
        inputPane.getChildren().addAll(workcenterLabel, shiftLabel,
                                        rankLabel, startDateLabel);
        
        //      =======================  Buttons  ========================
        ChoiceBox workcenterBox = new ChoiceBox();
        GridPane.setConstraints(workcenterBox, 0, 1);
        ChoiceBox shiftBox = new ChoiceBox();
        GridPane.setConstraints(shiftBox, 1, 1);
        ChoiceBox rankBox = new ChoiceBox();
        GridPane.setConstraints(rankBox, 2, 1);
        DatePicker startDateBox = new DatePicker();
        GridPane.setConstraints(startDateBox, 3, 1);
        
        inputPane.getChildren().addAll(workcenterBox, shiftBox,
                                          rankBox, startDateBox);
        
        
        // =============================  Finish  =============================
        
        
        // =============================  Finish  =============================
        window.initModality(Modality.APPLICATION_MODAL);
        window.showAndWait();
    
    }
    
}
