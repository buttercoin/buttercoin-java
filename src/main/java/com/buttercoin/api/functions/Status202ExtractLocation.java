package com.buttercoin.api.functions;

import com.google.common.base.Function;
import com.ning.http.client.Response;

import java.net.MalformedURLException;
import java.net.URL;

public class Status202ExtractLocation implements Function<Response, URL> {
    @Override
    public URL apply(Response response) {
        if (response.getStatusCode() != 202)
            throw new RuntimeException("Not a 202 Created response: " + response.getStatusCode() + " " + response.getStatusText());

        try {
            return new URL(response.getHeader("Location"));
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
