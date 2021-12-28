package agh.ics.oop.gui;

import agh.ics.oop.AbstractMap;
import agh.ics.oop.SimulationEngine;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static java.lang.System.out;

public class DataChart {

    final NumberAxis an_xAxis = new NumberAxis();
    final NumberAxis an_yAxis = new NumberAxis();
    final NumberAxis pn_xAxis = new NumberAxis();
    final NumberAxis pn_yAxis = new NumberAxis();
    final NumberAxis ac_xAxis = new NumberAxis();
    final NumberAxis ac_yAxis = new NumberAxis();
    final NumberAxis ae_xAxis = new NumberAxis();
    final NumberAxis ae_yAxis = new NumberAxis();
    final NumberAxis all_xAxis = new NumberAxis();
    final NumberAxis all_yAxis = new NumberAxis();
    private AbstractMap map;
    private SimulationEngine engine;
    public LineChart<Number, Number> animals_number = new LineChart<>(an_xAxis, an_yAxis);
    public LineChart<Number, Number> plants_number = new LineChart<>(pn_xAxis, pn_yAxis);
    public LineChart<Number, Number> averageChildren = new LineChart<>(ac_xAxis, ac_yAxis);
    public LineChart<Number, Number> averageEnergy = new LineChart<>(ae_xAxis, ae_yAxis);
    public LineChart<Number, Number> averageLifeLength = new LineChart<>(all_xAxis, all_yAxis);
    public XYChart.Series an_series = new XYChart.Series();
    public XYChart.Series pn_series = new XYChart.Series();
    public XYChart.Series ac_series = new XYChart.Series();
    public XYChart.Series ae_series = new XYChart.Series();
    public XYChart.Series all_series = new XYChart.Series();


    public DataChart(AbstractMap map)
    {
        this.map = map;
        animals_number.getData().add(an_series);
        animals_number.setMaxHeight(1);
        animals_number.setMaxWidth(2);
        animals_number.setTitle("number of animals per day");
        plants_number.getData().add(pn_series);
        plants_number.setMaxHeight(1);
        plants_number.setMaxWidth(2);
        plants_number.setTitle("number of plants per day");
        averageChildren.getData().add(ac_series);
        averageChildren.setMaxHeight(1);
        averageChildren.setMaxWidth(2);
        averageChildren.setTitle("average children number");
        averageEnergy.getData().add(ae_series);
        averageEnergy.setMaxHeight(1);
        averageEnergy.setMaxWidth(2);
        averageEnergy.setTitle("average animal energy");
        averageLifeLength.getData().add(all_series);
        averageLifeLength.setMaxHeight(1);
        averageLifeLength.setMaxWidth(2);
        averageLifeLength.setTitle("avg life length for dead");
    }

    public void updateCharts()
    {
        an_series.getData().add(new XYChart.Data<>(this.map.getDayCount(), this.map.getAnimalsNumber()));
        pn_series.getData().add(new XYChart.Data<>(this.map.getDayCount(), this.map.getPlants_number()));
        ae_series.getData().add(new XYChart.Data<>(this.map.getDayCount(), this.map.getAverageEnergy()));
        ac_series.getData().add(new XYChart.Data<>(this.map.getDayCount(), this.map.getAverageChildrenNumber()));
        all_series.getData().add(new XYChart.Data<>(this.map.getDayCount(), this.map.getAverageLifeLengthDead()));
    }

    public VBox get_chart_VBox()
    {
        HBox hbox1 = new HBox(this.animals_number, this.plants_number);
        hbox1.setSpacing(10);
        hbox1.setAlignment(Pos.CENTER);
        HBox hbox2 = new HBox(this.averageEnergy, this.averageChildren, this.averageLifeLength);
        hbox2.setSpacing(10);
        hbox2.setAlignment(Pos.CENTER);
        VBox vbox = new VBox(hbox1, hbox2);
        vbox.setSpacing(10);
        return vbox;
    }
}
