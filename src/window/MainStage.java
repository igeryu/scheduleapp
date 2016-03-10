// MainStage.java

/**
 * Changelog:
 * 
 * 2016-03-07 : Created file
 * 2016-03-07 : Added a tabbed pane with 'Schedule' and 'Manage' tabs
 * 
 * 2016-03-09 : Schedule Shift View works, shows each person (rows) with their current shift today and next six days.
 * 2016-03-09 : Made shift cells color-coded for easier viewing.
 */
package window;

import domain.Person;
import domain.PersonDAO;
import domain.ShiftDateDAO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author alanjohnson
 */
public class MainStage {
    
    // ===========================  Inputs  ==========================
    private static Stage window;
    
    // ===========================   Misc  ===========================
    private static TabPane tabPane;
    private static Tab manageTab;
    private static TableView scheduleTable;
    private static HBox filtersBox;
    private static HBox optionsBox;
    private static VBox scheduleWindow;
    private static Tab scheduleTab;
    
    /**
     * <p>Adapted from
     * <code>https://community.oracle.com/thread/2474328</code></p>>
     * @param columnIndex
     * @param columnTitle
     * @return 
     */
    private TableColumn<ObservableList<StringProperty>, String> createColumn(
            final int columnIndex, String columnTitle) {
        TableColumn<ObservableList<StringProperty>, String> column = new TableColumn<>();
        String title = columnTitle;

        column.setText(title);
        column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<StringProperty>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(
                    CellDataFeatures<ObservableList<StringProperty>, String> cellDataFeatures) {
                return cellDataFeatures.getValue().get(columnIndex);
            }
        });
        column.setCellFactory(thisColumn -> {
            return new TableCell<ObservableList<StringProperty>, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item + " Shift");

                        switch (item) {
                            case "Mid":
                                this.setId("mid_style");
                                break;

                            case "Day":
                                this.setId("day_style");
                                break;

                            case "Swing":
                                this.setId("swing_style");
                                break;
                        }
                    }

                    // Style all dates in March with a different color.
//                    if (item.getMonth() == Month.MARCH) {
//                        setTextFill(Color.CHOCOLATE);
//                        setStyle("-fx-background-color: yellow");
//                    } else {
//                        setTextFill(Color.BLACK);
//                        setStyle("");
//                    }
                    
                }
            };
        });
        return column;
    }

    
    /**
     * <p>Displays the main stage (window).</p>
     */
    public void display () {
        window = new Stage();
        window.setTitle("Schedule Application");
        StackPane rootLayout = new StackPane();
        int width = 650;
        int height = 300;
        Scene scene = new Scene(rootLayout, width, height);
        height = 0;
        scene.getStylesheets().add("css/mainStage.css");
        window.setScene(scene);
        
        window.setMinWidth(width);
        window.setMinHeight(height);
        // ===========================   Tab Area  ============================
        //      =====================  Schedule Tab  =====================
        fileTab = new Tab("File");
        fileTab.setClosable(false);
        fileTab.getStyleClass().add("file");
        fileWindow = new VBox();
        fileTab.setContent(fileWindow);
        
        //      =====================  Schedule Tab  =====================
        scheduleTab = new Tab("Schedule");
        scheduleTab.setClosable(false);
        scheduleTab.getStyleClass().add("options");
        scheduleWindow = new VBox();
        scheduleTab.setContent(scheduleWindow);
        
        //           ================  Options Row   ================
        optionsBox = new HBox(10);
        Button testButton = new Button("Get Shifts");
        testButton.setOnAction(e -> System.out.println("\n" + (new ShiftDateDAO()).getWeek(0, LocalDate.now())));
        optionsBox.getChildren().addAll(new Label("Options..."), testButton);
        optionsBox.getStyleClass().addAll("options");
        
        //           ================  Filters Row   ================
        filtersBox = new HBox(10);
        filtersBox.getChildren().add(new Label("Filters..."));
        filtersBox.getStyleClass().add("pane");
        
        //           ============  Main Schedule Window  ============
        scheduleTable = new TableView();
        int scheduleBoxWidth = 500;
        int scheduleBoxHeight = 500;
        scheduleTable.setMinSize(scheduleBoxWidth, scheduleBoxHeight);
        scheduleTable.getStyleClass().add("pane");
        scheduleTable.getSelectionModel().setCellSelectionEnabled(true);
        scheduleTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        scheduleTable.setId("schedule_table");
        
        //           ===========  Finalize Schedule Tab   ===========
        scheduleWindow.getChildren().addAll(optionsBox, filtersBox, scheduleTable);
        
        //      ======================  Manage Tab  ======================
        manageTab = new Tab("Manage");
        manageTab.setClosable(false);
        
        //      ====================  Finalize Tabs   ====================
        tabPane = new TabPane();
        tabPane.getTabs().addAll(fileTab, scheduleTab, manageTab);
        tabPane.getSelectionModel().select(scheduleTab);
        rootLayout.getChildren().add(tabPane);

        // =============================  Finish   ============================
        //  TODO: Change modality once this is actually the main window:
        window.initModality(Modality.APPLICATION_MODAL);
        rebuildTable();
        resize();
//        window.setMaximized(true);
        window.showAndWait();
      
    }  //  end method display(Person)
    private static VBox fileWindow;
    private static Tab fileTab;
    
    private void rebuildTable() {
        ArrayList<PersonRow> tableRows = new ArrayList<>();
        ArrayList<Person> people;
        //  TODO: Add date parameter and use that for the following value:
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMM uuuu");
        scheduleTable.getColumns().clear();
        
        // =============================  Columns  ============================
        
        //      =========================  Name  =========================
        scheduleTable.getColumns().add(createColumn(0, "Name"));
        
        //      ========================  Shifts  ========================
        LocalDate currentDate;
        String dateString;
        for (int x = 0; x < 7; x++) {
            currentDate = today.plusDays(x);
            dateString = currentDate.format(dateFormatter);
            scheduleTable.getColumns().add(createColumn(x+1, dateString));
        }

        // ===========================  Build  List  ==========================
        ObservableList<ObservableList<StringProperty>> data = FXCollections.observableArrayList();
        PersonDAO personDao = new PersonDAO();
        ShiftDateDAO shiftDateDao = new ShiftDateDAO();
        people = personDao.getAllPeople();
        
        //  DEBUG:
//        System.out.println("\n[MainStage.rebuildTable()] people = " + people);
        
        for (Person p : people) {
            ArrayList<String> shifts = shiftDateDao.getWeek(p, LocalDate.now());
            
            //  DEBUG:
//            System.out.println("\n[MainStage.rebuildTable()] shifts = " + shifts);
            
            ObservableList<StringProperty> row = FXCollections.observableArrayList();
            for (String s : shifts) {
                row.add(new SimpleStringProperty(s));
            }
            
            String personName = p.getRank() + " " + p.getFirstName() + " " + p.getLastName();
            row.add(0, new SimpleStringProperty(personName));
            data.add(row);
        }
        
        scheduleTable.setItems(data);
    }
    
    private void resize() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                double width;
                double height;
                // =======================   Schedule Tab  =======================
                width = scheduleWindow.getWidth();
                height = scheduleWindow.getHeight();

                //  DEBUG:
                System.out.println("\n[MainStage.display()] scheduleWindow's width: " + scheduleWindow.getWidth());
                System.out.println("\n[MainStage.display()] scheduleWindow's height: " + scheduleWindow.getHeight());

                // ========================   Manage Tab  ========================
//        width = scheduleWindow.getPrefWidth();
//        height = scheduleWindow.getPrefHeight();
                // =========================   Set Size  =========================
//        window.setMinWidth(width);
                window.setMinHeight(height);
            }
        });

    }

    private class PersonRow {
        private Person person;
        private final ArrayList<String> shifts;
        private final String shift0, shift1, shift2, shift3, shift4, shift5, shift6;
        
        public PersonRow(Person p, ArrayList<String> s) {
            person = p;
            if (s.size() == 7) {
                shifts = s;

                shift0 = shifts.get(0);
                shift1 = shifts.get(1);
                shift2 = shifts.get(2);
                shift3 = shifts.get(3);
                shift4 = shifts.get(4);
                shift5 = shifts.get(5);
                shift6 = shifts.get(6);

            } else {
                shifts = new ArrayList<>();
                
                shift0 = "blank";
                shift1 = "blank";
                shift2 = "blank";
                shift3 = "blank";
                shift4 = "blank";
                shift5 = "blank";
                shift6 = "blank";
            }
                
        }  //  end constructor(Person, ArrayList<String>)
        
        public Person getPerson() { return person; }
//        public static PersonRow getPersonRow (Person p, ArrayList<String> s) { return new PersonRow(p, s);}
        public ArrayList<String> getShifts() { return shifts; }
        
        private int getShiftField (int num) {
            java.lang.reflect.Field field;
            try {
                field = getClass().getDeclaredField("shift" + num);
                return (Integer) field.get(this);
            } catch (Exception e) {
                System.out.println("\n[PersonRow.getShiftField()] Exception thrown");
            }
            return -1;
        }
        
        private void setShiftField (int num, String val) throws NoSuchFieldException, IllegalAccessException {
            java.lang.reflect.Field field = getClass().getDeclaredField("shift" + num);
            field.set(this, val);
        }
    }
}

