package view;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;


/**
 * Class RequestFocusListener
 * <p>
 * Implements AncestorListener
 * <p>
 * Allows JComponents within an OptionPane to request focus
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
class RequestFocusListener implements AncestorListener {
    /**
     * Requests focus
     *
     * @param e AncestorEvent
     */
    @SuppressWarnings("Convert2Lambda")
    public void ancestorAdded(final AncestorEvent e) {
        final AncestorListener al = this;
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JComponent component = e.getComponent();
                component.requestFocusInWindow();
                component.removeAncestorListener(al);
            }
        });
    }

    public void ancestorMoved(AncestorEvent e) {
    }

    public void ancestorRemoved(AncestorEvent e) {
    }
}