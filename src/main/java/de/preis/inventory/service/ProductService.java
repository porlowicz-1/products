package de.preis.inventory.service;

import de.preis.inventory.data.Category;
import de.preis.inventory.data.Offer;
import de.preis.inventory.data.Product;
import de.preis.inventory.exception.ResourceNotFoundException;
import de.preis.inventory.repository.CategoryRepository;
import de.preis.inventory.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.Normalizer;
import java.util.*;

@Service
public class ProductService extends CrudService<Product, Integer> {

    private static final String PDP_URL_BASE = "/produkte";

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferService offerService;

    @Transactional
    @Override
    public Product createResource(Product resource) {
        resolveCategory(resource);
        resolveOffers(resource);

        Product resourceWithId = super.createResource(resource);
        return updateResource(resourceWithId.getId(), resourceWithId);
    }

    @Transactional
    @Override
    public Product updateResource(Integer id, Product resource) {
        resolveCategory(resource);
        resolveOffers(resource);
        resolveBestPrice(resource);
        resolvePdpUrl(id, resource);

        return super.updateResource(id, resource);
    }

    @Transactional
    public void updateBestPrice(Offer offer) {
        List<Product> products = new ArrayList<>(offer.getProducts());
        products.forEach(p -> {
            resolveBestPrice(p);
            updateResource(p.getId(), p);
        });
    }

    private void resolveBestPrice(Product resource) {
        List<Offer> offers = resource.getOffers();
        if (offers.isEmpty()) {
            resource.setBestPrice(0D);
        } else {
            Offer min = offers.stream().min((o1, o2) -> (int) (o1.getPrice() - o2.getPrice())).get();
            resource.setBestPrice(min.getPrice());
        }
    }

    private void resolvePdpUrl(Integer id, Product resource) {
        String normalizedTitle = Normalizer.normalize(resource.getTitle().toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        String urlReadyTitle = normalizedTitle.replaceAll("\\s+", "-");
        String pdpUrl = PDP_URL_BASE + String.format("/%s/%s.html", urlReadyTitle, id);
        resource.setProductDetailsPageUrl(pdpUrl);
    }

    private void resolveOffers(Product resource) {
        List<Integer> offerIds = resource.getOfferIdsValue();
        if (!offerIds.isEmpty()) {
            resource.setOffers(offerRepository.findAllById(offerIds));
            resource.setOfferIds(Collections.emptyList());
        }
        Set<Offer> offers = new HashSet<>(resource.getOffers());
        offers.forEach(o -> {
            if (!o.getProducts().contains(resource)) {
                o.getProducts().add(resource);
            }
        });
        resource.setOffers(new ArrayList<>(offers));
    }

    private void resolveCategory(Product resource) {
        Integer categoryId = resource.getCategoryIdValue();
        if (categoryId != null) {
            Optional<Category> category = categoryRepository.findById(categoryId);
            if (!category.isPresent()) {
                throw new ResourceNotFoundException("Category " + categoryId);
            }
            resource.setCategory(category.get());
            resource.setCategoryId(null);
        }
        Category category = resource.getCategory();
        if (category != null && !category.getProducts().contains(resource)) {
            category.getProducts().add(resource);
        }
    }
}
