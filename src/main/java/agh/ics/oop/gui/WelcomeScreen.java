package agh.ics.oop.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class WelcomeScreen extends VBox
{
    public ParameterBox widthbox = new ParameterBox("width", "10");
    public ParameterBox heightbox = new ParameterBox("height", "10");
    public ParameterBox startenergybox = new ParameterBox("start energy", "100");
    public ParameterBox plantenergybox = new ParameterBox("plant energy", "15");
    public ParameterBox energylossbox = new ParameterBox("energy loss", "10");
    public ParameterBox jungleratiobox = new ParameterBox("jungle ratio", "0.25");
    public ParameterBox startanimalsnumberbox = new ParameterBox("number of animals to start with", "10");
    public Button startButton = new Button("start!");


    public WelcomeScreen()
    {
        super();
        VBox parameters_box = new VBox(widthbox.parameterBox, heightbox.parameterBox, startenergybox.parameterBox, plantenergybox.parameterBox,
                energylossbox.parameterBox, jungleratiobox.parameterBox, startanimalsnumberbox.parameterBox);
        parameters_box.setAlignment(Pos.CENTER);
        parameters_box.setSpacing(10);
        VBox welcomeScreen = new VBox(parameters_box, startButton);
        welcomeScreen.setSpacing(20);
        welcomeScreen.setAlignment(Pos.CENTER);
        this.getChildren().add(welcomeScreen);
        this.setAlignment(Pos.CENTER);
    }

}
