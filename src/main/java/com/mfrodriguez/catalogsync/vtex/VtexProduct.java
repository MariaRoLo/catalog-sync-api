package com.mfrodriguez.catalogsync.vtex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Subset of the fields VTEX's public products/search response returns per product.
 * The real payload has many more fields (items, sku, images...); unknown ones are ignored.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record VtexProduct(
        String productId,
        String productName,
        String brand,
        String categoryId,
        String link
) {
}
