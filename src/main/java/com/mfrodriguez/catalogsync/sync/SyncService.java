package com.mfrodriguez.catalogsync.sync;

import com.mfrodriguez.catalogsync.product.Product;
import com.mfrodriguez.catalogsync.product.ProductRepository;
import com.mfrodriguez.catalogsync.vtex.VtexClient;
import com.mfrodriguez.catalogsync.vtex.VtexProduct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SyncService {

    private final VtexClient vtexClient;
    private final ProductRepository productRepository;

    public SyncService(VtexClient vtexClient, ProductRepository productRepository) {
        this.vtexClient = vtexClient;
        this.productRepository = productRepository;
    }

    /**
     * Pulls products matching {@code query} from the given VTEX account's public catalog
     * and upserts them locally. Returns how many were synced.
     */
    public int syncFromVtex(String account, String query) {
        List<VtexProduct> vtexProducts = vtexClient.searchProducts(account, query);
        List<Product> products = vtexProducts.stream().map(Product::fromVtex).toList();
        productRepository.saveAll(products);
        return products.size();
    }
}
