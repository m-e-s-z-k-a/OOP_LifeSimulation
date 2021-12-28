package agh.ics.oop.gui;

import agh.ics.oop.AbstractMap;

import java.io.FileWriter;
import java.io.IOException;

public class FileData {

    private AbstractMap map;
    private StringBuffer fileData = new StringBuffer();
    private int sum_animals = 0;
    private int sum_plants = 0;
    private double sum_ae = 0;
    private double sum_ac = 0;
    private double sum_all = 0;

    public FileData(AbstractMap map)
    {
        this.map = map;
        String header = ("day, number of animals, number of plants, average energy, average children number, " +
                "average life length of dead");
        fileData.append(header).append("\n");
    }

    public void updateFileData()
    {
        sum_ac += this.map.getAverageChildrenNumber();
        sum_all += this.map.getAverageLifeLengthDead();
        sum_ae += this.map.getAverageEnergy();
        sum_animals += this.map.getAnimalsNumber();
        sum_plants += this.map.getPlants_number();

        fileData.append(String.valueOf(this.map.getDayCount())).append(", ").
        append(String.valueOf(this.map.getAnimalsNumber())).append(", ").
        append(String.valueOf(this.map.getPlants_number())).append(", ").
        append(String.valueOf(this.map.getAverageEnergy())).append(", ").
        append(String.valueOf(this.map.getAverageChildrenNumber())).append(", ").
        append(String.valueOf(this.map.getAverageLifeLengthDead())).append("\n");

    }

    public void exportToCSV() throws IOException {
        int days_passed = this.map.getDayCount() + 1;
        String filename = this.map.getMapType() +"_afterday_"+ (days_passed-1) + "_stats.csv";
        FileWriter fileWriter = new FileWriter(filename);
        fileWriter.append(fileData);
        fileWriter.append("average: ").append(String.valueOf((double)sum_animals/days_passed)).append(", ").
                append(String.valueOf((double)sum_plants/days_passed)).append(", ").
                append(String.valueOf((double)sum_ae/days_passed)).append(", ").
                append(String.valueOf((double)sum_ac/days_passed)).append(", ").
                append(String.valueOf((double)sum_all/days_passed));
        fileWriter.close();
    }

}
