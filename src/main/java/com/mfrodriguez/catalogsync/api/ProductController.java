package com.mfrodriguez.catalogsync.api;

import com.mfrodriguez.catalogsync.product.Product;
import com.mfrodriguez.catalogsync.product.ProductRepository;
import com.mfrodriguez.catalogsync.sync.SyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductRepository productRepository;
    private final SyncService syncService;

    public ProductController(ProductRepository productRepository, SyncService syncService) {
        this.productRepository = productRepository;
        this.syncService = syncService;
    }

    @GetMapping("/products")
    public List<Product> all() {
        return productRepository.findAll();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> byId(@PathVariable String id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/sync")
    public Map<String, Object> sync(@RequestParam String account, @RequestParam String query) {
        int synced = syncService.syncFromVtex(account, query);
        return Map.of("account", account, "query", query, "synced", synced);
    }
}
