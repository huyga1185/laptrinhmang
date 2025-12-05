package ltm.ntn.share;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class DBConnection {
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        try(InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("applications.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            URL =  prop.getProperty("db.url");
            USER =  prop.getProperty("db.user");
            PASSWORD =  prop.getProperty("db.pass");
        } catch (Exception e) {
            log.error("Failed to load properties file: ", e);
            throw new RuntimeException("Failed to load properties file: ", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
