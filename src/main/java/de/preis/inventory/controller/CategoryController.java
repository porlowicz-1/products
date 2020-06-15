package de.preis.inventory.controller;

import de.preis.inventory.data.Category;
import de.preis.inventory.data.Product;
import de.preis.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController extends CrudController<Category, Integer> {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/{id}/products")
    public List<Product> getProductsForCategory(@PathVariable int id) {

        return inventoryService.findProductsForCategory(id);
    }
}
