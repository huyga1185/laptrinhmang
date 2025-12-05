package ltm.ntn;

import ltm.ntn.controllers.HomeController;
import ltm.ntn.views.HomeView;

public class App 
{
    public static void main( String[] args ) {
        System.out.print("Hello World!");
        HomeView homeView = new HomeView();
        HomeController app = new HomeController(homeView);
        app.getView().setVisible(true);
    }
}