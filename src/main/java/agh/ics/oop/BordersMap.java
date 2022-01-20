package agh.ics.oop;

public class BordersMap extends AbstractMap implements IWorldMap {


    public BordersMap(int width, int height, double jungleRatio, int plantEnergy, int energyLoss, int startEnergy)
    {
        super(width, height, jungleRatio, plantEnergy, energyLoss, startEnergy);
    }

    public boolean canMoveTo(Vector2d position)
    {
        return position.follows(this.lower_left) && position.precedes(this.upper_right);    // IntelliJ to zrobił

    }



}
