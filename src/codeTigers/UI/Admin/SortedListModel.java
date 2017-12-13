package codeTigers.UI.Admin;

/**
 * written by an anonymous author at:
 *
 *          http://www.java2s.com/Tutorials/Java/Swing_How_to/JList/Create_Sorted_List_Model.htm
 *
 *   copied and slightly modified by cameron on 12/5/2017
 *          (added addElement method)
 *          Modified, Set: TreeSet<String>(String.CASE_INSENSITIVE_ORDER);;
 *
 */

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.AbstractListModel;


public class SortedListModel extends AbstractListModel {
    SortedSet<String> model;

    public SortedListModel() {
        model = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);;
    }

    public int getSize() {
        return model.size();
    }

    public Object getElementAt(int index) {
        return model.toArray()[index];
    }

    public void add(String element) {
        if (model.add(element)) {
            fireContentsChanged(this, 0, getSize());
        }
    }

    public void addElement(String element) {
        if (model.add(element)) {
            fireContentsChanged(this, 0, getSize());
        }
    }

    public void clear() {
        model.clear();
        fireContentsChanged(this, 0, getSize());
    }

    public boolean contains(Object element) {
        return model.contains(element);
    }

    public Object firstElement() {
        return model.first();
    }

    public Iterator iterator() {
        return model.iterator();
    }

    public Object lastElement() {
        return model.last();
    }

    public boolean removeElement(Object element) {
        boolean removed = model.remove(element);
        if (removed) {
            fireContentsChanged(this, 0, getSize());
        }
        return removed;
    }
}