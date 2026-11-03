package com.example.miniservice.listing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ListingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createsAndReadsListing() throws Exception {
        long id = createListing("Vintage camera", "249.99");

        mockMvc.perform(get("/api/listings/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Vintage camera"))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.currency").value("USD"));
    }

    @Test
    void rejectsInvalidCreateRequest() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "title", "",
                "price", new BigDecimal("-1.00"),
                "currency", "usd",
                "sellerId", ""
        ));

        mockMvc.perform(post("/api/listings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.fieldErrors.title").exists())
                .andExpect(jsonPath("$.fieldErrors.price").exists())
                .andExpect(jsonPath("$.fieldErrors.currency").exists())
                .andExpect(jsonPath("$.fieldErrors.sellerId").exists());
    }

    @Test
    void returns404ForMissingListing() throws Exception {
        mockMvc.perform(get("/api/listings/{id}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("LISTING_NOT_FOUND"))
                .andExpect(jsonPath("$.path").value("/api/listings/999999"));
    }

    @Test
    void listsByStatusWithPagination() throws Exception {
        long draftId = createListing("Draft item", "10.00");
        long activeId = createListing("Active item", "20.00");

        mockMvc.perform(put("/api/listings/{id}", activeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "title", "Active item",
                                "description", "now active",
                                "price", new BigDecimal("20.00"),
                                "currency", "USD",
                                "status", "ACTIVE"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        mockMvc.perform(get("/api/listings")
                        .param("status", "ACTIVE")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.items[0].id").value(activeId))
                .andExpect(jsonPath("$.items[0].status").value("ACTIVE"));

        // Keep the draft id used so the test data is clearly two listings.
        mockMvc.perform(get("/api/listings/{id}", draftId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DRAFT"));
    }

    @Test
    void updatesAndDeletesListing() throws Exception {
        long id = createListing("Old title", "5.00");

        mockMvc.perform(put("/api/listings/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "title", "New title",
                                "description", "updated",
                                "price", new BigDecimal("7.50"),
                                "currency", "EUR",
                                "status", "SOLD"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New title"))
                .andExpect(jsonPath("$.price").value(7.50))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.status").value("SOLD"));

        mockMvc.perform(delete("/api/listings/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/listings/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsOversizedPageRequest() throws Exception {
        mockMvc.perform(get("/api/listings").param("size", "500"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.fieldErrors.size").exists());
    }

    private long createListing(String title, String price) throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "title", title,
                "description", "test listing",
                "price", new BigDecimal(price),
                "currency", "USD",
                "sellerId", "seller-1"
        ));

        MvcResult result = mockMvc.perform(post("/api/listings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        return json.get("id").asLong();
    }
}
