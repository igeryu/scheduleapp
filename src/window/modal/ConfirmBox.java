//  ConfirmBox.java

/**
 * Changelog:
 * 2016-02-25 : Created file
 * 
 * 2016-02-29 : Corrected formatting/layout for better presentation
 * 
 * 2016-03-01 : Added Javadoc to display()
 * 
 * 2016-03-24 : Formatted to match Google Java Style
 */

package window.modal;

import java.util.logging.Logger;

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
  static final private Logger logger = Logger.getLogger(ConfirmBox.class.getName());
  
  /**
    * <p>Displays a pop-up window to ask the user to confirm a request.</p>
    * 
    * @param title
    * @param message
    * @return 
    */
  public static boolean display (String title, String message) {
        
      // ==========================  Setup  ============================
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
