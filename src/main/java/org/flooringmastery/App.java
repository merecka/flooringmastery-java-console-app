package org.flooringmastery;

import org.flooringmastery.controller.FlooringMasteryController;
import org.flooringmastery.dao.FlooringMasteryPersistenceException;
import org.flooringmastery.dao.OrderDao;
import org.flooringmastery.dao.OrderDaoFileImpl;
import org.flooringmastery.service.FlooringMasteryServiceLayer;
import org.flooringmastery.ui.FlooringMasteryView;
import org.flooringmastery.ui.UserIO;
import org.flooringmastery.ui.UserIOConsoleImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) throws FlooringMasteryPersistenceException {
        // Instantiate the UserIO implementation
        UserIO myIo = new UserIOConsoleImpl();
        // Instantiate the View and wire the UserIO implementation into it
        FlooringMasteryView orderView = new FlooringMasteryView(myIo);
        // Instantiate the OrderDAO
        OrderDao orderDao = new OrderDaoFileImpl();
        // Instantiate the FlooringMasteryService Layer and wire the DAO into it
        FlooringMasteryServiceLayer flooringService = new FlooringMasteryServiceLayer(orderDao);
        //  Instantiate the Controller and wire the Service Layer into it
        FlooringMasteryController controller = new FlooringMasteryController(orderView, flooringService);
        // Kick off the Controller
        controller.run();

        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
    }
}