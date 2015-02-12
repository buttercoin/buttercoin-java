package com.buttercoin.api.functions;

import com.google.common.base.Function;
import com.ning.http.client.Response;

public class Status204SuccessFailure implements Function<Response, Boolean> {
    @Override
    public Boolean apply(Response response) {
        return response.getStatusCode() == 204 ? Boolean.TRUE : Boolean.FALSE;
    }
}