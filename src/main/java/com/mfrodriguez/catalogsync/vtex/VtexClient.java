package com.mfrodriguez.catalogsync.vtex;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Talks to a VTEX store's public Catalog API (catalog_system/pub) — no API key needed,
 * it's the same endpoint the store's own search page calls.
 */
@Component
public class VtexClient {

    private final RestClient restClient;

    public VtexClient(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public List<VtexProduct> searchProducts(String account, String query) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String url = "https://%s.vtexcommercestable.com.br/api/catalog_system/pub/products/search?ft=%s"
                .formatted(account, encodedQuery);

        VtexProduct[] response = restClient.get().uri(url).retrieve().body(VtexProduct[].class);
        return response == null ? List.of() : List.of(response);
    }
}
