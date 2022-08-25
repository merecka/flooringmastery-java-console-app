package org.flooringmastery.dto;

import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Order {

    private LocalDate orderDate;
    private int orderNumber;

    private String customerName;

    private Tax tax;

    private Product product;

    private BigDecimal area;

    private BigDecimal materialCost;

    private BigDecimal laborCost;

    private BigDecimal taxTotal;

    private BigDecimal totalCost;

    @Autowired
    public Order() {}

    @Override
    public int hashCode() {
        return Objects.hash(orderDate, orderNumber, customerName, tax, product, area, materialCost, laborCost, taxTotal, totalCost);
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public BigDecimal getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(BigDecimal taxTotal) {
        this.taxTotal = taxTotal;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderNumber == order.orderNumber && orderDate.equals(order.orderDate) && customerName.equals(order.customerName) && tax.equals(order.tax) && product.equals(order.product) && area.equals(order.area) && materialCost.equals(order.materialCost) && laborCost.equals(order.laborCost) && taxTotal.equals(order.taxTotal) && totalCost.equals(order.totalCost);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderDate=" + orderDate +
                ", orderNumber=" + orderNumber +
                ", customerName='" + customerName + '\'' +
                ", tax=" + tax +
                ", product=" + product +
                ", area=" + area +
                ", materialCost=" + materialCost +
                ", laborCost=" + laborCost +
                ", taxTotal=" + taxTotal +
                ", totalCost=" + totalCost +
                '}';
    }
}
