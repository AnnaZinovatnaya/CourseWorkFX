package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Date;

public class Manager
{
    private static User            currentUser;
    private static User            user;
    private static Component       component;
    private static Charge          charge;
    private static Melt            melt;

    public static boolean userExists(String name, String lastname) throws RuntimeException
    {
        return User.userExists(name, lastname);
    }

    public static void saveNewUser(String name, String lastname, String password, String role) throws RuntimeException
    {
        user = new User(0, name, lastname, password, role);
        user.saveToDB();
    }

    public static boolean findUser(String name, String lastname) throws RuntimeException
    {
        user = User.readUserFromDB(name, lastname);

        return user != null;
    }

    public static String getFoundName()
    {
        return user.getName();
    }

    public static String getFoundLastname()
    {
        return user.getLastname();
    }

    public static String getFoundPassword()
    {
        return user.getPassword();
    }

    public static String getFoundRole()
    {
        return user.getRole();
    }

    public static void deleteUser() throws RuntimeException
    {
        user.deleteFromDB();

        user = null;
    }

    public static void setUserToNull()
    {
        user = null;
    }

    public static boolean login(String name, String lastname, String password) throws RuntimeException
    {
        currentUser = User.loginAndReturnUser(name, lastname, password);

        return currentUser != null;
    }

    public static String getRole()
    {
        return currentUser.getRole();
    }

    public static void newComponent()
    {
        component = new Component("", "", 0, 0, 0, 0, 0, new ArrayList<>());
    }

    public static boolean componentExists(String name) throws RuntimeException
    {
        return component.componentExists(name);
    }

    public static void setComponentParam(String name, String brand,double adopt,double amount,double price,int mandatory)
    {
        component.setName(name);
        component.setBrand(brand);
        component.setAdoptBase(adopt);
        component.setAmount(amount);
        component.setPrice(price);
        component.setMandatory(mandatory);
    }

    public static ObservableList<String> getAllElements() throws RuntimeException
    {
        return Element.getAllElements();
    }

    public static void saveComponentParam() throws RuntimeException
    {
        component.saveComponentGeneralInfo();
    }

    public static void setComponentElement(String name, double percent, double adopt)
    {
        component.setComponentElement(name, percent, adopt);
    }

    public static void saveComponentElements() throws RuntimeException
    {
        component.saveComponentElementsInfo();

        component = null;
    }

    public static void setComponentToNull()
    {
        component = null;
    }

    public static ObservableList<String> getAllBrands() throws RuntimeException
    {
        return MeltBrand.getAllBrandNamesFromDB();
    }

    public static void createNewCharge(double mass, double deltaMass, String meltBrand)
    {
        charge = new Charge(currentUser, mass, deltaMass, null, null, null, null, null);
        charge.setChargeBrand(meltBrand);
    }

    public static ObservableList<Element> getChargeElements()
    {
        return FXCollections.observableList(charge.getElements());
    }

    public static boolean canEditPercent(String element, double percent)
    {
        return charge.canEditPercent(element, percent);
    }

    public static void setChargeElements(ObservableList<Element> elements)
    {
        charge.setElements(elements);
    }

    public static ObservableList<String> getAllMandatoryComponentNames() throws RuntimeException
    {
        return Component.getAllMandatoryComponentNames();
    }

    public static ObservableList<String> getAllOptionalComponentNames() throws RuntimeException
    {
        return Component.getAllOptionalComponentNames();
    }

    public static void setMandatoryComponents(ObservableList<String> components)
    {
        charge.setMandatoryComponents(components);
        charge.setOptionalComponents();
    }

    public static ObservableList<CompInCharge> getChargeMandatoryComps()
    {
        return FXCollections.observableList(charge.getMandatoryComponents());
    }

    public static ObservableList<CompInCharge> getChargeOptionalComps()
    {
        return FXCollections.observableList(charge.getOptionalComponents());
    }

    public static boolean isChargePossible()
    {
        return charge.isPossible();
    }
    public static void calculateCheapCharge()
    {
        charge.calculateCheapCharge();
    }

    public static ObservableList<CompInCharge> getChargeResultComps()
    {
        return FXCollections.observableList(charge.getChargeResultComponents());
    }

    public static String getChargeMeltBrand()
    {
        return charge.getMeltBrand().getName();
    }

    public static String getChargeMass()
    {
        return String.valueOf(charge.getMass());
    }

    public static ObservableList<Component> getAllMandatoryComponents()  throws RuntimeException
    {
        return Component.getAllMandatoryComponents();
    }

    public static ObservableList<Component> getAllOptionalComponents()  throws RuntimeException
    {
        return FXCollections.observableArrayList(Component.getAllOptionalComponents());
    }

    public static Component readComponentFromDB(String name) throws RuntimeException
    {
        return Component.readComponentFromDB(name);
    }

    public static void saveCharge() throws RuntimeException
    {
        charge.setDateCharge(new Date());
        charge.setUser(currentUser);
        charge.saveToDB();
    }

    public static void logout()
    {
        currentUser = null;
        component = null;
        charge = null;
        melt = null;
    }

    public static ObservableList<Charge> getCharges(String meltBrand)
    {
        return Charge.getChargesOfBrand(meltBrand);
    }

    public static boolean isUserDefaultAdmin()
    {
        return user.isDefaultAdmin();
    }

    public static void newMelt()
    {
        melt = new Melt(currentUser, null, null);
    }
    public static void setMeltCharge(Charge charge)
    {
        melt.setCharge(charge);
    }

    public static void saveMelt()
    {
        melt.setDate(new Date());
        melt.saveToDB();
    }

    public static User getUser() {
        return user;
    }
}
