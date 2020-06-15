package de.preis.inventory.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Offer implements PersistableInventoryEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;

    private Double price;

    @Column(name = "delivery_constraint_days")
    private Integer deliveryConstraintDays;

    @Column(name = "shop_id")
    private String shopId;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "offer_product",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products = new ArrayList<>();

    @Transient
    private List<Integer> productIds = new ArrayList<>();

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Integer> getProductIds() {
        return products.stream().map(Product::getId).collect(Collectors.toList());
    }

    public void setDeliveryConstraintDays(Integer deliveryConstraintDays) {
        this.deliveryConstraintDays = deliveryConstraintDays;
    }

    public Integer getDeliveryConstraintDays() {
        return deliveryConstraintDays;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }

    @JsonIgnore
    public List<Integer> getProductIdsValue() {
        return productIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return Objects.equals(id, offer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
