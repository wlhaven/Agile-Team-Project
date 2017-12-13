package codeTigers.UI.Test;

import codeTigers.BusinessLogic.*;
import codeTigers.Main;
import codeTigers.UI.PicturePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * CodeTiger class to pick a preferred color
 *
 * @author Wally Haven
 * @version 11/30/2017
 * <p>
 * Modifications: Refactored class to pull db logic and calculations from class and into business layer.
 * Add back button and logic
 * Support for multiple tests
 * Fixed progress bar issues
 */

public class UserTakingTestForm {
    private JPanel rootPanel;
    private JRadioButton firstChoiceRadioButton;
    private JRadioButton secondChoiceRadioButton;
    private JRadioButton undecidedRadioButton;
    private JButton nextButton;
    private JButton backButton;
    private JLabel questionNumberLabel;
    private PicturePanel image1;
    private PicturePanel image2;
    private PicturePanel undecidedImage;
    private JProgressBar testProgressBar;
    private JLabel testNameLabel;
    private static int currentPair = 0;
    private static int numberQuestions = 0;
    private static int sessionID = 0;
    private final ButtonGroup choiceGroup;
    private final ArrayList<TestQuestion> testQuestions;
    private final TestDriver testDriver;

    /**
     * Constructor to set up the screen and components
     */
    public UserTakingTestForm(User u, Test t) {
        rootPanel.setPreferredSize(new Dimension(1000, 645));
        rootPanel.setAlignmentX(100);
        String testName = t.getTestName();
        testNameLabel.setText(testName);

        choiceGroup = new ButtonGroup();
        choiceGroup.add(firstChoiceRadioButton);
        choiceGroup.add(secondChoiceRadioButton);
        choiceGroup.add(undecidedRadioButton);

        int testID = t.getTestID();
        int userID = u.getUserID();

        testDriver = new TestDriver(testID);
        testQuestions = testDriver.createTestQuestions();
        numberQuestions = testDriver.getNumberQuestions();
        displayQuestions();

        try {
            BufferedImage image = ImageIO.read(Main.class.getResourceAsStream("/images/undecided.jpg"));
            undecidedImage.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        nextButton.addActionListener(e -> {
            //check to see if at end of the test. If so write results to the database
            if (currentPair == numberQuestions - 1) {
                sessionID = testDriver.createSession(userID, testID);
                testDriver.insertTestResults(testQuestions, sessionID);
                JOptionPane.showMessageDialog(rootPanel, "Test Completed. Returning to Log in screen");
                currentPair = 0;
                testDriver.endTest(0);
            } else {
                nextButton.setEnabled((false));
                currentPair++;
                displayQuestions();
                displayCurrentAnswer();
            }
        });

        backButton.addActionListener(e -> {
            currentPair--;
            //User has completely backed out of test.
            if (currentPair < 0) {
                currentPair = 0;
                JOptionPane.showMessageDialog(rootPanel, "Returning to test selection screen");
                testDriver.endTest(1);
            } else {
                displayQuestions();
                displayCurrentAnswer();
            }
        });

        firstChoiceRadioButton.addActionListener(e -> {
            if (firstChoiceRadioButton.isSelected()) {
                testQuestions.get(currentPair).setScore(1);
                nextButton.setEnabled((true));
                backButton.setEnabled((true));
            }
        });

        secondChoiceRadioButton.addActionListener(e -> {
            if (secondChoiceRadioButton.isSelected()) {
                testQuestions.get(currentPair).setScore(-1);
                nextButton.setEnabled((true));
                backButton.setEnabled((true));
            }
        });

        undecidedRadioButton.addActionListener(e -> {
            if (undecidedRadioButton.isSelected()) {
                testQuestions.get(currentPair).setScore(0);
                nextButton.setEnabled((true));
                backButton.setEnabled((true));
            }
        });

        image1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                firstChoiceRadioButton.setSelected(true);
                testQuestions.get(currentPair).setScore(1);
                nextButton.setEnabled((true));
                backButton.setEnabled((true));
            }
        });

        image2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                secondChoiceRadioButton.setSelected(true);
                testQuestions.get(currentPair).setScore(-1);
                nextButton.setEnabled((true));
                backButton.setEnabled((true));
            }
        });

        undecidedImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                undecidedRadioButton.setSelected(true);
                testQuestions.get(currentPair).setScore(0);
                nextButton.setEnabled((true));
                backButton.setEnabled((true));
            }
        });
    }

    /**
     * Display the next question on the screen.
     */

    private void displayQuestions() {
        progressBarUpdate();
        if (currentPair < testQuestions.size()) {
            questionNumberLabel.setText("Question " + (currentPair + 1));
            firstChoiceRadioButton.setText(testQuestions.get(currentPair).getFirstItemName());
            secondChoiceRadioButton.setText(testQuestions.get(currentPair).getSecondItemName());
            if (testQuestions.get(currentPair).getImage1() == null && testQuestions.get(currentPair).getImage2() == null) {
                setDefaultImages(0);
            } else if (testQuestions.get(currentPair).getImage1() == null) {
                setDefaultImages(1);
                image2.setImage(testQuestions.get(currentPair).getImage2());
            } else if (testQuestions.get(currentPair).getImage2() == null) {
                image1.setImage(testQuestions.get(currentPair).getImage1());
                setDefaultImages(2);
            } else {
                image1.setImage(testQuestions.get(currentPair).getImage1());
                image2.setImage(testQuestions.get(currentPair).getImage2());
            }
        } else {
            JOptionPane.showMessageDialog(rootPanel, "Unable to create the test. Program will now exit.");
            testDriver.endTest(2);
        }
    }

    /**
     * Assign default image if the test question does not contain an image
     *
     * @param option determines which image panel contains needs the default image assigned to it.
     */
    private void setDefaultImages(int option) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Main.class.getResourceAsStream("/images/NoImageAvailable.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (option) {
            case 0:
                image1.setImage(image);
                image2.setImage(image);
                break;
            case 1:
                image1.setImage(image);
                break;
            case 2:
                image2.setImage(image);
                break;
        }
    }

    /**
     * Display the selected answer to the screen when buttons invoked.
     */

    private void displayCurrentAnswer() {
        int tmpScore = testQuestions.get(currentPair).getScore();
        switch (tmpScore) {
            case 1:
                firstChoiceRadioButton.setSelected(true);
                nextButton.setEnabled((true));
                break;
            case -1:
                secondChoiceRadioButton.setSelected(true);
                nextButton.setEnabled((true));
                break;
            case 0:
                undecidedRadioButton.setSelected(true);
                nextButton.setEnabled((true));
                break;
            default:
                choiceGroup.clearSelection();
                nextButton.setEnabled((false));
                break;
        }
    }

    /**
     * Progress Bar set with currentPair and numberQuestions
     */
    private void progressBarUpdate() {
        testProgressBar.setMinimum(0);
        testProgressBar.setMaximum(numberQuestions - 1);
        testProgressBar.setValue(currentPair);
        testProgressBar.setString("Question " + (currentPair + 1) + " of " + numberQuestions);
    }

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
        rootPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        Font rootPanelFont = this.$$$getFont$$$(null, Font.PLAIN, 28, rootPanel.getFont());
        if (rootPanelFont != null) rootPanel.setFont(rootPanelFont);
        rootPanel.setForeground(new Color(-16777216));
        rootPanel.setMaximumSize(new Dimension(-1, -1));
        rootPanel.setMinimumSize(new Dimension(600, 440));
        rootPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Item Comparison Test", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$("Times New Roman", Font.PLAIN, 28, rootPanel.getFont())));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(9, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(panel1);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Times New Roman", Font.PLAIN, 24, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-16777216));
        label1.setText("Which item do you prefer?");
        panel1.add(label1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(219, 22), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setEnabled(false);
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(219, 45), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        firstChoiceRadioButton = new JRadioButton();
        Font firstChoiceRadioButtonFont = this.$$$getFont$$$("Times New Roman", Font.PLAIN, 24, firstChoiceRadioButton.getFont());
        if (firstChoiceRadioButtonFont != null) firstChoiceRadioButton.setFont(firstChoiceRadioButtonFont);
        firstChoiceRadioButton.setForeground(new Color(-16777216));
        firstChoiceRadioButton.setText("Image1");
        panel3.add(firstChoiceRadioButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        secondChoiceRadioButton = new JRadioButton();
        Font secondChoiceRadioButtonFont = this.$$$getFont$$$("Times New Roman", Font.PLAIN, 24, secondChoiceRadioButton.getFont());
        if (secondChoiceRadioButtonFont != null) secondChoiceRadioButton.setFont(secondChoiceRadioButtonFont);
        secondChoiceRadioButton.setForeground(new Color(-16777216));
        secondChoiceRadioButton.setText("Image2");
        panel4.add(secondChoiceRadioButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(30, -1), null, null, 0, false));
        image2 = new PicturePanel();
        panel2.add(image2, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(280, 250), null, null, 0, false));
        image1 = new PicturePanel();
        panel2.add(image1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(280, 250), null, null, 0, false));
        undecidedRadioButton = new JRadioButton();
        Font undecidedRadioButtonFont = this.$$$getFont$$$("Times New Roman", Font.PLAIN, 24, undecidedRadioButton.getFont());
        if (undecidedRadioButtonFont != null) undecidedRadioButton.setFont(undecidedRadioButtonFont);
        undecidedRadioButton.setForeground(new Color(-16777216));
        undecidedRadioButton.setText("Undecided");
        panel2.add(undecidedRadioButton, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(30, -1), null, null, 0, false));
        undecidedImage = new PicturePanel();
        panel2.add(undecidedImage, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(280, 250), null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel5, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 50), new Dimension(219, 47), null, 0, false));
        nextButton = new JButton();
        nextButton.setActionCommand("Next\\ Question");
        nextButton.setEnabled(false);
        Font nextButtonFont = this.$$$getFont$$$("Times New Roman", Font.PLAIN, 24, nextButton.getFont());
        if (nextButtonFont != null) nextButton.setFont(nextButtonFont);
        nextButton.setForeground(new Color(-16777216));
        nextButton.setText("Next");
        panel5.add(nextButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), null, null, 0, false));
        backButton = new JButton();
        backButton.setEnabled(true);
        Font backButtonFont = this.$$$getFont$$$("Times New Roman", Font.PLAIN, 24, backButton.getFont());
        if (backButtonFont != null) backButton.setFont(backButtonFont);
        backButton.setForeground(new Color(-16777216));
        backButton.setText("Back");
        backButton.putClientProperty("hideActionText", Boolean.FALSE);
        panel5.add(backButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel1.add(panel6, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        questionNumberLabel = new JLabel();
        Font questionNumberLabelFont = this.$$$getFont$$$("Times New Roman", Font.PLAIN, 24, questionNumberLabel.getFont());
        if (questionNumberLabelFont != null) questionNumberLabel.setFont(questionNumberLabelFont);
        questionNumberLabel.setForeground(new Color(-16777216));
        questionNumberLabel.setText("Question 1");
        panel6.add(questionNumberLabel);
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 30), null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel7, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        testProgressBar = new JProgressBar();
        testProgressBar.setBackground(new Color(-1973791));
        testProgressBar.setDoubleBuffered(false);
        Font testProgressBarFont = this.$$$getFont$$$("Times New Roman", Font.PLAIN, 18, testProgressBar.getFont());
        if (testProgressBarFont != null) testProgressBar.setFont(testProgressBarFont);
        testProgressBar.setForeground(new Color(-15695862));
        testProgressBar.setStringPainted(true);
        panel7.add(testProgressBar, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel8, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        testNameLabel = new JLabel();
        Font testNameLabelFont = this.$$$getFont$$$("Times New Roman", Font.PLAIN, 26, testNameLabel.getFont());
        if (testNameLabelFont != null) testNameLabel.setFont(testNameLabelFont);
        testNameLabel.setForeground(new Color(-16777216));
        testNameLabel.setText("Test Name");
        panel8.add(testNameLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(20, 10), null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel9, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        panel9.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 25), new Dimension(-1, 25), null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel10, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        panel10.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 10), null, null, 0, false));
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
