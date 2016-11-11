package Models;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Charge {

    private User user;
    private double mass;
    private double deltaMass;
    private Date dateCharge;
    private MeltBrand meltBrand;
    private List<CompInCharge> components;
    private List<Element> elements;

    public Charge(User user, double mass, double deltaMass, Date dateCharge, MeltBrand meltBrand, List<CompInCharge> components, List<Element> elements) {
        this.user = user;
        this.mass = mass;
        this.deltaMass = deltaMass;
        this.dateCharge = dateCharge;
        this.meltBrand = meltBrand;
        this.components = components;
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

    public List<CompInCharge> getComponents() {
        return components;
    }

    public void setComponents(List<CompInCharge> components) {
        this.components = components;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

}
