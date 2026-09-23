package com.taxapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;

import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
@Import(TestConfig.class)
class ApiControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private LocalStorageService localStorageService;

    private MockMvc mockMvc;

    @TempDir
    Path tempDir;

    protected static final String VALID_KEY = "valid-key";

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        Files.writeString(tempDir.resolve("clients.json"),
            "[{\"id\":\"client-1\",\"name\":\"Alice\",\"apiKey\":\"valid-key\"}]");
        Files.writeString(tempDir.resolve("items.json"),
            "[{\"id\":\"item-1\",\"name\":\"Laptop\",\"category\":\"electronics\",\"basePrice\":999.99}]");
        Files.writeString(tempDir.resolve("taxrates.json"),
            "[{\"state\":\"CA\",\"category\":\"electronics\",\"rate\":0.0725},"
            + "{\"state\":\"NY\",\"category\":\"clothing\",\"rate\":0.04}]");

        localStorageService.setDirectory(tempDir);
    }

    // TODO(student): add @Test methods that exercise ApiController endpoints
    // via mockMvc.perform(...). Aim for >= 55% JaCoCo coverage overall.

    @Test
    void createClientTest() throws Exception {
        mockMvc.perform(post("/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Jake\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void createItemTest() throws Exception {
        mockMvc.perform(post("/v1/items")
                        .header("X-API-Key", VALID_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"PS5\",\"category\":\"electronics\",\"basePrice\":600}"))
                .andExpect(status().isOk());
    }

    @Test
    void updateItemPriceTest() throws Exception {
        mockMvc.perform(patch("/v1/items/item-1")
                        .header("X-API-Key", VALID_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"basePrice\":500}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.basePrice").value(500.0));
    }

    @Test
    void getItemsTest() throws Exception {
        mockMvc.perform(get("/v1/items")
                        .header("X-API-Key", VALID_KEY))
                .andExpect(status().isOk());
    }

    @Test
    void getItemByIdTest() throws Exception {
        mockMvc.perform(get("/v1/items/item-1")
                        .header("X-API-Key", VALID_KEY))
                .andExpect(status().isOk());
    }

    @Test
    void deleteItemTest() throws Exception {
        mockMvc.perform(delete("/v1/items/item-1")
                        .header("X-API-Key", VALID_KEY))
                .andExpect(status().isNoContent());
    }

    @Test
    void calculateTaxTest() throws Exception {
        mockMvc.perform(post("/v1/tax/quote")
                        .header("X-API-Key", VALID_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"state\":\"CA\",\"price\":1000,\"category\":\"electronics\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void getSupportedTest() throws Exception {
        mockMvc.perform(get("/v1/supported")
                        .header("X-API-Key", VALID_KEY))
                .andExpect(status().isOk());
    }

    @Test
    void getItemsFilterTest() throws Exception {
        mockMvc.perform(get("/v1/items")
                        .param("category", "electronics")
                        .header("X-API-Key", VALID_KEY))
                .andExpect(status().isOk());
    }
}
