package schleicher.softwareoneproj;

/**
 * @author William Zapata9Schleicher at WGU
 */

/**This class extends the Part class, indicating that a part is in-house, and adds a machineID int feature.*/
public class InHouse extends Part {
    private int machineID;
    /**Constructor for the InHouse class
     * @param id
     * @param name
     * @param stock
     * @param price
     * @param min
     * @param max*/
    public InHouse(int id, String name, int stock, double price, int min, int max) {
        super(id, name, price, stock, min, max);
    }

    /**Getter for the InHouse machineID
     * @return the machineID int*/
    public int getMachineID() {
        return machineID;
    }

    /**Setter for the inHouse machineID
     * @param machineID */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }
}
