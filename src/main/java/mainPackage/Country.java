package mainPackage;

public class Country {
    private String name;
    private String capital;
    private String area;
    private String population;
    private String continent;

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String returnInfo() {
        return "Страна: " + getName() + "\n"
                + "Столица: " + getCapital() + "\n"
                + "Площадь: " + getArea() + " км²" + "\n"
                + "Население: " + getPopulation() + " чел." + "\n"
                + "Континент: " + getContinent();
    }

}
