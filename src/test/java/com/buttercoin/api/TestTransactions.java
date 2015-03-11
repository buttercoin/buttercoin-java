package com.buttercoin.api;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class TestTransactions extends ButtercoinAPITest {
    @Test
    public void testTransactions() throws Exception {
        Transactions transactions = buttercoin.getTransactions(Transaction.Status.Funded).get();
        assertThat(transactions.getTransactions().size()).isGreaterThan(0);
    }
}
