package com.ylab.walletservice.infrastructure;

import com.ylab.walletservice.domain.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class InMemoryTransactionRepositoryTest {

    private InMemoryTransactionRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new InMemoryTransactionRepository();
    }

    @Test
    public void testGet() {
        Transaction transaction = new Transaction(1, 1, new BigDecimal(100), "credit");
        repository.create(transaction);

        Transaction retrievedTransaction = repository.get(1);
        assertNotNull(retrievedTransaction);
        assertEquals(transaction, retrievedTransaction);
    }

    @Test
    public void testExists() {
        Transaction transaction = new Transaction(1, 1, new BigDecimal(100), "credit");
        repository.create(transaction);
        assertTrue(repository.exists(1));
    }

    @Test
    public void testCreate() {
        Transaction forCreate=new Transaction(1, 1, new BigDecimal(100), "credit");
        Transaction transaction = repository.create(forCreate);
        assertNotNull(transaction);
        assertEquals(1, transaction.getId());
        assertEquals(1, transaction.getCreatorId());
        assertEquals(new BigDecimal(100), transaction.getAmount());
        assertEquals("credit", transaction.getType());
        assertTrue(repository.exists(1));
    }

    @Test
    public void testGetTransactionsSize() {
        Transaction transaction1=new Transaction(1, 1, new BigDecimal(100), "credit");
        repository.create(transaction1);
        assertEquals(1, repository.getTransactionsSize());
        Transaction transaction2=new Transaction(2, 2, new BigDecimal(200), "debit");
        repository.create(transaction2);
        assertEquals(2, repository.getTransactionsSize());
    }
}