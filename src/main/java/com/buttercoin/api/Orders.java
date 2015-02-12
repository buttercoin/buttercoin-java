package com.buttercoin.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class Orders implements Iterable<Order>, HasPagination {
    @JsonProperty("results")
    private List<Order> orders;

    @JsonIgnore
    private URL nextPage;

    @JsonIgnore
    private int pageSize;

    @Override
    public Iterator<Order> iterator() {
        return orders != null ? orders.iterator() : ImmutableSet.<Order>of().iterator();
    }

    public List<Order> getOrders() {
        return orders;
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
                .add("orders", orders)
                .add("nextPage", nextPage)
                .add("pageSize", pageSize)
                .toString();
    }
}
