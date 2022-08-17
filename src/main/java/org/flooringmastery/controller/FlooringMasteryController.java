package org.flooringmastery.controller;

import org.flooringmastery.ui.FlooringMasteryView;

public class FlooringMasteryController {

    private FlooringMasteryView view;

    public FlooringMasteryController(FlooringMasteryView view) {
        this.view = view;
    }

    public void run() {
        boolean keepGoing = true;
        int menuSelection;

        while (keepGoing) {

            menuSelection = getMenuSelection();

            switch (menuSelection) {
                case 1:
//                    displayOrders();
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

    private void exitMessage() {
        view.displayExitBanner();
    }
}
