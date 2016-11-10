package Models;
import java.time.LocalDate;
import java.util.Date;

public class Charge {

    private int idCharge;
    private double mass;
    private double deltaMass;
    private double resultMass;
    private Date dateCharge;
    private int idUser;

    public Charge(int idCharge, double mass, double deltaMass, double resultMass, Date dateCharge, int idUser) {
        this.idCharge = idCharge;
        this.mass = mass;
        this.deltaMass = deltaMass;
        this.resultMass = resultMass;
        this.dateCharge = dateCharge;
        this.idUser = idUser;
    }

    public int getIdCharge() {
        return idCharge;
    }

    public void setIdCharge(int idCharge) {
        this.idCharge = idCharge;
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

    public double getResultMass() {
        return resultMass;
    }

    public void setResultMass(double resultMass) {
        this.resultMass = resultMass;
    }

    public Date getDateCharge() {
        return dateCharge;
    }

    public void setDateCharge(Date dateCharge) {
        this.dateCharge = dateCharge;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
