package agh.ics.oop.gui;

import agh.ics.oop.AbstractMap;
import javafx.scene.text.Text;
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

    public Text getGenDominant()
    {
        return this.text;
    }
}
