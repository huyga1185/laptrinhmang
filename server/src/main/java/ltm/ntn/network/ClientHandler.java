package ltm.ntn.network;

import lombok.extern.slf4j.Slf4j;
import ltm.ntn.controllers.NetController;

import java.io.*;
import java.net.Socket;

@Slf4j
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final NetController netController;

    public ClientHandler(Socket socket) {
        this.netController = new NetController();
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                int spaceIndex = line.indexOf(' ');
                if (spaceIndex == -1) {
                    writer.write("ERROR: Invalid format\n");
                    writer.flush();
                    continue;
                }

                String command = line.substring(0, spaceIndex);
                String jsonPayload = line.substring(spaceIndex + 1);

                String response = netController.route(command, jsonPayload);

                if (response == null)
                    response = "Error: unknown error\n";

                writer.write(response + "\n");
                writer.flush();
            }
        } catch (Exception e) {
            log.error("Failed to run thread: ", e);
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                log.error("Failed to close socket: ", ex);
            }
        }
    }
}
