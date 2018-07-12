package Models;

import Util.ErrorMessage;
import Util.SQLiteUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User
{
    private int          id;
    private final String name;
    private final String lastname;
    private final String password;
    private final String role;

    public User(int id, String name, String lastname, String password, String role)
    {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.password = password;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public String getLastname()
    {
        return lastname;
    }

    public String getPassword()
    {
        return password;
    }

    public String getRole()
    {
        return role;
    }

    public void saveToDB()
    {
        SQLiteUtil.dbExecuteUpdate("INSERT INTO user (name, lastname, password, role) " +
                                   "VALUES ('" + name     + "', " +
                                           "'" + lastname + "', " +
                                           "'" + password + "', " +
                                           "'" + role     + "');");
    }

    public static boolean userExists(String name, String lastname)
    {
        ResultSet rs;
        String query = "";

        try
        {
            query = "SELECT * FROM user WHERE name = '" + name + "' AND lastname = '" + lastname + "';";
            rs = SQLiteUtil.dbExecuteQuery(query);
            return rs.next();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }
    }

    public static User readUserFromDB(String name, String lastname)
    {
        User user = null;
        ResultSet rs;
        String query = "";
        try
        {
            query = "SELECT * FROM user WHERE name = '" + name + "' AND lastname = '" + lastname + "';";
            rs = SQLiteUtil.dbExecuteQuery(query);

            if (rs.next())
            {
                user = new User(rs.getInt("idUser"),
                                rs.getString("name"),
                                rs.getString("lastname"),
                                rs.getString("password"),
                                rs.getString("role"));
            }

            rs.close();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }

        return user;
    }

    public void deleteFromDB()
    {
        SQLiteUtil.dbExecuteUpdate("DELETE FROM user WHERE name = '" + name + "' AND lastname = '" + lastname + "';");
    }

    public static boolean isLoginSuccessful(String name, String lastname, String password)
    {
        ResultSet rs;
        String query = "";
        boolean result = false;

        try
        {
            query = "SELECT * FROM user WHERE name = '" + name + "' AND lastname = '" + lastname + "' AND password ='" + password + "';";
            rs = SQLiteUtil.dbExecuteQuery(query);

            if (rs.next())
            {
                result = true;
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }
        return result;
    }

    public static User getUserFromDB(String name, String lastname, String password)
    {
        User tempUser = null;
        ResultSet rs;
        String query = "";

        try
        {
            query = "SELECT * FROM user WHERE name = '" + name + "' AND lastname = '" + lastname + "' AND password ='" + password + "';";
            rs = SQLiteUtil.dbExecuteQuery(query);

            if (rs.next())
            {
                tempUser = new User(rs.getInt("IdUser"),
                                    rs.getString("name"),
                                    rs.getString("lastname"),
                                    rs.getString("password"),
                                    rs.getString("role"));
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }
        return tempUser;
    }

    public boolean isDefaultAdmin()
    {
        return name.equals("Администратор") && lastname.equals("Администратор") && role.equals("администратор");
    }
}
