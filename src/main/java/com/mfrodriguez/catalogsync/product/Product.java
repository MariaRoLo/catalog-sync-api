package com.mfrodriguez.catalogsync.product;

import com.mfrodriguez.catalogsync.vtex.VtexProduct;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
public class Product {

    @Id
    private String id;
    private String name;
    private String brand;
    private String categoryId;
    private String link;
    private Instant syncedAt;

    protected Product() {
        // JPA
    }

    public static Product fromVtex(VtexProduct vtexProduct) {
        Product product = new Product();
        product.id = vtexProduct.productId();
        product.name = vtexProduct.productName();
        product.brand = vtexProduct.brand();
        product.categoryId = vtexProduct.categoryId();
        product.link = vtexProduct.link();
        product.syncedAt = Instant.now();
        return product;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getLink() {
        return link;
    }

    public Instant getSyncedAt() {
        return syncedAt;
    }
}
