package codeTigers.Database;

import codeTigers.BusinessLogic.Test;
import codeTigers.BusinessLogic.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.*;

/**
 * Created by Wally Haven on 1/31/2018.
 */
public class LoginDB {
    private Connection mConnection;
    private Database db;

    public LoginDB() {
        db = new Database();
        mConnection = db.connect();

    }

/*********************************** USER LOGIN/REGISTRATION SQL ******************************/

    /**
     * Look up user by email
     *
     * @param email email
     * @return User
     */
    public User lookupUser(String email) {

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
     * @param email    email
     * @param name     name
     * @param password password
     * @param role     role
     * @return user
     */
    public User regUser(String email, String name, String password, String role) {
        int getKey;
        String inserts = "INSERT INTO UserInfo(eMail, Name, Password, Role) VALUES (?, ?, ?, ?);";
        try {
            PreparedStatement statmt = mConnection.prepareStatement(inserts, Statement.RETURN_GENERATED_KEYS);
            statmt.setString(1, email);
            statmt.setString(2, name);
            statmt.setString(3, password);
            statmt.setString(4, "User");
            statmt.executeUpdate();
            ResultSet rs = statmt.getGeneratedKeys();
            ;
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

/*********************************** END USER LOGIN/REGISTRATION SQL **************************/

}
