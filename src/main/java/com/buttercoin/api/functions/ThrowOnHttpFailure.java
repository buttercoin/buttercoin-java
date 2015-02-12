package com.buttercoin.api.functions;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.Response;

public class ThrowOnHttpFailure extends AsyncCompletionHandler<Response> {
    @Override
    public Response onCompleted(Response response) throws Exception {
        if (response.getStatusCode() < 200 || response.getStatusCode() > 206)
            throw new RuntimeException("Unexpected Buttercoin API response: " + response.getStatusCode() + " " + response.getStatusText() + " " + response.getResponseBody("UTF-8"));

        return response;
    }
}