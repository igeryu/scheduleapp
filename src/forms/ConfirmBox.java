//  ConfirmBox.java

/**
 * Changelog:
 * 2016-02-25 : Created file
 */

package forms;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
        
        layout.getChildren().addAll(new Label(message), yesButton, noButton);
        window.setScene(scene);
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.showAndWait();
        
        return answer;
    }
    
}
