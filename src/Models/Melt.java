package Models;

import Util.ErrorMessage;
import Util.SQLiteUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


public class Melt
{
    private User   user;
    private Charge charge;
    private Date   date;

    public Melt(User user, Charge charge, Date date)
    {
        this.user = user;
        this.charge = charge;
        this.date = date;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Charge getCharge()
    {
        return charge;
    }

    public void setCharge(Charge charge)
    {
        this.charge = charge;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public static int getMaxIdFromDB()
    {
        int id = 0;
        try
        {
            ResultSet rs = SQLiteUtil.dbExecuteQuery("SELECT max(idMelt) FROM melt;");
            if (rs.next())
            {
                id = rs.getInt("max(idMelt)");
            }

            rs.close();

        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + "SELECT max(idMelt) FROM melt;");
        }

        return id;
    }

    public void saveToDB()
    {
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        SQLiteUtil.dbExecuteUpdate("INSERT INTO melt (date, Charge_idCharge, User_idUser)\n" +
                "VALUES ('" + sqlDate + "', '" + this.charge.getId() + "', '" + this.user.getId() +"');");
    }
}
