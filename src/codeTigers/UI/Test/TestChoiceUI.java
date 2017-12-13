package codeTigers.UI.Test;

import codeTigers.BusinessLogic.Test;
import codeTigers.BusinessLogic.User;
import codeTigers.Database.Database;
import codeTigers.Main;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * Test Choice GUI
 *
 * @author Jon Grevstad
 * @version 11/22/2017
 * Modified     11/28/2017
 * - Added arrow selection prevention in drop down list
 * - Made drop down initial field
 * - Used Steve's drop down color code
 */
public class TestChoiceUI {
    private JPanel rootPanel;
    private JLabel greetLabel;
    private JTable testTable;
    private JComboBox testComboBox;
    private JButton beginTestButton;

    /**
     * Build test choice form with user data parameter
     *
     * @param u user
     */
    public TestChoiceUI(User u) {
        Database database = new Database();

        greetLabel.setText("Hello " + u.getFName());
        String[] colNames = {"Test Name", "Taken"};

        // Set up Table Model
        DefaultTableModel myDefaultTableModel = new DefaultTableModel() {
            // Editable boolean
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        // Add table header to model
        myDefaultTableModel.setColumnIdentifiers(colNames);

        // Set int's for query
        int rowCount = 0;
        int sessID;

        // Build table & query tests user has taken
        for (Object[] row : database.getTestRow()) {
            int userID = u.getUserID();
            int testID = (Integer) row[1];
            myDefaultTableModel.addRow(row);
            // check to see if the user has taken the test before
            sessID = database.getSessionID(userID, testID);
            // Set query result in 2nd column
            if (sessID != 0) {
                myDefaultTableModel.setValueAt("Yes", rowCount, 1);
            } else {
                myDefaultTableModel.setValueAt("No", rowCount, 1);
            }
            // Increase row count incrementally
            rowCount++;
        }
        testTable.setModel(myDefaultTableModel);
        testTable.setFocusable(false);
        testTable.setRowSelectionAllowed(false);

        // Set up table cell & columns structure
        DefaultTableCellRenderer centRend = new DefaultTableCellRenderer();
        centRend.setHorizontalAlignment(SwingConstants.CENTER);

        TableColumnModel columnModel = testTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(150);
        columnModel.getColumn(1).setCellRenderer(centRend);
        columnModel.getColumn(1).setPreferredWidth(50);


        // Set up pull down menu background color
        testComboBox.setBackground(new Color(255, 255, 255));
        // Need special stuff to set background color of selected item
        Object child = testComboBox.getAccessibleContext().getAccessibleChild(0);
        ((BasicComboPopup) child).getList().setSelectionBackground(new Color(240, 255, 240));
        // Need to prevent selection when using arrow keys
        testComboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);

        testComboBox.addItem("<html><i>-- Select Test --</i>");
        for (Object[] rows : database.getTestRow()) {
            String testNam = (String) rows[0];
            testComboBox.addItem(testNam);
        }

        // Map tests for drop down pass
        HashMap<String, Integer> test = database.populateCombo();

        beginTestButton.setEnabled(false);
        testComboBox.addActionListener(e -> {
            JComboBox tstSelect = (JComboBox) e.getSource();
            String tstNam = (String) tstSelect.getSelectedItem();
            int tid = tstSelect.getSelectedIndex();
            if (tid == 0) {
                beginTestButton.setEnabled(false);
            } else {
                beginTestButton.setEnabled(true);
                Test t = new Test(test.get(tstNam));
                t.setTestName(tstNam);
                Main.setTest(t);
                beginTestButton.requestFocus();
            }
        });

        // Set initial field to drop down list
        testComboBox.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                testComboBox.requestFocusInWindow();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
        });


        beginTestButton.addActionListener(e -> Main.showUserTakingTest());

        beginTestButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    Main.showUserTakingTest();
                }
            }
        });
    }

    /**
     * Returns rootPanel
     *
     * @return rootPanel
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
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 1, new Insets(10, 10, 10, 10), -1, -1));
        rootPanel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(20, 10, 10, 10), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        testTable = new JTable();
        testTable.setPreferredScrollableViewportSize(new Dimension(250, 150));
        scrollPane1.setViewportView(testTable);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 24, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Tests Taken");
        panel2.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        greetLabel = new JLabel();
        Font greetLabelFont = this.$$$getFont$$$(null, -1, 14, greetLabel.getFont());
        if (greetLabelFont != null) greetLabel.setFont(greetLabelFont);
        greetLabel.setText("");
        panel1.add(greetLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, 24, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Test Selection");
        panel3.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        testComboBox = new JComboBox();
        panel3.add(testComboBox, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, -1), null, new Dimension(256, -1), 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(10, 0, 0, 0), -1, -1));
        panel1.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        beginTestButton = new JButton();
        beginTestButton.setEnabled(false);
        beginTestButton.setText("Begin Test");
        panel4.add(beginTestButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
