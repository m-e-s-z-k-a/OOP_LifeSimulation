package agh.ics.oop;

public class Plant {

    private final Vector2d plant_position;
    private int energy; // właściwie też final
    private AbstractMap map;    // nie lepiej sobie zapamiętać tylko, czy jest w dżungli?

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

    public boolean isInJungle() // a czemu trawa powinna to wiedzieć?
    {
        return this.map.isInJungle(this.plant_position);
    }


}
