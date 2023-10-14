package com.ylab.walletservice.infrastructure;

import com.ylab.walletservice.domain.Transaction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryTransactionRepositoryTest {

    private InMemoryTransactionRepository repository;

    @Before
    public void setUp() {
        repository = new InMemoryTransactionRepository();
    }

    @Test
    public void testGet() {
        Transaction transaction = new Transaction(1, 1, 100, "credit");
        repository.create(1, 1, 100, "credit");

        Transaction retrievedTransaction = repository.get(1);
        assertNotNull(retrievedTransaction);
        assertEquals(transaction, retrievedTransaction);
    }

    @Test
    public void testExists() {
        repository.create(1, 1, 100, "credit");
        assertTrue(repository.exists(1));
    }

    @Test
    public void testCreate() {
        Transaction transaction = repository.create(1, 1, 100, "credit");
        assertNotNull(transaction);
        assertEquals(1, transaction.getId());
        assertEquals(1, transaction.getCreatorId());
        assertEquals(100, transaction.getAmount());
        assertEquals("credit", transaction.getType());
        assertTrue(repository.exists(1));
    }

    @Test
    public void testGetTransactionsSize() {
        repository.create(1, 1, 100, "credit");
        assertEquals(1, repository.getTransactionsSize());
        repository.create(2, 2, 200, "debit");
        assertEquals(2, repository.getTransactionsSize());
    }
}