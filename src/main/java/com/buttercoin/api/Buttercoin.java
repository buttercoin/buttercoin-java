package com.buttercoin.api;

import com.buttercoin.api.functions.Status202ExtractLocation;
import com.buttercoin.api.functions.Status204SuccessFailure;
import com.buttercoin.api.functions.ThrowOnHttpFailure;
import com.buttercoin.api.functions.MapResponseJson;
import com.buttercoin.api.utils.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableFuture;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.SignatureCalculator;
import com.ning.http.client.extra.ThrottleRequestFilter;
import com.ning.http.client.providers.jdk.JDKAsyncHttpProvider;
import com.ning.http.client.providers.netty.NettyAsyncHttpProviderConfig;

import java.math.BigDecimal;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static com.google.common.util.concurrent.Futures.transform;

interface UnauthenticatedAPI {
    /**
     * Returns the current bid, ask, and last sell prices on Buttercoin.
     *
     * @return the current bid, ask, and last sell prices on Buttercoin
     */
    ListenableFuture<Ticker> getTicker();

    /**
     * Returns the current orders in the Buttercoin order book.
     *
     * @return the current orders in the Buttercoin order book
     */
    ListenableFuture<OrderBook> getOrderBook();

    /**
     * Returns the last 100 trades.
     *
     * @return the last 100 trades
     */
    ListenableFuture<TradeHistory> getTradeHistory();
}

interface AccountAPI {
    /**
     * Returns set of permissions associated with this account.
     *
     * @return permissions associated with this account
     */
    ListenableFuture<Permissions> getKey();

    /**
     * Returns set of permissions associated with this account.
     *
     * @return permissions associated with this account
     */
    ListenableFuture<Permissions> getKey(long timestamp);

    /**
     * Returns balances for this account.
     *
     * @return balances for this account
     */
    ListenableFuture<AccountBalances> getBalances();

    /**
     * Returns balances for this account.
     *
     * @return balances for this account
     */
    ListenableFuture<AccountBalances> getBalances(long timestamp);

    /**
     * Returns bitcoin address string to deposit your funds into the Buttercoin platform.
     *
     * @return bitcoin address string to deposit your funds into the Buttercoin platform
     */
    ListenableFuture<DepositAddress> getDepositAddress();

    /**
     * Returns bitcoin address string to deposit your funds into the Buttercoin platform.
     *
     * @return bitcoin address string to deposit your funds into the Buttercoin platform
     */
    ListenableFuture<DepositAddress> getDepositAddress(long timestamp);
}

interface OrderAPI {
    ListenableFuture<Order> getOrder(String orderId);

    ListenableFuture<Order> getOrder(long timestamp, String orderId);

    ListenableFuture<Order> getOrder(URL url);

    ListenableFuture<Order> getOrder(long timestamp, URL url);

    ListenableFuture<Orders> getOrders(URL url);

    ListenableFuture<Orders> getOrders(long timestamp, URL url);

    ListenableFuture<Orders> getOrders(Order.Status... status);

    ListenableFuture<Orders> getOrders(long timestamp, Order.Status... status);

    ListenableFuture<Orders> getOrders(Order.Side... side);

    ListenableFuture<Orders> getOrders(long timestamp, Order.Side... side);

    ListenableFuture<Orders> getOrders(Order.OrderType... orderType);

    ListenableFuture<Orders> getOrders(long timestamp, Order.OrderType... orderType);

    ListenableFuture<Orders> getOrders(Order.Status status, Order.Side side, Order.OrderType orderType);

    ListenableFuture<Orders> getOrders(long timestamp, Order.Status status, Order.Side side, Order.OrderType orderType);

    ListenableFuture<Orders> getOrders(Iterable<Order.Status> status, Iterable<Order.Side> sides, Iterable<Order.OrderType> orderTypes);

    ListenableFuture<Orders> getOrders(long timestamp, Iterable<Order.Status> status, Iterable<Order.Side> sides, Iterable<Order.OrderType> orderTypes);

    ListenableFuture<Orders> getOrders(Iterable<Order.Status> status, Iterable<Order.Side> sides, Iterable<Order.OrderType> orderTypes, int page, int pageSize);

    ListenableFuture<Orders> getOrders(long timestamp, Iterable<Order.Status> status, Iterable<Order.Side> sides, Iterable<Order.OrderType> orderTypes, int page, int pageSize);

    ListenableFuture<Orders> getOrders(String... orderIds);

    ListenableFuture<Orders> getOrders(long timestamp, String... orderIds);

    ListenableFuture<URL> createOrder(CreateOrder.Instrument instrument, Order.Side side, Order.OrderType orderType, BigDecimal price, BigDecimal quantity);

    ListenableFuture<URL> createOrder(long timestamp, CreateOrder.Instrument instrument, Order.Side side, Order.OrderType orderType, BigDecimal price, BigDecimal quantity);

    ListenableFuture<Boolean> cancelOrder(String orderId);

    ListenableFuture<Boolean> cancelOrder(long timestamp, String orderId);
}

interface TransactionAPI {
    ListenableFuture<Transaction> getTransaction(String transactionId);

    ListenableFuture<Transaction> getTransaction(long timestamp, String transactionId);

    ListenableFuture<Transaction> getTransaction(URL url);

    ListenableFuture<Transaction> getTransaction(long timestamp, URL url);

    ListenableFuture<Transactions> getTransactions(URL url);

    ListenableFuture<Transactions> getTransactions(long timestamp, URL url);

    ListenableFuture<Transactions> getTransactions(Transaction.Status... status);

    ListenableFuture<Transactions> getTransactions(long timestamp, Transaction.Status... status);

    ListenableFuture<Transactions> getTransactions(Transaction.TransactionType... transactionType);

    ListenableFuture<Transactions> getTransactions(long timestamp, Transaction.TransactionType... transactionType);

    ListenableFuture<Transactions> getTransactions(Transaction.Status status, Transaction.TransactionType transactionType);

    ListenableFuture<Transactions> getTransactions(long timestamp, Transaction.Status status, Transaction.TransactionType transactionType);

    ListenableFuture<Transactions> getTransactions(Iterable<Transaction.Status> status, Iterable<Transaction.TransactionType> transactionTypes);

    ListenableFuture<Transactions> getTransactions(long timestamp, Iterable<Transaction.Status> status, Iterable<Transaction.TransactionType> transactionTypes);

    ListenableFuture<Transactions> getTransactions(Iterable<Transaction.Status> status, Iterable<Transaction.TransactionType> transactionTypes, int page, int pageSize);

    ListenableFuture<Transactions> getTransactions(long timestamp, Iterable<Transaction.Status> status, Iterable<Transaction.TransactionType> transactionTypes, int page, int pageSize);

    ListenableFuture<URL> createDeposit(String method, String currency, BigDecimal amount);

    ListenableFuture<URL> createDeposit(long timestamp, String method, String currency, BigDecimal amount);

    ListenableFuture<URL> createWithdrawal(String method, String currency, BigDecimal amount);

    ListenableFuture<URL> createWithdrawal(long timestamp, String method, String currency, BigDecimal amount);

    ListenableFuture<URL> sendBitcoin(BigDecimal amount, String destination);

    ListenableFuture<URL> sendBitcoin(long timestamp, BigDecimal amount, String destination);

    ListenableFuture<URL> sendBitcoin(String currency, BigDecimal amount, String destination);

    ListenableFuture<URL> sendBitcoin(long timestamp, String currency, BigDecimal amount, String destination);

    ListenableFuture<Boolean> cancelTransaction(String transactionId);

    ListenableFuture<Boolean> cancelTransaction(long timestamp, String transactionId);
}

public interface Buttercoin extends UnauthenticatedAPI, AccountAPI, OrderAPI, TransactionAPI {
    public static Builder newBuilder() {
        return new Builder();
    }

    static final class Builder {
        private int throttleRequestFilterMaxConnections = 16;
        private int requestTimeout = 60 * 1000;
        private int idleTimeout = 60 * 1000;
        private boolean compressionEnforced = true;
        private ExecutorService executorService;
        private ObjectMapper objectMapper = new ObjectMapper();

        private String apiKey;
        private String apiSecret;

        private String baseUrl = "https://api.buttercoin.com";

        // specific to Async Http Client
        private boolean useJDKAsyncHttpProvider = false;
        private NettyAsyncHttpProviderConfig nettyAsyncHttpProviderConfig = null;

        public Builder() {
        }

        public Builder throttleRequestFilterMaxConnections(int throttleRequestFilterMaxConnections) {
            this.throttleRequestFilterMaxConnections = throttleRequestFilterMaxConnections;
            return this;
        }

        public Builder requestTimeout(Duration duration) {
            this.requestTimeout = (int) duration.toMillis();
            return this;
        }

        public Builder requestTimeout(long timeout, TimeUnit unit) {
            this.requestTimeout = (int) unit.toMillis(timeout);
            return this;
        }

        public Builder idleTimeout(Duration duration) {
            this.idleTimeout = (int) duration.toMillis();
            return this;
        }

        public Builder idleTimeout(long timeout, TimeUnit unit) {
            this.idleTimeout = (int) unit.toMillis(timeout);
            return this;
        }

        public Builder compressionEnforced(boolean compressionEnforced) {
            this.compressionEnforced = compressionEnforced;
            return this;
        }

        public Builder executorService(ExecutorService executorService) {
            Preconditions.checkNotNull(executorService);

            this.executorService = executorService;
            return this;
        }

        public Builder objectMapper(ObjectMapper objectMapper) {
            Preconditions.checkNotNull(objectMapper);

            this.objectMapper = objectMapper;
            return this;
        }

        public Builder apiKey(String apiKey) {
            Preconditions.checkNotNull(apiKey);
            Preconditions.checkArgument(apiKey.length() == 32, "Your Buttercoin API key seems invalid, length should be 32");

            this.apiKey = apiKey;
            return this;
        }

        public Builder apiSecret(String apiSecret) {
            Preconditions.checkNotNull(apiSecret);
            Preconditions.checkArgument(apiSecret.length() == 32, "Your Buttercoin API secret seems invalid, length should be 32");

            this.apiSecret = apiSecret;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            Preconditions.checkNotNull(baseUrl);

            if (baseUrl.endsWith("/")) throw new IllegalArgumentException("The baseUrl should not end with /");

            this.baseUrl = baseUrl;
            return this;
        }

        public Builder useSandbox() {
            this.baseUrl = "https://sandbox.buttercoin.com";
            return this;
        }

        public Builder useJDKAsyncHttpProvider(boolean useJDKAsyncHttpProvider) {
            this.useJDKAsyncHttpProvider = useJDKAsyncHttpProvider;
            return this;
        }

        public Builder nettyAsyncHttpProviderConfig(NettyAsyncHttpProviderConfig nettyAsyncHttpProviderConfig) {
            Preconditions.checkNotNull(nettyAsyncHttpProviderConfig);

            this.nettyAsyncHttpProviderConfig = nettyAsyncHttpProviderConfig;
            return this;
        }

        public Buttercoin build() {
            return new DefaultButtercoin(this);
        }
    }

    static final class DefaultButtercoin implements Buttercoin {
        private final AsyncHttpClient httpClient;
        private final ObjectMapper objectMapper;
        private final String baseUrl;
        private final String apiKey;
        private final String apiSecret;

        private final Supplier<SignatureCalculator> signatureCalculatorSupplier = new Supplier<SignatureCalculator>(){
            public SignatureCalculator get(){
                return new HMAC256SignatureCalculator(
                        !Strings.isNullOrEmpty(apiKey) ? apiKey : System.getenv("BUTTERCOIN_API_KEY"),
                        !Strings.isNullOrEmpty(apiSecret) ? apiSecret : System.getenv("BUTTERCOIN_API_SECRET"));
            }
        };

        private volatile Supplier<SignatureCalculator> memorizedSignatureCalculator = Suppliers.memoize(signatureCalculatorSupplier);

        private DefaultButtercoin(Builder builder) {
            AsyncHttpClientConfig.Builder clientConfigBuilder = new AsyncHttpClientConfig.Builder();
            clientConfigBuilder.addRequestFilter(new ThrottleRequestFilter(builder.throttleRequestFilterMaxConnections));
            clientConfigBuilder.setRequestTimeout(builder.requestTimeout);
            clientConfigBuilder.setReadTimeout(builder.idleTimeout);
            clientConfigBuilder.setCompressionEnforced(builder.compressionEnforced);
            if (builder.executorService != null) clientConfigBuilder.setExecutorService(builder.executorService);
            clientConfigBuilder.setUserAgent("buttercoin-java/" + Buttercoin.class.getPackage().getImplementationVersion());

            if (builder.useJDKAsyncHttpProvider) {
                httpClient = new AsyncHttpClient(new JDKAsyncHttpProvider(clientConfigBuilder.build()));
            } else {
                if (builder.nettyAsyncHttpProviderConfig != null)
                    clientConfigBuilder.setAsyncHttpClientProviderConfig(builder.nettyAsyncHttpProviderConfig);
                httpClient = new AsyncHttpClient(clientConfigBuilder.build());
            }

            objectMapper = builder.objectMapper;
            baseUrl = builder.baseUrl;
            apiKey = builder.apiKey;
            apiSecret = builder.apiSecret;
        }

        private SignatureCalculator signatureCalculator() {
            return memorizedSignatureCalculator.get();
        }

        @Override
        public ListenableFuture<Ticker> getTicker() {
            return transform(new ListenableFutureAdapter<>(httpClient.prepareGet(baseUrl + "/v1/ticker")
                    .execute(new ThrowOnHttpFailure())), new MapResponseJson<>(objectMapper.reader(Ticker.class)));
        }

        @Override
        public ListenableFuture<OrderBook> getOrderBook() {
            return transform(new ListenableFutureAdapter<>(httpClient.prepareGet(baseUrl + "/v1/orderbook")
                    .execute(new ThrowOnHttpFailure())), new MapResponseJson<>(objectMapper.reader(OrderBook.class)));
        }

        @Override
        public ListenableFuture<TradeHistory> getTradeHistory() {
            return transform(new ListenableFutureAdapter<>(httpClient.prepareGet(baseUrl + "/v1/trades")
                    .execute(new ThrowOnHttpFailure())), new MapResponseJson<>(objectMapper.reader(TradeHistory.class)));
        }

        @Override
        public ListenableFuture<Permissions> getKey() {
            return getKey(System.currentTimeMillis());
        }

        @Override
        public ListenableFuture<Permissions> getKey(long timestamp) {
            return transform(new ListenableFutureAdapter<>(httpClient.prepareGet(baseUrl + "/v1/key")
                    .addHeader("X-Buttercoin-Date", "" + timestamp)
                    .setSignatureCalculator(signatureCalculator())
                    .execute(new ThrowOnHttpFailure())), new MapResponseJson<>(objectMapper.reader(Permissions.class)));
        }

        @Override
        public ListenableFuture<AccountBalances> getBalances() {
            return getBalances(System.currentTimeMillis());
        }

        @Override
        public ListenableFuture<AccountBalances> getBalances(long timestamp) {
            return transform(new ListenableFutureAdapter<>(httpClient.prepareGet(baseUrl + "/v1/account/balances")
                    .addHeader("X-Buttercoin-Date", "" + timestamp)
                    .setSignatureCalculator(signatureCalculator())
                    .execute(new ThrowOnHttpFailure())), new MapResponseJson<>(objectMapper.reader(AccountBalances.class)));
        }

        @Override
        public ListenableFuture<DepositAddress> getDepositAddress() {
            return getDepositAddress(System.currentTimeMillis());
        }

        @Override
        public ListenableFuture<DepositAddress> getDepositAddress(long timestamp) {
            return transform(new ListenableFutureAdapter<>(httpClient.prepareGet(baseUrl + "/v1/account/depositAddress")
                    .addHeader("X-Buttercoin-Date", "" + timestamp)
                    .setSignatureCalculator(signatureCalculator())
                    .execute(new ThrowOnHttpFailure())), new MapResponseJson<>(objectMapper.reader(DepositAddress.class)));
        }

        @Override
        public ListenableFuture<Order> getOrder(String orderId) {
            return getOrder(System.currentTimeMillis(), orderId);
        }

        @Override
        public ListenableFuture<Order> getOrder(long timestamp, String orderId) {
            Preconditions.checkNotNull(orderId, "orderId is null");
            return transform(new ListenableFutureAdapter<>(httpClient.prepareGet(baseUrl + "/v1/orders/" + orderId)
                    .addHeader("X-Buttercoin-Date", "" + timestamp)
                    .setSignatureCalculator(signatureCalculator())
                    .execute(new ThrowOnHttpFailure())), new MapResponseJson<>(objectMapper.reader(Order.class)));
        }

        @Override
        public ListenableFuture<Order> getOrder(URL url) {
            return getOrder(System.currentTimeMillis(), url);
        }

        @Override
        public ListenableFuture<Order> getOrder(long timestamp, URL url) {
            Preconditions.checkNotNull(url, "order url is null");
            return transform(new ListenableFutureAdapter<>(httpClient.prepareGet(url.toString())
                    .addHeader("X-Buttercoin-Date", "" + timestamp)
                    .setSignatureCalculator(signatureCalculator())
                    .execute(new ThrowOnHttpFailure())), new MapResponseJson<>(objectMapper.reader(Order.class)));
        }

        @Override
        public ListenableFuture<Orders> getOrders(URL url) {
            return getOrders(System.currentTimeMillis(), url);
        }

        @Override
        public ListenableFuture<Orders> getOrders(long timestamp, URL url) {
            Preconditions.checkNotNull(url, "orders url is null");
            return transform(new ListenableFutureAdapter<>(httpClient.prepareGet(url.toString())
                    .addHeader("X-Buttercoin-Date", "" + timestamp)
                    .setSignatureCalculator(signatureCalculator())
                    .execute(new ThrowOnHttpFailure())), new MapResponseJson<>(objectMapper.reader(Orders.class)));
        }

        @Override
        public ListenableFuture<Orders> getOrders(Order.Status... status) {
            return getOrders(System.currentTimeMillis(), status);
        }

        @Override
        public ListenableFuture<Orders> getOrders(long timestamp, Order.Status... status) {
            return getOrders(timestamp, Arrays.asList(status), null, null);
        }

        @Override
        public ListenableFuture<Orders> getOrders(Order.Side... side) {
            return getOrders(System.currentTimeMillis(), side);
        }

        @Override
        public ListenableFuture<Orders> getOrders(long timestamp, Order.Side... side) {
            return getOrders(timestamp, null, Arrays.asList(side), null);
        }

        @Override
        public ListenableFuture<Orders> getOrders(Order.OrderType... orderType) {
            return getOrders(System.currentTimeMillis(), orderType);
        }

        @Override
        public ListenableFuture<Orders> getOrders(long timestamp, Order.OrderType... orderType) {
            return getOrders(timestamp, null, null, Arrays.asList(orderType));
        }

        @Override
        public ListenableFuture<Orders> getOrders(Order.Status status, Order.Side side, Order.OrderType orderType) {
            return getOrders(System.currentTimeMillis(), status, side, orderType);
        }

        @Override
        public ListenableFuture<Orders> getOrders(long timestamp, Order.Status status, Order.Side side, Order.OrderType orderType) {
            return getOrders(timestamp, Lists.newArrayList(status), Lists.newArrayList(side), Lists.newArrayList(orderType));
        }

        @Override
        public ListenableFuture<Orders> getOrders(Iterable<Order.Status> status, Iterable<Order.Side> sides, Iterable<Order.OrderType> orderTypes) {
            return getOrders(System.currentTimeMillis(), status, sides, orderTypes);
        }

        @Override
        public ListenableFuture<Orders> getOrders(long timestamp, Iterable<Order.Status> status, Iterable<Order.Side> sides, Iterable<Order.OrderType> orderTypes) {
            return getOrders(timestamp, status, sides, orderTypes, 0, 0);
        }

        @Override
        public ListenableFuture<Orders> getOrders(Iterable<Order.Status> status, Iterable<Order.Side> sides, Iterable<Order.OrderType> orderTypes, int page, int pageSize) {
            return getOrders(System.currentTimeMillis(), status, sides, orderTypes, page, pageSize);
        }

        @Override
        public ListenableFuture<Orders> getOrders(long timestamp, Iterable<Order.Status> status, Iterable<Order.Side> sides, Iterable<Order.OrderType> orderTypes, int page, int pageSize) {
            AsyncHttpClient.BoundRequestBuilder builder = httpClient.prepareGet(baseUrl + "/v1/orders")
                    .addHeader("X-Buttercoin-Date", "" + timestamp);

            if (status != null && status.iterator().hasNext()) builder.addQueryParam("status", mkString(status, ","));
            if (sides != null && sides.iterator().hasNext()) builder.addQueryParam("side", mkString(sides, ","));
            if (orderTypes != null && orderTypes.iterator().hasNext())
                builder.addQueryParam("orderType", mkString(orderTypes, ","));

            if (page > 0) builder.addQueryParam("page", "" + page);
            if (pageSize > 0) builder.addQueryParam("pageSize", "" + pageSize);

            return transform(new ListenableFutureAdapter<>(builder.setSignatureCalculator(signatureCalculator()).execute(new ThrowOnHttpFailure())), new MapResponseJson<>(objectMapper.reader(Orders.class)));
        }

        @Override
        public ListenableFuture<Orders> getOrders(String... orderIds) {
            return getOrders(System.currentTimeMillis(), orderIds);
        }

        @Override
        public ListenableFuture<Orders> getOrders(long timestamp, String... orderIds) {
            AsyncHttpClient.BoundRequestBuilder builder = httpClient.prepareGet(baseUrl + "/v1/orders")
                    .addHeader("X-Buttercoin-Date", "" + timestamp);
            builder.addQueryParam("id", mkString(Arrays.asList(orderIds), ","));
            return transform(new ListenableFutureAdapter<>(builder.setSignatureCalculator(signatureCalculator()).execute(new ThrowOnHttpFailure())), new MapResponseJson<>(objectMapper.reader(Orders.class)));
        }

        @Override
        public ListenableFuture<URL> createOrder(CreateOrder.Instrument instrument, Order.Side side, Order.OrderType orderType, BigDecimal price, BigDecimal quantity) {
            return createOrder(System.currentTimeMillis(), instrument, side, orderType, price, quantity);
        }

        @Override
        public ListenableFuture<URL> createOrder(long timestamp, CreateOrder.Instrument instrument, Order.Side side, Order.OrderType orderType, BigDecimal price, BigDecimal quantity) {
            AsyncHttpClient.BoundRequestBuilder builder = httpClient.preparePost(baseUrl + "/v1/orders");
            builder.addHeader("Content-Type", "application/json");
            builder.addHeader("X-Buttercoin-Date", "" + timestamp);
            try {
                builder.setBody(objectMapper.writeValueAsString(new CreateOrder(instrument, side, orderType,
                        price.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString(),
                        quantity.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString())));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return transform(new ListenableFutureAdapter<>(builder.setSignatureCalculator(signatureCalculator()).execute(new ThrowOnHttpFailure())), new Status202ExtractLocation());
        }

        @Override
        public ListenableFuture<Boolean> cancelOrder(String orderId) {
            return cancelOrder(System.currentTimeMillis(), orderId);
        }

        @Override
        public ListenableFuture<Boolean> cancelOrder(long timestamp, String orderId) {
            return transform(new ListenableFutureAdapter<>(httpClient.prepareDelete(baseUrl + "/v1/orders/" + orderId)
                    .addHeader("X-Buttercoin-Date", "" + timestamp)
                    .setSignatureCalculator(signatureCalculator())
                    .execute(new ThrowOnHttpFailure())), new Status204SuccessFailure());
        }

        @Override
        public ListenableFuture<Transaction> getTransaction(String transactionId) {
            return getTransaction(System.currentTimeMillis(), transactionId);
        }

        @Override
        public ListenableFuture<Transaction> getTransaction(long timestamp, String transactionId) {
            Preconditions.checkNotNull(transactionId, "transactionId is null");
            return transform(new ListenableFutureAdapter<>(httpClient.prepareGet(baseUrl + "/v1/transaction/" + transactionId)
                    .addHeader("X-Buttercoin-Date", "" + timestamp)
                    .setSignatureCalculator(signatureCalculator())
                    .execute(new ThrowOnHttpFailure())), new MapResponseJson<>(objectMapper.reader(Transaction.class)));
        }

        @Override
        public ListenableFuture<Transaction> getTransaction(URL url) {
            return getTransaction(System.currentTimeMillis(), url);
        }

        @Override
        public ListenableFuture<Transaction> getTransaction(long timestamp, URL url) {
            Preconditions.checkNotNull(url, "transaction url is null");
            return transform(new ListenableFutureAdapter<>(httpClient.prepareGet(url.toString())
                    .addHeader("X-Buttercoin-Date", "" + timestamp)
                    .setSignatureCalculator(signatureCalculator())
                    .execute(new ThrowOnHttpFailure())), new MapResponseJson<>(objectMapper.reader(Transaction.class)));
        }

        @Override
        public ListenableFuture<Transactions> getTransactions(URL url) {
            return getTransactions(System.currentTimeMillis(), url);
        }

        @Override
        public ListenableFuture<Transactions> getTransactions(long timestamp, URL url) {
            Preconditions.checkNotNull(url, "transactions url is null");
            return transform(new ListenableFutureAdapter<>(httpClient.prepareGet(url.toString())
                    .addHeader("X-Buttercoin-Date", "" + timestamp)
                    .setSignatureCalculator(signatureCalculator())
                    .execute(new ThrowOnHttpFailure())), new MapResponseJson<>(objectMapper.reader(Transactions.class)));
        }

        @Override
        public ListenableFuture<Transactions> getTransactions(Transaction.Status... status) {
            return getTransactions(System.currentTimeMillis(), status);
        }

        @Override
        public ListenableFuture<Transactions> getTransactions(long timestamp, Transaction.Status... status) {
            return getTransactions(timestamp, Arrays.asList(status), null);
        }

        @Override
        public ListenableFuture<Transactions> getTransactions(Transaction.TransactionType... transactionType) {
            return getTransactions(System.currentTimeMillis(), transactionType);
        }

        @Override
        public ListenableFuture<Transactions> getTransactions(long timestamp, Transaction.TransactionType... transactionType) {
            return getTransactions(timestamp, null, Arrays.asList(transactionType));
        }

        @Override
        public ListenableFuture<Transactions> getTransactions(Transaction.Status status, Transaction.TransactionType transactionType) {
            return getTransactions(System.currentTimeMillis(), status, transactionType);
        }

        @Override
        public ListenableFuture<Transactions> getTransactions(long timestamp, Transaction.Status status, Transaction.TransactionType transactionType) {
            return getTransactions(timestamp, Lists.newArrayList(status), Lists.newArrayList(transactionType));
        }

        @Override
        public ListenableFuture<Transactions> getTransactions(Iterable<Transaction.Status> status, Iterable<Transaction.TransactionType> transactionTypes) {
            return getTransactions(System.currentTimeMillis(), status, transactionTypes);
        }

        @Override
        public ListenableFuture<Transactions> getTransactions(long timestamp, Iterable<Transaction.Status> status, Iterable<Transaction.TransactionType> transactionTypes) {
            return getTransactions(timestamp, status, transactionTypes, 0, 0);
        }

        @Override
        public ListenableFuture<Transactions> getTransactions(Iterable<Transaction.Status> status, Iterable<Transaction.TransactionType> transactionTypes, int page, int pageSize) {
            return getTransactions(System.currentTimeMillis(), status, transactionTypes, page, pageSize);
        }

        @Override
        public ListenableFuture<Transactions> getTransactions(long timestamp, Iterable<Transaction.Status> status, Iterable<Transaction.TransactionType> transactionTypes, int page, int pageSize) {
            AsyncHttpClient.BoundRequestBuilder builder = httpClient.prepareGet(baseUrl + "/v1/transactions")
                    .addHeader("X-Buttercoin-Date", "" + timestamp);

            if (status != null && status.iterator().hasNext()) builder.addQueryParam("status", mkString(status, ","));
            if (transactionTypes != null && transactionTypes.iterator().hasNext())
                builder.addQueryParam("transactionType", mkString(transactionTypes, ","));

            if (page > 0) builder.addQueryParam("page", "" + page);
            if (pageSize > 0) builder.addQueryParam("pageSize", "" + pageSize);

            return transform(new ListenableFutureAdapter<>(builder.setSignatureCalculator(signatureCalculator()).execute(new ThrowOnHttpFailure())), new MapResponseJson<>(objectMapper.reader(Transactions.class)));
        }

        @Override
        public ListenableFuture<URL> createDeposit(String method, String currency, BigDecimal amount) {
            return createDeposit(System.currentTimeMillis(), method, currency, amount);
        }

        @Override
        public ListenableFuture<URL> createDeposit(long timestamp, String method, String currency, BigDecimal amount) {
            AsyncHttpClient.BoundRequestBuilder builder = httpClient.preparePost(baseUrl + "/v1/transactions/deposit");
            builder.addHeader("Content-Type", "application/json");
            builder.addHeader("X-Buttercoin-Date", "" + timestamp);
            try {
                builder.setBody(objectMapper.writeValueAsString(new CreateDeposit(method, currency, amount)));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return transform(new ListenableFutureAdapter<>(builder.setSignatureCalculator(signatureCalculator()).execute(new ThrowOnHttpFailure())), new Status202ExtractLocation());
        }

        @Override
        public ListenableFuture<URL> createWithdrawal(String method, String currency, BigDecimal amount) {
            return createWithdrawal(System.currentTimeMillis(), method, currency, amount);
        }

        @Override
        public ListenableFuture<URL> createWithdrawal(long timestamp, String method, String currency, BigDecimal amount) {
            AsyncHttpClient.BoundRequestBuilder builder = httpClient.preparePost(baseUrl + "/v1/transactions/withdraw");
            builder.addHeader("Content-Type", "application/json");
            builder.addHeader("X-Buttercoin-Date", "" + timestamp);
            try {
                builder.setBody(objectMapper.writeValueAsString(new CreateWithdrawal(method, currency, amount)));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return transform(new ListenableFutureAdapter<>(builder.setSignatureCalculator(signatureCalculator()).execute(new ThrowOnHttpFailure())), new Status202ExtractLocation());
        }

        @Override
        public ListenableFuture<URL> sendBitcoin(BigDecimal amount, String destination) {
            return sendBitcoin(System.currentTimeMillis(), amount, destination);
        }

        @Override
        public ListenableFuture<URL> sendBitcoin(long timestamp, BigDecimal amount, String destination) {
            return sendBitcoin(timestamp, "BTC", amount, destination);
        }

        @Override
        public ListenableFuture<URL> sendBitcoin(String currency, BigDecimal amount, String destination) {
            return sendBitcoin(System.currentTimeMillis(), currency, amount, destination);
        }

        @Override
        public ListenableFuture<URL> sendBitcoin(long timestamp, String currency, BigDecimal amount, String destination) {
            AsyncHttpClient.BoundRequestBuilder builder = httpClient.preparePost(baseUrl + "/v1/transactions/send");
            builder.addHeader("Content-Type", "application/json");
            builder.addHeader("X-Buttercoin-Date", "" + timestamp);
            try {
                builder.setBody(objectMapper.writeValueAsString(new SendBitcoin(currency, amount, destination)));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return transform(new ListenableFutureAdapter<>(builder.setSignatureCalculator(signatureCalculator()).execute(new ThrowOnHttpFailure())), new Status202ExtractLocation());
        }

        @Override
        public ListenableFuture<Boolean> cancelTransaction(String transactionId) {
            return cancelTransaction(System.currentTimeMillis(), transactionId);
        }

        @Override
        public ListenableFuture<Boolean> cancelTransaction(long timestamp, String transactionId) {
            Preconditions.checkNotNull(transactionId, "transactionId is null");
            return transform(new ListenableFutureAdapter<>(httpClient.prepareDelete(baseUrl + "/v1/transactions/" + transactionId)
                    .addHeader("X-Buttercoin-Date", "" + timestamp)
                    .setSignatureCalculator(signatureCalculator())
                    .execute(new ThrowOnHttpFailure())), new Status204SuccessFailure());
        }

        private static String mkString(Iterable<?> values, String sep) {
            if (values == null || !values.iterator().hasNext())
                return "";

            List<String> nonEmptyVals = new LinkedList<>();
            for (Object val : values) {
                if (val != null && val.toString().trim().length() > 0) {
                    nonEmptyVals.add(val.toString());
                }
            }

            if (nonEmptyVals.size() == 0)
                return "";

            StringBuilder result = new StringBuilder();
            int i = 0;
            for (String val : nonEmptyVals) {
                if (i > 0)
                    result.append(sep);
                result.append(val);
                i++;
            }

            return result.toString();
        }
    }
}