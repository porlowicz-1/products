package de.preis.inventory.controller;

import de.preis.inventory.data.Offer;
import de.preis.inventory.data.Product;
import de.preis.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController extends CrudController<Product, Integer> {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/{id}/offers")
    public List<Offer> getOffersForProduct(@PathVariable int id,
                                           @RequestParam(required = false, defaultValue = "") String search,
                                           @RequestParam(required = false) String sortBy,
                                           @RequestParam(required = false) String sortDirection) {
        return inventoryService.findOffersForProduct(id, search, sortBy, sortDirection);
    }
}
