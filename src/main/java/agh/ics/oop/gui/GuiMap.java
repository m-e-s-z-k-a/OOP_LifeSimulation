//package agh.ics.oop.gui;
//
//import agh.ics.oop.*;
//import javafx.application.Platform;
//import javafx.geometry.HPos;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.ToggleButton;
//import javafx.scene.layout.ColumnConstraints;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.RowConstraints;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.Random;
//
//public class GuiMap extends GridPane
//{
//    public GridPane gridPane = new GridPane();
//    public ToggleButton pause_button = new ToggleButton("pause/start");
//    private SimulationEngine engine;
//    private AbstractMap map;
//    private Vector2d lower_left;
//    private Vector2d upper_right;
//    private int columns_number;
//    private int rows_number;
//    public GuiMap(AbstractMap map, SimulationEngine engine)
//    {
//        super();
//        this.engine = engine;
//        this.map = map;
//        this.lower_left = map.get_lower_left();
//        this.upper_right = map.get_upper_right();
//        this.columns_number = (this.upper_right.y - this.lower_left.y)+1;
//        this.rows_number = (this.upper_right.x - this.lower_left.x)+1;
//    }
//
//    private void set_the_map()
//    {
//        this.gridPane.getChildren().clear();
//        this.gridPane.getColumnConstraints().clear();
//        this.gridPane.getRowConstraints().clear();
//        this.gridPane.setGridLinesVisible(true);
//        LinkedHashMap<Vector2d, Plant> plants_copy = new LinkedHashMap<>(this.map.getPlantsHashMap());
//        LinkedHashMap<Vector2d, ArrayList<Animal>> animals_copy = new LinkedHashMap<>(this.map.getAnimalsHashMap());
//        Label axis = new Label("y\\x");
//        this.gridPane.add(axis, 0, 0);
//        this.gridPane.setHalignment(axis, HPos.CENTER);
//        for(int i = 0; i<=rows_number; i++)
//        {
//            this.gridPane.getColumnConstraints().add(new ColumnConstraints(30));
//        }
//        for (int i = 0; i<=columns_number; i++)
//        {
//            this.gridPane.getRowConstraints().add(new RowConstraints(30));
//        }
//        for (int i = 0; i<rows_number; i++)
//        {
//            Label row_no = new Label(Integer.toString(lower_left.x + i));
//            this.gridPane.add(row_no, i+1, 0);
//            this.gridPane.setHalignment(row_no, HPos.CENTER);
//        }
//        for (int i = 0; i<columns_number; i++)
//        {
//            Label col_no = new Label(Integer.toString(upper_right.y-i));
//            this.gridPane.add(col_no, 0, i+1);
//            this.gridPane.setHalignment(col_no, HPos.CENTER);
//        }
//        for(Vector2d animal_key: animals_copy.keySet())
//        {
//            ArrayList<Animal> animals_to_show_list = new ArrayList<>();
//            animals_to_show_list = map.getMaxEnergyAnimals(animals_copy.get(animal_key));
//            Animal animal_to_show = animals_to_show_list.get(new Random().nextInt(animals_to_show_list.size()));
//            Button button = GuiElementBox.createGuiButton(animal_to_show);
//            this.gridPane.add(button, animal_key.x-lower_left.x+1, upper_right.y-animal_key.y+1);
//            this.gridPane.setHalignment(button, HPos.CENTER);
//        }
//        for(Vector2d plants_key: plants_copy.keySet())
//        {
//            if (map.objectAt(plants_key) instanceof Plant) {
//                Button button = GuiElementBox.createGuiButton(plants_copy.get(plants_key));
//                this.gridPane.add(button, plants_key.x - lower_left.x + 1, upper_right.y - plants_key.y + 1);
//                this.gridPane.setHalignment(button, HPos.CENTER);
//            }
//        }
//    }
//
//
//}
