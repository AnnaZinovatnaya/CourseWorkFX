package util;

import com.sun.rowset.CachedRowSetImpl;

import java.sql.*;

public class DBUtil {
    //Declare JDBC Driver
    private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";

    //Connection
    private static Connection conn = null;

    private static final String  userName = "root";//username to connect to mysql
    private static final String password = "MySQLPassword";//password to connect to mysql
    private static final String driverClass = "com.mysql.jdbc.Driver";//driver class to connect to mysql
    //needs mysql connector jar
    private static final  String db_url = "jdbc:mysql://localhost:3306/mydb";

    public static void dbConnect(){

        try {
            Class.forName(driverClass);
            conn = DriverManager.getConnection(db_url, userName, password);

            if(!conn.isClosed())
            {
                System.out.println("Connection Successful");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    //Close Connection
    public static void dbDisconnect() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //DB Execute Query Operation
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        //Declare statement, resultSet and CachedResultSet as null
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            dbConnect();

            //Create statement
            stmt = conn.createStatement();

            //Execute select (query) operation
            resultSet = stmt.executeQuery(queryStmt);

            //CachedRowSet Implementation
            //In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
            //We are using CachedRowSet
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (stmt != null) {
                //Close Statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
        //Return CachedRowSet
        return crs;
    }

    //DB Execute Update (For Update/Insert/Delete) Operation
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        //Declare statement as null
        Statement stmt = null;
        try {

            //Connect to DB (Establish Oracle Connection)
            dbConnect();
            //Create Statement
            stmt = conn.createStatement();
            //Run executeUpdate operation with given sql statement
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            if (stmt != null) {
                //Close statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
    }
}