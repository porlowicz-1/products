package de.preis.inventory.service;

import de.preis.inventory.data.PersistableInventoryEntity;
import de.preis.inventory.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public abstract class CrudService<T extends PersistableInventoryEntity<ID>, ID> {

    @Autowired
    private JpaRepository<T, ID> repository;

    public List<T> getResources() {
        return repository.findAll();
    }

    public T getResource(ID id) {
        Optional<T> resource = repository.findById(id);
        if (!resource.isPresent()) {
            throw new ResourceNotFoundException("Resource with id " + id);
        }
        return resource.get();
    }

    @Transactional
    public T createResource(T resource) {
        resource.setId(null);
        return repository.save(resource);
    }

    @Transactional
    public T updateResource(ID id, T resource) {
        resource.setId(id);
        return repository.save(resource);
    }

    @Transactional
    public void deleteResource(ID id) {
        repository.deleteById(id);
    }

}
