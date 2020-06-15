package de.preis.inventory.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Product implements PersistableInventoryEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "best_price")
    private Double bestPrice;

    @Column(name = "pdp_url")
    private String productDetailsPageUrl;

    @JsonIgnore
    @ManyToMany(mappedBy = "products")
    private List<Offer> offers = new ArrayList<>();

    @Transient
    private List<Integer> offerIds = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    private Category category;

    @Transient
    private Integer categoryId;

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

    public Double getBestPrice() {
        return bestPrice;
    }

    public void setBestPrice(Double bestPrice) {
        this.bestPrice = bestPrice;
    }

    public String getProductDetailsPageUrl() {
        return productDetailsPageUrl;
    }

    public void setProductDetailsPageUrl(String productDetailsPageUrl) {
        this.productDetailsPageUrl = productDetailsPageUrl;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getCategoryId() {
        return category != null ? category.getId() : null;
    }

    public List<Integer> getOfferIds() {
        return offers.stream().map(Offer::getId).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<Integer> getOfferIdsValue() {
        return offerIds;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @JsonIgnore
    public Integer getCategoryIdValue() {
        return categoryId;
    }

    public void setOfferIds(List<Integer> offerIds) {
        this.offerIds = offerIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
