package org.flooringmastery.ui;

import org.flooringmastery.dao.ProductDao;
import org.flooringmastery.dto.Order;
import org.flooringmastery.dto.Product;
import org.flooringmastery.dto.Tax;

import javax.print.attribute.standard.OrientationRequested;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Array;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlooringMasteryView {

    private UserIO io;

    public final String DATE_FORMAT = "MM/dd/yyyy";

    public FlooringMasteryView(UserIO userIO) {
        this.io = userIO;
    }

    public int printMenuAndGetSelection() {
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("* <<Flooring Program>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 6. Quit");

        return io.readInt("Please select from the above six Options (1-6).", 1, 6);
    }

    public String displayOrdersPrompt() {
        io.print("* * * * Display Orders * * * * * * *");
        String userEnteredDate;
        boolean keepGoing = false;
        do {
            userEnteredDate = io.readString("Please enter an Order date that you would like view Orders for. (ex: 08/03/2022)").trim();

            boolean validDate = checkValidDateFormat(userEnteredDate);

            if (validDate) {
                keepGoing = false;
            } else {
                keepGoing = true;
            }
        } while (keepGoing);
        return userEnteredDate;
    }

    public void displayAllOrders(List<Order> allOrders, String orderDate) {
        io.print("* * * * Orders for " + orderDate + " * * * * * * *");
        for (Order currentOrder : allOrders) {
            String orderInfo = String.format("Order Number %s - Customer: %s - State: %s - Tax Rate(percent): %s - Product Type: %s - Area: %s sq/ft - CostPerSqFt: $%s sq/ft - LaborCostPerSqFt $%s sq/ft - Material $%s - Labor $%s - Tax: $%s - Total: $%s",
                    currentOrder.getOrderNumber(),
                    currentOrder.getCustomerName(),
                    currentOrder.getState(),
                    currentOrder.getTaxRate(),
                    currentOrder.getProductType(),
                    currentOrder.getArea(),
                    currentOrder.getCostPerSquareFoot(),
                    currentOrder.getLaborCostPerSquareFoot(),
                    currentOrder.getMaterialCost(),
                    currentOrder.getLaborCost(),
                    currentOrder.getTax(),
                    currentOrder.getTotal());
            io.print(orderInfo);
        }
    }

    public LocalDate getNewOrderDate() {
        io.print("* * * * Create New Order * * * * * * *");
        String newOrderDateString;

        // Get new Order date
        boolean keepGoing = false;
        do {
            newOrderDateString = io.readString("Enter the Order date.  Date must be a future date and in the following format (ex: 08/03/2022)").trim();
            if (checkValidDateFormat(newOrderDateString)) {
                keepGoing = false;
            } else {
                keepGoing = true;
                continue;
            }

            if (checkIsFutureDate((newOrderDateString)) ) {
                keepGoing = false;
            } else {
                keepGoing = true;
                continue;
            }
        } while (keepGoing);

        return LocalDate.parse(newOrderDateString, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    private boolean checkValidDateFormat(String newOrderDateString) {
        DateTimeFormatter datePattern = DateTimeFormatter.ofPattern(DATE_FORMAT);
        try {
            LocalDate.parse(newOrderDateString, datePattern);
        } catch (DateTimeParseException e) {
            displayErrorMessage("Invalid date format.  Please use the correct date format.");
            return false;
        }
        return true;
    }

    private boolean checkIsFutureDate(String newOrderDateString) {
        LocalDate newOrderDate = LocalDate.parse(newOrderDateString, DateTimeFormatter.ofPattern(DATE_FORMAT));
        LocalDate todayDate = LocalDate.now();
        if (newOrderDate.isAfter(todayDate)) {
            return true;
        } else {
            displayErrorMessage("Order date must be at least 1 day in the future from today's date, " + todayDate);
            return false;
        }
    }

    public String getNewOrderCustomerName() {
        // Get Customer name
        String newOrderCustomerName;
        boolean keepGoing = false;
        do {
            newOrderCustomerName = io.readString("Enter the Customer name for the order.").trim();
            if (checkValidCustomerNameFormat((newOrderCustomerName))) {
                keepGoing = false;
            } else {
                keepGoing = true;
            }
        } while (keepGoing);
        return newOrderCustomerName;
    }

    private boolean checkValidCustomerNameFormat(String userEnteredName) {
        if (userEnteredName.length() < 1) {
            displayErrorMessage("Invalid name.  Please only use letters, numbers 0-9, and periods & commas.");
            return false;
        }
        final String regex = "\\A[a-zA-Z\\d.,\\s]*\\z";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(userEnteredName);
        if (matcher.find()) {
            return true;
        } else {
            displayErrorMessage("Invalid name.  Please only use letters, numbers 0-9, and periods & commas.");
            return false;
        }
    }

    public Tax getNewOrderTax(List<Tax> allTaxes) {
        Tax userSelectedTax;
        int userStateSelectionInt;

        // Get State for Order
        int countStates = 0;
        for (Tax tax : allTaxes) {
            String states = String.format("%s. %s",
                    ++countStates,
                    tax.getStateName());
            io.print(states);
        }
        userStateSelectionInt = io.readInt("Please select the State for the Order from the following States:", 1, countStates);
        return allTaxes.get(--userStateSelectionInt);
    }

    public Product getNewOrderProduct(List<Product> allProducts) {
        int userProductSelectionInt;

        // Get Product for Order
        int countProducts = 0;
        for (Product product : allProducts) {
            String products = String.format("%s. %s",
                    ++countProducts,
                    product.getProductType());
            io.print(products);
        }
        userProductSelectionInt = io.readInt("Please select from the following Products:", 1, countProducts);
        return allProducts.get(--userProductSelectionInt);
    }

    public BigDecimal getNewOrderArea() {
        // Get Area for Order
        Double userAreaEnteredDouble = io.readDouble("Enter the total Area in sq/ft required for the Order (must be 100 sq/ft or GREATER): ", 100.00, Double.POSITIVE_INFINITY);
        String userAreaEnteredString = String.valueOf(userAreaEnteredDouble);
        return new BigDecimal(userAreaEnteredString).setScale(4, RoundingMode.HALF_UP);
    }

    public boolean confirmOrder(BigDecimal newOrderTotal) {
        io.print("The total cost for the Order is: $" + newOrderTotal);
        boolean keepGoing = false;
        boolean decision = false;
        do {
            String userConfirmSelection = io.readString("Would you like to submit the order?  Type 'Y' for Yes or 'N' for No");
            if (userConfirmSelection.equalsIgnoreCase("y")) {
                io.print("Order created successfully.");
                decision = true;
                keepGoing = false;
            } else if (userConfirmSelection.equalsIgnoreCase("n")) {
                decision = false;
                keepGoing = false;
            } else {
                keepGoing = true;
            }
        } while (keepGoing);
        return decision;
    }

    public void displayErrorMessage(String errorMsg) {
        io.print("=== ERROR ===");
        io.print(errorMsg);
        io.print("");
    }

    public void displayExitBanner() {
        io.print("Have a good day!!");
    }
}
