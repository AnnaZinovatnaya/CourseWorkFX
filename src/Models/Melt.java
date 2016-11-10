package Models;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created by Анюта on 30.10.2016.
 */
public class Melt {

    private  int idMelt;
    private Date date;
    private int idCharge;
    private int idUser;

    public Melt(int idMelt, Date date, int idCharge, int idUser) {
        this.idMelt = idMelt;
        this.date = date;
        this.idCharge = idCharge;
        this.idUser = idUser;
    }

    public int getIdMelt() {
        return idMelt;
    }

    public void setIdMelt(int idMelt) {
        this.idMelt = idMelt;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIdCharge() {
        return idCharge;
    }

    public void setIdCharge(int idCharge) {
        this.idCharge = idCharge;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
