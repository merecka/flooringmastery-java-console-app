package org.flooringmastery.service;

import org.flooringmastery.dao.*;
import org.flooringmastery.dto.Order;
import org.flooringmastery.dto.Product;
import org.flooringmastery.dto.Tax;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    Tax tax2 = new Tax("WA", "Washington", new BigDecimal("9.25"));
    Product product3 = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));

    Order order1;
    Order order2;
    Order order3;
    Order order4;

    public FlooringMasteryServiceLayerTest() throws FlooringMasteryPersistenceException {
        taxDao = new TaxDaoFileImpl();
        productDao = new ProductDaoFileImpl();
        orderDao = new OrderDaoStubImpl();
        service = new FlooringMasteryServiceLayer(orderDao, taxDao, productDao);

        order1 = orderDao.getOrder(ORDER_DATE_STRING, 1);
        order2 = orderDao.getOrder(ORDER_DATE_STRING, 2);
        order3 = orderDao.getOrder(ORDER_DATE_STRING, 3);
        order4 = orderDao.getOrder(ORDER_DATE_STRING, 4);

    }

    @Test
    void calculateOrderMaterialCost() {
        service.calculateOrderMaterialCost(order1);
        service.calculateOrderMaterialCost(order2);
        service.calculateOrderMaterialCost(order3);
        service.calculateOrderMaterialCost(order4);

        assertEquals(new BigDecimal("2060.00"), order1.getMaterialCost());
        assertEquals(new BigDecimal("612.50"), order2.getMaterialCost());
        assertEquals(new BigDecimal("1925.00"), order3.getMaterialCost());
        assertEquals(new BigDecimal("1260.00"), order4.getMaterialCost());
    }

    @Test
    void calculateOrderLaborCost() {
        service.calculateOrderLaborCost(order1);
        service.calculateOrderLaborCost(order2);
        service.calculateOrderLaborCost(order3);
        service.calculateOrderLaborCost(order4);

        assertEquals(new BigDecimal("1900.00"), order1.getLaborCost());
        assertEquals(new BigDecimal("735.00"), order2.getLaborCost());
        assertEquals(new BigDecimal("2282.50"), order3.getLaborCost());
        assertEquals(new BigDecimal("1176.00"), order4.getLaborCost());
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

        assertEquals(new BigDecimal("158.40"), order1.getTaxTotal());
        assertEquals(new BigDecimal("121.28"), order2.getTaxTotal());
        assertEquals(new BigDecimal("252.45"), order3.getTaxTotal());
        assertEquals(new BigDecimal("609.00"), order4.getTaxTotal());
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

        assertEquals(new BigDecimal("4118.40"), order1.getTotalCost());
        assertEquals(new BigDecimal("1468.78"), order2.getTotalCost());
        assertEquals(new BigDecimal("4459.95"), order3.getTotalCost());
        assertEquals(new BigDecimal("3045.00"), order4.getTotalCost());
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
        newService.calculateOrderMaterialCost(order5);
        newService.calculateOrderLaborCost(order5);
        newService.calculateOrderTax(order5);
        newService.calculateTotalOrderCost(order5);
        newService.getNewOrderNumber(order5);

        assertEquals(order5.getOrderNumber(),5);
    }
}