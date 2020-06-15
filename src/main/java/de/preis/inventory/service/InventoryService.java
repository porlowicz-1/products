package de.preis.inventory.service;

import de.preis.inventory.data.Category;
import de.preis.inventory.data.Offer;
import de.preis.inventory.data.Product;
import de.preis.inventory.exception.ResourceNotFoundException;
import de.preis.inventory.repository.CategoryRepository;
import de.preis.inventory.repository.OfferRepository;
import de.preis.inventory.repository.ProductRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OfferRepository offerRepository;

    public List<Product> findProductsForCategory(int id) {

        Optional<Category> category = categoryRepository.findById(id);
        if (!category.isPresent()) {
            throw new ResourceNotFoundException("Category " + id);
        }

        return category.get().getProducts();
    }

    public List<Offer> findOffersForProduct(int id, String search, String sortBy, String sortDirection) {
        Sort sort = prepareSort(sortBy, sortDirection);
        String searchTerm = prepareSearchTerm(search);
        List<Offer> offers = offerRepository.findByTitleAndProductId(id, searchTerm, sort);
        if (offers.isEmpty()) {
            throw new ResourceNotFoundException("Offers for product " + id);
        }

        return offers;
    }


    public List<Product> findProductsForOffer(int id) {
        Optional<Offer> offer = offerRepository.findById(id);
        if (!offer.isPresent()) {
            throw new ResourceNotFoundException("Offer " + id);
        }

        return offer.get().getProducts();
    }

    private Sort prepareSort(String sortBy, String sortDirection) {
        Sort sort = Sort.by("id");
        if (Strings.isNotBlank(sortBy)) {
            sort = Sort.by(sortBy);
            if (sortDirection == null || sortDirection.toLowerCase().startsWith("asc")) {
                sort = sort.ascending();
            } else {
                sort = sort.descending();
            }
        }
        return sort;
    }

    private String prepareSearchTerm(String search) {
        if (Strings.isBlank(search)) {
            return "%";
        }
        return "%" + search + "%";
    }
}
