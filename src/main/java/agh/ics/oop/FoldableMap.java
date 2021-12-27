package agh.ics.oop;

public class FoldableMap extends AbstractMap implements IWorldMap {


    public FoldableMap(int width, int height, double jungleRatio, int plantEnergy, int energyLoss, int startEnergy)
    {
        super(width, height, jungleRatio, plantEnergy, energyLoss, startEnergy);
    }

    public boolean canMoveTo(Vector2d position)
    {
        return true;
    }


}
