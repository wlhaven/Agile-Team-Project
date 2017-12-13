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
                    String uEmail = String.valueOf(emailTextField.getText().toLowerCase());
                    String uPaswd = String.valueOf(textPasswordField.getPassword());

                    Main.setUser(new User(-1, uEmail, "", uPaswd, ""));
                    Main.showLogin();
                }
            }
        });

        existingAccountLoginButton.addActionListener(actionEvent -> {
            String uEmail = String.valueOf(emailTextField.getText().toLowerCase());
            String uPaswd = String.valueOf(textPasswordField.getPassword());

            Main.setUser(new User(-1, uEmail, "", uPaswd, ""));
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(11, 2, new Insets(20, 20, 20, 20), -1, -1));
        rootPanel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 24, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Register a New Account");
        panel1.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        emailTextField = new JTextField();
        panel1.add(emailTextField, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        firstNameTextField = new JTextField();
        panel1.add(firstNameTextField, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, -1), new Dimension(200, -1), new Dimension(200, -1), 0, false));
        textPasswordField = new JPasswordField();
        panel1.add(textPasswordField, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        confirmPasswordField = new JPasswordField();
        panel1.add(confirmPasswordField, new com.intellij.uiDesigner.core.GridConstraints(8, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Email Address:");
        panel1.add(label2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("First Name:");
        panel1.add(label3, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Password:");
        panel1.add(label4, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Confirm Password");
        panel1.add(label5, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(10, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(9, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        existingAccountLoginButton = new JButton();
        existingAccountLoginButton.setHideActionText(false);
        existingAccountLoginButton.setText("Login to Existing Account");
        panel2.add(existingAccountLoginButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        registerButton = new JButton();
        registerButton.setText("Register");
        panel2.add(registerButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(5, 0, 20, 0), -1, -1));
        panel1.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(10, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        regAlertLabel = new JLabel();
        Font regAlertLabelFont = this.$$$getFont$$$(null, -1, 11, regAlertLabel.getFont());
        if (regAlertLabelFont != null) regAlertLabel.setFont(regAlertLabelFont);
        regAlertLabel.setForeground(new Color(-65536));
        regAlertLabel.setText("");
        panel3.add(regAlertLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lastNameTextField = new JTextField();
        panel1.add(lastNameTextField, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(225, -1), new Dimension(225, -1), new Dimension(225, -1), 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Last Name:");
        panel1.add(label6, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Please provide the following information");
        panel1.add(label7, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        Font label8Font = this.$$$getFont$$$(null, -1, 10, label8.getFont());
        if (label8Font != null) label8.setFont(label8Font);
        label8.setText("<html>Strong passwords include a UPPER case, lower case,<br>special charcter, digit, and are at least 6 characters long");
        panel1.add(label8, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        Font label9Font = this.$$$getFont$$$(null, -1, 10, label9.getFont());
        if (label9Font != null) label9.setFont(label9Font);
        label9.setText("Please provide a first and last name");
        panel1.add(label9, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}
