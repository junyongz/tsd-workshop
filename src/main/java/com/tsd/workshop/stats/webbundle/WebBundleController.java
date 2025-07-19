package com.tsd.workshop.stats.webbundle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
@RequestMapping("/api/stats/webbundle")
public class WebBundleController {

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/main.js")
    public String mainJsPath() throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("public/asset-manifest.json");
        JsonNode asset = objectMapper.readValue(is, JsonNode.class);
        return asset.get("files").get("main.js").asText();
    }
}
