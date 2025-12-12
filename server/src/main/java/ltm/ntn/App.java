package ltm.ntn;

import lombok.extern.slf4j.Slf4j;
import ltm.ntn.controllers.HomeController;
import ltm.ntn.network.TCPServer;
import ltm.ntn.views.HomeView;

import javax.swing.*;

@Slf4j
public class App {
    public static void main(String[] args) {
        TCPServer server = new TCPServer();

        // Start TCP server trên thread riêng
        Thread serverThread = new Thread(server::start, "TCPServerThread");
        serverThread.start();

        // Start GUI trên EDT
        SwingUtilities.invokeLater(() -> {
            HomeView homeView = new HomeView();
            HomeController app = new HomeController(homeView);

            homeView.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    // Stop TCP server trước khi exit
                    server.stop();
                    System.exit(0); // đảm bảo JVM tắt
                }
            });

            app.getView().setVisible(true);
        });
    }
}
