package ltm.ntn;

import ltm.ntn.controllers.HomeController;
import ltm.ntn.network.TCPServer;
import ltm.ntn.views.HomeView;

import javax.swing.*;

public class App 
{
    public static void main( String[] args ) {
        System.out.print("Hello World!");
        // ===== 1. Start GUI trên EDT =====
        SwingUtilities.invokeLater(() -> {
            HomeView homeView = new HomeView();
            HomeController app = new HomeController(homeView);
            app.getView().setVisible(true);
        });

        // ===== 2. Start TCP server trên thread riêng =====
        new Thread(() -> {
            TCPServer server = new TCPServer();
            server.start();
        }, "TCPServerThread").start();
    }
}