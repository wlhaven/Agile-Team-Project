package codeTigers.Database;

import codeTigers.BusinessLogic.Test;
import codeTigers.BusinessLogic.TestData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Wally Haven on 1/31/2018.
 */
public class TestDB {
    private final Connection mConnection;
    private final Database db;

    private static final String GET_ITEMS_SQL =
            "SELECT TestID, ItemID, ItemName, ItemImage FROM Test_Data\n" +
                    "WHERE TestID = ?;";

    private static final String CREATE_TEST_SESSION_SQL =
            "INSERT INTO Test_Session(UserID, TestID) VALUES(?,?); ";

    private static final String INSERT_RESULTS_SQL =
            "INSERT INTO Test_Result(Choice, TestSessionID, FirstItemID, SecondItemID)\n" +
                    "VALUES(?,?,?,?);";


    //Constructor
    public TestDB() {
        db = new Database();
        mConnection = db.connect();
    }


    /**
     * Read the database to retrieve the items needed to build the test questions
     *
     * @return itemList   ArrayList holding the item data from Test_Data table
     * @author Wally Haven
     * @version 11/17/2017
     * <p>
     * Modification:  Added image to query results.
     */
    public ArrayList<TestData> readTestItem(int m_testID) {
        ArrayList<TestData> itemList = new ArrayList<TestData>();
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
            db.close();
        }
        return null;
    }

    /**
     * Create the test session for the user
     *
     * @param userID   userID of person taking the test
     * @param m_testID current test id
     * @author Wally Haven
     * @version 12/21/2017
     * Modifications:  Changed query to use Return_Generated_Keys instead of
     * Select Scope_Identity() to make query compatible with both MYSQL and SQL Server.
     */
    public int createTestSession(int userID, int m_testID) {

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
            db.close();
        }
        return 0;
    }

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
        try {
            PreparedStatement itemQuery = mConnection.prepareStatement(INSERT_RESULTS_SQL);
            itemQuery.setInt(1, choice);
            itemQuery.setInt(2, testSessionID);
            itemQuery.setInt(3, firstItem);
            itemQuery.setInt(4, secondItem);
            itemQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            db.close();
        }
    }

    /**
     * Return list of tests
     *
     * @return testList
     */
    public ArrayList<Object[]> getTestRow() {
        ArrayList<Object[]> testList = new ArrayList<>();
        String query = "SELECT * FROM Test ORDER BY TestName ASC";

        try {
            PreparedStatement statmt = mConnection.prepareStatement(query);
            ResultSet results = statmt.executeQuery();
            while (results.next()) {
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
     * @return test
     */
    public HashMap<String, Integer> populateCombo() {
        HashMap<String, Integer> test = new HashMap<>();
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
     * @param userID   user ID
     * @param m_testID test ID
     * @return session ID
     */
    public int getSessionID(int userID, int m_testID) {
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
            db.close();
        }
        return -1;
    }


    public void close() {
        db.close();
    }
}


