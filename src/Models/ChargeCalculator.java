package Models;

import java.util.ArrayList;
import java.util.List;

public class ChargeCalculator {

    private final Charge charge;

    public ChargeCalculator (Charge charge)
    {
        this.charge = charge;
    }

    public boolean isChargePossible()
    {
        double temp;

        double accumulatedShares = 0;

        double massChTemp;
        double massChRes;

        double[] currentElementMasses;
        double[] minAllowedElementMasses = initializeMinAllowedElementMasses();
        double[] maxAllowedElementMasses = initializeMaxAllowedElementMasses();

        this.charge.setMandatoryComponents(sortMandatoryComponents());

        for (CompInCharge mandatoryComponent : this.charge.getMandatoryComponents())
        {
            mandatoryComponent.setCurrentPercent(mandatoryComponent.getMinPercent());
            accumulatedShares += mandatoryComponent.getCurrentPercent() / 100;
        }

        massChRes = calculateCurrentMassChargeOfComponents(this.charge.getMandatoryComponents());
        massChTemp = massChRes * accumulatedShares;

        for (int i = 0; i < this.charge.getMandatoryComponents().size(); ++i)
        {
            this.charge.getMandatoryComponents().get(i).setCurrentMass(massChTemp * this.charge.getMandatoryComponents().get(i).getCurrentPercent() / 100);
        }

        currentElementMasses = calculateElementMassesInCurrentMelt();
        //ошибка: взяли всех обяхательных компонентов по минимуму и получилось элементов юольше, чем максимальная граница:

        for (int i = 0; i < this.charge.getElements().size(); ++i)
        {
            if (currentElementMasses[i] > (this.charge.getElements().get(i).getMaxPercentDouble() * this.charge.getMass() / 100))
            {
                return false;
            }
        }

        int p = 0;
        while (true)
        {
            accumulatedShares = 0;
            this.charge.getMandatoryComponents().get(p).setCurrentPercent(this.charge.getMandatoryComponents().get(p).getMaxPercent());
            for (CompInCharge mandatoryComponent : this.charge.getMandatoryComponents())
            {
                accumulatedShares += mandatoryComponent.getCurrentPercent() / 100;
            }

            if (accumulatedShares > 1)
            {
                temp = accumulatedShares - 1;
                this.charge.getMandatoryComponents().get(p).setCurrentPercent(this.charge.getMandatoryComponents().get(p).getCurrentPercent() - temp * 100);
                accumulatedShares = 1;
            }

            massChRes = calculateCurrentMassChargeOfComponents(this.charge.getMandatoryComponents());
            massChTemp = massChRes * accumulatedShares;

            for (int i = 0; i < this.charge.getMandatoryComponents().size(); ++i)
            {
                this.charge.getMandatoryComponents().get(i).setCurrentMass(massChTemp * this.charge.getMandatoryComponents().get(i).getCurrentPercent() / 100);
            }

            for (int i = 0; i < this.charge.getElements().size(); ++i)
            {
                currentElementMasses = calculateElementMassesInCurrentMelt();

                if (currentElementMasses[i] > maxAllowedElementMasses[i])
                {
                    temp = currentElementMasses[i] - minAllowedElementMasses[i];//масса, которую надо вычесть
                    temp /= massChTemp;//процент, который нужно вычесть
                    this.charge.getMandatoryComponents().get(p).setCurrentPercent(this.charge.getMandatoryComponents().get(p).getCurrentPercent() - temp * 100);

                    massChRes = calculateCurrentMassChargeOfComponents(this.charge.getMandatoryComponents());
                    massChTemp = massChRes * accumulatedShares;

                    for (i = 0; i < this.charge.getMandatoryComponents().size(); ++i)
                    {
                        this.charge.getMandatoryComponents().get(i).setCurrentMass(massChTemp * this.charge.getMandatoryComponents().get(i).getCurrentPercent() / 100);
                    }
                }
            }
            accumulatedShares = 0;
            for (CompInCharge mandatoryComponent : this.charge.getMandatoryComponents())
            {
                accumulatedShares += mandatoryComponent.getCurrentPercent() / 100;
            }
            if (1 == accumulatedShares)
            {
                break;
            }
            else
            {
                ++p;
                if (p >= this.charge.getMandatoryComponents().size())
                {
                    return false;
                }
            }
        }

        for (int i = 0; i < this.charge.getMandatoryComponents().size(); ++i)
        {
            this.charge.getMandatoryComponents().get(i).setCurrentMass((double)Math.round(massChTemp * this.charge.getMandatoryComponents().get(i).getCurrentPercent()/100*10)/10);
        }

        return true;
    }

    private double[] initializeMinAllowedElementMasses()
    {
        double minAllowedElementMasses[] = new double[this.charge.getElements().size()];
        for (int i = 0; i < this.charge.getElements().size(); ++i)
        {
            minAllowedElementMasses[i] = this.charge.getElements().get(i).getMinPercentDouble() * this.charge.getMass() / 100;
        }

        return minAllowedElementMasses;
    }

    private double[] initializeMaxAllowedElementMasses()
    {
        double[] maxAllowedElementMasses = new double[this.charge.getElements().size()];
        for (int i = 0; i < this.charge.getElements().size(); ++i)
        {
            maxAllowedElementMasses[i] = this.charge.getElements().get(i).getMaxPercentDouble() * this.charge.getMass() / 100;
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
        double coefficients[][] = new double[this.charge.getMandatoryComponents().size()][this.charge.getMandatoryComponents().get(0).getComponent().getElements().size()];
        for (int componentIndex = 0; componentIndex < this.charge.getMandatoryComponents().size(); ++componentIndex)
        {
            CompInCharge currentComponent = this.charge.getMandatoryComponents().get(componentIndex);
            for (int elementInCompIndex = 0; elementInCompIndex < currentComponent.getComponent().getElements().size(); ++elementInCompIndex)
            {
                Element currentElementInComponent = currentComponent.getComponent().getElements().get(elementInCompIndex);
                for (Element currentElementInCharge : this.charge.getElements()) {
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
        double maxCoefficients[] = new double[this.charge.getMandatoryComponents().size()];

        for (int componentIndex = 0; componentIndex < this.charge.getMandatoryComponents().size(); ++componentIndex)
        {
            double maxCoefficient = coefficients[componentIndex][0];
            for (int elementIndex = 0; elementIndex < this.charge.getMandatoryComponents().get(0).getComponent().getElements().size(); ++elementIndex)
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

        while (sortedList.size() < this.charge.getMandatoryComponents().size())
        {
            int min = 0;
            for (int i = 0; i < this.charge.getMandatoryComponents().size(); ++i)
            {
                if (maxCoefficients[i] < maxCoefficients[min])
                {
                    min = i;
                }
            }
            sortedList.add(this.charge.getMandatoryComponents().get(min));
            maxCoefficients[min] = 100;
        }
        return sortedList;
    }

    public double[] calculateElementMassesInCurrentMelt()
    {
        double[] elementInMeltMasses = new double[this.charge.getElements().size()];
        for (int i = 0; i < this.charge.getElements().size(); ++i)
        {
            elementInMeltMasses[i] = 0;
        }

        for (CompInCharge mandatoryComponent : this.charge.getMandatoryComponents())
        {
            for (int j = 0; j < this.charge.getElements().size(); ++j)
            {
                elementInMeltMasses[j] += mandatoryComponent.getCurrentMass() * mandatoryComponent.getComponent().getElements().get(j).getPercent() / 100 * mandatoryComponent.getComponent().getElements().get(j).getAdopt() / 100;
            }
        }

        for (CompInCharge optionalComponent: this.charge.getOptionalComponents())
        {
            if (optionalComponent.getCurrentMass() > 0)
            {
                for (int j = 0; j < this.charge.getElements().size(); ++j)
                {
                    elementInMeltMasses[j] += optionalComponent.getCurrentMass() * optionalComponent.getComponent().getElements().get(j).getPercent() / 100 * optionalComponent.getComponent().getElements().get(j).getAdopt() / 100;
                }
            }
        }
        return elementInMeltMasses;
    }

    public double[] calculateElementMassesInGivenComponents(ArrayList<CompInCharge> components)
    {
        double[] elementMasses = new double[this.charge.getElements().size()];
        for (int i = 0; i < this.charge.getElements().size(); ++i)
        {
            elementMasses[i] = 0;
        }

        for (CompInCharge component : components)
        {
            for (int j = 0; j < this.charge.getElements().size(); ++j)
            {
                elementMasses[j] += component.getCurrentMass() * component.getComponent().getElements().get(j).getPercent() / 100 * component.getComponent().getElements().get(j).getAdopt() / 100;
            }
        }

        for (CompInCharge optionalComponent: this.charge.getOptionalComponents())
        {
            if (optionalComponent.getCurrentMass() > 0)
            {
                for (int j = 0; j < this.charge.getElements().size(); ++j)
                {
                    elementMasses[j] += optionalComponent.getCurrentMass() * optionalComponent.getComponent().getElements().get(j).getPercent() / 100 * optionalComponent.getComponent().getElements().get(j).getAdopt() / 100;
                }
            }
        }

        return elementMasses;
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

        return this.charge.getMass() * sumOfPercents / sumOfAdoptedPercents;
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

        this.charge.setMandatoryComponents(sortMandatoryComponents());

        ArrayList<CompInCharge> mandatoryCompsTemp = sortMandatoryComponentsByPrice();

        while(!areListsEqual((ArrayList) this.charge.getMandatoryComponents(), mandatoryCompsTemp))
        {
            for (CompInCharge mandatoryComponentTemp : mandatoryCompsTemp)
            {
                mandatoryComponentTemp.setCurrentPercent(mandatoryComponentTemp.getMinPercent());
                curPercSum += mandatoryComponentTemp.getCurrentPercent() / 100;
            }

            massChRes = calculateCurrentMassChargeOfComponents(mandatoryCompsTemp);
            massChTemp = massChRes * curPercSum;

            for (CompInCharge mandatoryComponentTemp : mandatoryCompsTemp) {
                mandatoryComponentTemp.setCurrentMass(massChTemp * mandatoryComponentTemp.getCurrentPercent() / 100);
            }

            int p = 0;
            while (true)
            {
                curPercSum = 0;
                mandatoryCompsTemp.get(p).setCurrentPercent(mandatoryCompsTemp.get(p).getMaxPercent());
                for (CompInCharge mandatoryComponentTemp : mandatoryCompsTemp)
                {
                    curPercSum += mandatoryComponentTemp.getCurrentPercent() / 100;
                }

                if (curPercSum > 1)
                {
                    temp = curPercSum - 1;
                    mandatoryCompsTemp.get(p).setCurrentPercent(mandatoryCompsTemp.get(p).getCurrentPercent() - temp * 100);
                    curPercSum = 1;
                }

                massChRes = calculateCurrentMassChargeOfComponents(mandatoryCompsTemp);
                massChTemp = massChRes * curPercSum;

                for (CompInCharge mandatoryComponentTemp : mandatoryCompsTemp) {
                    mandatoryComponentTemp.setCurrentMass(massChTemp * mandatoryComponentTemp.getCurrentPercent() / 100);
                }

                for (int i = 0; i < this.charge.getElements().size(); ++i)
                {
                    currentElementMasses = calculateElementMassesInGivenComponents(mandatoryCompsTemp);

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
                for (CompInCharge mandatoryComponentTemp : mandatoryCompsTemp)
                {
                    curPercSum += mandatoryComponentTemp.getCurrentPercent() / 100;
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

        for (CompInCharge mandatoryComponentTemp : mandatoryCompsTemp) {
            mandatoryComponentTemp.setCurrentMass((double) Math.round(massChTemp * mandatoryComponentTemp.getCurrentPercent() / 100 * 10) / 10);
        }
        this.charge.setMandatoryComponents(mandatoryCompsTemp);

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
        double mass = this.charge.getMass();

        double[] minElementMasses = new double[this.charge.getElements().size()];
        for (int i = 0; i < this.charge.getElements().size(); ++i)
        {
            minElementMasses[i] = this.charge.getElements().get(i).getMinPercentDouble() * mass / 100;
        }
        double currentElementMasses[] = calculateElementMassesInCurrentMelt();

        double[] delta = new double[this.charge.getElements().size()];
        for (int i = 0; i < this.charge.getElements().size(); ++i)
        {
            delta[i] = currentElementMasses[i] - minElementMasses[i];
            if (delta[i] < 0) // if delta < 0, mass of element is less than min allowed mass
            {
                this.charge.setOptionalComponents(getOptionalComponentsSortedByElement(this.charge.getElements().get(i)));

                double adoptOfCurrentElement = this.charge.getOptionalComponents().get(0).getComponent().getElements().get(i).getAdopt();
                double percentOfCurrentElement = this.charge.getOptionalComponents().get(0).getComponent().getElements().get(i).getPercent();
                double minPercentOfCurrentElement = this.charge.getElements().get(i).getMinPercentDouble();

                long   newComponentMassMultipliedBy100 = Math.round(((minPercentOfCurrentElement / 100 * mass - currentElementMasses[i]) / (adoptOfCurrentElement / 100 * (percentOfCurrentElement / 100 - minPercentOfCurrentElement / 100)) * 100));
                double newComponentMass = (double) newComponentMassMultipliedBy100 / 100;
                this.charge.getOptionalComponents().get(0).setCurrentMass(newComponentMass);
            }
        }
    }

    // what is this method for?
    public ArrayList<CompInCharge> change(ArrayList<CompInCharge> mandatoryCompsTemp)
    {
        for (int i = 0; i < this.charge.getMandatoryComponents().size(); ++i)
        {
            if (mandatoryCompsTemp.get(i).getName().compareTo(this.charge.getMandatoryComponents().get(i).getName()) != 0)
            {
                for (int j = i; j < mandatoryCompsTemp.size(); ++j)
                {
                    if (mandatoryCompsTemp.get(j).getName().compareTo(this.charge.getMandatoryComponents().get(i).getName()) == 0)
                    {
                        while (j != i)
                        {
                            CompInCharge temp = new CompInCharge(null, 0, 0, 0);
                            temp.setCompInCharge(mandatoryCompsTemp.get(j));
                            mandatoryCompsTemp.get(j).setCompInCharge(mandatoryCompsTemp.get(j - 1));
                            mandatoryCompsTemp.get(j - 1).setCompInCharge(temp);
                            --j;
                        }
                        return mandatoryCompsTemp;
                    }
                }
            }
        }
        return mandatoryCompsTemp;
    }

    private ArrayList<CompInCharge> getOptionalComponentsSortedByElement(Element element)
    {
        ArrayList<CompInCharge> sortedList = new ArrayList<>();
        while (sortedList.size() < this.charge.getOptionalComponents().size())
        {
            int index = findIndexOfComponentWithMaxElementPercent(element);
            sortedList.add(this.charge.getOptionalComponents().get(index));
            this.charge.getOptionalComponents().remove(index);
        }
        return sortedList;
    }

    private int findIndexOfComponentWithMaxElementPercent(Element element)
    {
        int maxIndex = 0;

        for (int i = 0; i < this.charge.getOptionalComponents().size(); ++i)
        {
            for (int j = 0; j < this.charge.getOptionalComponents().get(i).getComponent().getElements().size(); ++j)
            {
                Element elementInCurrentComponent = this.charge.getOptionalComponents().get(i).getComponent().getElements().get(j);
                Element elementInCurrentMaxComponent = this.charge.getOptionalComponents().get(maxIndex).getComponent().getElements().get(j);

                if (elementInCurrentComponent.getName().equals(element.getName()) &&
                        (elementInCurrentComponent.getPercent() > elementInCurrentMaxComponent.getPercent()))
                {
                    maxIndex = i;
                }
            }
        }

        return maxIndex;
    }

    public ArrayList<CompInCharge> sortMandatoryComponentsByPrice()
    {
        ArrayList<CompInCharge> sortedList = new ArrayList<>();
        double prices[] = new double[this.charge.getMandatoryComponents().size()];

        for (int i = 0; i < this.charge.getMandatoryComponents().size(); ++i)
        {
            prices[i] = this.charge.getMandatoryComponents().get(i).getComponent().getPriceDouble();
        }

        while (sortedList.size() < this.charge.getMandatoryComponents().size())
        {
            int indexMinPrice = 0;
            for (int i = 0; i < this.charge.getMandatoryComponents().size(); ++i)
            {
                if (prices[i] < prices[indexMinPrice])
                {
                    indexMinPrice = i;
                }
            }
            sortedList.add(this.charge.getMandatoryComponents().get(indexMinPrice));
            prices[indexMinPrice] = 10000000;
        }
        return sortedList;
    }
}
