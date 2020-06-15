package de.preis.inventory.data;

public interface PersistableInventoryEntity<ID> {
    ID getId();

    void setId(ID id);
}
