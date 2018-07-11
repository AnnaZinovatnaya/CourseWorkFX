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
    private int                id;
    private User               user;
    private double             mass;
    private double             deltaMass;
    private Date               dateCharge;
    private MeltBrand          meltBrand;
    private List<CompInCharge> mandatoryComponents;
    private List<CompInCharge> optionalComponents;
    private List<Element>      elements;

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

    public void setMandatoryComponents(List<String> mandatoryComponents)
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
        double temp;

        double curPercSum = 0;

        double massChTemp;
        double massChRes;

        double[] currentElementMasses;
        double[] minAllowedElementMasses = initializeMinAllowedElementMasses();
        double[] maxAllowedElementMasses = initializeMaxAllowedElementMasses();

        mandatoryComponents = sortMandatoryComponents();

        for (CompInCharge mandatoryComponent : mandatoryComponents)
        {
            mandatoryComponent.setCurrentPercent(mandatoryComponent.getMinPercent());
            curPercSum += mandatoryComponent.getCurrentPercent() / 100;
        }

        massChRes = calculateCurrentMassChargeOfComponents(mandatoryComponents);
        massChTemp = massChRes * curPercSum;

        for (int i = 0; i < mandatoryComponents.size(); ++i)
        {
            mandatoryComponents.get(i).setCurrentMass(massChTemp * mandatoryComponents.get(i).getCurrentPercent() / 100);
        }

        currentElementMasses = calculateElementMassesInCurrentMelt();
        //ошибка: взяли всех обяхательных компонентов по минимуму и получилось элементов юольше, чем максимальная граница:

        for (int i = 0; i < elements.size(); ++i)
        {
            if (currentElementMasses[i] > (elements.get(i).getMaxPercentDouble() * mass / 100))
            {
                return false;
            }
        }

        int p = 0;
        while (true)
        {
            curPercSum = 0;
            mandatoryComponents.get(p).setCurrentPercent(mandatoryComponents.get(p).getMaxPercent());
            for (CompInCharge mandatoryComponent : mandatoryComponents)
            {
                curPercSum += mandatoryComponent.getCurrentPercent() / 100;
            }

            if (curPercSum > 1)
            {
                temp = curPercSum - 1;
                mandatoryComponents.get(p).setCurrentPercent(mandatoryComponents.get(p).getCurrentPercent() - temp * 100);
                curPercSum = 1;
            }

            massChRes = calculateCurrentMassChargeOfComponents(mandatoryComponents);
            massChTemp = massChRes * curPercSum;

            for (int i = 0; i < mandatoryComponents.size(); ++i)
            {
                mandatoryComponents.get(i).setCurrentMass(massChTemp * mandatoryComponents.get(i).getCurrentPercent() / 100);
            }

            for (int i = 0; i < elements.size(); ++i)
            {
                currentElementMasses = calculateElementMassesInCurrentMelt();

                if (currentElementMasses[i] > maxAllowedElementMasses[i])
                {
                    temp = currentElementMasses[i] - minAllowedElementMasses[i];//масса, которую надо вычесть
                    temp /= massChTemp;//процент, который нужно вычесть
                    mandatoryComponents.get(p).setCurrentPercent(mandatoryComponents.get(p).getCurrentPercent() - temp * 100);

                    massChRes = calculateCurrentMassChargeOfComponents(mandatoryComponents);
                    massChTemp = massChRes * curPercSum;

                    for (i = 0; i < mandatoryComponents.size(); ++i)
                    {
                        mandatoryComponents.get(i).setCurrentMass(massChTemp * mandatoryComponents.get(i).getCurrentPercent() / 100);
                    }
                }
            }
            curPercSum = 0;
            for (CompInCharge mandatoryComponent : mandatoryComponents)
            {
                curPercSum += mandatoryComponent.getCurrentPercent() / 100;
            }
            if (1 == curPercSum)
            {
                break;
            }
            else
            {
                ++p;
                if (p >= mandatoryComponents.size())
                {
                    return false;
                }
            }
        }

        for (int i = 0; i < mandatoryComponents.size(); ++i)
        {
            mandatoryComponents.get(i).setCurrentMass((double)Math.round(massChTemp * mandatoryComponents.get(i).getCurrentPercent()/100*10)/10);
        }

        return true;
    }

    private double[] initializeCurrentElementMasses()
    {
        double initializedCurrentElementMasses[] = new double[elements.size()];
        for (int i = 0; i < elements.size(); ++i)
        {
            initializedCurrentElementMasses[i] = 0;
        }

        return initializedCurrentElementMasses;
    }

    private double[] initializeMinAllowedElementMasses()
    {
        double minAllowedElementMasses[] = new double[elements.size()];
        for (int i = 0; i < elements.size(); ++i)
        {
            minAllowedElementMasses[i] = elements.get(i).getMinPercentDouble() * mass / 100;
        }

        return minAllowedElementMasses;
    }

    private double[] initializeMaxAllowedElementMasses()
    {
        double[] maxAllowedElementMasses = new double[elements.size()];
        for (int i = 0; i < elements.size(); ++i)
        {
            maxAllowedElementMasses[i] = elements.get(i).getMaxPercentDouble() * mass / 100;
        }

        return maxAllowedElementMasses;
    }

    // HOW IS IT SORTING???
    public ArrayList<CompInCharge> sortMandatoryComponents()
    {
        // всегда ли у всех заданных компонентов одиноковый набор элементов?
        double someCoefficients[][] = calculateCoefficientsForEachComponentElementPair();
        double maxCoefficients[] = findMaxCoefficientForEachComponent(someCoefficients);

        return sortComponentsByMaxCoefficientsInAscendingOrder(maxCoefficients);
    }

    // WHAT IS someCoefficients???
    private double[][] calculateCoefficientsForEachComponentElementPair()
    {
        double coefficients[][] = new double[mandatoryComponents.size()][mandatoryComponents.get(0).getComponent().getElements().size()];
        for (int componentIndex = 0; componentIndex < mandatoryComponents.size(); ++componentIndex)
        {
            CompInCharge currentComponent = mandatoryComponents.get(componentIndex);
            for (int elementInCompIndex = 0; elementInCompIndex < currentComponent.getComponent().getElements().size(); ++elementInCompIndex)
            {
                Element currentElementInComponent = currentComponent.getComponent().getElements().get(elementInCompIndex);
                for (Element currentElementInCharge : elements) {
                    String elementInComponentName = currentElementInComponent.getName();
                    String elementInChargeName = currentElementInCharge.getName();

                    if (elementInComponentName.equals(elementInChargeName)) {
                        double percentOfElementInComponentInShares = currentElementInComponent.getPercent() / 100;
                        double adoptOfElementInComponentInShares = currentElementInComponent.getAdopt() / 100;
                        double maxPercentOfElementInChargeInShares = currentElementInCharge.getMaxPercentDouble() / 100;

                        coefficients[componentIndex][elementInCompIndex] = percentOfElementInComponentInShares * adoptOfElementInComponentInShares / maxPercentOfElementInChargeInShares;
                    }
                }
            }
        }
        return coefficients;
    }

    private double[] findMaxCoefficientForEachComponent(double[][] coefficients)
    {
        double maxCoefficients[] = new double[mandatoryComponents.size()];

        for (int componentIndex = 0; componentIndex < mandatoryComponents.size(); ++componentIndex)
        {
            double maxCoefficient = coefficients[componentIndex][0];
            for (int elementIndex = 0; elementIndex < mandatoryComponents.get(0).getComponent().getElements().size(); ++elementIndex)
            {
                if (coefficients[componentIndex][elementIndex] > maxCoefficient)
                {
                    maxCoefficient = coefficients[componentIndex][elementIndex];
                }
            }
            maxCoefficients[componentIndex] = maxCoefficient;
        }
        return maxCoefficients;
    }

    private ArrayList<CompInCharge> sortComponentsByMaxCoefficientsInAscendingOrder(double[] maxCoefficients)
    {
        ArrayList<CompInCharge> sortedList = new ArrayList<>();

        while (sortedList.size() < mandatoryComponents.size())
        {
            int min = 0;
            for (int i = 0; i < mandatoryComponents.size(); ++i)
            {
                if (maxCoefficients[i] < maxCoefficients[min])
                {
                    min = i;
                }
            }
            sortedList.add(mandatoryComponents.get(min));
            maxCoefficients[min] = 100;
        }
        return sortedList;
    }

    public double[] calculateElementMassesInCurrentMelt()
    {
        double[] elementInMeltMasses = new double[elements.size()];
        for (int i = 0; i < elements.size(); ++i)
        {
            elementInMeltMasses[i] = 0;
        }

        for (CompInCharge mandatoryComponent : mandatoryComponents)
        {
            for (int j = 0; j < elements.size(); j++)
            {
                elementInMeltMasses[j] += mandatoryComponent.getCurrentMass() * mandatoryComponent.getComponent().getElements().get(j).getPercent() / 100 * mandatoryComponent.getComponent().getElements().get(j).getAdopt() / 100;
            }
        }

        for (CompInCharge optionalComponent: optionalComponents)
        {
            if (optionalComponent.getCurrentMass() > 0)
            {
                for (int j = 0; j < elements.size(); j++)
                {
                    elementInMeltMasses[j] += optionalComponent.getCurrentMass() * optionalComponent.getComponent().getElements().get(j).getPercent() / 100 * optionalComponent.getComponent().getElements().get(j).getAdopt() / 100;
                }
            }
        }
        return elementInMeltMasses;
    }

    public double[] calculateElementMassesInCurrentMelt(ArrayList<CompInCharge> list)
    {
        double[] massET = new double[elements.size()];
        for (int i = 0; i < elements.size(); ++i)
        {
            massET[i] = 0;
        }

        for (CompInCharge aList : list)
        {
            for (int j = 0; j < elements.size(); j++)
            {
                massET[j] += aList.getCurrentMass() * aList.getComponent().getElements().get(j).getPercent() / 100 * aList.getComponent().getElements().get(j).getAdopt() / 100;
            }
        }

        for (CompInCharge optionalComponent: optionalComponents)
        {
            if (optionalComponent.getCurrentMass()>0)
            {
                for (int j = 0; j < elements.size(); j++)
                {
                    massET[j] += optionalComponent.getCurrentMass() * optionalComponent.getComponent().getElements().get(j).getPercent() / 100 * optionalComponent.getComponent().getElements().get(j).getAdopt() / 100;
                }
            }
        }

        return massET;
    }

    double calculateCurrentMassChargeOfComponents(List<CompInCharge> components)
    {
        double sumOfPercents = 0;
        double sumOfAdoptedPercents = 0;
        for (CompInCharge component : components)
        {
            sumOfPercents += component.getCurrentPercent() / 100;
            sumOfAdoptedPercents += (component.getComponent().getAdoptComp() * component.getCurrentPercent() / 100);
        }

        if (sumOfPercents > 1)
        {
            return -1;
        }

        return mass * sumOfPercents / sumOfAdoptedPercents;
    }

    public ArrayList<CompInCharge> sortMandatoryComponentsByPrice()
    {
        ArrayList<CompInCharge> sortedList = new ArrayList<>();
        double prices[] = new double[mandatoryComponents.size()];

        for (int i = 0; i < mandatoryComponents.size(); ++i)
        {
            prices[i] = mandatoryComponents.get(i).getComponent().getPriceDouble();
        }

        while (sortedList.size() < mandatoryComponents.size())
        {
            int indexMinPrice = 0;
            for (int i = 0; i < mandatoryComponents.size(); ++i)
            {
                if (prices[i] < prices[indexMinPrice])
                {
                    indexMinPrice = i;
                }
            }
            sortedList.add(mandatoryComponents.get(indexMinPrice));
            prices[indexMinPrice] = 10000000;
        }
        return sortedList;
    }

    public ArrayList<CompInCharge> change(ArrayList<CompInCharge> mandatoryCompsTemp)
    {
        ArrayList<CompInCharge> newList = new ArrayList<>();
        CompInCharge temp = new CompInCharge(null, 0, 0, 0);
        int i;
        for (i = 0; i < mandatoryCompsTemp.size(); ++i)
        {
            newList.add(mandatoryCompsTemp.get(i));
        }
        for (i = 0; i < mandatoryComponents.size(); ++i)
        {
            if (mandatoryComponents.get(i).getName().compareTo(newList.get(i).getName()) != 0)
            {
                for (int j = i; j < newList.size(); ++j)
                {
                    if (newList.get(j).getName().compareTo(mandatoryComponents.get(i).getName()) == 0)
                    {
                        while (j != i)
                        {
                            temp.setCompInCharge(newList.get(j));
                            newList.get(j).setCompInCharge(newList.get(j - 1));
                            newList.get(j - 1).setCompInCharge(temp);
                            --j;
                        }
                        return newList;
                    }
                }
            }
        }
        return newList;
    }

    private ArrayList<CompInCharge> sortOptionalComponentsByElement(Element element)
    {
        ArrayList<CompInCharge> sortedList = new ArrayList<>();
        while (sortedList.size() < optionalComponents.size())
        {
            int index = findIndexOfComponentWithMaxElementPercent(element);
            sortedList.add(optionalComponents.get(index));
            optionalComponents.remove(index);
        }
        return sortedList;
    }

    private int findIndexOfComponentWithMaxElementPercent(Element element)
    {
        int maxIndex = 0;

        for (int i = 0; i < optionalComponents.size(); ++i)
        {
            for (int j = 0; j < optionalComponents.get(i).getComponent().getElements().size(); ++j)
            {
                Element elementInCurrentComponent = optionalComponents.get(i).getComponent().getElements().get(j);
                Element elementInCurrentMaxComponent = optionalComponents.get(maxIndex).getComponent().getElements().get(j);

                if (elementInCurrentComponent.getName().equals(element.getName()) &&
                   (elementInCurrentComponent.getPercent() > elementInCurrentMaxComponent.getPercent()))
                {
                   maxIndex = i;
                }
            }
        }

        return maxIndex;
    }

    public void calculateCheapCharge()
    {
        boolean isChargeSuccessful = false;
        double temp;

        double curPercSum = 0;

        double massChTemp = 0;
        double massChRes;

        double[] currentElementMasses;
        double[] minAllowedElementMasses = initializeMinAllowedElementMasses();
        double[] maxAllowedElementMasses = initializeMaxAllowedElementMasses();

        mandatoryComponents = sortMandatoryComponents();

        ArrayList<CompInCharge> mandatoryCompsTemp = sortMandatoryComponentsByPrice();

        while(!areListsEqual((ArrayList) mandatoryComponents, mandatoryCompsTemp))
        {
            for (CompInCharge aMandatoryCompsTemp : mandatoryCompsTemp)
            {
                aMandatoryCompsTemp.setCurrentPercent(aMandatoryCompsTemp.getMinPercent());
                curPercSum += aMandatoryCompsTemp.getCurrentPercent() / 100;
            }

            massChRes = calculateCurrentMassChargeOfComponents(mandatoryCompsTemp);
            massChTemp = massChRes * curPercSum;

            for (int i = 0; i < mandatoryCompsTemp.size(); ++i)
            {
                mandatoryCompsTemp.get(i).setCurrentMass(massChTemp * mandatoryCompsTemp.get(i).getCurrentPercent() / 100);
            }

            int p = 0;
            while (true)
            {
                curPercSum = 0;
                mandatoryCompsTemp.get(p).setCurrentPercent(mandatoryCompsTemp.get(p).getMaxPercent());
                for (CompInCharge aMandatoryCompsTemp : mandatoryCompsTemp)
                {
                    curPercSum += aMandatoryCompsTemp.getCurrentPercent() / 100;
                }

                if (curPercSum > 1)
                {
                    temp = curPercSum - 1;
                    mandatoryCompsTemp.get(p).setCurrentPercent(mandatoryCompsTemp.get(p).getCurrentPercent() - temp * 100);
                    curPercSum = 1;
                }

                massChRes = calculateCurrentMassChargeOfComponents(mandatoryCompsTemp);
                massChTemp = massChRes * curPercSum;

                for (int i = 0; i < mandatoryCompsTemp.size(); ++i)
                {
                    mandatoryCompsTemp.get(i).setCurrentMass(massChTemp *mandatoryCompsTemp.get(i).getCurrentPercent() / 100);
                }

                for (int i = 0; i < elements.size(); ++i)
                {
                    currentElementMasses = calculateElementMassesInCurrentMelt(mandatoryCompsTemp);

                    if (currentElementMasses[i] > maxAllowedElementMasses[i])
                    {
                        temp = currentElementMasses[i] - minAllowedElementMasses[i];//масса, которую надо вычесть
                        temp /= massChTemp;//процент, который нужно вычесть
                        mandatoryCompsTemp.get(p).setCurrentPercent(mandatoryCompsTemp.get(p).getCurrentPercent() - temp * 100);

                        massChRes = calculateCurrentMassChargeOfComponents(mandatoryCompsTemp);
                        massChTemp = massChRes * curPercSum;

                        for (i = 0; i < mandatoryCompsTemp.size(); ++i)
                        {
                            mandatoryCompsTemp.get(i).setCurrentMass(massChTemp * mandatoryCompsTemp.get(i).getCurrentPercent() / 100);
                        }

                    }
                }
                curPercSum = 0;
                for (CompInCharge aMandatoryCompsTemp : mandatoryCompsTemp)
                {
                    curPercSum += aMandatoryCompsTemp.getCurrentPercent() / 100;
                }

                if (1 == curPercSum)
                {
                    isChargeSuccessful = true;
                    break;
                }
                else
                {
                    ++p;
                    if (p >= mandatoryCompsTemp.size())
                    {
                        mandatoryCompsTemp = change(mandatoryCompsTemp);
                        break;
                    }
                }
            }
            if (isChargeSuccessful)
            {
               break;
            }
        }

        for (int i = 0; i < mandatoryCompsTemp.size(); ++i)
        {
            mandatoryCompsTemp.get(i).setCurrentMass((double)Math.round(massChTemp * mandatoryCompsTemp.get(i).getCurrentPercent()/100*10)/10);
        }
        mandatoryComponents = mandatoryCompsTemp;

        correctCharge();
    }

    public boolean areListsEqual(ArrayList<CompInCharge> list1, ArrayList<CompInCharge> list2)
    {
        if (list1.size() != list2.size())
        {
            return false;
        }

        for (int i = 0; i < list1.size(); ++i)
        {
            if (list1.get(i).getName().compareTo(list2.get(i).getName()) != 0)
            {
                return false;
            }
        }
        return true;
    }

    public void correctCharge()
    {
        double[] minElementMasses = new double[elements.size()];
        for (int i = 0; i < elements.size(); ++i)
        {
            minElementMasses[i] = elements.get(i).getMinPercentDouble() * mass / 100;
        }
        double currentElementMasses[] = calculateElementMassesInCurrentMelt();
        double[] delta = new double[elements.size()];

        for (int i = 0; i < elements.size(); ++i)
        {
            delta[i] = currentElementMasses[i] - minElementMasses[i];
            if (delta[i] < 0)
            {
                optionalComponents = sortOptionalComponentsByElement(elements.get(i));

                double adoptOfCurrentElement = optionalComponents.get(0).getComponent().getElements().get(i).getAdopt();
                double percentOfCurrentElement = optionalComponents.get(0).getComponent().getElements().get(i).getPercent();
                double minPercentOfCurrentElement = elements.get(i).getMinPercentDouble();

                long   newComponentMassMultipliedBy100 = Math.round(((minPercentOfCurrentElement / 100 * mass - currentElementMasses[i]) / (adoptOfCurrentElement / 100 * (percentOfCurrentElement / 100 - minPercentOfCurrentElement / 100)) * 100));
                double newComponentMass = (double) newComponentMassMultipliedBy100 / 100;
                optionalComponents.get(0).setCurrentMass(newComponentMass);
            }
        }
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