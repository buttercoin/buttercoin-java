package com.buttercoin.api.functions;

import com.buttercoin.api.HasPagination;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.base.Function;
import com.ning.http.client.Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MapResponseJson<T> implements Function<Response, T> {
    private final ObjectReader objectReader;

    public MapResponseJson(ObjectReader objectReader) {
        this.objectReader = objectReader;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T apply(Response response) {
        T value;
        try {
            value = objectReader.readValue(response.getResponseBodyAsBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (response.getHeaders().containsKey("X-Next-Page-Url")) {
            try {
                HasPagination hp = ((HasPagination) value);
                hp.setNextPage(new URL(response.getHeader("X-Next-Page-Url")));
                hp.setPageSize(Integer.valueOf(response.getHeader("X-Page-Size")));
            }
            catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        return value;
    }
}