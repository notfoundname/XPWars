package nfn11.xpwars;

import java.sql.Connection;

public class XPWarsDatabaseManager {
    String host, database, user, password;
    int port;

    public XPWarsDatabaseManager(String database, String host, int port, String user, String password) {
        this.database = database;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }
}
