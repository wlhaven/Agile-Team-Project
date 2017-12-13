package codeTigers.UI.Admin;


import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.event.*;
import javax.xml.bind.Element;
import java.awt.event.*;
import java.util.ArrayList;

import codeTigers.BusinessLogic.Test;
import codeTigers.BusinessLogic.TestData;
import codeTigers.Database.Database;
import codeTigers.UI.DroppablePicturePanel;

/*
 *  This class displays the GUI for the Administrator Setup Tools
 *
 * @author  Cameron Bergh
 * @version 2017.11.3
 *
 *
 *
 * Product Owner comments:
 *
 * done:Text box next to Add Item should be blank -.5
 *
 * done:Text box next to Add Item should have focus on start up  -.5
 * jon figured out tha focus thing
 *
 * done Don't allow adding duplicate entry (MAJOR ISSUE) -2
 *
 * done:Don't exit on Cancel Changes; just cancel changes made since last change -1
 *
 * done:Don't allow Finish if only one item in the list (MAJOR ISSUE) -2
 * this was already done by the method checkIfAtLeastTwoItemsInList()
 * which only enables the finish button if there are at least two items.
 * commenting out the setControlsEnabledStatus() method for debugging will cause this
 *
 * done:When item added, clear text box and put focus on it -1
 *
 * done:Don't put text box on screen (test name), if changing it has no effect on the screen result -1
 * note: changed it to uneditable because removing it would be pointless since im going to re add it tomorro
 *
 * :Don't say "Changes successfully added to the database", if no changes made -1
 * :Better message would be "Changes made" since the current message is confusing if I only delete an item (MINOR ISSUE)
 *  done : changed this to the more technically correct: Specified Item List Has been Saved
 *
 * done: alphabetize test items list in listbox
 *
 * done: alphavetize combo box case insenseitve
 *
 * done: make separate save button and exit button
 *
 * done disable image changes after test has been taken
 *
 * done: new test shouldnt be created until finish button
 *
 * done: create new test should be separate buton
 *
 * done: upon creation of new test set focus on test
 *
 * done on add item set focus on textbox and keypress enter event
 *
 * done: order combobox alphabetically and
 *
 * done: ignore case in db
 *
 * done: remove uplaod image button
 *
 * done: delete button doesnt work on new tests
 *
 *
 *
 * 12- 5 changes:
 *
 * many! ++
 * added check for if testname has been used
 *
 * done: open test button
 *
 */

public class AdminSetupGUI {
    private JPanel adminSetupPanel;
    private JButton addItemButton;
    public JTextField itemToAddTextField;
    private JButton deleteSelectedItemButton;
    private JButton cancelButton;

    private Database db = new Database();

    public JList itemsJList; // this is the Jlist
    private JComboBox comboBox1;
    private DroppablePicturePanel droppablePicturePanel1;
    private JButton saveButton;
    private JButton newTestButton;
    private JButton loadButton;
    private JLabel currentTestLabel;

    private BufferedImage myImage = null;

    //holds all the tests
    private ArrayList<Test> testList = new ArrayList<Test>();

    //holds current test
    private Test currentTest = new Test(null, null);

    //this holds the data which is in the Jlist
    //to add or remove data to jlist it must be added to the list model
    private SortedListModel listModel = new SortedListModel();
    private SortedListModel comboModel = new SortedListModel();

    private boolean isTestEditable = false;

    /**
     * Constructor to set up the screen and components
     * and most importantly event listeners
     */
    public AdminSetupGUI() {

        downloadTestListFromDatabase();

        comboBox1.setSelectedIndex(0);

        selectTestFromComboBox();    //selects and downloads selected test, which is whatever is first.

        setControlsEnabledStatus(); // checks to see if test has been taken. NOTE : this must happen after the above line


        /**
         * EVENT HANDLERS
         */

        //this sets a callback
        droppablePicturePanel1.setCallback(() -> picturePanelFileLoadCallBack());

        //Add Item button
        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickTheAddItemButton();
            }

        });

        // cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //todo: maybe move this to its own method

                comboBox1.setEnabled(true);


                downloadTestListFromDatabase();
                refreshCombobox();

                selectTestFromComboBox();

                setControlsEnabledStatus(); // checks to see if test has been taken. NOTE : this must happen after the above line

                downloadTestItemList();


            }
        });

        //delete item button
        deleteSelectedItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteSelectedItem();
            }
        });


        //this nifty little ancestor listener thing
        // was discovered by jon
        itemToAddTextField.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                itemToAddTextField.requestFocusInWindow();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
        });

        //any time the value changes in the listbox
        itemsJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                setControlsEnabledStatus();

                if (itemsJList.getSelectedValue() != null) {
                    displayImageOfSelectedTestItem();
                }


            }
        });

        //save button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SaveChangesToDatabase();
            }
        });

        //create new button clicked
        newTestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewTest();
                droppablePicturePanel1.setImage(null);
            }
        });

        //open test button
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    selectTestFromComboBox();
                    setControlsEnabledStatus();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });

        // keypress in the add item textfield
        itemToAddTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickTheAddItemButton();
            }
        });
    }


    // This is a method used for the callback set for the droppable picture panel
    // this method gets called back by the picture box when the loadfile() method is called
    public void picturePanelFileLoadCallBack() {
        //BufferedImage image = droppablePicturePanel1.getImage();

        if ((itemsJList.isSelectionEmpty() == false) && (isTestEditable == true)) {
            updateItemImage();
            updateCurrentTestLabel();
            setControlsEnabledStatus();
        } else {
            displayImageOfSelectedTestItem();
        }

    }

    //returns root jpanel
    public JPanel getRootPanel() {
        return adminSetupPanel;
    }

    /**
     * gets called by add item button or when someone presses enter on the textfield
     */
    private void clickTheAddItemButton() {
        boolean AlreadyAddedITem = true;

        AlreadyAddedITem = CheckIfListContainsItem(itemToAddTextField.getText());

        if (AlreadyAddedITem == false) {
            addTextBoxToList(itemToAddTextField.getText());

            setControlsEnabledStatus();
        } else {
            JOptionPane.showMessageDialog(null, "Error: cannot add duplicate item to list");
        }

        updateCurrentTestLabel();
        itemToAddTextField.setText("");
        itemToAddTextField.requestFocusInWindow();
    }

    /**
     * keeps the current test label updated
     */
    private void updateCurrentTestLabel() {

        if (currentTest.isModified() == false && currentTest.isNewtest() == false) {
            //update the label
            currentTestLabel.setText("Currently Editing Test: " + currentTest.getTestName());
        } else {
            //update the label
            currentTestLabel.setText("Currently Editing Test: " + currentTest.getTestName() + " (Modified)");
        }

    }

    /**
     * A method which is called when a test name is selected with the
     * combobox, if the test has been changed it will load the local version
     * if it has not, then it will refresh it from the database.
     */
    private void selectTestFromComboBox() {
        try {
            //get selected test name
            String selectedItem = comboBox1.getSelectedItem().toString();

            //make it our current working test object
            currentTest = GetTestObjByName(selectedItem);

            if (currentTest.isModified() == false && currentTest.isNewtest() == false) {
                // download current version
                downloadTestItemList();
            } else {
                //load from memory
                refreshTestItemListbox();
            }

            updateCurrentTestLabel();
            setControlsEnabledStatus();
            droppablePicturePanel1.setImage(null);
        } catch (Exception e) {
            System.out.println("switchtest");
            e.printStackTrace();
        }

    }

    /**
     * Counts how many tests have been modified or are new
     *
     * @return
     */
    private int howManyTestsModifiedOrNew() {
        int counter = 0;
        for (Test i : testList) {   //add our things to sorted list model so it can do the sorting
            if (i.isNewtest() == true || i.isModified() == true) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Takes the image dropped in the droppable picture panel and
     * stores it in the corresponding TestData Object
     */
    private void updateItemImage() {
        String itemName = "";

        itemName = itemsJList.getSelectedValue().toString();

        myImage = droppablePicturePanel1.getImage();

        ArrayList<TestData> testItems = currentTest.getTestItems();

        //find test item in this set
        for (TestData d : testItems) {
            //set the image for that item
            if (d.getItemName().equals(itemName)) { //find our test in the collection
                d.setItemImage(myImage);
            }
        }

        updateCurrentTestLabel();
        currentTest.setTestItems(testItems);
        currentTest.setModified(true);
    }

    /**
     * Reads the selected listbox item and displays the corresponding
     * image in the droppable picture panel.
     */
    private void displayImageOfSelectedTestItem() {
        String itemName = "";
        BufferedImage itemImage = null;
        int listLength = 0;

        try {
            itemName = itemsJList.getSelectedValue().toString();

            //get test items from test object, by name of test
            ArrayList<TestData> testItems = currentTest.getTestItems();

            // search through test item list and find the item selected
            for (TestData i : testItems) {
                if (i.getItemName().equals(itemName)) {
                    itemImage = i.getItemImage(); //return its image
                    break;
                }
            }

            if (itemImage == null) { // if no image returned
                droppablePicturePanel1.setImage(null);

            } else {               //if there is an image
                droppablePicturePanel1.setImage(itemImage);
            }

        } catch (Exception ex) {
            System.out.println("fetchlistselected");
            ex.printStackTrace();
        }
    }

    /**
     * Searches Test Array for a test object with a specified name
     *
     * @param testName
     * @return Test
     */
    private Test GetTestObjByName(String testName) {
        Test result = null;

        for (Test i : testList) { // search through tests to find our test
            if (i.getTestName().equals(testName)) {
                result = i;
                break;
            }
        }
        return result;
    }


    /**
     * Setter Which adds an item to the JLIST
     *
     * @param thingToAdd
     */
    public void addTextBoxToList(String thingToAdd) {
        try {
            //create new item and add to listarray of items
            TestData newItem = new TestData(currentTest.getTestID(), thingToAdd);

            //get current set of things from test object
            ArrayList<TestData> testItems = currentTest.getTestItems();

            //add our new one
            testItems.add(newItem);

            //update the test object with our modified set
            currentTest.setTestItems(testItems);

            //set modified flag
            currentTest.setModified(true);
            currentTest.setTestItems(testItems);

            //refresh the textbox to reflect modified object
            refreshTestItemListbox();


        } catch (Exception err) {
            System.out.println("addtextboxtolist");
            err.printStackTrace();
        }
    }

    /**
     * Method Which removes the selected item from the JLIST
     */
    public void DeleteSelectedItem() {

        String selectedItem = itemsJList.getSelectedValue().toString();
        ArrayList<TestData> testItems = currentTest.getTestItems();

        for (int it = 0; it < testItems.size(); it++) {
            if (testItems.get(it).getItemName().equals(selectedItem)) {
                testItems.remove(it);
                break;
            }
        }

        //update current test with modified list
        currentTest.setTestItems(testItems);
        currentTest.setModified(true);

        updateCurrentTestLabel();
        refreshTestItemListbox();
        setControlsEnabledStatus();
    }


    /**
     * Searches listbox for a given item and returns true if
     * it is already in the list
     *
     * @param itemName
     * @return boolean
     */
    public boolean CheckIfListContainsItem(String itemName) {
        boolean result = false;

        ArrayList<TestData> testItems = currentTest.getTestItems();

        for (TestData i : testItems) { // search through test item list and find the item selected

            if (i.getItemName().equals(itemName)) {
                result = true;
                break;
            }
        }

        return result;
    }


    /**
     * iterates through our Test Collection and uploads any Tests with a "modified" or "new" flag
     * todo: reduce code duplication
     */
    public void SaveChangesToDatabase() {

        if (howManyTestsModifiedOrNew() > 0) {
            try {

                String message = "New or Modified tests saved: \n"; //begin crafting our success message

                for (Test i : testList) { // search through tests to find our test

                    //get test items
                    ArrayList<TestData> testItems = i.getTestItems();

                    //check if we are editing an existing test
                    if (i.isNewtest() == false && i.isModified() == true) {

                        //todo: test this error throwing thing because it works but it seems like it shouldnt
                        if (testItems.size() < 2) {
                            throw new Exception(i.getTestName() + " Contains less than two items.");
                        }

                        // clear the table so we dont duplicate items
                        db.ClearTestDataTable(i.getTestID());

                        for (TestData td : testItems) { // search through test item list and find the item selected
                            BufferedImage myimage = null;
                            myimage = td.getItemImage();

                            if (myimage == null) { // if no image returned
                                db.InsertTestItem(td.getItemName(), td.getTestID());
                            } else {                     //if there is an image present
                                db.InsertTestItemWithImage(td.getItemName(), td.getTestID(), td.getItemImage());
                            }
                        }
                        message = message + i.getTestName() + "\n"; //create our message

                    } else if (i.isNewtest() == true) { //we are saving a new test

                        if (testItems.size() < 2) {
                            throw new Exception(i.getTestName() + " Contains less than two items.");
                        }

                        int newtestID = 0;

                        db.InsertNewTest(i.getTestName());  //create test in db test table

                        //lookup what id number the database has given this new test
                        newtestID = db.lookupTestIDbyName(i.getTestName()); //todo: this line seems gross but it works

                        //update test with its database id number
                        i.setTestID(newtestID);

                        for (TestData ti : testItems) { // search through test item list and find the item selected
                            BufferedImage myimage = ti.getItemImage();

                            if (myimage == null) { // if no image returned
                                db.InsertTestItem(ti.getItemName(), newtestID);

                            } else {                     //if there is an image present
                                db.InsertTestItemWithImage(ti.getItemName(), newtestID, ti.getItemImage());
                            }
                        }
                        message = message + i.getTestName() + "\n"; //create our message
                    }

                    i.setNewtest(false); //reset these flags
                    i.setModified(false);
                }

                downloadTestListFromDatabase();

                //set focus on current test
                comboBox1.setSelectedItem(currentTest.getTestName().toString());

                updateCurrentTestLabel();
                downloadTestItemList();
                setControlsEnabledStatus();

                JOptionPane.showMessageDialog(null, message); //display success message

            } catch (Exception err) { //failure
                JOptionPane.showMessageDialog(null, err.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "There are no changes to save.");
        }


    }

    /**
     * Creates a new Test Object named from the user input and places
     * it in the collection of Test Objects
     */
    public void createNewTest() {
        String name = "";

        name = JOptionPane.showInputDialog(adminSetupPanel, "name?");

        if ((name != null) && (name.length() > 0)) {
            if (name == "" || name.length() < 1) {
                JOptionPane.showMessageDialog(null, "Test Name Must Not Be Blank.");
            } else {

                if (db.doesTestAlreadyExist(name) == false) {

                    isTestEditable = true;

                    Test newTest = new Test(null, name, true, true);

                    //set new test name
                    testList.add(newTest);

                    refreshCombobox();

                    //set controls enabled
                    addItemButton.setEnabled(true);

                    //clear listbox and model
                    listModel.clear();
                    itemsJList.setModel(listModel);

                    //select our new test
                    currentTest = newTest;
                    comboBox1.setSelectedItem(name.toString());

                    updateCurrentTestLabel();
                    setControlsEnabledStatus();

                } else if ((db.doesTestAlreadyExist(name) == true)) { //todo: make case insensitive
                    JOptionPane.showMessageDialog(null, "Specified Test Name Has Already Been Used. Pick Another Name Please.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Specified Test Name Was Invalid.");
        }
    }


    /**
     * Method checks if any user has started the test
     * if they havent it enables the buttons
     */
    public void setControlsEnabledStatus() {
        boolean hasAnyoneTakenThisTest;

        try {

            if (currentTest.isNewtest() == true) { //if its a new test then we dont ask the db
                hasAnyoneTakenThisTest = false;
            } else {
                hasAnyoneTakenThisTest = db.HasUserTakenTest(currentTest.getTestID());
            }

            if (hasAnyoneTakenThisTest == true) {     //if test has been started, disable editing.
                saveButton.setEnabled(false);
                addItemButton.setEnabled(false);
                deleteSelectedItemButton.setEnabled(false);
                isTestEditable = false;
            } else {
                saveButton.setEnabled(true);
                addItemButton.setEnabled(true);
                isTestEditable = true;

                if (listModel.getSize() < 1) {
                    deleteSelectedItemButton.setEnabled(false);
                } else {
                    deleteSelectedItemButton.setEnabled(true);
                }
            }

            if (listModel.getSize() < 2) {
                saveButton.setEnabled(false);
            } else {
                saveButton.setEnabled(true);
            }

            if (howManyTestsModifiedOrNew() > 0) {
                saveButton.setEnabled(true);
            } else {
                saveButton.setEnabled(false);
            }


        } catch (Exception err) { //if error, disable finish button
            saveButton.setEnabled(false);
            addItemButton.setEnabled(false);
            deleteSelectedItemButton.setEnabled(false);
            isTestEditable = false;
            System.out.println("setcontrolstats");
            err.printStackTrace();

        }
    }

    /**
     * Takes the list of Tests and puts it in the combobox
     */
    public void refreshCombobox() {


        try {

            //dirty hack:
            // because the combobox model doesnt have a sort feature
            // but ive already got a sorted list model which is not case sensitive.
            // we convert our list to a sorted list model
            // then to a String array
            // then to combobox model

            SortedListModel newModel = new SortedListModel(); //create new sorted List model
            String[] stringArray = new String[testList.size()]; //create string array

            for (Test i : testList) {   //add our things to sorted list model so it can do the sorting
                newModel.addElement(i.getTestName());
            }

            int index = 0; //now convert our sorted list to a simple string array
            for (int i = 0; i < newModel.getSize(); i++) {
                stringArray[index] = newModel.getElementAt(i).toString();
                index++;
            }

            comboBox1.setModel(new DefaultComboBoxModel(stringArray)); // convert string array to combomodel type and assign it to our combobox
            comboBox1.setEnabled(true);

        } catch (Exception err) {
            System.out.println("refreshcombobox");
            err.printStackTrace();
        }

    }

    /**
     * Gets the List of Tests from the database and calls the method which puts them in the
     * combo box
     */
    public void downloadTestListFromDatabase() {

        try {
            testList = db.lookupTests();
            refreshCombobox();

        } catch (Exception err) {
            System.out.println("downloadlistfromdb");
            err.printStackTrace();
        }

    }

    /**
     * Method which downloads the selected test's Items from database
     */
    public void downloadTestItemList() {

        try {
            currentTest.setTestItems(db.lookupTestItems(currentTest.getTestID()));
            refreshTestItemListbox();

        } catch (Exception err) {
            System.out.println("downloadtestitemlist");
            err.printStackTrace();

        }
    }

    /**
     * Takes the list of test items and puts it into the listbox
     */
    public void refreshTestItemListbox() {
        //todo: code repetition with the above
        try {
            ArrayList<TestData> QuestionsFromDb = new ArrayList<TestData>();
            QuestionsFromDb = currentTest.getTestItems();

            listModel.clear();

            for (TestData t : QuestionsFromDb) {
                // add it also to the listbox's listmodel
                listModel.addElement(t.getItemName());
            }

            //sets listbox to mirror the contents of the list model
            itemsJList.setModel(listModel);

        } catch (Exception err) {
            System.out.println("refresgtestitemlistbox");
            err.printStackTrace();
        }
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
        adminSetupPanel = new JPanel();
        adminSetupPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 1, new Insets(20, 20, 20, 20), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(13, 3, new Insets(20, 20, 20, 20), -1, -1));
        adminSetupPanel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 4, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(400, 400), new Dimension(600, 600), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 5, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        itemsJList = new JList();
        scrollPane1.setViewportView(itemsJList);
        final JLabel label1 = new JLabel();
        label1.setText("Items in Test");
        panel1.add(label1, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBox1 = new JComboBox();
        panel1.add(comboBox1, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loadButton = new JButton();
        loadButton.setText("Open Test");
        panel1.add(loadButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cancelButton = new JButton();
        cancelButton.setText("Cancel All Changes");
        panel1.add(cancelButton, new com.intellij.uiDesigner.core.GridConstraints(11, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saveButton = new JButton();
        saveButton.setText("Save All Changes");
        panel1.add(saveButton, new com.intellij.uiDesigner.core.GridConstraints(12, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Test Name");
        panel1.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        droppablePicturePanel1 = new DroppablePicturePanel();
        panel1.add(droppablePicturePanel1, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(128, 128), null, null, 0, false));
        deleteSelectedItemButton = new JButton();
        deleteSelectedItemButton.setText("Delete Selected Item");
        panel1.add(deleteSelectedItemButton, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currentTestLabel = new JLabel();
        currentTestLabel.setText("Currently Editding Test: ");
        panel1.add(currentTestLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        newTestButton = new JButton();
        newTestButton.setText("Create New Test");
        panel1.add(newTestButton, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        itemToAddTextField = new JTextField();
        itemToAddTextField.setText("");
        panel1.add(itemToAddTextField, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        addItemButton = new JButton();
        addItemButton.setText("Add Item");
        panel1.add(addItemButton, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return adminSetupPanel;
    }
}