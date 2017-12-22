package codeTigers.Database;

import java.util.HashMap;

import codeTigers.BusinessLogic.Test;
import codeTigers.BusinessLogic.TestData;
import codeTigers.BusinessLogic.User;

import java.sql.*;
import java.io.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.IOException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 * Database class shared by the Application.
=======
 * codeTigers.BusinessLogic.Database Class for this project
 *
 * This class is shared between each of the different interfaces
 * this file contains methods which they all use and some used only by specific interfaces
 *
 * @author      Jon Grevstad, Wally, Steve, And Cameron
 * @version     11/3/2017
 * Modified     11/3/2017
 * - Added lookupUser which searches for user by distinct email
 * - Added regUser which creates new user
 * - Added SELECT SCOPE giving new users passable ID
 *
 * 11/3/2017
 *
 * @author  Jon Grevstad, Cameron Berg, Steve Daily, Wally Haven
 * @version  11/1/2017
 *
 * -Modifications:  Initial version
 * Author: Steve Daily 12/8/2017
 * add number of tests to title of test summary results
 * changed sort order of dates to show most recent test first in test pulldown menus
 */
public class Database {
    private static final String SERVER = "cisdbss.pcc.edu";
    private static final String DATABASE = "234a_CodeTigers";
    private static final String USERNAME = "234a_CodeTigers";
    private static final String PASSWORD = "me0wme0wme0w";
    private static final String CONN_STRING = "jdbc:jtds:sqlserver://" + SERVER + "/" + DATABASE;

    private static final Boolean LOCAL_DB = false;
    private static final String LOCAL_CONN_STRING = "jdbc:jtds:sqlserver://./" + DATABASE + ";instance=SQLEXPRESS;namedPipe=true";
    private static final String LOCAL_USERNAME = "";
    private static final String LOCAL_PASSWORD = "";

    private static final String userName = "root";
    private static final String password = "Blazer99";
    private static final String url = "jdbc:mysql://localhost/codetigers?autoReconnect=true&useSSL=false";

    private Connection mConnection = null;

    /**
     * This connects to database
     */
    private void connect() {
        if (mConnection != null)
            return;
        try {
            if (LOCAL_DB) {
                mConnection = DriverManager.getConnection(LOCAL_CONN_STRING, LOCAL_USERNAME, LOCAL_PASSWORD);
            } else {
                mConnection = DriverManager.getConnection(url, userName, password);
            }
            System.out.println("\nConnected to database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This closes connection to database
     */
    public void close() {
        if (mConnection != null) {
            System.out.println("Closing database connection.\n");
            try {
                mConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /*********************************** USER LOGIN/REGISTRATION SQL ******************************/

    /**
     * Look up user by email
     *
     * @param email email
     * @return User
     */
    public User lookupUser(String email) {
        connect();
        String query = "SELECT * FROM UserInfo WHERE eMail = ?";
        try {
            PreparedStatement statmt = mConnection.prepareStatement(query);
            statmt.setString(1, email);
            ResultSet results = statmt.executeQuery();
            if (results.next()) {
                return new User(
                        results.getInt("UserID"),
                        results.getString("Email"),
                        results.getString("Name"),
                        results.getString("Password"),
                        results.getString("Role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This registers a new user with for parameters
     *
     * @param email email
     * @param name name
     * @param password password
     * @param role role
     * @return user
     */
    public User regUser(String email, String name, String password, String role) {
        connect();
        int getKey;
        String inserts = "INSERT INTO UserInfo(eMail, Name, Password, Role) VALUES (?, ?, ?, ?);";
        try {
            PreparedStatement statmt = mConnection.prepareStatement(inserts,  Statement.RETURN_GENERATED_KEYS);
            statmt.setString(1, email);
            statmt.setString(2, name);
            statmt.setString(3, password);
            statmt.setString(4, "User");
            statmt.executeUpdate();
            ResultSet rs = statmt.getGeneratedKeys();;
            if (rs.next()) {
                getKey = rs.getInt(1);
                return new User(getKey, email, name, password, role);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //SQL server version
    /*  public User regUser(String email, String name, String password, String role) {
        connect();
        String inserts = "INSERT INTO UserInfo(eMail, Name, Password, Role) VALUES (?, ?, ?, ?); SELECT SCOPE_IDENTITY() AS ID;";
        try {
            PreparedStatement statmt = mConnection.prepareStatement(inserts);
            statmt.setString(1, email);
            statmt.setString(2, name);
            statmt.setString(3, password);
            statmt.setString(4, "User");
            ResultSet rs = statmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("ID"), email, name, password, role);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * Return list of tests
     *
     * @return  testList
     */
    public ArrayList<Object[]> getTestRow() {
        ArrayList<Object[]> testList = new ArrayList<>();
        connect();
        String query = "SELECT * FROM Test ORDER BY TestName ASC";

        try {
            PreparedStatement statmt = mConnection.prepareStatement(query);
            ResultSet results = statmt.executeQuery();
            while(results.next()) {
                Object[] tablRow = {
                        results.getString("TestName"),
                        results.getInt("TestID")
                };
                testList.add(tablRow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return testList;
    }

    /**
     * Map tests by string and integer
     *
     * @return  test
     */
    public HashMap<String, Integer> populateCombo() {
        HashMap<String, Integer> test = new HashMap<>();
        connect();
        String query = "SELECT * FROM Test";
        try {
            PreparedStatement statmt = mConnection.prepareStatement(query);
            ResultSet results = statmt.executeQuery();
            Test tci;

            while (results.next()) {
                tci = new Test(results.getInt(1), results.getString(2));
                test.put(tci.getTestName(), tci.getTestID());
            }
        } catch (SQLException e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return test;
    }

    /**
     * Check for test sessions taken by user
     *
     * @param userID user ID
     * @param m_testID test ID
     * @return session ID
     */
    public int getSessionID(int userID, int m_testID) {
        connect();
        int sessionID = 0;
        String query1 = "SELECT TestSessionID FROM Test_Session WHERE UserID = ? AND TestID = ?";
        try {
            PreparedStatement itemQuery = mConnection.prepareStatement(query1);
            itemQuery.setInt(1, userID);
            itemQuery.setInt(2, m_testID);
            ResultSet results = itemQuery.executeQuery();
            if (results.next()) {
                sessionID = results.getInt("TestSessionID");
            }
            return sessionID;

        } catch (SQLException e) {
            e.printStackTrace();
            close();
        }
        return -1;
    }


/*********************************** END USER LOGIN/REGISTRATION SQL **************************/




/************************************ ADMIN SETUP SQL *****************************************/

    /**
     * Method which removes items of a specific test from the TestData Table
     */
    public void ClearTestDataTable(int testID) { //todo: add testid parameter
        connect();
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
        connect();

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
        connect();
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
        try{

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
        try{
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos );
            byte[] imageBytes = baos.toByteArray();

            return imageBytes;

        }catch(IOException ie){
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
        connect();
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
        connect();

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
        connect();
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
        connect();
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
        connect();
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
            close();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * returns boolean of if this test name has already been used
     * todo: put a unique constraint on this db column so we can delete this method
     */
    public boolean doesTestAlreadyExist(String testName) { //todo: make this a constraint in the database
        connect();
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
            close();
            e.printStackTrace();
            return false;
        }
    }

    /************************************ END ADMIN SETUP SQL *****************************************/


    /**************************************** RESULTS SQL *********************************************/

    private static final String GET_EMAIL_TEST_NAME_SQL =
            "SELECT U.eMail, TS.TestSessionID  FROM UserInfo AS U " +
                    "   JOIN Test_Session AS TS ON U.UserID = TS.UserID " +
                    "     JOIN Test AS T ON T.TestID = TS.TestID And T.TestName = ? " +
                    "WHERE U.Role = 'User'" +
            "ORDER BY U.eMail ASC";
    private static final String GET_EMAIL_USER_ID_SQL =
            "SELECT DISTINCT U.eMail, U.UserID  FROM UserInfo AS U " +
              "JOIN Test_Session AS TS ON U.UserID = TS.UserID " +
            "ORDER BY U.eMail ASC";
    private static final String GET_USER_TEST_LIST_SQL =
            "SELECT T.TestName, TS.TestDate, TS.TestSessionID, T.TestID FROM Test_Session AS TS " +
               "JOIN Test AS T ON T.TestID = TS.TestID and TS.UserID = ? " +
             "ORDER BY T.TestName ASC, TS.TestDate DESC";
    private static final String GET_ITEM_NAME_ID_SQL =
            "SELECT TD.ItemID, TD.ItemName FROM Test_Data AS TD " +
                    "   JOIN Test AS T ON TD.TestID = T.TestID AND T.TestName = ? ";
    private static final String GET_TABLE_DATA_SQL =
            "SELECT TR.FirstItemID, TR.SecondItemID, TR.Choice FROM Test_Result AS TR " +
                    "WHERE TR.TestSessionID = ?";
    private static final String GET_SUMMARY_DATA_SQL =
            "SELECT TR.FirstItemID, TR.SecondItemID, TR.Choice FROM Test_Result AS TR " +
              "JOIN Test_Session AS TS ON TS.TestSessionID = TR.TestSessionID " +
                "JOIN Test AS T ON T.TestID = TS.TestID AND T.TestID = ?";
    private static final String GET_TEST_INFO_SQL =
            "SELECT T.TestName, T.TestID, COUNT(TS.TestSessionID) AS 'TestCount' FROM Test AS T " +
              "JOIN Test_Session AS TS  ON T.TestID = TS.TestID " +
            "GROUP BY T.TestName, T.TestID " +
            "ORDER BY T.TestName ASC ";

    /**
     * Read Item info from the Test_Data table for a particular test.
     * If an error occurs, a stack trace is printed to standard error and an empty list is returned.
     *
     * @param testName - name of the test
     * @return map of item ID to Item Name
     */
    public HashMap<Integer, String> readItems(String testName) {
        HashMap<Integer, String> items = new HashMap<>();
        connect();
        try (
                PreparedStatement stmt = mConnection.prepareStatement(GET_ITEM_NAME_ID_SQL);
        ) {
            stmt.setString(1, testName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                items.put(rs.getInt("ItemID"), rs.getString("ItemName"));
            }
        } catch (SQLException e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Read email address info and test session ID for a particular test
     * If an error occurs, a stack trace is printed to standard error and an empty list is returned.
     *
     * @param testName - name of the test
     * @return the map of email address to test session IDs
     */
    public LinkedHashMap<String, Integer> readEmail(String testName) {
        LinkedHashMap<String, Integer> email = new LinkedHashMap<>();
        connect();
        try (
                PreparedStatement stmt = mConnection.prepareStatement(GET_EMAIL_TEST_NAME_SQL);
        ) {
            stmt.setString(1, testName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                email.put(rs.getString("eMail"), rs.getInt("TestSessionID"));
            }
        } catch (SQLException e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return email;
    }

    /**
     * Read email address and User ID for any user that has taken a test
     * If an error occurs, a stack trace is printed to standard error and an empty list is returned.
     *
     * @return the map of email address to User IDs
     */
    public LinkedHashMap<String, Integer> readUserInfo() {
        LinkedHashMap<String, Integer> email = new LinkedHashMap<>();
        connect();
        try (
                PreparedStatement stmt = mConnection.prepareStatement(GET_EMAIL_USER_ID_SQL);
        ) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                email.put(rs.getString("eMail"), rs.getInt("UserID"));
            }
        } catch (SQLException e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return email;
    }

    /**
     * Read Test info for a particular user
     * If an error occurs, a stack trace is printed to standard error and an empty list is returned.
     *
     * @param userID - ID of user we want test info for
     * @return list of test info - test name, test date, test session ID
     */
    public ArrayList<Object[]> readUserTestInfo(Integer userID) {
        ArrayList<Object[]> testInfo = new ArrayList<>();
        connect();
        try (
                PreparedStatement stmt = mConnection.prepareStatement(GET_USER_TEST_LIST_SQL);
        ) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] tableRow = {
                        rs.getString("TestName"),
                        rs.getString("TestDate"),
                        rs.getInt("TestSessionID"),
                        rs.getInt("TestID")
                };
                testInfo.add(tableRow);
            }
        } catch (SQLException e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return testInfo;
    }

    /**
     * Read Test Info:  Test Name, Test ID, and Test Count for all tests that have been taken at least once
     * If an error occurs, a stack trace is printed to standard error and an empty list is returned.
     *
     * @return list of test info
     */
    public ArrayList<Object[]> readSummaryTestInfo() {
        ArrayList<Object[]> testInfo = new ArrayList<>();
        connect();
        try (
                PreparedStatement stmt = mConnection.prepareStatement(GET_TEST_INFO_SQL);
        ) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] tableRow = {
                        rs.getString("TestName"),
                        rs.getInt("TestID"),
                        rs.getInt("TestCount")
                };
                testInfo.add(tableRow);
            }
        } catch (SQLException e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return testInfo;
    }

    /**
     * Read test data from the Test_Data table for a particular test session ID
     * If an error occurs, a stack trace is printed to standard error and an empty list is returned.
     *
     * @param testSessionId - name of the test
     * @return the list of test data read
     */
    public ArrayList<Object[]> readTableData(Integer testSessionId) {
        ArrayList<Object[]> testData = new ArrayList<>();
        connect();
        try (
                PreparedStatement stmt = mConnection.prepareStatement(GET_TABLE_DATA_SQL);
        ) {
            stmt.setInt(1, testSessionId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] tableRow = {
                        rs.getInt("FirstItemID"),
                        rs.getInt("SecondItemID"),
                        rs.getInt("Choice")
                };
                testData.add(tableRow);
            }
        } catch (SQLException e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return testData;
    }

    /**
     * Read test data from the Test_Data table for a particular test session ID
     * If an error occurs, a stack trace is printed to standard error and an empty list is returned.
     *
     * @param testId - ID number of the test
     * @return the list of test data read
     */
    public ArrayList<Object[]> readSummaryTableData(Integer testId) {
        ArrayList<Object[]> testData = new ArrayList<>();
        connect();
        try (
                PreparedStatement stmt = mConnection.prepareStatement(GET_SUMMARY_DATA_SQL);
        ) {
            stmt.setInt(1, testId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] tableRow = {
                        rs.getInt("FirstItemID"),
                        rs.getInt("SecondItemID"),
                        rs.getInt("Choice")
                };
                testData.add(tableRow);
            }
        } catch (SQLException e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return testData;
    }


/********************************** END RESULTS SQL ***********************************************************/


/********************************** TEST SQL ************************************************/

    private static final String GET_ITEMS_SQL =
            "SELECT TestID, ItemID, ItemName, ItemImage FROM Test_Data\n" +
                    "WHERE TestID = ?;";

    //Below query is for use with SQL server
   /* private static final String CREATE_TEST_SESSION_SQL =
            "INSERT INTO Test_Session(UserID, TestID) VALUES(?,?); "  +
            " SELECT SCOPE_IDENTITY() AS ID;";*/

    private static final String CREATE_TEST_SESSION_SQL =
            "INSERT INTO Test_Session(UserID, TestID) VALUES(?,?); " ;

    private static final String INSERT_RESULTS_SQL =
            "INSERT INTO Test_Result(Choice, TestSessionID, FirstItemID, SecondItemID)\n" +
            "VALUES(?,?,?,?);";

    /**
     * Read the database to retrieve the items needed to build the test questions
     *
     * @return itemList   ArrayList holding the item data from Test_Data table
     * @author Wally Haven
     * @version 11/17/2017
     *
     * Modification:  Added image to query results.
     */
    public ArrayList<TestData> readTestItem(int m_testID) {
        ArrayList itemList = new ArrayList<TestData>();
        connect();
        try {
            PreparedStatement itemQuery = mConnection.prepareStatement(GET_ITEMS_SQL);
            itemQuery.setInt(1, m_testID);
            ResultSet rst = itemQuery.executeQuery();
            while (rst.next()) {
                InputStream stream = rst.getBinaryStream("ItemImage");
                BufferedImage image = null;
                try {
                    if (stream != null)
                        image = ImageIO.read(stream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                TestData testQuestion = new TestData(rst.getInt("TestID"),
                        rst.getInt("ItemID"), rst.getString("ItemName"),
                        image);
                itemList.add(testQuestion);
            }
            return itemList;
        } catch (SQLException e) {
            e.printStackTrace();
            close();
        }
        return null;
    }

    /**
     * Create the test session for the user
     *
     * @param userID  userID of person taking the test
     * @param m_testID  current test id
     * @author Wally Haven
     * @version 10/27/2017
     */
    public int createTestSession(int userID, int m_testID) {
        connect();
        try {
            PreparedStatement itemQuery = mConnection.prepareStatement(CREATE_TEST_SESSION_SQL, Statement.RETURN_GENERATED_KEYS);
            itemQuery.setInt(1, userID);
            itemQuery.setInt(2, m_testID);
            itemQuery.executeUpdate();
            ResultSet rs = itemQuery.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            close();
        }
        return 0;
    }
/*  SQL server version of query
    public int createTestSession(int userID, int m_testID) {
        connect();
        try {
            PreparedStatement itemQuery = mConnection.prepareStatement(CREATE_TEST_SESSION_SQL);
            itemQuery.setInt(1, userID);
            itemQuery.setInt(2, m_testID);
            ResultSet rs = itemQuery.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            close();
        }
        return 0;
    }
*/
    /**
     * Insert the test results into the Test Results table on the database.
     *
     * @param choice        The score for the question
     * @param testSessionID Test Session ID
     * @param firstItem     First Item in the pair of the question
     * @param secondItem    Second Item in the pair of the question
     * @author Wally Haven
     * @version 10/27/2017
     */
    public void insertResults(int choice, int testSessionID, int firstItem, int secondItem) {
        connect();
        try {
            PreparedStatement itemQuery = mConnection.prepareStatement(INSERT_RESULTS_SQL);
            itemQuery.setInt(1, choice);
            itemQuery.setInt(2, testSessionID);
            itemQuery.setInt(3, firstItem);
            itemQuery.setInt(4, secondItem);
            itemQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            close();
        }
    }

/********************************** END TEST SETUP AND RESULTS SQL **********************************/

    @Override
    protected void finalize() {
        close();
    }

}

