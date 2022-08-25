package org.flooringmastery.dao;

import org.flooringmastery.dto.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoFileImplTest {

    ProductDaoFileImpl testProductDao;

    public static final String TEST_FILE = "data/tests/TestProducts.txt";

    private final String DELIMITER = ",";

    Product product1 = new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10"));

    Product product2 = new Product("Laminate", new BigDecimal("1.75"), new BigDecimal("2.10"));

    Product product3 = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));

    Product product4 = new Product("Wood", new BigDecimal("5.15"), new BigDecimal("4.75"));


    @BeforeEach
    void setUp() {
        testProductDao = new ProductDaoFileImpl(TEST_FILE);
    }

    @Test
    void getAllProducts() throws FlooringMasteryPersistenceException {
        ArrayList<Product> allProducts = testProductDao.getAllProducts();
        assertNotNull(allProducts, "The list of Products must not null");
        assertEquals(4, allProducts.size(), "List of Products should have 4 items.");

        assertTrue(testProductDao.getAllProducts().contains(product1),
                "The list of items should include Carpet.");
        assertTrue(testProductDao.getAllProducts().contains(product2),
                "The list of items should include Laminate.");
        assertTrue(testProductDao.getAllProducts().contains(product3),
                "The list of items should include Tile.");
        assertTrue(testProductDao.getAllProducts().contains(product4),
                "The list of items should include Wood.");
    }

    @Test
    void getIndividualProduct() throws FlooringMasteryPersistenceException {
        Product carpetProduct = testProductDao.getIndividualProduct("Carpet");
        Product laminateProduct = testProductDao.getIndividualProduct("Laminate");

        assertNotNull(carpetProduct, "The Carpet product must not be null");
        assertNotNull(laminateProduct, "The Laminate product must not be null");

        assertTrue(carpetProduct.equals(product1),
                "The Object should be Carpet.");
        assertTrue(laminateProduct.equals(product2),
                "The Object should be Laminate.");
    }
}