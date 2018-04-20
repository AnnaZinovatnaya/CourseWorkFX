package Models;

import Util.ErrorMessage;
import Util.SQLiteUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User
{
    private int    id;
    private String name;
    private String lastname;
    private String password;
    private String role;

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

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLastname()
    {
        return lastname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public void saveToDB() throws RuntimeException
    {
        try
        {
            SQLiteUtil.dbExecuteUpdate("INSERT INTO user (name, lastname, password, role) " +
                                       "VALUES ('" + name     + "', " +
                                               "'" + lastname + "', " +
                                               "'" + password + "', " +
                                               "'" + role     + "');");
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    public static boolean userExists(String name, String lastname) throws RuntimeException
    {
        ResultSet rs;
        String query = "";

        try
        {
            query = "SELECT * FROM user WHERE name = '" + name + "' AND lastname = '" + lastname + "';";
            rs = SQLiteUtil.dbExecuteQuery(query);
            return rs.next();
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }
    }

    public static User readUserFromDB(String name, String lastname) throws RuntimeException
    {
        User user = null;
        ResultSet rs;
        String query = "";
        try
        {
            query = "SELECT * FROM user WHERE name = '" + name + "' AND lastname = '" + lastname + "';";
            rs = SQLiteUtil.dbExecuteQuery(query);

            if(rs.next())
            {
                user = new User(rs.getInt("idUser"),
                                rs.getString("name"),
                                rs.getString("lastname"),
                                rs.getString("password"),
                                rs.getString("role"));
            }

            rs.close();
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }

        return user;
    }

    public void deleteFromDB() throws RuntimeException
    {
        try
        {
            SQLiteUtil.dbExecuteUpdate("DELETE FROM user WHERE name = '" + name + "' AND lastname = '" + lastname + "';");
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    public static User loginAndReturnUser(String name, String lastname, String password) throws RuntimeException
    {
        User tempUser=null;
        ResultSet rs;
        String query = "";

        try
        {
            query = "SELECT * FROM user WHERE name = '" + name + "' AND lastname = '" + lastname + "' AND password ='" + password + "';";
            rs = SQLiteUtil.dbExecuteQuery(query);

            if(rs.next())
            {
                tempUser = new User(rs.getInt("IdUser"),
                                    rs.getString("name"),
                                    rs.getString("lastname"),
                                    rs.getString("password"),
                                    rs.getString("role"));
            }
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }

        return tempUser;
    }

    public boolean isDefaultAdmin()
    {
        if (name.equals("Администратор") && lastname.equals("Администратор") && role.equals("администратор"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
