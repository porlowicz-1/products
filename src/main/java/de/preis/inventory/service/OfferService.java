package de.preis.inventory.service;

import de.preis.inventory.data.Offer;
import de.preis.inventory.data.Product;
import de.preis.inventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class OfferService extends CrudService<Offer, Integer> {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Transactional
    @Override
    public Offer createResource(Offer resource) {
        resolveProducts(resource);
        Offer offer = super.createResource(resource);
        productService.updateBestPrice(offer);
        return offer;
    }

    @Transactional
    @Override
    public Offer updateResource(Integer integer, Offer resource) {
        resolveProducts(resource);
        Offer offer = super.updateResource(integer, resource);
        productService.updateBestPrice(offer);
        return offer;
    }

    @Transactional
    @Override
    public void deleteResource(Integer id) {
        Offer offer = getResource(id);
        super.deleteResource(id);
        List<Product> products = offer.getProducts();
        products.forEach(p -> {
            p.getOffers().remove(offer);
            productService.updateResource(p.getId(), p);
        });
    }

    private void resolveProducts(Offer resource) {
        List<Integer> productIds = resource.getProductIdsValue();
        if (!productIds.isEmpty()) {
            resource.setProducts(productRepository.findAllById(productIds));
            resource.setProductIds(Collections.emptyList());
        }
        Set<Product> products = new HashSet<>(resource.getProducts());
        products.forEach(p -> {
            if (!p.getOffers().contains(resource)) {
                p.getOffers().add(resource);
            }
        });
        resource.setProducts(new ArrayList<>(products));

    }


}
