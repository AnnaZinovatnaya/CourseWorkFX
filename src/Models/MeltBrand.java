package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBUtil;
import util.ErrorMessage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MeltBrand
{
    private String        name;
    private List<Element> elements;

    public MeltBrand(String name, List<Element> elements)
    {
        this.name = name;
        this.elements = elements;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Element> getElements()
    {
        return elements;
    }

    public void setElements(List<Element> elements)
    {
        this.elements = elements;
    }

    public static ObservableList<String> getAllBrands() throws RuntimeException
    {
        ObservableList<String> list = FXCollections.observableArrayList ();

        String temp;
        ResultSet rs;
        String query = "";

        try
        {
            query = "SELECT name FROM mydb.meltbrand";
            rs = DBUtil.dbExecuteQuery(query);
            while (rs.next())
            {
                temp = rs.getString("name");
                list.add(temp);
            }
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

    public static MeltBrand getMeltBrand(String name) throws RuntimeException
    {
        MeltBrand temp = new MeltBrand("", new ArrayList<>());
        ResultSet rs;
        ResultSet rs2;
        int idMeltBrand=0;
        String query = "";

        try
        {
            query = "SELECT idMeltBrand, name FROM mydb.meltbrand  WHERE name = '" + name + "'";
            rs = DBUtil.dbExecuteQuery(query);
            if (rs.next())
            {
                temp.setName(rs.getString("name"));
                idMeltBrand = rs.getInt("idMeltBrand");
            }

            query = "SELECT name, minProcent, maxProcent FROM mydb.element E JOIN mydb.elementinbrand EB ON E.idElement = EB.Element_idElement WHERE EB.MeltBrand_idMeltBrand = " + idMeltBrand;
            rs2 = DBUtil.dbExecuteQuery(query);
            while (rs2.next())
            {
                temp.elements.add(new Element(rs2.getString("name"), rs2.getDouble("minProcent"), rs2.getDouble("maxProcent"), 0, 0));
            }
        }
        catch (RuntimeException ex)
        {
            throw ex;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }

        return temp;
    }
}
