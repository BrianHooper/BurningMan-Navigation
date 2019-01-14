package view;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

class RequestFocusListener implements AncestorListener {
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