package de.preis.inventory.service;

import de.preis.inventory.data.Offer;
import de.preis.inventory.data.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static de.preis.inventory.testutil.EntityFactory.offer;
import static de.preis.inventory.testutil.EntityFactory.product;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@SpringBootTest
class OfferServiceTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private OfferService offerService;

    @Test
    public void shouldUpdateOfferWhenOfferRemovedFromProduct() {

        Product p1 = productService.createResource(product("p1"));
        Product p2 = productService.createResource(product("p2"));

        Offer offer = offerService.createResource(offer("o", 100, p1));
        offer.getProducts().add(p2);

        offerService.updateResource(offer.getId(), offer);

        assertTrue(p2.getOffers().remove(offer));
        assertTrue(offer.getProducts().remove(p2));

        productService.updateResource(p2.getId(), p2);
        offerService.updateResource(offer.getId(), offer);

        assertEquals(0, p2.getOffers().size());

        assertEquals(1, offer.getProducts().size());
        assertEquals("p1", offer.getProducts().get(0).getTitle());

    }

    @Test
    public void shouldUpdateOfferWhenOfferDeleted() {

        Product p1 = productService.createResource(product("p1"));
        Product p2 = productService.createResource(product("p2"));

        Offer offer1 = offerService.createResource(offer("o1", 100, p1));

        Offer offer2 = offerService.createResource(offer("o2", 200, p1));
        offer2.getProducts().add(p2);

        offerService.updateResource(offer1.getId(), offer1);
        offerService.updateResource(offer2.getId(), offer2);

        assertEquals(100, p1.getBestPrice());
        assertEquals(200, p2.getBestPrice());

        offerService.deleteResource(offer1.getId());

        assertEquals(200, p1.getBestPrice());
        assertEquals(200, p2.getBestPrice());
    }
}