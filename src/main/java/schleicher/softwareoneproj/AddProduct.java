package schleicher.softwareoneproj;

/**
 * @author William Zapata9Schleicher at WGU
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

import static schleicher.softwareoneproj.Inventory.getAllParts;
import static schleicher.softwareoneproj.Inventory.lookupPart;

/**This class acts as a controller to the add product screen, and supports adding a new product and its associated parts to the inventory.*/
public class AddProduct {

    /**GUI element*/
    private Stage stage;
    /**GUI element*/
    private Scene scene;
    /**GUI element*/
    public TextField idField;
    /**GUI element*/
    public TextField nameField;
    /**GUI element*/
    public TextField invField;
    /**GUI element*/
    public TextField priceField;
    /**GUI element*/
    public TextField maxField;
    /**GUI element*/
    public TextField minField;
    /**GUI element*/
    public TextField searchPartsField;

    //The unassociated parts table:
    /**GUI element*/
    public TableView allPartsView;
    /**GUI element*/
    public TableColumn idSearchCol;
    /**GUI element*/
    public TableColumn nameSearchCol;
    /**GUI element*/
    public TableColumn invSearchCol;
    /**GUI element*/
    public TableColumn priceSearchCol;

    //The associated parts table components:
    /**GUI element*/
    public TableView associatedPartsTable;
    /**GUI element*/
    public TableColumn idCol;
    /**GUI element*/
    public TableColumn nameCol;
    /**GUI element*/
    public TableColumn stockCol;
    /**GUI element*/
    @FXML
    private TableColumn priceCol;
    /**GUI element*/
    public Button addPartButton;
    /**GUI element*/
    public Button saveButton;
    /**GUI element*/
    public Button cancelButton;
    /**GUI element*/
    public Button removePartButton;
    /**new temp Product creation so that its associated list can be accessed*/
    private Product tempProduct = new Product(Inventory.productCounter, "name", 0.0, 0, 0,0);

    /**Populates the auto-generated ID Field and initializes the tableViews*/
    public void initialize() {
        idField.setText(Inventory.productCounter + " (auto-generated)");
        updateTableviews();
    }

    /**Updates tableViews with the latest data from the relevant ObservableLists*/
    public void updateTableviews() {
        //Update/set all (unassociated) parts table
        idSearchCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameSearchCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        invSearchCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceSearchCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        allPartsView.setItems(getAllParts());

        //Update/set associated parts table
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartsTable.setItems(tempProduct.getAllAssociatedParts());
    }

    /**Error checks all fields and saves new product if text entries are valid*/
    public void onSaveProduct(ActionEvent actionEvent) throws IOException {
        //Declare variables
        int id = Inventory.productCounter;
        String name;
        int stock;
        double price;
        int max;
        int min;

        //Get name
        name = nameField.getText();
        if (name.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error, please include a name!");
            alert.showAndWait();
            return;
        }

        //Get inv
        try {
            stock = Integer.parseInt(invField.getText());
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error, please enter an int in the Inv field!");
            alert.showAndWait();
            return;
        }

        //Get price
        try {
            price = Double.parseDouble(priceField.getText());
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error, please enter a double in the Price field!");
            alert.showAndWait();
            return;
        }

        //Get max
        try {
            max = Integer.parseInt(maxField.getText());
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error, please enter an int in the max field!");
            alert.showAndWait();
            return;
        }

        //Get min
        try {
            min = Integer.parseInt(minField.getText());
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error, please enter an int in the min field!");
            alert.showAndWait();
            return;
        }

        //Min max checks
        if (max < min) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error, please make sure the max is equal to or greater than the min!");
            alert.showAndWait();
            return;
        }
        if (stock < min || stock > max) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error, please make sure the inv is at or between the min and max!");
            alert.showAndWait();
            return;
        }

        //Create product from data entered
        tempProduct.setId(id);
        tempProduct.setName(name);
        tempProduct.setStock(stock);
        tempProduct.setPrice(price);
        tempProduct.setMax(max);
        tempProduct.setMin(min);

        //Add product to inventory
        Inventory.addProduct(tempProduct);

        //Return to main view after successful save product
        Parent root = FXMLLoader.load(getClass().getResource("mainView.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Inventory Management System - Home");
        stage.setScene(scene);
        stage.show();

        //Assuming code ran and new Product added, update the product ID autogenerated counter
        Inventory.productCounter = Inventory.productCounter + 1;
    }

    /**Cancels product creation and returns user to main screen without comment*/
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("mainView.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Inventory Management System - Home");
        stage.setScene(scene);
        stage.show();
    }


    /**Removes a part from the product's associated parts list (and the table)*/
    public void onRemovePart(ActionEvent actionEvent) {
        Part part = (Part) associatedPartsTable.getSelectionModel().getSelectedItem();
        if (part == null) {
            Alert noPartDeleteAlert = new Alert(Alert.AlertType.ERROR,
                    "No part has been selected to delete. Please select a part to delete it.");
            noPartDeleteAlert.showAndWait();
            return;
        }
        tempProduct.deleteAssociatedPart(part);
        updateTableviews();
    }

    /**Searches for all parts within the product's allParts tableview*/
    public void onSearchParts(KeyEvent keyEvent) {
        //ENTER Key only check (warning is annoying otherwise)
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String s = searchPartsField.getText();
            ObservableList<Part> sParts = lookupPart(s);
            allPartsView.setItems(sParts);
            if (sParts.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "No matching parts found! Input blank search term and press enter to return to complete part list.");
                alert.showAndWait();
            }
        }
    }

    /**Adds a part from the unassociated parts list to the product's associatedParts*/
    public void onAddPart(ActionEvent actionEvent) {
        Part part = (Part)allPartsView.getSelectionModel().getSelectedItem();
        if (part == null) {
            Alert noPartDeleteAlert = new Alert(Alert.AlertType.ERROR,
                    "No part has been selected to add. Please select a part to add it.");
            noPartDeleteAlert.showAndWait();
            return;
        }
        tempProduct.addAssociatedPart(part);
        updateTableviews();
    }
}
