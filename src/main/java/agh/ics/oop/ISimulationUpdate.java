package agh.ics.oop;

import agh.ics.oop.gui.DataChart;
import agh.ics.oop.gui.GenotypeText;
import javafx.scene.layout.GridPane;

public interface ISimulationUpdate {

    void mapUpdate(AbstractMap map, GridPane gridPane, SimulationEngine engine, DataChart dataChart, GenotypeText text);
}
