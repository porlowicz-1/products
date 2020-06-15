package de.preis.inventory.service;

import de.preis.inventory.data.Offer;
import de.preis.inventory.data.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static de.preis.inventory.testutil.EntityFactory.offer;
import static de.preis.inventory.testutil.EntityFactory.product;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@SpringBootTest
class InventoryServiceTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OfferService offerService;

    private Product p1;
    private Product p2;

    @BeforeEach
    public void setUp() {
        p1 = productService.createResource(product("p1"));
        p2 = productService.createResource(product("p2"));

        Offer o1 = offerService.createResource(offer("o1", 100, p1));
        Offer o2 = offerService.createResource(offer("o2", 200, p1));
        Offer o3 = offerService.createResource(offer("o3", 150, p1));

        Offer o4 = offerService.createResource(offer("o4", 500, p2));

    }

    @Test
    public void shouldFindOffersByProductId() {

        List<Offer> offersForProduct = inventoryService.findOffersForProduct(p1.getId(), null, null, null);

        assertEquals(3, offersForProduct.size());
        assertEquals("o1", offersForProduct.get(0).getTitle());
        assertEquals("o2", offersForProduct.get(1).getTitle());
        assertEquals("o3", offersForProduct.get(2).getTitle());
    }

    @Test
    public void shouldFindOffersByProductIdAndSearchTerm() {

        List<Offer> offersForProduct = inventoryService.findOffersForProduct(p1.getId(), "o2", null, null);

        assertEquals(1, offersForProduct.size());
        assertEquals("o2", offersForProduct.get(0).getTitle());
    }

    @Test
    public void shouldSortByPriceAsc() {

        testSort(p1.getId(), "price", "asc", Arrays.asList("o1", "o3", "o2"));
    }

    @Test
    public void shouldSortByPriceDesc() {

        testSort(p1.getId(), "price", "desc", Arrays.asList("o2", "o3", "o1"));
    }

    @Test
    public void shouldSortByPriceDefaultDirection() {

        testSort(p1.getId(), "price", null, Arrays.asList("o1", "o3", "o2"));
    }

    @Test
    public void shouldDefaultSort() {

        testSort(p1.getId(), null, null, Arrays.asList("o1", "o2", "o3"));
    }

    private void testSort(Integer productId, String proprety, String direction, List<String> expected) {

        List<Offer> offersForProduct = inventoryService.findOffersForProduct(productId, null, proprety, direction);

        assertEquals(expected.size(), offersForProduct.size());
        int i = 0;
        for (String expectedTitle : expected) {
            assertEquals(expectedTitle, offersForProduct.get(i).getTitle());
            i++;
        }
    }

}