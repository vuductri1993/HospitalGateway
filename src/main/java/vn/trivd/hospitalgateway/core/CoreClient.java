package vn.trivd.hospitalgateway.core;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CoreClient {
    private final RestTemplate coreRestTemplate;
    private final String baseUrl;

    public CoreClient(
            @Qualifier("coreRestTemplate") RestTemplate coreRestTemplate,
            @Value("${app.core.base-url}") String baseUrl
    ) {
        this.coreRestTemplate = coreRestTemplate;
        this.baseUrl = baseUrl;
    }

    public ResponseEntity<String> get(String path, String bearerToken) {
        HttpHeaders headers = new HttpHeaders();
        if (bearerToken != null && !bearerToken.isBlank()) {
            headers.set(HttpHeaders.AUTHORIZATION, bearerToken.startsWith("Bearer ") ? bearerToken : ("Bearer " + bearerToken));
        }
        return coreRestTemplate.exchange(baseUrl + path, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }
}

