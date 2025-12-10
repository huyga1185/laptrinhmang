package com.ltm.ntn.networks;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

@Slf4j
public class TCPClient {
    private final String host;
    private final int port;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public TCPClient() {
        try (InputStream input = TCPClient.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            Properties prop = new Properties();
            prop.load(input);

            this.host = prop.getProperty("server.host");
            this.port = Integer.parseInt(prop.getProperty("server.port"));

        } catch (Exception e) {
            log.error("Failed to load properties: ", e);
            throw new RuntimeException("Failed to load properties", e);
        }
    }

    /**
     * Kết nối đến server
     */
    public void connect() throws IOException {
        socket = new Socket(host, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        log.info("Connected to server at {}:{}", host, port);
    }

    /**
     * Gửi request đến server và nhận response
     * @param command Lệnh (CREATE_INVOICE, GET_ALL_INVOICES, GET_INVOICE)
     * @param jsonPayload Dữ liệu JSON
     * @return Response từ server
     */
    public String sendRequest(String command, String jsonPayload) throws IOException {
        if (socket == null || socket.isClosed()) {
            connect();
        }

        // Format: COMMAND JSON_PAYLOAD
        String request = command + " " + jsonPayload;

        writer.write(request);
        writer.newLine();
        writer.flush();

        log.info("Sent request: {} {}", command, jsonPayload);

        // Đọc response
        StringBuilder response = new StringBuilder();
        String line = reader.readLine();

        if (line != null) {
            response.append(line);
        }

        log.info("Received response: {}", response);
        return response.toString();
    }

    /**
     * Đóng kết nối
     */
    public void disconnect() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
            log.info("Disconnected from server");
        } catch (IOException e) {
            log.error("Error closing connection: ", e);
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}
