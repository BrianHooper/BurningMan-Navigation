package navigation;

public class NavDriver {
    public static void main(String[] args) {
        Landmarks lm = new Landmarks();
        lm.readCampsTSV();
        System.out.println();
    }
}
