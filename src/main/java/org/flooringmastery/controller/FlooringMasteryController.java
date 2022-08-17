package org.flooringmastery.controller;

import org.flooringmastery.dao.FlooringMasteryPersistenceException;
import org.flooringmastery.dto.Order;
import org.flooringmastery.service.FlooringMasteryServiceLayer;
import org.flooringmastery.ui.FlooringMasteryView;

import java.util.List;

public class FlooringMasteryController {

    private FlooringMasteryView view;

    private FlooringMasteryServiceLayer service;

    public FlooringMasteryController(FlooringMasteryView view, FlooringMasteryServiceLayer service) {
        this.view = view;
        this.service = service;
    }

    public void run() throws FlooringMasteryPersistenceException {
        boolean keepGoing = true;
        int menuSelection;

        while (keepGoing) {

            menuSelection = getMenuSelection();

            switch (menuSelection) {
                case 1:
                    displayOrders();
                    break;
                case 2:
//                    addOrder();
                    break;
                case 3:
//                    editOrder();
                    break;
                case 4:
//                    deleteOrder();
                    break;
                case 5:
                    System.out.println("Feature not enabled yet.");
                    break;
                case 6:
                    keepGoing = false;
                    break;
                default:
//                    unknownCommand();
            }
        }
        exitMessage();
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    private void displayOrders() throws FlooringMasteryPersistenceException {
        String orderDateToDisplayString = view.displayOrdersPrompt();
        List<Order> allOrders =  service.allOrdersForDate(orderDateToDisplayString);
        view.displayAllOrders(allOrders, orderDateToDisplayString);
    }

    private void exitMessage() {
        view.displayExitBanner();
    }
}
