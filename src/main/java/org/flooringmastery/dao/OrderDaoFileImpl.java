package org.flooringmastery.dao;

import org.flooringmastery.dto.Order;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class OrderDaoFileImpl implements OrderDao {

    private final String DELIMITER = ",";

    private Map<Integer, Order> orders = new HashMap<>();

    public List<Order> getAllOrders(String orderDate) throws FlooringMasteryPersistenceException {
        String orderDateFormatted = orderDate.replace("/", "");
        System.out.println(orderDateFormatted);
        loadOrders(orderDateFormatted);
        return new ArrayList<>(orders.values());
    }

    private void loadOrders(String orderDate) throws FlooringMasteryPersistenceException {
        Scanner scanner;
        String ordersFileName = "data/Orders_" + orderDate + ".txt";
        System.out.println(ordersFileName);

        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(ordersFileName)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException(
                    "-_- Could not load Orders data into memory.", e);
        }
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
    }

    private Order unmarshallOrder(String orderAsText){
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
        String state = orderTokens[2];
        BigDecimal taxRate = new BigDecimal(orderTokens[3]);
        String productType = orderTokens[4];
        BigDecimal area = new BigDecimal(orderTokens[5]);
        BigDecimal costPerSquareFoot = new BigDecimal(orderTokens[6]);
        BigDecimal laborCostPerSquareFoot = new BigDecimal(orderTokens[7]);
        BigDecimal materialCost = new BigDecimal(orderTokens[8]);
        BigDecimal laborCost = new BigDecimal(orderTokens[9]);
        BigDecimal tax = new BigDecimal(orderTokens[10]);
        BigDecimal total = new BigDecimal(orderTokens[11]);


        Order newOrderFromFile = new Order(orderNumber, customerName, state, taxRate, productType, area, costPerSquareFoot, laborCostPerSquareFoot, materialCost, laborCost, tax, total);

        // Return new Order
        return newOrderFromFile;
    }
}
