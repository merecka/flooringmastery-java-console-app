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

    public BigDecimal calculateTotalOrderCost(Tax newOrderTax, Product newOrderProduct, BigDecimal newOrderArea) {
        BigDecimal materialCost;
        BigDecimal laborCost;
        BigDecimal tax;

        materialCost = newOrderArea.multiply(newOrderProduct.getCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP);
        laborCost = newOrderArea.multiply(newOrderProduct.getLaborCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP);

        BigDecimal taxRate = newOrderTax.getTaxRate().divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
        tax = (materialCost.add(laborCost)).multiply(taxRate).setScale(2, RoundingMode.HALF_UP);

        return (materialCost.add(laborCost)).add(tax).setScale(2, RoundingMode.HALF_UP);
    }

    public void createOrder(LocalDate newOrderDate, String newOrderCustomerName, Tax newOrderTax, Product newOrderProduct, BigDecimal newOrderArea, BigDecimal newOrderTotal) {

        new Order
    }
}
