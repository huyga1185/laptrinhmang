package ltm.ntn.network;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

@Slf4j
public class TCPServer {
    private final int PORT;
    // Cờ kiểm soát loops
    private volatile boolean running = false;
    private ServerSocket serverSocket;

    public TCPServer() {
        try (InputStream input = TCPServer.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            PORT = Integer.parseInt(prop.getProperty("server.port"));
        } catch (Exception e) {
            log.error("Failed to load properties file: ", e);
            throw new RuntimeException("Failed to load properties file: ", e);
        }
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            running = true;
            log.info("Starting TCP server on port {}", PORT);

            while (running) {
                try {
                    Socket socket = serverSocket.accept();
                    new Thread(new ClientHandler(socket)).start();
                } catch (Exception e) {
                    if (running) { // nếu đang stop thì bỏ qua exception
                        log.error("Error accepting client connection", e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to start TCP server: ", e);
        } finally {
            stop(); // đảm bảo socket đóng
        }
    }

    public void stop() {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close(); // unblock accept()
            } catch (Exception e) {
                log.error("Error closing server socket", e);
            }
        }
        log.info("TCP server stopped");
    }
}