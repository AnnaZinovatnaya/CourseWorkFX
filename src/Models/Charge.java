package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Util.ErrorMessage;
import Util.SQLiteUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Charge
{
    private int                    id;
    private User                   user;
    private double                 mass;
    private double                 deltaMass;
    private Date                   dateCharge;
    private MeltBrand              meltBrand;
    private List<CompInCharge>     mandatoryComponents;
    private List<CompInCharge>     optionalComponents;
    private List<Element>          elements;
    private final ChargeCalculator calculator;

    public Charge(int id, User user, double mass, double deltaMass, Date dateCharge, MeltBrand meltBrand,
                  List<CompInCharge> mandatoryComponents, List<CompInCharge> optionalComponents,
                  List<Element> elements)
    {
        this.id = id;
        this.user = user;
        this.mass = mass;
        this.deltaMass = deltaMass;
        this.dateCharge = dateCharge;
        this.meltBrand = meltBrand;
        this.mandatoryComponents = mandatoryComponents;
        this.optionalComponents = optionalComponents;
        this.elements = elements;

        this.optionalComponents = new ArrayList<>();
        for (Component aComponent: Component.getAllOptionalComponents())
        {
            this.optionalComponents.add(new CompInCharge(aComponent, 0, 0, 0));
        }

        this.calculator = new ChargeCalculator(this);
    }

    public void setMandatoryComponents(List<CompInCharge> mandatoryComponents) {
        this.mandatoryComponents = mandatoryComponents;
    }

    public void setOptionalComponents(List<CompInCharge> optionalComponents) {
        this.optionalComponents = optionalComponents;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public double getMass()
    {
        return mass;
    }

    public void setMass(double mass)
    {
        this.mass = mass;
    }

    public double getDeltaMass()
    {
        return deltaMass;
    }

    public void setDeltaMass(double deltaMass)
    {
        this.deltaMass = deltaMass;
    }

    public Date getDateCharge()
    {
        return dateCharge;
    }

    public void setDateCharge(Date dateCharge)
    {
        this.dateCharge = dateCharge;
    }

    public MeltBrand getMeltBrand()
    {
        return meltBrand;
    }

    public void setMeltBrand(MeltBrand meltBrand)
    {
        this.meltBrand = meltBrand;
    }

    public List<CompInCharge> getMandatoryComponents()
    {
        return mandatoryComponents;
    }

    public void setMandatoryComponentsString(List<String> mandatoryComponents)
    {
        this.mandatoryComponents = new ArrayList<>();

        for (Component component: Component.getAllMandatoryComponents())
        {
            if (mandatoryComponents.contains(component.getName()))
            {
                this.mandatoryComponents.add(new CompInCharge(component, 0, 0, 0));
            }
        }
    }

    public List<CompInCharge> getOptionalComponents()
    {
        return optionalComponents;
    }

    public void setOptionalComponents()
    {
        this.optionalComponents = new ArrayList<>();
        for (Component component: Component.getAllOptionalComponents())
        {
            this.optionalComponents.add(new CompInCharge(component, 0, 0, 0));
        }
    }

    public List<Element> getElements()
    {
        return elements;
    }

    public void setElements(List<Element> elements)
    {
        this.elements = elements;
    }

    public void setChargeBrand(String brand)
    {
        meltBrand = MeltBrand.readMeltBrandFromDB(brand);
        elements = new ArrayList<>();
        elements.addAll(meltBrand.getElements());
    }

    public  boolean isNewElementPercentInAllowedRange(String elementName, double percent)
    {
        for (Element element: meltBrand.getElements())
        {
            if (element.getName().equals(elementName))
            {
                return percent >= element.getMinPercentDouble() && percent <= element.getMaxPercentDouble();
            }
        }
        return false;
    }

    public boolean isChargePossible()
    {
        return this.calculator.isChargePossible();
    }

    public void calculateCheapCharge()
    {
        this.calculator.calculateCheapCharge();
    }

    public ObservableList<CompInCharge> getChargeResultComponents()
    {
        ObservableList<CompInCharge> allComponents = FXCollections.observableArrayList();
        allComponents.addAll(mandatoryComponents);

        for (CompInCharge component: optionalComponents)
        {
            if (component.getCurrentMass() > 0)
            {
                allComponents.add(component);
            }
        }
        return allComponents;
    }

    public void saveToDB()
    {
        saveGeneralChargeToDB();

        int chargeID = getIndexOfLastSavedCharge();

        saveElementsInCharge(chargeID);
        saveMandatoryComponentsInCharge(chargeID);
        saveOptionalComponentsInCharge(chargeID);
    }

    private void saveGeneralChargeToDB()
    {
        SQLiteUtil.dbExecuteUpdate("INSERT INTO charge (mass, deltaMass, dateCharge, User_idUser, MeltBrand_idMeltBrand)\n" +
                                   "VALUES ('" + mass + "', '" +
                                                 deltaMass + "', '" +
                                                 new java.sql.Date(dateCharge.getTime()) + "', '" +
                                                 getUserIdFromDb(user.getName(), user.getLastname()) + "', '" +
                                                 getMeltIdBrandFromDb(meltBrand.getName()) + "');");
    }

    private void saveElementsInCharge(int chargeID)
    {
        for (Element element: elements)
        {
            SQLiteUtil.dbExecuteUpdate("INSERT INTO elementincharge (minProcent, maxProcent, Charge_idCharge, Element_idElement)\n" +
                                        "VALUES ('" + element.getMinPercentDouble() + "', '" +
                                                      element.getMaxPercentDouble() + "', '" +
                                                      chargeID + "', '" +
                                                      getElementIdFromDb(element.getName()) + "');");
        }
    }

    private void saveMandatoryComponentsInCharge(int chargeID)
    {
        for (CompInCharge component: mandatoryComponents)
        {
            SQLiteUtil.dbExecuteUpdate("INSERT INTO componentincharge (currentMass, minProcent, maxProcent, " +
                                       "                               Charge_idCharge, Component_idComponent)\n" +
                                        "VALUES ('" + component.getCurrentMass() + "', '" +
                                                      component.getMinPercent() + "', '" +
                                                      component.getMaxPercent() + "', '" +
                                                      chargeID + "', '" +
                                                      getComponentIdFromDb(component.getName()) + "');");
        }
    }

    private void saveOptionalComponentsInCharge(int chargeID)
    {
        for (CompInCharge component: optionalComponents)
        {
            if (0 != component.getCurrentMass())
            {
                SQLiteUtil.dbExecuteUpdate("INSERT INTO componentincharge (currentMass, minProcent, maxProcent, " +
                                           "                               Charge_idCharge, Component_idComponent)\n" +
                                           "VALUES ('" + component.getCurrentMass() + "', '" +
                                                         component.getMinPercent() + "', '" +
                                                         component.getMaxPercent() + "', '" +
                                                         chargeID + "', '" +
                                                         getComponentIdFromDb(component.getName()) + "');");
            }
        }
    }

    public static int getIndexOfLastSavedCharge()
    {
        int index = 0;
        String query = "SELECT max(idCharge) FROM charge;";
        try
        {
            ResultSet rs = SQLiteUtil.dbExecuteQuery(query);
            if (rs.next())
            {
               index = rs.getInt("max(idCharge)");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }
        return index;
    }

    public static ObservableList<Charge> getChargesOfBrand(String meltBrand)
    {
        ObservableList<Charge> charges = FXCollections.observableArrayList();

        String query = "";
        try
        {
            query = "SELECT idCharge, mass, deltaMass FROM charge " +
                    "WHERE MeltBrand_idMeltBrand = '" + getMeltIdBrandFromDb(meltBrand) + "';";
            ResultSet rs = SQLiteUtil.dbExecuteQuery(query);
            while (rs.next())
            {
                Charge charge = new Charge(rs.getInt("idCharge"), null, rs.getDouble("mass"),
                                               rs.getDouble("deltaMass"), null, new MeltBrand(meltBrand, null),
                                               new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

                charge.getOptionalComponents().clear();
                query = "SELECT name, minProcent, maxProcent " +
                        "FROM elementincharge A join element B on A.Element_idElement=B.idElement " +
                        "WHERE Charge_idCharge = '" + rs.getInt("idCharge") + "';";
                ResultSet rs2 = SQLiteUtil.dbExecuteQuery(query);

                while (rs2.next())
                {
                   charge.getElements().add(new Element(rs2.getString("name"), rs2.getDouble("minProcent"),
                                                            rs2.getDouble("maxProcent"), 0, 0));
                }

                query = "SELECT name, currentMass, mandatory FROM componentincharge CIC " +
                        "JOIN component C ON C.idComponent = CIC.Component_idComponent " +
                        "WHERE Charge_idCharge = '" + rs.getInt("idCharge") + "';";
                ResultSet rs3 = SQLiteUtil.dbExecuteQuery(query);
                while (rs3.next())
                {
                    CompInCharge compInCharge = new CompInCharge(new Component(rs3.getString("name"), null, 0, 0, 0, 0, 0,
                                                                 new ArrayList<>()), rs3.getDouble("currentMass"), 0, 0);
                    if (1 == rs3.getInt("mandatory"))
                    {
                        charge.getMandatoryComponents().add(compInCharge);
                    }
                    else
                    {
                        charge.getOptionalComponents().add(compInCharge);
                    }
                }

                charges.add(charge);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }

        return charges;
    }

    private static int getMeltIdBrandFromDb(String meltBrandName)
    {
        int meltBrandID = 0;
        String query = "";
        try
        {
            query = "SELECT idMeltBrand FROM meltbrand WHERE name = '" + meltBrandName + "';";
            ResultSet rs = SQLiteUtil.dbExecuteQuery(query);
            if (rs.next())
            {
               meltBrandID = rs.getInt("idMeltBrand");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }
        return meltBrandID;
    }

    private static int getUserIdFromDb(String firstname, String lastname)
    {
        int userID = 0;
        String query = "";
        try
        {
            query = "SELECT idUser FROM user WHERE name = '" + firstname + "' AND lastname = '" + lastname + "';";
            ResultSet rs = SQLiteUtil.dbExecuteQuery(query);
            if (rs.next())
            {
                userID = rs.getInt("idUser");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }
        return userID;
    }

    private static int getElementIdFromDb(String elementName)
    {
        int elementID = 0;
        String query = "SELECT idElement FROM element WHERE name = '" + elementName + "'";
        try
        {
            ResultSet rs = SQLiteUtil.dbExecuteQuery(query);
            if (rs.next())
            {
                elementID = rs.getInt("idElement");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }
        return elementID;
    }

    private static int getComponentIdFromDb(String componentName)
    {
        int componentID = 0;
        String query = "SELECT idComponent FROM component WHERE name = '" + componentName + "';";
        try
        {
            ResultSet rs = SQLiteUtil.dbExecuteQuery(query);
            if (rs.next())
            {
                componentID = rs.getInt("idComponent");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }
        return componentID;
    }
}