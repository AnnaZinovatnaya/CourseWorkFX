package Models;

public class CompInCharge {

    private int idCompInCharge;
    private double currentMass;
    private double minPercent;
    private double maxPercent;
    private int idCharge;
    private int idComp;

    public CompInCharge(int idCompInCharge, double currentMass, double minPercent, double maxPercent, int idCharge, int idComp) {
        this.idCompInCharge = idCompInCharge;
        this.currentMass = currentMass;
        this.minPercent = minPercent;
        this.maxPercent = maxPercent;
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

    public double getMinPercent() {
        return minPercent;
    }

    public void setMinPercent(double minPercent) {
        this.minPercent = minPercent;
    }

    public double getMaxPercent() {
        return maxPercent;
    }

    public void setMaxPercent(double maxPercent) {
        this.maxPercent = maxPercent;
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
