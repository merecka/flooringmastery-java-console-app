package org.flooringmastery.service;

import org.flooringmastery.dao.FlooringMasteryPersistenceException;
import org.flooringmastery.dao.OrderDao;
import org.flooringmastery.dao.ProductDao;
import org.flooringmastery.dao.TaxDao;
import org.flooringmastery.dto.Order;
import org.flooringmastery.dto.Product;
import org.flooringmastery.dto.Tax;

import java.util.List;

public class FlooringMasteryServiceLayer {

    private OrderDao orderDao;

    private TaxDao taxDao;

    private ProductDao productDao;

    public FlooringMasteryServiceLayer(OrderDao orderDao, TaxDao taxDao, ProductDao productDao) {
        this.orderDao = orderDao;
        this.taxDao = taxDao;
        this.productDao = productDao;
    }

    public List<Order> allOrdersForDate(String orderDate) throws FlooringMasteryPersistenceException {
       List<Order> allOrders = orderDao.getAllOrders(orderDate);
       return allOrders;
    }

    public List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException {
        return taxDao.getAllTaxes();
    }

    public List<Product> getAllProducts() throws FlooringMasteryPersistenceException {
        return productDao.getAllProducts();
    }
}
