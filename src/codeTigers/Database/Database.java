package codeTigers.Database;

import java.sql.*;
import java.util.Properties;
import java.io.IOException;


/**
 * Database class shared by the Application.
=======
 * codeTigers.BusinessLogic.Database Class for this project
 *
 * This class is shared between each of the different interfaces
 * this file contains methods which they all use and some used only by specific interfaces
 *
 *
 * @author  Jon Grevstad, Cameron Berg, Steve Daily, Wally Haven
 * @version  11/1/2017
 *
 */
public class Database {
    private  String userName;
    private  String password;
    private  String url;
    private Connection mConnection = null;
    private final Properties prop = new Properties();

    public Database() {
        getConnectionInfo();
    }

    private void getConnectionInfo() {
        try {
          //String filename = "mysqlConfig.properties";
            String filename = "MobileSqlServer.properties";
           // String filename = "SQLServerConfig.properties";
            prop.load(getClass().getClassLoader().getResourceAsStream(filename));
            url = prop.getProperty("database");
            userName = prop.getProperty("dbuser");
            password = prop.getProperty("dbpassword");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This connects to database
     */
    protected Connection connect() {
        if (mConnection != null)
            return mConnection;
        try {
            mConnection = DriverManager.getConnection(url, userName, password);
            System.out.println("\nConnected to database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mConnection;
    }

    /**
     * This closes connection to database
     */
    public void close() {
        if (mConnection != null) {
            System.out.println("Closing database connection.\n");
            try {
                mConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void finalize() {
        close();
    }

}

