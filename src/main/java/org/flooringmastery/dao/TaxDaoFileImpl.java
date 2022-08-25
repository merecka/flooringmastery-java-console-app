package org.flooringmastery.dao;

import org.flooringmastery.dto.Tax;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

@Component
public class TaxDaoFileImpl implements TaxDao {

    private final String DELIMITER = ",";

    private final String TAXES_FILE;

    private Map<String, Tax> taxes = new HashMap<>();

    public TaxDaoFileImpl() {
        this.TAXES_FILE = "data/Taxes.txt";
    }

    public TaxDaoFileImpl(String taxTextFile) {
        this.TAXES_FILE = taxTextFile;
    }

    public ArrayList<Tax> getAllTaxes() throws FlooringMasteryPersistenceException {
        loadTaxes();
        return new ArrayList<>(taxes.values());
    }

    public Tax getIndividualTax(String stateAbbrev) throws FlooringMasteryPersistenceException {
        loadTaxes();
        return taxes.get(stateAbbrev);
    }

    private void loadTaxes() throws FlooringMasteryPersistenceException {
        Scanner scanner;

        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(TAXES_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException(
                    "-_- Could not load Taxes data into memory.", e);
        }
        // currentItem holds the most recent line read from the file
        String currentLine;
        // currentTax holds the most recent Tax unmarshalled
        Tax currentTax;
        // skips the Header line
        scanner.nextLine();
        // Go through TAXES_FILE line by line, decoding each line into a
        // Tax object by calling the unmarshallTax method.
        // Process while we have more lines in the file

        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into a Student
            currentTax = unmarshallTax(currentLine);

            // We are going to use the student id as the map key for our student object.
            // Put currentStudent into the map using student id as the key
            taxes.put(currentTax.getStateAbbreviation(), currentTax);
        }
        // close scanner
        scanner.close();
    }

    private Tax unmarshallTax(String taxAsText) {
        // Creates Tax from String
        // Parses Tax into String Array:

        String[] taxTokens = taxAsText.split(DELIMITER);

        String stateAbbreviation = taxTokens[0];
        String stateName = taxTokens[1];
        BigDecimal taxRate = new BigDecimal(taxTokens[2]);

        // Return new Order
        return new Tax(stateAbbreviation, stateName, taxRate);
    }
}
