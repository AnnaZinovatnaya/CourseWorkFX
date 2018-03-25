package util;
import com.sun.rowset.CachedRowSetImpl;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.*;

public class SQLiteUtil {

    private static Connection conn = null;
    static Statement stmt = null;

    private static boolean structureCreated() {
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM user;");

            if (!rs.next()) {
                //TODO check if this works (return false when user table is empty)
                rs.close();
                stmt.close();
                return false;
            }

            rs.close();
            stmt.close();
            return true;
        } catch ( Exception e ) {
            return false;
        }
    }

    private static void createStructure()
    {
        try
        {
            stmt = conn.createStatement();
            for (String query : DbStructure.getDbStructure())
            {
                stmt.execute(query);
            }

            stmt.close();
        } catch ( Exception e ) {
            throw new RuntimeException(ErrorMessage.CANNOT_CREATE_DB);
        }
    }

    private static void dbConnect() throws RuntimeException
    {
        try
        {
            String url = "jdbc:sqlite://";
            url += new File(".").getAbsoluteFile();
            url = url.replace("\\", "//");
            url = url.substring(0, url.length() - 1);
            url += "charge2.0.db";

            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection(url);

            if (!structureCreated())
            {
                createStructure();
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
}
