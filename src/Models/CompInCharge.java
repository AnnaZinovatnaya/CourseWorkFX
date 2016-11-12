package Models;

public class CompInCharge {

    private Component component;
    private double currentMass;
    private double minPercent;
    private double maxPercent;

    public CompInCharge(Component component, double currentMass, double minPercent, double maxPercent) {
        this.component = component;
        this.currentMass = currentMass;
        this.minPercent = minPercent;
        this.maxPercent = maxPercent;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
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
}
