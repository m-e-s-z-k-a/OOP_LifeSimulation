package agh.ics.oop;

public class Plant {

    private final Vector2d plant_position;
    private int energy;
    private AbstractMap map;

    public Plant(AbstractMap map, Vector2d grass_position, int energy)
    {
        this.plant_position = grass_position;
        this.energy = energy;
        this.map = map;
    }

    public Vector2d getposition()
    {
        return this.plant_position;
    }

    public int getEnergy()
    {
        return this.energy;
    }

    public String toString()
    {
        return "*";
    }

    public boolean isInJungle()
    {
        return this.map.isInJungle(this.plant_position);
    }


}
