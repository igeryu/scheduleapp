// MainStage.java

/**
 * Changelog:
 * 
 * 2016-03-07 : Created file
 * 2016-03-07 : Added a tabbed pane with 'Schedule' and 'Manage' tabs
 * 
 * 2016-03-09 : Schedule Shift View works, shows each person (rows) with their current shift today and next six days.
 * 2016-03-09 : Made shift cells color-coded for easier viewing.
 * 
 * 2016-03-13 : Changed rootLayout to a BorderPane (from StackPane)
 * 2016-03-13 : Grouped class variables by type/function
 * 2016-03-13 : Extracted code to build scheduleTab, manageTab, and filtersBox from display() to buildScheduleTab(), buildManageTab() and buildFiltersBox()
 * 2016-03-13 : Changed name of rebuildTable() to populateShiftViewTable()
 * 2016-03-13 : Changed name of scheduleTable to outputTable
 */
package window;

import domain.Person;
import domain.PersonDAO;
import domain.ShiftDAO;
import domain.ShiftDateDAO;
import domain.WorkcenterDAO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
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
    private static VBox fileWindow;
    private static Tab fileTab;
    private static int workcenter = -1;
    private static int shift = -1;
    
    // ===========================   Misc  ===========================
    private static TabPane tabPane;
    private static HBox filtersBox;
    private static TableView outputTable;
    
    // =========================   Schedule   ========================
    private static Tab scheduleTab;
    private static VBox scheduleWindow;
    private static HBox scheduleOptionsBox;
    
    // ==========================   Manage   =========================
    private static Tab manageTab;
    private static VBox manageWindow;
    private static HBox manageOptionsBox;
    
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
        BorderPane rootLayout = new BorderPane();
        int width = 650;
        int height = 300;
        Scene scene = new Scene(rootLayout, width, height);
        height = 0;
        scene.getStylesheets().add("css/mainStage.css");
        window.setScene(scene);
        
        window.setMinWidth(width);
        window.setMinHeight(height);
        
        // ===========================   Tab Area  ============================
        fileTab = new Tab("File");
        fileTab.setClosable(false);
        fileTab.getStyleClass().add("file");
        fileWindow = new VBox();
        fileTab.setContent(fileWindow);
        outputTable = new TableView();

        //      =====================  Schedule Tab  =====================
        buildScheduleTab();
        
        //      ======================  Manage Tab  ======================
        buildManageTab();
        
        // ==========================  Filters Row   ==========================
        buildFiltersBox();
        
        
        
        //      ====================  Finalize Tabs   ====================
        buildShiftViewTable();
        tabPane = new TabPane();
        tabPane.getTabs().addAll(fileTab, scheduleTab, manageTab);
        tabPane.getSelectionModel().select(scheduleTab);
        rootLayout.setTop(tabPane);
        VBox displayBox = new VBox();
        displayBox.getChildren().addAll(filtersBox, outputTable);
        rootLayout.setCenter(displayBox);

        // =============================  Finish   ============================
        //  TODO: Change modality once this is actually the main window:
        window.initModality(Modality.APPLICATION_MODAL);
        populateShiftViewTable();
        resize();
//        window.setMaximized(true);
        window.showAndWait();
      
    }  //  end method display(Person)

    private void buildFiltersBox() {
        filtersBox = new HBox();
        double scaleFactor = .8;
//        filtersBox.getChildren().add(new Label("Filters..."));
        
        // ========================  Workcenters   =======================
        filtersBox.getChildren().add(new Label("Workcenters:"));
        ObservableList<String> workcenterList = (new WorkcenterDAO()).getList();
        ToggleGroup workcenterGroup = new ToggleGroup();
        workcenterGroup.selectedToggleProperty().addListener((ov, oldToggle, newToggle) -> {
            workcenter = workcenterGroup.getToggles().indexOf(newToggle);
            populateShiftViewTable();
        });
        
        RadioButton newButton = new RadioButton("All");
        newButton.setToggleGroup(workcenterGroup);
        filtersBox.getChildren().add(newButton);
        workcenterGroup.selectToggle(newButton);
        newButton.setScaleX(scaleFactor);
        newButton.setScaleY(scaleFactor);
        
        for (String item : workcenterList) {
            newButton = new RadioButton(item);
            newButton.setToggleGroup(workcenterGroup);
            newButton.setScaleX(scaleFactor);
            newButton.setScaleY(scaleFactor);
            filtersBox.getChildren().add(newButton);
        }
        
        Separator separator = new Separator();
//        separator.setValignment(VPos.CENTER);
        separator.setOrientation(Orientation.VERTICAL);
        filtersBox.getChildren().add(separator);
        
        // ==========================  Shifts   ==========================
        filtersBox.getChildren().add(new Label("Shifts:"));
        ObservableList<String> shiftList = (new ShiftDAO()).getList();
        ToggleGroup shiftGroup = new ToggleGroup();
        shiftGroup.selectedToggleProperty().addListener((ov, oldToggle, newToggle) -> {
            shift = shiftGroup.getToggles().indexOf(newToggle);
            populateShiftViewTable();
        });
        
        newButton = new RadioButton("All");
        newButton.setToggleGroup(shiftGroup);
        filtersBox.getChildren().add(newButton);
        shiftGroup.selectToggle(newButton);
        newButton.setScaleX(scaleFactor);
        newButton.setScaleY(scaleFactor);
        
        for (String item : shiftList) {
            newButton = new RadioButton(item);
            newButton.setToggleGroup(shiftGroup);
            newButton.setScaleX(scaleFactor);
            newButton.setScaleY(scaleFactor);
            filtersBox.getChildren().add(newButton);
        }
        
        filtersBox.getStyleClass().add("pane");
    }
    
    private void buildManageTab() {
        manageWindow = new VBox();
        
        manageTab = new Tab("Manage");
        manageTab.setClosable(false);
        manageTab.getStyleClass().add("manage");
        manageTab.setContent(manageWindow);
        
        //           ================  Options Row   ================
        manageOptionsBox = new HBox(10);
        
        Button addPersonButton = new Button("Add Person");
        addPersonButton.setOnAction(e -> AddPersonStage.display());
        
        Button editPersonButton = new Button("Edit Person");
        editPersonButton.setOnAction(e -> EditPersonStage.display(null));
        
        manageOptionsBox.getChildren().addAll(addPersonButton, editPersonButton);
        manageOptionsBox.getStyleClass().addAll("options");
        
        //           ===========  Finalize Schedule Tab   ===========
//        manageWindow.getChildren().addAll(manageOptionsBox, filtersBox, outputTable);
        manageWindow.getChildren().addAll(manageOptionsBox);
    }
    
    private void buildScheduleTab() {
        scheduleWindow = new VBox();
        
        scheduleTab = new Tab("Schedule");
        scheduleTab.setClosable(false);
        scheduleTab.getStyleClass().add("schedule");
        scheduleTab.setContent(scheduleWindow);
        
        //      =====================  Options Row   =====================
        scheduleOptionsBox = new HBox(10);
        scheduleOptionsBox.getChildren().addAll(new Label("Options..."));
        scheduleOptionsBox.getStyleClass().addAll("options");
        
        //           ===========  Finalize Schedule Tab   ===========
//        scheduleWindow.getChildren().addAll(scheduleOptionsBox, filtersBox, outputTable);
        scheduleWindow.getChildren().addAll(scheduleOptionsBox);
        
    }
    
    private void populateShiftViewTable() {
        ArrayList<PersonRow> tableRows = new ArrayList<>();
        ArrayList<Person> people;
        //  TODO: Add date parameter and use that for the following value:
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMM uuuu");
        outputTable.getColumns().clear();

        // =============================  Columns  ============================
        //      =========================  Name  =========================
        outputTable.getColumns().add(createColumn(0, "Name"));

        //      ========================  Shifts  ========================
        LocalDate currentDate;
        String dateString;
        for (int x = 0; x < 7; x++) {
            currentDate = today.plusDays(x);
            dateString = currentDate.format(dateFormatter);
            outputTable.getColumns().add(createColumn(x + 1, dateString));
        }

        // ===========================  Build  List  ==========================
        ObservableList<ObservableList<StringProperty>> data = FXCollections.observableArrayList();
        PersonDAO personDao = new PersonDAO();
        ShiftDateDAO shiftDateDao = new ShiftDateDAO();
        people = personDao.getPeopleArrayListByShift(shift, workcenter);
//        people = personDao.getAllPeople();

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

        outputTable.setItems(data);
    }

    private void buildShiftViewTable() {
        //      =================  Main Schedule Window  =================
        int scheduleBoxWidth = 500;
        int scheduleBoxHeight = 500;
        outputTable.setMinSize(scheduleBoxWidth, scheduleBoxHeight);
        outputTable.getStyleClass().add("pane");
        outputTable.getSelectionModel().setCellSelectionEnabled(true);
        outputTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        outputTable.setId("schedule_table");
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
                return (int) field.get(this);
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

