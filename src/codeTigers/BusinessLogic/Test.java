package codeTigers.BusinessLogic;

import java.util.ArrayList;

/**
 * Class that hold the data from the Test table.
 *
 * @author Wally Haven
 * @version 12/04/2017.
 * <p>
 * Modifications: Refactored out unused code.
 */
public class Test {
    private Integer testID;
    private String testName;
    private boolean modified = false;
    private boolean newtest = false;

    private ArrayList<TestData> testItems = new ArrayList<>();

    public Test(Integer testID) {
        this(testID,null);
    }

    public Test(Integer testID, String testName) {
        this(testID, testName, false, false);
    }

    public Test(Integer testID, String testName, boolean modified, boolean newtest) {
        this.testID = testID;
        this.testName = testName;
        this.modified = modified;
        this.newtest = newtest;
    }

    public Integer getTestID() {
        return testID;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setTestID(Integer testID) {
        this.testID = testID;
    }

    public void setModified(boolean bool) {
        modified = bool;
    }

    public boolean isModified() {
        return modified;
    }

    public void setTestItems(ArrayList<TestData> testItems) {
        this.testItems = testItems;
    }

    public ArrayList<TestData> getTestItems() {
        return testItems;
    }

    public void setNewtest(boolean isnew) { newtest = isnew; }

    public boolean isNewtest() { return newtest; }
}

