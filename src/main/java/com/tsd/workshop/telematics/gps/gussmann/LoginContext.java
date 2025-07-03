package com.tsd.workshop.telematics.gps.gussmann;

import org.springframework.http.ResponseCookie;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

class LoginContext {
    MultiValueMap<String, ResponseCookie> cookies;

    Map<String, String> formValues;

    void toCookies(MultiValueMap<String, String> c) {
        c.add("ASP.NET_SessionId", cookies.getFirst("ASP.NET_SessionId").getValue());
        c.add("Pload", "1");
    }

    MultiValueMap<String, String> asFormData(String username, String password) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.setAll(formValues);
        formData.add("uname", username);
        formData.add("password", password);
        // special one, can be any value
        formData.add("ImageButton1", "Login");
        return formData;
    }
}
