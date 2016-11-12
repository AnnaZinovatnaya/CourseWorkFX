package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBUtil;

import java.sql.ResultSet;

public class Element {
    private String name;
    private double minPercent;
    private double maxPercent;
    private double percent;
    private double adopt;

    public Element(String name, double minPercent, double maxPercent, double percent, double adopt) {
        this.name = name;
        this.minPercent = minPercent;
        this.maxPercent = maxPercent;
        this.percent = percent;
        this.adopt = adopt;
    }

    public Element(Element element) {
        this.name = element.getName();
        this.minPercent = element.getMinPercentDouble();
        this.maxPercent = element.getMaxPercentDouble();
        this.percent = element.getPercent();
        this.adopt = element.getAdopt();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinPercent() {
        return String.valueOf(minPercent);
    }

    public String getMaxPercent() {
        return String.valueOf(maxPercent);
    }

    public double getMinPercentDouble() {
        return minPercent;
    }

    public void setMinPercent(double minPercent) {
        this.minPercent = minPercent;
    }

    public double getMaxPercentDouble() {
        return maxPercent;
    }

    public void setMaxPercent(double maxPercent) {
        this.maxPercent = maxPercent;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public double getAdopt() {
        return adopt;
    }

    public void setAdopt(double adopt) {
        this.adopt = adopt;
    }

    public static ObservableList<String> getAllElements(){
        ObservableList<String> list = FXCollections.observableArrayList ();
        try{
            ResultSet rs;
            rs = DBUtil.dbExecuteQuery("SELECT name FROM mydb.element");
            while(rs.next()){
                list.add(rs.getString("name"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    public void saveElementInComponent(int idComponent){
        ResultSet rs;
        int idElement = 0;
        try{
            rs = DBUtil.dbExecuteQuery("SELECT idElement FROM mydb.element WHERE name = '"+name+"'");
            if(rs.next())
                idElement = rs.getInt("idElement");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            DBUtil.dbExecuteUpdate("INSERT INTO mydb.elementincomponent (procent, Element_idElement, Component_idComponent, adopt) " +
                    "VALUES ('" + percent + "', '" + idElement + "', '" + idComponent + "', '" + adopt + "')");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
