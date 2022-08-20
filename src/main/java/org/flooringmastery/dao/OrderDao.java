package org.flooringmastery.dao;

import org.flooringmastery.dto.Order;

import java.util.List;

public interface OrderDao {

    public List<Order> getAllOrders(String orderDate);

    public Order addOrder(Order newOrder) throws FlooringMasteryPersistenceException;
}
