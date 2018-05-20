package Util;
import com.sun.rowset.CachedRowSetImpl;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteUtil
{
    private static Connection connection = null;
    private static Statement statement = null;
    private static ResultSet resultSet = null;

    public static ResultSet dbExecuteQuery(String queryStatement)
    {
        resultSet = null;
        CachedRowSetImpl cachedResultSet;
        try
        {
            prepareStatement();
            resultSet = statement.executeQuery(queryStatement);

            cachedResultSet = new CachedRowSetImpl();
            cachedResultSet.populate(resultSet);
        }
        catch (SQLException e)
        {
            throw new RuntimeException (ErrorMessage.CANNOT_EXECUTE_QUERY + queryStatement);
        }
        finally
        {
            closeResultSet();
            closeStatement();
            disconnectDb();
        }
        return cachedResultSet;
    }

    public static void dbExecuteUpdate(String sqlStatement)
    {
        try
        {
            prepareStatement();
            statement.executeUpdate(sqlStatement);
        }
        catch (SQLException e)
        {
            throw new RuntimeException (ErrorMessage.CANNOT_EXECUTE_QUERY + sqlStatement);
        }
        finally
        {
            closeStatement();
            disconnectDb();
        }
    }

    private static void prepareStatement()
    {
        connectDb();
        createDbIfNotCreated();
        createStatement();
    }

    private static void connectDb()
    {
        try
        {
            if (null == connection || connection.isClosed())
            {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(createURL());
            }
        } catch (SQLException | ClassNotFoundException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_CONNECT_TO_DB);
        }
    }

    private static void createDbIfNotCreated()
    {
        if (!isDBCreated())
        {
            createDB();
        }
    }

    private static boolean isDBCreated()
    {
        boolean result;
        try
        {
            createStatement();
            resultSet = statement.executeQuery("SELECT * FROM user;");

            result = resultSet.next();

            closeResultSet();
            closeStatement();
        } catch (SQLException e)
        {
            result = false;
        }
        return result;
    }

    private static void createDB()
    {
        try
        {
            createStatement();
            for (String query : DbStructure.getDbStructure())
            {
                statement.execute(query);
            }
            closeStatement();
        } catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_CREATE_DB);
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

    private static void disconnectDb()
    {
        try
        {
            if (null != connection && !connection.isClosed())
            {
                connection.close();
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_CLOSE_CONNECTION);
        }
    }

    private static void createStatement()
    {
        try
        {
            statement = connection.createStatement();

        } catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_CONNECT_TO_DB);
        }
    }

    private static void closeStatement()
    {
        try
        {
            if (null != statement)
            {
                statement.close();
            }
        } catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_CONNECT_TO_DB);
        }
    }

    private static void closeResultSet()
    {
        try
        {
            if (null != resultSet)
            {
                resultSet.close();
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_CONNECT_TO_DB);
        }
    }
}
