package com.buttercoin.api;

import org.junit.Test;

import java.math.BigDecimal;
import java.net.URL;

import static com.buttercoin.api.Order.OrderType.Limit;
import static com.buttercoin.api.Order.Side.Buy;
import static com.buttercoin.api.Order.Status.Canceled;
import static com.buttercoin.api.Order.Status.Opened;
import static org.fest.assertions.Assertions.assertThat;

public class TestOrders extends ButtercoinAPITest {
    @Test
    public void testOrder() throws Exception {
        URL orderUrl = buttercoin.createOrder(CreateOrder.Instrument.BTC_USD, Buy, Limit, new BigDecimal(10), new BigDecimal(1)).get();
        Order order = buttercoin.getOrder(orderUrl).get();

        assertThat(order.getSide()).isEqualTo(Buy);
        assertThat(order.getOrderType()).isEqualTo(Limit);
        assertThat(order.getStatus()).isEqualTo(Opened);
        assertThat(order.getPrice().compareTo(new BigDecimal(10)) == 0);
        assertThat(order.getQuantity().compareTo(new BigDecimal(1)) == 0);
        assertThat(order.getEvents().size() == 1);

        buttercoin.cancelOrder(order.getOrderId()).get();

        Order canceledOrder = buttercoin.getOrder(order.getOrderId()).get();
        assertThat(order.getSide()).isEqualTo(Buy);
        assertThat(canceledOrder.getOrderType()).isEqualTo(Limit);
        assertThat(canceledOrder.getStatus()).isEqualTo(Canceled);
        assertThat(canceledOrder.getPrice().compareTo(new BigDecimal(10)) == 0);
        assertThat(canceledOrder.getQuantity().compareTo(new BigDecimal(1)) == 0);
        assertThat(canceledOrder.getEvents().size() == 2);
    }
}
