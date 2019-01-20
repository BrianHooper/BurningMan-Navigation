package view;

import navigation.Navigator;

/**
 * Interface OptionPaneController
 * <p>
 * Provides method for creating OptionPanes from MenuLabel objects
 *
 * @author Brian Hooper
 * @since 0.9.5
 */
interface OptionPaneController {
    /**
     * Override
     *
     * @param view      view
     * @param navigator navigator
     */
    void createPane(View view, Navigator navigator);
}
