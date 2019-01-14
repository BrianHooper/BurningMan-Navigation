package driver;

import view.UserInterfaceController;


class NavigationDriver {

    private void run() {
        UserInterfaceController.start();
    }

    public static void main(String[] args) {
        new NavigationDriver().run();
    }
}
