package com.tsd.workshop.telematics.gps.gussmann;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GussMannFleetLoginModule {

    private final String username;

    private final transient String password;

    private final String loginUrl;

    public GussMannFleetLoginModule(@Value("${gussmann.login.username}") String username,
                                    @Value("${gussmann.login.password}") String password,
                                    @Value("${gussmann.login.url}") String loginUrl) {
        // go to login page
        // get cookie "ASP.NET_SessionId", "Pload always 1"
        // get __VIEWSTATE, __VIEWSTATEGENERATOR & __EVENTVALIDATION, and encode them
        // submit with these 5 elements
        // get cookie userinfo
        this.username = username;
        this.password = password;
        this.loginUrl = loginUrl;
    }

    // just need the userinfo for cookie usage
    public Mono<String> login() {
        return loginPage()
                .flatMap(this::submit)
                .map(HttpCookie::getValue);
    }

    // to get userinfo from cookie
    private Mono<LoginContext> loginPage() {
        return WebClient.create(this.loginUrl)
                .get()
                .exchangeToMono(res -> {
                    LoginContext ctx = new LoginContext();
                    ctx.cookies = res.cookies();

                    return res.bodyToMono(String.class)
                            .map(html -> {
                                Document doc = Jsoup.parse(html);
                                ctx.formValues = Stream.of("__VIEWSTATE", "__VIEWSTATEGENERATOR", "__EVENTVALIDATION")
                                        .collect(Collectors.toMap(
                                                key -> key,
                                                key -> doc.select("input[name=\"%s\"]".formatted(key)).attr("value")));
                                return ctx;
                            });
                });
    }

    private Mono<ResponseCookie> submit(LoginContext loginCtx) {
        return WebClient.create(this.loginUrl)
                    .post()
                    .cookies(loginCtx::toCookies)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(loginCtx.asFormData(this.username, this.password)))
                    .exchangeToMono(res -> Mono.just(res.cookies()))
                    .map(cookies -> cookies.getFirst("userinfo"));
    }
}
