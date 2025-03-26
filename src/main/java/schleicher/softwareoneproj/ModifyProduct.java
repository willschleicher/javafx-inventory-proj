package schleicher.softwareoneproj;

/**
 * @author William Zapata9Schleicher at WGU
 */

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

import static schleicher.softwareoneproj.Inventory.*;

/**This class acts as the controller for the Modify Product window, which allows the user modify a product (except for its id)*/
public class ModifyProduct {
    /**GUI Element*/
    private Stage stage;
    /**GUI Element*/
    private Scene scene;
    /**GUI Element*/
    public TextField idField;
    /**GUI Element*/
    public TextField nameField;
    /**GUI Element*/
    public TextField invField;
    /**GUI Element*/
    public TextField priceField;
    /**GUI Element*/
    public TextField maxField;
    /**GUI Element*/
    public TextField minField;
    /**GUI Element*/
    public TextField searchPartsField;

    //The unassociated parts table:
    /**GUI Element*/
    public TableView allPartsView;
    /**GUI Element*/
    public TableColumn idSearchCol;
    /**GUI Element*/
    public TableColumn nameSearchCol;
    /**GUI Element*/
    public TableColumn invSearchCol;
    /**GUI Element*/
    public TableColumn priceSearchCol;

    //The associated parts table components:
    /**GUI Element*/
    public TableView associatedPartsTable;
    /**GUI Element*/
    public TableColumn idCol;
    /**GUI Element*/
    public TableColumn nameCol;
    /**GUI Element*/
    public TableColumn stockCol;
    /**GUI Element*/
    @FXML
    private TableColumn priceCol;
    /**GUI Element*/
    public Button addPartButton;
    /**GUI Element*/
    public Button saveButton;
    /**GUI Element*/
    public Button cancelButton;
    /**GUI Element*/
    public Button removePartButton;
    /**Make a tempProduct to be edited from the selected product*/
    private Product tempProduct = lookupProduct(modifyProductId);
    /**Keep track of the modified product's place in its list*/
    private int tempProductIndex = lookupProductIndex(modifyProductId);

    /**Initializes tableViews and populates text fields*/
    public void initialize() {
        updateTableviews();

        //Populate text fields with modified product's information
        idField.setText("" + tempProduct.getId() + " (auto-generated)");
        nameField.setText(tempProduct.getName());
        invField.setText("" + tempProduct.getStock());
        priceField.setText("" + tempProduct.getPrice());
        maxField.setText("" + tempProduct.getMax());
        minField.setText("" + tempProduct.getMin());
    }

    /**Initializes or updates all tableViews on the page*/
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

    /**Checks text field for valid input then saves the product*/
    public void onSaveProduct(ActionEvent actionEvent) throws IOException {
        //Declare variables
        int id = tempProduct.getId();
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

        //Replace the existing Product in the Product Inventory list
        Inventory.updateProduct(tempProductIndex, tempProduct);

        //Return to main view after successful save product
        Parent root = FXMLLoader.load(getClass().getResource("mainView.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Inventory Management System - Home");
        stage.setScene(scene);
        stage.show();

        //Assuming code ran and new Product added, update the product ID autogenerated counter (this code leftover from AddProduct controller)
        //Inventory.productCounter = Inventory.productCounter + 1;
    }

    /**Cancels editing the product and returns user to main screen*/
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("mainView.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Inventory Management System - Home");
        stage.setScene(scene);
        stage.show();
    }


    /**Removes a part from the product's associated parts*/
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

    /**Searches all possible parts for the entered search term*/
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

    /**Adds an associated part to the product*/
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
