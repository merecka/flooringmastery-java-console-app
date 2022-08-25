package org.flooringmastery.service;

import org.flooringmastery.dao.*;
import org.flooringmastery.dto.Order;
import org.flooringmastery.dto.Product;
import org.flooringmastery.dto.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlooringMasteryServiceLayerTest {

    private FlooringMasteryServiceLayer service;

    private TaxDao taxDao;

    private ProductDao productDao;

    private OrderDao orderDao;

    private OrderDaoStubImpl orderDaoStub;

    private String ORDER_DATE_STRING = "12/01/1980";

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private LocalDate localDateOrderDate = LocalDate.parse(ORDER_DATE_STRING, dateFormatter);

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

    public FlooringMasteryServiceLayerTest() {
        taxDao = new TaxDaoFileImpl();
        productDao = new ProductDaoFileImpl();
        orderDao = new OrderDaoFileImpl();
        service = new FlooringMasteryServiceLayer(orderDao, taxDao, productDao);

    }

    @BeforeEach
    void setUp() throws FlooringMasteryPersistenceException {
        order1.setOrderDate(localDateOrderDate);
        order1.setOrderNumber(1);
        order1.setProduct(product4);
        order1.setTax(tax1);
        order1.setCustomerName("Alex");
        order1.setArea(new BigDecimal("400"));
        service.createOrder(order1);

        order2.setOrderDate(localDateOrderDate);
        order1.setOrderNumber(2);
        order2.setProduct(product2);
        order2.setTax(tax2);
        order2.setCustomerName("William");
        order2.setArea(new BigDecimal("350"));
        service.createOrder(order2);

        order3.setOrderDate(localDateOrderDate);
        order1.setOrderNumber(3);
        order3.setProduct(product3);
        order3.setTax(tax3);
        order3.setCustomerName("Jim");
        order3.setArea(new BigDecimal("550"));
        service.createOrder(order3);

        order4.setOrderDate(localDateOrderDate);
        order1.setOrderNumber(4);
        order4.setProduct(product1);
        order4.setTax(tax4);
        order4.setCustomerName("Bob");
        order4.setArea(new BigDecimal("560"));
        service.createOrder(order4);
    }

    @Test
    void calculateOrderMaterialCost() {
        service.calculateOrderMaterialCost(order1);
        service.calculateOrderMaterialCost(order2);
        service.calculateOrderMaterialCost(order3);
        service.calculateOrderMaterialCost(order4);

        assertTrue(new BigDecimal("2060.00").compareTo(order1.getMaterialCost()) == 0);
        assertTrue(new BigDecimal("612.50").compareTo(order2.getMaterialCost()) == 0);
        assertTrue(new BigDecimal("1925.00").compareTo(order3.getMaterialCost()) == 0);
        assertTrue(new BigDecimal("1260.00").compareTo(order4.getMaterialCost()) == 0);
    }

    @Test
    void calculateOrderLaborCost() {
        service.calculateOrderLaborCost(order1);
        service.calculateOrderLaborCost(order2);
        service.calculateOrderLaborCost(order3);
        service.calculateOrderLaborCost(order4);

        assertTrue(new BigDecimal("1900.00").compareTo(order1.getLaborCost()) == 0);
        assertTrue(new BigDecimal("735.00").compareTo(order2.getLaborCost()) == 0);
        assertTrue(new BigDecimal("2282.50").compareTo(order3.getLaborCost()) == 0);
        assertTrue(new BigDecimal("1176.00").compareTo(order4.getLaborCost()) == 0);
    }

    @Test
    void calculateOrderTax() {
        service.calculateOrderMaterialCost(order1);
        service.calculateOrderLaborCost(order1);
        service.calculateOrderTax(order1);

        service.calculateOrderMaterialCost(order2);
        service.calculateOrderLaborCost(order2);
        service.calculateOrderTax(order2);

        service.calculateOrderMaterialCost(order3);
        service.calculateOrderLaborCost(order3);
        service.calculateOrderTax(order3);

        service.calculateOrderMaterialCost(order4);
        service.calculateOrderLaborCost(order4);
        service.calculateOrderTax(order4);

        assertTrue(new BigDecimal("158.40").compareTo(order1.getTaxTotal()) == 0);
        assertTrue(new BigDecimal("121.28").compareTo(order2.getTaxTotal()) == 0);
        assertTrue(new BigDecimal("252.45").compareTo(order3.getTaxTotal()) == 0);
        assertTrue(new BigDecimal("609.00").compareTo(order4.getTaxTotal()) == 0);
    }

    @Test
    void calculateTotalOrderCost() {
        service.calculateOrderMaterialCost(order1);
        service.calculateOrderLaborCost(order1);
        service.calculateOrderTax(order1);
        service.calculateTotalOrderCost(order1);

        service.calculateOrderMaterialCost(order2);
        service.calculateOrderLaborCost(order2);
        service.calculateOrderTax(order2);
        service.calculateTotalOrderCost(order2);

        service.calculateOrderMaterialCost(order3);
        service.calculateOrderLaborCost(order3);
        service.calculateOrderTax(order3);
        service.calculateTotalOrderCost(order3);

        service.calculateOrderMaterialCost(order4);
        service.calculateOrderLaborCost(order4);
        service.calculateOrderTax(order4);
        service.calculateTotalOrderCost(order4);

        assertTrue(new BigDecimal("4118.40").compareTo(order1.getTotalCost()) == 0);
        assertTrue(new BigDecimal("1468.78").compareTo(order2.getTotalCost()) == 0);
        assertTrue(new BigDecimal("4459.95").compareTo(order3.getTotalCost()) == 0);
        assertTrue(new BigDecimal("3045.00").compareTo(order4.getTotalCost()) == 0);
    }

    @Test
    void getNewOrderNumber() throws FlooringMasteryPersistenceException {
        // Add a new Order to an existing file with 4 other Orders
        orderDaoStub = new OrderDaoStubImpl();
        FlooringMasteryServiceLayer newService = new FlooringMasteryServiceLayer(orderDaoStub, taxDao, productDao);

        Order order5 = new Order();
        order5.setOrderDate(localDateOrderDate);
        order5.setProduct(product3);
        order5.setTax(tax2);
        order5.setCustomerName("Joe");
        order5.setArea(new BigDecimal("525"));
//        service.calculateOrderMaterialCost(order5);
//        service.calculateOrderLaborCost(order5);
//        service.calculateOrderTax(order5);
//        service.calculateTotalOrderCost(order5);
//        service.createOrder(order5);
        newService.calculateOrderMaterialCost(order5);
        newService.calculateOrderLaborCost(order5);
        newService.calculateOrderTax(order5);
        newService.calculateTotalOrderCost(order5);
        newService.createOrder(order5);
//        service.getNewOrderNumber(order5);
        newService.getNewOrderNumber(order5);

        assertTrue(order5.getOrderNumber() == 5);
    }
}