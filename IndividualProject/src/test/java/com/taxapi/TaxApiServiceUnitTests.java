package com.taxapi;
import com.taxapi.model.TaxQuoteRequest;
import com.taxapi.model.TaxQuoteResponse;

import com.taxapi.service.TaxApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
@Import(TestConfig.class)
class TaxApiServiceUnitTests {

    @Autowired
    private TaxApiService service;

    @Autowired
    private LocalStorageService localStorageService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws Exception {
        Files.writeString(tempDir.resolve("clients.json"),
            "[{\"id\":\"client-1\",\"name\":\"Alice\",\"apiKey\":\"valid-key\"}]");
        Files.writeString(tempDir.resolve("items.json"),
            "[{\"id\":\"item-1\",\"name\":\"Laptop\",\"category\":\"electronics\",\"basePrice\":999.99}]");
        Files.writeString(tempDir.resolve("taxrates.json"),
            "[{\"state\":\"CA\",\"category\":\"electronics\",\"rate\":0.0725},"
            + "{\"state\":\"NY\",\"category\":\"clothing\",\"rate\":0.04}]");

        localStorageService.setDirectory(tempDir);
    }

    // TODO(student): add @Test methods that exercise TaxApiService directly.
    // The `service` field above is the autowired bean under test.

    @Test
    void validateApiKey() throws Exception {
        assertTrue(service.validateApiKey("valid-key"));
        assertFalse(service.validateApiKey("wrong-key"));
        assertFalse(service.validateApiKey(null));
    }

    @Test
    void createItemTest() throws Exception {
        var item = service.createItem(
                "PS5",
                "electronics",
                600.00
        );
        var items = service.getItems();
        assertEquals(2, items.size());

        assertEquals("PS5", item.getName());
        assertEquals("electronics", item.getCategory());
        assertEquals(600.00, item.getBasePrice());
    }

    @Test
    void getItemsTest() throws Exception {
        var items = service.getItems();
        assertEquals(1, items.size());

        var item = items.get(0);

        assertEquals("Laptop", item.getName());
        assertEquals("electronics", item.getCategory());
        assertEquals(999.99, item.getBasePrice());
    }

    @Test
    void getItemByIdTest() throws Exception {
        var item = service.getItemById("item-1");
        assertEquals("Laptop", item.getName());
        assertEquals("electronics", item.getCategory());
        assertEquals(999.99, item.getBasePrice());
    }

    @Test
    void deleteItemTest() throws Exception {
        boolean deleted1 = service.deleteItem("item-3");
        assertEquals(1, service.getItems().size());

        boolean deleted2 = service.deleteItem("item-1");
        assertEquals(0, service.getItems().size());
    }

    @Test
    void createClientTest() throws Exception {
        var client = service.createClient("Jake");
        assertEquals("Jake", client.getName());
        assertNotNull(client.getId());
    }

    @Test
    void calculateTaxTest() throws Exception {
        TaxQuoteRequest request = new TaxQuoteRequest();
        request.setState("CA");
        request.setPrice(1000.00);
        request.setCategory("electronics");
        TaxQuoteResponse response = service.calculateTax(request);

        assertEquals(1000.00, response.getPrice());
        assertEquals(0.0725, response.getTaxRate());
        assertEquals(72.50, response.getTaxAmount());
        assertEquals(1072.50, response.getTotal());
    }

    @Test
    void getSupportedTest() throws Exception {
        var supported = service.getSupported();

        assertTrue(supported.getStates().contains("CA"));
        assertTrue(supported.getCategories().contains("electronics"));
    }








}
