package schleicher.softwareoneproj;

/**
 * @author William Zapata9Schleicher at WGU
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

//import static schleicher.softwareoneproj.AddPart.initializeIDField;

/**This class acts as a controller for the main screen, and implements functions that allow access and modification of the lists of all the parts and all the products stored in the program.*/
public class Inventory implements Initializable {
    /**GUI element*/
    public TableView partTable;
    /**GUI element*/
    public TableView productTable;
    /**GUI element*/
    public Button exitButton;
    /**GUI element*/
    public TableColumn partIDCol;
    /**GUI element*/
    public TableColumn partNameCol;
    /**GUI element*/
    public TableColumn partInventoryCol;
    /**GUI element*/
    public TableColumn partPriceCol;
    /**GUI element*/
    public TableColumn productIDCol;
    /**GUI element*/
    public TableColumn productNameCol;
    /**GUI element*/
    public TableColumn productInventoryCol;
    /**GUI element*/
    public TableColumn productPriceCol;
    /**Autogen ID count keeper for parts*/
    public static int PartCounter = 1;
    /**Autogen ID count keeper for products*/
    public static int productCounter = 1;
    /**GUI element*/
    public TextField partSearchField;
    /**GUI element*/
    public TextField productSearchField;
    /**GUI element*/
    private Stage stage;
    /**GUI element*/
    private Scene scene;
    /**GUI element*/
    public Label TheLabel;
    /**GUI element*/
    public Button addPart;
    /**GUI element*/
    public Button modifyPart;
    /**GUI element*/
    public Button deletePart;
    /**GUI element*/
    public Button addProduct;
    /**GUI element*/
    public Button modifyProduct;
    /**GUI element*/
    public Button deleteProduct;
    /**temp Part to pass onto modify part function*/
    public static Part toModify;
    /**Tracker to save as appropriate Part type later*/
    public static int modifyTypeToggle = 0;
    /**First time start tracker int*/
    private static int firstTime = 1;
    /**Keeps track of the product ID of what will be modified*/
    public static int modifyProductId;

    /**Inventory ObservableList, also create an InHouse and an OutSourced sample object*/
    private static ObservableList<Part> allParts = FXCollections.observableArrayList(
            new InHouse(98, "Sample A", 3, 1.13, 1, 10),
            new OutSourced(99, "Sample B", 2, 1.99, 2, 20)
    );

    /**Returns allParts list as an ObservableList*/
    public static ObservableList<Part> getAllParts() {
        return FXCollections.observableArrayList(allParts);
    }

    /**Products ObservableList with sample Products*/
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList(
            new Product(96, "Samplina A", 1.50, 4, 5, 2),
            new Product(97, "Samplina B", 3.25, 8, 10, 1)
    );

    /**Returns allProducts list as an ObservableList*/
    public static ObservableList<Product> getAllProducts() {
        return FXCollections.observableArrayList(allProducts);
    }

    /**Starts the mainView by making the tableViews visible and adds sample data to the two sample Parts if it is the first time running the program.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Set the table with the allParts list
        updateTableviews();

        //Add some company name and machine ID to the sample items!
        if (firstTime == 1) {
            //Set Sample A's company name field.
            OutSourced testOsB = (OutSourced) allParts.get(1);
            testOsB.setCoName("Sample B CO!");

            //Set Sample B's machine ID field.
            InHouse testIhA = (InHouse) allParts.get(0);
            testIhA.setMachineID(300);

            firstTime = 0;
            System.out.println("Main screen successfully initialized!");
        }
    }

    /**Initializes or refreshes the tableViews with the latest observableList data*/
    public void updateTableviews() {
        // Set the items for the Part TableView

        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        partTable.setItems(getAllParts());

        //Set the items for the Product TableView
        productIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTable.setItems(getAllProducts());
        //System.out.println("Items were set!");
    }
    //cell factory code derived from Kinkead WGU webinar: https://wgu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=9969ffe1-8a4e-4d6c-aa1d-abbd01057497

    /**A public static addPart to the allParts ObservableList function, for use in the add Part page*/
    public static void addPart(Part Part){
        allParts.add(Part);
    }

    /**A public static addProduct to the allProducts List, for use in the add Product page*/
    public static void addProduct(Product product) {
        allProducts.add(product);
    }

    /**A public static looks up part by its ID function*/
    public static Part lookupPart (int partId) {
        for (Part part : allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

    /**A lookupProduct function similar to lookupPart by ID*/
    public static Product lookupProduct (int productID) {
        for (Product product : allProducts) {
            if (product.getId() == productID) {
                return product;
            }
        }
        return null;
    }

    /**Returns the index for a part based on its ID! (helpful for modify parts features)*/
    public static int lookupPartIndex(int partID) {
        Part s = lookupPart(partID);
        return allParts.indexOf(s);
    }

    /**Returns the index for a product based on its ID! (helpful for modify products features)*/
    public static int lookupProductIndex(int productID) {
        Product s = lookupProduct(productID);
        return allProducts.indexOf(s);
    }

    /**Finds matching parts by name and returns a list of those matching names*/
    public static ObservableList<Part> lookupPart (String partName) {
        ObservableList<Part> matchingParts = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().contains(partName)) {
                matchingParts.add(part);
            }
        }
        return matchingParts;
    }

    /**Finds matching products by name and returns a list of those matching names*/
    public static ObservableList<Product> lookupProduct (String productName) {
        ObservableList<Product> matchingProducts = FXCollections.observableArrayList();
        for (Product product : allProducts) {
            if (product.getName().contains(productName)) {
                matchingProducts.add(product);
            }
        }
        return matchingProducts;
    }

   /**Activates a search for Parts when user presses enter and warns if no matches are found*/
    public void onSearchEnter(KeyEvent keyEvent) {
        //ENTER Key only check (warning is annoying otherwise)
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String s = partSearchField.getText();
            ObservableList<Part> sParts = lookupPart(s);
            partTable.setItems(sParts);
            if (sParts.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "No matching parts found! Input blank search term and press enter to return to complete part list.");
                alert.showAndWait();
            }
        }
    }

    /**Activates a search for Products when user presses enter and warns if no matches are found*/
    public void onProductSearchEnter(KeyEvent keyEvent) {
        //ENTER Key only check (warning is annoying otherwise)
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String s = productSearchField.getText();
            ObservableList<Product> sProducts = lookupProduct(s);
            productTable.setItems(sProducts);
            if (sProducts.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "No matching products found! Input blank search term and press enter to return to complete product list.");
                alert.showAndWait();
            }
        }
    }

    //Above two functions derived from 16:00 min in Marc Kinkead Webinar: https://wgu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=7cfdfc5a-19ad-43e6-a503-ab70003199c6

    /**Replaces the part in the index of the allParts list with inputted part*/
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**Replaces the product in the index of the allProducts list with inputted product*/
    public static void updateProduct(int index, Product product) {
        allProducts.set(index, product);
    }

    /**Exits the program with confirmation warning*/
    public void OnButtonClicked(ActionEvent actionEvent) {

        int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit program? You will lose all data if you exit!", "Exit Warning", JOptionPane.YES_NO_CANCEL_OPTION);

        if (reply == JOptionPane.YES_OPTION) {
        }
        else {
            return;
        }
        //System.out.println("Program exiting now!");
        System.exit(0);
    }

    /**Opens the Add Part page*/
    public void switchToAddPart(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addPart.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Inventory Management System - Add Part");
        stage.setScene(scene);
        stage.show();

    }
    //Scene switch code derived from https://www.youtube.com/watch?v=hcM-R-YOKkQ
    //and from WGU webinar: https://wgu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=33b1ead5-7f29-4b1d-85df-acb7010c48dd

    /**Extracts part to be modified and switches to the Modify Part window*/
    public void onModifyPart(ActionEvent event) throws IOException {
        int isPart = 0;

        if (partTable.getSelectionModel().getSelectedItem() instanceof InHouse){
            toModify = (InHouse) partTable.getSelectionModel().getSelectedItem();
            modifyTypeToggle = 0;
            isPart = 1;
        }
        else if (partTable.getSelectionModel().getSelectedItem() instanceof OutSourced){
            toModify = (OutSourced) partTable.getSelectionModel().getSelectedItem();
            modifyTypeToggle = 1;
            isPart = 1;
        }

        if (isPart == 0) {
            Alert noPart = new Alert(Alert.AlertType.ERROR,
                    "There is no part selected to be modified. Please select a part before attempting to modify.");
            noPart.showAndWait();
            return;
        }

        else {
            Parent root = FXMLLoader.load(getClass().getResource("modifyPart.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle("Inventory Management System - Modify Part");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**Deletes selected part with user confirmation then calls part deletion and updates table*/
    public void onDeletePart(ActionEvent actionEvent) {

        int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?", "Delete", JOptionPane.YES_NO_CANCEL_OPTION);

        if (reply != JOptionPane.YES_OPTION) {
            return;
        }
        //System.out.println("Delete button actually pressed!");

        Part toDelete = (Part)partTable.getSelectionModel().getSelectedItem();

        deletePart(toDelete);
        updateTableviews();
    }

    /**Sends user to the Add Product page*/
    public void onAddProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addProduct.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Inventory Management System - Add Product");
        stage.setScene(scene);
        stage.show();
    }

    /**Sends user to the Modify Product page and sends over information to identify the product to be modified*/
    public void onModifyProduct(ActionEvent actionEvent) throws IOException {
        Product product = (Product)productTable.getSelectionModel().getSelectedItem();
        if (product == null) {
            Alert alert = new Alert(AlertType.ERROR,
                    "No product has been selected to be modified! Please select a product to modify it.");
            alert.showAndWait();
            return;
        }
        //Identify product that will be modified in static int so that it can be called in ModifyProduct controller
        modifyProductId = product.getId();

        Parent root = FXMLLoader.load(getClass().getResource("modifyProduct.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Inventory Management System - Modify Product");
        stage.setScene(scene);
        stage.show();
    }

    /**Deletes a product after prompting user for confirmation then calls product deletion and updates table*/
    public void onDeleteProduct(ActionEvent actionEvent) {
        int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?", "Delete", JOptionPane.YES_NO_CANCEL_OPTION);

        if (reply != JOptionPane.YES_OPTION) {
            return;
        }
        Product toDelete = (Product)productTable.getSelectionModel().getSelectedItem();
        deleteProduct(toDelete);
        updateTableviews();
    }

    /**Deletes a part from the parts list*/
    public static void deletePart(Part part) {
        if (part == null) {
            Alert noPartDeleteAlert = new Alert(Alert.AlertType.ERROR,
                    "No part has been selected to delete. Please select a part to delete it.");
            noPartDeleteAlert.showAndWait();
            return;
        }
        allParts.remove(part);
    }

    /**Deletes a product from the allProducts ObservableList*/
    public static void deleteProduct(Product product) {
        if (product == null) {
            Alert alert = new Alert(AlertType.ERROR,
                    "No product has been selected to delete. Please select a product to delete it.");
            alert.showAndWait();
            return;
        }
        if (product.getAllAssociatedParts().isEmpty() != true) {
            Alert alert = new Alert(AlertType.ERROR,
                    "You cannot delete a product with associated parts. Please remove all associated parts before deleting this product.");
            alert.showAndWait();
            return;
        }
        allProducts.remove(product);
    }
}