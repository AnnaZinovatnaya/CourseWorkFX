package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBUtil;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Component
{
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

    public boolean componentExists(String name)
    {
        try
        {
            ResultSet rs = DBUtil.dbExecuteQuery("SELECT * FROM mydb.component WHERE `name` = '"+name+"'");

            if(rs.next())
            {
                return true;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return  false;
    }

    public void saveComponentParam()
    {
        this.calculateAdopt();

        try
        {

            DBUtil.dbExecuteUpdate("INSERT INTO mydb.component (`name`, brand, adoptBase, currentAmount, currentPrice, mandatory, adoptComp) " +
                                   "VALUES ('" + name + "', '" + brand + "', '" + adoptBase + "', '" + amount + "', '" + price + "', '" + mandatory +"', '" +adoptComp+ "')");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void setComponentElement(String name, double percent, double adopt)
    {
        elements.add(new Element(name, 0, 0, percent, adopt));
    }

    public void saveComponentElements()
    {
        ResultSet rs;
        int id = 0;
        try
        {
            rs = DBUtil.dbExecuteQuery("SELECT idComponent FROM mydb.component WHERE `name` = '" + name + "'");
            if (rs.next())
                id = rs.getInt("idComponent");

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        for (Element element : elements)
        {
            element.saveElementInComponent(id);
        }

    }

    public static ObservableList<String> getAllMandatoryComponentsString()
    {
        ObservableList<String> list = FXCollections.observableArrayList ();

        String temp;
        ResultSet rs;


        try
        {
            rs = DBUtil.dbExecuteQuery("SELECT name FROM mydb.component WHERE mandatory=1");
            while (rs.next())
            {
                temp = rs.getString("name");
                list.add(temp);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return list;
    }

    public static ObservableList<String> getAllOptionalComponentsString()
    {
        ObservableList<String> list = FXCollections.observableArrayList ();

        String temp;
        ResultSet rs;


        try
        {
            rs = DBUtil.dbExecuteQuery("SELECT name FROM mydb.component WHERE mandatory=0");
            while (rs.next())
            {
                temp = rs.getString("name");
                list.add(temp);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return list;
    }

    public static ObservableList<Component> getAllMandatoryComponents()
    {
        ObservableList<Component> list = FXCollections.observableArrayList ();

        ResultSet rs;
        ResultSet rs2;
        int i=0;

        try
        {
            rs = DBUtil.dbExecuteQuery("SELECT * FROM mydb.component WHERE mandatory=1");
            while (rs.next())
            {
                list.add(new Component(rs.getString("name"), rs.getString("brand"), rs.getDouble("adoptBase"), rs.getDouble("currentAmount"), rs.getDouble("currentPrice"), 1, rs.getDouble("adoptComp"), new ArrayList<>()));
                rs2 = DBUtil.dbExecuteQuery("SELECT name, procent, adopt FROM mydb.elementincomponent JOIN mydb.element ON idElement = Element_idElement WHERE Component_idComponent = "+rs.getString("idComponent"));
                while (rs2.next())
                {
                    list.get(i).elements.add(new Element(rs2.getString("name"), 0, 0, rs2.getDouble("procent"), rs2.getDouble("adopt")));
                }
                i++;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return list;
    }

    public static ArrayList<Component> getAllOptionalComponents()
    {
        ArrayList<Component> list = new ArrayList<>();

        ResultSet rs;
        ResultSet rs2;
        int i=0;

        try
        {
            rs = DBUtil.dbExecuteQuery("SELECT * FROM mydb.component WHERE mandatory=0");
            while (rs.next())
            {
                list.add(new Component(rs.getString("name"), rs.getString("brand"), rs.getDouble("adoptBase"), rs.getDouble("currentAmount"), rs.getDouble("currentPrice"), 0, rs.getDouble("adoptComp"), new ArrayList<>()));
                rs2 = DBUtil.dbExecuteQuery("SELECT name, procent, adopt FROM mydb.elementincomponent JOIN mydb.element ON idElement = Element_idElement WHERE Component_idComponent = "+rs.getString("idComponent"));
                while (rs2.next())
                {
                    list.get(i).elements.add(new Element(rs2.getString("name"), 0, 0, rs2.getDouble("procent"), rs2.getDouble("adopt")));
                }
                i++;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return list;
    }

    private void calculateAdopt()
    {
        double temp = 100;
        for(Element aElement: elements)
        {
            temp-=aElement.getPercent();
        }
        temp /= adoptBase;

        for(Element aElement: elements)
        {
            temp+=aElement.getPercent()/aElement.getAdopt();
        }

        adoptComp = 1/temp;
    }

    public static void deleteComponent(String name)
    {

        try
        {
            ResultSet rs = DBUtil.dbExecuteQuery("SELECT * FROM mydb.component WHERE `name` = '" + name +"';");
            rs.next();
            DBUtil.dbExecuteUpdate("DELETE FROM `mydb`.`elementincomponent` WHERE `Component_idComponent`='"+rs.getInt("idComponent")+"';");
            DBUtil.dbExecuteUpdate("DELETE FROM mydb.component WHERE `name` = '" + name + "';");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static Component findComponent(String name)
    {
        Component temp = null;
        int idComponent;
        try
        {
            ResultSet rs = DBUtil.dbExecuteQuery("SELECT * FROM mydb.component WHERE `name` = '"+name+"'");
            rs.next();
            temp = new Component(name, rs.getString("brand"), rs.getDouble("adoptBase"), rs.getDouble("currentAmount"), rs.getDouble("currentPrice"), rs.getInt("mandatory"), rs.getDouble("adoptComp"), new ArrayList<>());
            idComponent = rs.getInt("idComponent");
            ResultSet rs2 = DBUtil.dbExecuteQuery("SELECT `name`, procent, adopt FROM mydb.element E JOIN mydb.elementincomponent EC ON E.idElement=EC.Element_idElement WHERE EC.Component_idComponent='"+idComponent+"';");

            while(rs2.next())
            {
                temp.getElements().add(new Element(rs2.getString("name"), 0, 0, rs2.getDouble("procent"), rs2.getDouble("adopt")));
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return temp;
    }

    public static void updateComponentData(Component component)
    {
        ResultSet rs;
        int idComponent;
        int idElement;
        try
        {
            DBUtil.dbExecuteUpdate("UPDATE mydb.component SET currentAmount='"+component.getAmount()+"',currentPrice='"+component.getPrice()+"', adoptBase='"+component.getAdoptBase()+"', brand='"+component.getBrand()+"' WHERE `name`='"+component.getName()+"';");
            rs = DBUtil.dbExecuteQuery("SELECT * FROM mydb.component WHERE `name`='"+component.getName()+"'");
            rs.next();
            idComponent = rs.getInt("idComponent");
            for(Element aElement: component.getElements())
            {
                if(aElement.getName().equals("C"))
                {

                    rs = DBUtil.dbExecuteQuery("SELECT * FROM mydb.element WHERE `name`='C'");
                    rs.next();
                    idElement = rs.getInt("idElement");
                    DBUtil.dbExecuteUpdate("UPDATE mydb.elementincomponent SET procent='"+aElement.getPercent()+"',adopt='"+aElement.getAdopt()+"' WHERE Element_idElement='"+idElement+"' AND Component_idComponent='"+idComponent+"';");
                }
                if(aElement.getName().equals("Si"))
                {

                    rs = DBUtil.dbExecuteQuery("SELECT * FROM mydb.element WHERE `name`='Si'");
                    rs.next();
                    idElement = rs.getInt("idElement");
                    DBUtil.dbExecuteUpdate("UPDATE mydb.elementincomponent SET procent='"+aElement.getPercent()+"',adopt='"+aElement.getAdopt()+"' WHERE Element_idElement='"+idElement+"' AND Component_idComponent='"+idComponent+"';");
                }
                if(aElement.getName().equals("S"))
                {

                    rs = DBUtil.dbExecuteQuery("SELECT * FROM mydb.element WHERE `name`='S'");
                    rs.next();
                    idElement = rs.getInt("idElement");
                    DBUtil.dbExecuteUpdate("UPDATE mydb.elementincomponent SET procent='"+aElement.getPercent()+"',adopt='"+aElement.getAdopt()+"' WHERE Element_idElement='"+idElement+"' AND Component_idComponent='"+idComponent+"';");
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}