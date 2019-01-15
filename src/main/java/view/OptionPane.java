package view;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.ArrayList;

class OptionPane {
    private final JPanel panel;
    private final ArrayList<JComponent> jComponents;
    private boolean focusSet = false;
    private boolean okPressed = false;

    /**
     * Constructor
     * <p>
     * Initializes JPanel
     */
    public OptionPane() {
        jComponents = new ArrayList<>();

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    }

    /**
     * Adds a text layout to the panel
     *
     * @param labelText String label text
     */
    public void addLabel(String labelText) {
        panel.add(new JLabel(labelText));
    }

    /**
     * Adds a JComponent to the panel
     * <p>
     * If it is a TextArea, it will be placed in a JScrollPane
     *
     * @param component JComponent
     */
    public void addTextInput(JTextComponent component) {
        if (!focusSet) {
            component.addAncestorListener(new RequestFocusListener());
            focusSet = true;
        }
        jComponents.add(component);
        if (component instanceof OptionPaneTextArea) {
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
     * @param parent     parent JFrame, or null
     * @param title      String panel title
     * @param buttonType int, enum for JOptionPane.OK_CANCEL or other
     */
    public void show(JFrame parent, String title, int buttonType) {
        okPressed = JOptionPane.showConfirmDialog(parent, panel, title, buttonType, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION;
    }

    /**
     * Returns a String array containing the fields of all components
     *
     * @return String array
     */
    public String[] getInputs() {
        String[] inputStrings = new String[jComponents.size()];
        for (int i = 0; i < jComponents.size(); i++) {
            JComponent component = jComponents.get(i);
            if (component instanceof JTextComponent) {
                inputStrings[i] = ((JTextComponent) component).getText();
            } else if (component instanceof JList) {
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
    public void addListInput(ArrayList<String> listElements, int rows) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addAll(listElements);
        JList<String> list = new JList<>(listModel);

        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(rows);
        list.setSelectedIndex(0);
        list.setFont(View.standardFont);

        if (!focusSet) {
            list.addAncestorListener(new RequestFocusListener());
            focusSet = true;
        }

        JScrollPane menuScrollPane = new JScrollPane(list);
        menuScrollPane.setAlignmentX(0);

        panel.add(menuScrollPane);
        jComponents.add(list);
    }

    public void addComponent(JComponent component) {
        component.setAlignmentX(0);
        panel.add(component);
        jComponents.add(component);
    }

    public boolean okPressed() {
        return okPressed;
    }

    public ArrayList<JComponent> getJComponents() {
        return jComponents;
    }
}
