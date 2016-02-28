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
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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
    private Label messageLabel;
    private TextField answerField;
    private ToggleGroup toggleGroup;
    private String answer;
    
    enum Orientation { LANDSCAPE, PORTRAIT };
    
    public String display (String title, String message, String defaultAnswer) {
        init(title, message);
        double width = 250;
        double height = 160;
        
        window.setMinWidth(width);
        window.setMinHeight(height);
        layout.setPadding(new Insets(0, 10, 10, 10));
        
        // ============================  Body  ===========================
        answerField = new TextField(defaultAnswer);
//        answerField.setMaxWidth(100);
//        answerField.setM
        String answer = null;
        
        layout.getChildren().addAll(answerField);
        
        //      =====================  Buttons  =====================
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
        
        // ==========================  Sizing  ===========================
        //  Get width based on Orientation parameter and the Golden Ratio:
        height += 20;
        width = height * 1.618;
        window.setMinHeight(height);
        window.setMinWidth(width);
//        window.setResizable(false);
        
        //  The following makes messageLabel grow to its preferred size, and
        //  then increases the window's height by the amount messageLabel itself
        //  increased:
        messageLabel.setMinHeight(Control.USE_PREF_SIZE);
        
        // =========================  Finalize  ==========================
        window.setOnCloseRequest(e -> {
            answerField.setText(null);
            window.close();
        });
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
        messageLabel = new Label(message);
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
    public String display (String title, String message, ObservableList<String> answers, Orientation orientation) {
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
        
        int height = 80 + (40 * rows);
        double width;
        //  Get width based on Orientation parameter and the Golden Ratio:
        if (orientation == Orientation.LANDSCAPE) {
            width = height * 1.618;
        } else {
            width = height / 1.618;
        }
        
        window.setMinHeight(height);
        window.setMinWidth(width);
//        window.setResizable(false);
        
        //  DEBUG:
        System.out.printf("\n[AnswerBox.display()]\nheight: %s\nwidth: %s\n", height, width);
        
        //  The following makes messageLabel grow to its preferred size, and
        //  then increases the window's height by the amount messageLabel itself
        //  increased:
        messageLabel.setMinHeight(Control.USE_PREF_SIZE);
        
        //  DEBUG:
//        System.out.printf("\n[AnswerBox.display()]\nheight difference: %s\n", messageHeight);
        
        
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
