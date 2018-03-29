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

    public static ObservableList<MeltForView> getMeltsFromTill(Date firstDate, Date secondDate)  throws RuntimeException
    {
        ObservableList<MeltForView> list = FXCollections.observableArrayList ();

        MeltForView temp;
        ResultSet rs;
        String query = "";
        try
        {
            query = "SELECT MB.name, mass, date, U.lastname " +
                    "FROM melt M join charge C on M.Charge_idCharge=C.idCharge " +
                    "JOIN meltbrand MB ON MB.idMeltBrand=C.MeltBrand_idMeltBrand " +
                    "JOIN user U ON U.idUser = M.User_idUser " +
                    "WHERE `date`>='"+new java.sql.Date(firstDate.getTime())+"' AND `date` <= '"+new java.sql.Date(secondDate.getTime())+"'";
            rs = SQLiteUtil.dbExecuteQuery(query);
            while (rs.next())
            {
                temp = new MeltForView(rs.getString("name"), rs.getDouble("mass"), format.parse(rs.getString("date")), rs.getString("lastname"));
                list.add(temp);
            }

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

        return list;
    }

    public static ObservableList<MeltForView> getAllMelts()  throws RuntimeException
    {
        ObservableList<MeltForView> list = FXCollections.observableArrayList ();

        MeltForView temp;
        ResultSet rs;
        String query = "";

        try
        {
            query = "SELECT MB.name, mass, M.date, U.lastname " +
                    "FROM melt M join charge C on M.Charge_idCharge=C.idCharge " +
                    "JOIN meltbrand MB ON MB.idMeltBrand=C.MeltBrand_idMeltBrand " +
                    "JOIN user U ON U.idUser = M.User_idUser;";
            rs = SQLiteUtil.dbExecuteQuery(query);
            while (rs.next())
            {
                temp = new MeltForView(rs.getString("name"), rs.getDouble("mass"), format.parse(rs.getString("date")), rs.getString("lastname"));
                list.add(temp);
            }

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

        return list;
    }

    public static ObservableList<MeltForView> getMeltsFrom(Date firstDate)  throws RuntimeException
    {
        ObservableList<MeltForView> list = FXCollections.observableArrayList ();

        MeltForView temp;
        ResultSet rs;
        String query = "";

        try
        {
            query = "SELECT MB.name, mass, `date`, U.lastname " +
                    "FROM melt M join charge C on M.Charge_idCharge=C.idCharge " +
                    "JOIN meltbrand MB ON MB.idMeltBrand=C.MeltBrand_idMeltBrand " +
                    "JOIN user U ON U.idUser = M.User_idUser " +
                    "WHERE `date`>='"+new java.sql.Date(firstDate.getTime())+"'";
            rs = SQLiteUtil.dbExecuteQuery(query);
            while (rs.next())
            {
                temp = new MeltForView(rs.getString("name"), rs.getDouble("mass"), format.parse(rs.getString("date")), rs.getString("lastname"));
                list.add(temp);
            }

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

        return list;
    }

    public static ObservableList<MeltForView> getMeltsTill(Date secondDate)  throws RuntimeException
    {
        ObservableList<MeltForView> list = FXCollections.observableArrayList ();

        MeltForView temp;
        ResultSet rs;
        String query = "";

        try
        {
            query = "SELECT MB.name, mass, `date`, U.lastname " +
                    "FROM melt M join charge C on M.Charge_idCharge=C.idCharge " +
                    "JOIN meltbrand MB ON MB.idMeltBrand=C.MeltBrand_idMeltBrand " +
                    "JOIN user U ON U.idUser = M.User_idUser " +
                    "WHERE `date`<='"+new java.sql.Date(secondDate.getTime())+"'";
            rs = SQLiteUtil.dbExecuteQuery(query);
            while (rs.next())
            {
                temp = new MeltForView(rs.getString("name"), rs.getDouble("mass"), format.parse(rs.getString("date")), rs.getString("lastname"));
                list.add(temp);
            }
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


        return list;
    }
}
