package agh.ics.oop.gui;

import agh.ics.oop.AbstractMap;
import agh.ics.oop.Animal;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TrackingText
{
    private Animal animal;
    private AbstractMap map;
    private Text days_length_text = new Text();
    private Text kids_number = new Text();
    private Text descendants_number = new Text();

    public TrackingText(Animal animal, AbstractMap map)
    {
        this.animal = animal;
        this.map = map;
        this.updateTrackingTexts();
    }

    public void updateTrackingTexts()
    {
        this.days_length_text.setText("this animal has been alive for " + this.animal.getLifeLength() + " days");
        this.kids_number.setText("this animal has " + this.animal.getChildrenNumber() + " children");
        this.descendants_number.setText("this animal has " + this.animal.getDescendants_number() + " descendants");
    }

    public VBox getTrackingInfo()
    {
        VBox vbox = new VBox(this.days_length_text, this.kids_number, this.descendants_number);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }
}
