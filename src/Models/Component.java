package Models;

import util.DBUtil;
import java.sql.ResultSet;
import java.util.List;

public class Component {
    private String name;
    private String brand;
    private double adoptBase;
    private double amount;
    private double price;
    private int mandatory;
    private double adoptComp;
    private List<Element> elements;

    public Component(String name, String brand, double adoptBase, double amount, double price, int mandatory, double adoptComp, List<Element> elements) {
        this.name = name;
        this.brand = brand;
        this.adoptBase = adoptBase;
        this.amount = amount;
        this.price = price;
        this.mandatory = mandatory;
        this.adoptComp = adoptComp;
        this.elements = elements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getAdoptBase() {
        return adoptBase;
    }

    public void setAdoptBase(double adoptBase) {
        this.adoptBase = adoptBase;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMandatory() {
        return mandatory;
    }

    public void setMandatory(int mandatory) {
        this.mandatory = mandatory;
    }

    public double getAdoptComp() {
        return adoptComp;
    }

    public void setAdoptComp(double adoptComp) {
        this.adoptComp = adoptComp;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public boolean componentExists(String name){
        try {
            ResultSet rs = DBUtil.dbExecuteQuery("SELECT * FROM mydb.component WHERE `name` = '"+name+"'");

            if(rs.next()) {
                return true;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return  false;
    }

    public void saveComponentParam(){
        ResultSet rs = null;
        int id = 0;
        try {

            DBUtil.dbExecuteUpdate("INSERT INTO mydb.component (`name`, brand, adoptBase, currentAmount, currentPrice, mandatory) " +
                    "VALUES ('" + name + "', '" + brand + "', '" + adoptBase + "', '" + amount + "', '" + price + "', '" + mandatory + "')");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setComponentElement(String name, double percent, double adopt){
        elements.add(new Element(name, 0, 0, percent, adopt));
    }

    public void saveComponentElements(){
        ResultSet rs;
        int id = 0;
        try {
            rs = DBUtil.dbExecuteQuery("SELECT idComponent FROM mydb.component WHERE `name` = '" + name + "'");
            if (rs.next())
                id = rs.getInt("idComponent");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (Element element : elements) {
            element.saveElementInComponent(id);
        }

    }

}