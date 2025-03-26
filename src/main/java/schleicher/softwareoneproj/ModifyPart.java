package schleicher.softwareoneproj;

/**
 * @author William Zapata9Schleicher at WGU
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

import static schleicher.softwareoneproj.Inventory.*;

/**This class acts as a controller for the Modify Part window, which allows users to modify existing parts*/
public class ModifyPart {
    /**GUI Element*/
    public RadioButton modifyPartInHouse;
    /**GUI Element*/
    public RadioButton modifyPartOutsourced;
    /**GUI Element*/
    public ToggleGroup modifyPart;
    /**GUI Element*/
    public TextField modifyID;
    /**GUI Element*/
    public TextField modifyName;
    /**GUI Element*/
    public TextField modifyInv;
    /**GUI Element*/
    public TextField modifyPrice;
    /**GUI Element*/
    public TextField modifyMax;
    /**GUI Element*/
    public TextField modifyMacID;
    /**GUI Element*/
    public TextField modifyMin;
    /**GUI Element*/
    public Label modifyMacIDToggle;
    /**GUI Element*/
    public Label modifyErrorMessage;
    /**GUI Element*/
    private Stage stage;
    /**GUI Element*/
    private Scene scene;
    /**GUI Element*/
    public Button modifyPartSave;
    /**GUI Element*/
    public Button modifyPartCancel;
    /**Keeps track of the index of the part that is being modified*/
    int modifyIndex = 0;

    /**Populates the text fields with the part's existing information*/
    public void initialize(){
        modifyErrorMessage.setText("");

        //Populate the machine ID or the company name fields
            //(depending on if object is InHouse or OutSourced)
            // inHouse == 0, outSourced == 1
        if (Inventory.modifyTypeToggle == 0)
        {
            InHouse toModifyInHouse = (InHouse) toModify;
            int modifyMachineIDTemp = toModifyInHouse.getMachineID();
            modifyMacID.setText("" + modifyMachineIDTemp);
            modifyPartInHouse.setSelected(true);
            modifyMacIDToggle.setText("Machine ID");
        }
        else if (Inventory.modifyTypeToggle == 1)
        {
            OutSourced toModifyOutSourced = (OutSourced) toModify;
            String modifyCoName = toModifyOutSourced.getCoName();
            modifyMacID.setText(modifyCoName);
            modifyPartOutsourced.setSelected(true);
            modifyMacIDToggle.setText("Co. Name");
        }

        //Populate other fields that are same for InHouse / OutSourced
        int modifyIDtemp = toModify.getId();
        modifyID.setText("" + modifyIDtemp + " (auto-generated)");

        String modifyNameTemp = toModify.getName();
        modifyName.setText(modifyNameTemp);

        int modifyInvTemp = toModify.getStock();
        modifyInv.setText("" + modifyInvTemp);

        double modifyPriceTemp = toModify.getPrice();
        modifyPrice.setText("" + modifyPriceTemp);

        int modifyPriceMaxTemp = toModify.getMax();
        modifyMax.setText("" + modifyPriceMaxTemp);

        int modifyPriceMinTemp = toModify.getMin();
        modifyMin.setText("" + modifyPriceMinTemp);

        modifyIndex = lookupPartIndex(toModify.getId());
        //System.out.println(modifyIndex);
    }

    /**Checks if fields are valid and saves the changes to the part*/
    public void onModifyPartSave(ActionEvent event) throws IOException {

        //Are you sure of modification prompt
        int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to modify this part? The part will be updated with the values you used in the text boxes if they are valid.", "Modify!", JOptionPane.YES_NO_CANCEL_OPTION);

        if (reply == JOptionPane.YES_OPTION) {
            //System.out.println("HEY! We did it! Modifying!");
        }
        else {
            return;
        }
        int modifyPartError = 0;

        int index = 0;
        int id = toModify.getId();
        String partName = "";
        int inv = 0;
        double price = 0.0;
        int max = 0;
        int min = 0;
        int machineID = 0;
        String coName = "";

        partName = modifyName.getText();
        if (modifyName.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please enter a part name in the part field.");
            alert.showAndWait();
            modifyPartError = 1;
            return;
        }
        try {
            inv = Integer.parseInt(modifyInv.getText());
        }
        catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please use an int in the Inv field.");
            alert.showAndWait();
            modifyPartError = 1;
            return;
        }
        try {
            price = Double.parseDouble(modifyPrice.getText());
        }
        catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please use an int or double in the Price/Cost field.");
            alert.showAndWait();
            modifyPartError = 1;
            return;
        }

        //Fill the max field int
        try {
            max = Integer.parseInt(modifyMax.getText());
        }
        catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please use an int in the Max field.");
            alert.showAndWait();
            modifyPartError = 1;
            return;
        }

        //Fill the min int
        try {
            min = Integer.parseInt(modifyMin.getText());
        }
        catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please use an int in the Min field.");
            alert.showAndWait();
            modifyPartError = 1;
            return;
        }

        //Error if max not equal or greater than min
        if (min > max) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please make the Max equal to or greater than the Min field.");
            alert.showAndWait();
            modifyPartError = 1;
            return;
        }

        //A check and error message to make sure inventory is at or between min and max values
        if (inv > max || inv < min) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please make sure the Inv field is at or between the min and the max.");
            alert.showAndWait();
            modifyPartError = 1;
            return;
        }

        //Create an updated Part and save it into the partTable of the correct Outsourced or InHouse type
        if (Inventory.modifyTypeToggle == 0) {
            InHouse modified = new InHouse(id, partName, inv, price, min, max);
            try {
                machineID = Integer.parseInt(modifyMacID.getText());
            }
            catch (Exception exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Please use an int in the Machine ID Field.");
                alert.showAndWait();
                modifyPartError = 1;
                return;
            }
            //Get machineID from text and set
            modified.setMachineID(machineID);
            updatePart(modifyIndex, (InHouse)modified);
        }

        else if (Inventory.modifyTypeToggle == 1) {
            OutSourced modified = new OutSourced(id, partName, inv, price, min, max);
            try {
                if (modifyMacID.getText().equals("")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Please enter a Company Name in the Co. Name field.");
                    alert.showAndWait();
                    modifyPartError = 1;
                    return;
                }
            }
            catch (Exception exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Please enter a Company Name in the Co. Name field.");
                alert.showAndWait();
                modifyPartError = 1;
                return;
            }
            //Get coName from text and set
            modified.setCoName(modifyMacID.getText());
            updatePart(modifyIndex, modified);
            //Some diagnostic code:
            //System.out.println(modifyMacID.getText());
            /*OutSourced s = lookupPart(id);
            s.setCoName("hey");
            System.out.println("going in " + modified.getCoName());
             */
        }

        Parent root = FXMLLoader.load(getClass().getResource("mainView.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Inventory Management System - Home");
        stage.setScene(scene);
        stage.show();
    }

    /**Cancels modification and return to the main screen*/
    public void onModifyPartCancel(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("mainView.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Inventory Management System - Home");
        stage.setScene(scene);
        stage.show();
    }

    /**Sets label to Machine ID if InHouse  is selected*/
    public void onModifyPartInHouse(ActionEvent actionEvent) {
        modifyMacIDToggle.setText("Machine ID");
        Inventory.modifyTypeToggle = 0;
    }
    /**Sets label to Co. Name if OutSourced radio button is is selected*/
    public void onModifyPartOutSourced(ActionEvent actionEvent) {
        modifyMacIDToggle.setText("Co. Name");
        Inventory.modifyTypeToggle = 1;
    }
}