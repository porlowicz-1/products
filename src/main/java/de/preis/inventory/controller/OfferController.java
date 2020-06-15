package de.preis.inventory.controller;

import de.preis.inventory.data.Offer;
import de.preis.inventory.data.Product;
import de.preis.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/offers")
public class OfferController extends CrudController<Offer, Integer> {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/{id}/products")
    public List<Product> getProductsForOffer(@PathVariable int id) {
        return inventoryService.findProductsForOffer(id);
    }


}
