package util;
import com.sun.rowset.CachedRowSetImpl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class SQLiteUtil {

    private static Connection conn = null;
    private static String  DB_URL;
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
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY);
        }
    }



    //TODO make private
    private static void dbConnect() throws RuntimeException
    {
        boolean isConfigurationOK;
        try
        {
            FileReader fileReader = new FileReader("D:\\Charge 2.0\\CourseWorkFX\\src\\configuration.txt");
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
            throw new RuntimeException(ErrorMessage.FILE_NOT_FOUND);
        }
        catch (IOException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_READ_FILE);
        }

        if (isConfigurationOK)
        {
            try
            {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:" + DB_URL);

                if (!structureCreated())
                {
                    createStructure();
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException(ErrorMessage.CANNOT_CONNECT_TO_DB);
            }
        }
        else
        {
            throw new RuntimeException(ErrorMessage.BAD_CONF_FILE);
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
            !(strings[2].split("\""))[0].contains("url")      ||
            !(strings[3].split("\""))[0].contains("sqliteurl"))
        {
            return false;
        }

        DB_URL   = (strings[3].split("\""))[1];

        return true;
    }
}
