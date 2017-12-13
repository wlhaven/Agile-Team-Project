package codeTigers.BusinessLogic;
import codeTigers.Database.Database;

import java.util.*;

import static java.lang.Math.round;

/*
 *  This class processes the data from the DB to present to the codeTigers.BusinessLogic.UI
 *
 * @author  Steve Daily
 * @version 2017.11.29
 *
 * Modifications:
 * Author: Steve Daily 11/15/2017
 * add maps for email to test session ID and Item ID to Item mName
 * add method to build results data table from data read from Test_Results in DB
 * modified getEmailAddr to return a tree set so the email list will be sorted
 * modified buildResultsTable to calculate table values in one loop instead of nested loops
  * Author: Steve Daily 11/29/2017
 * add support for multiple tests
 * Author: Steve Daily 12/8/2017
 * add display of test summary results when no user is selected
 * add number of tests to title of test summary results
 * changed sort order of dates to show most recent test first in test pulldown menus
 */

public class ResultsDisplay {

    private class TestInfo {
        private String mName;
        private String mDate;
        private int mTestSessionID;
        private int mTestID;

        TestInfo() {
            mName = null;
            mDate = null;
            mTestSessionID = -1;
            mTestID = -1;
        }
        TestInfo(String name, String date, int testSessionID, int TestID) {
            mName = name;
            mDate = date;
            mTestSessionID = testSessionID;
            mTestID = TestID;
        }
        String getName() { return mName; }
        String getDate() { return mDate; }
        int getTestSessionID() { return mTestSessionID; }
        int getTestID() { return mTestID; }
    }

    private Database rddb = new Database();
    private String selectedTestName;
    private LinkedHashMap<String, Integer> emailTestSession;     // Map email address to test session id
    private LinkedHashMap<String, Integer> emailUserID;          // Map email address to user id
    private HashMap<Integer, String> itemIdName;                 // Map item ID to item mName
    private ArrayList<TestInfo> testInfoList = new ArrayList<>();// List of tests
    HashMap<String, Integer> testNameToCountMap;

    private ArrayList<Object[]> tableDataList = new ArrayList<>();

    private ArrayList<Object[]> defaultTableData;

    public ResultsDisplay() {
        // get list of users
        emailUserID = rddb.readUserInfo();

        Object[] tableRow = {null, null, null, null, null};
        tableDataList.add(tableRow);
        defaultTableData =  tableDataList;
    }

    /**
     * Set test mName and collect data for test
     *
     * @param testName - mName of test selected for result display
     */
    public void setTest(String testName) {
        selectedTestName = testName;
        emailTestSession = rddb.readEmail(testName);
        itemIdName = rddb.readItems(testName);

        for(String itemName : itemIdName.values()) {
            Object[] tableRow = {itemName, null, null, null, null};
            tableDataList.add(tableRow);
        }
        defaultTableData =  tableDataList;
    }

    /**
     * Provide column names
     *
     * @return the array of column names
     */
    public String[] getResultsColumnNames() {
        return new String[] {"Item", "Wins", "Losses", "Ties", "Score"};
    }

    /**
     * Provide column names
     *
     * @return the array of column names
     */
    public String[] getSummaryColumnNames() {
        return new String[] {"Item", "% Wins", "% Losses", "% Ties", "Score"};
    }

    /**
     * Provide column number
     *
     * @return the number of columns
     */
    public int getColumnNumber() { return 5; }

    /**
     * Get email address data from DB
     *
     * @return a set containing the sorted email addresses
     */
    public Set<String> getEmailAddr(){
        return emailTestSession.keySet();
    }

    /**
     * Get email address data from DB
     *
     * @return a set containing the sorted email addresses
     */
    public Set<String> getUserInfo() {
        return emailUserID.keySet();
    }

    /**
     * Provide test session info for a user
     *
     * @param userEmail - string holding user email address for look up in DB
     * @return the array of test names with dates appended to test names that occur multiple times
     */
    public ArrayList<String> getTestInfo(String userEmail) {
        testInfoList.clear();
        if (userEmail.equals("")) {
            return null;
        } else {
            ArrayList<String> testName = new ArrayList<>();
            ArrayList<Object[]> testInfoTable = rddb.readUserTestInfo(emailUserID.get(userEmail));
            for(Object[] test : testInfoTable) {
                testInfoList.add(new TestInfo((String) test[0], (String) test[1], (Integer) test[2], (Integer) test[3]));
            }
            Integer testListLength = testInfoList.size();
            boolean matchPrevious = false;
            boolean matchNext = false;

            for(Integer i = 0; i < testListLength; i++) {
                TestInfo t = testInfoList.get(i);
                // list is sorted from DB, check to see if consecutive test names match
                if( i < testListLength - 1 && testInfoList.get(i + 1).getName().equals(t.getName())) {
                    matchPrevious = true;
                    matchNext = true;
                } else {
                    matchNext = false;
                }
                if (matchPrevious || matchNext) {
                    testName.add(t.getName() + "-" + t.getDate());
                    if(matchNext == false) {
                        matchPrevious = false;
                    }
                } else {
                    testName.add(t.getName());
                }
            }
            return testName;
        }
    }

    /**
     * Provide table data for a user
     *
     * @param userEmail - string holding user email address for look up in DB
     * @return the array of arrays of table data
     */
    public ArrayList<Object[]> getTableData(String userEmail) {
        if (userEmail.equals("")) {
            return defaultTableData;
        } else {
            tableDataList = rddb.readTableData(emailTestSession.get(userEmail));
            ArrayList<Object[]> resultsTable = buildResultsTable();
            resultsTable.sort((o1, o2) -> (Integer)o2[4] - (Integer)o1[4]);
            return resultsTable;
        }
    }

    /**
     * Provide test info for the summary results table
     *
     * @return the array of arrays of test info for the summary display
     */
    public ArrayList<Object[]> getSummaryTestInfo() {
        testInfoList.clear();
        testNameToCountMap = new HashMap<>();
        ArrayList<Object[]> sumTestIn = rddb.readSummaryTestInfo();
        for(Object[] row : sumTestIn) {
            testNameToCountMap.put((String)row[0], (Integer)row[2]);
            testInfoList.add(new TestInfo((String) row[0], "", -1, (Integer) row[1]));
        }
        return sumTestIn;
    }

    /**
     * Provide number of times a test has been taken
     *
     * @param testName - name of test to use as key into map
     * @return the count of the number of times the test has been taken
     */
    public Integer getTestCount(String testName) {
        return testNameToCountMap.get(testName);
    }

    /**
     * Provide number of times a test has been taken
     *
     * @param testIndex - index into test info list
     * @return the count of the number of times the test has been taken
     */
    public Integer getTestCount(Integer testIndex) {
        String tName = testInfoList.get(testIndex).getName();
        return getTestCount(tName);
    }

    /**
     * Provide table data for a test
     *
     * @param testIndex - index into list holding test session info
     * @return the array of arrays of table data
     */
    public ArrayList<Object[]> getTableData(Integer testIndex) {
        if (testIndex == -1 || testIndex == 0) {
            return defaultTableData;
        } else {
            testIndex -= 1;   //  adjust index from pulldown menu since top menu item is blank
            itemIdName = rddb.readItems(testInfoList.get(testIndex).getName());
            tableDataList = rddb.readTableData(testInfoList.get(testIndex).getTestSessionID());
            ArrayList<Object[]> resultsTable = buildResultsTable();
            resultsTable.sort((o1, o2) -> (Integer)o2[4] - (Integer)o1[4]);
            return resultsTable;
        }
    }

    /**
     * Provide summary table data for a test
     *
     * @param testIndex - index into list holding test session info
     * @return the array of arrays of summary table data
     */
     public ArrayList<Object[]> getSummaryTableData(Integer testIndex) {
         // if (testIndex == -1 || testIndex == 0) {
         if (testIndex == -1) {
            return defaultTableData;
        } else {
            testIndex -= 1;   //  adjust index from pulldown menu since top menu item is blank
            itemIdName = rddb.readItems(testInfoList.get(testIndex).getName());
            tableDataList = rddb.readSummaryTableData(testInfoList.get(testIndex).getTestID());
            ArrayList<Object[]> resultsTable = buildResultsTable();
            // int tableSize = resultsTable.size();
            ArrayList<Object[]> summaryResultsTable = new ArrayList<>();
            for(Object[]row : resultsTable){
                // Sum wins, losses, and ties to get total number of tests for the item
                int rowSum = (int)row[1] + (int)row[2] + (int)row[3];
                for(int i = 1; i < 4; i++) {
                    row[i] = round(((int) row[i] * 100.0) / rowSum);
                }
                summaryResultsTable.add(row);
            }
            summaryResultsTable.sort((o1, o2) -> (Integer)o2[4] - (Integer)o1[4]);
            return summaryResultsTable;
        }
    }

    private static final int WINS = 0;
    private static final int LOSSES = 1;
    private static final int TIES = 2;
    private static final int SCORE = 3;
    private static final int FIRST_ITEM = 0;
    private static final int SECOND_ITEM = 1;
    private static final int CHOICE = 2;

    /**
     * Provide table data for a user
     *
     * @return the list of arrays of result table
     */
    private ArrayList<Object[]> buildResultsTable() {
        ArrayList<Object[]> resultsTable = new ArrayList<>();
        HashMap<Integer, Integer[]> resultsSums = new HashMap<>();

        for (int item : itemIdName.keySet()) {
            resultsSums.put(item, new Integer[]{0, 0, 0, 0});
        }
        for (Object[] row : tableDataList) {
            switch ((Integer)row[CHOICE]) {
                case 1:
                    Integer[] tmpArray = resultsSums.get(row[FIRST_ITEM]);
                    tmpArray[WINS]++;
                    tmpArray[SCORE] += (Integer)row[CHOICE];
                    resultsSums.put((Integer)row[FIRST_ITEM], tmpArray);
                    tmpArray = resultsSums.get(row[SECOND_ITEM]);
                    tmpArray[LOSSES]++;
                    tmpArray[SCORE] -= (Integer)row[CHOICE];
                    resultsSums.put((Integer)row[SECOND_ITEM], tmpArray);
                    break;
                case -1:
                    tmpArray = resultsSums.get(row[FIRST_ITEM]);
                    tmpArray[LOSSES]++;
                    tmpArray[SCORE] += (Integer)row[CHOICE];
                    resultsSums.put((Integer)row[FIRST_ITEM], tmpArray);
                    tmpArray = resultsSums.get(row[SECOND_ITEM]);
                    tmpArray[WINS]++;
                    tmpArray[SCORE] -= (Integer)row[CHOICE];
                    resultsSums.put((Integer)row[SECOND_ITEM], tmpArray);
                    break;
                case 0:
                    tmpArray = resultsSums.get(row[FIRST_ITEM]);
                    tmpArray[TIES]++;
                    resultsSums.put((Integer)row[FIRST_ITEM], tmpArray);
                    tmpArray = resultsSums.get(row[SECOND_ITEM]);
                    tmpArray[TIES]++;
                    resultsSums.put((Integer)row[SECOND_ITEM], tmpArray);
                    break;
                default:
                    break;
            }
        }

        for (int item : itemIdName.keySet()) {
            Integer[] tmpArray = resultsSums.get(item);
            Object[] tableRow = {itemIdName.get(item),
                                   tmpArray[WINS], tmpArray[LOSSES], tmpArray[TIES], tmpArray[SCORE]};
            resultsTable.add(tableRow);
        }

        return resultsTable;
    }
}
