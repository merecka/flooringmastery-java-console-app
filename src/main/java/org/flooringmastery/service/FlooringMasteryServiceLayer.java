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

    public List<Order> allOrdersForDate(String orderDate) {
       return orderDao.getAllOrders(orderDate);
    }

    public List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException {
        return taxDao.getAllTaxes();
    }

    public List<Product> getAllProducts() throws FlooringMasteryPersistenceException {
        return productDao.getAllProducts();
    }

    public BigDecimal calculateOrderMaterialCost(BigDecimal newOrderArea, Product newOrderProduct) {
        return newOrderArea.multiply(newOrderProduct.getCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateOrderLaborCost(BigDecimal newOrderArea, Product newOrderProduct) {
        return newOrderArea.multiply(newOrderProduct.getLaborCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateOrderTax(Tax newOrderTax, BigDecimal newOrderMaterialCost, BigDecimal newOrderLaborCost) {
        BigDecimal taxRate = newOrderTax.getTaxRate().divide(new BigDecimal("100"), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
        return (newOrderMaterialCost.add(newOrderLaborCost)).multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateTotalOrderCost(BigDecimal newOrderTax, BigDecimal newOrderMaterialCost, BigDecimal newOrderLaborCost) {
        return (newOrderMaterialCost.add(newOrderLaborCost)).add(newOrderTax).setScale(2, RoundingMode.HALF_UP);
    }

    public int getNewOrderNumber(LocalDate newOrderDate) {
        String formattedDate = newOrderDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        System.out.println("formattedDate in service.getNewOrderNumber() is: " + formattedDate);
        List<Order> allOrders = allOrdersForDate(formattedDate);
        return allOrders.size();
    }

    public void createOrder(Order newOrder) throws FlooringMasteryPersistenceException {
        orderDao.addOrder(newOrder);
    }
}
