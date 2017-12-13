package codeTigers.UI.Results;
import codeTigers.BusinessLogic.ResultsDisplay;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/*
 *  This class displays the GUI for displaying the results of a test
 *
 * @author  Steve Daily
 * @version 2017.11.29
 *
 * Modifications:
 * Author: Steve Daily 10/28/2017
 * provide a test name to ResultsDisplay to prepare for multiple tests later, hardcode value for now
 * Author: Steve Daily 11/29/2017
 * add support for multiple tests
 * Author: Steve Daily 12/8/2017
 * add display of test summary results when no user is selected
 * add number of tests to title of test summary results
 * changed sort order of dates to show most recent test first in test pulldown menus
 */

public class ResultsDisplayUI {
    private JPanel rootPanel;
    private JPanel tablePanel;
    private JLabel formTitle;
    private JComboBox usersSelectPulldown;
    private JScrollPane resultsScrollPane;
    private JTable resultsTable;
    private JPanel userPanel;
    private JLabel userLabel;
    private JPanel testPanel;
    private JLabel testLabel;
    private JComboBox testSelectPulldown;
    private JPanel titlePanel;
    private JPanel pulldownPanel;
    private JPanel displayPanel;
    private JScrollPane summaryScrollPane;
    private JTable summaryTable;
    private JLabel summaryLabel;
    private DefaultTableModel resultDefaultTableModel;
    private DefaultTableModel summaryDefaultTableModel;
    private DefaultComboBoxModel myTestComboBoxModel;
    private String userName;

    public ResultsDisplayUI() {
        ResultsDisplay resultsDisplay = new ResultsDisplay();

        // Set up Table Models
        resultDefaultTableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        summaryDefaultTableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        // add table header to model
        $$$setupUI$$$();
        resultDefaultTableModel.setColumnIdentifiers(resultsDisplay.getResultsColumnNames());
        summaryDefaultTableModel.setColumnIdentifiers(resultsDisplay.getSummaryColumnNames());

        resultsTable.setModel(resultDefaultTableModel);
        summaryTable.setModel(summaryDefaultTableModel);

        resultsTable.setCellSelectionEnabled(false);
        resultsTable.setFocusable(false);
        summaryTable.setCellSelectionEnabled(false);
        summaryTable.setFocusable(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 1; i < resultsDisplay.getColumnNumber(); i++) {
            resultsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        for (int i = 1; i < resultsDisplay.getColumnNumber(); i++) {
            summaryTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Set up pull down menu background color
        usersSelectPulldown.setBackground(new Color(255, 255, 255));
        testSelectPulldown.setBackground(new Color(255, 255, 255));
        // Need special stuff to set background color of selected item
        Object child = usersSelectPulldown.getAccessibleContext().getAccessibleChild(0);
        ((BasicComboPopup) child).getList().setSelectionBackground(new Color(240, 255, 240));
        child = testSelectPulldown.getAccessibleContext().getAccessibleChild(0);
        ((BasicComboPopup) child).getList().setSelectionBackground(new Color(240, 255, 240));

        // Need to prevent selection when using arrow keys
        usersSelectPulldown.putClientProperty("JComboBox.isTableCellEditor", true);

        // set up User Pulldown menu
        // Insert prompt for user at top of menu
        usersSelectPulldown.addItem("-- Select User --");
        for (String email : resultsDisplay.getUserInfo()) {
            usersSelectPulldown.addItem(email);
        }

        userName = "-- Select User --";   // holds selected item from user pulldown

        // set up Test Pulldown menu
        myTestComboBoxModel = new DefaultComboBoxModel();
        summaryLabel.setText("Summary of All Results for This Test");
        myTestComboBoxModel.addElement("-- Select Test --");
        for (Object[] row : resultsDisplay.getSummaryTestInfo()) {
            myTestComboBoxModel.addElement((String) row[0]);
        }
        testSelectPulldown.setModel(myTestComboBoxModel);

        usersSelectPulldown.addActionListener(e -> {
            JComboBox cb = (JComboBox) e.getSource();
            userName = (String) cb.getSelectedItem();
            int userIndex = (int) cb.getSelectedIndex();

            // set up Test Pulldown menu
            testSelectPulldown.setEnabled(false);
            // Need to prevent selection when using arrow keys
            testSelectPulldown.putClientProperty("JComboBox.isTableCellEditor", true);
            // remove table rows from the bottom to the top
            testSelectPulldown.removeAllItems();
            testSelectPulldown.setEnabled(true);
            summaryLabel.setText("Summary of All Results for This Test");
            if (userName.equals("-- Select User --")) {
                // no user selected display default summary table title
                myTestComboBoxModel.addElement("-- Select Test --");
                for (Object[] row : resultsDisplay.getSummaryTestInfo()) {
                    myTestComboBoxModel.addElement((String) row[0]);
                }
                testSelectPulldown.setModel(myTestComboBoxModel);
            } else {
                // Insert prompt for test at top of menu
                myTestComboBoxModel.addElement("-- Select Test --");
                ArrayList<String> testInfo = resultsDisplay.getTestInfo(userName);
                if (testInfo.size() == 1) {
                    String selectedItem = testInfo.get(0);
                    myTestComboBoxModel.addElement(selectedItem);
                    testSelectPulldown.setModel(myTestComboBoxModel);
                    testSelectPulldown.setSelectedItem(selectedItem);
                } else {
                    for (String testName : resultsDisplay.getTestInfo(userName)) {
                        myTestComboBoxModel.addElement(testName);
                    }
                    testSelectPulldown.setModel(myTestComboBoxModel);
                }
            }
        });

        testSelectPulldown.addActionListener(ev -> {
            JComboBox testCb = (JComboBox) ev.getSource();
            int testIndex = (int) testCb.getSelectedIndex();
            String testName = (String) testCb.getSelectedItem();

            // remove table rows from the bottom to the top
            for (int i = (resultDefaultTableModel.getRowCount() - 1); i >= 0; i--) {
                resultDefaultTableModel.removeRow(i);
            }
            for (int i = (summaryDefaultTableModel.getRowCount() - 1); i >= 0; i--) {
                summaryDefaultTableModel.removeRow(i);
            }
            summaryLabel.setText("Summary of All Results for This Test");

            if (testIndex > 0) {      // testIndex = 0 selects the menu prompt so no data to display
                if (userName.equals("-- Select User --")) {
                    int numTests = resultsDisplay.getTestCount(testName);
                    summaryLabel.setText("Summary of All Results for This Test Taken " + numTests + " Times");
                } else {
                    for (Object[] row : resultsDisplay.getTableData(testIndex)) {
                        resultDefaultTableModel.addRow(row);
                    }
                    int numTests = resultsDisplay.getTestCount(testIndex - 1);  //adjust index for prompt element
                    summaryLabel.setText("Summary of All Results for This Test Taken " + numTests + " Times");
                }
                Object[] newRow;
                for (Object[] row : resultsDisplay.getSummaryTableData(testIndex)) {
                    summaryDefaultTableModel.addRow(row);
                }
            }

            resultsTable.setModel(resultDefaultTableModel);
            summaryTable.setModel(summaryDefaultTableModel);
        });
    }

    /**
     * Return a reference to the rootPanel
     *
     * @return reference to the rootPanel
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }

    /**
     * Create the required GUI components
     */
    private void createUIComponents() {
        rootPanel = new JPanel();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        rootPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        tablePanel = new JPanel();
        tablePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(tablePanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        titlePanel = new JPanel();
        titlePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(5, 0, 5, 0), -1, -1));
        tablePanel.add(titlePanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        formTitle = new JLabel();
        Font formTitleFont = this.$$$getFont$$$(null, Font.BOLD, 24, formTitle.getFont());
        if (formTitleFont != null) formTitle.setFont(formTitleFont);
        formTitle.setText("Test Results");
        titlePanel.add(formTitle, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pulldownPanel = new JPanel();
        pulldownPanel.setLayout(new BorderLayout(0, 5));
        tablePanel.add(pulldownPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout(0, 0));
        pulldownPanel.add(userPanel, BorderLayout.NORTH);
        userLabel = new JLabel();
        Font userLabelFont = this.$$$getFont$$$(null, Font.BOLD, 16, userLabel.getFont());
        if (userLabelFont != null) userLabel.setFont(userLabelFont);
        userLabel.setHorizontalAlignment(0);
        userLabel.setText("User");
        userPanel.add(userLabel, BorderLayout.NORTH);
        usersSelectPulldown = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        usersSelectPulldown.setModel(defaultComboBoxModel1);
        userPanel.add(usersSelectPulldown, BorderLayout.SOUTH);
        testPanel = new JPanel();
        testPanel.setLayout(new BorderLayout(0, 0));
        pulldownPanel.add(testPanel, BorderLayout.SOUTH);
        testLabel = new JLabel();
        Font testLabelFont = this.$$$getFont$$$(null, Font.BOLD, 16, testLabel.getFont());
        if (testLabelFont != null) testLabel.setFont(testLabelFont);
        testLabel.setHorizontalAlignment(0);
        testLabel.setText("Test");
        testPanel.add(testLabel, BorderLayout.NORTH);
        testSelectPulldown = new JComboBox();
        testSelectPulldown.setEditable(false);
        testSelectPulldown.setEnabled(true);
        testPanel.add(testSelectPulldown, BorderLayout.SOUTH);
        displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout(0, 0));
        tablePanel.add(displayPanel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        resultsScrollPane = new JScrollPane();
        resultsScrollPane.setBackground(new Color(-855310));
        displayPanel.add(resultsScrollPane, BorderLayout.NORTH);
        resultsTable = new JTable();
        resultsTable.setPreferredScrollableViewportSize(new Dimension(450, 150));
        resultsTable.setRowSelectionAllowed(false);
        resultsScrollPane.setViewportView(resultsTable);
        summaryScrollPane = new JScrollPane();
        displayPanel.add(summaryScrollPane, BorderLayout.SOUTH);
        summaryTable = new JTable();
        summaryTable.setEnabled(true);
        summaryTable.setPreferredScrollableViewportSize(new Dimension(450, 150));
        summaryScrollPane.setViewportView(summaryTable);
        summaryLabel = new JLabel();
        Font summaryLabelFont = this.$$$getFont$$$(null, Font.BOLD, 14, summaryLabel.getFont());
        if (summaryLabelFont != null) summaryLabel.setFont(summaryLabelFont);
        summaryLabel.setHorizontalAlignment(0);
        summaryLabel.setText("Summary of All Results for This Test");
        displayPanel.add(summaryLabel, BorderLayout.CENTER);
        formTitle.setLabelFor(resultsScrollPane);
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
