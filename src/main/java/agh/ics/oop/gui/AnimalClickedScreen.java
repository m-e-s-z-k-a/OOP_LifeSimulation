package agh.ics.oop.gui;

import agh.ics.oop.Animal;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Arrays;

public class AnimalClickedScreen extends VBox {

    public Button genotype_button = new Button("get genotype");
    public Text text = new Text();


    public AnimalClickedScreen(Animal animal)
    {
        super();
        VBox animal_clicked_screen = new VBox(this.genotype_button, this.text);
        animal_clicked_screen.setAlignment(Pos.CENTER);
        genotype_button.setOnAction(e ->
        {
            text.setText(Arrays.toString(animal.getGenotype()));
        });
    }



}
