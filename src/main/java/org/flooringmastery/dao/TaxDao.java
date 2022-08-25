package org.flooringmastery.dao;

import org.flooringmastery.dto.Tax;
import java.util.ArrayList;


public interface TaxDao {

    ArrayList<Tax> getAllTaxes() throws FlooringMasteryPersistenceException;

    Tax getIndividualTax(String stateAbbrev) throws FlooringMasteryPersistenceException;
}
