package agh.ics.oop.gui;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ParameterBox extends HBox {

    public TextField textField;
    public HBox parameterBox;

    public ParameterBox(String field, String defaultValue)
    {
        this.textField = new TextField(defaultValue);
        Text text = new Text(field);
        this.parameterBox = new HBox(text, textField);
        parameterBox.setAlignment(Pos.CENTER);
        parameterBox.setSpacing(10);
    }

}
