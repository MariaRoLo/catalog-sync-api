package com.mfrodriguez.catalogsync;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CatalogSyncApplicationTests {

    @Test
    void contextLoads() {
        // fails if wiring (datasource, JPA, RestClient) is broken
    }
}
