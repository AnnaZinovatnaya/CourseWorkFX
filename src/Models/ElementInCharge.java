package Models;

/**
 * Created by Анюта on 08.11.2016.
 */
public class ElementInCharge {
    private int idElementInCharge;
    private double minProcent;
    private double maxProcent;
    private int idElement;
    private int idCharge;

    public ElementInCharge(int idElementInCharge, double minProcent, double maxProcent, int idElement, int idCharge) {
        this.idElementInCharge = idElementInCharge;
        this.minProcent = minProcent;
        this.maxProcent = maxProcent;
        this.idElement = idElement;
        this.idCharge = idCharge;
    }

    public int getIdCharge() {
        return idCharge;
    }

    public void setIdCharge(int idCharge) {
        this.idCharge = idCharge;
    }

    public int getIdElementInCharge() {
        return idElementInCharge;
    }

    public void setIdElementInCharge(int idElementInCharge) {
        this.idElementInCharge = idElementInCharge;
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

    public int getIdElement() {
        return idElement;
    }

    public void setIdElement(int idElement) {
        this.idElement = idElement;
    }
}
