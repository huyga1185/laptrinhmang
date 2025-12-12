package ltm.ntn.network;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

@Slf4j
public class TCPServer {
    private final int PORT;

    public TCPServer() {
        try (InputStream input = TCPServer.class .getClassLoader() .getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            PORT = Integer.parseInt(prop.getProperty("server.port"));
        } catch (Exception e) {
            log.error("Failed to load properties file: ", e);
            throw new RuntimeException("Failed to load properties file: ", e);
        }
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            log.info("Starting TCP server on port {}", PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (Exception e) {
            log.error("Failed to start TCP server: ", e);
            throw new RuntimeException("Failed to start TCP server: ", e);
        }
    }
}
