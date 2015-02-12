package com.buttercoin.api;

import org.junit.Test;

public class TestTransactions extends ButtercoinAPITest {
    @Test
    public void testTransactions() throws Exception {
        Transactions transactions = buttercoin.getTransactions(Transaction.Status.Funded).get();
        for (Transaction tx: transactions) {
            System.out.println(tx);
        }
    }
}
