package com.buttercoin.api;

import org.junit.Test;

import java.math.BigDecimal;

import static org.fest.assertions.Assertions.assertThat;

public class TestAccount extends ButtercoinAPITest {
    @Test
    public void testPermissions() throws Exception {
        Permissions permissions = buttercoin.getKey().get();
        assertThat(permissions.getPermissions().size() > 0);
    }

    @Test
    public void testBalances() throws Exception {
        AccountBalances accountBalances = buttercoin.getBalances().get();
        assertThat(accountBalances.getUsd().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    public void testDepositAddress() throws Exception {
        DepositAddress depositAddress = buttercoin.getDepositAddress().get();
        assertThat(!depositAddress.getAddress().isEmpty());
    }
}
