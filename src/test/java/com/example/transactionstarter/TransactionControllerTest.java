package com.example.transactionstarter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Test 1: Valid transaction should be created successfully
    @Test
    void shouldCreateTransactionSuccessfully() throws Exception {

        String request = """
                {
                    "transactionId": "TXN001",
                    "customerId": "CUST001",
                    "amount": 1000.00,
                    "currency": "INR",
                    "transactionType": "PAYMENT",
                    "status": "PENDING"
                }
                """;

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value("TXN001"))
                .andExpect(jsonPath("$.customerId").value("CUST001"));
    }

    // Test 2: Invalid transaction should be rejected
    @Test
    void shouldRejectInvalidTransaction() throws Exception {

        String request = """
                {
                    "transactionId": "TXN002",
                    "customerId": "CUST002",
                    "amount": -100.00,
                    "currency": "INR",
                    "transactionType": "PAYMENT",
                    "status": "PENDING"
                }
                """;

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest());
    }

    // Test 3: Duplicate transaction ID should be rejected
    @Test
    void shouldRejectDuplicateTransactionId() throws Exception {

        String request = """
                {
                    "transactionId": "TXN003",
                    "customerId": "CUST003",
                    "amount": 500.00,
                    "currency": "INR",
                    "transactionType": "PAYMENT",
                    "status": "PENDING"
                }
                """;

        // First transaction
        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated());

        // Same transaction ID
        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isConflict());
    }

    // Test 4: Non-existent transaction should return 404
    @Test
    void shouldReturnNotFoundForNonExistentTransaction() throws Exception {

        mockMvc.perform(get("/api/transactions/TXN999"))
                .andExpect(status().isNotFound());
    }
    // Test 5: Transaction status should be updated successfully
@Test
void shouldUpdateTransactionStatusSuccessfully() throws Exception {

    String createRequest = """
            {
                "transactionId": "TXN004",
                "customerId": "CUST004",
                "amount": 750.00,
                "currency": "INR",
                "transactionType": "PAYMENT",
                "status": "PENDING"
            }
            """;

    // Create transaction first
    mockMvc.perform(post("/api/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequest))
            .andExpect(status().isCreated());

    String updateRequest = """
            {
                "status": "COMPLETED"
            }
            """;

    // Update status
    mockMvc.perform(patch("/api/transactions/TXN004/status")
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.transactionId").value("TXN004"))
            .andExpect(jsonPath("$.status").value("COMPLETED"));
}
// Test 6: Get all transactions for a customer
@Test
void shouldGetTransactionsByCustomer() throws Exception {

    String request1 = """
            {
                "transactionId": "TXN005",
                "customerId": "CUST005",
                "amount": 1000.00,
                "currency": "INR",
                "transactionType": "PAYMENT",
                "status": "PENDING"
            }
            """;

    String request2 = """
            {
                "transactionId": "TXN006",
                "customerId": "CUST005",
                "amount": 500.00,
                "currency": "INR",
                "transactionType": "TRANSFER",
                "status": "PENDING"
            }
            """;

    // Create first transaction
    mockMvc.perform(post("/api/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request1))
            .andExpect(status().isCreated());

    // Create second transaction for same customer
    mockMvc.perform(post("/api/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request2))
            .andExpect(status().isCreated());

    // Get all transactions for customer
    mockMvc.perform(get("/api/transactions/customer/CUST005"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].customerId").value("CUST005"))
            .andExpect(jsonPath("$[1].customerId").value("CUST005"));
}
}