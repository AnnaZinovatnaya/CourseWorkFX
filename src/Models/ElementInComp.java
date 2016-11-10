package Models;

import util.DBUtil;


public class ElementInComp {
    private int idElementInComp;
    private double procent;
    private double adopt;
    private int idElement;
    private int idComp;

    public ElementInComp(int idElementInComp, double procent, double adopt, int idElement, int idComp) {
        this.idElementInComp = idElementInComp;
        this.procent = procent;
        this.adopt = adopt;
        this.idElement = idElement;
        this.idComp = idComp;
    }

    public int getIdElementInComp() {
        return idElementInComp;
    }

    public void setIdElementInComp(int idElementInComp) {
        this.idElementInComp = idElementInComp;
    }

    public double getProcent() {
        return procent;
    }

    public void setProcent(double procent) {
        this.procent = procent;
    }

    public double getAdopt() {
        return adopt;
    }

    public void setAdopt(double adopt) {
        this.adopt = adopt;
    }

    public int getIdElement() {
        return idElement;
    }

    public void setIdElement(int idElement) {
        this.idElement = idElement;
    }

    public int getIdComp() {
        return idComp;
    }

    public void setIdComp(int idComp) {
        this.idComp = idComp;
    }

    public boolean addElementInComp() {

        try {
            DBUtil.dbExecuteUpdate("INSERT INTO mydb.elementincomponent (procent, Element_idElement, Component_idComponent, adopt) " +
                    "VALUES ('" + procent + "', '" + idElement + "', '" + idComp + "', '" + adopt + "')");
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }
}
