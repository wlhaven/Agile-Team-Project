package codeTigers.Database;

import codeTigers.BusinessLogic.Test;
import codeTigers.BusinessLogic.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.*;

/**
 * Created by Wally Haven on 1/31/2018.
 */
public class LoginDB {
    private final Connection mConnection;
    private final Database db;

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


    public void close() {
        db.close();
    }

/*********************************** END USER LOGIN/REGISTRATION SQL **************************/

}
