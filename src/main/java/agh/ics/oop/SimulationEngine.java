package agh.ics.oop;

import javafx.scene.layout.GridPane;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import static java.lang.System.out;

public class SimulationEngine implements IEngine, Runnable
{
    private AbstractMap map;
    private GridPane gridPane;
    private boolean pause = false;
    private Object lock = this;
    private LinkedList<ISimulationUpdate> observers;
    public SimulationEngine(AbstractMap map, int start_animals_number, GridPane gridPane)
    {
        this.map = map;
        this.gridPane = gridPane;
        this.observers = new LinkedList<>();
        do
        {
            int new_animal_direction = new Random().nextInt(8);
            int[] genotype = new int[32];
            int x_coord = new Random().nextInt(this.map.width);
            int y_coord = new Random().nextInt(this.map.height);
            for(int j=0; j<32; j++)
            {
                genotype[j] = new Random().nextInt(8);
            }
            Arrays.sort(genotype);
            Vector2d init_position = new Vector2d(x_coord, y_coord);
            if(!this.map.isOccupied(init_position))
            {
            Animal animal = new Animal(this.map, init_position, genotype, this.map.startEnergy, new_animal_direction);
            this.map.place(animal);}
        }while(this.map.animals.size()<start_animals_number);
    }

    public void mapUpdate()
    {
        for (ISimulationUpdate observer: this.observers)
        {
            observer.mapUpdate(this.map, this.gridPane);
        }
    }

    public void addObserver(ISimulationUpdate new_observer)
    {
        this.observers.add(new_observer);
    }


    public synchronized void pauseThread() throws InterruptedException {
        while(!pause)
        {
            wait();
        }
        pause = false;
        notifyAll();
    }

    public void run()
    {
        do {
            try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.map.day_passing();
            out.println(map);
            this.mapUpdate();
        }while (!(this.map.animals.isEmpty()));
        return;
    }
}