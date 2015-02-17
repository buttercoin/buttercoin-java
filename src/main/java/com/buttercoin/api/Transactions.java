package com.buttercoin.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class Transactions implements Iterable<Transaction>, HasPagination {
    @JsonProperty("results")
    private List<Transaction> transactions;

    @JsonIgnore
    private URL nextPage;

    @JsonIgnore
    private int pageSize;

    @Override
    public Iterator<Transaction> iterator() {
        return transactions != null ? transactions.iterator() : ImmutableSet.<Transaction>of().iterator();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public URL getNextPage() {
        return nextPage;
    }

    @Override
    public void setNextPage(URL nextPage) {
        this.nextPage = nextPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("transactions", transactions)
                .add("nextPage", nextPage)
                .add("pageSize", pageSize)
                .toString();
    }
}
