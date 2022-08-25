package org.flooringmastery.dao;

import org.flooringmastery.dto.Tax;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public interface TaxDao {

    ArrayList<Tax> getAllTaxes() throws FlooringMasteryPersistenceException;

    Tax getIndividualTax(String stateAbbrev) throws FlooringMasteryPersistenceException;
}
