package org.flooringmastery.dao;

import org.flooringmastery.dto.Product;

import java.util.ArrayList;

public interface ProductDao {

    public ArrayList<Product> getAllProducts() throws FlooringMasteryPersistenceException;
}
