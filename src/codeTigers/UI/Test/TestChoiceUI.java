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
}
