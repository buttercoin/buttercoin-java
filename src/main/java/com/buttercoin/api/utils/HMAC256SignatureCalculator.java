package com.buttercoin.api.utils;

import com.google.common.base.Preconditions;
import com.google.common.net.UrlEscapers;
import com.ning.http.client.Param;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilderBase;
import com.ning.http.client.SignatureCalculator;
import com.ning.http.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public final class HMAC256SignatureCalculator implements SignatureCalculator {
    private static final String HMAC_ALGO = "HmacSHA256";
    private static final Mac SHARED_MAC;

    static {
        try {
            SHARED_MAC = Mac.getInstance(HMAC_ALGO);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private final String apiKey;
    private final SecretKeySpec signingKey;

    public HMAC256SignatureCalculator(String apiKey, String apiSecret) {
        Preconditions.checkNotNull(apiKey, "Authenticated requests require you set the API key or set the environment variable BUTTERCOIN_API_KEY");
        Preconditions.checkArgument(apiKey.length() == 32, "Buttercoin's API Key should be 32 characters long");
        Preconditions.checkNotNull(apiSecret, "Authenticated requests require you set the API secret or set the environment variable BUTTERCOIN_API_SECRET");
        Preconditions.checkArgument(apiSecret.length() == 32, "Buttercoin's API Secret should be 32 characters long");

        this.apiKey = apiKey;
        this.signingKey = new SecretKeySpec(apiSecret.getBytes(), HMAC_ALGO);
    }

    @Override
    public void calculateAndAddSignature(Request request, RequestBuilderBase<?> builder) {
        long now = Long.parseLong(request.getHeaders().getFirstValue("X-Buttercoin-Date"));

        StringBuilder signature = new StringBuilder();
        signature.append(now);
        signature.append(request.getUrl());
        if (request.getStringData() != null && request.getStringData().length() > 0)
            signature.append(request.getStringData());

        builder.addHeader("X-Buttercoin-Access-Key", apiKey);

        Mac mac;
        try {
            mac = (Mac) SHARED_MAC.clone();
            mac.init(signingKey);
        } catch (CloneNotSupportedException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        builder.addHeader("X-Buttercoin-Signature", Base64.encode(mac.doFinal(Base64.encode((signature.toString()).getBytes()).getBytes())));
    }
}
