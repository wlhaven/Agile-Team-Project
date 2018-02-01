package codeTigers.Database;

import codeTigers.BusinessLogic.Test;
import codeTigers.BusinessLogic.TestData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Wally Haven on 1/31/2018.
 */
public class AdminDB {
    private Connection mConnection;
    private Database db;

    public AdminDB() {
        db = new Database();
        mConnection = db.connect();
    }

    /************************************ ADMIN SETUP SQL *****************************************/

    /**
     * Method which removes items of a specific test from the TestData Table
     */
    public void ClearTestDataTable(int testID) { //todo: add testid parameter
        String query = "DELETE FROM Test_Data WHERE TestID = ?;";
        try {
            //todo: is this neccessary here?
            PreparedStatement stmt = mConnection.prepareStatement(query);
            stmt.setInt(1, testID);
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an array list of TestItem for
     *
     * @return QuestionsFromDb
     */
    public ArrayList<TestData> lookupTestItems(int testid) {
        ArrayList<TestData> QuestionsFromDb = new ArrayList<TestData>();
        String query = "SELECT ItemName, ItemImage FROM Test_Data WHERE TestID = ? ORDER BY ItemName ASC";
        try {
            PreparedStatement stmt = mConnection.prepareStatement(query);
            stmt.setInt(1, testid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String itemName;
                byte[] fileBytes = null;
                BufferedImage itemImage = null;
                TestData myItem = null;
                itemName = rs.getString(1);
                fileBytes = rs.getBytes(2);

                if (fileBytes == null) { // if record does not contain an image
                    myItem = new TestData(testid, itemName);
                } else if (fileBytes != null) { //if record contains an image do this
                    itemImage = ConvertByteArrayToBufferedImage(fileBytes);
                    myItem = new TestData(testid, itemName, itemImage);
                }
                QuestionsFromDb.add(myItem);
            }
            return QuestionsFromDb;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @return testsList
     */
    public ArrayList<Test> lookupTests() { //testid param goes here for sprint 2

        ArrayList<Test> testsList = new ArrayList<Test>();

        String query = "SELECT TestID, TestName FROM Test ORDER BY TestName ASC";
        try {
            PreparedStatement stmt = mConnection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String testName;
                int testID;
                testID = rs.getInt(1);
                testName = rs.getString(2);
                Test myItem = new Test(testID, testName, false, false);
                testsList.add(myItem);
            }
            return testsList;
        } catch (SQLException e) {
            System.out.println("lookuptests");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Coverts a byte array to a bufferedimage object
     * @param input
     * @return output
     * //todo: this should probably be in some sort of utilities file
     */
    public BufferedImage ConvertByteArrayToBufferedImage(byte[] input){
        // convert byte array back to BufferedImage
        try {
            InputStream in = new ByteArrayInputStream(input);
            BufferedImage output = ImageIO.read(in);
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("convert byte to buffered");
            System.out.println(e.toString());
        }
        return null;
    }

    /**
     * Converts a buffered image to a byte array
     *
     * @param image
     * @return imageBytes
     * //todo: this should probably be in some sort of utilities file
     */
    public byte[] ConvertBufferedImageToByteArray(BufferedImage image){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos );
            byte[] imageBytes = baos.toByteArray();
            return imageBytes;

        } catch(IOException ie){
            ie.printStackTrace();
            System.out.println("convert buffered to byte");
            System.out.println(ie.toString());
            return null;
        }
    }

    /**
     * method which tells DB object to insert a new test item into the testquestions table
     * this should probably
     * but it works as is.
     *
     * @param ItemName
     * @param TestID
     */
    public void InsertTestItem(String ItemName, int TestID) {
        String query = "INSERT INTO dbo.Test_Data (ItemName, TestID) values (?, ?)";
        try {

            PreparedStatement stmt = mConnection.prepareStatement(query);
            stmt.setString(1, ItemName); //param goes here for sprint 2
            stmt.setInt(2, TestID);
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("insert test item");
            e.printStackTrace();
        }
    }

    /**
     * method which tells DB object to insert a new test item into the testquestions table
     *  todo: make give itemname a unique constraint in the database
     * @param ItemName
     * @param TestID
     */
    public void InsertTestItemWithImage(String ItemName, int TestID, BufferedImage img) {
        String query = "INSERT INTO dbo.Test_Data (ItemName, TestID, ItemImage) values (?, ?, ?)";
        try {
            byte[] imageBytes = null;
            // convert image to byte array
            imageBytes = ConvertBufferedImageToByteArray(img);
            PreparedStatement stmt = mConnection.prepareStatement(query);
            stmt.setString(1, ItemName);
            stmt.setInt(2, TestID);
            stmt.setBytes (3, imageBytes);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("insert with image");
            e.printStackTrace();
        }
    }

    /**
     * Creates a new Test Record in the Tests Table
     * @param TestName
     */
    public void InsertNewTest(String TestName) {
        String query = "INSERT INTO dbo.Test (TestName) values (?)";
        try {
            PreparedStatement stmt = mConnection.prepareStatement(query);
            stmt.setString(1, TestName);
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("insertnewtest");
            e.printStackTrace();
        }
    }

    /**
     * Returns the TestID number generated by the database
     * for a test with the specified name
     * @param testName
     * @return TestID
     */
    public int lookupTestIDbyName(String testName) {
        ArrayList<TestData> QuestionsFromDb = new ArrayList<TestData>();
        String query = "SELECT TestID FROM Test WHERE TestName = ?";
        int TestID = 0;
        try {
            PreparedStatement stmt = mConnection.prepareStatement(query);
            stmt.setString(1,testName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TestID = rs.getInt(1);
            }
            return TestID;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * returns boolean of if anyone has taken the test
     */
    public boolean HasUserTakenTest(int testID) {
        String query = "SELECT TestID FROM Test_Session WHERE TestID = ?";
        int returnedInt = 0;
        try {
            PreparedStatement stmt = mConnection.prepareStatement(query);
            stmt.setInt(1, testID); //param goes here for sprint 2
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                returnedInt = rs.getInt(1);
            }
            if (returnedInt > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            db.close();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * returns boolean of if this test name has already been used
     * todo: put a unique constraint on this db column so we can delete this method
     */
    public boolean doesTestAlreadyExist(String testName) { //todo: make this a constraint in the database
        String query = "SELECT TestID FROM Test WHERE UPPER(TestName) = UPPER(?)";
        int returnedInt = 0;
        try {
            PreparedStatement stmt = mConnection.prepareStatement(query);
            stmt.setString(1, testName); //param goes here for sprint 2
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                returnedInt = rs.getInt(1);
            }
            if (returnedInt > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            db.close();
            e.printStackTrace();
            return false;
        }
    }

    /************************************ END ADMIN SETUP SQL *****************************************/

}
