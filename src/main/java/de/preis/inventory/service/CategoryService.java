package de.preis.inventory.service;

import de.preis.inventory.data.Category;
import de.preis.inventory.data.Product;
import de.preis.inventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class CategoryService extends CrudService<Category, Integer> {

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    @Override
    public Category createResource(Category resource) {
        resolveProducts(resource);
        return super.createResource(resource);
    }

    @Transactional
    @Override
    public Category updateResource(Integer integer, Category resource) {
        resolveProducts(resource);
        return super.updateResource(integer, resource);
    }

    @Transactional
    @Override
    public void deleteResource(Integer id) {
        Category resource = getResource(id);
        List<Product> products = new ArrayList<>(resource.getProducts());
        super.deleteResource(id);
        products.forEach(p -> {
            p.setCategory(null);
            productRepository.save(p);
        });
    }

    private void resolveProducts(Category resource) {
        List<Integer> productIds = resource.getProductIdsValue();
        if (!productIds.isEmpty()) {
            resource.setProducts(productRepository.findAllById(productIds));
            resource.setProductIds(Collections.emptyList());
        }
        Set<Product> products = new HashSet<>(resource.getProducts());
        products.forEach(p -> {
            p.setCategory(resource);
        });
        resource.setProducts(new ArrayList<>(products));
    }

}
