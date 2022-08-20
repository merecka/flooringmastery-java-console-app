package org.flooringmastery.dao;

import org.flooringmastery.dto.Tax;

import java.util.List;

public interface TaxDao {

    public List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException;
}
