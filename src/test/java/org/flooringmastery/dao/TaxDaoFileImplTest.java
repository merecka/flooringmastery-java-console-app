package org.flooringmastery.dao;

import org.flooringmastery.dto.Product;
import org.flooringmastery.dto.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaxDaoFileImplTest {

    TaxDaoFileImpl testTaxDao;

    public static final String TEST_FILE = "data/tests/TestTaxes.txt";

    private final String DELIMITER = ",";

    Tax tax1 = new Tax("TX", "Texas", new BigDecimal("4.45"));

    Tax tax2 = new Tax("WA", "Washington", new BigDecimal("9.25"));

    Tax tax3 = new Tax("KY", "Kentucky", new BigDecimal("6.00"));

    Tax tax4 = new Tax("CA", "California", new BigDecimal("25.00"));

    @BeforeEach
    void setUp() {
        testTaxDao = new TaxDaoFileImpl(TEST_FILE);
    }

    @Test
    void getAllTaxes() throws FlooringMasteryPersistenceException {
        ArrayList<Tax> allTaxes = testTaxDao.getAllTaxes();
        assertNotNull(allTaxes, "The list of Products must not null");
        assertEquals(4, allTaxes.size(), "List of Taxes should have 4 items.");

        assertTrue(testTaxDao.getAllTaxes().contains(tax1),
                "The list of items should include Texas.");
        assertTrue(testTaxDao.getAllTaxes().contains(tax2),
                "The list of items should include Washington.");
        assertTrue(testTaxDao.getAllTaxes().contains(tax3),
                "The list of items should include Kentucky.");
        assertTrue(testTaxDao.getAllTaxes().contains(tax4),
                "The list of items should include California.");
    }

    @Test
    void getIndividualTax() throws FlooringMasteryPersistenceException {
        Tax texas = testTaxDao.getIndividualTax("TX");
        Tax washington = testTaxDao.getIndividualTax("WA");
        Tax kentucky = testTaxDao.getIndividualTax("KY");
        Tax california = testTaxDao.getIndividualTax("CA");

        assertNotNull(texas, "The Texas tax must not be null");
        assertNotNull(washington, "The Washington tax must not be null");
        assertNotNull(kentucky, "The Kentucky tax must not be null");
        assertNotNull(california, "The California tax must not be null");

        assertTrue(texas.equals(tax1),
                "The Tax should be Texas.");
        assertTrue(washington.equals(tax2),
                "The Tax should be Washington.");
        assertTrue(kentucky.equals(tax3),
                "The Tax should be Kentucky.");
        assertTrue(california.equals(tax4),
                "The Tax should be California.");
    }
}