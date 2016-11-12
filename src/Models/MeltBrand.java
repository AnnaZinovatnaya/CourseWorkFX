package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBUtil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MeltBrand {
    private String name;
    private List<Element> elements;

    public MeltBrand(String name, List<Element> elements) {
        this.name = name;
        this.elements = elements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public static ObservableList<String> getAllBrands(){
        ObservableList<String> list = FXCollections.observableArrayList ();

        String temp;
        ResultSet rs;


        try{
            rs = DBUtil.dbExecuteQuery("SELECT name FROM mydb.meltbrand");
            while (rs.next()) {
                temp = rs.getString("name");
                list.add(temp);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

    public static MeltBrand getMeltBrand(String name){
        MeltBrand temp = new MeltBrand("", new ArrayList<>());
        ResultSet rs;
        ResultSet rs2;
        int idMeltBrand=0;

        try{
            rs = DBUtil.dbExecuteQuery("SELECT idMeltBrand, name FROM mydb.meltbrand  WHERE name = '"+name+"'");
            if (rs.next()) {
                temp.setName(rs.getString("name"));
                idMeltBrand = rs.getInt("idMeltBrand");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }


        try{
            rs2 = DBUtil.dbExecuteQuery("SELECT name, minProcent, maxProcent FROM mydb.element E JOIN mydb.elementinbrand EB ON E.idElement = EB.Element_idElement WHERE EB.MeltBrand_idMeltBrand = "+idMeltBrand);
            while (rs2.next()) {
                temp.elements.add(new Element(rs2.getString("name"), rs2.getDouble("minProcent"), rs2.getDouble("maxProcent"), 0, 0));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return temp;
    }
}
