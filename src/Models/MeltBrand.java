package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Util.ErrorMessage;
import Util.SQLiteUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MeltBrand
{
    private int           id;
    private String        name;
    private String        standard;
    private List<Element> elements;

    public MeltBrand(String name, List<Element> elements)
    {
        this.id = 0;
        this.name = name;
        this.elements = elements;
    }

    public MeltBrand(int id, String name, List<Element> elements) {
        this.id = id;
        this.name = name;
        this.elements = elements;
    }

    public MeltBrand(int id, String name, String standard, List<Element> elements) {
        this.id = id;
        this.name = name;
        this.standard = standard;
        this.elements = elements;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public List<Element> getElements()
    {
        return elements;
    }

    public void setElements(List<Element> elements)
    {
        this.elements = elements;
    }

    public static ObservableList<String> getAllBrandNamesFromDB() throws RuntimeException
    {
        ObservableList<String> list = FXCollections.observableArrayList ();

        String temp;
        ResultSet rs;
        String query = "";

        try
        {
            query = "SELECT name FROM meltbrand";
            rs = SQLiteUtil.dbExecuteQuery(query);
            while (rs.next())
            {
                temp = rs.getString("name");
                list.add(temp);
            }

            rs.close();
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

    public static MeltBrand readMeltBrandFromDB(String name) throws RuntimeException
    {
        MeltBrand resultMeltBrand = new MeltBrand(name, new ArrayList<>());
        ResultSet rs;
        ResultSet rs2;
        String query = "";

        try
        {
            query = "SELECT * FROM meltbrand  WHERE name = '" + name + "'";
            rs = SQLiteUtil.dbExecuteQuery(query);
            if (rs.next())
            {
                resultMeltBrand.setId(rs.getInt("idMeltBrand"));
                resultMeltBrand.setStandard(rs.getString("standard"));
            }

            rs.close();

            query = "SELECT name, minProcent, maxProcent FROM element E JOIN elementinbrand EB ON E.idElement = EB.Element_idElement WHERE EB.MeltBrand_idMeltBrand = " + resultMeltBrand.getId();
            rs2 = SQLiteUtil.dbExecuteQuery(query);
            while (rs2.next())
            {
                resultMeltBrand.elements.add(new Element(rs2.getString("name"), rs2.getDouble("minProcent"), rs2.getDouble("maxProcent"), 0, 0));
            }

            rs2.close();
        }
        catch (RuntimeException ex)
        {
            throw ex;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }

        return resultMeltBrand;
    }
}
