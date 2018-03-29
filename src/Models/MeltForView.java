package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Util.ErrorMessage;
import Util.SQLiteUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MeltForView
{
    private String brand;
    private double mass;
    private Date date;
    private String lastname;
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public MeltForView(String brand, double mass, Date date, String lastname)
    {
        this.brand = brand;
        this.mass = mass;
        this.date = date;
        this.lastname = lastname;
    }

    public String getBrand()
    {
        return brand;
    }

    public void setBrand(String brand)
    {
        this.brand = brand;
    }

    public double getMass()
    {
        return mass;
    }

    public void setMass(double mass)
    {
        this.mass = mass;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public static ObservableList<MeltForView> getMeltsFromTill(Date startDate, Date endDate)  throws RuntimeException
    {
        ObservableList<MeltForView> melts = FXCollections.observableArrayList ();

        ResultSet rs;
        String query = formQuery(startDate, endDate);
        try
        {
            rs = SQLiteUtil.dbExecuteQuery(query);

            while (rs.next())
            {
                melts.add(new MeltForView(rs.getString("name"), rs.getDouble("mass"), format.parse(rs.getString("date")), rs.getString("lastname")));
            }

            rs.close();

        }
        catch (ParseException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_PARSE_DATE);
        }
        catch (RuntimeException ex)
        {
            throw ex;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }

        return melts;
    }

    private static String formQuery(Date startDate, Date endDate)
    {
        String query = "SELECT MB.name, C.mass, M.date, U.lastname " +
                       "FROM melt M " +
                       "JOIN charge C on M.Charge_idCharge=C.idCharge " +
                       "JOIN meltbrand MB ON MB.idMeltBrand=C.MeltBrand_idMeltBrand " +
                       "JOIN user U ON U.idUser = M.User_idUser ";

        if (startDate != null && endDate != null)
        {
            query += "WHERE `date`>='" + new java.sql.Date(startDate.getTime()) + "' AND `date` <= '" + new java.sql.Date(endDate.getTime()) + "'";
        }
        if (startDate != null && endDate == null)
        {
            query += "WHERE `date`>='" + new java.sql.Date(startDate.getTime()) + "'";
        }
        if (startDate == null && endDate != null)
        {
            query += "WHERE `date`<='" + new java.sql.Date(endDate.getTime()) + "'";
        }

        return query;
    }
}
