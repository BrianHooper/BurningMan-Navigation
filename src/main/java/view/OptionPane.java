package view;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.ArrayList;


/**
 * Class OptionPane
 * <p>
 * Custom container for JOptionPane objects
 * <p>
 * Creates a JPanel with a vertically oriented BoxLayout for obtaining input from the user
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
class OptionPane {
    private final JPanel panel;
    private final ArrayList<JComponent> jComponents;
    private final ArrayList<JList<String>> jLists;
    private boolean focusSet = false;

    /**
     * Constructor
     * <p>
     * Initializes JPanel
     */
    OptionPane() {
        jComponents = new ArrayList<>();
        jLists = new ArrayList<>();

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    }

    /**
     * Adds a text layout to the panel
     *
     * @param labelText String label text
     */
    void addLabel(String labelText) {
        panel.add(new JLabel(labelText));
    }

    /**
     * Adds a JComponent to the panel
     * <p>
     * If it is a TextArea, it will be placed in a JScrollPane
     *
     * @param component JComponent
     */
    void addTextInput(JTextComponent component) {
        if(!focusSet) {
            component.addAncestorListener(new RequestFocusListener());
            focusSet = true;
        }
        jComponents.add(component);
        if(component instanceof OptionPaneTextArea) {
            JScrollPane scrollPane = new JScrollPane(component);
            scrollPane.setAlignmentX(0);
            panel.add(scrollPane);
        } else {
            component.setAlignmentX(0);
            panel.add(component);
        }
    }

    /**
     * Displays the JPanel in a JOptionFrame
     *
     * @param parent parent JFrame, or null
     * @param title  String panel title
     */
    boolean show(JFrame parent, String title) {
        for(JComponent component : jComponents) {
            if(!(component instanceof JLabel)) {
                component.addAncestorListener(new RequestFocusListener());
                break;
            }
        }
        return JOptionPane.showConfirmDialog(parent, panel, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION;
    }

    /**
     * Returns a String array containing the fields of all components
     *
     * @return String array
     */
    String[] getInputs() {
        String[] inputStrings = new String[jComponents.size()];
        for(int i = 0; i < jComponents.size(); i++) {
            JComponent component = jComponents.get(i);
            if(component instanceof JTextComponent) {
                inputStrings[i] = ((JTextComponent) component).getText();
            } else if(component instanceof JList) {
                inputStrings[i] = (String) ((JList) component).getSelectedValue();
            }
        }
        return inputStrings;
    }

    /**
     * Adds a list element for single-interval selection
     *
     * @param listElements ArrayList of Strings for list choices
     * @param rows         number of rows to display in list
     */
    void addListInput(ArrayList<String> listElements, int rows) {
        JList<String> list = new JList<>(listElements.toArray(new String[0]));

        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(rows);
        list.setSelectedIndex(0);
        list.setFont(View.standardFont);

        if(!focusSet) {
            list.addAncestorListener(new RequestFocusListener());
            focusSet = true;
        }

        JScrollPane menuScrollPane = new JScrollPane(list);
        menuScrollPane.setAlignmentX(0);

        panel.add(menuScrollPane);
        jComponents.add(list);
        jLists.add(list);
    }

    /**
     * Adds a component to the panel
     *
     * @param component JComponent object
     */
    void addComponent(JComponent component) {
        component.setAlignmentX(0);
        panel.add(component);
        jComponents.add(component);
    }

    /**
     * Returns the selected index for a specific JList
     *
     * @param index index of jList
     * @return selected index, or -1 if out of bounds
     */
    int getJListSelectedIndex(int index) {
        if(index < 0 || index >= jLists.size()) {
            return -1;
        } else {
            return jLists.get(index).getSelectedIndex();
        }
    }

    /**
     * Returns the selected value for a specific JList
     *
     * @param index index of JList
     * @return selected value, or null if out of bounds
     */
    String getJListSelectedValue(int index) {
        if(index < 0 || index >= jLists.size()) {
            return null;
        } else {
            return jLists.get(index).getSelectedValue();
        }
    }
}
