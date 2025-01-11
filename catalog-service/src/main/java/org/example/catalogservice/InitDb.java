package org.example.catalogservice;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.catalogservice.jpa.CatalogEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct  //스프링빈이 올라온 후 (의존성 주입 후) 실행
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            CatalogEntity catalog1 = createCatalog("CATALOG-001", "Berlin", 100, 1500);
            em.persist(catalog1);

            CatalogEntity catalog2 = createCatalog("CATALOG-002", "Oslo", 50, 2300);
            em.persist(catalog2);

            CatalogEntity catalog3 = createCatalog("CATALOG-003", "Seoul", 200, 800);
            em.persist(catalog3);
        }

        private CatalogEntity createCatalog(String productId, String productName, Integer stock, Integer unitPrice) {

            CatalogEntity catalog = new CatalogEntity();
            catalog.setProductId(productId);
            catalog.setProductName(productName);
            catalog.setStock(stock);
            catalog.setUnitPrice(unitPrice);
            return catalog;
        }
    }
}