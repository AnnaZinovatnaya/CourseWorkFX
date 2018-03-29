package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Manager
{
    private static User            currentUser;
    private static User            user;
    private static Component       component;
    private static List<Component> components;
    private static Charge          charge;
    private static List<Charge>    charges;
    private static Melt            melt;
    private static List<MeltBrand> meltBrands;

    public static boolean userExists(String name, String lastname) throws RuntimeException
    {
        try
        {
            return User.userExists(name, lastname);
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    public static void saveNewUser(String name, String lastname, String password, String role) throws RuntimeException
    {
        try
        {
            user = new User(name, lastname, password, role);
            user.saveUser();
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    public static boolean findUser(String name, String lastname) throws RuntimeException
    {
        try
        {
            user = User.findUser(name, lastname);
        }
        catch (RuntimeException e)
        {
            throw e;
        }

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
        try
        {
            user.deleteUser();
        }
        catch (RuntimeException e)
        {
            throw e;
        }

        user = null;
    }

    public static void setUserToNull()
    {
        user = null;
    }

    public static boolean login(String name, String lastname, String password) throws RuntimeException
    {
        try
        {
            currentUser = User.login(name, lastname, password);
        }
        catch (RuntimeException e)
        {
            throw e;
        }

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
        try
        {
            return component.componentExists(name);
        }
        catch (RuntimeException e)
        {
            throw e;
        }
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
        try
        {
            return Element.getAllElements();
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    public static void saveComponentParam() throws RuntimeException
    {
        try
        {
            component.saveComponentParam();
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    public static void setComponentElement(String name, double percent, double adopt)
    {
        component.setComponentElement(name, percent, adopt);
    }

    public static void saveComponentElements() throws RuntimeException
    {
        try
        {
            component.saveComponentElements();
        }
        catch (RuntimeException e)
        {
            throw e;
        }

        component = null;
    }

    public static void setComponentToNull()
    {
        component = null;
    }

    public static ObservableList<String> getAllBrands() throws RuntimeException
    {
        try
        {
            return MeltBrand.getAllBrands();
        }
        catch (RuntimeException e)
        {
            throw e;
        }
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

    public static ObservableList<String> getAllMandatoryComponentsString() throws RuntimeException
    {
        try
        {
            return Component.getAllMandatoryComponentsString();
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    public static ObservableList<String> getAllOptionalComponentsString() throws RuntimeException
    {
        try
        {
            return Component.getAllOptionalComponentsString();
        }
        catch (RuntimeException e)
        {
            throw e;
        }
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
        return FXCollections.observableList(charge.getChargeResultComps());
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
        try
        {
            return Component.getAllMandatoryComponents();
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    public static ObservableList<Component> getAllOptionalComponents()  throws RuntimeException
    {
        try
        {
            return FXCollections.observableArrayList(Component.getAllOptionalComponents());
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    public static void updateComponentData(Component component) throws RuntimeException
    {
        try
        {
            Component.updateComponentData(component);
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    public static void deleteComponent(String name) throws RuntimeException
    {
        try
        {
            Component.deleteComponent(name);
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    public static Component findComponent(String name) throws RuntimeException
    {
        try
        {
            return Component.findComponent(name);
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    public static void saveCharge() throws RuntimeException
    {
        charge.setDateCharge(new Date());
        charge.setUser(currentUser);
        try
        {
            charge.saveToDB();
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    public static int getMaxChargeIndex() throws RuntimeException
    {
        int res = 0;

        try
        {
            res = charge.getMaxIndexFromDB();
        }
        catch (RuntimeException e)
        {
            throw e;
        }

        return res;
    }

    public static int getMaxMeltIndex() throws RuntimeException
    {
        int res = 0;

        try
        {
            res = Melt.getMaxIndexFromDB();
        }
        catch (RuntimeException e)
        {
            throw e;
        }

        return res;
    }

    public static void logout()
    {
        currentUser = null;
        component = null;
        components = null;
        charge = null;
        charges = null;
        melt = null;
        meltBrands = null;
    }

    public static ObservableList<Charge> getCharges(String meltBrand)
    {
        return Charge.getCharges(meltBrand);
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
