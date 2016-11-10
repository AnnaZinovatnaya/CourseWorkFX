package Models;

import util.DBUtil;

import java.sql.ResultSet;

/**
 * Created by Анюта on 04.11.2016.
 */
public class ElementForView {
    private  String name;
    private double procent;
    private double adopt;

    public ElementForView(String name, double procent, double adopt) {
        this.name = name;
        this.procent = procent;
        this.adopt = adopt;
    }
    public double getAdopt() {
        return adopt;
    }

    public void setAdopt(double adopt) {
        this.adopt = adopt;
    }

    public double getProcent() {
        return procent;
    }

    public void setProcent(double procent) {
        this.procent = procent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static int getElementIdfromDB (String name){
        int id=0;
        ResultSet rs=null;
        try {
            rs = DBUtil.dbExecuteQuery("SELECT * FROM mydb.element WHERE `name` = '" + name + "'");

            if(rs.next()) {
                id = rs.getInt("idElement");
            }


        }
        catch (Exception ex){
            ex.printStackTrace();
            return 0;
        }
        return id;
    }
}
