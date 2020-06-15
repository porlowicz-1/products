package de.preis.inventory.service;

import de.preis.inventory.data.Offer;
import de.preis.inventory.data.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;

import static de.preis.inventory.testutil.EntityFactory.offer;
import static de.preis.inventory.testutil.EntityFactory.product;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private OfferService offerService;

    @Test
    public void shouldUpdateBestPriceWhenOffersChange() {

        Product p1 = productService.createResource(product("p1"));

        assertEquals(0, p1.getBestPrice());

        offerService.createResource(offer("o1", 100, p1));

        assertEquals(100, p1.getBestPrice());

        offerService.createResource(offer("o2", 200, p1));
        offerService.createResource(offer("o3", 50, p1));

        assertEquals(50, p1.getBestPrice());
    }

    @Test
    public void shouldUpdatePdpUrlWhenTitleChanges() {

        Product product = productService.createResource(product("first title"));
        int id = product.getId();

        assertEquals("/produkte/first-title/" + id + ".html", product.getProductDetailsPageUrl());

        product.setTitle("second title");
        productService.updateResource(product.getId(), product);

        assertEquals("/produkte/second-title/" + id + ".html", product.getProductDetailsPageUrl());
    }


    @Test
    public void shouldAssignUniqueOffersToProduct() {

        Product p1 = productService.createResource(product("p1"));

        Offer offer = offerService.createResource(offer("o1", 200, p1));
        p1.getOffers().add(offer);
        p1.getOffers().add(offer);
        p1.getOffers().add(offer);

        productService.updateResource(p1.getId(), p1);

        assertEquals(1, p1.getOffers().size());
    }


}