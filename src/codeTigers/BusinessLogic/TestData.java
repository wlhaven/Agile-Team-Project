package codeTigers.BusinessLogic;

import java.awt.image.BufferedImage;
/**
 * Class to hold the Test Data used to create the test.
 *
 * @author Wally Haven
 * @version 11/17/2017.
 *
 * Modifications:  Removed unused code ( setters or getters )
 *              :  Added BufferedImage to support images
 */
public class TestData  {
    private final Integer itemID;
    private final String itemName;
    private BufferedImage itemImage;
    private final Integer testID;

    public TestData(Integer testID, Integer itemID, String itemName, BufferedImage itemImage) {
        this.testID = testID;
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemImage = itemImage;
    }

    //this one is used by admin setup tool
    public TestData(Integer testID, String itemName, BufferedImage itemImage) {
        this.testID = testID;
        this.itemID = null;
        this.itemName = itemName;
        this.itemImage = itemImage;
    }

    //this one is used by admin setup tool
    public TestData(Integer testID, String itemName) {
        this.testID = testID;
        this.itemID = null;
        this.itemName = itemName;
        this.itemImage = null;
    }

    public Integer getTestID() {
        return testID;
    }

    public Integer getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public BufferedImage getItemImage() {
        return itemImage;
    }

    public void setItemImage(BufferedImage myimage) {
        itemImage = myimage;
    }
}
