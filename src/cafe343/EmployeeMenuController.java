/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafe343;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ronnie
 */
public class EmployeeMenuController implements Initializable {

    //Images and ImageViews for Buttons.
    private Image imageRefreshIcon = new Image("resources/refresh_icon.png");
    private ImageView imageViewRefreshIcon = new ImageView(imageRefreshIcon);
    private ImageView imageViewMealRefreshIcon = new ImageView(imageRefreshIcon);
    private ImageView imageViewDepartmentRefreshIcon = new ImageView(imageRefreshIcon);
    private ImageView imageViewJobRefreshIcon = new ImageView(imageRefreshIcon);
    private ImageView imageViewEmployeeRefreshIcon = new ImageView(imageRefreshIcon);

    private Image imageTableIcon = new Image("resources/table_icon.png");

     //**********ROOT TAB PANE**********
    @FXML
    public TabPane tabPaneMainWindow;
    
    //**********MAIN PANEL TAB**********


    @FXML
    private GridPane gridPaneTables;

    //Table ID for Buttons.
    private int tableID = 1;
    //Column Index and Row Index for adding Buttons to GridPane.
    private int columnIndex = 0;
    private int rowIndex = 0;

    @FXML
    private Button buttonAddTable;

    @FXML
    private Button buttonDeleteTable;

    @FXML
    private Button buttonRefreshTable;
    
    @FXML
    private Button tabPaneSignOutButton;
    
    @FXML
    private TableView tableViewOrders;
    
    @FXML
    private TableColumn<CustomerOrder, Integer> tableNumCol;
    
    @FXML
    private TableColumn<CustomerOrder, Integer> orderNumCol;
    
    @FXML
    private TableColumn<CustomerOrder, String> nameCol;
    
    private ObservableList<CustomerOrder> history = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       
        //Calling refresh to all tables at start.
        tableRefresh();
        try {
            orderRefresh();
            
            //Initializing Root TabPane.
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EmployeeMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
      

        //Initializing Main Panel Tab.
        
        setButtonAddTable(buttonAddTable);
        

        //Initializing Restaurant Menu Tab.
        
  
    } 
    
    @FXML
    private void handleSignOutButtonAction(ActionEvent event) throws IOException {
        Parent Parent = FXMLLoader.load(getClass().getResource("WelcomeScreen.fxml"));
        Scene nextScene = new Scene(Parent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setResizable(false);
        window.setScene(nextScene);
        window.setTitle("Welcome Screen");
        window.show();
    }
    
    @FXML
    private void handleAddTableButtonAction(ActionEvent event) {
        
    }
    
    @FXML
    private void handleDeleteTableButtonAction(ActionEvent event) {
        
    }

    //Method used for adding Buttons(Tables) to GridPane.
    private void tableCreate(){

        //Checking the columnIndex initially to see if it equals to the limit.
        if (columnIndex == gridPaneTables.getColumnConstraints().size()){
            if (rowIndex < gridPaneTables.getRowConstraints().size() - 1){
                //Resetting the columnIndex and incrementing rowIndex.
                columnIndex = 0;
                rowIndex++;
            }
        }
        //Checking ColumnConstraints and RowConstraints before adding any Tables.
        if (columnIndex <= gridPaneTables.getColumnConstraints().size() - 1
                && rowIndex <= gridPaneTables.getRowConstraints().size() - 1){

            //Menu Items for Split Menu Button
            MenuItem menuObjectAddOrder = new MenuItem("Add Order");
            MenuItem menuObjectSetAvailable = new MenuItem("Set Available");
            MenuItem menuObjectSetUnavailable = new MenuItem("Set Unavailable");
            MenuItem menuObjectSetBooked = new MenuItem("Set Booked");

            //Initializing Button for GridPane.
            final SplitMenuButton splitMenuButtonTable = new SplitMenuButton(menuObjectAddOrder, menuObjectSetAvailable,
                                                                    menuObjectSetUnavailable, menuObjectSetBooked);
            //Setting appearance of the table buttons.
            splitMenuButtonTable.setText("Table " + tableID);
            splitMenuButtonTable.setGraphic(new ImageView(imageTableIcon));
            splitMenuButtonTable.setStyle("-fx-background-color:#7CFC00;");
            //Setting Button's Text as TABLE_ID from database.
            splitMenuButtonTable.setId(String.valueOf(tableID));
            //Setting Button's size to MAX for forcing it to fit GridPane cells.
            splitMenuButtonTable.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
            //Sets the Icon on top of the text.
            splitMenuButtonTable.setContentDisplay(ContentDisplay.TOP);


            splitMenuButtonTable.setOnAction(event -> {
                //Order TableID
                String tableID = splitMenuButtonTable.getId();
                
            });

            menuObjectAddOrder.setOnAction(event -> {
                //Creating the Add Order Dialog.
                Dialog addOrderDialog = new Dialog();
                addOrderDialog.setTitle("Add Order");

                //Setting "OK" Button type.
                ButtonType buttonTypeAdd = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
                //Adding Button types "OK" and "CANCEL".
                addOrderDialog.getDialogPane().getButtonTypes().addAll(buttonTypeAdd, ButtonType.CANCEL);

                //Creating the GridPane.
                GridPane gridPane = new GridPane();
                gridPane.setHgap(25);
                gridPane.setVgap(15);
                gridPane.setPadding(new Insets(20, 150, 10, 10));

                //ComboBox for MenuObjects Name.
                ComboBox<String> comboBoxMenuObjectsName = new ComboBox<>();
                comboBoxMenuObjectsName.setPrefWidth(150);
                //Adding MenuObjects Names to ComboBox.
                comboBoxMenuObjectsName.getItems().addAll(MenuObject.fillComboBoxMeal());
                comboBoxMenuObjectsName.getSelectionModel().selectFirst();
                comboBoxMenuObjectsName.setOnMousePressed(event1 -> {
                    //When the ComboBox is clicked, delete and add MenuObjects Names again to update changes.
                    comboBoxMenuObjectsName.getItems().clear();
                    comboBoxMenuObjectsName.getItems().addAll(MenuObject.fillComboBoxMeal());
                    comboBoxMenuObjectsName.getSelectionModel().selectFirst();
                });

                //TextField for TableID.
                TextField textFieldTableID = new TextField();
                textFieldTableID.setPrefWidth(150);
                textFieldTableID.setEditable(false);
                textFieldTableID.setDisable(true);
                textFieldTableID.setText(splitMenuButtonTable.getId());

                //Adding Nodes to GridPane.
                gridPane.add(new Label("Meal"), 1, 0);
                gridPane.add(comboBoxMenuObjectsName, 0, 0);
                gridPane.add(new Label("Table"),1,1);
                gridPane.add(textFieldTableID,0,1);

                addOrderDialog.getDialogPane().setContent(gridPane);

                //Adding application_icon to Stage.
                Stage addOrderStage = (Stage) addOrderDialog.getDialogPane().getScene().getWindow();
                addOrderStage.getIcons().add(new Image("resources/application_icon.png"));

                //Getting result depending on the Button Type clicked. (Add or Cancel)
                Optional<ButtonType> result = addOrderDialog.showAndWait();

                if (result.get() == buttonTypeAdd){
                    //Add Button is clicked. Creating Order and closing Dialog.

                    //Order TableID
                    String tableID = textFieldTableID.getText();
                    //Order MenuObjects Name
                    String menuObjectName = comboBoxMenuObjectsName.getValue();
                    //Order Date
                    String orderDate = String.valueOf(LocalDateTime.now());

                    

                    //Creating Order Added Dialog.
                    Alert alertOrderAdded = new Alert(Alert.AlertType.INFORMATION);
                    alertOrderAdded.setTitle("Order");
                    alertOrderAdded.setHeaderText(null);
                    alertOrderAdded.setContentText("The order has been added.");

                    //Adding application_icon to Order Added Dialog.
                    Stage orderCreatedStage = (Stage) alertOrderAdded.getDialogPane().getScene().getWindow();
                    orderCreatedStage.getIcons().add(new Image("resources/application_icon.png"));

                    
                    alertOrderAdded.showAndWait();
                }
            });

            menuObjectSetAvailable.setOnAction(event -> splitMenuButtonTable.setStyle("-fx-background-color:#7CFC00;"));

            menuObjectSetUnavailable.setOnAction(event -> splitMenuButtonTable.setStyle("-fx-background-color:#FF0000;"));

            menuObjectSetBooked.setOnAction(event -> splitMenuButtonTable.setStyle("-fx-background-color:#FFFF00;"));

            //Adding the created Button to GridPane and incrementing columnIndex.
            gridPaneTables.add(splitMenuButtonTable,columnIndex,rowIndex);
            columnIndex++;

            //Calling createTable Method to add created table to database by using tableID.
            Table.createTable(tableID);
            //Incrementing tableID after the add process in order to synchronize with TABLE_ID column from database.
            tableID++;

        } else {

            //Showing Error Dialog.
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Error: You can't add more tables.");
            errorAlert.setContentText("You have reached the maximum number of tables." +
                    " You can't add more tables to the panel.");
            //Adding error_icon to Error Window.
            Stage errorStage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
            errorStage.getIcons().add(new Image("resources/error_icon.png"));
            errorAlert.showAndWait();
        }
    }

    //Setting buttonAddTable with tableCreate Method.
    private void setButtonAddTable(Button buttonAddTable) {
        buttonAddTable.setOnAction(event -> tableCreate());
        this.buttonAddTable = buttonAddTable;
    }
    
    public void orderRefresh() throws ClassNotFoundException{
        
         try {
                //Connecting with database.
                Class.forName("org.apache.derby.jdbc.ClientDriver");
                Connection connection = DriverManager.getConnection( DatabaseConnection.DB_URL );
                Statement statement = connection.createStatement();
                //Returning the Usernames from database that are same with entered text.
                ResultSet resultSet = statement.executeQuery("SELECT ORDERID, TABLEID, OBJECTNAME FROM CUSTOMERORDER ");
                System.out.println(resultSet.toString());
                
                //Checking any Username is found from database that are same with entered text.
                while (resultSet.next()){
                    CustomerOrder createdOrder = new CustomerOrder(resultSet.getInt("ORDERID"), resultSet.getInt("TABLEID"), resultSet.getString("OBJECTNAME"));
                        history.add(createdOrder);
                
            } 
             }
      
                    
                    
                
                
                
                  catch (SQLException e) {
                e.printStackTrace();
            }
        tableNumCol.setCellValueFactory(cellData -> cellData.getValue().getTableIDProperty().asObject());
        nameCol.setCellValueFactory(cellData -> cellData.getValue().getObjectNameProperty());
        orderNumCol.setCellValueFactory(cellData -> cellData.getValue().getOrderIDProperty().asObject());
        tableViewOrders.setItems(history);
        
    }

    private void tableRefresh() {
        //Add Tables until tableID is equal to TABLES in database.
        while (tableID <= Table.listTable()){

            //Checking the columnIndex initially to see if it equals to the limit.
            if (columnIndex == gridPaneTables.getColumnConstraints().size()){
                if (rowIndex < gridPaneTables.getRowConstraints().size() - 1){
                    //Resetting the columnIndex and incrementing rowIndex.
                    columnIndex = 0;
                    rowIndex++;
                }
            }
            //Checking ColumnConstraints and RowConstraints before adding any Tables.
            if (columnIndex <= gridPaneTables.getColumnConstraints().size() - 1
                    && rowIndex <= gridPaneTables.getRowConstraints().size() - 1) {

                //Menu Items for Split Menu Button
                MenuItem menuObjectAddOrder = new MenuItem("Add Order");
                MenuItem menuObjectSetAvailable = new MenuItem("Set Available");
                MenuItem menuObjectSetUnavailable = new MenuItem("Set Unavailable");
                MenuItem menuObjectSetBooked = new MenuItem("Set Booked");

                //Initializing Button for GridPane.
                final SplitMenuButton splitMenuButtonTable = new SplitMenuButton(menuObjectAddOrder, menuObjectSetAvailable,
                                                                                menuObjectSetUnavailable, menuObjectSetBooked);
                //Setting appearance of the table buttons.
                splitMenuButtonTable.setText("Table " + tableID);
                splitMenuButtonTable.setGraphic(new ImageView(imageTableIcon));
                splitMenuButtonTable.setStyle("-fx-background-color:#7CFC00;");
                //Setting Button's Text as TABLE_ID from database.
                splitMenuButtonTable.setId(String.valueOf(tableID));
                //Setting Button's size to MAX for forcing it to fit GridPane cells.
                splitMenuButtonTable.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                //Sets the Icon on top of the text.
                splitMenuButtonTable.setContentDisplay(ContentDisplay.TOP);

                splitMenuButtonTable.setOnAction(event -> {
                    //Order TableID
                    String tableID = splitMenuButtonTable.getId();
                    
                });

                menuObjectAddOrder.setOnAction(event -> {
                    //Creating the Add Order Dialog.
                    Dialog addOrderDialog = new Dialog();
                    addOrderDialog.setTitle("Add Order");

                    //Setting "OK" Button type.
                    ButtonType buttonTypeAdd = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
                    //Adding Button types "OK" and "CANCEL".
                    addOrderDialog.getDialogPane().getButtonTypes().addAll(buttonTypeAdd, ButtonType.CANCEL);

                    //Creating the GridPane.
                    GridPane gridPane = new GridPane();
                    gridPane.setHgap(25);
                    gridPane.setVgap(15);
                    gridPane.setPadding(new Insets(20, 150, 10, 10));

                    //ComboBox for MenuObjects Name.
                    ComboBox<String> comboBoxMealName = new ComboBox<>();
                    comboBoxMealName.setPrefWidth(150);
                    //Adding MenuObjects Names to ComboBox.
                    comboBoxMealName.getItems().addAll(MenuObject.fillComboBoxMeal());
                    comboBoxMealName.getSelectionModel().selectFirst();
                    comboBoxMealName.setOnMousePressed(event1 -> {
                        //When the ComboBox is clicked, delete and add MenuObjects Names again to update changes.
                        comboBoxMealName.getItems().clear();
                        comboBoxMealName.getItems().addAll(MenuObject.fillComboBoxMeal());
                        comboBoxMealName.getSelectionModel().selectFirst();
                    });

                    //TextField for TableID.
                    TextField textFieldTableID = new TextField();
                    textFieldTableID.setPrefWidth(150);
                    textFieldTableID.setEditable(false);
                    textFieldTableID.setDisable(true);
                    textFieldTableID.setText(splitMenuButtonTable.getId());

                    //Adding Nodes to GridPane.
                    gridPane.add(new Label("Meal"), 1, 0);
                    gridPane.add(comboBoxMealName, 0, 0);
                    gridPane.add(new Label("Table"),1,1);
                    gridPane.add(textFieldTableID,0,1);

                    addOrderDialog.getDialogPane().setContent(gridPane);

                    //Adding application_icon to Stage.
                    Stage addOrderStage = (Stage) addOrderDialog.getDialogPane().getScene().getWindow();
                    addOrderStage.getIcons().add(new Image("resources/application_icon.png"));

                    //Getting result depending on the Button Type clicked. (Add or Cancel)
                    Optional<ButtonType> result = addOrderDialog.showAndWait();

                    if (result.get() == buttonTypeAdd){
                        //Add Button is clicked. Creating Order and closing Dialog.

                        //Order TableID
                        String tableID = textFieldTableID.getText();
                        //Order MenuObjects Name
                        String menuObjectName = comboBoxMealName.getValue();
                        //Order Date
                        String orderDate = String.valueOf(LocalDateTime.now());

                        //Creating Order in the database.
                       

                        //Creating Order Added Dialog.
                        Alert alertOrderAdded = new Alert(Alert.AlertType.INFORMATION);
                        alertOrderAdded.setTitle("Order");
                        alertOrderAdded.setHeaderText(null);
                        alertOrderAdded.setContentText("The order has been added.");

                        //Adding application_icon to Order Added Dialog.
                        Stage orderCreatedStage = (Stage) alertOrderAdded.getDialogPane().getScene().getWindow();
                        orderCreatedStage.getIcons().add(new Image("resources/application_icon.png"));

                        

                        alertOrderAdded.showAndWait();
                    }
                });

                menuObjectSetAvailable.setOnAction(event -> splitMenuButtonTable.setStyle("-fx-background-color:#7CFC00;"));

                menuObjectSetUnavailable.setOnAction(event -> splitMenuButtonTable.setStyle("-fx-background-color:#FF0000;"));

                menuObjectSetBooked.setOnAction(event -> splitMenuButtonTable.setStyle("-fx-background-color:#FFFF00;"));

                //Adding the created Button to GridPane and incrementing columnIndex.
                gridPaneTables.add(splitMenuButtonTable, columnIndex, rowIndex);
                columnIndex++;

                //Incrementing tableID after the add process in order to synchronize with TABLE_ID column from database.
                tableID++;
            }
        }
    }
    
    
      
    
}
