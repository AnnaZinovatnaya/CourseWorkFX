package Util;
import com.sun.rowset.CachedRowSetImpl;

import java.io.*;
import java.sql.*;

public class SQLiteUtil
{
    private static Connection connection = null;
    private static Statement statement = null;

    private static boolean isDBCreated()
    {
        try
        {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM user;");

            if (!rs.next())
            {
                //TODO check if this works (return false when user table is empty)
                rs.close();
                statement.close();
                return false;
            }

            rs.close();
            statement.close();
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    private static void createDB()
    {
        try
        {
            statement = connection.createStatement();
            for (String query : DbStructure.getDbStructure())
            {
                statement.execute(query);
            }

            statement.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(ErrorMessage.CANNOT_CREATE_DB);
        }
    }

    private static void dbConnect() throws RuntimeException
    {
        try
        {
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection(createURL());

            if (!isDBCreated())
            {
                createDB();
            }
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_CONNECT_TO_DB);
        }
    }

    private static String createURL()
    {
        String url = "jdbc:sqlite://";
        url += new File(".").getAbsoluteFile();
        url = url.replace("\\", "//");
        url = url.substring(0, url.length() - 1);
        url += "charge2.0.db";
        return url;
    }

    private static void dbDisconnect() throws RuntimeException
    {
        try
        {
            if (connection != null && !connection.isClosed())
            {
                connection.close();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_CLOSE_CONNECTION);
        }
    }

    public static ResultSet dbExecuteQuery(String queryStatement) throws RuntimeException
    {
        statement = null;
        ResultSet resultSet = null;
        CachedRowSetImpl cachedResultSet;
        try
        {
            tryConnectingToDB();

            statement = connection.createStatement();
            resultSet = statement.executeQuery(queryStatement);
            cachedResultSet = new CachedRowSetImpl();
            cachedResultSet.populate(resultSet);
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (SQLException e)
        {
            throw new RuntimeException (ErrorMessage.CANNOT_EXECUTE_QUERY + queryStatement);
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
            if (statement != null)
            {
                try
                {
                    statement.close();
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
        return cachedResultSet;
    }

    public static void dbExecuteUpdate(String sqlStatement) throws RuntimeException
    {
        statement = null;
        try
        {
            tryConnectingToDB();

            statement = connection.createStatement();
            statement.executeUpdate(sqlStatement);
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (SQLException e)
        {
            throw new RuntimeException (ErrorMessage.CANNOT_EXECUTE_QUERY + sqlStatement);
        }
        finally
        {
            if (statement != null)
            {
                try
                {
                    statement.close();
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

    private static void tryConnectingToDB() throws RuntimeException
    {
        try
        {
            if (null == connection || connection.isClosed())
            {
                dbConnect();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException (ErrorMessage.CANNOT_CONNECT_TO_DB);
        }
    }
}
