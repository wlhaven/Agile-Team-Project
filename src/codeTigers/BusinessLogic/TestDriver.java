package codeTigers.BusinessLogic;

import codeTigers.Database.Database;
import codeTigers.Database.TestDB;
import codeTigers.Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Driver Class to populate the UserTakingTest UI
 *
 * @author Wally Haven
 * @version 11/25/2017.
 * <p>
 * Modifications:  Reworked CreateQuestions method to randomize test question positions
 * Add constructor parameter testID to support multiple tests.
 */
public class TestDriver {
    private final ArrayList<TestData> testItem;
    private final ArrayList<TestQuestion> createTestList;
    private final TestDB db;

    /**
     * Constructor
     */
    public TestDriver(int testID) {
        createTestList = new ArrayList<>();
        db = new TestDB();
        testItem = db.readTestItem(testID);
    }

    /**
     * Create the test questions based on the list generated from the database.
     * Avoid positional bias by swapping item order in random fashion. ie (Red/Blue could be Blue/Red)
     * and shuffling test order from the database.
     */
    public ArrayList<TestQuestion> createTestQuestions() {
        Random random = new Random();
        Collections.shuffle(testItem);    // shuffle the test data
        for (int i = 0; i < testItem.size() - 1; i++) {
            for (int j = i + 1; j < testItem.size(); j++) {
                Boolean flag = false;
                do {
                    int r = random.nextInt(2);
                    TestData q1 = testItem.get(r == 0 ? i : j);
                    TestData q2 = testItem.get(r == 0 ? j : i);
                    if (q1 != q2) {
                        createTestList.add(new TestQuestion(q1.getItemID(), q1.getItemName(), q1.getItemImage(), q2.getItemID(),
                                q2.getItemName(), q2.getItemImage()));
                        flag = true;
                    }
                } while (!flag);
            }
        }
        Collections.shuffle(createTestList);  // shuffle the pair order
        return createTestList;
    }

    /**
     * Determine the number of questions that will be on the test
     *
     * @return numberQuestions
     */
    public int getNumberQuestions() {
        int numberQuestions = testItem.size();
        return numberQuestions * (numberQuestions - 1) / 2;
    }

    /**
     * Create the test session for the user in the database.
     *
     * @param userID userID of person taking the test
     * @param testID current test
     * @return session id
     */
    public int createSession(int userID, int testID) {
        return db.createTestSession(userID, testID);
    }

    /**
     * Inserts the results of the test into the database
     *
     * @param resultsList list of test results
     * @param sessionID   session id for the test
     */

    public void insertTestResults(ArrayList<TestQuestion> resultsList, int sessionID) {
        for (TestQuestion results : resultsList) {
            db.insertResults(results.getScore(), sessionID, results.getFirstItemID(), results.getSecondItemID());
        }
    }

    /**
     * Closes db connection and exits the program.
     *
     * @param code value determines the path to take when user completes or backs out of test
     */
    public void endTest(int code) {
        switch (code) {
            case 0:
                db.close();
                Main.closeTakingTest();
                Main.createAndShowGUI();
                break;
            case 1:
                db.close();
                Main.closeTakingTest();
                Main.showChoiceUI();
                break;
            default:
                db.close();
                System.exit(0);
        }
    }
}
