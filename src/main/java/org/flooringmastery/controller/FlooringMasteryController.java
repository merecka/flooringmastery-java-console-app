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
import java.util.Objects;

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
                    editOrder();
                    break;
                case 4:
                    deleteOrder();
                    break;
                case 5:
                    System.out.println("Feature not enabled yet.");
                    break;
                case 6:
                    keepGoing = false;
                    break;
                default:
                    unknownCommand();
            }
        }
        exitMessage();
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    private void displayOrders() throws FlooringMasteryPersistenceException {
        String orderDateToDisplayString = view.displayOrdersPrompt();
        List<Order> allOrders = service.allOrdersForDate(orderDateToDisplayString);
        view.displayAllOrders(allOrders, orderDateToDisplayString);
    }

    private void addOrder() throws FlooringMasteryPersistenceException {
        boolean isEditOrder = false;

        // Retrieve info for new Order
        List<Tax> allTaxes = service.getAllTaxes();
        List<Product> allProducts = service.getAllProducts();
        Order newOrder = view.startNewOrder();
        view.getNewOrderCustomerName(isEditOrder);
        view.getNewOrderTax(allTaxes);
        view.getNewOrderProduct(allProducts);
        view.getNewOrderArea();
        service.calculateOrderMaterialCost(newOrder);
        service.calculateOrderLaborCost(newOrder);
        service.calculateOrderTax(newOrder);
        service.calculateTotalOrderCost(newOrder);

        // Confirm Order purchase with User
        boolean newOrderConfirmation = view.confirmOrderEdits(newOrder, true);

        if (newOrderConfirmation == false) {
            return;   // Cancel the Order and return to main menu
        }

        // Get and set the Order number
        service.getNewOrderNumber(newOrder);

        // Persist the new Order
        service.createOrder(newOrder);
    }

    public void editOrder() throws FlooringMasteryPersistenceException {
        view.displayEditOrderBanner();
        // Locate Order to Edit
        LocalDate editOrderDate = view.getEditOrderDate();
        int editOrderNumber = view.getEditOrderNumber();
        Order orderToEdit = service.retrieveOrderToEdit(editOrderNumber, editOrderDate);

        if (Objects.isNull(orderToEdit)) {
            view.displayErrorMessage("No order exists with this criteria.");
            return;
        }

        // Edit Customer Name
        view.getEditOrderCustomerName(orderToEdit);

        // Edit Tax State for Order
        List<Tax> allTaxes = service.getAllTaxes();
        view.getEditOrderTax(allTaxes, orderToEdit);

        // Edit Product for Order
        List<Product> allProducts = service.getAllProducts();
        view.getEditOrderProduct(allProducts, orderToEdit);

        // Edit Area for Order
        view.getEditOrderArea(orderToEdit);

        // Calculate Order totals
        service.calculateOrderMaterialCost(orderToEdit);
        service.calculateOrderLaborCost(orderToEdit);
        service.calculateOrderTax(orderToEdit);
        service.calculateTotalOrderCost(orderToEdit);

        // Confirm Order purchase with User
        boolean newOrderConfirmation = view.confirmOrderEdits(orderToEdit, true);

        if (newOrderConfirmation == false) {
            return;   // Cancel the Order edit and return to main menu
        }

        //Persist the edited Order
        service.replaceEditedOrder(orderToEdit);
    }

    public void deleteOrder() throws FlooringMasteryPersistenceException {
        view.displayRemoveOrderBanner();
        // Locate Order to Delete
        LocalDate editOrderDate = view.getEditOrderDate();
        int editOrderNumber = view.getEditOrderNumber();
        Order orderToDelete = service.retrieveOrderToEdit(editOrderNumber, editOrderDate);

        if (Objects.isNull(orderToDelete)) {
            view.displayErrorMessage("No order exists with this criteria.");
            return;
        }

        // Confirm Order deletion with User
        boolean newOrderConfirmation = view.confirmOrderEdits(orderToDelete, false);

        if (newOrderConfirmation == false) {
            return;   // Cancel the Order deletion and return to main menu
        }

        //Persist the deleted Order
        service.removeDeletedOrder(orderToDelete);
    }

    private void unknownCommand() {
        view.displayErrorMessage("You must select from one of the available options.");
    }

    private void exitMessage() {
        view.displayExitBanner();
    }
}
