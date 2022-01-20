package agh.ics.oop;
import agh.ics.oop.gui.App;
import javafx.application.Application;

import static java.lang.System.out;

public class World {
    public static void main(String[] args)
    {
        Application.launch(App.class, args);
//        System.out.println("Start");  // nie zostawiamy wykomentowanego kodu
//        AbstractMap map = new BordersMap(15, 10, 0.5, 10, 1, 20);
//        Vector2d[] positions = { new Vector2d(2,2), new Vector2d(3,4),  new Vector2d(3,4)};
//        IEngine engine = new SimulationEngine(map, positions);
//        out.println(map);
//        engine.run();
    }
}
