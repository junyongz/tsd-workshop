package com.tsd.workshop;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.util.Base64;

public class UrlSigner {

    private final SecretKeySpec secretKeySpec;

    private final String algorithm;

    public UrlSigner(String base64SecretKey, String algorithm) {
        base64SecretKey = base64SecretKey.replace('-', '+');
        base64SecretKey = base64SecretKey.replace('_', '/');
        byte[] secretKey = Base64.getDecoder().decode(base64SecretKey);

        // "HmacSHA1"
        this.secretKeySpec = new SecretKeySpec(secretKey, algorithm);
        this.algorithm = algorithm;
    }

    public String sign(String path, String query) throws GeneralSecurityException {
        return sign(path + "?" +query);
    }

    public String sign(String fullPath) throws GeneralSecurityException {
        Mac mac = Mac.getInstance(algorithm);
        mac.init(this.secretKeySpec);

        byte[] sigBytes = mac.doFinal(fullPath.getBytes());
        String signature = Base64.getEncoder().encodeToString(sigBytes);

        // convert the signature to 'web safe' base 64
        signature = signature.replace('+', '-');
        signature = signature.replace('/', '_');

        return fullPath + "&signature=" + signature;
    }
}
