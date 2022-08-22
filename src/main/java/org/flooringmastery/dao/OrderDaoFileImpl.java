package org.flooringmastery.dao;

import org.flooringmastery.dto.Order;
import org.flooringmastery.dto.Product;
import org.flooringmastery.dto.Tax;

import java.io.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDaoFileImpl implements OrderDao {

    private final String DELIMITER = ",";

    private Map<Integer, Order> orders = new HashMap<>();

    private TaxDao taxDao = new TaxDaoFileImpl();

    private ProductDao productDao = new ProductDaoFileImpl();

    public List<Order> getAllOrders(String orderDate) throws FlooringMasteryPersistenceException {
        String orderDateFormatted = orderDate.replace("/", "");
        if (loadOrders(orderDateFormatted)) {
            return new ArrayList<>(orders.values());
        } else {
            return new ArrayList<>();
        }
    }

    public Order addOrder(Order newOrder) throws FlooringMasteryPersistenceException {
        String formattedDate = newOrder.getOrderDate().format(DateTimeFormatter.ofPattern("MMddyyyy"));
        orders.put(newOrder.getOrderNumber(), newOrder);
        String ordersFileName = "orders/Orders_" + formattedDate + ".txt";
        writeOrder(ordersFileName);
        return newOrder;
    }

    private boolean loadOrders(String orderDate) throws FlooringMasteryPersistenceException {
        orders = new HashMap<>();
        Scanner scanner = null;
        String ordersFileName = "orders/Orders_" + orderDate + ".txt";
        boolean wasSuccessful = true;
        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(ordersFileName)));
        } catch (FileNotFoundException e) {
            System.out.println("No orders exist for this date or a new Order date was created.");
            wasSuccessful = false;
        }

        if (wasSuccessful == false) {
            return false;
        } else {
            // currentItem holds the most recent line read from the file
            String currentLine;
            // currentItem holds the most recent Order unmarshalled
            Order currentOrder;
            // skips the Header line
            scanner.nextLine();
            // Go through ORDERS_FILE line by line, decoding each line into a
            // Item object by calling the unmarshallOrder method.
            // Process while we have more lines in the file

            while (scanner.hasNextLine()) {
                // get the next line in the file
                currentLine = scanner.nextLine();
                // unmarshall the line into a Student
                currentOrder = unmarshallOrder(currentLine);

                // We are going to use the student id as the map key for our student object.
                // Put currentStudent into the map using student id as the key
                orders.put(currentOrder.getOrderNumber(), currentOrder);
            }
            // close scanner
            scanner.close();
            return true;
        }
    }

    private String marshallOrder(Order anOrder){
        // We need to turn a Student object into a line of text for our file.
        // For example, we need an in memory object to end up like this:
        // 4321::Charles::Babbage::Java-September1842

        // It's not a complicated process. Just get out each property,
        // and concatenate with our DELIMITER as a kind of spacer.

        // Start with the student id, since that's supposed to be first.
        String orderAsText = anOrder.getOrderNumber() + DELIMITER;

        // add the rest of the properties in the correct order:

        // CustomerName
        orderAsText += anOrder.getCustomerName() + DELIMITER;

        // State
        orderAsText += anOrder.getTax().getStateAbbreviation() + DELIMITER;

        // TaxRate
        orderAsText += anOrder.getTax().getTaxRate() + DELIMITER;

        // ProductType
        orderAsText += anOrder.getProduct().getProductType() + DELIMITER;

        // Area
        orderAsText += anOrder.getArea() + DELIMITER;

        // CostPerSqFoot
        orderAsText += anOrder.getProduct().getCostPerSquareFoot() + DELIMITER;

        // LaborCostPerSqFoot
        orderAsText += anOrder.getProduct().getLaborCostPerSquareFoot() + DELIMITER;

        // MaterialCost
        orderAsText += anOrder.getMaterialCost() + DELIMITER;

        // LaborCost
        orderAsText += anOrder.getLaborCost() + DELIMITER;

        // Tax
        orderAsText += anOrder.getTaxTotal() + DELIMITER;

        // Total
        orderAsText += anOrder.getTotalCost();

        // We have now turned a student to text! Return it!
        return orderAsText;
    }

    private Order unmarshallOrder(String orderAsText) throws FlooringMasteryPersistenceException {
        // Creates Order from String
        // Parses Item into String Array like the following:
        // id::name::count::price
        //
        // Example
        // ____________________________
        // |     |        |    |      |
        // | 0001|Twinkies| 10 | 1.50 |
        // |     |        |    |      |
        // ----------------------------
        //  [0]      [1]   [2]    [3]
        String[] orderTokens = orderAsText.split(DELIMITER);

        int orderNumber = Integer.parseInt(orderTokens[0]);
        String customerName = orderTokens[1];
        String stateAbbrev = orderTokens[2];
        String productType = orderTokens[4];
        BigDecimal area = new BigDecimal(orderTokens[5]);
        BigDecimal materialCost = new BigDecimal(orderTokens[8]);
        BigDecimal laborCost = new BigDecimal(orderTokens[9]);
        BigDecimal tax = new BigDecimal(orderTokens[10]);
        BigDecimal total = new BigDecimal(orderTokens[11]);

        Tax newOrderTax = taxDao.getIndividualTax(stateAbbrev);
        Product newOrderProduct = productDao.getIndividualProduct(productType);


        Order newOrderFromFile = new Order();
        newOrderFromFile.setOrderNumber(orderNumber);
        newOrderFromFile.setCustomerName(customerName);
        newOrderFromFile.setTax(newOrderTax);
        newOrderFromFile.setProduct(newOrderProduct);
        newOrderFromFile.setArea(area);
        newOrderFromFile.setMaterialCost(materialCost);
        newOrderFromFile.setLaborCost(laborCost);
        newOrderFromFile.setTaxTotal(tax);
        newOrderFromFile.setTotalCost(total);

        // Return new Order
        return newOrderFromFile;
    }

    private void writeOrder(String orderFileName) throws FlooringMasteryPersistenceException {
        // NOTE FOR APPRENTICES: We are not handling the IOException - but
        // we are translating it to an application specific exception and
        // then simple throwing it (i.e. 'reporting' it) to the code that
        // called us.  It is the responsibility of the calling code to
        // handle any errors that occur.
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(orderFileName));
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException(
                    "Could not create Order file.", e);
        }

        // Write out the Student objects to the roster file.
        // NOTE TO THE APPRENTICES: We could just grab the student map,
        // get the Collection of Students and iterate over them but we've
        // already created a method that gets a List of Students so
        // we'll reuse it.
        String orderFileHeader = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total";
        out.println(orderFileHeader);
        String orderAsText ;
        for (Order currentOrder : orders.values()) {
            // turn a Student into a String
            orderAsText = marshallOrder(currentOrder);
            // write the Student object to the file
            out.println(orderAsText);
            // force PrintWriter to write line to the file
            out.flush();
        }
        // Clean up
        out.close();
    }

//    public Order retrieveOrder(int orderNumber, String orderDate) {
//        loadOrders(orderDate);
//    }
}
