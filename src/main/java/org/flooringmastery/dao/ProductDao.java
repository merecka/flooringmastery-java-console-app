package org.flooringmastery.dao;

import org.flooringmastery.dto.Product;

import java.util.ArrayList;

public interface ProductDao {

    ArrayList<Product> getAllProducts() throws FlooringMasteryPersistenceException;

    Product getIndividualProduct(String productType) throws FlooringMasteryPersistenceException;
}
