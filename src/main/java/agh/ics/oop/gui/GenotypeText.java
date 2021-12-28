package agh.ics.oop.gui;

import agh.ics.oop.AbstractMap;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Arrays;

import static java.lang.System.out;

public class GenotypeText {

    private AbstractMap map;
    private Text text = new Text("");

    public GenotypeText(AbstractMap map)
    {
        this.map = map;
    }

    public void updateGenDominant()
    {
        this.text.setText(this.map.getDomGen());
    }

    public HBox getGenDominantHBox()
    {
        this.text.setTextAlignment(TextAlignment.CENTER);
        HBox hbox = new HBox(this.text);
        hbox.setAlignment(Pos.CENTER);
        return hbox;
    }
}
