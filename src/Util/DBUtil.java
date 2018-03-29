package Util;

import com.sun.rowset.CachedRowSetImpl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class DBUtil
{
    private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";

    private static Connection conn = null;

    private static String  USERNAME;
    private static String  PASSWORD;
    private static String  DB_URL;

    private static void dbConnect() throws RuntimeException
    {
        boolean isConfigurationOK = false;
        try
        {
            FileReader fileReader = new FileReader("C:\\Users\\Анюта\\IdeaProjects\\CourseWorkFX\\src\\configuration.txt");
            StringBuilder str = new StringBuilder();
            int c;
            while ((c = fileReader.read()) != -1)
            {
                str.append((char) c);
            }

            isConfigurationOK = parseData(str.toString());

        }
        catch (FileNotFoundException e)
        {
            //throw new RuntimeException(ErrorMessage.FILE_NOT_FOUND);
        }
        catch (IOException e)
        {
            //throw new RuntimeException(ErrorMessage.CANNOT_READ_FILE);
        }

        if (isConfigurationOK)
        {
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            }
            catch (Exception e)
            {
                throw new RuntimeException(ErrorMessage.CANNOT_CONNECT_TO_DB);
            }
        }
        else
        {
            //throw new RuntimeException(ErrorMessage.BAD_CONF_FILE);
        }
    }

    private static void dbDisconnect() throws RuntimeException
    {
        try
        {
            if (conn != null && !conn.isClosed())
            {
                conn.close();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_CLOSE_CONNECTION);
        }
    }

    public static ResultSet dbExecuteQuery(String queryStmt) throws RuntimeException
    {
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs;
        try
        {
            if (null == conn || conn.isClosed())
            {
                try
                {
                    dbConnect();
                }
                catch (RuntimeException e)
                {
                    throw e;
                }
            }

            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(queryStmt);
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        }
        catch (SQLException e)
        {
            throw new RuntimeException (ErrorMessage.CANNOT_EXECUTE_QUERY + queryStmt);
        }
        finally
        {
            if (resultSet != null)
            {
                try
                {
                    resultSet.close();
                }
                catch (SQLException e)
                {
                    throw new RuntimeException(ErrorMessage.CANNOT_CONNECT_TO_DB);
                }
            }
            if (stmt != null)
            {
                try
                {
                    stmt.close();
                }
                catch (SQLException e)
                {
                    throw new RuntimeException(ErrorMessage.CANNOT_CONNECT_TO_DB);
                }
            }

            try
            {
                dbDisconnect();
            }
            catch (RuntimeException e)
            {
                throw e;
            }
        }
        return crs;
    }

    public static void dbExecuteUpdate(String sqlStmt) throws RuntimeException
    {
        Statement stmt = null;
        try
        {
            if (null == conn || conn.isClosed())
            {
                try
                {
                    dbConnect();
                }
                catch (RuntimeException e)
                {
                    throw e;
                }
            }

            stmt = conn.createStatement();

            stmt.executeUpdate(sqlStmt);
        }
        catch (SQLException e)
        {
            throw new RuntimeException (ErrorMessage.CANNOT_EXECUTE_QUERY + sqlStmt);
        }
        finally
        {
            if (stmt != null)
            {
                try
                {
                    stmt.close();
                }
                catch (SQLException e)
                {
                    throw new RuntimeException(ErrorMessage.CANNOT_CONNECT_TO_DB);
                }
            }

            try
            {
                dbDisconnect();
            }
            catch (RuntimeException e)
            {
                throw e;
            }
        }
    }

    private static boolean parseData(String string)
    {
        String strings[] = string.split("\n");

        if (!(strings[0].split("\""))[0].contains("username") ||
                !(strings[1].split("\""))[0].contains("password") ||
                !(strings[2].split("\""))[0].contains("url"))
        {
            return false;
        }

        USERNAME = (strings[0].split("\""))[1];
        PASSWORD = (strings[1].split("\""))[1];
        DB_URL   = (strings[2].split("\""))[1];

        return true;
    }
}