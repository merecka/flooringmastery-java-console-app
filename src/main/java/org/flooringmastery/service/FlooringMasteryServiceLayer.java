package org.flooringmastery.service;

import org.flooringmastery.dao.FlooringMasteryPersistenceException;
import org.flooringmastery.dao.OrderDao;
import org.flooringmastery.dao.ProductDao;
import org.flooringmastery.dao.TaxDao;
import org.flooringmastery.dto.Order;
import org.flooringmastery.dto.Product;
import org.flooringmastery.dto.Tax;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        return orderDao.getAllOrders(orderDate);
    }

    public List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException {
        return taxDao.getAllTaxes();
    }

    public List<Product> getAllProducts() throws FlooringMasteryPersistenceException {
        return productDao.getAllProducts();
    }

    public void calculateOrderMaterialCost(Order newOrder) {
        BigDecimal materialCost = newOrder.getArea().multiply(newOrder.getProduct().getCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP);
        newOrder.setMaterialCost(materialCost);
    }

    public void calculateOrderLaborCost(Order newOrder) {
        BigDecimal laborCost = newOrder.getArea().multiply(newOrder.getProduct().getLaborCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP);
        newOrder.setLaborCost(laborCost);
    }

    public void calculateOrderTax(Order newOrder) {
        BigDecimal taxRate = newOrder.getTax().getTaxRate().divide(new BigDecimal("100"), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
        BigDecimal orderTaxTotal = (newOrder.getMaterialCost().add(newOrder.getLaborCost())).multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
        newOrder.setTaxTotal(orderTaxTotal);
    }

    public void calculateTotalOrderCost(Order newOrder) {
        BigDecimal orderTotalCost = (newOrder.getMaterialCost().add(newOrder.getLaborCost())).add(newOrder.getTaxTotal()).setScale(2, RoundingMode.HALF_UP);
        newOrder.setTotalCost(orderTotalCost);
    }

    public void getNewOrderNumber(Order newOrder) throws FlooringMasteryPersistenceException {
        String formattedDate = newOrder.getOrderDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<Order> allOrders = orderDao.getAllOrders(formattedDate);
        if (allOrders.size() == 0) {
            newOrder.setOrderNumber(1);
        } else {
            newOrder.setOrderNumber(allOrders.size() + 1);
        }
    }

    public void createOrder(Order newOrder) throws FlooringMasteryPersistenceException {
        orderDao.addOrder(newOrder);
    }

    public Order retrieveOrderToEdit(int orderNumber, LocalDate orderDate) throws FlooringMasteryPersistenceException, NullPointerException {
        try {
            String formattedDate = orderDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            return orderDao.getOrder(formattedDate, orderNumber);
        } catch (FlooringMasteryPersistenceException | NullPointerException e) {
            return null;
        }

    }

    public void replaceEditedOrder(Order editedOrder) throws FlooringMasteryPersistenceException {
        orderDao.addOrder(editedOrder);
    }
}
