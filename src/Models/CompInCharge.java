package Models;

public class CompInCharge {
    private String name; //only for view
    private Component component;
    private double currentMass;
    private double minPercent;
    private double maxPercent;
    private double currentPercent;
    public CompInCharge(Component component, double currentMass, double minPercent, double maxPercent) {
        this.component = component;
        if(component != null) {
            this.name = component.getName();
        }
        this.currentMass = currentMass;
        this.minPercent = minPercent;
        this.maxPercent = maxPercent;
        this.currentPercent = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getCurrentPercent() {
        return currentPercent;
    }

    public void setCurrentPercent(double currentPercent) {
        this.currentPercent = currentPercent;
    }
}
