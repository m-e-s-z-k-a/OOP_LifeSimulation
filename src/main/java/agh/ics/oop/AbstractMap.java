package agh.ics.oop;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;


public abstract class AbstractMap implements IWorldMap, IPositionChangeObserver {

    protected int width;
    protected int height;
    protected double jungleRatio;
    protected Vector2d upper_right;
    protected Vector2d lower_left;
    protected Vector2d jungle_upperright;
    protected Vector2d jungle_lowerleft;
    protected int plantEnergy;
    protected MapVisualizer visualizer;
    protected int energyLoss;
    protected int animals_number;
    protected int plants_number;
    protected float sumLifeLengthDead;
    protected int dead_number;
    protected int daily_children_number;
    protected float averageChildrenNumber;
    protected float averageEnergy;
    protected int startEnergy;
    protected float averageLifeLengthDead;
    protected int sum_daily_energy;
    protected int daysPassed;
    protected LinkedHashMap<Vector2d, ArrayList<Animal>> animals = new LinkedHashMap<>();
    protected LinkedHashMap<Vector2d, Plant> plants = new LinkedHashMap<>();

    public AbstractMap(int width, int height, double jungleRatio, int plantEnergy, int energyLoss, int startEnergy)
    {
        this.energyLoss = energyLoss;
        this.jungleRatio = jungleRatio;
        this.width = width;
        this.height = height;
        this.plantEnergy = plantEnergy;
        this.startEnergy = startEnergy;
        this.daysPassed = 0;
        this.plants_number = 0;
        this.sum_daily_energy = 0;
        this.dead_number = 0;
        this.sumLifeLengthDead = 0;
        this.averageLifeLengthDead = 0;
        this.animals_number = 0;
        this.daily_children_number = 0;
        this.averageChildrenNumber = 0;
        this.averageEnergy = 0;
        this.visualizer = new MapVisualizer(this);
        this.setCoordinates();
    }

    public void setCoordinates()
    {
        this.lower_left = new Vector2d(0, 0);
        this.upper_right = new Vector2d(width-1, height-1);
        this.jungle_lowerleft = new Vector2d((int)((width/2) - (width*jungleRatio)/2),(int)((height/2)-((height)*jungleRatio)/2));
        this.jungle_upperright = new Vector2d((int)((width/2)+(width*jungleRatio)/2), (int)((height/2)+(height*jungleRatio)/2));
    }

    public Vector2d get_upper_right()
    {
        setCoordinates();
        return this.upper_right;
    }

    public Vector2d get_lower_left()
    {
        setCoordinates();
        return this.lower_left;
    }

    public boolean isOccupied(Vector2d position)
    {
        return this.objectAt(position) != null;
    }

    public boolean place(Animal animal)
    {
        if (this.canMoveTo(animal.getPosition()))
        {
            if (this.animals.containsKey(animal.getPosition()))
            {
                ArrayList<Animal> animals_here = this.animals.get(animal.getPosition());
                animals_here.add(animal);
            }
            else
            {
                ArrayList<Animal> animals_here = new ArrayList<>();
                animals_here.add(animal);
                this.animals.put(animal.getPosition(), animals_here);
            }
            animal.addObserver(this);
            return true;
        }
        return false;
    }


    public Object objectAt(Vector2d position)
    {
        if(this.animals.containsKey(position))
        {
            return this.animals.get(position);
        }
        if(this.plants.containsKey(position))
        {
            return this.plants.get(position);
        }
        return null;
    }

    public boolean isInJungle(Vector2d position)
    {
        return (this.jungle_lowerleft.precedes(position) && this.jungle_upperright.follows(position));
    }

    public void seedPlants()
    {
        ArrayList<Vector2d> jungle_free_spots = new ArrayList<>();
        ArrayList<Vector2d> plain_free_spots = new ArrayList<>();
        for(int i = this.lower_left.x; i <= this.upper_right.x; i++)
        {
            for(int j = this.lower_left.y; j<= this.upper_right.y; j++)
            {
                Vector2d location = new Vector2d(i, j);
                if (objectAt(location) == null)
                {
                    if (isInJungle(location))
                    {
                        jungle_free_spots.add(location);

                    }
                    else
                    {
                        plain_free_spots.add(location);

                    }
                }
            }
        }
        if (plain_free_spots.size() > 0)
        {
            Vector2d new_plain_plant;
            do
            {
                new_plain_plant = plain_free_spots.get((new Random()).nextInt(plain_free_spots.size()));
            }while (this.objectAt(new_plain_plant) != null);
            this.plants.put(new_plain_plant, new Plant(this, new_plain_plant, this.plantEnergy));
            this.plants_number += 1;
        }
        if (jungle_free_spots.size() > 0)
        {
            Vector2d new_jungle_plant;
            do
            {
                new_jungle_plant = jungle_free_spots.get((new Random()).nextInt(jungle_free_spots.size()));
            }while (this.objectAt(new_jungle_plant) != null);
            this.plants.put(new_jungle_plant, new Plant(this, new_jungle_plant, this.plantEnergy));
            this.plants_number += 1;
        }
    }

    public boolean plantsHere(Vector2d position)
    {
        return (objectAt(position) instanceof Plant);
    }

    public void eatingPlants()
    {
        LinkedHashMap<Vector2d, Plant> plants_copy = new LinkedHashMap<>(this.plants);
        for (Vector2d plant_positions: plants_copy.keySet())
        {
            if (this.animals.containsKey(plant_positions))
            {
                if (this.animals.get(plant_positions).size() > 1)
                {
                    ArrayList<Animal> animals_to_check = this.animals.get(plant_positions);
                    ArrayList<Animal> animals_that_eat = getMaxEnergyAnimals(animals_to_check);
                    int energyIncrease = this.plantEnergy/animals_that_eat.size();
                    for (Animal animal : animals_that_eat) {
                        animal.addEnergy(energyIncrease);
                    }
                }
                else
                {
                    this.animals.get(plant_positions).get(0).addEnergy(this.plantEnergy);
                }
                this.plants.remove(plant_positions);
                this.plants_number -= 1;
            }
        }
    }


    public ArrayList<Animal> getMaxEnergyAnimals(ArrayList<Animal> animals_to_check)
    {
        int max_energy = 0;
        ArrayList<Animal> animals_that_eat = new ArrayList<>();
        for (Animal animal : animals_to_check) {
            int energy_check = animal.getEnergy();
            if (energy_check > max_energy) {
                max_energy = energy_check;
                animals_that_eat.clear();
                animals_that_eat.add(animal);
            } else if (energy_check == max_energy) {
                animals_that_eat.add(animal);
            }

        }
        return animals_that_eat;
    }

    public void removeDeadAnimals()
    {
        LinkedHashMap<Vector2d, ArrayList<Animal>> animals_copy = new LinkedHashMap<>(this.animals);
        for (Vector2d animal_positions: animals_copy.keySet())
        {
            ArrayList<Animal> animals_to_check = animals_copy.get(animal_positions);
            if (animals_to_check.size() == 1)
            {
                if (animals_to_check.get(0).getEnergy() < this.energyLoss)
                {
                    this.animals.remove(animal_positions);
                    this.sumLifeLengthDead += animals_to_check.get(0).getLifeLength();
                    this.dead_number += 1;
                }
                else
                {
                    animals_to_check.get(0).anotherDaySurvived();
                    this.daily_children_number += animals_to_check.get(0).getChildrenNumber();
            }}
            else if (animals_to_check.size() > 1)
            {
                for(int i = 0; i<animals_to_check.size(); i++)
                {
                    if (animals_to_check.get(i).getEnergy() < this.energyLoss)
                    {
                        this.sumLifeLengthDead += animals_to_check.get(i).getLifeLength();
                        this.dead_number += 1;
                        this.animals.get(animal_positions).remove(i);
                    }
                    else {
                        animals_to_check.get(i).anotherDaySurvived();
                        this.daily_children_number += animals_to_check.get(i).getChildrenNumber();
                    }
                }
            }

        }
    }


    public ArrayList<Animal> find_the_pair(ArrayList<Animal> potential_animals)
    {
        ArrayList<Animal> the_couple = new ArrayList<>();
        if (getMaxEnergyAnimals(potential_animals).size() > 2)
        {
            Animal first_animal = potential_animals.get((new Random()).nextInt(potential_animals.size()));
            Animal second_animal;
            do {
                second_animal = potential_animals.get((new Random()).nextInt(potential_animals.size()));
            }while(second_animal == first_animal);
            the_couple.add(first_animal);
            the_couple.add(second_animal);
        }
        else if(getMaxEnergyAnimals(potential_animals).size() == 2)
        {
            the_couple = getMaxEnergyAnimals(potential_animals);
        }
        else
        {
            ArrayList<Animal>potential_animals_copy = new ArrayList<>(potential_animals);
            Animal first_animal = getMaxEnergyAnimals(potential_animals).get(0);
            int animal_index_to_remove = 0;
            for(int i = 0; i < potential_animals_copy.size(); i++)
            {
                if (potential_animals_copy.get(i).equals(first_animal))
                {
                    animal_index_to_remove = i;
                }
            }
            potential_animals_copy.remove(animal_index_to_remove);
            ArrayList<Animal> potential_2nd_animal = getMaxEnergyAnimals(potential_animals_copy);
            if (potential_2nd_animal.size() > 1)
            {
                Animal second_animal = potential_2nd_animal.get(new Random().nextInt(potential_2nd_animal.size()));
                the_couple.add(first_animal);
                the_couple.add(second_animal);
            }
            else if (potential_2nd_animal.size() == 1)
            {
                Animal second_animal = potential_2nd_animal.get(0);
                the_couple.add(first_animal);
                the_couple.add(second_animal);
            }
        }
        return the_couple;
    }

    public void copulate()
    {
        for(Vector2d animal_positions: this.animals.keySet())
        {
            if(this.animals.get(animal_positions).size()>1)
            {
                ArrayList<Animal> the_couple = find_the_pair(this.animals.get(animal_positions));
                if ((double)the_couple.get(0).getEnergy()>= (double)this.startEnergy/2 && (double)the_couple.get(1).getEnergy()>= (double)this.startEnergy/2)
                {
                    int[] new_animal_genotype = distribute_the_genes(the_couple);
                    int child_energy = 0;
                    for(Animal parent: the_couple)
                    {
                        child_energy += ((0.25)*parent.getEnergy());
                        parent.subtractEnergy((int)((0.25)*parent.getEnergy()));
                        parent.addAChild();
                    }
                    int child_direction = new Random().nextInt(8);
                    place(new Animal(this, animal_positions, new_animal_genotype, child_energy, child_direction));
                    this.animals_number += 1;
                    this.daily_children_number += 1;
                }
            }
        }
    }

    public int[] distribute_the_genes(ArrayList<Animal> couple)
    {
        int the_side = new Random().nextInt(2);
        int[] child_genes = new int[32];
        int energyPool = couple.get(0).getEnergy() + couple.get(1).getEnergy();
        int dominating_genes = (int)Math.ceil(32*((double)couple.get(0).getEnergy()/(double)energyPool));
        int rest_of_genes = 32 - dominating_genes;
        if (couple.get(0).getEnergy()>couple.get(1).getEnergy())
        {
            if (the_side == 0)
            {
                for(int i = 0; i<dominating_genes; i++)
                {
                    child_genes[i] = couple.get(0).getGenotype()[i];
                }
                for(int i = dominating_genes; i<32; i++)
                {
                    child_genes[i] = couple.get(1).getGenotype()[i];
                }
            }
            if (the_side == 1)
            {
                for(int i = 0; i<rest_of_genes; i++)
                {
                    child_genes[i] = couple.get(1).getGenotype()[i];
                }
                for(int i = rest_of_genes; i<32; i++)
                {
                    child_genes[i] = couple.get(0).getGenotype()[i];
                }
            }

        }
        else if (couple.get(0).getEnergy()<couple.get(1).getEnergy())
        {
            if (the_side == 0)
            {
                for(int i = 0; i<dominating_genes; i++)
                {
                    child_genes[i] = couple.get(1).getGenotype()[i];
                }
                for(int i = dominating_genes; i<32; i++)
                {
                    child_genes[i] = couple.get(0).getGenotype()[i];
                }
            }
            if (the_side == 1)
            {
                for(int i = 0; i<rest_of_genes; i++)
                {
                    child_genes[i] = couple.get(0).getGenotype()[i];
                }
                for(int i = rest_of_genes; i<32; i++)
                {
                    child_genes[i] = couple.get(1).getGenotype()[i];
                }
            }
        }
        else
        {
            int whose_first_part = new Random().nextInt(2);
            for(int i = 0; i<16; i++)
                {
                    child_genes[i] = couple.get(whose_first_part).getGenotype()[i];
                }
            for(int i = 16; i<32; i++)
                {
                    child_genes[i] = couple.get(Math.abs(whose_first_part-1)).getGenotype()[i];
                }
            }
        return child_genes;
    }


    public void moveAllAnimals()
    {
        LinkedHashMap<Vector2d, ArrayList<Animal>> animal_positions_copy = new LinkedHashMap<>(this.animals);
        ArrayList<ArrayList<Animal>> list_of_arraylist_copies = new ArrayList<>();
        for(Vector2d animal_positions: animal_positions_copy.keySet())
        {
            ArrayList<Animal> list_to_add = new ArrayList<>(animal_positions_copy.get(animal_positions));
            list_of_arraylist_copies.add(list_to_add);
        }
        for(ArrayList<Animal> array_list_copies: list_of_arraylist_copies)
        {
            for(Animal animal_to_move: array_list_copies)
            {
                animal_to_move.subtractEnergy(energyLoss);
                animal_to_move.move();
                this.animals_number += 1;
                this.sum_daily_energy += animal_to_move.getEnergy();
            }
        }
    }

    public int getAnimalsNumber()
    {
        return this.animals_number;
    }

    public void day_passing()
    {
        this.animals_number = 0;
        this.averageEnergy = 0;
        this.sum_daily_energy = 0;
        this.daily_children_number = 0;
        this.averageChildrenNumber = 0;
        removeDeadAnimals();
        moveAllAnimals();
        eatingPlants();
        copulate();
        seedPlants();
        this.averageChildrenNumber = (float)this.daily_children_number/this.animals_number;
        this.averageEnergy = (float)this.sum_daily_energy/this.animals_number;
        this.averageLifeLengthDead = (float)this.sumLifeLengthDead/this.dead_number;
        this.daysPassed += 1;
    }

    public int getDayCount()
    {
        return this.daysPassed;
    }

    public int getPlants_number()
    {
        return this.plants_number;
    }

    public float getAverageEnergy()
    {
        return this.averageEnergy;
    }

    public float getAverageLifeLengthDead()
    {
        return this.averageLifeLengthDead;
    }

    public float getAverageChildrenNumber()
    {
        return this.averageChildrenNumber;
    }



    public void positionChanged(Animal animal_to_change, Vector2d old_position, Vector2d new_position)
    {
        LinkedHashMap<Vector2d, ArrayList<Animal>> animal_positions_copied = new LinkedHashMap<>(this.animals);
        if (animal_positions_copied.get(old_position).size() == 1)
        {
            this.animals.remove(old_position);
        }
        else if (animal_positions_copied.get(old_position).size() > 1)
        {
            for(int i = 0; i<animal_positions_copied.get(old_position).size(); i++)
            {
                if (animal_positions_copied.get(old_position).get(i).equals(animal_to_change))
                {
                    this.animals.get(old_position).remove(i);
                    break;
                }
            }
        }
        if (!animal_positions_copied.containsKey(new_position))
        {
            ArrayList<Animal> new_position_animals = new ArrayList<>();
            new_position_animals.add(animal_to_change);
            this.animals.put(new_position, new_position_animals);
        }
        else
        {
            this.animals.get(new_position).add(animal_to_change);
        }
    }

    public String toString()
    {
        return this.visualizer.draw(this.lower_left, this.upper_right);
    }

    public LinkedHashMap<Vector2d, ArrayList<Animal>> getAnimalsHashMap()
    {
        return this.animals;
    }

    public LinkedHashMap<Vector2d, Plant> getPlantsHashMap()
    {
        return this.plants;
    }


}


