package com.mfrodriguez.catalogsync.sync;

import com.mfrodriguez.catalogsync.product.Product;
import com.mfrodriguez.catalogsync.product.ProductRepository;
import com.mfrodriguez.catalogsync.vtex.VtexClient;
import com.mfrodriguez.catalogsync.vtex.VtexProduct;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class SyncServiceTest {

    @Test
    void syncFromVtexMapsVtexProductsAndSavesThem() {
        VtexClient vtexClient = mock(VtexClient.class);
        ProductRepository productRepository = mock(ProductRepository.class);
        when(vtexClient.searchProducts("mystore", "shoes")).thenReturn(List.of(
                new VtexProduct("1", "Red Shoes", "Nike", "45", "https://mystore.com/red-shoes")
        ));
        SyncService syncService = new SyncService(vtexClient, productRepository);

        int synced = syncService.syncFromVtex("mystore", "shoes");

        assertEquals(1, synced);
        verify(productRepository).saveAll(argThat((List<Product> products) -> {
            Product product = products.get(0);
            return products.size() == 1
                    && "1".equals(product.getId())
                    && "Red Shoes".equals(product.getName())
                    && "Nike".equals(product.getBrand());
        }));
    }

    @Test
    void syncFromVtexWithNoResultsSavesNothing() {
        VtexClient vtexClient = mock(VtexClient.class);
        ProductRepository productRepository = mock(ProductRepository.class);
        when(vtexClient.searchProducts("mystore", "nonexistent")).thenReturn(List.of());
        SyncService syncService = new SyncService(vtexClient, productRepository);

        int synced = syncService.syncFromVtex("mystore", "nonexistent");

        assertEquals(0, synced);
        verify(productRepository).saveAll(anyList());
    }
}
