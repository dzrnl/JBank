package ru.dzrnl.apigateway.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@Component
public class ProxyUtil {
    private final RestTemplate restTemplate;

    @Value("${bank-app.base-url}")
    private String baseUrl;

    public ProxyUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HttpHeaders extractHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                if ("host".equalsIgnoreCase(headerName)) continue;
                if ("content-length".equalsIgnoreCase(headerName)) continue;
                if ("transfer-encoding".equalsIgnoreCase(headerName)) continue;
                List<String> headerValues = Collections.list(request.getHeaders(headerName));
                headers.put(headerName, headerValues);
            }
        }
        return headers;
    }

    public ResponseEntity<String> forwardRequest(HttpServletRequest request,
                                                 String path,
                                                 HttpMethod method,
                                                 String body) {
        String query = request.getQueryString();
        String url = baseUrl + path + (query != null ? "?" + query : "");
        HttpEntity<String> entity = new HttpEntity<>(body, extractHeaders(request));
        return restTemplate.exchange(url, method, entity, String.class);
    }
}
