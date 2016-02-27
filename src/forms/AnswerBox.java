//  AnswerBox.java

/**
 * Changelog:
 * 2016-02-25 : Created class, added display(String, String, String) and display(String, String)
 */

package forms;


import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author alanjohnson
 */
public class AnswerBox {
    
    protected Stage window;
    protected Scene scene;
    protected VBox layout;
    private TextField answerField;
    private ToggleGroup toggleGroup;
    private String answer;
    
    public String display (String title, String message, String defaultAnswer) {
        init(title, message);
        
        window.setMinWidth(250);
        window.setMinHeight(160);
        window.setResizable(false);
        
        // ============================  Body  ===========================
        answerField = new TextField(defaultAnswer);
        String answer = null;
        
        layout.getChildren().addAll(answerField);
        window.setOnCloseRequest(e -> {
            answerField.setText(null);
            window.close();
        });
        
        // ==========================  Buttons  ==========================
        Button confirmButton = new Button("OK");
        confirmButton.setOnAction(e -> window.close());
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            answerField.setText(null);
            window.close();
        });
        
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(confirmButton, cancelButton);
        layout.getChildren().add(buttonBox);
        
        window.setScene(scene);
        window.initModality(Modality.APPLICATION_MODAL);
        window.showAndWait();
        
        return answerField.getText();
    }

    private void init(String title, String message) {
        window = new Stage();
        window.setTitle(title);
        
        layout = new VBox(20);
        layout.setPadding(new Insets(0, 10, 10, 10));
        scene = new Scene(layout, 200, 100);
        
        // ============================  Body  ===========================
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        layout.getChildren().addAll(messageLabel);
        
    }
    
    
    /**
     * <p>Displays a question with list of options (radio boxes).
     * 
     * @param title
     * @param message
     * @param answers
     * @return 
     */
    public String display (String title, String message, ObservableList<String> answers) {
        init(title, message);
        
        // ============================  Body  ===========================
        toggleGroup = new ToggleGroup();
        VBox answersBox = new VBox(10);
        layout.getChildren().add(answersBox);
        int rows = 0;
        
        for (String item : answers) {
            RadioButton newButton = new RadioButton(item);
            newButton.setToggleGroup(toggleGroup);
//            toggleGroup.getToggles().add(newButton);
            answersBox.getChildren().add(newButton);
            
            rows++;
        }
        
        int height = 60 + (40 * rows);
        double width = Math.sqrt((1+height)*height);
        
        window.setMinHeight(height);
        window.setMinWidth(width);
        window.setResizable(false);
        
        window.setOnCloseRequest(e -> {
            answer = null;
            window.close();
        });
        
        // ==========================  Buttons  ==========================
        Button confirmButton = new Button("OK");
        confirmButton.setOnAction(e -> {
            answer = ((RadioButton)toggleGroup.getSelectedToggle()).getText();
            window.close();
                });
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            answer = null;
            window.close();
        });
        
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(confirmButton, cancelButton);
        layout.getChildren().add(buttonBox);
        
        window.setScene(scene);
        window.initModality(Modality.APPLICATION_MODAL);
        window.showAndWait();
        
        return answer;
    }
    
    public String display(String title, String message) {
        return display(title, message, "");
    }
    
}
