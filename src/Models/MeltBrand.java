package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBUtil;

import java.sql.ResultSet;

public class MeltBrand {
    private String name;

    public MeltBrand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
