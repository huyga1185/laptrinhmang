package com.ltm.ntn;

import lombok.extern.slf4j.Slf4j;
import com.ltm.ntn.controllers.MainController;

import javax.swing.*;

@Slf4j
public class App {
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            log.warn("Could not set system look and feel", e);
        }

        // Run on EDT
        SwingUtilities.invokeLater(() -> {
            try {
                MainController controller = new MainController();

                // Add shutdown hook
                Runtime.getRuntime().addShutdownHook(new Thread(controller::shutdown));

                controller.show();

                log.info("Client application started successfully");

            } catch (Exception e) {
                log.error("Failed to start application: ", e);
                JOptionPane.showMessageDialog(null,
                        "Không thể khởi động ứng dụng!\n" + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}