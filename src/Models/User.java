package Models;

import util.DBUtil;
import util.ErrorMessage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User
{
    private String name;
    private String lastname;
    private String password;
    private String role;

    public User(String name, String lastname, String password, String role)
    {
        this.name = name;
        this.lastname = lastname;
        this.password = password;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void saveUser() throws RuntimeException
    {
        try
        {
            DBUtil.dbExecuteUpdate("INSERT INTO mydb.user (`name`, lastname, `password`, role) " +
                                   "VALUES ('" + name + "', '" + lastname + "', '" + password + "', '" + role + "')");
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    public boolean isUser(String name, String lastname) throws RuntimeException
    {
        ResultSet rs;
        try
        {
            rs = DBUtil.dbExecuteQuery("SELECT * FROM mydb.user WHERE `name` = '" + name +
                                       "' AND lastname = '" + lastname + "'");
            return rs.next();
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (SQLException e)
        {
            //TODO: change error message
            throw new RuntimeException(ErrorMessage.CANNOT_CONNECT_TO_DB);
        }
    }

    public static User findUser(String name, String lastname) throws RuntimeException
    {
        User tempUser=null;
        ResultSet rs;
        try
        {
            rs = DBUtil.dbExecuteQuery("SELECT name, lastname, password, role FROM mydb.user WHERE `name` = '" +
                                       name + "' AND lastname = '" + lastname + "'");
            if(rs.next())
            {
                tempUser = new User(rs.getString("name"), rs.getString("lastname"),
                                    rs.getString("password"), rs.getString("role"));
            }
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (SQLException e)
        {
            //TODO: change error message
            throw new RuntimeException(e.getMessage());
        }
        return tempUser;
    }

    public void deleteUser() throws RuntimeException
    {
        try
        {
            DBUtil.dbExecuteUpdate("DELETE FROM mydb.user WHERE name = '" + name + "' AND lastname = '" +
                                   lastname + "' AND password = '" + password + "'");
        } catch (RuntimeException e)
        {
            throw e;
        }
    }

    public static User login(String name, String lastname, String password) throws RuntimeException
    {
        User tempUser=null;
        ResultSet rs;
        try
        {
            rs = DBUtil.dbExecuteQuery("SELECT name, lastname, password, role FROM mydb.user WHERE `name` = '" +
                                       name + "' AND lastname = '" + lastname + "' AND `password` ='" + password + "'");
            if(rs.next())
            {
                tempUser = new User(rs.getString("name"), rs.getString("lastname"),
                                    rs.getString("password"), rs.getString("role"));
            }
        }
        catch (SQLException e)
        {
            //TODO: change error message
            throw new RuntimeException(e.getMessage());
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        return tempUser;
    }


}
