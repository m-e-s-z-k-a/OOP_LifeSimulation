package agh.ics.oop;

import agh.ics.oop.gui.DataChart;
import agh.ics.oop.gui.FileData;
import agh.ics.oop.gui.GenotypeText;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

import static java.lang.System.out;

public class SimulationEngine implements IEngine, Runnable
{
    private AbstractMap map;
    private int daysPassed;
    private GridPane gridPane;
    private boolean running = true;
    private boolean pause = false;
    private Object lock = this;
    private DataChart dataChart;
    private boolean isMagicGameplayOn;
    private GenotypeText text;
    private FileData fileData;
    private int animals_to_start_with;
    private LinkedList<ISimulationUpdate> observers;
    private int timesMagicUsed = 0;
    private LinkedHashMap<Vector2d, ArrayList<Animal>> animals;
    public SimulationEngine(AbstractMap map, int start_animals_number, boolean isMagic, GridPane gridPane, DataChart dataChart, GenotypeText text, FileData fileData)
    {
        this.map = map;
        this.isMagicGameplayOn = isMagic;
        this.text = text;
        this.animals = this.map.animals;
        this.fileData = fileData;
        this.animals_to_start_with = start_animals_number;
        this.running = true;
        this.pause = false;
        this.dataChart = dataChart;
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
            observer.mapUpdate(this.map, this.gridPane, this, this.dataChart, text, fileData);
        }
    }

    public void addObserver(ISimulationUpdate new_observer)
    {
        this.observers.add(new_observer);
    }

    public ArrayList<int[]> magic_get_genotypes()
    {
        ArrayList<int[]> copied_genes = new ArrayList<>();
        for(Vector2d animal_positions: this.animals.keySet())
        {
            for(Animal animals_to_copy_genes: this.animals.get(animal_positions))
            {
                copied_genes.add(animals_to_copy_genes.getGenotype());
            }
        }
        return copied_genes;
    }

    public void pause()
    {
        pause = true;
    }

    public void resume()
    {
        synchronized(lock)
        {
            this.pause = false;
            this.lock.notifyAll();
        }
    }

    public void pauseandrun()
    {
        if(pause)
        {
            this.resume();
        }
        else {
            this.pause();
        }
    }
    public void run()
    {
        do {
            synchronized(lock)
            {
                if (!running)
                {
                    break;
                }
                if (pause)
                { try{
                    synchronized(lock){
                        lock.wait();
                    }}catch (InterruptedException ex)
                {
                    break;
                }
                    if (!running)
                    {
                        break;
                    }
                }
            }
            try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.map.day_passing();
            daysPassed += 1;
            this.mapUpdate();
            if(this.map.animals_number == 5 && this.isMagicGameplayOn && timesMagicUsed < 3)
            {
                int animals_placed = 0;
                timesMagicUsed += 1;
                out.println("wow");
                Platform.runLater(() -> {
                    Text popup_text = new Text(this.map.getMapType() + " Magic! " + (3-timesMagicUsed) + " time(s) left");
                    VBox vbox = new VBox(popup_text);
                    vbox.setAlignment(Pos.CENTER);
                    Scene newScene = new Scene(vbox, 300, 300);
                    Stage stage = new Stage();
                    stage.setScene(newScene);
                    stage.show();
                });
                do {
                    int x_coord = new Random().nextInt(this.map.width);
                    int y_coord = new Random().nextInt(this.map.height);
                    Vector2d init_position = new Vector2d(x_coord, y_coord);
                    if (!this.map.isOccupied(init_position)) {
                        int new_animal_direction = new Random().nextInt(8);
                        int[] genotype = this.magic_get_genotypes().get(animals_placed);
                        Animal animal = new Animal(this.map, init_position, genotype, this.map.startEnergy, new_animal_direction);
                        this.map.place(animal);
                        animals_placed += 1;
                    }
                }while (animals_placed < 5);
            }
        }while (!(this.map.animals.isEmpty()));
        return;
    }
}
