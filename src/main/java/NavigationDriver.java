public class NavigationDriver {

    public static void main(String[] args) {
        Location location = new Location(6,0,'K');
        Landmarks landmarks = new Landmarks();
        landmarks.readCamps("camps.csv");

        View view = new View();
        Controller controller = new Controller(location, landmarks, view);
        view.setActionHandler(new ActionHandler(controller));
        view.buildLayout();
//        controller.updateView();

    }


}
