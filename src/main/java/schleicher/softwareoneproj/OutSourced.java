package schleicher.softwareoneproj;

/**
 * @author William Zapata9Schleicher at WGU
 */

/**This class extends the Part class, and adds the functionality of identifying a part as OutSourced and as containing a company name field*/
public class OutSourced extends Part {
    private String coName;
    /**This is the OutSourced constructor
     * @param partID
     * @param partName
     * @param partInventoryLevel
     * @param partPricePerUnit
     * @param min
     * @param max */
    public OutSourced(int partID, String partName, int partInventoryLevel, double partPricePerUnit, int min, int max) {
        super(partID, partName, partPricePerUnit, partInventoryLevel, min, max);
    }
    /**Gets the company name
     * @return the company name String*/
    public String getCoName() {return coName;}
    /**Sets the company name
     * @param coName*/
    public void setCoName(String coName){this.coName = coName;}
}
