package org.flooringmastery.dao;

import org.flooringmastery.dto.Order;

import java.util.List;

public interface OrderDao {

    List<Order> getAllOrders(String orderDate) throws FlooringMasteryPersistenceException;

    void addOrder(Order newOrder) throws FlooringMasteryPersistenceException;

    Order getOrder(String orderDate, int orderNumber) throws FlooringMasteryPersistenceException;
}
