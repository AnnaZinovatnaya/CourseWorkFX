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
        for(String aString: mandatoryComponents)
        {
            for(Component aComponent: Component.getAllMandatoryComponents())
            {
                if(aComponent.getName().equals(aString))
                {
                    this.mandatoryComponents.add(new CompInCharge(aComponent, 0, 0, 0));
                }
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
        ArrayList<Component> temp = Component.getAllOptionalComponents();
        for(Component aComponent: temp)
        {
            this.optionalComponents.add(new CompInCharge(aComponent, 0, 0, 0));
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

    public  boolean canEditPercent(String element, double percent)
    {
        for(Element aElement: meltBrand.getElements())
        {
            if(aElement.getName().equals(element))
            {
                return percent >= aElement.getMinPercentDouble() && percent <= aElement.getMaxPercentDouble();
            }
        }

        return false;
    }

    public boolean isPossible()
    {
        int i;
        double temp;

        double curPercSum=0;

        double massM[] = new double[mandatoryComponents.size()];

        double massChTemp;
        double massChRes;
        double massMTemp = 0;

        double massET[] = new double[elements.size()];
        for(i = 0; i<elements.size(); i++)
        {
            massET[i] = 0;
        }


        double[] massETmin = new double[elements.size()];
        for(i=0; i<elements.size(); i++)
        {
            massETmin[i] = elements.get(i).getMinPercentDouble()*mass/100;
        }

        double[] massETmax = new double[elements.size()];
        for(i=0; i<elements.size(); i++)
        {
            massETmax[i] = elements.get(i).getMaxPercentDouble()*mass/100;
        }

        mandatoryComponents = sortMandatoryComps();

        for(i=0; i<mandatoryComponents.size(); i++)
        {

            mandatoryComponents.get(i).setCurrentPercent(mandatoryComponents.get(i).getMinPercent());
            curPercSum+=mandatoryComponents.get(i).getCurrentPercent()/100;

        }

        massChRes=calculateMassCharge();
        massChTemp=massChRes*curPercSum;

        massMTemp=mass*curPercSum;

        for(i=0; i<mandatoryComponents.size(); i++)
        {

            mandatoryComponents.get(i).setCurrentMass(massChTemp * mandatoryComponents.get(i).getCurrentPercent()/100);
            massM[i] = mandatoryComponents.get(i).getCurrentMass() * mandatoryComponents.get(i).getComponent().getAdoptComp()/100;
        }

        massET = checkElements();
        //ошибка: взяли всех обяхательных компонентов по минимуму и получилось элементов юольше, чем минимальная граница:
        for(i=0; i<elements.size(); i++)
        {
            if(massET[i]> (elements.get(i).getMaxPercentDouble()*mass/100))
            {
                //System.out.println("Строка 185");
                //System.out.println(elements.get(i).getName()+"\t"+(elements.get(i).getMaxPercentDouble()*mass/100)+"\t"+massET[i]);
                return false;
            }
        }

        int p=0;
        while(true)
        {

            curPercSum = 0;
            mandatoryComponents.get(p).setCurrentPercent(mandatoryComponents.get(p).getMaxPercent());
            for (i = 0; i < mandatoryComponents.size(); i++)
            {
                curPercSum += mandatoryComponents.get(i).getCurrentPercent()/100;
            }

            if (curPercSum > 1)
            {
                temp = curPercSum - 1;
                mandatoryComponents.get(p).setCurrentPercent(mandatoryComponents.get(p).getCurrentPercent()-temp*100);
                curPercSum = 1;
            }


            massChRes = calculateMassCharge();
            massChTemp = massChRes * curPercSum;
            massMTemp = mass * curPercSum;

            for (i = 0; i < mandatoryComponents.size(); i++)
            {
                mandatoryComponents.get(i).setCurrentMass(massChTemp *mandatoryComponents.get(i).getCurrentPercent()/100);

                massM[i] = mandatoryComponents.get(i).getCurrentMass() * mandatoryComponents.get(i).getComponent().getAdoptComp()/100;
            }

            for (i = 0; i < elements.size(); i++)
            {
                massET = checkElements();

                if (massET[i] > massETmax[i])
                {
                    temp = massET[i] - massETmin[i];//масса, которую надо вычесть
                    temp /= massChTemp;//процент, который нужно вычесть
                    mandatoryComponents.get(p).setCurrentPercent(mandatoryComponents.get(p).getCurrentPercent()-temp*100);

                    massChRes = calculateMassCharge();
                    massChTemp = massChRes * curPercSum;
                    massMTemp = mass * curPercSum;

                    for (i = 0; i < mandatoryComponents.size(); i++)
                    {
                        mandatoryComponents.get(i).setCurrentMass(massChTemp * mandatoryComponents.get(i).getCurrentPercent()/100);
                        massM[i] = mandatoryComponents.get(i).getCurrentMass() * mandatoryComponents.get(i).getComponent().getAdoptComp()/100;
                    }

                }
            }
            curPercSum=0;
            for (i = 0; i < mandatoryComponents.size(); i++)
            {
                curPercSum += mandatoryComponents.get(i).getCurrentPercent()/100;
            }
            if (curPercSum==1)
            {
                break;
            }
            else
            {
                p++;
                if (p >= mandatoryComponents.size())
                {
                    //System.out.println("Строка 245");
                    return false;
                }
            }
        }


        for (i = 0; i < mandatoryComponents.size(); i++)
        {

            mandatoryComponents.get(i).setCurrentMass((double)Math.round(massChTemp * mandatoryComponents.get(i).getCurrentPercent()/100*10)/10);
            massM[i] = (double)Math.round(mandatoryComponents.get(i).getCurrentMass() * mandatoryComponents.get(i).getComponent().getAdoptComp()/100*10)/10;

        }

        //massChTemp=(double)Math.round(massChTemp*10)/10;
        return true;

    }

    public ArrayList<CompInCharge> sortMandatoryComps()
    {
        ArrayList<CompInCharge> newList = new ArrayList<>();
        // всегда ли у всех заданных компонентов одиноковый набор элементов?
        double array[][]=new double[mandatoryComponents.size()][mandatoryComponents.get(0).getComponent().getElements().size()];
        for(int i=0; i<mandatoryComponents.size(); i++)
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

        for(int i=0; i<mandatoryComponents.size(); i++)
        {
            max=array[i][0];
            for (int j = 0; j < mandatoryComponents.get(0).getComponent().getElements().size(); j++)
            {
                if(array[i][j]>max)
                    max=array[i][j];
            }
            arrayMax[i]=max;
        }

        int min;

        while(newList.size()<mandatoryComponents.size())
        {
            min=0;
            for (int i = 0; i < mandatoryComponents.size(); i++)
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
    public double[] checkElements()
    {
        double[] massET = new double[elements.size()];
        for(int i = 0; i<elements.size(); i++)
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

    public double[] checkElements(ArrayList<CompInCharge> list)
    {
        double[] massET = new double[elements.size()];
        for(int i = 0; i<elements.size(); i++)
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

    double calculateMassCharge()
    {

        double sumPerc=0;
        double massCharge;
        double sumKadoptPerc=0;
        for (CompInCharge mandatoryComponent : mandatoryComponents)
        {
            sumPerc += mandatoryComponent.getCurrentPercent() / 100;
            sumKadoptPerc += (mandatoryComponent.getComponent().getAdoptComp() * mandatoryComponent.getCurrentPercent() / 100);
        }

        if(sumPerc>1)
            return -1;

        massCharge=mass*sumPerc;
        massCharge/=sumKadoptPerc;

        return massCharge;
    }

    double calculateMassCharge(ArrayList<CompInCharge> list)
    {

        double sumPerc=0;
        double massCharge;
        double sumKadoptPerc=0;
        for (CompInCharge aList : list)
        {
            sumPerc += aList.getCurrentPercent() / 100;
            sumKadoptPerc += (aList.getComponent().getAdoptComp() * aList.getCurrentPercent() / 100);
        }

        if(sumPerc>1)
            return -1;

        massCharge=mass*sumPerc;
        massCharge/=sumKadoptPerc;

        return massCharge;
    }

    public ArrayList<CompInCharge> sortByPrice()
    {
        ArrayList<CompInCharge> newList = new ArrayList<>();
        double prices[] = new double[mandatoryComponents.size()];

        for (int i = 0; i < mandatoryComponents.size(); i++)
        {
            prices[i]=mandatoryComponents.get(i).getComponent().getPriceDouble();
        }

        while(newList.size()<mandatoryComponents.size())
        {
            int indexMinPrice=0;
            for (int i = 0; i < mandatoryComponents.size(); i++)
            {
                if (prices[i] < prices[indexMinPrice])
                {
                    indexMinPrice = i;
                }
            }
            newList.add(mandatoryComponents.get(indexMinPrice));
            prices[indexMinPrice]=10000000;
        }
        return newList;
    }

    public ArrayList<CompInCharge> change(ArrayList<CompInCharge> mandatoryCompsTemp)
    {
        ArrayList<CompInCharge> newList = new ArrayList<>();
        CompInCharge temp = new CompInCharge(null, 0, 0, 0);
        int i;
        for(i=0; i<mandatoryCompsTemp.size(); i++)
        {
            newList.add(mandatoryCompsTemp.get(i));
        }
        for(i=0; i<mandatoryComponents.size(); i++)
        {
            if (mandatoryComponents.get(i).getName().compareTo(newList.get(i).getName())!=0)
            {
                for(int j=i; j<newList.size(); j++)
                {
                    if(newList.get(j).getName().compareTo(mandatoryComponents.get(i).getName())==0)
                    {
                        while(j!=i)
                        {
                            temp.setCompInCharge(newList.get(j));
                            newList.get(j).setCompInCharge(newList.get(j-1));
                            newList.get(j-1).setCompInCharge(temp);
                            j--;
                        }
                        return newList;
                    }
                }
            }
        }
        return newList;
    }

    public ArrayList<CompInCharge> sortByElement(Element element)
    {
        int max=0;
        int size;
        ArrayList<CompInCharge> newList = new ArrayList<>();
        size = optionalComponents.size();

        while(newList.size()<size)
        {
            for(int i=0; i<optionalComponents.size(); i++)
            {
                for(int j = 0; j<optionalComponents.get(i).getComponent().getElements().size(); j++)
                {
                    if(optionalComponents.get(i).getComponent().getElements().get(j).getName().equals(element.getName()))
                    {
                        if(optionalComponents.get(i).getComponent().getElements().get(j).getPercent()>optionalComponents.get(max).getComponent().getElements().get(j).getPercent())
                        {
                            max=i;
                        }
                    }
                }
            }
            newList.add(optionalComponents.get(max));
            optionalComponents.remove(max);
            max=0;
        }
        return newList;
    }

    public void calculateCheapCharge()
    {
        boolean meltSuccess = false;
        int i;
        double temp;

        double curPercSum=0;

        double massM[] = new double[mandatoryComponents.size()];

        double massChTemp=0;
        double massChRes;
        double massMTemp = 0;

        double massET[] = new double[elements.size()];
        for(i = 0; i<elements.size(); i++)
        {
            massET[i] = 0;
        }


        double[] massETmin = new double[elements.size()];
        for(i=0; i<elements.size(); i++)
        {
            massETmin[i] = elements.get(i).getMinPercentDouble()*mass/100;
        }

        double[] massETmax = new double[elements.size()];
        for(i=0; i<elements.size(); i++)
        {
            massETmax[i] = elements.get(i).getMaxPercentDouble()*mass/100;
        }

        mandatoryComponents = sortMandatoryComps();

        ArrayList<CompInCharge> mandatoryCompsTemp = sortByPrice();

        while(!areListsEqual((ArrayList)mandatoryComponents, mandatoryCompsTemp))
        {

            for(i=0; i<mandatoryCompsTemp.size(); i++)
            {

                mandatoryCompsTemp.get(i).setCurrentPercent(mandatoryCompsTemp.get(i).getMinPercent());
                curPercSum+=mandatoryCompsTemp.get(i).getCurrentPercent()/100;
            }

            massChRes = calculateMassCharge(mandatoryCompsTemp);
            massChTemp = massChRes * curPercSum;
            //massMTemp = mass * curPercSum;

            for(i=0; i<mandatoryCompsTemp.size(); i++)
            {

                mandatoryCompsTemp.get(i).setCurrentMass(massChTemp * mandatoryCompsTemp.get(i).getCurrentPercent()/100);
                massM[i] = mandatoryCompsTemp.get(i).getCurrentMass() * mandatoryCompsTemp.get(i).getComponent().getAdoptComp()/100;
            }

            massET = checkElements(mandatoryCompsTemp);

            int p = 0;
            while (true)
            {

                curPercSum = 0;
                mandatoryCompsTemp.get(p).setCurrentPercent(mandatoryCompsTemp.get(p).getMaxPercent());
                for (i = 0; i < mandatoryCompsTemp.size(); i++)
                {
                    curPercSum += mandatoryCompsTemp.get(i).getCurrentPercent()/100;
                }

                if (curPercSum > 1)
                {
                    temp = curPercSum - 1;
                    mandatoryCompsTemp.get(p).setCurrentPercent(mandatoryCompsTemp.get(p).getCurrentPercent()-temp*100);
                    curPercSum = 1;
                }


                massChRes = calculateMassCharge(mandatoryCompsTemp);
                massChTemp = massChRes * curPercSum;
                massMTemp = mass * curPercSum;

                for (i = 0; i < mandatoryCompsTemp.size(); i++)
                {
                    mandatoryCompsTemp.get(i).setCurrentMass(massChTemp *mandatoryCompsTemp.get(i).getCurrentPercent()/100);
                    massM[i] = mandatoryCompsTemp.get(i).getCurrentMass() * mandatoryCompsTemp.get(i).getComponent().getAdoptComp()/100;
                }



                for (i = 0; i < elements.size(); i++)
                {
                    massET = checkElements(mandatoryCompsTemp);

                    if (massET[i] > massETmax[i])
                    {
                        temp = massET[i] - massETmin[i];//масса, которую надо вычесть
                        temp /= massChTemp;//процент, который нужно вычесть
                        mandatoryCompsTemp.get(p).setCurrentPercent(mandatoryCompsTemp.get(p).getCurrentPercent()-temp*100);

                        massChRes = calculateMassCharge(mandatoryCompsTemp);
                        massChTemp = massChRes * curPercSum;
                        massMTemp = mass * curPercSum;

                        for (i = 0; i < mandatoryCompsTemp.size(); i++)
                        {
                            mandatoryCompsTemp.get(i).setCurrentMass(massChTemp * mandatoryCompsTemp.get(i).getCurrentPercent()/100);
                            massM[i] = mandatoryCompsTemp.get(i).getCurrentMass() * mandatoryCompsTemp.get(i).getComponent().getAdoptComp()/100;
                        }

                    }
                }
                curPercSum=0;
                for (i = 0; i < mandatoryCompsTemp.size(); i++)
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

        for (i = 0; i < mandatoryCompsTemp.size(); i++)
        {

            mandatoryCompsTemp.get(i).setCurrentMass((double)Math.round(massChTemp * mandatoryCompsTemp.get(i).getCurrentPercent()/100*10)/10);
            massM[i] = (double)Math.round(mandatoryCompsTemp.get(i).getCurrentMass() * mandatoryCompsTemp.get(i).getComponent().getAdoptComp()/100*10)/10;

        }
        mandatoryComponents = mandatoryCompsTemp;
        massChTemp=(double)Math.round(massChTemp*10)/10;

        correctCharge();
    }

    public boolean areListsEqual(ArrayList<CompInCharge> list1, ArrayList<CompInCharge> list2)
    {
        if(list1.size()!=list2.size())
            return false;
        for(int i=0; i<list1.size(); i++)
        {
            if(list1.get(i).getName().compareTo(list2.get(i).getName())!=0)
                return false;
        }
        return true;
    }

    public void correctCharge()
    {
        int i;
        double massET[];

        double[] massETmin = new double[elements.size()];
        double[] massETmax = new double[elements.size()];
        double[] delta = new double[elements.size()];
        for(i=0; i<elements.size(); i++)
        {
            massETmin[i] = elements.get(i).getMinPercentDouble()*mass/100;
            massETmax[i] = elements.get(i).getMaxPercentDouble()*mass/100;
        }

        for(i=0; i<elements.size(); i++)
        {
            massET = checkElements();
            delta[i] = massETmin[i] - massET[i];
            if(delta[i]>0)
            {
                optionalComponents = sortByElement(elements.get(i));

                optionalComponents.get(0).setCurrentMass((double) Math.round(((elements.get(i).getMinPercentDouble()/100*mass-massET[i])/(optionalComponents.get(0).getComponent().getElements().get(i).getAdopt()/100*(optionalComponents.get(0).getComponent().getElements().get(i).getPercent()/100-elements.get(0).getMinPercentDouble()/100))*100))/100);
                massET = checkElements();
            }
        }
    }

    public ObservableList<CompInCharge> getChargeResultComps()
    {
        ObservableList<CompInCharge> list = FXCollections.observableArrayList();
        list.addAll(mandatoryComponents);
        for(CompInCharge aComponent: optionalComponents)
        {
            if(aComponent.getCurrentMass()>0)
            {
                list.add(aComponent);
            }
        }
        return list;
    }

    public void saveToDB() throws RuntimeException
    {
        int idUser;
        int idMeltBrand;
        int idCharge;
        int idElement;
        int idComponent;
        try
        {
            ResultSet rs = SQLiteUtil.dbExecuteQuery("SELECT * FROM user WHERE `name`='"+user.getName()+"' AND lastname = '"+user.getLastname()+"';");
            rs.next();
            idUser = rs.getInt("idUser");
            rs = SQLiteUtil.dbExecuteQuery("SELECT * FROM meltbrand WHERE `name`='"+meltBrand.getName()+"';");
            rs.next();
            idMeltBrand = rs.getInt("idMeltBrand");
            java.sql.Date sqlDate = new java.sql.Date(dateCharge.getTime());

            SQLiteUtil.dbExecuteUpdate("INSERT INTO charge (mass, deltaMass, dateCharge, User_idUser, MeltBrand_idMeltBrand)\n" +
                                   "VALUES ('"+mass+"', '"+deltaMass+"', '"+sqlDate+"', '"+idUser+"', '"+idMeltBrand+"');");
            //rs = SQLiteUtil.dbExecuteQuery("SELECT * FROM charge WHERE dateCharge='"+sqlDate+"'");
            //rs.next();
            idCharge = getMaxIndexFromDB();
            //idCharge = rs.getInt("idCharge");
            for(Element aElement: elements)
            {
                if(aElement.getName().equals("C"))
                {
                    rs = SQLiteUtil.dbExecuteQuery("SELECT * FROM element WHERE `name`='C'");
                    rs.next();
                    idElement = rs.getInt("idElement");
                    SQLiteUtil.dbExecuteUpdate("INSERT INTO elementincharge (minProcent, maxProcent, Charge_idCharge, Element_idElement)\n" +
                                           "VALUES ('"+aElement.getMinPercentDouble()+"', '"+aElement.getMaxPercentDouble()+"', '"+idCharge+"', '"+idElement+"');");
                }
                if(aElement.getName().equals("Si"))
                {

                    rs = SQLiteUtil.dbExecuteQuery("SELECT * FROM element WHERE `name`='Si'");
                    rs.next();
                    idElement = rs.getInt("idElement");
                    SQLiteUtil.dbExecuteUpdate("INSERT INTO elementincharge (minProcent, maxProcent, Charge_idCharge, Element_idElement)\n" +
                                           "VALUES ('"+aElement.getMinPercentDouble()+"', '"+aElement.getMaxPercentDouble()+"', '"+idCharge+"', '"+idElement+"');");
                }
                if(aElement.getName().equals("S"))
                {

                    rs = SQLiteUtil.dbExecuteQuery("SELECT * FROM element WHERE `name`='S'");
                    rs.next();
                    idElement = rs.getInt("idElement");
                    SQLiteUtil.dbExecuteUpdate("INSERT INTO elementincharge (minProcent, maxProcent, Charge_idCharge, Element_idElement)\n" +
                                           "VALUES ('"+aElement.getMinPercentDouble()+"', '"+aElement.getMaxPercentDouble()+"', '"+idCharge+"', '"+idElement+"');");
                }
            }
            for(CompInCharge aComp: mandatoryComponents)
            {

                rs = SQLiteUtil.dbExecuteQuery("SELECT * FROM component WHERE `name` = '"+aComp.getName()+"';");
                rs.next();
                idComponent = rs.getInt("idComponent");
                SQLiteUtil.dbExecuteUpdate("INSERT INTO componentincharge (currentMass, minProcent, maxProcent, Charge_idCharge, Component_idComponent)\n" +
                                       "VALUES ('"+aComp.getCurrentMass()+"', '"+aComp.getMinPercent()+"', '"+aComp.getMaxPercent()+"', '"+idCharge+"', '"+idComponent+"');");
            }

            for(CompInCharge aComp: optionalComponents)
            {
                if (aComp.getCurrentMass() != 0)
                {

                    rs = SQLiteUtil.dbExecuteQuery("SELECT * FROM component WHERE `name` = '" + aComp.getName() + "';");
                    rs.next();
                    idComponent = rs.getInt("idComponent");
                    SQLiteUtil.dbExecuteUpdate("INSERT INTO componentincharge (currentMass, minProcent, maxProcent, Charge_idCharge, Component_idComponent)\n" +
                                           "VALUES ('" + aComp.getCurrentMass() + "', '" + aComp.getMinPercent() + "', '" + aComp.getMaxPercent() + "', '" + idCharge + "', '" + idComponent + "');");
                }
            }

        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY);
        }
    }

    public static int getMaxIndexFromDB() throws RuntimeException
    {
        int res = 0;
        try
        {
            ResultSet rs = SQLiteUtil.dbExecuteQuery("SELECT max(idCharge) FROM charge;");
            rs.next();
            res = rs.getInt("max(idCharge)");
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + "SELECT max(idCharge) FROM charge;");
        }

        return res;
    }

    public static ObservableList<Charge> getCharges(String meltBrand)
    {
        ObservableList<Charge> resCharges = FXCollections.observableArrayList();;

        int idMeltBrand;
        String query = "";
        try
        {
            query = "SELECT * FROM meltbrand WHERE `name`='" + meltBrand + "';";
            ResultSet rs = SQLiteUtil.dbExecuteQuery(query);
            rs.next();
            idMeltBrand = rs.getInt("idMeltBrand");

            query = "SELECT * FROM charge WHERE `MeltBrand_idMeltBrand`='" + idMeltBrand + "';";
            rs = SQLiteUtil.dbExecuteQuery(query);
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

                query = "SELECT name, currentMass, mandatory FROM componentincharge CIC JOIN component C ON C.idComponent = CIC.Component_idComponent WHERE `Charge_idCharge`='" + rs.getInt("idCharge") + "';";
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

                resCharges.add(tempCharge);
            }

        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(ErrorMessage.CANNOT_EXECUTE_QUERY + query);
        }

        return resCharges;
    }
}
