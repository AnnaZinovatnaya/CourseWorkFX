package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Util.ErrorMessage;
import Util.SQLiteUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Element
{
    private String name;
    private double minPercent;
    private double maxPercent;
    private double percent;
    private double adopt;

    public Element(String name, double minPercent, double maxPercent, double percent, double adopt)
    {
        this.name = name;
        this.minPercent = minPercent;
        this.maxPercent = maxPercent;
        this.percent = percent;
        this.adopt = adopt;
    }

    public Element(Element element)
    {
        this.name = element.getName();
        this.minPercent = element.getMinPercentDouble();
        this.maxPercent = element.getMaxPercentDouble();
        this.percent = element.getPercent();
        this.adopt = element.getAdopt();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getMinPercent()
    {
        return String.valueOf(minPercent);
    }

    public String getMaxPercent()
    {
        return String.valueOf(maxPercent);
    }

    public double getMinPercentDouble()
    {
        return minPercent;
    }

    public void setMinPercent(double minPercent)
    {
        this.minPercent = minPercent;
    }

    public double getMaxPercentDouble()
    {
        return maxPercent;
    }

    public void setMaxPercent(double maxPercent)
    {
        this.maxPercent = maxPercent;
    }

    public double getPercent()
    {
        return percent;
    }

    public void setPercent(double percent)
    {
        this.percent = percent;
    }

    public double getAdopt()
    {
        return adopt;
    }

    public void setAdopt(double adopt)
    {
        this.adopt = adopt;
    }

    public static ObservableList<String> getAllElements() throws RuntimeException
    {
        ObservableList<String> elementNames = FXCollections.observableArrayList ();
        String query = "";

        try
        {
            ResultSet rs;
            query = "SELECT name FROM element";
            rs = SQLiteUtil.dbExecuteQuery(query);
            while(rs.next())
            {
                elementNames.add(rs.getString("name"));
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

        return elementNames;
    }

    public void saveElementInComponent(int idComponent) throws RuntimeException
    {
        ResultSet rs;
        int idElement = 0;
        String query = "";

        try
        {
            query = "SELECT idElement FROM element WHERE name = '" + name + "'";
            rs = SQLiteUtil.dbExecuteQuery(query);
            if(rs.next())
            {
                idElement = rs.getInt("idElement");
            }

            rs.close();

            query = "INSERT INTO elementincomponent (procent, Element_idElement, Component_idComponent, adopt) " +
                    "VALUES ('" + percent + "', '" + idElement + "', '" + idComponent + "', '" + adopt + "')";
            SQLiteUtil.dbExecuteUpdate(query);
        }
        catch (RuntimeException ex)
        {
            throw ex;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }
    }
}
