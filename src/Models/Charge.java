package Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Charge {

    private User user;
    private double mass;
    private double deltaMass;
    private Date dateCharge;
    private MeltBrand meltBrand;
    private List<CompInCharge> mandatoryComponents;
    private List<CompInCharge> optionalComponents;
    private List<Element> elements;

    public Charge(User user, double mass, double deltaMass, Date dateCharge, MeltBrand meltBrand, List<CompInCharge> mandatoryComponents, List<CompInCharge> optionalComponents, List<Element> elements) {
        this.user = user;
        this.mass = mass;
        this.deltaMass = deltaMass;
        this.dateCharge = dateCharge;
        this.meltBrand = meltBrand;
        this.mandatoryComponents = mandatoryComponents;
        this.optionalComponents = optionalComponents;
        this.elements = elements;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getDeltaMass() {
        return deltaMass;
    }

    public void setDeltaMass(double deltaMass) {
        this.deltaMass = deltaMass;
    }

    public Date getDateCharge() {
        return dateCharge;
    }

    public void setDateCharge(Date dateCharge) {
        this.dateCharge = dateCharge;
    }

    public MeltBrand getMeltBrand() {
        return meltBrand;
    }

    public void setMeltBrand(MeltBrand meltBrand) {
        this.meltBrand = meltBrand;
    }

    public List<CompInCharge> getMandatoryComponents() {
        return mandatoryComponents;
    }

    public void setMandatoryComponents(List<CompInCharge> mandatoryComponents) {
        this.mandatoryComponents = mandatoryComponents;
    }

    public List<CompInCharge> getOptionalComponents() {
        return optionalComponents;
    }

    public void setOptionalComponents(List<CompInCharge> optionalComponents) {
        this.optionalComponents = optionalComponents;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public void setChargeBrand(String brand){
        meltBrand = MeltBrand.getMeltBrand(brand);
        elements = new ArrayList<>();
        for(Element aElement: meltBrand.getElements()){
            elements.add(aElement);
        }
    }

    public  boolean canEditPercent(String element, double percent){
        for(Element aElement: elements){
            if(aElement.getName().equals(element)) {
                if (percent >=  aElement.getMinPercentDouble() && percent <= aElement.getMaxPercentDouble())
                    return true;
                else
                    return false;
            }
        }

        return false;
    }



}
