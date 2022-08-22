package org.flooringmastery.dao;

import org.flooringmastery.dto.Product;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class ProductDaoFileImpl implements ProductDao {
    private final String DELIMITER = ",";

    private final String PRODUCTS_FILE = "data/Products.txt";

    private Map<String, Product> products = new HashMap<>();

    public ArrayList<Product> getAllProducts() throws FlooringMasteryPersistenceException {
        loadProducts();
        return new ArrayList<>(products.values());
    }

    public Product getIndividualProduct(String productType) throws FlooringMasteryPersistenceException {
        loadProducts();
        return products.get(productType);
    }

    private void loadProducts() throws FlooringMasteryPersistenceException {
        Scanner scanner;

        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(PRODUCTS_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException(
                    "-_- Could not load Products data into memory.", e);
        }
        // currentItem holds the most recent line read from the file
        String currentLine;
        // currentProduct holds the most recent Tax unmarshalled
        Product currentProduct;
        // skips the Header line
        scanner.nextLine();
        // Go through PRODUCTS_FILE line by line, decoding each line into a
        // Product object by calling the unmarshallTax method.
        // Process while we have more lines in the file

        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into a Student
            currentProduct = unmarshallProduct(currentLine);

            // We are going to use the student id as the map key for our student object.
            // Put currentStudent into the map using student id as the key
            products.put(currentProduct.getProductType(), currentProduct);
        }
        // close scanner
        scanner.close();
    }

    private Product unmarshallProduct(String productAsText) {
        // Creates Product from String
        // Parses Product into String Array:

        String[] productTokens = productAsText.split(DELIMITER);

        String productType = productTokens[0];
        BigDecimal costPerSquareFoot = new BigDecimal(productTokens[1]);
        BigDecimal laborCostPerSquareFoot = new BigDecimal(productTokens[2]);

        // Return new Product
        return new Product(productType, costPerSquareFoot, laborCostPerSquareFoot);
    }
}
