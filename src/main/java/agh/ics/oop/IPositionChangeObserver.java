package agh.ics.oop;

public interface IPositionChangeObserver {

    void positionChanged(Animal animal_to_change, Vector2d oldPosition, Vector2d newPosition);  // jest sens przekazywać nową pozycję, skoro przekazujemy całe zwierzę?

}
