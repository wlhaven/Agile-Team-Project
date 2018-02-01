package codeTigers.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Wally Haven on 1/31/2018.
 */
public class ResultsDB {
    private Connection mConnection;
    private Database db;

    private static final String GET_EMAIL_TEST_NAME_SQL =
            "SELECT U.eMail, TS.TestSessionID  FROM UserInfo AS U " +
                    "   JOIN Test_Session AS TS ON U.UserID = TS.UserID " +
                    "   JOIN Test AS T ON T.TestID = TS.TestID And T.TestName = ? " +
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
                    "  JOIN Test AS T ON TD.TestID = T.TestID AND T.TestName = ? ";
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


    //Constructor
    public ResultsDB() {
        db = new Database();
        mConnection = db.connect();
    }

    /**
     * Read Item info from the Test_Data table for a particular test.
     * If an error occurs, a stack trace is printed to standard error and an empty list is returned.
     *
     * @param testName - name of the test
     * @return map of item ID to Item Name
     */
    public HashMap<Integer, String> readItems(String testName) {
        HashMap<Integer, String> items = new HashMap<>();
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
}
