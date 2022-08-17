package org.flooringmastery.service;

import org.flooringmastery.dao.FlooringMasteryPersistenceException;
import org.flooringmastery.dao.OrderDao;
import org.flooringmastery.dto.Order;

import java.util.List;

public class FlooringMasteryServiceLayer {

    private OrderDao orderDao;

    public FlooringMasteryServiceLayer(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public List<Order> allOrdersForDate(String orderDate) throws FlooringMasteryPersistenceException {
       List<Order> allOrders = orderDao.getAllOrders(orderDate);
       return allOrders;
    }
}
