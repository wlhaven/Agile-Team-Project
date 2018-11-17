package codeTigers.UI.Login;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import codeTigers.Main;
import codeTigers.BusinessLogic.User;

/**
 * A Register GUI with email, name, password, & confirm password fields
 *
 * @author Jon Grevstad
 * @version 10/11/2017
 * Modified     11/15/2017
 * - Added getters and setters
 * - Put validations in the User Class
 * - Added Enter Key Listeners to all fields
 * - Added Ancestor Listeners to emailTextField
 */

public class RegisterGUI {
    JPanel rootPanel;
    JTextField emailTextField;
    JTextField firstNameTextField;
    JTextField lastNameTextField;
    JPasswordField textPasswordField;
    JPasswordField confirmPasswordField;
    JButton existingAccountLoginButton;
    JButton registerButton;
    JLabel regAlertLabel;


    /**
     * This sets the email & password to be passed
     *
     * @param email    set a String email
     * @param password set a String password
     */
    public RegisterGUI(String email, String password) {
        emailTextField.setText(email);
        textPasswordField.setText(password);

        emailTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String emal = emailTextField.getText().toLowerCase();
                    String fnam = firstNameTextField.getText();
                    String lnam = lastNameTextField.getText();
                    String paswd = String.valueOf(textPasswordField.getPassword());
                    String conPas = String.valueOf(confirmPasswordField.getPassword());

                    User.uRegister(emal, fnam, lnam, paswd, "User", conPas, regAlertLabel, emailTextField,
                            firstNameTextField, lastNameTextField, textPasswordField, confirmPasswordField);
                }
            }
        });

        firstNameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String emal = emailTextField.getText().toLowerCase();
                    String fnam = firstNameTextField.getText();
                    String lnam = lastNameTextField.getText();
                    String paswd = String.valueOf(textPasswordField.getPassword());
                    String conPas = String.valueOf(confirmPasswordField.getPassword());

                    User.uRegister(emal, fnam, lnam, paswd, "User", conPas, regAlertLabel, emailTextField,
                            firstNameTextField, lastNameTextField, textPasswordField, confirmPasswordField);
                }
            }
        });

        lastNameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String emal = emailTextField.getText().toLowerCase();
                    String fnam = firstNameTextField.getText();
                    String lnam = lastNameTextField.getText();
                    String paswd = String.valueOf(textPasswordField.getPassword());
                    String conPas = String.valueOf(confirmPasswordField.getPassword());

                    User.uRegister(emal, fnam, lnam, paswd, "User", conPas, regAlertLabel, emailTextField,
                            firstNameTextField, lastNameTextField, textPasswordField, confirmPasswordField);
                }
            }
        });

        textPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String emal = emailTextField.getText().toLowerCase();
                    String fnam = firstNameTextField.getText();
                    String lnam = lastNameTextField.getText();
                    String paswd = String.valueOf(textPasswordField.getPassword());
                    String conPas = String.valueOf(confirmPasswordField.getPassword());

                    User.uRegister(emal, fnam, lnam, paswd, "User", conPas, regAlertLabel, emailTextField,
                            firstNameTextField, lastNameTextField, textPasswordField, confirmPasswordField);
                }
            }
        });

        confirmPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String emal = emailTextField.getText().toLowerCase();
                    String fnam = firstNameTextField.getText();
                    String lnam = lastNameTextField.getText();
                    String paswd = String.valueOf(textPasswordField.getPassword());
                    String conPas = String.valueOf(confirmPasswordField.getPassword());

                    User.uRegister(emal, fnam, lnam, paswd, "User", conPas, regAlertLabel, emailTextField,
                            firstNameTextField, lastNameTextField, textPasswordField, confirmPasswordField);
                }
            }
        });

        registerButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String emal = emailTextField.getText().toLowerCase();
                    String fnam = firstNameTextField.getText();
                    String lnam = lastNameTextField.getText();
                    String paswd = String.valueOf(textPasswordField.getPassword());
                    String conPas = String.valueOf(confirmPasswordField.getPassword());

                    User.uRegister(emal, fnam, lnam, paswd, "User", conPas, regAlertLabel, emailTextField,
                            firstNameTextField, lastNameTextField, textPasswordField, confirmPasswordField);
                }
            }
        });

        registerButton.addActionListener(actionEvent -> {
            String emal = emailTextField.getText().toLowerCase();
            String fnam = firstNameTextField.getText();
            String lnam = lastNameTextField.getText();
            String paswd = String.valueOf(textPasswordField.getPassword());
            String conPas = String.valueOf(confirmPasswordField.getPassword());

            User.uRegister(emal, fnam, lnam, paswd, "User", conPas, regAlertLabel, emailTextField,
                    firstNameTextField, lastNameTextField, textPasswordField, confirmPasswordField);
        });

        existingAccountLoginButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    //String uEmail = String.valueOf(emailTextField.getText().toLowerCase());
                    //String uPaswd = String.valueOf(textPasswordField.getPassword());
                    //Main.setUser(new User(-1, uEmail, "", uPaswd, ""));
                    Main.showLogin();
                }
            }
        });

        existingAccountLoginButton.addActionListener(actionEvent -> {
            //String uEmail = String.valueOf(emailTextField.getText().toLowerCase());
            //String uPaswd = String.valueOf(textPasswordField.getPassword());
            //Main.setUser(new User(-1, uEmail, "", uPaswd, ""));
            Main.showLogin();
        });

        emailTextField.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                emailTextField.requestFocusInWindow();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
        });
    }

    /**
     * This returns the root panel
     *
     * @return returns the rootPanel
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }

}
