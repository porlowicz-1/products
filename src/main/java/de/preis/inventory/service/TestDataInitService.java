package de.preis.inventory.service;

import de.preis.inventory.data.Category;
import de.preis.inventory.data.Offer;
import de.preis.inventory.data.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@ConditionalOnProperty(name = "demonstration.data")
@Service
public class TestDataInitService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OfferService offerService;

    @Transactional
    @PostConstruct
    public void init() {

        Random random = new Random();
        int categoryCount = 5;
        int productCount = 30;
        int offerCount = 20;
        int maxProductsInOffer = 6;

        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < categoryCount; i++) {
            Category category = new Category();
            category.setTitle("Category " + i);
            Category resource = categoryService.createResource(category);
            categories.add(resource);
        }

        List<Product> products = new ArrayList<>();
        for (int i = 0; i < productCount; i++) {
            Product product = new Product();
            product.setTitle("Product " + i);
            product.setCategory(categories.get(random.nextInt(categoryCount)));
            Product resource = productService.createResource(product);
            products.add(resource);
        }

        List<Offer> offers = new ArrayList<>();
        for (int i = 0; i < offerCount; i++) {
            Offer offer = new Offer();
            offer.setTitle("Offer " + i);
            offer.setPrice((random.nextInt(500000) + 500) / 100.0);
            offer.setShopId(UUID.randomUUID().toString());
            offer.setDeliveryConstraintDays(random.nextInt(14) + 2);
            Offer resource = offerService.createResource(offer);
            offers.add(resource);
        }

        offers.forEach(o -> {
            int offeredProducts = random.nextInt(maxProductsInOffer);
            if (offeredProducts == 0) {
                return;
            }
            for (int i = 0; i < offeredProducts; i++) {
                Product product = products.get(random.nextInt(productCount));
                if (product.getOffers().contains(o)) {
                    continue;
                }
                product.getOffers().add(o);
                o.getProducts().add(product);
            }
            offerService.updateResource(o.getId(), o);
        });

        products.forEach(p -> productService.updateResource(p.getId(), p));
    }

}
