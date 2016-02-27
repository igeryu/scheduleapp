//  AlertBox.java

/**
 * Changelog:
 * 2016-02-25 : Created file
 */

package forms;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author alanjohnson
 */
public class AlertBox {
    
    public static void display (String title, String message) {
        Stage window = new Stage();
        window.setTitle(title);
        StackPane layout = new StackPane();
        Scene scene = new Scene(layout, 200, 100);
        layout.getChildren().add(new Label(message));
        window.setScene(scene);
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.show();
    }
    
}
