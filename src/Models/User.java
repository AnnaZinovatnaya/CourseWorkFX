package Models;

import util.DBUtil;

import java.sql.ResultSet;

/**
 * Created by Анюта on 01.11.2016.
 */
public class User {

    private int idUser;
    private String name;
    private String lastname;
    private String password;
    private String role;

    public User(int idUser, String name, String lastname, String password, String role) {
        this.idUser = idUser;
        this.name = name;
        this.lastname = lastname;
        this.password = password;
        this.role = role;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
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

    public int addUser() {
        ResultSet rs = null;
        try {
            rs = DBUtil.dbExecuteQuery("SELECT * FROM mydb.user WHERE `name` = '" + name + "' AND lastname = '" + lastname + "'");

            if (rs.next()) {
                return 1;
            } else {
                DBUtil.dbExecuteUpdate("INSERT INTO mydb.user (`name`, lastname, `password`, role) " +
                        "VALUES ('" + name + "', '" + lastname + "', '" + password + "', '" + role + "')");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return 2;
        }

        return 0;
    }

    public static User findUser(String name, String lastname){
        User tempUser=null;
        ResultSet rs=null;
        try {
            rs = DBUtil.dbExecuteQuery("SELECT * FROM mydb.user WHERE `name` = '"+name+"' AND lastname = '"+lastname+"'");

            if(rs.next()) {
                tempUser = new User(rs.getInt("idUser"), rs.getString("name"), rs.getString("lastname"), rs.getString("password"), rs.getString("role"));
            }


        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return tempUser;
    }

    public boolean deleteUser(){
        try {
            DBUtil.dbExecuteUpdate("DELETE FROM mydb.user WHERE idUser = " + idUser);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    public static User login(String name, String lastname, String password){
        User tempUser=null;
        ResultSet rs=null;
        try {
            rs = DBUtil.dbExecuteQuery("SELECT * FROM mydb.user WHERE `name` = '"+name+"' AND lastname = '"+lastname+"' AND `password` ='"+password+"'");

            if(rs.next()) {
                tempUser = new User(rs.getInt("idUser"), rs.getString("name"), rs.getString("lastname"), rs.getString("password"), rs.getString("role"));
            }


        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return tempUser;
    }


}
