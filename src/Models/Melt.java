package Models;

import java.util.Date;


public class Melt {

    private  User user;
    private Charge charge;
    private Date date;

    public Melt(User user, Charge charge, Date date) {
        this.user = user;
        this.charge = charge;
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
