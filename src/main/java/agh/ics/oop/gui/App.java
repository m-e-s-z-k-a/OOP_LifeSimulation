package agh.ics.oop.gui;
import agh.ics.oop.*;
import agh.ics.oop.AbstractMap;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

import static java.lang.System.out;

public class App  extends Application implements ISimulationUpdate
{
    private AbstractMap bordersmap;
    static Image image_dom = new Image("/kropa_dominanta.png");
    private AbstractMap foldablemap;
    private DataChart dataChart1;
    private DataChart dataChart2;
    private GenotypeText text1;
    private GenotypeText text2;
    private FileData fileData1;
    private FileData fileData2;
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
    private Stage secondaryStage;

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
            this.fileData1.updateFileData();
            threadzik = new Thread(this.engine1);
            threadzik.start();
            this.dataChart2.updateCharts();
            this.text2.updateGenDominant();
            this.fileData2.updateFileData();
            threadzik2 = new Thread(this.engine2);
            threadzik2.start();
            Button b_get_dom_gen_animals1 = new Button("get dominant genotype animals");
            ToggleButton button_pause1 = new ToggleButton("start/pause");
            Button button_save1 = new Button("save to CSV file");
            ToggleButton button_pause2 = new ToggleButton("start/pause");
            Button b_get_dom_gen_animals2 = new Button("get dominant genotype animals");
            Button button_save2 = new Button("save to CSV file");
            button_pause1.setOnAction(e1 ->
            {
                this.engine1.pauseandrun();
                button_save1.setOnAction(ev1 ->
                {
                    try {
                        this.fileData1.exportToCSV();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                b_get_dom_gen_animals1.setOnAction( event ->{
                    modify_dom_animals(this.bordersmap, this.gridPane1);
                });
            });
            button_pause2.setOnAction(e2 ->
            {
                this.engine2.pauseandrun();
                button_save2.setOnAction(ev1 ->
                {
                    try {
                        this.fileData2.exportToCSV();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                b_get_dom_gen_animals2.setOnAction(event -> {
                    modify_dom_animals(this.foldablemap, this.gridPane2);
                });
            });
            HBox buttons_hbox1 = new HBox(button_pause1, button_save1, b_get_dom_gen_animals1);
            buttons_hbox1.setAlignment(Pos.CENTER);
            buttons_hbox1.setSpacing(5);
            HBox buttons_hbox2 = new HBox(button_pause2, button_save2, b_get_dom_gen_animals2);
            buttons_hbox2.setAlignment(Pos.CENTER);
            buttons_hbox2.setSpacing(5);
            VBox first_map_vbox = new VBox(gridPane1, buttons_hbox1, dataChart1.get_chart_VBox(), text1.getGenDominantHBox());
            VBox second_map_vbox = new VBox(gridPane2, buttons_hbox2, dataChart2.get_chart_VBox(), text2.getGenDominantHBox());
            first_map_vbox.setSpacing(20);
            second_map_vbox.setSpacing(20);
            HBox map_hbox = new HBox(first_map_vbox, second_map_vbox);
            map_hbox.setSpacing(15);
            Scene scene1 = new Scene(map_hbox, 1920, 1080);
            primaryStage.setScene(scene1);
            primaryStage.show();
        });

    }

    public void pass_arguments(WelcomeScreen welcomeScreen)
    {
        try{
        int width = Integer.parseInt(welcomeScreen.widthbox.textField.getText());
        int height = Integer.parseInt(welcomeScreen.heightbox.textField.getText());
        int energyLoss = Integer.parseInt(welcomeScreen.energylossbox.textField.getText());
        int plantEnergy = Integer.parseInt(welcomeScreen.plantenergybox.textField.getText());
        int startEnergy = Integer.parseInt(welcomeScreen.startenergybox.textField.getText());
        double jungleRatio = Double.parseDouble(welcomeScreen.jungleratiobox.textField.getText());
        int startAnimalsNumber = Integer.parseInt(welcomeScreen.startanimalsnumberbox.textField.getText());
        boolean isMagicGameplayOn = welcomeScreen.checkbox.isSelected();
        Vector2d[] positions = { new Vector2d(2,2), new Vector2d(3,4),  new Vector2d(3,4)};
        this.bordersmap = new BordersMap(width, height, jungleRatio, plantEnergy, energyLoss, startEnergy);
        this.gridPane1 = new GridPane();
        this.gridPane1.setAlignment(Pos.CENTER);
        this.dataChart1 = new DataChart(this.bordersmap);
        this.text1 = new GenotypeText(this.bordersmap);
        this.fileData1 = new FileData(this.bordersmap);
        this.engine1 = new SimulationEngine(this.bordersmap, startAnimalsNumber, isMagicGameplayOn, this.gridPane1, this.dataChart1, this.text1, this.fileData1);
        this.engine1.addObserver(this);
        this.foldablemap = new FoldableMap(width, height, jungleRatio, plantEnergy, energyLoss, startEnergy);
        this.gridPane2 = new GridPane();
        this.gridPane2.setAlignment(Pos.CENTER);
        this.dataChart2 = new DataChart(this.foldablemap);
        this.text2 = new GenotypeText(this.foldablemap);
        this.fileData2 = new FileData(this.foldablemap);
        this.engine2 = new SimulationEngine(this.foldablemap, startAnimalsNumber, isMagicGameplayOn, this.gridPane2, this.dataChart2, this.text2, this.fileData2);
        this.engine2.addObserver(this);
        }catch(InputMismatchException e)
        {
            System.out.println("Wrong type of data in input!");
        }
    }

    private void set_the_map(AbstractMap map, GridPane gridPane)
    {

        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
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
           Text genotype = new Text();
           Button genotype_button = new Button("get genotype");
           VBox genotype_vbox = new VBox(genotype_button, genotype);
           genotype_vbox.setAlignment(Pos.CENTER);
           genotype_vbox.setSpacing(10);
           Scene new_scene = new Scene(genotype_vbox, 420, 150);
           Stage new_stage = new Stage();
           new_stage.setScene(new_scene);
               genotype_button.setOnAction(e ->{
                    genotype.setText(Arrays.toString(animal_to_show.getGenotype()));
               }) ;
           button.setStyle("-fx-background-color: transparent");
           button.setOnAction(e ->
           {
              new_stage.show();
           });
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
        gridPane.setGridLinesVisible(true);
    }

    public void mapUpdate(AbstractMap map, GridPane gridPane, SimulationEngine engine, DataChart dataChart, GenotypeText text, FileData fileData)
    {
        Platform.runLater(()->{
            gridPane.setGridLinesVisible(false);
            set_the_map(map, gridPane);
            text.updateGenDominant();
            dataChart.updateCharts();
            fileData.updateFileData();
        });
    }

    public void modify_dom_animals(AbstractMap map, GridPane grid)
    {
        grid.setGridLinesVisible(false);
        ArrayList<Vector2d> positions_to_modify = map.get_dom_animals_positions();
        ImageView imageView = new ImageView(image_dom);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        VBox vbox_to_add = new VBox(imageView);
        vbox_to_add.setAlignment(Pos.CENTER);
        for(Vector2d grid_position: positions_to_modify)
        {
            grid.add(vbox_to_add, grid_position.x - lower_left.x + 1, upper_right.y - grid_position.y +1);
        }
        grid.setGridLinesVisible(true);

    }

}

