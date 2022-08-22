package org.flooringmastery.ui;

import org.flooringmastery.dto.Order;
import org.flooringmastery.dto.Product;
import org.flooringmastery.dto.Tax;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlooringMasteryView {

    private UserIO io;

    private Order newOrder;

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
                    currentOrder.getTax().getStateName(),
                    currentOrder.getTax().getTaxRate(),
                    currentOrder.getProduct().getProductType(),
                    currentOrder.getArea(),
                    currentOrder.getProduct().getCostPerSquareFoot(),
                    currentOrder.getProduct().getLaborCostPerSquareFoot(),
                    currentOrder.getMaterialCost(),
                    currentOrder.getLaborCost(),
                    currentOrder.getTaxTotal(),
                    currentOrder.getTotalCost());
            io.print(orderInfo);
        }
    }

    public Order startNewOrder() {
        io.print("* * * * Create New Order * * * * * * *");
        io.print("");
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

            if (checkIsFutureDate((newOrderDateString))) {
                keepGoing = false;
            } else {
                keepGoing = true;
            }
        } while (keepGoing);

        LocalDate newOrderDate = LocalDate.parse(newOrderDateString, DateTimeFormatter.ofPattern(DATE_FORMAT));
        newOrder = new Order();
        newOrder.setOrderDate(newOrderDate);
        return newOrder;
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

    public void getNewOrderCustomerName(boolean isEditOrder) {
        // Get Customer name
        String newOrderCustomerName;
        boolean keepGoing = false;
        do {
            newOrderCustomerName = io.readString("Enter the Customer name for the order.").trim();
            if (isEditOrder && newOrderCustomerName.trim().equals("")) {
                return;
            }
            if (checkValidCustomerNameFormat(newOrderCustomerName, isEditOrder)) {
                keepGoing = false;
            } else {
                keepGoing = true;
            }
        } while (keepGoing);
        newOrder.setCustomerName(newOrderCustomerName);
    }

    private boolean checkValidCustomerNameFormat(String userEnteredName, boolean isEdit) {
        if (!isEdit && userEnteredName.length() < 1) {
            displayErrorMessage("Customer name cannot be blank.");
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

    public void getNewOrderTax(List<Tax> allTaxes) {
        int userStateSelectionInt;
        Tax newOrderTax;

        // Get State for Order
        int countStates = 0;
        for (Tax tax : allTaxes) {
            String states = String.format("%s. %s",
                    ++countStates,
                    tax.getStateName());
            io.print(states);
        }
        userStateSelectionInt = io.readInt("Please select the State for the Order from the following States:", 1, countStates);
        newOrderTax = allTaxes.get(--userStateSelectionInt);
        newOrder.setTax(newOrderTax);
    }

    public void getNewOrderProduct(List<Product> allProducts) {
        int userProductSelectionInt;
        Product newOrderProduct;

        // Get Product for Order
        int countProducts = 0;
        for (Product product : allProducts) {
            String products = String.format("%s. %s",
                    ++countProducts,
                    product.getProductType());
            io.print(products);
        }
        userProductSelectionInt = io.readInt("Please select from the following Products:", 1, countProducts);
        newOrderProduct = allProducts.get(--userProductSelectionInt);
        newOrder.setProduct(newOrderProduct);
    }

    public void getNewOrderArea() {
        BigDecimal newOrderArea;
        // Get Area for Order
        Double userAreaEnteredDouble = io.readDouble("Enter the total Area in sq/ft required for the Order (must be 100 sq/ft or GREATER): ", 100.00, Double.POSITIVE_INFINITY);
        String userAreaEnteredString = String.valueOf(userAreaEnteredDouble);
        newOrderArea = new BigDecimal(userAreaEnteredString).setScale(4, RoundingMode.HALF_UP);
        newOrder.setArea(newOrderArea);
    }

    public boolean confirmOrder(Order newOrder) {
        io.print("The total cost for the Order is: $" + newOrder.getTotalCost());
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

    public LocalDate getEditOrderDate() {
        io.print("* * * * Edit Order * * * * * * *");
        io.print("");
        String editOrderDateString;

        // Get edit Order date
        boolean keepGoing = false;
        do {
            editOrderDateString = io.readString("Enter the Order date.  Date must be in the following format (ex: 08/03/2022)").trim();
            if (checkValidDateFormat(editOrderDateString)) {
                keepGoing = false;
            } else {
                keepGoing = true;
                continue;
            }

            if (checkIsFutureDate((editOrderDateString))) {
                keepGoing = false;
            } else {
                keepGoing = true;
                continue;
            }
        } while (keepGoing);

        return LocalDate.parse(editOrderDateString, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public int getEditOrderNumber() {
        return io.readInt("Please enter the Order number you wish to edit.", 1, 99999999);
    }

    public Tax getEditOrderTax(List<Tax> allTaxes) throws NullPointerException, NumberFormatException {
        String userStateSelectionString;
        boolean keepGoing = false;
        Tax editedTax = null;

        do {
            // Get State for Order
            int countStates = 0;
            for (Tax tax : allTaxes) {
                String states = String.format("%s. %s",
                        ++countStates,
                        tax.getStateName());
                io.print(states);
            }
            userStateSelectionString = io.readString("Please select the State for the Order from the following States:");
            try {
                int userStateSelectionInt = Integer.parseInt(userStateSelectionString);
                if (userStateSelectionInt < 1 || userStateSelectionInt > allTaxes.size()) {
                    io.print("You must either make a selection from the list or hit Enter to keep the existing State.");
                    keepGoing = true;
                } else {
                    editedTax = allTaxes.get(--userStateSelectionInt);
                    keepGoing = false;
                }
            } catch (NullPointerException | NumberFormatException e) {
                keepGoing = false;
            }
        } while (keepGoing);
        return editedTax;
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
