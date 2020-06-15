package de.preis.inventory.testutil;

import de.preis.inventory.data.Offer;
import de.preis.inventory.data.Product;

public class EntityFactory {

    public static Offer offer(String title, double price, Product p1) {
        Offer o = new Offer();
        o.setTitle(title);
        o.setPrice(price);
        o.getProducts().add(p1);
        p1.getOffers().add(o);
        return o;
    }

    public static Product product(String title) {
        Product p = new Product();
        p.setTitle(title);
        return p;
    }
}
