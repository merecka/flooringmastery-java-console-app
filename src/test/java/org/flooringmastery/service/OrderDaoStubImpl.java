package org.flooringmastery.service;

import org.flooringmastery.dao.FlooringMasteryPersistenceException;
import org.flooringmastery.dao.OrderDao;
import org.flooringmastery.dto.Order;
import org.flooringmastery.dto.Product;
import org.flooringmastery.dto.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoStubImpl implements OrderDao {

    private String ORDER_DATE_STRING = "12011980";

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMddyyyy");
    private LocalDate localDateOrderDate = LocalDate.parse(ORDER_DATE_STRING, dateFormatter);

    Tax tax1 = new Tax("TX", "Texas", new BigDecimal("4.45"));
    Tax tax2 = new Tax("WA", "Washington", new BigDecimal("9.25"));
    Tax tax3 = new Tax("KY", "Kentucky", new BigDecimal("6.00"));
    Tax tax4 = new Tax("CA", "California", new BigDecimal("25.00"));

    Product product1 = new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10"));
    Product product2 = new Product("Laminate", new BigDecimal("1.75"), new BigDecimal("2.10"));
    Product product3 = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));
    Product product4 = new Product("Wood", new BigDecimal("5.15"), new BigDecimal("4.75"));

    ArrayList<Order> itemList = new ArrayList<>();

    public OrderDaoStubImpl() {
        Order order1 = new Order();
        Order order2 = new Order();
        Order order3 = new Order();
        Order order4 = new Order();

        order1.setOrderDate(localDateOrderDate);
        order1.setOrderNumber(1);
        order1.setProduct(product4);
        order1.setTax(tax1);
        order1.setCustomerName("Alex");
        order1.setArea(new BigDecimal("400"));


        order2.setOrderDate(localDateOrderDate);
        order2.setOrderNumber(2);
        order2.setProduct(product2);
        order2.setTax(tax2);
        order2.setCustomerName("William");
        order2.setArea(new BigDecimal("350"));


        order3.setOrderDate(localDateOrderDate);
        order3.setOrderNumber(3);
        order3.setProduct(product3);
        order3.setTax(tax3);
        order3.setCustomerName("Jim");
        order3.setArea(new BigDecimal("550"));


        order4.setOrderDate(localDateOrderDate);
        order4.setOrderNumber(4);
        order4.setProduct(product1);
        order4.setTax(tax4);
        order4.setCustomerName("Bob");
        order4.setArea(new BigDecimal("560"));

        itemList.add(order1);
        itemList.add(order2);
        itemList.add(order3);
        itemList.add(order4);
    }

    public List<Order> getAllOrders(String orderDate) {
        return itemList;
    }

    public void addOrder(Order newOrder) {}

    public Order getOrder(String orderDate, int orderNumber) {
        return itemList.get(orderNumber - 1);
    }

    public void removeOrder(Order deletedOrder) {}
}
