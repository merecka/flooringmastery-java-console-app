package org.flooringmastery.controller;

import org.flooringmastery.dao.FlooringMasteryPersistenceException;
import org.flooringmastery.dto.Order;
import org.flooringmastery.dto.Product;
import org.flooringmastery.dto.Tax;
import org.flooringmastery.service.FlooringMasteryServiceLayer;
import org.flooringmastery.ui.FlooringMasteryView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    private void createOrder() throws FlooringMasteryPersistenceException {
        List<Tax> allTaxes = service.getAllTaxes();
//        List<String> allTaxStates = allTaxes.stream().map(tax -> tax.getStateAbbreviation()).collect(Collectors.toList());
        List<Product> allProducts = service.getAllProducts();
        LocalDate newOrderDate = view.getNewOrderDate();
        String newOrderCustomerName = view.getNewOrderCustomerName();
        Tax newOrderTax = view.getNewOrderTax(allTaxes);
        Product newOrderProduct = view.getNewOrderProduct(allProducts);
        BigDecimal newOrderArea = view.getNewOrderArea();

        BigDecimal newOrderTotal = service.calculateTotalOrderCost(newOrderTax, newOrderProduct, newOrderArea);
        boolean newOrderConfirmation = view.confirmOrder(newOrderTotal);

        if (newOrderConfirmation) {
            service.createOrder(newOrderDate, newOrderCustomerName, newOrderTax, newOrderProduct, newOrderArea, newOrderTotal);
        }
    }

    private void exitMessage() {
        view.displayExitBanner();
    }
}
