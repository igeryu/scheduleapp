//  ConfirmBox.java

/**
 * Changelog:
 * 2016-02-25 : Created file
 * 
 * 2016-02-29 : Corrected formatting/layout for better presentation
 */

package window.modal;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author alanjohnson
 */
public class ConfirmBox {
    
    static boolean answer;
    
    public static boolean display (String title, String message) {
        Stage window = new Stage();
        window.setTitle(title);
        Pane layout = new VBox(20);
        Scene scene = new Scene(layout, 200, 120);
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        
        // =========================  Buttons  ===========================
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
                });
        
        Button noButton = new Button("No");
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
                });
        
        buttonBox.getChildren().addAll(yesButton, noButton);
        
        // =========================  Finalize  ==========================
        layout.getChildren().addAll(messageLabel, buttonBox);
        window.setScene(scene);
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.showAndWait();
        
        return answer;
    }
    
}
