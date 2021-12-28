package agh.ics.oop.gui;
import agh.ics.oop.AbstractMap;
import agh.ics.oop.Animal;
import agh.ics.oop.Plant;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class GuiElementBox
{
    static Image image_animal_green = new Image("/kropa_duzoenergii.png");
    static Image image_animal_lime = new Image("/kropa_srednioenergii.png");
    static Image image_animal_yellow = new Image("/kropa_maloenergii.png");
    static Image image_animal_orange = new Image("/kropa_bmaloenergii.png");
    static Image image_animal_red = new Image("/kropa_smierc.png");
    static Image image_plant_jungle = new Image("/jungle_plant.png");
    static Image image_plant_plain = new Image("/plain_plant.png");



    public static Button createGuiButton(Animal animal)
    {
        ImageView imageView;
        if (animal.getEnergy() >= 0.8*(double)animal.getMapStartEnergy())
           imageView = new ImageView(image_animal_green);
        else if (animal.getEnergy() >= 0.6*(double)animal.getMapStartEnergy())
            imageView = new ImageView(image_animal_lime);
        else if (animal.getEnergy() >= 0.4*(double)animal.getMapStartEnergy())
            imageView = new ImageView(image_animal_yellow);
        else if (animal.getEnergy() >= 0.2*(double)animal.getMapStartEnergy())
            imageView = new ImageView(image_animal_orange);
        else
            imageView = new ImageView(image_animal_red);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        Button button = new Button("", imageView);
        button.setAlignment(Pos.CENTER);
        return button;
    }
    public static VBox createGuiElement(Plant plant)
    {
        ImageView imageView;
        if (plant.isInJungle())
        {
            imageView = new ImageView(image_plant_jungle);
        }
        else
        {
            imageView = new ImageView(image_plant_plain);
        }
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        VBox vbox = new VBox(imageView);
        vbox.setMaxHeight(20);
        vbox.setMaxWidth(20);
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }
}
