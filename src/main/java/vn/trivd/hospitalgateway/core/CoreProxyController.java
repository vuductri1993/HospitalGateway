package vn.trivd.hospitalgateway.core;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/core-proxy")
public class CoreProxyController {
    private final CoreClient coreClient;

    public CoreProxyController(CoreClient coreClient) {
        this.coreClient = coreClient;
    }

    @GetMapping("/patients")
    public ResponseEntity<String> proxyPatients(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization) {
        return coreClient.get("/patients", authorization);
    }
}

