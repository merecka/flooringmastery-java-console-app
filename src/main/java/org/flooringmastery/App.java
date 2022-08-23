package org.flooringmastery;

import org.flooringmastery.controller.FlooringMasteryController;
import org.flooringmastery.dao.*;
import org.flooringmastery.service.FlooringMasteryServiceLayer;
import org.flooringmastery.ui.FlooringMasteryView;
import org.flooringmastery.ui.UserIO;
import org.flooringmastery.ui.UserIOConsoleImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;

public class App {
    public static void main(String[] args) throws FlooringMasteryPersistenceException {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.scan("org.flooringmastery");
        appContext.refresh();

        FlooringMasteryController controller = appContext.getBean("flooringMasteryController", FlooringMasteryController.class);
        controller.run();

//        Commented code below is configuration without Spring DI
//        // Instantiate the UserIO implementation
//        UserIO myIo = new UserIOConsoleImpl();
//        // Instantiate the View and wire the UserIO implementation into it
//        FlooringMasteryView orderView = new FlooringMasteryView(myIo);
//        // Instantiate the OrderDAO
//        OrderDao orderDao = new OrderDaoFileImpl();
//        // Instantiate the TaxDAO
//        TaxDao taxDao = new TaxDaoFileImpl();
//        // Instantiate the ProductDAO
//        ProductDao productDao = new ProductDaoFileImpl();
//        // Instantiate the FlooringMasteryService Layer and wire the DAOs into it
//        FlooringMasteryServiceLayer flooringService = new FlooringMasteryServiceLayer(orderDao, taxDao, productDao);
//        //  Instantiate the Controller and wire the Service Layer into it
//        FlooringMasteryController controller = new FlooringMasteryController(orderView, flooringService);
//        // Kick off the Controller
//        controller.run();


    }
}