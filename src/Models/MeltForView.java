package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBUtil;

import java.sql.ResultSet;
import java.util.Date;

public class MeltForView
{
    private String brand;
    private double mass;
    private Date   date;

    public MeltForView(String brand, double mass, Date date)
    {
        this.brand = brand;
        this.mass = mass;
        this.date = date;
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

    public static ObservableList<MeltForView> getMeltsFromTill(Date firstDate, Date secondDate)
    {
        ObservableList<MeltForView> list = FXCollections.observableArrayList ();

        MeltForView temp;
        ResultSet rs;

        try
        {
            rs = DBUtil.dbExecuteQuery("SELECT `name`, mass, `date` FROM mydb.melt M join mydb.charge C on M.Charge_idCharge=C.idCharge JOIN mydb.meltbrand MB ON MB.idMeltBrand=C.MeltBrand_idMeltBrand WHERE `date`>='"+new java.sql.Date(firstDate.getTime())+"' AND `date` <= '"+new java.sql.Date(secondDate.getTime())+"'");
            while (rs.next())
            {
                temp = new MeltForView(rs.getString("name"), rs.getDouble("mass"), rs.getDate("date"));
                list.add(temp);
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return list;
    }

    public static ObservableList<MeltForView> getAllMelts()
    {
        ObservableList<MeltForView> list = FXCollections.observableArrayList ();

        MeltForView temp;
        ResultSet rs;


        try
        {
            rs = DBUtil.dbExecuteQuery("SELECT `name`, mass, `date` FROM mydb.melt M join mydb.charge C on M.Charge_idCharge=C.idCharge JOIN mydb.meltbrand MB ON MB.idMeltBrand=C.MeltBrand_idMeltBrand");
            while (rs.next())
            {
                temp = new MeltForView(rs.getString("name"), rs.getDouble("mass"), rs.getDate("date"));
                list.add(temp);
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return list;
    }

    public static ObservableList<MeltForView> getMeltsFrom(Date firstDate)
    {
        ObservableList<MeltForView> list = FXCollections.observableArrayList ();

        MeltForView temp;
        ResultSet rs;


        try
        {
            rs = DBUtil.dbExecuteQuery("SELECT `name`, mass, `date` FROM mydb.melt M join mydb.charge C on M.Charge_idCharge=C.idCharge  JOIN mydb.meltbrand MB ON MB.idMeltBrand=C.MeltBrand_idMeltBrand WHERE `date`>='"+new java.sql.Date(firstDate.getTime())+"'");
            while (rs.next())
            {
                temp = new MeltForView(rs.getString("name"), rs.getDouble("mass"), rs.getDate("date"));
                list.add(temp);
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return list;
    }

    public static ObservableList<MeltForView> getMeltsTill(Date secondDate)
    {
        ObservableList<MeltForView> list = FXCollections.observableArrayList ();

        MeltForView temp;
        ResultSet rs;


        try
        {
            rs = DBUtil.dbExecuteQuery("SELECT `name`, mass, `date` FROM mydb.melt M join mydb.charge C on M.Charge_idCharge=C.idCharge JOIN mydb.meltbrand MB ON MB.idMeltBrand=C.MeltBrand_idMeltBrand  WHERE `date`<='"+new java.sql.Date(secondDate.getTime())+"'");
            while (rs.next())
            {
                temp = new MeltForView(rs.getString("name"), rs.getDouble("mass"), rs.getDate("date"));
                list.add(temp);
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return list;
    }
}
