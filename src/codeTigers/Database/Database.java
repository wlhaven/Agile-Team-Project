package codeTigers.Database;

import java.sql.*;


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
    private static final String SERVER = "cisdbss.pcc.edu";
    private static final String DATABASE = "234a_CodeTigers";
    private static final String USERNAME = "234a_CodeTigers";
    private static final String PASSWORD = "me0wme0wme0w";
    private static final String CONN_STRING = "jdbc:jtds:sqlserver://" + SERVER + "/" + DATABASE;

    private static final String userName = "root";
    private static final String password = "Blazer99";
    private static final String url = "jdbc:mysql://localhost/codetigers?autoReconnect=true&useSSL=false";

    private static final String CODETIGERS_DB = "jdbc:sqlite:CodeTigers.db";
    private Connection mConnection = null;

    /**
     * This connects to database
     */
    protected Connection connect() {
        if (mConnection != null)
            return mConnection;
        try {
            // MYSQL connection
            mConnection = DriverManager.getConnection(url, userName, password);

            //SQL Server connection
           // mConnection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);

            //SQLite Connection
           // mConnection = DriverManager.getConnection(CODETIGERS_DB );

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

