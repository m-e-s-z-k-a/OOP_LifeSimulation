package agh.ics.oop.gui;
import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import static java.lang.System.out;

public class App  extends Application implements ISimulationUpdate
{
    private AbstractMap bordersmap;
    private AbstractMap foldablemap;
    private DataChart dataChart1;
    private DataChart dataChart2;
    private GenotypeText text1;
    private GenotypeText text2;
    private LinkedHashMap<Vector2d, ArrayList<Animal>> animals;
    private LinkedHashMap<Vector2d, Plant> plants;
    private GridPane gridPane1;
    private GridPane gridPane2;
    private SimulationEngine engine1;
    private SimulationEngine engine2;
    private Thread threadzik;
    private Thread threadzik2;
    private Vector2d upper_right;
    private Vector2d lower_left;
    private int columns_number;
    private int rows_number;

    public void start(Stage primaryStage)
    {
        WelcomeScreen welcomeScreen = new WelcomeScreen();
        Scene scene = new Scene(welcomeScreen, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
        welcomeScreen.startButton.setOnAction(e -> {
            pass_arguments(welcomeScreen);
            set_the_map(this.bordersmap, gridPane1);
            set_the_map(this.foldablemap, gridPane2);
            this.dataChart1.updateCharts();
            this.text1.updateGenDominant();
            threadzik = new Thread(this.engine1);
            threadzik.start();
            this.dataChart2.updateCharts();
            this.text2.updateGenDominant();
            threadzik2 = new Thread(this.engine2);
            threadzik2.start();
            ToggleButton button_pause1 = new ToggleButton("start/pause");
            ToggleButton button_pause2 = new ToggleButton("start/pause");
            button_pause1.setOnAction(e1 ->
            {
                this.engine1.pauseandrun();
            });
            button_pause2.setOnAction(e2 ->
            {
                this.engine2.pauseandrun();
            });
            button_pause1.setAlignment(Pos.CENTER);
            button_pause2.setAlignment(Pos.CENTER);
            VBox first_map_vbox = new VBox(gridPane1, button_pause1, dataChart1.get_chart_VBox(), text1.getGenDominant());
            VBox second_map_vbox = new VBox(gridPane2, button_pause2, dataChart2.get_chart_VBox(), text2.getGenDominant());
            first_map_vbox.setSpacing(20);
            second_map_vbox.setSpacing(20);
            HBox map_hbox = new HBox(first_map_vbox, second_map_vbox);
            map_hbox.setSpacing(15);
            Scene scene1 = new Scene(map_hbox, 400, 400);
            primaryStage.setScene(scene1);
            primaryStage.show();
        });

    }

    public void pass_arguments(WelcomeScreen welcomeScreen)
    {
        int width = Integer.parseInt(welcomeScreen.widthbox.textField.getText());
        int height = Integer.parseInt(welcomeScreen.heightbox.textField.getText());
        int energyLoss = Integer.parseInt(welcomeScreen.energylossbox.textField.getText());
        int plantEnergy = Integer.parseInt(welcomeScreen.plantenergybox.textField.getText());
        int startEnergy = Integer.parseInt(welcomeScreen.startenergybox.textField.getText());
        double jungleRatio = Double.parseDouble(welcomeScreen.jungleratiobox.textField.getText());
        int startAnimalsNumber = Integer.parseInt(welcomeScreen.startanimalsnumberbox.textField.getText());
        Vector2d[] positions = { new Vector2d(2,2), new Vector2d(3,4),  new Vector2d(3,4)};
        this.bordersmap = new BordersMap(width, height, jungleRatio, plantEnergy, energyLoss, startEnergy);
        this.gridPane1 = new GridPane();
        this.gridPane1.setAlignment(Pos.CENTER);
        this.dataChart1 = new DataChart(this.bordersmap);
        this.text1 = new GenotypeText(this.bordersmap);
        this.engine1 = new SimulationEngine(this.bordersmap, startAnimalsNumber, this.gridPane1, this.dataChart1, this.text1);
        this.engine1.addObserver(this);
        this.foldablemap = new FoldableMap(width, height, jungleRatio, plantEnergy, energyLoss, startEnergy);
        this.gridPane2 = new GridPane();
        this.gridPane2.setAlignment(Pos.CENTER);
        this.dataChart2 = new DataChart(this.foldablemap);
        this.text2 = new GenotypeText(this.foldablemap);
        this.engine2 = new SimulationEngine(this.foldablemap, startAnimalsNumber, this.gridPane2, this.dataChart2, this.text2);
        this.engine2.addObserver(this);
    }

    private void set_the_map(AbstractMap map, GridPane gridPane)
    {
        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.setGridLinesVisible(true);
        LinkedHashMap<Vector2d, Plant> plants_copy = new LinkedHashMap<>(map.getPlantsHashMap());
        LinkedHashMap<Vector2d, ArrayList<Animal>> animals_copy = new LinkedHashMap<>(map.getAnimalsHashMap());
        Label axis = new Label("y\\x");
        this.lower_left = map.get_lower_left();
        this.upper_right = map.get_upper_right();
        this.columns_number = (this.upper_right.y - this.lower_left.y)+1;
        this.rows_number = (this.upper_right.x - this.lower_left.x)+1;
        gridPane.add(axis, 0, 0);
        gridPane.setHalignment(axis, HPos.CENTER);
        for(int i = 0; i<=rows_number; i++)
        {
            gridPane.getColumnConstraints().add(new ColumnConstraints(20));
        }
        for (int i = 0; i<=columns_number; i++)
        {
            gridPane.getRowConstraints().add(new RowConstraints(20));
        }
        for (int i = 0; i<rows_number; i++)
        {
            Label row_no = new Label(Integer.toString(lower_left.x + i));
            gridPane.add(row_no, i+1, 0);
            gridPane.setHalignment(row_no, HPos.CENTER);
        }
        for (int i = 0; i<columns_number; i++)
        {
            Label col_no = new Label(Integer.toString(upper_right.y-i));
            gridPane.add(col_no, 0, i+1);
            gridPane.setHalignment(col_no, HPos.CENTER);
        }
       for(Vector2d animal_key: animals_copy.keySet())
       {
           ArrayList<Animal> animals_to_show_list = new ArrayList<>();
           animals_to_show_list = map.getMaxEnergyAnimals(animals_copy.get(animal_key));
           if (animals_to_show_list.size() > 0)
           {Animal animal_to_show = animals_to_show_list.get(new Random().nextInt(animals_to_show_list.size()));
           Button button = GuiElementBox.createGuiButton(animal_to_show);
           gridPane.add(button, animal_key.x-lower_left.x+1, upper_right.y-animal_key.y+1);
           gridPane.setHalignment(button, HPos.CENTER);
       }}
       for(Vector2d plants_key: plants_copy.keySet())
        {
            if (map.objectAt(plants_key) instanceof Plant) {
                VBox vbox = GuiElementBox.createGuiElement(plants_copy.get(plants_key));
                gridPane.add(vbox, plants_key.x - lower_left.x + 1, upper_right.y - plants_key.y + 1);
                gridPane.setHalignment(vbox, HPos.CENTER);
            }
        }
    }

    public void mapUpdate(AbstractMap map, GridPane gridPane, SimulationEngine engine, DataChart dataChart, GenotypeText text)
    {
        Platform.runLater(()->{
            gridPane.setGridLinesVisible(false);
            set_the_map(map, gridPane);
            text.updateGenDominant();
            dataChart.updateCharts();
        });
    }

}

