package nfn11.xpwars;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*
    https://www.spigotmc.org/wiki/connecting-to-databases-mysql/
*/
public class XPWarsDatabaseManager {
    String host, database, user, password;
    static Connection connection;
    int port;

    public XPWarsDatabaseManager(String database, String host, int port, String user, String password) {
        this.database = database;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://"
                        + this.host + ":" + this.port + "/" + this.database,
                this.user, this.password);
        Statement statement = connection.createStatement();
    }
    
}
