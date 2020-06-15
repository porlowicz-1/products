package de.preis.inventory.repository;

import de.preis.inventory.data.Offer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Integer> {

    @Query("SELECT distinct o FROM Offer o JOIN o.products p WHERE p.id = :id AND o.title LIKE :search")
    List<Offer> findByTitleAndProductId(int id, String search, Sort sort);

}
