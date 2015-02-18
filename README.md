# Buttercoin API Java Client [![Build Status](https://travis-ci.org/buttercoin/buttercoin-java.svg?branch=master)](https://travis-ci.org/buttercoin/buttercoin-java)

Official Java Client of the [Buttercoin API](https://developer.buttercoin.com). [Buttercoin](https://buttercoin.com) is a trading platform that makes buying and selling [bitcoin](http://en.wikipedia.org/wiki/Bitcoin) easy.

## Installation

#### Maven

Add this dependency to your project's POM file:

```xml
<dependency>
  <groupId>com.buttercoin</groupId>
  <artifactId>buttercoin-java</artifactId>
  <version>1.0.0</version>
</dependency>
```

The client's jar will automatically be pulled from Maven Central. Depends on [Async Http Client](https://github.com/AsyncHttpClient/async-http-client),
[Google Guava](https://github.com/google/guava) and [Jackson](https://github.com/FasterXML/jackson). This client is fully asynchronous.

## Usage

#### HMAC-SHA256 Authentication

You need an [API key and secret](https://buttercoin.com/#/api) to use [HMAC](http://en.wikipedia.org/wiki/Hash-based_message_authentication_code).
You can either set them as the environment variables `BUTTERCOIN_API_KEY` and `BUTTERCOIN_API_SECRET`, or pass them to the builder, for example:

```java
import com.buttercoin.api.*;

Buttercoin buttercoin = Buttercoin.newBuilder()
    .apiKey("pzovcp0kkxmchkuj3smej2wmtjwlare6")
    .apiSecret("KQvYkSZyt0EpJMeKLvMd1sPb1SdrxBqD")
    .build();
```

The client will then sign your API calls with your key, the url (with potential JSON body) and client timestamp. This timestamp needs to be
within 5 minutes of Buttercoin server times (GMT). You can optionally pass the timestamp in milliseconds (`System.currentTimeMillis()`) to every client method.

## Getting Started

To get the ticker, which has the current bid, ask, and last sell prices for bitcoin on Buttercoin (no authentication needed):

```java
import com.buttercoin.api.*;

Buttercoin buttercoin = Buttercoin.newBuilder().build();
Future<Ticker> t = buttercoin.getTicker();
BigDecimal last = t.get().getLast();
```

To get the orderbook (no authentication needed):

```java
import com.buttercoin.api.*;

Buttercoin buttercoin = Buttercoin.newBuilder().build();
Future<OrderBook> t = buttercoin.getOrderBook();
```

To get the trade history (no authentication needed):

```java
import com.buttercoin.api.*;

Buttercoin buttercoin = Buttercoin.newBuilder().build();
Future<TradeHistory> t = buttercoin.getTradeHistory();
```

#### Account

To get your bitcoin wallet address (authentication needed):

```java
import com.buttercoin.api.*;

// Expects BUTTERCOIN_API_KEY and BUTTERCOIN_API_SECRET environment
// variables to be set, see HMAC Authentication.

Buttercoin buttercoin = Buttercoin.newBuilder().build();
Future<DepositAddress> d = buttercoin.getDepositAddress();
String myAddress = d.get().toString();
```

#### Orders

To list all your open and filled orders:

```java
import com.buttercoin.api.*;
import static com.buttercoin.api.Order.Status.*;

Buttercoin buttercoin = Buttercoin.newBuilder().build();
Orders orders = buttercoin.getOrders(Opened, Filled).get();
```

The [`.get()`](http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Future.html#get) waits for the
request to complete, and returns the orders.

To create an order (buy bitcoin):

```java
import com.buttercoin.api.*;
import static com.buttercoin.api.CreateOrder.Instrument.*;
import static com.buttercoin.api.Order.Side.*;
import static com.buttercoin.api.Order.OrderType.*;

Buttercoin buttercoin = Buttercoin.newBuilder().build();
URL orderUrl = buttercoin.createOrder(BTC_USD, Buy, Limit, new BigDecimal(10), new BigDecimal(10)).get();
Future<Order> orderFuture = buttercoin.getOrder(orderUrl);
```

Orders return by default 20 per page. To request a next page, use `buttercoin.getOrders(previousOrders.getNextPage())`.

#### Transactions

Please [contact Buttercoin support](mailto:support@buttercoin.com) before creating a USD deposit using the API.

To deposit:

```java
import com.buttercoin.api.*;

Buttercoin buttercoin = Buttercoin.newBuilder().build();
Future<URL> depositUrl = buttercoin.createDeposit("wire", "USD", new BigDecimal(100));
```

To withdraw:

```java
import com.buttercoin.api.*;

Buttercoin buttercoin = Buttercoin.newBuilder().build();
Future<URL> withdrawalUrl = buttercoin.createWithdrawal("us_ach", "USD", new BigDecimal(100));
```

To list all your funded deposits:

```java
import com.buttercoin.api.*;
import static com.buttercoin.api.Transaction.Status.*;
import static com.buttercoin.api.Transaction.TransactionType.*;

Buttercoin buttercoin = Buttercoin.newBuilder().build();
Transactions transactions = buttercoin.getTransactions(Funded, Deposit).get();
```

Transactions return by default 20 per page. To request a next page, use `buttercoin.getTransactions(previousTx.getNextPage())`.

#### Sending Bitcoin

To send bitcoin from your Buttercoin account to someone else, you'll need their Bitcoin address. For example, let's
send 10 BTC to [Greenpeace](https://blockchain.info/address/1MNcXuKVFpLpkoWijKeXrcyab2U8J762Ua):

```java
import com.buttercoin.api.*;

Buttercoin buttercoin = Buttercoin.newBuilder().build();
buttercoin.sendBitcoin(new BigDecimal(10),
                       "1MNcXuKVFpLpkoWijKeXrcyab2U8J762Ua").get();
```

For your security, the default setting for withdrawals requires email confirmation. You can view or change this setting [here](https://buttercoin.com/#/settings#tab-notifications).

## Advanced Usage

#### Passing in your own ExecutorService

To better integrate with other libraries (like e.g. [Akka](http://akka.io/)) you can pass in your own [ExecutorService](http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html).

```java
import com.buttercoin.api.*;
import com.google.common.util.concurrent.MoreExecutors;

Buttercoin buttercoin = Buttercoin.newBuilder()
    .executorService(MoreExecutors.directExecutor())
    .build();
```

#### Timeouts

If you're on a slow connection, it might be handy to customize the request and idle timeouts.

```java
import com.buttercoin.api.*;
import java.util.concurrent.TimeUnit;

Buttercoin buttercoin = Buttercoin.newBuilder()
    .requestTimeout(5, TimeUnit.SECONDS)
    .idleTimeout(2, TimeUnit.SECONDS)
    .build();
```

#### Chaining API calls

All futures returned by the client extend Google Guava's [ListenableFuture](https://code.google.com/p/guava-libraries/wiki/ListenableFutureExplained).
For example, let's cancel all open orders:

```java
import com.buttercoin.api.*;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import static com.buttercoin.api.Order.Status.*;

Buttercoin buttercoin = Buttercoin.newBuilder().build();

Futures.addCallback(buttercoin.getOrders(Opened), new FutureCallback<Orders>() {
    @Override
    public void onSuccess(Orders orders) {
        for (Order order: orders) {
            buttercoin.cancelOrder(System.currentTimeMillis(), order.getOrderId());
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        throwable.printStackTrace();
    }
});
```

#### Is a `Buttercoin` instance thread-safe?

Yes. Further: it is beneficial to use just one instance; optimizations for reuse (buffers, etc).

## Further Reading

  * [Buttercoin - Website](https://www.buttercoin.com)
  * [Buttercoin API Documentation](https://developer.buttercoin.com)

## License

Licensed under the MIT license.

Copyright 2015 [Buttercoin Inc](mailto:hello@buttercoin.com). All Rights Reserved.