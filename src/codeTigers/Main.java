package codeTigers;

import codeTigers.BusinessLogic.Test;
import codeTigers.BusinessLogic.User;
import codeTigers.UI.Admin.AdminSetupGUI;
import codeTigers.UI.Login.LogginGUI;
import codeTigers.UI.Login.RegisterGUI;
import codeTigers.UI.Results.ResultsDisplayUI;
import codeTigers.UI.Test.TestChoiceUI;
import codeTigers.UI.Test.UserTakingTestForm;

import javax.swing.*;

import static javax.swing.SwingUtilities.invokeLater;

/**
 * codeTigers.Main Class For This Project
 * @author      Jon, Wally, Steve, Cameron
 * @version     11/3/2017
 * Modified     11/3/2017
 * - Added methods for Admin, Test, and Test Results
 * - Pass User ID to Taking Test
 *
 * Consolidated all the separate main files into this one,
 * refactored and removed redundant code
 */

public class Main extends JFrame {
    private static User uData = new User(-1, "", "", "", "");
    private static Test tData = new Test(-1, "");
    private static JFrame uFrame = null;

    /**
     * Show basic GUI structure which opens to the Login GUI with exit on close to
     * ensure it closes
     */
    public static void createAndShowGUI() {
        uFrame = new JFrame();
        uFrame.setDefaultCloseOperation(uFrame.EXIT_ON_CLOSE);
        showLogin();
    }

    /**
     * Show Login GUI with passed email & password fields
     * Centered and not resizable
     */
    public static void showLogin() {
        uFrame.setTitle("Login");
        uFrame.getContentPane().removeAll();
        uFrame.getContentPane().add(new LogginGUI().getRootPanel());
        uFrame.setResizable(false);
        uFrame.pack();
        uFrame.setLocationRelativeTo(null);
        uFrame.setVisible(true);
    }

    /**
     * Show Register GUI with passed email & password fields
     * Centered and not resizable
     */
    public static void showRegister() {
        uFrame.setTitle("Register");
        uFrame.getContentPane().removeAll();
        uFrame.getContentPane().add(new RegisterGUI(uData.getEmail(), uData.getPassword()).getRootPanel());
        uFrame.setResizable(false);
        uFrame.pack();
        uFrame.setLocationRelativeTo(null);
        uFrame.setVisible(true);
    }

    /**
     * Show Test Choice GUI
     * Centered and not resizable
     */
    public static void showChoiceUI() {
        uFrame.setTitle("Test Choice");
        uFrame.getContentPane().removeAll();
        uFrame.getContentPane().add(new TestChoiceUI(uData).getRootPanel());
        uFrame.setResizable(false);
        uFrame.pack();
        uFrame.setLocationRelativeTo(null);
        uFrame.setVisible(true);
    }

    /**
     * Create the screen for the Administrator Setup form
     * Cameron's GUI
     */
    public static void showAdminSetupGui() {
        uFrame.setTitle("Admin setup");
        JPanel mypanel = new AdminSetupGUI().getRootPanel();
        uFrame.getContentPane().removeAll();
        uFrame.getContentPane().add(mypanel);
        uFrame.pack();
        uFrame.setLocationRelativeTo(null);
        uFrame.setVisible(true);
    }

    /**
     * Create the screen for the user Taking Test form
     * Wally's GUI
     */

    public static void showUserTakingTest() {
        uFrame.setTitle("Test Questions");
        uFrame.getContentPane().removeAll();
        uFrame.getContentPane().add(new UserTakingTestForm(uData, tData).getRootPanel());
        uFrame.setResizable(true);
        uFrame.pack();
        uFrame.setLocationRelativeTo(null);
        uFrame.setVisible(true);
    }

    /**
     * Close the Test window when test finished.
     */
    public static void closeTakingTest() {
        uFrame.setVisible(false);
    }

    /**
     * Create the screen for the Test Results form
     * Steve's GUI
     */
    public static void showTestResults() {
        uFrame.setTitle("Test Results");
        uFrame.getContentPane().removeAll();
        uFrame.getContentPane().add(new ResultsDisplayUI().getRootPanel());
        uFrame.pack();
        uFrame.setLocationRelativeTo(null);
        uFrame.setVisible(true);
    }

    /**
     * This sets users data for passing
     * @param user  User object
     */
    public static void setUser(User user) {
        uData = user;
    }

    /**
     * Set Test data for passing
     * @param test  Test object
     */
    public static void setTest(Test test) {
        tData = test;
    }

    /**
     * Application entry point
     * @param args array of command-line arguments passed to this method
     */
    public static void main(String[] args) {
        invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
