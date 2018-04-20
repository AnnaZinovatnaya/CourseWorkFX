package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Util.ErrorMessage;
import Util.SQLiteUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Component
{
    private int           id;
    private String        name;
    private String        brand;
    private double        adoptBase;
    private double        amount;
    private double        price;
    private int           mandatory;
    private double        adoptComp;
    private List<Element> elements;

    public Component(String name, String brand, double adoptBase, double amount, double price, int mandatory, double adoptComp, List<Element> elements)
    {
        this.name = name;
        this.brand = brand;
        this.adoptBase = adoptBase;
        this.amount = amount;
        this.price = price;
        this.mandatory = mandatory;
        this.adoptComp = adoptComp;
        this.elements = elements;
    }

    public Component(int id, String name, String brand, double adoptBase, double amount, double price, int mandatory, double adoptComp, List<Element> elements) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.adoptBase = adoptBase;
        this.amount = amount;
        this.price = price;
        this.mandatory = mandatory;
        this.adoptComp = adoptComp;
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

    public String getBrand()
    {
        return brand;
    }

    public void setBrand(String brand)
    {
        this.brand = brand;
    }

    public double getAdoptBase()
    {
        return adoptBase;
    }

    public void setAdoptBase(double adoptBase)
    {
        this.adoptBase = adoptBase;
    }

    public String getAmount()
    {
        return String.valueOf(amount);
    }

    public double getAmountDouble()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public String getPrice()
    {
        return String.valueOf(price);
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public double getPriceDouble()
    {
        return price;
    }

    public int getMandatory()
    {
        return mandatory;
    }

    public void setMandatory(int mandatory)
    {
        this.mandatory = mandatory;
    }

    public double getAdoptComp()
    {
        return adoptComp;
    }

    public void setAdoptComp(double adoptComp)
    {
        this.adoptComp = adoptComp;
    }

    public List<Element> getElements()
    {
        return elements;
    }

    public void setElements(List<Element> elements)
    {
        this.elements = elements;
    }

    public boolean componentExists(String name) throws RuntimeException
    {
        String query = "SELECT * FROM component WHERE name = '" + name + "'";

        try
        {
            ResultSet rs = SQLiteUtil.dbExecuteQuery(query);

            if (rs.next())
            {
                return true;
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

        return  false;
    }

    public void saveComponentGeneralInfo() throws RuntimeException
    {
        this.calculateAdopt();

        String query = "INSERT INTO component (name, brand, adoptBase, currentAmount, currentPrice, mandatory, adoptComp) " +
                "VALUES ('" + name + "', '" + brand + "', '" + adoptBase + "', '" + amount + "', '" + price + "', '" + mandatory + "', '" + adoptComp + "')";
        try
        {
            SQLiteUtil.dbExecuteUpdate(query);
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    public void setComponentElement(String name, double percent, double adopt)
    {
        elements.add(new Element(name, 0, 0, percent, adopt));
    }

    public void saveComponentElementsInfo() throws RuntimeException
    {
        ResultSet rs;
        int id = 0;
        String query = "SELECT idComponent FROM component WHERE name = '" + name + "'";
        try
        {
            rs = SQLiteUtil.dbExecuteQuery(query);
            if (rs.next())
            {
                id = rs.getInt("idComponent");
            }
            rs.close();

            for (Element element : elements)
            {
                element.saveElementInComponent(id);
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
    }

    public static ObservableList<String> getAllMandatoryComponentNames() throws RuntimeException
    {
        ObservableList<String> names = FXCollections.observableArrayList ();

        String temp;
        ResultSet rs;
        String query = "SELECT name FROM component WHERE mandatory = 1";
        try
        {
            rs = SQLiteUtil.dbExecuteQuery(query);
            while (rs.next())
            {
                temp = rs.getString("name");
                names.add(temp);
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

        return names;
    }

    public static ObservableList<String> getAllOptionalComponentNames() throws RuntimeException
    {
        ObservableList<String> names = FXCollections.observableArrayList ();

        String temp;
        ResultSet rs;
        String query = "SELECT name FROM component WHERE mandatory = 0";

        try
        {
            rs = SQLiteUtil.dbExecuteQuery(query);
            while (rs.next())
            {
                temp = rs.getString("name");
                names.add(temp);
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

        return names;
    }

    public static ObservableList<Component> getAllMandatoryComponents() throws RuntimeException
    {
        ObservableList<Component> components = FXCollections.observableArrayList ();

        ResultSet rs;
        ResultSet rs2;
        int i = 0;
        String query = "";

        try
        {
            query = "SELECT * FROM component WHERE mandatory = 1";
            rs = SQLiteUtil.dbExecuteQuery(query);
            while (rs.next())
            {
                Component component = new Component(rs.getInt("idComponent"),
                                                    rs.getString("name"),
                                                    rs.getString("brand"),
                                                    rs.getDouble("adoptBase"),
                                                    rs.getDouble("currentAmount"),
                                                    rs.getDouble("currentPrice"),
                                                    1,
                                                    rs.getDouble("adoptComp"),
                                                    new ArrayList<>());
                components.add(component);

                query = "SELECT * FROM elementincomponent JOIN element ON idElement = Element_idElement WHERE Component_idComponent = " + component.getId();
                rs2 = SQLiteUtil.dbExecuteQuery(query);
                while (rs2.next())
                {
                    components.get(i).elements.add(new Element(rs2.getString("name"), 0, 0, rs2.getDouble("procent"), rs2.getDouble("adopt")));
                }
                i++;
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

        return components;
    }

    public static ArrayList<Component> getAllOptionalComponents() throws RuntimeException
    {
        ArrayList<Component> components = new ArrayList<>();

        ResultSet rs;
        ResultSet rs2;
        int i = 0;
        String query = "";

        try
        {
            query = "SELECT * FROM component WHERE mandatory = 0";
            rs = SQLiteUtil.dbExecuteQuery(query);
            while (rs.next())
            {
                Component component = new Component(rs.getInt("idComponent"),
                                                    rs.getString("name"),
                                                    rs.getString("brand"),
                                                    rs.getDouble("adoptBase"),
                                                    rs.getDouble("currentAmount"),
                                                    rs.getDouble("currentPrice"),
                                                    0,
                                                    rs.getDouble("adoptComp"),
                                                    new ArrayList<>());

                components.add(component);

                query = "SELECT * FROM elementincomponent JOIN element ON idElement = Element_idElement WHERE Component_idComponent = " + component.getId();
                rs2 = SQLiteUtil.dbExecuteQuery(query);
                while (rs2.next())
                {
                    components.get(i).elements.add(new Element(rs2.getString("name"), 0, 0, rs2.getDouble("procent"), rs2.getDouble("adopt")));
                }
                i++;
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

        return components;
    }

    private void calculateAdopt()
    {
        double temp = 100;
        for(Element aElement: elements)
        {
            temp -= aElement.getPercent();
        }
        temp /= adoptBase;

        for(Element aElement: elements)
        {
            temp += aElement.getPercent()/aElement.getAdopt();
        }

        adoptComp = 1/temp;
    }

    public void deleteFromDB() throws RuntimeException
    {
        String query = "";
        try
        {
            query = "SELECT * FROM component WHERE name = '" + this.name +"';";
            ResultSet rs = SQLiteUtil.dbExecuteQuery(query);
            rs.next();
            query = "DELETE FROM elementincomponent WHERE `Component_idComponent`='" + rs.getInt("idComponent")+"';";
            SQLiteUtil.dbExecuteUpdate(query);
            query = "DELETE FROM component WHERE `name` = '" + this.name + "';";
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

    public static Component readComponentFromDB(String name) throws RuntimeException
    {
        Component component = null;
        int idComponent;
        String query = "";
        try
        {
            query = "SELECT * FROM component WHERE name = '" + name + "'";
            ResultSet rs = SQLiteUtil.dbExecuteQuery(query);
            rs.next();
            component = new Component(name, rs.getString("brand"), rs.getDouble("adoptBase"), rs.getDouble("currentAmount"), rs.getDouble("currentPrice"), rs.getInt("mandatory"), rs.getDouble("adoptComp"), new ArrayList<>());
            idComponent = rs.getInt("idComponent");
            query = "SELECT `name`, procent, adopt FROM element E JOIN elementincomponent EC ON E.idElement=EC.Element_idElement WHERE EC.Component_idComponent='"+idComponent+"';";
            ResultSet rs2 = SQLiteUtil.dbExecuteQuery(query);

            while (rs2.next())
            {
                component.getElements().add(new Element(rs2.getString("name"), 0, 0, rs2.getDouble("procent"), rs2.getDouble("adopt")));
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

        return component;
    }

    public void update() throws RuntimeException
    {
        String query = "";
        try
        {
            query = "UPDATE component SET " +
                    "currentAmount = '" + this.amount + "', " +
                    "currentPrice = '" + this.price + "' " +
                    "WHERE name = '" + this.name + "';";

            SQLiteUtil.dbExecuteUpdate(query);
        }
        catch (RuntimeException ex)
        {
            throw ex;
        }
    }
}