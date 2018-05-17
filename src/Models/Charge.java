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

    public Charge(User user, double mass, double deltaMass, Date dateCharge, MeltBrand meltBrand, List<CompInCharge> mandatoryComponents, List<CompInCharge> optionalComponents, List<Element> elements)
    {
        this.id = 0;
        this.user = user;
        this.mass = mass;
        this.deltaMass = deltaMass;
        this.dateCharge = dateCharge;
        this.meltBrand = meltBrand;
        this.mandatoryComponents = mandatoryComponents;
        this.optionalComponents = optionalComponents;
        this.elements = elements;

        this.optionalComponents = new ArrayList<>();
        for(Component aComponent: Component.getAllOptionalComponents())
        {
            this.optionalComponents.add(new CompInCharge(aComponent, 0, 0, 0));
        }
    }

    public Charge(int id, User user, double mass, double deltaMass, Date dateCharge, MeltBrand meltBrand, List<CompInCharge> mandatoryComponents, List<CompInCharge> optionalComponents, List<Element> elements)
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
        for(Component aComponent: Component.getAllOptionalComponents())
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

        for(Component component: Component.getAllMandatoryComponents())
        {
            if(mandatoryComponents.contains(component.getName()))
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
        for(Component component: Component.getAllOptionalComponents())
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

    public  boolean canEditPercent(String elementName, double percent)
    {
        for(Element element: meltBrand.getElements())
        {
            if(element.getName().equals(elementName))
            {
                return percent >= element.getMinPercentDouble() && percent <= element.getMaxPercentDouble();
            }
        }
        return false;
    }

    public boolean isPossible()
    {
        int i;
        double temp;

        double curPercSum = 0;

        double massM[] = new double[mandatoryComponents.size()];

        double massChTemp;
        double massChRes;
        double massMTemp = 0;

        double massET[] = new double[elements.size()];
        for(i = 0; i < elements.size(); ++i)
        {
            massET[i] = 0;
        }

        double[] massETmin = new double[elements.size()];
        for(i = 0; i < elements.size(); ++i)
        {
            massETmin[i] = elements.get(i).getMinPercentDouble() * mass / 100;
        }

        double[] massETmax = new double[elements.size()];
        for(i = 0; i < elements.size(); ++i)
        {
            massETmax[i] = elements.get(i).getMaxPercentDouble() * mass / 100;
        }

        mandatoryComponents = sortMandatoryComps();

        for(i = 0; i < mandatoryComponents.size(); ++i)
        {
            mandatoryComponents.get(i).setCurrentPercent(mandatoryComponents.get(i).getMinPercent());
            curPercSum+=mandatoryComponents.get(i).getCurrentPercent() / 100;
        }

        massChRes = calculateCurrentMassChargeOfComponents(mandatoryComponents);
        massChTemp = massChRes*curPercSum;

        massMTemp = mass * curPercSum;

        for(i = 0; i < mandatoryComponents.size(); ++i)
        {
            mandatoryComponents.get(i).setCurrentMass(massChTemp * mandatoryComponents.get(i).getCurrentPercent() / 100);
            massM[i] = mandatoryComponents.get(i).getCurrentMass() * mandatoryComponents.get(i).getComponent().getAdoptComp() / 100;
        }

        massET = calculateElementMassesInCurrentMelt();
        //ошибка: взяли всех обяхательных компонентов по минимуму и получилось элементов юольше, чем минимальная граница:
        for(i = 0; i < elements.size(); ++i)
        {
            if(massET[i] > (elements.get(i).getMaxPercentDouble() * mass / 100))
            {
                return false;
            }
        }

        int p = 0;
        while(true)
        {
            curPercSum = 0;
            mandatoryComponents.get(p).setCurrentPercent(mandatoryComponents.get(p).getMaxPercent());
            for (i = 0; i < mandatoryComponents.size(); ++i)
            {
                curPercSum += mandatoryComponents.get(i).getCurrentPercent()/100;
            }

            if (curPercSum > 1)
            {
                temp = curPercSum - 1;
                mandatoryComponents.get(p).setCurrentPercent(mandatoryComponents.get(p).getCurrentPercent() - temp * 100);
                curPercSum = 1;
            }

            massChRes = calculateCurrentMassChargeOfComponents(mandatoryComponents);
            massChTemp = massChRes * curPercSum;
            massMTemp = mass * curPercSum;

            for (i = 0; i < mandatoryComponents.size(); ++i)
            {
                mandatoryComponents.get(i).setCurrentMass(massChTemp * mandatoryComponents.get(i).getCurrentPercent() / 100);

                massM[i] = mandatoryComponents.get(i).getCurrentMass() * mandatoryComponents.get(i).getComponent().getAdoptComp() / 100;
            }

            for (i = 0; i < elements.size(); ++i)
            {
                massET = calculateElementMassesInCurrentMelt();

                if (massET[i] > massETmax[i])
                {
                    temp = massET[i] - massETmin[i];//масса, которую надо вычесть
                    temp /= massChTemp;//процент, который нужно вычесть
                    mandatoryComponents.get(p).setCurrentPercent(mandatoryComponents.get(p).getCurrentPercent() - temp * 100);

                    massChRes = calculateCurrentMassChargeOfComponents(mandatoryComponents);
                    massChTemp = massChRes * curPercSum;
                    massMTemp = mass * curPercSum;

                    for (i = 0; i < mandatoryComponents.size(); ++i)
                    {
                        mandatoryComponents.get(i).setCurrentMass(massChTemp * mandatoryComponents.get(i).getCurrentPercent() / 100);
                        massM[i] = mandatoryComponents.get(i).getCurrentMass() * mandatoryComponents.get(i).getComponent().getAdoptComp() / 100;
                    }

                }
            }
            curPercSum = 0;
            for (i = 0; i < mandatoryComponents.size(); ++i)
            {
                curPercSum += mandatoryComponents.get(i).getCurrentPercent() / 100;
            }
            if (1 == curPercSum)
            {
                break;
            }
            else
            {
                p++;
                if (p >= mandatoryComponents.size())
                {
                    return false;
                }
            }
        }

        for (i = 0; i < mandatoryComponents.size(); ++i)
        {
            mandatoryComponents.get(i).setCurrentMass((double)Math.round(massChTemp * mandatoryComponents.get(i).getCurrentPercent()/100*10)/10);
            massM[i] = (double)Math.round(mandatoryComponents.get(i).getCurrentMass() * mandatoryComponents.get(i).getComponent().getAdoptComp()/100*10)/10;
        }

        return true;

    }

    public ArrayList<CompInCharge> sortMandatoryComps()
    {
        ArrayList<CompInCharge> newList = new ArrayList<>();
        // всегда ли у всех заданных компонентов одиноковый набор элементов?
        double array[][]=new double[mandatoryComponents.size()][mandatoryComponents.get(0).getComponent().getElements().size()];
        for(int i=0; i<mandatoryComponents.size(); ++i)
        {
            for(int j=0; j< mandatoryComponents.get(i).getComponent().getElements().size(); j++)
            {
                for(int k=0; k<elements.size(); k++)
                {
                    if(mandatoryComponents.get(i).getComponent().getElements().get(j).getName().equals(mandatoryComponents.get(i).getComponent().getElements().get(k).getName()))
                    {
                        array[i][j] = mandatoryComponents.get(i).getComponent().getElements().get(j).getPercent()/100*mandatoryComponents.get(i).getComponent().getElements().get(j).getAdopt()/100/elements.get(k).getMaxPercentDouble()/100;
                    }
                }
            }
        }

        double arrayMax[]=new double[mandatoryComponents.size()];
        double max;

        for(int i = 0; i < mandatoryComponents.size(); ++i)
        {
            max=array[i][0];
            for (int j = 0; j < mandatoryComponents.get(0).getComponent().getElements().size(); j++)
            {
                if(array[i][j]>max)
                {
                    max = array[i][j];
                }
            }
            arrayMax[i]=max;
        }

        int min;

        while(newList.size()<mandatoryComponents.size())
        {
            min = 0;
            for (int i = 0; i < mandatoryComponents.size(); ++i)
            {
                if (arrayMax[i] < arrayMax[min])
                {
                    min = i;
                }
            }
            newList.add(mandatoryComponents.get(min));

            arrayMax[min] = 100;
        }

        return newList;
    }

    //возвращает массу хим. элементов в расплаве
    public double[] calculateElementMassesInCurrentMelt()
    {
        double[] massET = new double[elements.size()];
        for(int i = 0; i < elements.size(); ++i)
        {
            massET[i] = 0;
        }

        for (CompInCharge mandatoryComponent : mandatoryComponents)
        {
            for (int j = 0; j < elements.size(); j++)
            {
                massET[j] += mandatoryComponent.getCurrentMass() * mandatoryComponent.getComponent().getElements().get(j).getPercent() / 100 * mandatoryComponent.getComponent().getElements().get(j).getAdopt() / 100;
            }
        }

        for(CompInCharge optionalComponent: optionalComponents)
        {
            if(optionalComponent.getCurrentMass() > 0)
            {
                for (int j = 0; j < elements.size(); j++)
                {
                    massET[j] += optionalComponent.getCurrentMass() * optionalComponent.getComponent().getElements().get(j).getPercent() / 100 * optionalComponent.getComponent().getElements().get(j).getAdopt() / 100;
                }
            }
        }

        return massET;
    }

    public double[] calculateElementMassesInCurrentMelt(ArrayList<CompInCharge> list)
    {
        double[] massET = new double[elements.size()];
        for(int i = 0; i < elements.size(); ++i)
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

        for(CompInCharge optionalComponent: optionalComponents)
        {
            if(optionalComponent.getCurrentMass()>0)
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

        while(sortedList.size() < mandatoryComponents.size())
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
        for(i = 0; i < mandatoryCompsTemp.size(); ++i)
        {
            newList.add(mandatoryCompsTemp.get(i));
        }
        for(i = 0; i < mandatoryComponents.size(); ++i)
        {
            if (mandatoryComponents.get(i).getName().compareTo(newList.get(i).getName()) != 0)
            {
                for(int j = i; j < newList.size(); j++)
                {
                    if(newList.get(j).getName().compareTo(mandatoryComponents.get(i).getName()) == 0)
                    {
                        while(j != i)
                        {
                            temp.setCompInCharge(newList.get(j));
                            newList.get(j).setCompInCharge(newList.get(j - 1));
                            newList.get(j - 1).setCompInCharge(temp);
                            j--;
                        }
                        return newList;
                    }
                }
            }
        }
        return newList;
    }

    public ArrayList<CompInCharge> sortOptionalComponentsByElement(Element element)
    {
        int max = 0;
        ArrayList<CompInCharge> sortedList = new ArrayList<>();

        while (sortedList.size() < optionalComponents.size())
        {
            for (int i = 0; i < optionalComponents.size(); ++i)
            {
                for (int j = 0; j < optionalComponents.get(i).getComponent().getElements().size(); j++)
                {
                    if (optionalComponents.get(i).getComponent().getElements().get(j).getName().equals(element.getName()))
                    {
                        if (optionalComponents.get(i).getComponent().getElements().get(j).getPercent() > optionalComponents.get(max).getComponent().getElements().get(j).getPercent())
                        {
                            max = i;
                        }
                    }
                }
            }
            sortedList.add(optionalComponents.get(max));
            optionalComponents.remove(max);
            max = 0;
        }
        return sortedList;
    }

    public void calculateCheapCharge()
    {
        boolean meltSuccess = false;
        int i;
        double temp;

        double curPercSum = 0;

        double massM[] = new double[mandatoryComponents.size()];

        double massChTemp = 0;
        double massChRes;
        double massMTemp = 0;

        double massET[] = new double[elements.size()];
        for(i = 0; i < elements.size(); ++i)
        {
            massET[i] = 0;
        }


        double[] massETmin = new double[elements.size()];
        for(i = 0; i < elements.size(); ++i)
        {
            massETmin[i] = elements.get(i).getMinPercentDouble() * mass / 100;
        }

        double[] massETmax = new double[elements.size()];
        for(i = 0; i < elements.size(); ++i)
        {
            massETmax[i] = elements.get(i).getMaxPercentDouble() * mass / 100;
        }

        mandatoryComponents = sortMandatoryComps();

        ArrayList<CompInCharge> mandatoryCompsTemp = sortMandatoryComponentsByPrice();

        while(!areListsEqual((ArrayList)mandatoryComponents, mandatoryCompsTemp))
        {

            for(i = 0; i<mandatoryCompsTemp.size(); ++i)
            {

                mandatoryCompsTemp.get(i).setCurrentPercent(mandatoryCompsTemp.get(i).getMinPercent());
                curPercSum+=mandatoryCompsTemp.get(i).getCurrentPercent() / 100;
            }

            massChRes = calculateCurrentMassChargeOfComponents(mandatoryCompsTemp);
            massChTemp = massChRes * curPercSum;

            for(i=0; i<mandatoryCompsTemp.size(); ++i)
            {

                mandatoryCompsTemp.get(i).setCurrentMass(massChTemp * mandatoryCompsTemp.get(i).getCurrentPercent() / 100);
                massM[i] = mandatoryCompsTemp.get(i).getCurrentMass() * mandatoryCompsTemp.get(i).getComponent().getAdoptComp() / 100;
            }

            massET = calculateElementMassesInCurrentMelt(mandatoryCompsTemp);

            int p = 0;
            while (true)
            {

                curPercSum = 0;
                mandatoryCompsTemp.get(p).setCurrentPercent(mandatoryCompsTemp.get(p).getMaxPercent());
                for (i = 0; i < mandatoryCompsTemp.size(); ++i)
                {
                    curPercSum += mandatoryCompsTemp.get(i).getCurrentPercent() / 100;
                }

                if (curPercSum > 1)
                {
                    temp = curPercSum - 1;
                    mandatoryCompsTemp.get(p).setCurrentPercent(mandatoryCompsTemp.get(p).getCurrentPercent() - temp * 100);
                    curPercSum = 1;
                }


                massChRes = calculateCurrentMassChargeOfComponents(mandatoryCompsTemp);
                massChTemp = massChRes * curPercSum;
                massMTemp = mass * curPercSum;

                for (i = 0; i < mandatoryCompsTemp.size(); ++i)
                {
                    mandatoryCompsTemp.get(i).setCurrentMass(massChTemp *mandatoryCompsTemp.get(i).getCurrentPercent() / 100);
                    massM[i] = mandatoryCompsTemp.get(i).getCurrentMass() * mandatoryCompsTemp.get(i).getComponent().getAdoptComp() / 100;
                }

                for (i = 0; i < elements.size(); ++i)
                {
                    massET = calculateElementMassesInCurrentMelt(mandatoryCompsTemp);

                    if (massET[i] > massETmax[i])
                    {
                        temp = massET[i] - massETmin[i];//масса, которую надо вычесть
                        temp /= massChTemp;//процент, который нужно вычесть
                        mandatoryCompsTemp.get(p).setCurrentPercent(mandatoryCompsTemp.get(p).getCurrentPercent() - temp * 100);

                        massChRes = calculateCurrentMassChargeOfComponents(mandatoryCompsTemp);
                        massChTemp = massChRes * curPercSum;
                        massMTemp = mass * curPercSum;

                        for (i = 0; i < mandatoryCompsTemp.size(); ++i)
                        {
                            mandatoryCompsTemp.get(i).setCurrentMass(massChTemp * mandatoryCompsTemp.get(i).getCurrentPercent() / 100);
                            massM[i] = mandatoryCompsTemp.get(i).getCurrentMass() * mandatoryCompsTemp.get(i).getComponent().getAdoptComp() / 100;
                        }

                    }
                }
                curPercSum=0;
                for (i = 0; i < mandatoryCompsTemp.size(); ++i)
                {
                    curPercSum += mandatoryCompsTemp.get(i).getCurrentPercent()/100;
                }


                if (curPercSum == 1)
                {
                    meltSuccess=true;
                    break;
                }
                else
                {
                    p++;
                    if (p >= mandatoryCompsTemp.size())
                    {
                        mandatoryCompsTemp = change(mandatoryCompsTemp);
                        break;
                    }
                }
            }
            if(meltSuccess)
                break;
        }

        for (i = 0; i < mandatoryCompsTemp.size(); ++i)
        {
            mandatoryCompsTemp.get(i).setCurrentMass((double)Math.round(massChTemp * mandatoryCompsTemp.get(i).getCurrentPercent()/100*10)/10);
            massM[i] = (double)Math.round(mandatoryCompsTemp.get(i).getCurrentMass() * mandatoryCompsTemp.get(i).getComponent().getAdoptComp()/100*10)/10;
        }
        mandatoryComponents = mandatoryCompsTemp;

        correctCharge();
    }

    public boolean areListsEqual(ArrayList<CompInCharge> list1, ArrayList<CompInCharge> list2)
    {
        if(list1.size() != list2.size())
        {
            return false;
        }

        for(int i = 0; i < list1.size(); ++i)
        {
            if(list1.get(i).getName().compareTo(list2.get(i).getName()) != 0)
            {
                return false;
            }
        }
        return true;
    }

    public void correctCharge()
    {
        double[] minElementMasses = new double[elements.size()];
        for(int i = 0; i < elements.size(); ++i)
        {
            minElementMasses[i] = elements.get(i).getMinPercentDouble() * mass / 100;
        }

        double[] delta = new double[elements.size()];

        double currentElementMasses[] = calculateElementMassesInCurrentMelt();

        for(int i = 0; i < elements.size(); ++i)
        {
            delta[i] = currentElementMasses[i] - minElementMasses[i];
            if(delta[i] < 0)
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

        for(CompInCharge component: optionalComponents)
        {
            if(component.getCurrentMass() > 0)
            {
                allComponents.add(component);
            }
        }
        return allComponents;
    }

    public void saveToDB() throws RuntimeException
    {
        saveGeneralChargeToDB();

        int idCharge = getIndexOfLastSavedCharge();

        saveElementsInCharge(idCharge);
        saveMandatoryComponentsInCharge(idCharge);
        saveOptionalComponentsInCharge(idCharge);
    }

    private void saveGeneralChargeToDB() throws RuntimeException
    {
        SQLiteUtil.dbExecuteUpdate("INSERT INTO charge (mass, deltaMass, dateCharge, User_idUser, MeltBrand_idMeltBrand)\n" +
                                   "VALUES ('" + mass + "', '" +
                                                 deltaMass + "', '" +
                                                 new java.sql.Date(dateCharge.getTime()) + "', '" +
                                                 getIdUserFromDb(user.getName(), user.getLastname()) + "', '" +
                                                 getIdMeltBrandFromDb(meltBrand.getName()) + "');");
    }

    private void saveElementsInCharge(int idCharge) throws RuntimeException
    {
        for(Element element: elements)
        {
            SQLiteUtil.dbExecuteUpdate("INSERT INTO elementincharge (minProcent, maxProcent, Charge_idCharge, Element_idElement)\n" +
                                        "VALUES ('" + element.getMinPercentDouble() + "', '" +
                                                      element.getMaxPercentDouble() + "', '" +
                                                      idCharge + "', '" +
                                                      getIdElementFromDb(element.getName()) + "');");
        }
    }

    private void saveMandatoryComponentsInCharge(int idCharge) throws RuntimeException
    {
        for(CompInCharge component: mandatoryComponents)
        {
            SQLiteUtil.dbExecuteUpdate("INSERT INTO componentincharge (currentMass, minProcent, maxProcent, Charge_idCharge, Component_idComponent)\n" +
                                        "VALUES ('" + component.getCurrentMass() + "', '" +
                                                      component.getMinPercent() + "', '" +
                                                      component.getMaxPercent() + "', '" +
                                                      idCharge + "', '" +
                                                      getIdComponentFromDb(component.getName()) + "');");
        }
    }

    private void saveOptionalComponentsInCharge(int idCharge) throws RuntimeException
    {
        for(CompInCharge component: optionalComponents)
        {
            if (component.getCurrentMass() != 0)
            {
                SQLiteUtil.dbExecuteUpdate("INSERT INTO componentincharge (currentMass, minProcent, maxProcent, Charge_idCharge, Component_idComponent)\n" +
                                           "VALUES ('" + component.getCurrentMass() + "', '" +
                                                         component.getMinPercent() + "', '" +
                                                         component.getMaxPercent() + "', '" +
                                                         idCharge + "', '" +
                                                         getIdComponentFromDb(component.getName()) + "');");
            }
        }
    }

    public static int getIndexOfLastSavedCharge() throws RuntimeException
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

    public static ObservableList<Charge> getChargesOfBrand(String meltBrand) throws RuntimeException
    {
        ObservableList<Charge> charges = FXCollections.observableArrayList();

        String query = "";
        try
        {
            query = "SELECT * FROM charge WHERE MeltBrand_idMeltBrand='" + getIdMeltBrandFromDb(meltBrand) + "';";
            ResultSet rs = SQLiteUtil.dbExecuteQuery(query);
            while (rs.next())
            {
                Charge tempCharge = new Charge(rs.getInt("idCharge"), null, rs.getDouble("mass"), rs.getDouble("deltaMass"), null, new MeltBrand(meltBrand, null), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

                tempCharge.getOptionalComponents().clear();
                query = "SELECT * FROM elementincharge WHERE `Charge_idCharge`='" + rs.getInt("idCharge") + "';";
                ResultSet rs2 = SQLiteUtil.dbExecuteQuery(query);

                while (rs2.next())
                {
                    query = "SELECT * FROM element WHERE `idElement`='" + rs2.getInt("Element_idElement") + "';";
                    ResultSet rs3 = SQLiteUtil.dbExecuteQuery(query);

                    if(rs3.next())
                    {
                        tempCharge.getElements().add(new Element(rs3.getString("name"), rs2.getDouble("minProcent"), rs2.getDouble("maxProcent"), 0, 0));
                    }
                }

                query = "SELECT name, currentMass, mandatory FROM componentincharge CIC JOIN component C ON C.idComponent = CIC.Component_idComponent WHERE Charge_idCharge='" + rs.getInt("idCharge") + "';";
                ResultSet rs4 = SQLiteUtil.dbExecuteQuery(query);
                while (rs4.next())
                {
                    if (rs4.getInt("mandatory") == 1)
                    {
                        tempCharge.getMandatoryComponents().add(new CompInCharge(new Component(rs4.getString("name"), null, 0, 0, 0, 0, 0, new ArrayList<>()), rs4.getDouble("currentMass"), 0, 0));
                    }
                    else
                    {
                        tempCharge.getOptionalComponents().add(new CompInCharge(new Component(rs4.getString("name"), null, 0, 0, 0, 0, 0, new ArrayList<>()), rs4.getDouble("currentMass"), 0, 0));
                    }
                }

                charges.add(tempCharge);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }

        return charges;
    }

    private static int getIdMeltBrandFromDb(String meltBrandName) throws RuntimeException
    {
        int idMeltBrand = 0;
        String query = "";
        try
        {
            query = "SELECT * FROM meltbrand WHERE name='" + meltBrandName + "';";
            ResultSet rs = SQLiteUtil.dbExecuteQuery(query);
            if (rs.next())
            {
               idMeltBrand = rs.getInt("idMeltBrand");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }
        return idMeltBrand;
    }

    private static int getIdUserFromDb(String firstname, String lastname) throws RuntimeException
    {
        int idUser = 0;
        String query = "";
        try
        {
            query = "SELECT * FROM user WHERE name = '" + firstname + "' AND lastname = '" + lastname + "';";
            ResultSet rs = SQLiteUtil.dbExecuteQuery(query);
            if (rs.next())
            {
                idUser = rs.getInt("idUser");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }
        return idUser;
    }

    private static int getIdElementFromDb(String elementName) throws RuntimeException
    {
        int idElement = 0;
        String query = "SELECT * FROM element WHERE name = '" + elementName + "'";
        try
        {
            ResultSet rs = SQLiteUtil.dbExecuteQuery(query);
            if (rs.next())
            {
                idElement = rs.getInt("idElement");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }
        return idElement;
    }

    private static int getIdComponentFromDb(String componentName) throws RuntimeException
    {
        int idComponent = 0;
        String query = "SELECT * FROM component WHERE name = '" + componentName + "';";
        try
        {
            ResultSet rs = SQLiteUtil.dbExecuteQuery(query);
            if (rs.next())
            {
                idComponent = rs.getInt("idComponent");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }
        return idComponent;
    }
}