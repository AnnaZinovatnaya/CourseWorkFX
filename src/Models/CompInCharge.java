package Models;

/**
 * Created by Анюта on 30.10.2016.
 */
public class CompInCharge {

    private int idCompInCharge;
    private double currentMass;
    private double minProcent;
    private double maxProcent;
    private int idCharge;
    private int idComp;

    public CompInCharge(int idCompInCharge, double currentMass, double minProcent, double maxProcent, int idCharge, int idComp) {
        this.idCompInCharge = idCompInCharge;
        this.currentMass = currentMass;
        this.minProcent = minProcent;
        this.maxProcent = maxProcent;
        this.idCharge = idCharge;
        this.idComp = idComp;
    }

    public int getIdCompInCharge() {
        return idCompInCharge;
    }

    public void setIdCompInCharge(int idCompInCharge) {
        this.idCompInCharge = idCompInCharge;
    }

    public double getCurrentMass() {
        return currentMass;
    }

    public void setCurrentMass(double currentMass) {
        this.currentMass = currentMass;
    }

    public double getMinProcent() {
        return minProcent;
    }

    public void setMinProcent(double minProcent) {
        this.minProcent = minProcent;
    }

    public double getMaxProcent() {
        return maxProcent;
    }

    public void setMaxProcent(double maxProcent) {
        this.maxProcent = maxProcent;
    }

    public int getIdCharge() {
        return idCharge;
    }

    public void setIdCharge(int idCharge) {
        this.idCharge = idCharge;
    }

    public int getIdComp() {
        return idComp;
    }

    public void setIdComp(int idComp) {
        this.idComp = idComp;
    }
}
