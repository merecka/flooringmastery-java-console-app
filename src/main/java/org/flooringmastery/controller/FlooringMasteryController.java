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
                    addOrder();
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

    private void addOrder() throws FlooringMasteryPersistenceException {
        int newOrderNumber;

        // Retrieve info for new Order
        List<Tax> allTaxes = service.getAllTaxes();
        List<Product> allProducts = service.getAllProducts();
        LocalDate newOrderDate = view.getNewOrderDate();
        String newOrderCustomerName = view.getNewOrderCustomerName();
        Tax newOrderTax = view.getNewOrderTax(allTaxes);
        Product newOrderProduct = view.getNewOrderProduct(allProducts);
        BigDecimal newOrderArea = view.getNewOrderArea();
        BigDecimal newOrderMaterialCost = service.calculateOrderMaterialCost(newOrderArea, newOrderProduct);
        BigDecimal newOrderLaborCost = service.calculateOrderLaborCost(newOrderArea, newOrderProduct);
        BigDecimal newOrderTaxCost = service.calculateOrderTax(newOrderTax, newOrderMaterialCost, newOrderLaborCost);
        BigDecimal newOrderTotal = service.calculateTotalOrderCost(newOrderTaxCost, newOrderMaterialCost, newOrderLaborCost);
        newOrderNumber = service.getNewOrderNumber(newOrderDate);
        if (newOrderNumber == 0) {
            newOrderNumber = 1;
        }

        // Confirm Order purchase with User
        boolean newOrderConfirmation = view.confirmOrder(newOrderTotal);

        if (newOrderConfirmation == false) {
            return;   // Cancel the Order and return to main menu
        }

        // Create new Order object
        Order newOrder = new Order(newOrderNumber);
        newOrder.setOrderDate(newOrderDate);
        newOrder.setCustomerName(newOrderCustomerName);
        newOrder.setState(newOrderTax.getStateAbbreviation());
        newOrder.setTaxRate(newOrderTax.getTaxRate());
        newOrder.setProductType(newOrderProduct.getProductType());
        newOrder.setArea(newOrderArea);
        newOrder.setCostPerSquareFoot(newOrderProduct.getCostPerSquareFoot());
        newOrder.setLaborCostPerSquareFoot(newOrderProduct.getLaborCostPerSquareFoot());
        newOrder.setMaterialCost(newOrderMaterialCost);
        newOrder.setLaborCost(newOrderLaborCost);
        newOrder.setTax(newOrderTaxCost);
        newOrder.setTotal(newOrderTotal);

        // Persist the new Order
        service.createOrder(newOrder);
    }

    private void exitMessage() {
        view.displayExitBanner();
    }
}
