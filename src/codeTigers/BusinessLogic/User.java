package codeTigers.BusinessLogic;

import codeTigers.Database.Database;
import codeTigers.Database.LoginDB;
import codeTigers.Main;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A User Class for login and register
 * @author      Jon Grevstad
 * @version     10/11/2017
 * Modified     11/22/2017
 * - Added parameters for users
 * - Added login which searches for user and also directs user from Role
 * - Added uRegister which searches if user already exists before adding
 */

public class User {
    private final int mUserID;
    private final String uEmail;
    private final String uName;
    private final String uPassword;
    private final String uRole;

    /**
     * Sets up user data structure
     *
     * @param userID    user id
     * @param email     email
     * @param name      name
     * @param password  password
     * @param role      role
     */
    public User(int userID, String email, String name, String password, String role) {
        mUserID = userID;
        uEmail = email;
        uName = name;
        uPassword = password;
        uRole = role;
    }

    /**
     * User login with checks and invalid focusing
     *
     * @param email email
     * @param passwd password
     * @param alertLabel alert label
     * @param emailTextField email text field
     * @param textPasswordField text password field
     */
    public static void login(String email, String passwd, JLabel alertLabel,
                             JTextField emailTextField, JPasswordField textPasswordField) {

        LoginDB database = new LoginDB();
        User user = database.lookupUser(email);
        database.close();

        if(user == null) {
            emailTextField.requestFocus();
            alertLabel.setText("User not found");
            System.out.println("User not found");
            return;
        }

        if(!user.getPassword().equals(passwd)) {
            textPasswordField.requestFocus();
            alertLabel.setText("Password incorrect");
            System.out.println("Password incorrect");
        } else {
            switch (user.getRole()) {
                case "Administrator":
                    Main.setUser(user);
                    Main.showAdminSetupGui();
                    System.out.println("Logged In as Administrator");
                    break;
                case "Therapist":
                    Main.setUser(user);
                    Main.showTestResults();
                    System.out.println("Logged In as Therapist");
                    break;
                case "User":
                    Main.setUser(user);
                    Main.showChoiceUI();
                    System.out.println("Logged In as User");
                    break;
            }
        }
    }

    //I found these at www.java2novice.com
    private static final Pattern emailPtrn = Pattern.compile("(?=.*@).{1,48}");
    private static final Pattern emailPtrn1 = Pattern.compile(
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    private static final Pattern namePtrn = Pattern.compile(".*\\S");

    private static final Pattern pswdPtrn = Pattern.compile(".{6,32}");
    private static final Pattern pswdPtrn1 = Pattern.compile("(?=.*[a-z]).{1,32}");
    private static final Pattern pswdPtrn2 = Pattern.compile("((?=.*[A-Z]).{1,32})");
    private static final Pattern pswdPtrn3 = Pattern.compile("((?=.*\\d).{1,32})");
    private static final Pattern pswdPtrn4 = Pattern.compile("(?=.*[!@#$%]).{1,32}");

    /**
     * User register with field validations and invalid focusing
     *
     * @param email email
     * @param fnam first name
     * @param lnam last name
     * @param password password
     * @param role role
     * @param confPass confirm password
     * @param regAlertLabel register alert label
     * @param emailTextField email text field
     * @param firstNameTextField first name text field
     * @param lastNameTextField last name text field
     * @param textPasswordField password field
     * @param confirmPasswordField confirm password field
     */
    public static void uRegister(String email, String fnam, String lnam, String password, String role, String confPass,
                                 JLabel regAlertLabel, JTextField emailTextField, JTextField firstNameTextField,
                                 JTextField lastNameTextField, JPasswordField textPasswordField,
                                 JPasswordField confirmPasswordField) {

        String name = fnam + " " + lnam;
        // Email matcher
        Matcher emtch = emailPtrn.matcher(email);
        Matcher emtch1 = emailPtrn1.matcher(email);

        // First & Last name matcher
        Matcher fNamMtch = namePtrn.matcher(fnam);
        Matcher lNamMtch = namePtrn.matcher(lnam);

        // Password matcher
        Matcher pmtch = pswdPtrn.matcher(password);
        Matcher pmtch1 = pswdPtrn1.matcher(password);
        Matcher pmtch2 = pswdPtrn2.matcher(password);
        Matcher pmtch3 = pswdPtrn3.matcher(password);
        Matcher pmtch4 = pswdPtrn4.matcher(password);

        LoginDB database = new LoginDB();
        User userMatch = database.lookupUser(email);

        if (userMatch != null) {
            emailTextField.requestFocus();
            regAlertLabel.setText("User already exists");
            System.out.println("User already exists");
        }
        else if(!(emtch.matches())) {
            emailTextField.requestFocus();
            regAlertLabel.setText("Email Address must contain @ symbol");
            System.out.println("Email Address must contain @ symbol");
        }
        else if(!(emtch1.matches())) {
            emailTextField.requestFocus();
            regAlertLabel.setText("<html>Email Address must only contain a @ symbol and a dot \".\" <br>" +
                    "followed by at least 2 characters, e.g. <i>test@gmail.com</i>");
            System.out.println("Email Address must only contain @ symbol and a \".\" followed by " +
                    "at least 2 characters, e.g. test@test.com");
        }
        else if(fnam.equals("") || !(fNamMtch.matches())) {
            firstNameTextField.requestFocus();
            regAlertLabel.setText("Please provide a first name");
            System.out.println("Please provide a first name");
        }
        else if(lnam.equals("") || !(lNamMtch.matches())) {
            lastNameTextField.requestFocus();
            regAlertLabel.setText("Please provide a last name");
            System.out.println("Please provide a last name");
        }
        else if(!(pmtch.matches())) {
            textPasswordField.requestFocus();
            regAlertLabel.setText("Password must be 6-32 characters long");
            System.out.println("Password must be 6-32 characters long");
        }
        else if(!(pmtch1.matches())) {
            textPasswordField.requestFocus();
            regAlertLabel.setText("Password must have a lower case character");
            System.out.println("Password must have a lower case character");
        }
        else if(!(pmtch2.matches())) {
            textPasswordField.requestFocus();
            regAlertLabel.setText("Password must have a Upper case character");
            System.out.println("Password must have a Upper case character");
        }
        else if(!(pmtch3.matches())) {
            textPasswordField.requestFocus();
            regAlertLabel.setText("Password must have a digit");
            System.out.println("Password must have a digit");
        }
        else if(!(pmtch4.matches())) {
            textPasswordField.requestFocus();
            regAlertLabel.setText("Password must have a special character");
            System.out.println("Password must have a special character");
        }
        else if(!(password.equals(confPass))) {
            confirmPasswordField.requestFocus();
            regAlertLabel.setText("Password doesn't match Confirm Password");
            System.out.println("Passwords doesn't match Confirm Password");
        }
        else {
            User u = database.regUser(email, name, password, role);
            System.out.println("User has been created");
            Main.setUser(u);
            Main.showChoiceUI();
        }
    }

    /**
     * Returns UserID
     *
     * @return  userID
     */
    public int getUserID() {
        return mUserID;
    }

    /**
     * Returns Email
     *
     * @return  email
     */
    public String getEmail() {
        return uEmail;
    }

    /**
     * Return first name
     *
     * @return first name
     */
    public String getFName() {
        return uName.substring(0, uName.indexOf(' '));
    }

    /**
     * Returns Password
     *
     * @return  password
     */
    public String getPassword() {
        return uPassword;
    }

    /**
     * Returns Role
     *
     * @return  role
     */
    private String getRole() {
        return uRole;
    }

}
