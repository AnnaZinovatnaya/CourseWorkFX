package Models;

import util.DBUtil;
import java.sql.ResultSet;

public class Component {
    private int idComp;
    private String name;
    private String brand;
    private double adoptBase;
    private double amount;
    private double price;
    private int mandatory;

    public Component(int idComp, String name, String brand, double adoptBase, double amount, double price, int mandatory) {
        this.idComp = idComp;
        this.name = name;
        this.brand = brand;
        this.adoptBase = adoptBase;
        this.amount = amount;
        this.price = price;
        this.mandatory = mandatory;
    }

    public int getIdComp() {
        return idComp;
    }

    public void setIdComp(int idComp) {
        this.idComp = idComp;
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

    public static boolean componentExists(String name){
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

    public boolean addComponent(){
        ResultSet rs = null;
        try {

            DBUtil.dbExecuteUpdate("INSERT INTO mydb.component (`name`, brand, adoptBase, currentAmount, currentPrice, mandatory) " +
                    "VALUES ('" + name + "', '" + brand + "', '" + adoptBase + "', '" + amount + "', '" + price + "', '" + mandatory + "')");
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean getIDfromDB(){
        try {
            ResultSet rs=null;
            rs = DBUtil.dbExecuteQuery("SELECT * FROM mydb.component WHERE `name` = '" + name + "'");
            if (rs.next()) {
                idComp = rs.getInt("idComponent");
            } else
                return false;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }


}