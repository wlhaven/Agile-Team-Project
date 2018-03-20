package codeTigers.UI.Test;

import codeTigers.BusinessLogic.*;
import codeTigers.Main;
import codeTigers.UI.PicturePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import static javax.swing.JOptionPane.QUESTION_MESSAGE;

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
    private static final int UNDECIDED_SCORE = 0;
    private static final int FIRST_ITEM_SCORE = 1;
    private static final int SECOND_ITEM_SCORE = -1;

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
                int n = JOptionPane.showConfirmDialog(
                        rootPanel,
                        "Would you like take another test?",
                        "Test Completed!",
                        JOptionPane.YES_NO_OPTION,
                        QUESTION_MESSAGE);
                if(n == 0) {
                    currentPair = 0;
                    testDriver.endTest(1);
                }
                else {
                    JOptionPane.showMessageDialog(rootPanel, "Test Completed. Returning to Log in screen");
                    currentPair = 0;
                    testDriver.endTest(0);
                }
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
                testQuestions.get(currentPair).setScore(FIRST_ITEM_SCORE);
                nextButton.setEnabled((true));
                backButton.setEnabled((true));
            }
        });

        secondChoiceRadioButton.addActionListener(e -> {
            if (secondChoiceRadioButton.isSelected()) {
                testQuestions.get(currentPair).setScore(SECOND_ITEM_SCORE);
                nextButton.setEnabled((true));
                backButton.setEnabled((true));
            }
        });

        undecidedRadioButton.addActionListener(e -> {
            if (undecidedRadioButton.isSelected()) {
                testQuestions.get(currentPair).setScore(UNDECIDED_SCORE);
                nextButton.setEnabled((true));
                backButton.setEnabled((true));
            }
        });

        image1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                firstChoiceRadioButton.setSelected(true);
                testQuestions.get(currentPair).setScore(FIRST_ITEM_SCORE);
                nextButton.setEnabled((true));
                backButton.setEnabled((true));
            }
        });

        image2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                secondChoiceRadioButton.setSelected(true);
                testQuestions.get(currentPair).setScore(SECOND_ITEM_SCORE);
                nextButton.setEnabled((true));
                backButton.setEnabled((true));
            }
        });

        undecidedImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                undecidedRadioButton.setSelected(true);
                testQuestions.get(currentPair).setScore(UNDECIDED_SCORE);
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
            case FIRST_ITEM_SCORE:
                firstChoiceRadioButton.setSelected(true);
                nextButton.setEnabled((true));
                break;
            case SECOND_ITEM_SCORE:
                secondChoiceRadioButton.setSelected(true);
                nextButton.setEnabled((true));
                break;
            case UNDECIDED_SCORE:
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

}
