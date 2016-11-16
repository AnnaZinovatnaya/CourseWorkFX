package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class Register {
    public static User currentUser;
    public static User user;
    public static Component component;
    public static List<Component> components;
    public static Charge charge;
    public static List<Charge> charges;
    public static List<Melt> melts;
    public static List<MeltBrand> meltBrands;


    public static void newUser(){
        user = new User("", "", "", "");
    }

    public static boolean setNameLastname(String name, String lastname){
        if(!user.isUser(name, lastname)){
            user.setName(name);
            user.setLastname(lastname);
            return true;
        } else
            return false;
    }

    public static void setPasswordRole(String password, String role){
        user.setPassword(password);
        user.setRole(role);
    }

    public static void saveUser(){
        user.saveUser();
    }

    public static boolean findUser(String name, String lastname){
        user = User.findUser(name, lastname);
        return user != null;
    }

    public static String getFoundName(){
        return user.getName();
    }

    public static String getFoundLastname(){
        return user.getLastname();
    }

    public static String getFoundPassword(){
        return user.getPassword();
    }

    public static String getFoundRole(){
        return user.getRole();
    }

    public static boolean canDelete(){
        return user != null;
    }

    public static void deleteUser(){
        user.deleteUser();
        user = null;
    }

    public static void setUserToNull(){
        user = null;
    }

    public static boolean login(String name, String lastname, String password){
        currentUser = User.login(name, lastname, password);
        return currentUser != null;
    }

    public static String getRole(){
        return currentUser.getRole();
    }

    public static void newComponent(){
        component = new Component("", "", 0, 0, 0, 0, 0, new ArrayList<>());
    }

    public static boolean componentExists(String name){
        return component.componentExists(name);
    }

    public static void setComponentParam(String name, String brand,double adopt,double amount,double price,int mandatory){
        component.setName(name);
        component.setBrand(brand);
        component.setAdoptBase(adopt);
        component.setAmount(amount);
        component.setPrice(price);
        component.setMandatory(mandatory);

    }

    public static ObservableList<String> getAllElements(){
        return Element.getAllElements();
    }

    public static void saveComponentParam(){
        component.saveComponentParam();
    }

    public static void setComponentElement(String name, double percent, double adopt){
        component.setComponentElement(name, percent, adopt);
    }

    public static void saveComponentElements(){
        component.saveComponentElements();
        component = null;
    }

    public static void setComponentToNull(){
        component = null;
    }

    public static ObservableList<String> getAllBrands(){
        return MeltBrand.getAllBrands();
    }

    public static void newCharge(){
        charge = new Charge(currentUser, 0, 0, null, null, null, null, null);
    }

    public static void setChargeBrand(String meltBrand){
        charge.setChargeBrand(meltBrand);
    }

    public static void setChatgeMassAndDelta(double mass, double deltaMass){
        charge.setMass(mass);
        charge.setDeltaMass(deltaMass);
    }

    public static ObservableList<Element> getChargeElements(){
        return FXCollections.observableList(charge.getElements());
    }

    public static boolean canEditPercent(String element, double percent){
        return charge.canEditPercent(element, percent);
    }

    public static void setChargeElements(ObservableList<Element> elements){
        charge.setElements(elements);
    }

    public static ObservableList<String> getAllMandatoryComponentsString(){
        return Component.getAllMandatoryComponentsString();
    }

    public static void setMandatoryComponents(ObservableList<String> components){
        charge.setMandatoryComponents(components);
        charge.setOptionalComponents();
    }

    public static ObservableList<CompInCharge> getChargeMandatoryComps(){
        return FXCollections.observableList(charge.getMandatoryComponents());
    }

    public static ObservableList<CompInCharge> getChargeOptionalComps(){
        return FXCollections.observableList(charge.getOptionalComponents());
    }

    public static boolean isChargePossible(){
        return charge.isPossible();
    }

}
