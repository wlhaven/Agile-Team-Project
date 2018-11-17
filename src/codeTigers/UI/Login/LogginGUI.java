package codeTigers.UI.Login;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import codeTigers.Main;
import codeTigers.BusinessLogic.User;

/**
 * A Login GUI with email & password fields
 *
 * @author Jon Grevstad
 * @version 10/11/2017
 * Modified     11/15/2017
 * - Added Enter Key Listeners to both fields
 * - Added Ancestor Listeners to emailTextField
 */

public class LogginGUI {
    JPanel rootPanel;
    JTextField emailTextField;
    JPasswordField textPasswordField;
    JButton createNewUserButton;
    JButton logInButton;
    JLabel alertLabel;

    public LogginGUI() {
        emailTextField.setText("");
        textPasswordField.setText("");

        emailTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String uEmail = String.valueOf(emailTextField.getText().toLowerCase());
                    String uPaswd = String.valueOf(textPasswordField.getPassword());

                    User.login(uEmail, uPaswd, alertLabel, emailTextField, textPasswordField);
                }
            }
        });

        textPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String uEmail = String.valueOf(emailTextField.getText().toLowerCase());
                    String uPaswd = String.valueOf(textPasswordField.getPassword());

                    User.login(uEmail, uPaswd, alertLabel, emailTextField, textPasswordField);
                }
            }
        });

        logInButton.addActionListener(actionEvent -> {
            String uEmail = String.valueOf(emailTextField.getText().toLowerCase());
            String uPaswd = String.valueOf(textPasswordField.getPassword());

            User.login(uEmail, uPaswd, alertLabel, emailTextField, textPasswordField);
        });

        logInButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String uEmail = String.valueOf(emailTextField.getText().toLowerCase());
                    String uPaswd = String.valueOf(textPasswordField.getPassword());

                    User.login(uEmail, uPaswd, alertLabel, emailTextField, textPasswordField);
                }
            }
        });

        createNewUserButton.addActionListener(actionEvent -> {
            String uEmail = String.valueOf(emailTextField.getText().toLowerCase());
            String uPaswd = String.valueOf(textPasswordField.getPassword());

            Main.setUser(new User(-1, uEmail, "", uPaswd, ""));
            Main.showRegister();
        });

        createNewUserButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String uEmail = String.valueOf(emailTextField.getText().toLowerCase());
                    String uPaswd = String.valueOf(textPasswordField.getPassword());

                    Main.setUser(new User(-1, uEmail, "", uPaswd, ""));
                    Main.showRegister();
                }
            }
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
