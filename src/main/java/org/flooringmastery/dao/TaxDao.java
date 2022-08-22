package org.flooringmastery.dao;

import org.flooringmastery.dto.Tax;

import java.util.List;

public interface TaxDao {

    List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException;

    Tax getIndividualTax(String stateAbbrev) throws FlooringMasteryPersistenceException;
}
