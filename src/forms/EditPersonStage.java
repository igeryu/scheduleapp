// EditPersonStage.java

/**
 * Changelog:
 * 2016-02-25 : Created class using AddPersonStage as template
 * 2016-02-25 : Added confirmChangeItem(Item) and confirmChangeItem(Item, ObservableList<String>)
 * 
 * 2016-02-27 : Added functionality for changing: workcenter, shift, rank, last name and skill level
 * 2016-02-27 : Added deleteButton and implemented functionality
 */

package forms;

import domain.Person;
import domain.PersonDAO_New;
import domain.RankDAO;
import domain.ShiftDAO;
import domain.SkillDAO;
import domain.WorkcenterDAO;
import forms.AnswerBox.Orientation;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author alanjohnson
 */
public class EditPersonStage {
    
    // ===========================  Inputs  ==========================
    private static Button lastNameButton;
    private static Button firstNameButton;
    
    private static DatePicker startDateBox;
    private static Button skillButton;
    private static Button rankButton;
    private static Button shiftButton;
    private static Button workcenterButton;
    
    // ============================  Misc  ===========================
    private static Person person;
    private enum Shift { MIDS, DAYS, SWINGS};
    private enum Item {
        FIRST_NAME, LAST_NAME, RANK, SHIFT, SKILL, WORKCENTER;
        
        @Override
        public String toString() {
            switch (this) {
                case FIRST_NAME: return "first name";
                case LAST_NAME: return "last name";
                case RANK: return "rank";
                case SHIFT: return "shift";
                case SKILL: return "skill level";
                case WORKCENTER: return "workcenter";
                default: throw new IllegalArgumentException();
            }
        }
    };
    
    private static void setupButton(Button button, Item item, Orientation orientation) {
        button.setOnAction(e -> {
            ObservableList<String> list = null;
            
            switch(item) {
                case RANK:
                    list = (new RankDAO()).getList();
                    break;
                
                case SHIFT:
                    list = (new ShiftDAO()).getList();
                    break;
                
                case SKILL:
                    list = (new SkillDAO()).getList();
                    break;
                
                case WORKCENTER:
                    list = (new WorkcenterDAO()).getList();
                    break;
                
                default: throw new IllegalArgumentException();
            }
            
            String newValue = confirmChangeItem(item, list, orientation);
            
            if (newValue != null && !newValue.equals("")) {
                switch(item) {
                case RANK:
                    person.setRank(newValue);
                    break;
                
                case SHIFT:
                    person.setShift(newValue);
                    break;
                
                case SKILL:
                    person.setSkill(newValue);
                    break;
                
                case WORKCENTER:
                    person.setWorkcenter(newValue);
                    break;
                
                default:
            }
                
                
                button.setText(newValue);
            }
                });
    }
    
    private static String confirmChangeItem (Item item) {
        String title = String.format("Change %s %s %s's %s?",
                                     person.getRank(),
                                     person.getFirstName(),
                                     person.getLastName(),
                                     item);
        
        String message = String.format("Change %s %s %s's %s to?",
                                     person.getRank(),
                                     person.getFirstName(),
                                     person.getLastName(),
                                     item);
        
        String newValue = (new AnswerBox()).display(title, message);
        
        // DEBUG:
        System.out.printf("\n[EditPersonStage.confirmChangeItem()] answer: %s\n", newValue);
        
        return newValue;
    }
    
    private static String confirmChangeItem (Item item, ObservableList<String> options, Orientation orientation) {
        String title = String.format("Change %s %s %s's %s?",
                                     person.getRank(),
                                     person.getFirstName(),
                                     person.getLastName(),
                                     item);
        
        String message = String.format("Change %s %s %s's %s to?",
                                     person.getRank(),
                                     person.getFirstName(),
                                     person.getLastName(),
                                     item);
        
        String newValue = (new AnswerBox()).display(title, message, options, orientation);
        
        // DEBUG:
        System.out.printf("\n[EditPersonStage.confirmChangeItem()] answer: %s\n", newValue);
        
        return newValue;
    }
    
    public static void display () {
        //  DEBUG/TESTING:
        person = new Person("First_Name", "Last_Name", "SSgt", "5");
        
        Stage window = new Stage();
        //  TODO: [maybe] Change this line to have the person's name:
        window.setTitle("Edit Person");
        Pane rootLayout = new VBox(20);
        int width = 650;
        int height = 300;
        Scene scene = new Scene(rootLayout, width, height);
        window.setScene(scene);
        
        window.setMinWidth(width);
        window.setMinHeight(height);
        window.setMaxWidth(width);
        window.setMaxHeight(height);
        
        // ===========================  Title Label  ==========================
        Pane titlePane = new StackPane();
        rootLayout.getChildren().add(titlePane);
        
        // ===========================  Input Area  ===========================
        GridPane inputPane = new GridPane();
        inputPane.setAlignment(Pos.CENTER);
        inputPane.setHgap(60);
        inputPane.setVgap(10);
        inputPane.setPadding(new Insets(0, 20, 0, 20));
        rootLayout.getChildren().add(inputPane);
        
        //      ========================  Labels  ========================
        
        //           ==================  1st  Row  ==================
        Label workcenterLabel = new Label("Workcenter");
        GridPane.setConstraints(workcenterLabel, 0, 0);
        
        Label shiftLabel = new Label("Shift");
        GridPane.setConstraints(shiftLabel, 1, 0);
        
        Label rankLabel = new Label("Rank");
        GridPane.setConstraints(rankLabel, 2, 0);
        
        Label startDateLabel = new Label("Start Date");
        GridPane.setConstraints(startDateLabel, 3, 0);
        
        //           ==================  2nd  Row  ==================
        Label firstNameLabel = new Label("First Name");
        GridPane.setConstraints(firstNameLabel, 0, 2);
        
        Label lastNameLabel = new Label("Last Name");
        GridPane.setConstraints(lastNameLabel, 1, 2);
        
        Label skillLabel = new Label("Skill Lv");
        GridPane.setConstraints(skillLabel, 2, 2);
        
        inputPane.getChildren().addAll(workcenterLabel, shiftLabel,
                                        rankLabel, startDateLabel,
                                        firstNameLabel, lastNameLabel,
                                        skillLabel);
        
        //      ========================  Inputs  ========================
        
        //           ==================  1st  Row  ==================
        workcenterButton = new Button(person.getWorkcenter());
        workcenterButton.setMinWidth(100);
        setupButton(workcenterButton, Item.WORKCENTER, Orientation.PORTRAIT);
        GridPane.setConstraints(workcenterButton, 0, 1);
        
        shiftButton = new Button(person.getShift());
        shiftButton.setMinWidth(50);
        setupButton(shiftButton, Item.SHIFT, Orientation.PORTRAIT);
        GridPane.setConstraints(shiftButton, 1, 1);
        
        rankButton = new Button(person.getRank());
        rankButton.setMinWidth(20);
        setupButton(rankButton, Item.RANK, Orientation.PORTRAIT);
        GridPane.setConstraints(rankButton, 2, 1);
        
        startDateBox = new DatePicker();
        GridPane.setConstraints(startDateBox, 3, 1);
        
        //           ==================  2nd  Row  ==================
        firstNameButton = new Button(person.getFirstName());
        firstNameButton.setOnAction(e -> {
            String newValue = confirmChangeItem(Item.FIRST_NAME);
            
            if (newValue != null && !newValue.equals("")) {
                person.setFirstName(newValue);
                firstNameButton.setText(newValue);
            }
                });
        GridPane.setConstraints(firstNameButton, 0, 3);
        
        lastNameButton = new Button(person.getLastName());
        lastNameButton.setOnAction(e -> {
            String newValue = confirmChangeItem(Item.LAST_NAME);
            
            if (newValue != null && !newValue.equals("")) {
                person.setLastName(newValue);
                lastNameButton.setText(newValue);
            }
                });
        GridPane.setConstraints(lastNameButton, 1, 3);
        
        skillButton = new Button(person.getSkill());
        skillButton.setMinWidth(10);
        setupButton(skillButton, Item.SKILL, Orientation.PORTRAIT);
        GridPane.setConstraints(skillButton, 2, 3);
        
        //           ==================  3rd  Row  ==================
        
        //                ===========  Save  Button  ===========
        Button saveButton = new Button("Save");
        //  TODO:  Fix functionality for saveButton
        saveButton.setOnAction(e -> {
            String firstName = firstNameButton.getText();
            String lastName = lastNameButton.getText();
            
            //  TODO:  Acquire start date data and use it as part of input requirements

            PersonDAO_New personDao = new PersonDAO_New();
        });
        saveButton.setAlignment(Pos.CENTER);
        StackPane saveButtonPane = new StackPane();
        GridPane.setConstraints(saveButtonPane, 0, 4, 4, 1);
        saveButtonPane.setPadding(new Insets(20, 0, 0, 0));
        saveButtonPane.getChildren().add(saveButton);
        
        //                ==========  Delete  Button  ==========
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            String title = "Delete";
            
            String message = String.format("Delete %s %s %s?", person.getRank(),
                                                               person.getFirstName(),
                                                               person.getLastName());
            
            message += "\nPlease type 'delete' in the box to confirm, and "
                       + "click OK.";
            
            String answer = (new AnswerBox()).display(title, message);
            
            if (answer == null) {
                return;
            } else if(answer.equals("delete")) {
                (new PersonDAO_New()).delete(person);
                
                title = "Person deleted";
                message = String.format("%s %s %s was deleted.",
                                        person.getRank(),
                                        person.getFirstName(),
                                        person.getLastName());
                
                AlertBox.display(title, message);
                window.close();
            } else {
                title = "Deletion unsuccessful";
                message = String.format("%s %s %s was not deleted.",
                                        person.getRank(),
                                        person.getFirstName(),
                                        person.getLastName());
                
                AlertBox.display(title, message);
            }
        });
        deleteButton.setAlignment(Pos.CENTER);
        StackPane deleteButtonPane = new StackPane();
        GridPane.setConstraints(deleteButtonPane, 0, 5, 4, 1);
        deleteButtonPane.setPadding(new Insets(20, 0, 0, 0));
        deleteButtonPane.getChildren().add(deleteButton);
        
        inputPane.getChildren().addAll(workcenterButton, shiftButton,
                                       rankButton, startDateBox,
                                       firstNameButton, lastNameButton,
                                       skillButton, saveButtonPane,
                                       deleteButtonPane);
        
        // =============================  Finish  =============================
        window.initModality(Modality.APPLICATION_MODAL);
        window.showAndWait();
    
    }
    
  
    
}
