package schleicher.softwareoneproj;

/**
 * @author William Zapata9Schleicher at WGU
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;

/**This class defines the Product fields and includes an internal ObservableList of parts*/
public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**Product constructor
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param max
     * @param min */
    public Product(int id, String name, double price, int stock, int max, int min){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**Get the product's id
     * @return the id*/
    public int getId() {
        return id;
    }

    /**Set the product's id
     * @param id */
    public void setId(int id) {
        this.id = id;
    }

    /**Get the product's name
     * @return the name*/
    public String getName() {
        return name;
    }

    /**Set the product's name
     * @param name */
    public void setName(String name) {
        this.name = name;
    }

    /**Get the product's price
     * @return the price*/
    public double getPrice() {
        return price;
    }

    /**Set the product's price
     * @param price */
    public void setPrice(double price) {
        this.price = price;
    }

    /**Get the product's inventory level
     * @return the stock*/
    public int getStock() {
        return stock;
    }

    /**Sets the product's inventory level
     * @param stock */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**Gets the product's min
     * @return the min*/
    public int getMin() {
        return min;
    }

    /**Sets the product's min
     * @param min */
    public void setMin(int min) {
        this.min = min;
    }

    /**Gets the product's max
     * @return the max*/
    public int getMax() {
        return max;
    }

    /**Sets the product's max
     * @param max*/
    public void setMax(int max) {
        this.max = max;
    }

    /**Adds a part to the product's associatedParts internal ObservableList
     * @param part*/
    public void addAssociatedPart (Part part){
        associatedParts.add(part);
    }

    /**Deletes an associated part from the Product's internal associatedPart list*/
    public boolean deleteAssociatedPart (Part selectedAssociatedPart){
        if (selectedAssociatedPart == null) {
            return false;
        }

        //A confirmation dialogue for removing an associated part
        int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this associated part?", "Remove Part", JOptionPane.YES_NO_CANCEL_OPTION);
        if (reply != JOptionPane.YES_OPTION) {
            return false;
        }

        associatedParts.remove(selectedAssociatedPart);
        return true;
    }


    /**Returns all associated Parts of a certain Product*/
    public ObservableList<Part> getAllAssociatedParts(){
        return FXCollections.observableArrayList(associatedParts);
    }

}
