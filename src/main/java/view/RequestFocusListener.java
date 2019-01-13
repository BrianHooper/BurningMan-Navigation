package view;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * Convenience class to request focus on a component.
 * <p>
 * When the component is added to a realized Window then component will
 * request focus immediately, since the ancestorAdded event is fired
 * immediately.
 * <p>
 * When the component is added to a non realized Window, then the focus
 * request will be made once the window is realized, since the
 * ancestorAdded event will not be fired until then.
 * <p>
 * Using the default constructor will cause the listener to be removed
 * from the component once the AncestorEvent is generated. A second constructor
 * allows you to specify a boolean value of false to prevent the
 * AncestorListener from being removed when the event is generated. This will
 * allow you to reuse the listener each time the event is generated.
 */
public class RequestFocusListener implements AncestorListener {
    public void ancestorAdded(final AncestorEvent e) {
        final AncestorListener al = this;
        SwingUtilities.invokeLater(() -> {
            JComponent component = e.getComponent();
            component.requestFocusInWindow();
            component.removeAncestorListener(al);
        });
    }

    public void ancestorMoved(AncestorEvent e) {
    }

    public void ancestorRemoved(AncestorEvent e) {
    }
}
