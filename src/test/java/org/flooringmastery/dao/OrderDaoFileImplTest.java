package org.flooringmastery.dao;

import org.flooringmastery.dto.Order;
import org.flooringmastery.dto.Product;
import org.flooringmastery.dto.Tax;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDaoFileImplTest {

    OrderDaoFileImpl testOrderDao;

    private final String DELIMITER = ",";

    private String ORDER_DATE_STRING = "12011980";

    private LocalDate orderDate = LocalDate.parse(ORDER_DATE_STRING, DateTimeFormatter.ofPattern("MMddyyyy"));

    Tax tax1 = new Tax("TX", "Texas", new BigDecimal("4.45"));
    Tax tax2 = new Tax("WA", "Washington", new BigDecimal("9.25"));
    Tax tax3 = new Tax("KY", "Kentucky", new BigDecimal("6.00"));
    Tax tax4 = new Tax("CA", "California", new BigDecimal("25.00"));

    Product product1 = new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10"));
    Product product2 = new Product("Laminate", new BigDecimal("1.75"), new BigDecimal("2.10"));
    Product product3 = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));
    Product product4 = new Product("Wood", new BigDecimal("5.15"), new BigDecimal("4.75"));

    Order order1 = new Order();
    Order order2 = new Order();
    Order order3 = new Order();
    Order order4 = new Order();
    Order order5 = new Order();

    Order[] testOrders = {order1, order2, order3, order4};

    @BeforeEach
    void setUp() throws FlooringMasteryPersistenceException {
        testOrderDao = new OrderDaoFileImpl();

        order1.setOrderDate(orderDate);
        order1.setOrderNumber(1);
        order1.setProduct(product4);
        order1.setTax(tax1);
        order1.setCustomerName("Alex");
        order1.setArea(new BigDecimal("400"));
        order1.setMaterialCost(new BigDecimal("2060.00"));
        order1.setLaborCost(new BigDecimal("1900.00"));
        order1.setTaxTotal(new BigDecimal("158.40"));
        order1.setTotalCost(new BigDecimal("4118.40"));
        testOrderDao.addOrder(order1);

        order2.setOrderDate(orderDate);
        order2.setOrderNumber(2);
        order2.setProduct(product2);
        order2.setTax(tax2);
        order2.setCustomerName("William");
        order2.setArea(new BigDecimal("350"));
        order2.setMaterialCost(new BigDecimal("612.50"));
        order2.setLaborCost(new BigDecimal("735.00"));
        order2.setTaxTotal(new BigDecimal("121.28"));
        order2.setTotalCost(new BigDecimal("1468.78"));
        testOrderDao.addOrder(order2);

        order3.setOrderDate(orderDate);
        order3.setOrderNumber(3);
        order3.setProduct(product3);
        order3.setTax(tax3);
        order3.setCustomerName("Jim");
        order3.setArea(new BigDecimal("550"));
        order3.setMaterialCost(new BigDecimal("1925.00"));
        order3.setLaborCost(new BigDecimal("2282.50"));
        order3.setTaxTotal(new BigDecimal("252.45"));
        order3.setTotalCost(new BigDecimal("4459.95"));
        testOrderDao.addOrder(order3);

        order4.setOrderDate(orderDate);
        order4.setOrderNumber(4);
        order4.setProduct(product1);
        order4.setTax(tax4);
        order4.setCustomerName("Bob");
        order4.setArea(new BigDecimal("560"));
        order4.setMaterialCost(new BigDecimal("1260.00"));
        order4.setLaborCost(new BigDecimal("1176.00"));
        order4.setTaxTotal(new BigDecimal("609.00"));
        order4.setTotalCost(new BigDecimal("3045.00"));
        testOrderDao.addOrder(order4);
    }

    @Test
    void getAllOrders() throws FlooringMasteryPersistenceException {
        List<Order> allOrders = testOrderDao.getAllOrders(ORDER_DATE_STRING);
        assertNotNull(allOrders, "The list of Orders must not null");
        assertEquals(4, allOrders.size(), "List of Orders should have 4 items.");

        assertTrue(allOrders.contains(order1),
                "The list of items should include Order 1.");
        assertTrue(allOrders.contains(order2),
                "The list of items should include Order 2.");
        assertTrue(allOrders.contains(order3),
                "The list of items should include Order 3.");
        assertTrue(allOrders.contains(order4),
                "The list of items should include Order 4.");
    }

    @Test
    void getOrder() throws FlooringMasteryPersistenceException {
        Order retrievedOrder1 = testOrderDao.getOrder(ORDER_DATE_STRING, 1);
        Order retrievedOrder2 = testOrderDao.getOrder(ORDER_DATE_STRING, 2);
        Order retrievedOrder3 = testOrderDao.getOrder(ORDER_DATE_STRING, 3);
        Order retrievedOrder4 = testOrderDao.getOrder(ORDER_DATE_STRING, 4);

        assertNotNull(retrievedOrder1, "The order1 order must not be null");
        assertNotNull(retrievedOrder2, "The order2 order must not be null");
        assertNotNull(retrievedOrder3, "The order3 order must not be null");
        assertNotNull(retrievedOrder4, "The order4 order must not be null");

        assertTrue(retrievedOrder1.equals(order1),
                "The Order1 should be Carpet.");
        assertTrue(retrievedOrder2.equals(order2),
                "The Object should be Laminate.");
        assertTrue(retrievedOrder3.equals(order3),
                "The Object should be Tile.");
        assertTrue(retrievedOrder4.equals(order4),
                "The Object should be Wood.");
    }

    @Test
    void addOrder() throws FlooringMasteryPersistenceException {
        order5.setOrderDate(orderDate);
        order5.setOrderNumber(5);
        order5.setProduct(product2);
        order5.setTax(tax3);
        order5.setCustomerName("Josh");
        order5.setArea(new BigDecimal("675.00"));
        order5.setMaterialCost(new BigDecimal("1181.25"));
        order5.setLaborCost(new BigDecimal("1417.50"));
        order5.setTaxTotal(new BigDecimal("155.93"));
        order5.setTotalCost(new BigDecimal("2754.68"));
        testOrderDao.addOrder(order5);

        Order retrievedOrder5 = testOrderDao.getOrder(ORDER_DATE_STRING, 5);

        assertNotNull(retrievedOrder5, "The added order must not be null");
        assertTrue(retrievedOrder5.equals(order5),
                "The added order should be Laminate.");
    }

    @Test
    void removeOrder() throws FlooringMasteryPersistenceException {
        testOrderDao.removeOrder(order3);
        List<Order> allOrders = testOrderDao.getAllOrders(ORDER_DATE_STRING);
        Order retrievedOrder3 = testOrderDao.getOrder(ORDER_DATE_STRING, 3);

        assertEquals(3, allOrders.size(), "List of Orders should have 3 items.");
        assertNull(retrievedOrder3, "Removed order should not be present in Orders list.");
    }
}