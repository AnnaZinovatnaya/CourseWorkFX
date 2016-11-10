package Models;

public class Element {
    private int idElement;
    private String name;

    public Element(int idElement, String name) {
        this.idElement = idElement;
        this.name = name;
    }

    public int getIdElement() {
        return idElement;
    }

    public void setIdElement(int idElement) {
        this.idElement = idElement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
