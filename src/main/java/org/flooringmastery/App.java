package org.flooringmastery;

import org.flooringmastery.controller.FlooringMasteryController;
import org.flooringmastery.ui.FlooringMasteryView;
import org.flooringmastery.ui.UserIO;
import org.flooringmastery.ui.UserIOConsoleImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        // Instantiate the UserIO implementation
        UserIO myIo = new UserIOConsoleImpl();
        // Instantiate the View and wire the UserIO implementation into it
        FlooringMasteryView myView = new FlooringMasteryView(myIo);
        //  Instantiate the Controller and wire the Service Layer into it
        FlooringMasteryController controller = new FlooringMasteryController(myView);
        // Kick off the Controller
        controller.run();

        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
    }
}